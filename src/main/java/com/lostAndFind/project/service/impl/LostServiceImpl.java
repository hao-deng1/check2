package com.lostAndFind.project.service.impl;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lostAndFind.project.common.BaseResponse;
import com.lostAndFind.project.common.ResponseResult;
import com.lostAndFind.project.common.ResultUtils;
import com.lostAndFind.project.config.RedisCache;
import com.lostAndFind.project.mapper.LostMapper;
import com.lostAndFind.project.mapper.TagMapper;
import com.lostAndFind.project.mapper.TagRelMapper;
import com.lostAndFind.project.mapper.UserMapper;
import com.lostAndFind.project.model.entity.Lost;
import com.lostAndFind.project.model.entity.Tag;
import com.lostAndFind.project.model.entity.TagRel;
import com.lostAndFind.project.model.query.LostQuery;
import com.lostAndFind.project.model.vo.LostVo;
import com.lostAndFind.project.model.vo.UrgentVo;
import com.lostAndFind.project.service.LostService;
import com.lostAndFind.project.service.TagRelService;
import com.lostAndFind.project.service.TagService;
import com.lostAndFind.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author a
* @description 针对表【lost(丢失物品表)】的数据库操作Service实现
* @createDate 2022-10-11 16:15:40
*/
@Service
public class LostServiceImpl extends ServiceImpl<LostMapper, Lost> implements LostService {

    @Autowired
    TagService tagService;

    @Autowired
    UserService userService;

    @Resource
    UserMapper userMapper;

    @Resource
    LostMapper lostMapper;

    @Autowired
    TagRelService tagRelService;

    @Autowired
    TagRelMapper tagRelMapper;

    @Autowired
    TagMapper tagMapper;
    @Resource
    private RedisCache redisCache;


