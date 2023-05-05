package com.lostAndFind.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lostAndFind.project.common.ResponseResult;
import com.lostAndFind.project.mapper.MenuMapper;
import com.lostAndFind.project.mapper.RoleMenuMapper;
import com.lostAndFind.project.mapper.UserRoleMapper;
import com.lostAndFind.project.model.entity.Menu;
import com.lostAndFind.project.model.entity.RoleMenu;
import com.lostAndFind.project.model.entity.UserRole;
import com.lostAndFind.project.model.vo.GrantMenuVo;
import com.lostAndFind.project.model.vo.GrantVo;
import com.lostAndFind.project.service.GrantService;
import com.lostAndFind.project.service.RoleMenuService;
import com.lostAndFind.project.service.UserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Djk
 */
@Service
public class GrantServiceImpl implements GrantService {

    @Resource
    UserRoleMapper userRoleMapper;

    @Resource
    UserRoleService userRoleService;

    @Resource
    RoleMenuMapper roleMenuMapper;

    @Resource
    MenuMapper menuMapper;

    @Resource
    RoleMenuService roleMenuService;

    @Override
    @Transactional(rollbackFor = Exception.class)//回滚防止误删
    public ResponseResult grant(GrantVo grantVo) {
        if(Objects.isNull(grantVo)){
            throw new RuntimeException("查询信息为空");
        }
        //查找用户角色
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",grantVo.getUserId());
        List<UserRole> userRoles = userRoleMapper.selectList(queryWrapper);
        if (!userRoles.isEmpty()){
            userRoleMapper.delete(queryWrapper);
        }
        //没有该角色赋权
        for (Long roleId : grantVo.getRoleIds()) {
            UserRole userNewRole = new UserRole();
            userNewRole.setUserId(grantVo.getUserId());
            userNewRole.setRoleId(roleId);
            boolean result = userRoleService.save(userNewRole);
            //此时该用户
            if (!result){
                throw new RuntimeException("赋权失败");
            }
        }
        return new ResponseResult<>(200,"用户给予角色成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)//回滚防止误删
    public ResponseResult grantMenu(GrantMenuVo grantMenuVo) {
        if(Objects.isNull(grantMenuVo)){
            throw new RuntimeException("查询信息为空");
        }
        long roleRealId = grantMenuVo.getRoleId();
        //查找用户角色,删除
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("roleId",roleRealId);
        List<RoleMenu> userRoles = roleMenuMapper.selectList(queryWrapper);
        if(!userRoles.isEmpty()){
            roleMenuMapper.delete(queryWrapper);
        }
        //没有该角色赋权
        for (Long grant : grantMenuVo.getMenuId()) {
            RoleMenu roleMenuVo = new RoleMenu();
            roleMenuVo.setRoleId(roleRealId);
            roleMenuVo.setMenuId(grant);
            boolean save = roleMenuService.save(roleMenuVo);
            if (!save){
                throw new RuntimeException("系统错误");
            }
        }
        return new ResponseResult<>(200,"角色赋权成功");
    }

    @Override
    public List<Long> listPermsId(List<String> perms) {
        List<Long> listId = new ArrayList<>();
        for (String perm : perms) {
            QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("perms",perm);
            Menu menu = menuMapper.selectOne(queryWrapper);
            listId.add(menu.getId());
        }
        return listId;
    }
}
