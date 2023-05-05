package com.lostAndFind.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lostAndFind.project.model.entity.Role;
import com.lostAndFind.project.model.request.RoleUpdateRequest;

import java.util.List;


/**
* @author a
* @description 针对表【sys_role(角色表)】的数据库操作Service
* @createDate 2022-10-25 14:49:12
*/
public interface RoleService extends IService<Role> {

    boolean updateInfo(RoleUpdateRequest roleUpdateRequeste);

    Long getRoleIdByName(String name);
}
