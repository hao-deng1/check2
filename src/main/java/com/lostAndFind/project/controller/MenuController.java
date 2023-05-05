package com.lostAndFind.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lostAndFind.project.annotation.LogOperation;
import com.lostAndFind.project.common.BaseResponse;
import com.lostAndFind.project.common.ErrorCode;
import com.lostAndFind.project.common.ResultUtils;
import com.lostAndFind.project.exception.BusinessException;
import com.lostAndFind.project.mapper.MenuMapper;
import com.lostAndFind.project.mapper.RoleMapper;
import com.lostAndFind.project.model.entity.Lost;
import com.lostAndFind.project.model.entity.Menu;
import com.lostAndFind.project.model.entity.Role;
import com.lostAndFind.project.model.request.MenuAddRequest;
import com.lostAndFind.project.model.request.MenuUpdateRequest;
import com.lostAndFind.project.model.request.RoleUpdateRequest;
import com.lostAndFind.project.model.vo.MenuVo;
import com.lostAndFind.project.service.MenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yc_
 * @version 1.0
 */

@RestController
@RequestMapping("/menu")
@CrossOrigin(origins = {"http://127.0.0.1:5173/"})
public class MenuController {

    @Resource
    MenuService menuService;

    @Resource
    MenuMapper menuMapper;

    @Resource
    RoleMapper roleMapper;

    /**
     * 新增权限
     * @param menuAddRequest
     * @return
     */
    @PostMapping("/add")
    @LogOperation("新增")
    public BaseResponse<String> save1(@RequestBody MenuAddRequest menuAddRequest) {
        Menu newMenu = new Menu();
        BeanUtils.copyProperties(menuAddRequest, newMenu);
        menuService.save(newMenu);
        return ResultUtils.success("新增权限成功");
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/delete")
    @LogOperation("删除")
    public BaseResponse<String> delete(@RequestParam List<Long> ids) {
        menuService.removeWithLost(ids);
        return ResultUtils.success("删除成功");
    }

    /**
     * 修改权限
     */
    @PostMapping("/update")
    @LogOperation("修改")
    public BaseResponse<Boolean> update(@RequestBody MenuUpdateRequest menuUpdateRequest) {
        Menu newMenu = new Menu();
        BeanUtils.copyProperties(menuUpdateRequest, newMenu);
        menuMapper.updateById(newMenu);
        return ResultUtils.success(true);
    }


    /**
     * 权限列表
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<Menu>> list() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getIsDelete,0);
        List<Menu> list = menuService.list(wrapper);
        return ResultUtils.success(list);
    }

    @GetMapping("/one")
    public BaseResponse<RoleUpdateRequest> listOne(Long id) {
        RoleUpdateRequest roleUpdateRequest = new RoleUpdateRequest();
        List<String> permissionKeyList = menuService.selectPermsByRoleId(id);
        roleUpdateRequest.setPerms(permissionKeyList);
        Role role = roleMapper.selectById(id);
        roleUpdateRequest.setId(id);
        roleUpdateRequest.setName(role.getName());
        roleUpdateRequest.setStatus(role.getStatus());
        roleUpdateRequest.setRemark(role.getRemark());
        return ResultUtils.success(roleUpdateRequest);
    }
    @GetMapping("/perms/one")
    public BaseResponse<Menu> listPermsOne(Long id) {
        Menu menu = menuMapper.selectById(id);
        return ResultUtils.success(menu);
    }


    @GetMapping("/tree/list")
    public BaseResponse<List<MenuVo>> findByTree(){
        List<MenuVo> byTree = menuService.findByTree();
        return ResultUtils.success(byTree);
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public BaseResponse<Page> page(int page, int pageSize, String string){
        //构造分页构造器
        Page<Menu> pageInfo = new Page<>(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(string!=null,Menu::getMenuName,string);
        wrapper.orderByDesc(Menu::getUpdateTime);
        Page<Menu> page1 = menuService.page(pageInfo, wrapper);
        return ResultUtils.success(page1);
    }

}
