package com.lostAndFind.project.controller;

import com.lostAndFind.project.annotation.LogOperation;
import com.lostAndFind.project.common.ResponseResult;
import com.lostAndFind.project.model.vo.GrantMenuVo;
import com.lostAndFind.project.model.vo.GrantVo;
import com.lostAndFind.project.service.GrantService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * @author djk
 */
@RestController
@RequestMapping("/grant")
public class GrantController {

    @Resource
    GrantService grantService;

    @PostMapping("/role")
    @PreAuthorize("hasAuthority('perms/role/add')")
    @LogOperation("为用户赋角色")
    public ResponseResult grantRole(@RequestBody GrantVo grantVo){
        return grantService.grant(grantVo);
    }


    @PostMapping("/menu")
    @PreAuthorize("hasAuthority('perms:menu:list')")
    @LogOperation("为角色赋权限")
    public ResponseResult grantMenu(@RequestBody GrantMenuVo grantMenuVo){
        return grantService.grantMenu(grantMenuVo);
    }

}
