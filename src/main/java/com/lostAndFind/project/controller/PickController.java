package com.lostAndFind.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lostAndFind.project.Utils.ThingFactory;
import com.lostAndFind.project.annotation.LogOperation;
import com.lostAndFind.project.common.BaseResponse;
import com.lostAndFind.project.common.ResponseResult;
import com.lostAndFind.project.common.ResultUtils;
import com.lostAndFind.project.config.RedisCache;
import com.lostAndFind.project.es.entity.EsPick;
import com.lostAndFind.project.es.service.EsPickService;
import com.lostAndFind.project.mapper.PickMapper;
import com.lostAndFind.project.model.dto.PickDto;
import com.lostAndFind.project.model.entity.Lost;
import com.lostAndFind.project.model.entity.Pick;
import com.lostAndFind.project.model.entity.Tag;
import com.lostAndFind.project.model.entity.TagRel;
import com.lostAndFind.project.model.query.LostQuery;
import com.lostAndFind.project.model.query.PickQuery;
import com.lostAndFind.project.model.request.PickAddRequest;
import com.lostAndFind.project.model.request.PickUpdateRequest;
import com.lostAndFind.project.model.request.PickUpdateStateRequest;
import com.lostAndFind.project.service.PickService;
import com.lostAndFind.project.service.TagRelService;
import com.lostAndFind.project.service.TagService;
import com.lostAndFind.project.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yc_
 * @version 1.0
 */

@RestController
@RequestMapping("/pick")
@CrossOrigin(origins = {"http://127.0.0.1:5173/"})
public class PickController {

    @Autowired
    PickService pickService;

    @Resource
    PickMapper pickMapper;
    @Autowired
    TagService tagService;
    @Autowired
    TagRelService tagRelService;

    @Resource
    private RedisCache redisCache;

    @Autowired
    UserService userService;

    @Autowired
    private EsPickService esPickService;

    /**
     * 新增丢失物品
     * @param pick 拾取物品对象
     * @return
     */
    @PostMapping("/add")
    @LogOperation("新增")
    public BaseResponse<String> save(@RequestBody PickAddRequest pick) {
        String url = pick.getImg().replaceAll("%3A", ":").replaceAll("%2F", "/")
                .replaceAll("%3F", "?").replaceAll("%3D", "=").replaceAll(
                        "%26", "&");
        pick.setImg(url);
        Pick newPick = (Pick) ThingFactory.getThing("pick");
        BeanUtils.copyProperties(pick, newPick);
        newPick.setUserid(userService.getLoginUser().getId());
        pickService.save(newPick);

        List<String> pickType = pick.getPickType();
        tagRelService.saveAll(pickType,"1",newPick.getId());

        if ( redisCache.getCacheObject("pickList") != null) {
            redisCache.deleteObject("pickList");
        }
        if (redisCache.getCacheObject("myPickList"+userService.getLoginUser().getId()) != null) {
            redisCache.deleteObject("myPickList"+userService.getLoginUser().getId());
        }

        //es
        EsPick esProduct = new EsPick();
        BeanUtils.copyProperties(newPick,esProduct);
        esProduct.setId(null);
        esProduct.setIdd(newPick.getId());
        esProduct.setTag(pickType.get(0));
        esPickService.save(esProduct);
        return ResultUtils.success("新增拾取物品成功");
    }

    /**
     * 删除拾取物品
     */
    @DeleteMapping("/delete")
    @LogOperation("删除")
    public BaseResponse<String> delete(@RequestParam List<Long> ids) {
        pickService.removeWithPick(ids);
        esPickService.esDelete(ids);
        return ResultUtils.success("删除成功");
    }

    /**
     * 修改拾取物品
     */
    @PostMapping("/update")
    @LogOperation("修改")
    public BaseResponse<Boolean> update(@RequestBody PickUpdateRequest pickUpdateRequest) {
        Pick pick = new Pick();
        BeanUtils.copyProperties(pickUpdateRequest, pick);
        pickService.updateById(pick);
        Pick byId = pickService.getById(pick.getId());
        BeanUtils.copyProperties(pickUpdateRequest, byId);
        esPickService.esUpdate(byId);
        return ResultUtils.success(true);
    }

