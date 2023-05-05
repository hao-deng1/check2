package com.lostAndFind.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lostAndFind.project.mapper.MenuMapper;
import com.lostAndFind.project.mapper.RoleMenuMapper;
import com.lostAndFind.project.model.entity.Menu;
import com.lostAndFind.project.model.entity.RoleMenu;
import com.lostAndFind.project.model.vo.MenuVo;
import com.lostAndFind.project.service.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Asus
* @description 针对表【sys_menu(菜单表)】的数据库操作Service实现
* @createDate 2022-11-02 19:03:09
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
implements MenuService{

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)//回滚防止误删
    public void removeWithLost(List<Long> ids) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getIsDelete,0);
        Menu menu = new Menu();
        menu.setIsDelete(1);
        for (Long id : ids) {
            menu.setId(id);
            update(menu,wrapper);
        }
    }

    @Override
    public List<MenuVo> findByTree() {

        //查询出所有的部门-->可以直接写SQL，用对应的Vo去接收就可以了
        List<Menu> menuList = menuMapper.selectList(null);

        //将部门转换成Vo
        List<MenuVo> menuVoList = menuList.stream().map((menu) -> {
            MenuVo vo = new MenuVo();
            vo.setId(menu.getId());
            vo.setTitle(menu.getMenuName());
            vo.setValue(menu.getPerms());
            vo.setParentId(menu.getParentId());
            vo.setStatus(menu.getStatus());
            return vo;
        }).collect(Collectors.toList());
        //使用filter去进行拦截，进行判断

        return menuVoList.stream().filter(menuVo -> 0 == menuVo.getParentId())
                .peek(menuVo -> menuVo.setChildren(createChildList(menuVo, menuVoList)))
                .sorted(Comparator.comparing(MenuVo::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> selectPermsByRoleId(Long id) {
        //根据id查找role_menu
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("roleId",id);
        List<RoleMenu> list = roleMenuMapper.selectList(queryWrapper);
        List<String> permsList = new ArrayList<>();
        for (RoleMenu roleMenu : list) {
            QueryWrapper<Menu> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("id",roleMenu.getMenuId());
            Menu menu = menuMapper.selectOne(queryWrapper1);
            permsList.add(menu.getPerms());
        }
        return permsList;
    }

    /**
     * @param menuVo  父级
     * @param menuVoList 对应的list
     * @return
     */
    private static List<MenuVo> createChildList(MenuVo menuVo, List<MenuVo> menuVoList) {
        return menuVoList.stream().filter(model -> menuVo.getId().equals(model.getParentId()))
                .peek(model -> model.setChildren(createChildList(model, menuVoList)))
                .sorted((Comparator.comparing(MenuVo::getId))).collect(Collectors.toList());
    }
}
