package com.lostAndFind.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lostAndFind.project.model.entity.Pick;
import com.lostAndFind.project.model.entity.Tag;
import com.lostAndFind.project.service.TagService;
import com.lostAndFind.project.mapper.TagMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
* @author Asus
* @description 针对表【tag(标签表)】的数据库操作Service实现
* @createDate 2022-11-15 16:46:52
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
implements TagService{
    //['钥匙','随身物品']

    @Override
    public String merge(List<Tag> list) {
        StringBuffer sb = new StringBuffer("[");
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                sb.append("'").append(list.get(i).getTagname()).append("'");
            } else {
                sb.append(",").append("'").append(list.get(i).getTagname()).append("'");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public Tag getTagByName(String tagName) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getTagname,tagName);
        return this.getOne(wrapper);
    }

    @Override
    public List<Tag> separate(String string) {
        String[] split = string.split(",");
        String name = "";
        List<Tag> list = new ArrayList<>();
        for (String s : split) {
            String[] split1 = s.split("'");
            name = split1[1];
            LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Tag::getTagname, name);
            Tag one = this.getOne(wrapper);
            list.add(one);
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)//回滚防止误删
    public void removeWithPick(List<Long> ids) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getIsdelete,0);
        Tag tag = new Tag();
        tag.setIsdelete(1);
        for (Long id : ids) {
            tag.setId(id);
            update(tag,wrapper);
        }
    }
}
