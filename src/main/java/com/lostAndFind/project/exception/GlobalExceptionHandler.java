package com.lostAndFind.project.exception;

//import cn.dev33.satoken.exception.NotLoginException;
//import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.map.MapUtil;
import com.lostAndFind.project.Utils.ExceptionUtils;
import com.lostAndFind.project.Utils.HttpContextUtils;
import com.lostAndFind.project.Utils.IpUtils;
import com.lostAndFind.project.Utils.JsonUtils;
import com.lostAndFind.project.common.BaseResponse;
import com.lostAndFind.project.common.ErrorCode;
import com.lostAndFind.project.common.ResultUtils;
import com.lostAndFind.project.constant.LoginOperationEnum;
import com.lostAndFind.project.constant.LoginStatusEnum;
import com.lostAndFind.project.model.entity.SysLogError;
import com.lostAndFind.project.model.entity.SysLogLogin;
import com.lostAndFind.project.service.SysLogErrorService;
import com.lostAndFind.project.service.SysLogLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 全局异常处理器
 *
 * @author djk
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    private SysLogErrorService sysLogErrorService;

    @Autowired
    private SysLogLoginService sysLogLoginService;


    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public BaseResponse badCredentialsExceptionHandler(BadCredentialsException e) {
        log.error("BadCredentialsException", e);
        SysLogLogin logLogin = new SysLogLogin();
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        logLogin.setOperation(LoginOperationEnum.LOGIN.value());
        DateFormat bf = new SimpleDateFormat("yyyy-MM-dd E a HH:mm:ss");
        logLogin.setCreateDate(bf.format(new Date()));
        logLogin.setIp(IpUtils.getInstance().getIpAddr(request));
        logLogin.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        logLogin.setStatus(LoginStatusEnum.FAIL.value());
        logLogin.setCreatorName((String) request.getSession().getAttribute("userAccount"));
        sysLogLoginService.save(logLogin);

        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "123");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public BaseResponse accessDeniedExceptionHandler(AccessDeniedException e) {
        log.error("BadCredentialsException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "123");
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse exceptionHandler(Exception e) {
        saveLog(e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }


    /**
     * 保存异常日志
     */
    private void saveLog(Exception ex){
        SysLogError log = new SysLogError();

        //请求相关信息
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        log.setIp(IpUtils.getInstance().getIpAddr(request));
        log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        log.setRequestUri(request.getRequestURI());
        log.setRequestMethod(request.getMethod());
        DateFormat bf = new SimpleDateFormat("yyyy-MM-dd E a HH:mm:ss");
        log.setCreateDate(bf.format(new Date()));
        Map<String, String> params = HttpContextUtils.getParameterMap(request);
        if(MapUtil.isNotEmpty(params)){
            log.setRequestParams(JsonUtils.toJsonString(params));
        }

        //异常信息
        log.setErrorInfo(ExceptionUtils.getErrorStackTrace(ex));

        //保存
        sysLogErrorService.save(log);
    }
//
//    @ExceptionHandler(NotLoginException.class)
//    public SaResult handlerNotLoginException(NotLoginException nle)
//            throws Exception {
//        // 打印堆栈，以供调试
//        nle.printStackTrace();
//
//        // 判断场景值，定制化异常信息
//        String message = "";
//        if (nle.getType().equals(NotLoginException.NOT_TOKEN)) {
//            message = "未提供token";
//        } else if (nle.getType().equals(NotLoginException.INVALID_TOKEN)) {
//            message = "token无效";
//        } else if (nle.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
//            message = "token已过期";
//        } else if (nle.getType().equals(NotLoginException.BE_REPLACED)) {
//            message = "token已被顶下线";
//        } else if (nle.getType().equals(NotLoginException.KICK_OUT)) {
//            message = "token已被踢下线";
//        } else {
//            message = "当前会话未登录";
//        }
//
//        // 返回给前端
//        return SaResult.error(message);
//    }
//
//    // 全局异常拦截
//    @ExceptionHandler
//    public SaResult handlerException(Exception e) {
//        e.printStackTrace();
//        return SaResult.error(e.getMessage());
//    }
}
