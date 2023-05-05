package com.lostAndFind.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lostAndFind.project.common.ResponseResult;
import com.lostAndFind.project.model.entity.LoginUser;
import com.lostAndFind.project.model.entity.Role;
import com.lostAndFind.project.model.entity.User;
import com.lostAndFind.project.model.request.NormalUpdateRequest;
import com.lostAndFind.project.model.request.UserUpdateRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
 *
 * @author djk
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount 用户账户
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return
     */
    ResponseResult userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 微信小程序登录
     * @param openid
     * @return
     */
    ResponseResult miniLogin(String openid);;

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    ResponseResult userLogout(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);


    /**
     * 断言是管理员
     *
     * @param request
     */
    void assertAdmin(HttpServletRequest request);

    /**
     * 获取登录用户（查缓存）
     *
     * @return
     * @throws com.lostAndFind.project.exception.BusinessException 未登录则抛异常
     */
    User getLoginUser();

    boolean updateInfo(UserUpdateRequest userUpdateRequest);

    boolean updateNormalInfo(NormalUpdateRequest normalUpdateRequest);

}