    @PostMapping("/updateState/{id}")
    @LogOperation("修改")
    public BaseResponse<Boolean> updateState(@PathVariable("id") Long id) {
        QueryWrapper<Pick> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        Pick pick = pickMapper.selectOne(queryWrapper);
        pick.setStatus("1");
        pickService.updateById(pick);
        if (redisCache.getCacheObject("myPickList"+userService.getLoginUser().getId()) != null) {
            redisCache.deleteObject("myPickList"+userService.getLoginUser().getId());
        }
        return ResultUtils.success(true);
    }

    /**
     * 根据id查询拾取物品
     */
    @GetMapping("/{id}")
    @LogOperation("查询")
    public BaseResponse<PickDto> get(@PathVariable("id") Long id){
        Pick pick = pickService.getById(id);
        PickDto pickDto = new PickDto();
        BeanUtils.copyProperties(pick,pickDto);
        pickDto.setUsername(userService.getById(pick.getUserid()).getUsername());
        return ResultUtils.success(pickDto);
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public BaseResponse<Page> page(int page, int pageSize, String string){
        //构造分页构造器
        Page<Pick> pageInfo = new Page<>(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Pick> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(string!=null,Pick::getPickDate,string);
        wrapper.orderByDesc(Pick::getReportTime);

        Page<Pick> page1 = pickService.page(pageInfo, wrapper);
        return ResultUtils.success(page1);
    }

    /**
     * 根据标签查询pick
     * @param tagName
     * @return
     */
    @GetMapping("/selectByTag")
    public BaseResponse<List<Pick>> selectByTag (String tagName) {
        Tag tag = tagService.getTagByName(tagName);
        List<Long> idList = new ArrayList<>();
        List<Long> newIdList = new ArrayList<>();
        if (tag.getParentId() == 0) {
            LambdaQueryWrapper<Tag> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(Tag::getParentId,tag.getParentId());
            List<Tag> list = tagService.list(wrapper1);
            for (Tag tag1 : list) {
                List<Long> thingId = tagRelService.getThingId(tag1.getId(), 1);
                idList.addAll(thingId);
            }
        } else {
            idList = tagRelService.getThingId(tag.getId(), 1);
        }

        //去重
        for (Long id : idList) {
            if(!newIdList.contains(id)){
                newIdList.add(id);
            }
        }

        List<Pick> pickList = new ArrayList<>();
        for (Long id : newIdList) {
            Pick pick = pickService.getById(id);
            pickList.add(pick);
        }

        return ResultUtils.success(pickList);
    }

    @GetMapping("/query")
    public ResponseResult query(PickQuery pickQuery) {
//        if (pickQuery.getSearchText() != null && pickQuery.getTagList() == null) {
//            return new ResponseResult<>(200,"查询成功",searchPickNameOrDescWithHits(pickQuery.getSearchText()));
//        }
        return pickService.queryAll(pickQuery);
    }

    @GetMapping("/getMy")
    public ResponseResult getMyOrder(){
        if (redisCache.getCacheObject("myPickList"+userService.getLoginUser().getId()) != null) {
            return new ResponseResult<>(200,"查询成功",redisCache.getCacheObject("myPickList"+userService.getLoginUser().getId()));
        }
        Long usId = userService.getLoginUser().getId();
        PickQuery query = new PickQuery();
        query.setUserId(usId);
        return pickService.queryAll(query);
    }

    @GetMapping("/esSearch")
    public List<EsPick> esSearch(String key) {
        return esPickService.searchProduct(key);
    }
    @GetMapping("/searchPickNameWithHits")
    public SearchHits<EsPick> searchPickNameWithHits(String key) {
        return esPickService.searchPickNameWithHits(key);
    }
    @GetMapping("/searchPickNameOrDescWithHits")
    public SearchHits<EsPick> searchPickNameOrTagOrDescWithHits(String key) {
        return esPickService.searchPickNameOrTagOrDescWithHits(key);
    }
}
