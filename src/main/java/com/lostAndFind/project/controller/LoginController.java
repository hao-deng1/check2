package com.lostAndFind.project.controller;

import com.lostAndFind.project.common.ResponseResult;
import com.lostAndFind.project.service.LoginService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Resource
    LoginService loginService;

    /**
     * 生成图片验证码
     * time用于保证每次可以刷新图片验证码
     */
    @GetMapping("/authCode/{time}")
    public void getAuthCode(@PathVariable("time")String time, HttpServletRequest request, HttpServletResponse response) throws IOException {
        loginService.getAuthCode(request,response);
    }

    /**
     * 验证图片验证码
     */
    @GetMapping("/verifyCode/{inputVerify}")
    public ResponseResult verifyCode(@PathVariable("inputVerify") String inputVerify, HttpServletRequest request){
        Boolean bool = loginService.verifyCode(inputVerify,request);
        return new ResponseResult(200,"已验证",bool);
    }
}
