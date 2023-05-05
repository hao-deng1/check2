package com.lostAndFind.project.service.impl;

import cn.hutool.json.JSONArray;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lostAndFind.project.common.ResponseResult;
import com.lostAndFind.project.config.RedisCache;
import com.lostAndFind.project.mapper.TagMapper;
import com.lostAndFind.project.mapper.TagRelMapper;
import com.lostAndFind.project.mapper.UserMapper;
import com.lostAndFind.project.model.entity.Lost;
import com.lostAndFind.project.model.entity.Pick;
import com.lostAndFind.project.model.entity.Tag;
import com.lostAndFind.project.model.entity.TagRel;
import com.lostAndFind.project.model.query.PickQuery;
import com.lostAndFind.project.model.vo.LostVo;
import com.lostAndFind.project.model.vo.PickVo;
import com.lostAndFind.project.service.PickService;
import com.lostAndFind.project.mapper.PickMapper;
import com.lostAndFind.project.service.TagRelService;
import com.lostAndFind.project.service.TagService;
import com.lostAndFind.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Asus
 */
@Service
public class PickServiceImpl extends ServiceImpl<PickMapper, Pick> implements PickService{

    @Autowired
    TagService tagService;
    @Autowired
    TagRelService tagRelService;
    @Resource
    private RedisCache redisCache;

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private TagRelMapper tagRelMapper;

    @Resource
    private TagMapper tagMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)//回滚防止误删
    public void removeWithPick(List<Long> ids) {
        LambdaQueryWrapper<Pick> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Pick::getIsDelete,0);
        Pick pick = new Pick();
        pick.setIsDelete(1);
        for (Long id : ids) {
            pick.setId(id);
            update(pick,wrapper);
        }
    }


    @Override
    public ResponseResult queryAll(PickQuery pickQuery) {
        boolean flag = false;
        List<PickVo> pickVos = new ArrayList<>();
        QueryWrapper<Pick> queryWrapper = new QueryWrapper<>();
        if (pickQuery.getUserId()!=null || pickQuery.getPickname() != null || pickQuery.getDescription() != null || pickQuery.getStatus() != null || pickQuery.getTagList() != null||pickQuery.getId()!=null ||pickQuery.getSearchText()!=null) {
            //标签
            if (pickQuery.getTagList() != null) {
                List<String> tagList = pickQuery.getTagList();
                List<Long> tagIdList = new ArrayList<>();
                for (String s : tagList) {
                    Tag tag = tagService.getTagByName(s);
                    tagIdList.add(tag.getId());
                }
                List<Long> pickId = tagRelService.getThingId(tagIdList,"1");
                queryWrapper.in("id",pickId);
            }

            if (pickQuery.getPickname() != null) {
                //名字
                String name = pickQuery.getPickname();
                if (StringUtils.isNotBlank(name)) {
                    queryWrapper.like("pickname", name);
                }
            }

            if (pickQuery.getDescription() != null) {
                //描述
                String description = pickQuery.getDescription();
                if (StringUtils.isNotBlank(description)) {
                    queryWrapper.like("description", description);
                }
            }
            if (pickQuery.getStatus() != null) {
                //状态
                String status = pickQuery.getStatus();
                queryWrapper.eq("status", status);
            }
            if (pickQuery.getId() != null) {
                queryWrapper.eq("id", pickQuery.getId() );
            }
            String searchText = pickQuery.getSearchText();
            if (StringUtils.isNotBlank(searchText)) {
                queryWrapper.and(qw -> qw.like("pickName", searchText).or().like("description", searchText));
            }
            if(pickQuery.getUserId()!= null){
                queryWrapper.eq("userId",pickQuery.getUserId());
                flag=true;
            }
            queryWrapper.orderByDesc("reportTime");
            List<Pick> list = this.list(queryWrapper);
            for (Pick pick : list) {
                PickVo pickVo = new PickVo();
                QueryWrapper<TagRel> queryWrapper1 = new QueryWrapper();
                queryWrapper1.eq("thingid",pick.getId());
                List<TagRel> tagRels = tagRelMapper.selectList(queryWrapper1);
                List<String> tagList = new ArrayList<>();
                for (TagRel tagRel : tagRels) {
                    Long tagid = tagRel.getTagid();
                    String tagname = tagMapper.selectById(tagid).getTagname();
                    if(StringUtils.isNotBlank(searchText)){
                        boolean contains = tagname.contains(searchText);
                        if(contains){
                            tagList.add(tagname);
                        }
                    }else {
                        tagList.add(tagname);
                    }
                }
                pickVo.setPickDate(pick.getPickDate());
                pickVo.setId(pick.getId());
                pickVo.setUsername(userMapper.selectById(pick.getUserid()).getUsername());
                pickVo.setReportTime(pick.getReportTime());
                pickVo.setPickPlace(pick.getPickPlace());
                pickVo.setIsDelete(pick.getIsDelete());
                pickVo.setPickName(pick.getPickName());
                pickVo.setPickType(tagList);
                pickVo.setContact(pick.getContact());
                pickVo.setDescription(pick.getDescription());
                pickVo.setStatus(pick.getStatus());
                pickVo.setImg(pick.getImg());
                pickVos.add(pickVo);
            }
            if (flag){
                redisCache.setCacheObject("myPickList"+userService.getLoginUser().getId(),pickVos);
            }
            return new ResponseResult<>(200,"查询成功",pickVos);
        } else {
            Object pickList = redisCache.getCacheObject("pickList");
            if (pickList == null) {
                queryWrapper.orderByDesc("reportTime");
                List<Pick> list = this.list(queryWrapper);
                for (Pick pick : list) {
                    PickVo pickVo = new PickVo();
                    QueryWrapper<TagRel> queryWrapper1 = new QueryWrapper();
                    queryWrapper1.eq("thingid", pick.getId());
                    List<TagRel> tagRels = tagRelMapper.selectList(queryWrapper1);
                    List<String> tagList = new ArrayList<>();
                    for (TagRel tagRel : tagRels) {
                        Long tagid = tagRel.getTagid();
                        tagList.add(tagMapper.selectById(tagid).getTagname());
                    }
                    pickVo.setPickDate(pick.getPickDate());
                    pickVo.setId(pick.getId());
                    pickVo.setUsername(userMapper.selectById(pick.getUserid()).getUsername());
                    pickVo.setReportTime(pick.getReportTime());
                    pickVo.setIsDelete(pick.getIsDelete());
                    pickVo.setPickName(pick.getPickName());
                    pickVo.setPickType(tagList);
                    pickVo.setContact(pick.getContact());
                    pickVo.setDescription(pick.getDescription());
                    pickVo.setStatus(pick.getStatus());
                    pickVo.setImg(pick.getImg());
                    pickVos.add(pickVo);
                }
                redisCache.setCacheObject("pickList",pickVos);
                return new ResponseResult<>(200,"查询成功",pickVos);
            }
            return new ResponseResult<>(200,"查询成功",pickList);
        }
    }
}
