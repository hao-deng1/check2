package com.lostAndFind.project.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lostAndFind.project.common.BaseResponse;
import com.lostAndFind.project.common.ResponseResult;
import com.lostAndFind.project.common.ResultUtils;
import com.lostAndFind.project.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

import static com.lostAndFind.project.common.ErrorCode.SYSTEM_ERROR;

/**
 * @author djk
 */
@Slf4j
@RestController
@RequestMapping("/mini")
@Api(tags = "微信小程序")
public class MiniController {

    @Value("${mini.appid}")
    private String appid;

    @Value("${mini.secret}")
    private String secret;

    @Resource
    UserService userService;

    @PostMapping("/hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("/login")
    public ResponseResult login(String code) throws IOException {
        if(StringUtils.isEmpty(code)){
            throw new RuntimeException("系统错误");
        }
        String url = "https://api.weixin.qq.com/sns/jscode2session?" + "appid=" +
                appid +
                "&secret=" +
                secret +
                "&js_code=" +
                code +
                "&grant_type=authorization_code";
        //创建一个client请求
        CloseableHttpClient client = HttpClientBuilder.create().build();
        //get请求
        HttpGet get = new HttpGet(url);
        //发送请求
        CloseableHttpResponse response = client.execute(get);
        log.info("请求响应码:{}",response.getStatusLine().getStatusCode());
        String result = EntityUtils.toString(response.getEntity());
        log.info("请求响应结果: --> {}",result);
        JSONObject jsonObject = JSON.parseObject(result);
        String openid = jsonObject.getString("openid");
        log.info("微信小程序唯一标识: {}", openid);
        return userService.miniLogin(openid);
    }
}
