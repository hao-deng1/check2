package com.lostAndFind.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lostAndFind.project.Utils.ThingFactory;
import com.lostAndFind.project.annotation.LogOperation;
import com.lostAndFind.project.common.BaseResponse;
import com.lostAndFind.project.common.ErrorCode;
import com.lostAndFind.project.common.ResponseResult;
import com.lostAndFind.project.common.ResultUtils;
import com.lostAndFind.project.config.RedisCache;
import com.lostAndFind.project.es.entity.EsLost;
import com.lostAndFind.project.es.entity.EsPick;
import com.lostAndFind.project.es.service.EsLostService;
import com.lostAndFind.project.exception.BusinessException;
import com.lostAndFind.project.mapper.LostMapper;
import com.lostAndFind.project.model.dto.LostDto;
import com.lostAndFind.project.model.entity.Lost;
import com.lostAndFind.project.model.entity.Pick;
import com.lostAndFind.project.model.entity.Role;
import com.lostAndFind.project.model.entity.Tag;
import com.lostAndFind.project.model.query.LostQuery;
import com.lostAndFind.project.model.query.PickQuery;
import com.lostAndFind.project.model.request.LostAddRequest;
import com.lostAndFind.project.model.request.LostUpdateRequest;
import com.lostAndFind.project.model.request.LostUpdateStateRequest;
import com.lostAndFind.project.model.vo.LostVo;
import com.lostAndFind.project.service.LostService;
import com.lostAndFind.project.service.TagRelService;
import com.lostAndFind.project.service.TagService;
import com.lostAndFind.project.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.lostAndFind.project.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author yc_
 * @version 1.0
 */

@RestController
@RequestMapping("/lost")
@CrossOrigin(origins = {"http://127.0.0.1:5173/"})
public class LostController {

    @Autowired
    LostService lostService;

    @Resource
    LostMapper lostMapper;
    @Autowired
    TagService tagService;
    @Autowired
    TagRelService tagRelService;

    @Resource
    private RedisCache redisCache;

    @Autowired
    UserService userService;

    @Autowired
    private EsLostService esLostService;

    /**
     * 新增丢失物品
     * @param lostAddRequest 丢失物品对象
     * @return
     */
    @PostMapping("/add")
    @LogOperation("新增")
    public BaseResponse<String> save(@RequestBody LostAddRequest lostAddRequest) {
        String url = lostAddRequest.getImg().replaceAll("%3A", ":").replaceAll("%2F", "/")
                .replaceAll("%3F", "?").replaceAll("%3D", "=").replaceAll(
                        "%26", "&");
        lostAddRequest.setImg(url);
        //新增丢失物品
        Lost lost = (Lost) ThingFactory.getThing("lost");
        BeanUtils.copyProperties(lostAddRequest,lost);
        lost.setUserId(userService.getLoginUser().getId());
        lostService.save(lost);

        List<String> lostType = lostAddRequest.getLostType();
        tagRelService.saveAll(lostType,"0",lost.getId());

        if (redisCache.getCacheObject("lostList") != null) {
            redisCache.deleteObject("lostList");
        }
        if (redisCache.getCacheObject("myLostList"+userService.getLoginUser().getId()) != null) {
            redisCache.deleteObject("myLostList"+userService.getLoginUser().getId());
        }
        if (redisCache.getCacheObject("UrgentOrder") != null) {
            redisCache.deleteObject("UrgentOrder");
        }

        //es
        EsLost esLost = new EsLost();
        BeanUtils.copyProperties(lost,esLost);
        esLost.setId(null);
        esLost.setIdd(lost.getId());
        esLost.setTag(lostType.get(0));
        esLostService.save(esLost);

        return ResultUtils.success("新增丢失物品成功");
    }

    /**
     * 删除丢失物品
     */
    @DeleteMapping("/deletes")
    @LogOperation("删除")
    public BaseResponse<String> deletes(@RequestParam List<Long> ids) {
        lostService.removeWithLost(ids);
        esLostService.esDelete(ids);
        return ResultUtils.success("删除成功");
    }

    /**
     * 修改丢失物品
     */
    @PostMapping("/update")
    @LogOperation("修改")
    public BaseResponse<Boolean> update(@RequestBody LostUpdateRequest lostUpdateRequest) {
        Lost lost = new Lost();
        BeanUtils.copyProperties(lostUpdateRequest,lost);
        lostService.updateById(lost);
        Lost byId = lostService.getById(lost.getId());
        BeanUtils.copyProperties(lostUpdateRequest,byId);
        esLostService.esUpdate(byId);
        return ResultUtils.success(true);
    }

