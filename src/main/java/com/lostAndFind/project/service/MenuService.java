package com.lostAndFind.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lostAndFind.project.model.entity.Menu;
import com.lostAndFind.project.model.vo.MenuVo;

import java.util.List;

/**
* @author Asus
* @description 针对表【sys_menu(菜单表)】的数据库操作Service
* @createDate 2022-11-02 19:03:09
*/
public interface MenuService extends IService<Menu> {

    void removeWithLost(List<Long> ids);

    List<MenuVo> findByTree();

    List<String> selectPermsByRoleId(Long id);
}
