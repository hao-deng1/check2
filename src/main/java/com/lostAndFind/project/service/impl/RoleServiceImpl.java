package com.lostAndFind.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.lostAndFind.project.mapper.RoleMapper;
import com.lostAndFind.project.model.entity.Role;
import com.lostAndFind.project.model.entity.User;
import com.lostAndFind.project.model.request.RoleUpdateRequest;
import com.lostAndFind.project.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author a
* @description 针对表【sys_role(角色表)】的数据库操作Service实现
* @createDate 2022-10-25 14:49:12
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    RoleMapper roleMapper;

    @Override
    public boolean updateInfo(RoleUpdateRequest roleUpdateRequest) {
        Role oldRole = roleMapper.selectById(roleUpdateRequest.getId());
        oldRole.setRemark(roleUpdateRequest.getRemark());
        oldRole.setStatus(roleUpdateRequest.getStatus());
        oldRole.setName(roleUpdateRequest.getName());
        return this.updateById(oldRole);
    }

    @Override
    public Long getRoleIdByName(String roleName) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getName,roleName);
        return roleMapper.selectOne(queryWrapper).getId();
    }
}




