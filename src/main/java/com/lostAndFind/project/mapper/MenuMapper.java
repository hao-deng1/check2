package com.lostAndFind.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lostAndFind.project.model.entity.Menu;

import java.util.List;

/**
 * @Author 三更  B站： https://space.bilibili.com/663528522
 */
public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermsByUserId(Long id);

}