    /**
     * 修改丢失物品
     * @param id
     * @return
     */
    @PostMapping("/updateState/{id}")
    @LogOperation("修改")
    public BaseResponse<Boolean> updateState(@PathVariable("id") Long id) {
        QueryWrapper<Lost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Lost lost = lostMapper.selectOne(queryWrapper);
        lost.setStatus("1");
        lostService.updateById(lost);
        if (redisCache.getCacheObject("myLostList"+userService.getLoginUser().getId()) != null) {
            redisCache.deleteObject("myLostList"+userService.getLoginUser().getId());
        }
        return ResultUtils.success(true);
    }



    /**
     * 根据id查询丢失物品
     */
    @GetMapping("/{id}")
    @LogOperation("查询")
    public BaseResponse<LostDto> get(@PathVariable("id") Long id){
        Lost lost = lostService.getById(id);
        LostDto lostDto = new LostDto();
        BeanUtils.copyProperties(lost,lostDto);
        lostDto.setUsername(userService.getById(lost.getUserId()).getUsername());
        return ResultUtils.success(lostDto);
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public BaseResponse<Page> page(int page, int pageSize, String string){
        //构造分页构造器
        Page<Lost> pageInfo = new Page<>(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Lost> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(string!=null,Lost::getLostName,string);
        wrapper.orderByDesc(Lost::getReportTime);

        Page<Lost> page1 = lostService.page(pageInfo, wrapper);
        return ResultUtils.success(page1);
    }


    @GetMapping("/list")
    public BaseResponse<List<Lost>> list() {
        List<Lost> list = lostService.list();
        return ResultUtils.success(list);
    }


    /**
     * 根据标签查询lost
     * @param tagName
     * @return
     */
    @GetMapping("/selectByTag")
    public BaseResponse<List<Lost>> selectByTag (String tagName) {
        Tag tag = tagService.getTagByName(tagName);
        List<Long> idList = new ArrayList<>();
        List<Long> newIdList = new ArrayList<>();
        if (tag.getParentId() == 0) {
            LambdaQueryWrapper<Tag> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(Tag::getParentId,tag.getParentId());
            List<Tag> list = tagService.list(wrapper1);
            for (Tag tag1 : list) {
                List<Long> thingId = tagRelService.getThingId(tag1.getId(), 0);
                idList.addAll(thingId);
            }
        } else {
            idList = tagRelService.getThingId(tag.getId(), 0);
        }

        //去重
        for (Long id : idList) {
            if(!newIdList.contains(id)){
                newIdList.add(id);
            }
        }

        List<Lost> lostList = new ArrayList<>();
        for (Long id : newIdList) {
            Lost lost = lostService.getById(id);
            lostList.add(lost);
        }

        return ResultUtils.success(lostList);
    }

    @GetMapping("/query")
    public ResponseResult query(LostQuery lostQuery) {
//        if (lostQuery.getSearchText() != null && lostQuery.getTagList() == null) {
//            return new ResponseResult<>(200,"查询成功",searchLostNameOrDescWithHits(lostQuery.getSearchText()));
//        }
        return lostService.queryAll(lostQuery);
    }

    @GetMapping("/getMy")
    public ResponseResult getMyOrder(){
        if (redisCache.getCacheObject("myLostList"+userService.getLoginUser().getId()) != null) {
            return new ResponseResult<>(200,"查询成功",redisCache.getCacheObject("myLostList"+userService.getLoginUser().getId()));
        }
        Long usId = userService.getLoginUser().getId();
        LostQuery query = new LostQuery();
        query.setUserId(usId);
        return lostService.queryAll(query);
    }

    @GetMapping("getUrgent")
    public ResponseResult getUrgent(){
        if (redisCache.getCacheObject("UrgentOrder") != null){
            return new ResponseResult<>(200,"查询成功",redisCache.getCacheObject("UrgentOrder"));
        }
        return lostService.getUrgent();
    }


    @GetMapping("/esSearch")
    public List<EsLost> esSearch(String key) {
        return esLostService.searchProduct(key);
    }
    @GetMapping("/searchLostNameWithHits")
    public SearchHits<EsLost> searchLostNameWithHits(String key) {
        return esLostService.searchLostNameWithHits(key);
    }
    @GetMapping("/searchLostNameOrTagOrDescWithHits")
    public SearchHits<EsLost> searchLostNameOrTagOrDescWithHits(String key) {
        return esLostService.searchLostNameOrTagOrDescWithHits(key);
    }
}