    @Override
    @Transactional(rollbackFor = Exception.class)//回滚防止误删
    public void removeWithLost(List<Long> ids) {
        LambdaQueryWrapper<Lost> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Lost::getIsDelete,0);
        Lost lost = new Lost();
        lost.setIsDelete(1);
        for (Long id : ids) {
            lost.setId(id);
            update(lost,wrapper);
        }
    }

    @Override
    public ResponseResult queryAll(LostQuery lostQuery) {
        int flag = 0;
        List<LostVo> lostVo = new ArrayList<>();
        QueryWrapper<Lost> queryWrapper = new QueryWrapper<>();
        System.out.println(lostQuery);
        if (lostQuery.getLostName() != null || lostQuery.getStatus() != null || lostQuery.getDescription() != null || lostQuery.getTagList() != null ||lostQuery.getId() !=null ||lostQuery.getSearchText()!=null || lostQuery.getUserId() !=null) {
            //标签
            if (lostQuery.getTagList() != null) {
                List<String> tagList = lostQuery.getTagList();
                List<Long> tagIdList = new ArrayList<>();
                for (String s : tagList) {
                    Tag tag = tagService.getTagByName(s);
                    tagIdList.add(tag.getId());
                }
                List<Long> pickId = tagRelService.getThingId(tagIdList,"1");
                queryWrapper.in("id",pickId);
            }

            if (lostQuery.getLostName() != null) {
                //名字
                String name = lostQuery.getLostName();
                if (org.apache.commons.lang3.StringUtils.isNotBlank(name)) {
                    queryWrapper.like("lostName", name);
                }
            }

            if (lostQuery.getDescription() != null) {
                //描述
                String description = lostQuery.getDescription();
                if (StringUtils.isNotBlank(description)) {
                    queryWrapper.like("description", description);
                }
            }

            if (lostQuery.getStatus() != null) {
                //状态
                String status = lostQuery.getStatus();
                queryWrapper.eq("status", status);
            }
            if (lostQuery.getId() != null){
                queryWrapper.eq("id",lostQuery.getId());
            }
            String searchText = lostQuery.getSearchText();
            if (StringUtils.isNotBlank(searchText)) {
                queryWrapper.and(qw -> qw.like("lostName", searchText).or().like("description", searchText));
            }
            if (lostQuery.getUserId() !=null){
                queryWrapper.eq("userId",lostQuery.getUserId());
                flag=1;
            }
            queryWrapper.orderByDesc("reportTime");
            List<Lost> list = this.list(queryWrapper);
            for (Lost lost : list) {
                LostVo lostVo1 = new LostVo();
                QueryWrapper<TagRel> queryWrapper1 = new QueryWrapper();
                queryWrapper1.eq("thingid",lost.getId());
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
                lostVo1.setLostDate(lost.getLostDate());
                lostVo1.setId(lost.getId());
                lostVo1.setUrgent(lost.getUrgent());
                lostVo1.setUsername(userMapper.selectById(lost.getUserId()).getUsername());
                lostVo1.setReportTime(lost.getReportTime());
                lostVo1.setIsDelete(lost.getIsDelete());
                lostVo1.setLostName(lost.getLostName());
                lostVo1.setLostType(tagList);
                lostVo1.setBusiness(lost.getBusiness());
                lostVo1.setContact(lost.getContact());
                lostVo1.setDescription(lost.getDescription());
                lostVo1.setStatus(lost.getStatus());
                lostVo1.setImg(lost.getImg());
                lostVo.add(lostVo1);
            }
            if (flag == 1){
                redisCache.setCacheObject("myLostList"+userService.getLoginUser().getId(),lostVo);
            }
            return new ResponseResult<>(200,"查询成功",lostVo);
        } else {
            List<LostVo> lostList = redisCache.getCacheObject("lostList");
            if (lostList == null) {
                queryWrapper.orderByDesc("reportTime");
                List<Lost> list = this.list(queryWrapper);
                for (Lost lost : list) {
                    LostVo lostVo1 = new LostVo();
                    QueryWrapper<TagRel> queryWrapper1 = new QueryWrapper();
                    queryWrapper1.eq("thingid",lost.getId());
                    List<TagRel> tagRels = tagRelMapper.selectList(queryWrapper1);
                    List<String> tagList = new ArrayList<>();
                    for (TagRel tagRel : tagRels) {
                        Long tagid = tagRel.getTagid();
                        tagList.add(tagMapper.selectById(tagid).getTagname());
                    }
                    lostVo1.setLostDate(lost.getLostDate());
                    lostVo1.setId(lost.getId());
                    lostVo1.setUrgent(lost.getUrgent());
                    lostVo1.setUsername(userMapper.selectById(lost.getUserId()).getUsername());
                    lostVo1.setReportTime(lost.getReportTime());
                    lostVo1.setIsDelete(lost.getIsDelete());
                    lostVo1.setLostName(lost.getLostName());
                    lostVo1.setLostType(tagList);
                    lostVo1.setBusiness(lost.getBusiness());
                    lostVo1.setContact(lost.getContact());
                    lostVo1.setDescription(lost.getDescription());
                    lostVo1.setStatus(lost.getStatus());
                    lostVo1.setImg(lost.getImg());
                    lostVo.add(lostVo1);
                }
                redisCache.setCacheObject("lostList",lostVo);
                return new ResponseResult<>(200,"查询成功",lostVo);
            }
            return new ResponseResult<>(200,"查询成功",lostList);
        }
    }

    @Override
    public ResponseResult getUrgent() {
        //1、为每一个未完成订单打分
        List<UrgentVo> urgentVos = doUrgent();
        //2、排序
        urgentVos = urgentVos.stream().sorted(Comparator.comparing(UrgentVo::getGrade)
                .reversed()).collect(Collectors.toList());
        //返回前10
        List<LostVo> lostVos = new ArrayList<>();
        urgentVos = urgentVos.subList(0, 9);
        for (UrgentVo urgentVo : urgentVos) {
            //按照id查订单
            LostVo lostVo = new LostVo();
            QueryWrapper<Lost> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",urgentVo.getId());
            Lost lost = lostMapper.selectOne(queryWrapper);
            QueryWrapper<TagRel> queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("thingid",lost.getId());
            List<TagRel> tagRels = tagRelMapper.selectList(queryWrapper1);
            List<String> tagList = new ArrayList<>();
            for (TagRel tagRel : tagRels) {
                Long tagid = tagRel.getTagid();
                tagList.add(tagMapper.selectById(tagid).getTagname());
            }
            lostVo.setLostDate(lost.getLostDate());
            lostVo.setId(lost.getId());
            lostVo.setUrgent(lost.getUrgent());
            lostVo.setUsername(userMapper.selectById(lost.getUserId()).getUsername());
            lostVo.setReportTime(lost.getReportTime());
            lostVo.setIsDelete(lost.getIsDelete());
            lostVo.setLostName(lost.getLostName());
            lostVo.setLostType(tagList);
            lostVo.setBusiness(lost.getBusiness());
            lostVo.setContact(lost.getContact());
            lostVo.setDescription(lost.getDescription());
            lostVo.setStatus(lost.getStatus());
            lostVo.setImg(lost.getImg());
            lostVos.add(lostVo);
        }
        redisCache.setCacheObject("UrgentOrder",lostVos);
        return new ResponseResult<>(200,"查询成功",lostVos);
    }

    private List<UrgentVo> doUrgent(){
        List<UrgentVo> urgentVos = new ArrayList<>();
        QueryWrapper<Lost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status","0");
        List<Lost> list = lostMapper.selectList(queryWrapper);
        for (Lost lost : list) {
            UrgentVo urgentVo = new UrgentVo();
            if (lost.getUrgent() == null ||lost.getBusiness() ==null){
                continue;
            }
            urgentVo.setId(lost.getId());
            urgentVo.setGrade(getGrade(lost));
            urgentVos.add(urgentVo);
        }
        return urgentVos;
    }

    private double getGrade(Lost lost){
        double grade = 0;
        grade = lost.getBusiness()*0.75 + lost.getUrgent()*0.25;
        return grade;
    }
}




