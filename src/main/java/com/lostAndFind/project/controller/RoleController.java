package com.lostAndFind.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lostAndFind.project.annotation.LogOperation;
import com.lostAndFind.project.common.BaseResponse;
import com.lostAndFind.project.common.ErrorCode;
import com.lostAndFind.project.common.ResponseResult;
import com.lostAndFind.project.common.ResultUtils;
import com.lostAndFind.project.exception.BusinessException;
import com.lostAndFind.project.model.entity.Lost;
import com.lostAndFind.project.model.entity.Menu;
import com.lostAndFind.project.model.entity.Role;
import com.lostAndFind.project.model.entity.User;
import com.lostAndFind.project.model.request.RoleAddRequest;
import com.lostAndFind.project.model.request.RoleUpdateRequest;
import com.lostAndFind.project.model.request.UserUpdateRequest;
import com.lostAndFind.project.model.vo.GrantMenuVo;
import com.lostAndFind.project.service.GrantService;
import com.lostAndFind.project.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yc_
 * @version 1.0
 */

@RestController
@RequestMapping("/role")
@CrossOrigin(origins = {"http://127.0.0.1:5173/"})
public class RoleController {


    @Resource
    RoleService roleService;


    @Resource
    GrantService grantService;

    /**
     * 新增角色
     * @param
     * @return
     */
    @PostMapping("/add")
    @LogOperation("新增")
    public BaseResponse<String> save(@RequestBody RoleAddRequest roleAddRequest) {
        Role newRole = new Role();
        newRole.setName(roleAddRequest.getName());
        newRole.setStatus(roleAddRequest.getStatus());
        newRole.setRemark(roleAddRequest.getRemark());
        roleService.save(newRole);
        Long id = roleService.getRoleIdByName(roleAddRequest.getName());
        List<String> perms = roleAddRequest.getPerms();
        List<Long> list = grantService.listPermsId(perms);
        GrantMenuVo grantMenuVo=new GrantMenuVo();
        grantMenuVo.setRoleId(id);
        grantMenuVo.setMenuId(list);
        grantService.grantMenu(grantMenuVo);
        return ResultUtils.success("新增用户成功");
    }

    /**
     * 修改角色
     */
    @PostMapping("/update")
    @LogOperation("修改")
    public BaseResponse<Boolean> update(@RequestBody RoleUpdateRequest roleUpdateRequest) {
        boolean result = roleService.updateInfo(roleUpdateRequest);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        //赋权
        List<Long> list = grantService.listPermsId(roleUpdateRequest.getPerms());
        GrantMenuVo grantMenuVo=new GrantMenuVo();
        grantMenuVo.setRoleId(roleUpdateRequest.getId());
        grantMenuVo.setMenuId(list);
        grantService.grantMenu(grantMenuVo);
        return ResultUtils.success(true);
    }


    /**
     *
     *改变角色状态：停启用
     */
    @PostMapping("/status/{status}")
        @LogOperation("停启用")
        public BaseResponse<String> status (@PathVariable("status") String status, @RequestParam List<Long> ids){
            for (Long id : ids) {
                Role role = roleService.getById(id);
                role.setStatus(status);
                roleService.updateById(role);
            }
            return ResultUtils.success("ok");
    }

    /**
     * 角色列表
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('perms:role:add')")
    public BaseResponse<List<Role>> list() {
        List<Role> list = roleService.list();
        return ResultUtils.success(list);
    }


    /**
     * 分页查询
     */
    @GetMapping("/page")
    public BaseResponse<Page> page(int page, int pageSize, String string){
        //构造分页构造器
        Page<Role> pageInfo = new Page<>(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(string!=null,Role::getName,string);
        wrapper.orderByDesc(Role::getUpdateTime);

        Page<Role> page1 = roleService.page(pageInfo, wrapper);
        return ResultUtils.success(page1);
    }
}
