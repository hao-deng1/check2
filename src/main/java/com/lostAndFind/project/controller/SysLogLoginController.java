package com.lostAndFind.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lostAndFind.project.Utils.ExcelUtils;
import com.lostAndFind.project.Utils.excel.SysLogLoginExcel;
import com.lostAndFind.project.annotation.LogOperation;
import com.lostAndFind.project.common.BaseResponse;
import com.lostAndFind.project.common.ResultUtils;
import com.lostAndFind.project.model.dto.SysLogLoginDTO;
import com.lostAndFind.project.model.entity.SysLogError;
import com.lostAndFind.project.model.entity.SysLogLogin;
import com.lostAndFind.project.service.SysLogLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
 * 登录日志
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@RestController
@RequestMapping("sys/log/login")
@Api(tags="登录日志")
public class SysLogLoginController {
    @Autowired
    private SysLogLoginService sysLogLoginService;

    @GetMapping("page")
    public BaseResponse<Page> page(int page, int pageSize){
        //构造分页构造器
        Page<SysLogLogin> pageInfo = new Page<>(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<SysLogLogin> wrapper = new LambdaQueryWrapper<>();

        Page<SysLogLogin> page1 = sysLogLoginService.page(pageInfo, wrapper);
        return ResultUtils.success(page1);
    }

//    @GetMapping("export")
//    public void export(@ApiIgnore @RequestParam Map<String, Object> params, HttpServletResponse response) throws Exception {
//        List<SysLogLoginDTO> list = sysLogLoginService.list(params);
//
//        ExcelUtils.exportExcelToTarget(response, null, list, SysLogLoginExcel.class);
//    }

    @GetMapping("/list")
    public BaseResponse<List<SysLogLogin>> list() {
        List<SysLogLogin> list = sysLogLoginService.list();
        return ResultUtils.success(list);
    }
}