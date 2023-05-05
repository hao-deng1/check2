/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.lostAndFind.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lostAndFind.project.Utils.ExcelUtils;
import com.lostAndFind.project.Utils.excel.SysLogOperationExcel;
import com.lostAndFind.project.common.BaseResponse;
import com.lostAndFind.project.common.ResultUtils;
import com.lostAndFind.project.model.dto.SysLogOperationDTO;
import com.lostAndFind.project.model.entity.SysLogLogin;
import com.lostAndFind.project.model.entity.SysLogOperation;
import com.lostAndFind.project.service.SysLogOperationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 操作日志
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("sys/log/operation")
@Api(tags="操作日志")
public class SysLogOperationController {
    @Autowired
    private SysLogOperationService sysLogOperationService;

    @GetMapping("page")
    public BaseResponse<Page> page(int page, int pageSize){
        //构造分页构造器
        Page<SysLogOperation> pageInfo = new Page<>(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<SysLogOperation> wrapper = new LambdaQueryWrapper<>();

        Page<SysLogOperation> page1 = sysLogOperationService.page(pageInfo, wrapper);
        return ResultUtils.success(page1);
    }

//    @GetMapping("export")
//    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
//        List<SysLogOperationDTO> list = sysLogOperationService.list(params);
//
//        ExcelUtils.exportExcelToTarget(response, null, list, SysLogOperationExcel.class);
//    }

    @GetMapping("/list")
    public BaseResponse<List<SysLogOperation>> list() {
        List<SysLogOperation> list = sysLogOperationService.list();
        return ResultUtils.success(list);
    }

}