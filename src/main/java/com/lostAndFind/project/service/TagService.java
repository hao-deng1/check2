package com.lostAndFind.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lostAndFind.project.model.entity.Tag;

import java.util.List;

/**
* @author Asus
* @description 针对表【tag(标签表)】的数据库操作Service
* @createDate 2022-11-15 16:46:52
*/
public interface TagService extends IService<Tag> {
    void removeWithPick(List<Long> ids);

    List<Tag> separate(String string);

    String merge(List<Tag> list);

    Tag getTagByName(String tagName);
}
