package com.lostAndFind.project.service.impl;

import com.lostAndFind.project.Utils.VerifyCodeUtils;
import com.lostAndFind.project.service.LoginService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class LoginServiceImpl implements LoginService {
    /**
     * 生成图片验证码
     */
    @Override
    public void getAuthCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //req.setCharacterEncoding("utf-8");
        //res.setContentType("text/html;charset=utf-8");
        // 设置http响应的文件MIME类型为图片
        response.setContentType("image/jpeg");
        // 不让浏览器记录此图片的缓存
        response.setDateHeader("expries", -1);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        // 这里调用了一个工具类VerifyCodeUtils来生成指定位数(也可指定内容)的验证码字符串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        // 将生成验证码字符串保存到session域中,方面进行表单验证
        request.getSession().setAttribute("verifyCode", verifyCode);
        VerifyCodeUtils.outputImage(60, 30, response.getOutputStream(), verifyCode);
    }

    /**
     * 验证图片验证码
     */
    @Override
    public Boolean verifyCode(String inputVerify, HttpServletRequest request) {
        String verifyCode = (String) request.getSession().getAttribute("verifyCode");
        return inputVerify.equalsIgnoreCase(verifyCode);
    }
}
