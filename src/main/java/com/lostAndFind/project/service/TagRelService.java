package com.lostAndFind.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lostAndFind.project.model.entity.TagRel;

import java.util.List;

/**
* @author Asus
* @description 针对表【tag_rel】的数据库操作Service
* @createDate 2022-11-15 22:09:10
*/
public interface TagRelService extends IService<TagRel> {

    List<Long> getThingId(Long tagId,int type);

    void saveAll(List<String> lostType, String type, Long id);

    List<Long> getThingId(List<Long> tagIdList,String type);
}
