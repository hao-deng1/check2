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
import com.lostAndFind.project.Utils.excel.SysLogErrorExcel;
import com.lostAndFind.project.annotation.LogOperation;
import com.lostAndFind.project.common.BaseResponse;
import com.lostAndFind.project.common.ResultUtils;
import com.lostAndFind.project.model.dto.SysLogErrorDTO;
import com.lostAndFind.project.model.entity.Role;
import com.lostAndFind.project.model.entity.SysLogError;
import com.lostAndFind.project.service.SysLogErrorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * 异常日志
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("sys/log/error")
@Api(tags="异常日志")
public class SysLogErrorController {
    @Autowired
    private SysLogErrorService sysLogErrorService;

    @GetMapping("page")
    public BaseResponse<Page> page(int page, int pageSize){
        //构造分页构造器
        Page<SysLogError> pageInfo = new Page<>(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<SysLogError> wrapper = new LambdaQueryWrapper<>();

        Page<SysLogError> page1 = sysLogErrorService.page(pageInfo, wrapper);
        return ResultUtils.success(page1);
    }

//    @GetMapping("export")
//    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
//        List<SysLogErrorDTO> list = sysLogErrorService.list(params);
//
//        ExcelUtils.exportExcelToTarget(response, null, list, SysLogErrorExcel.class);
//    }

    @GetMapping("/list")
    public BaseResponse<List<SysLogError>> list() {
        List<SysLogError> list = sysLogErrorService.list();
        return ResultUtils.success(list);
    }
}