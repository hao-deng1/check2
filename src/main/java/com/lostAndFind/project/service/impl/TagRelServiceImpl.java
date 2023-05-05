package com.lostAndFind.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lostAndFind.project.model.entity.Tag;
import com.lostAndFind.project.model.entity.TagRel;
import com.lostAndFind.project.service.TagRelService;
import com.lostAndFind.project.mapper.TagRelMapper;
import com.lostAndFind.project.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author Asus
* @description 针对表【tag_rel】的数据库操作Service实现
* @createDate 2022-11-15 22:09:10
*/
@Service
public class TagRelServiceImpl extends ServiceImpl<TagRelMapper, TagRel>
implements TagRelService{

    @Autowired
    TagService tagService;

    @Override
    public List<Long> getThingId(Long tagId,int type) {
        List<Long> idList = new ArrayList<>();
        LambdaQueryWrapper<TagRel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TagRel::getTagid,tagId).eq(TagRel::getType,type);
        List<TagRel> list = this.list(wrapper);
        for (TagRel tagRel : list) {
            idList.add(tagRel.getThingid());
        }
        return idList;
    }

    @Override
    public void saveAll(List<String> lostType, String type, Long id) {
        if (lostType == null) {
            return;
        }
        List<Tag> tagList = new ArrayList<>();
        for (String s : lostType) {
            Tag tag = tagService.getTagByName(s);
            tagList.add(tag);
            TagRel tagRel = new TagRel();
            tagRel.setType(type);
            tagRel.setTagid(tag.getId());
            tagRel.setThingid(id);
            this.save(tagRel);
        }
    }

    @Override
    public List<Long> getThingId(List<Long> tagIdList,String type) {
        Set<Long> set = new HashSet<>();
        List<Long> idList = new ArrayList<>();
        for (int i = 0; i < tagIdList.size(); i++) {
            LambdaQueryWrapper<TagRel> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TagRel::getTagid,tagIdList.get(i));
            wrapper.eq(TagRel::getType,type);
            List<TagRel> tagRelList = list(wrapper);
            if (i == 0) {
                for (TagRel tagRel : tagRelList) {
                    idList.add(tagRel.getThingid());
                }
            } else {
                for (TagRel tagRel : tagRelList) {
                    Long thingId = tagRel.getThingid();
                    if (!idList.contains(thingId)) {
                        idList.add(thingId);
                    }
                }
            }
        }
        return idList;
    }
}
