package com.lostAndFind.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lostAndFind.project.annotation.LogOperation;
import com.lostAndFind.project.common.BaseResponse;
import com.lostAndFind.project.common.ErrorCode;
import com.lostAndFind.project.common.ResponseResult;
import com.lostAndFind.project.common.ResultUtils;
import com.lostAndFind.project.config.RedisCache;
import com.lostAndFind.project.exception.BusinessException;
import com.lostAndFind.project.mapper.UserMapper;
import com.lostAndFind.project.model.entity.Lost;
import com.lostAndFind.project.model.entity.User;
import com.lostAndFind.project.model.request.UserLoginRequest;
import com.lostAndFind.project.model.request.UserRegisterRequest;
import com.lostAndFind.project.model.request.UserUpdateRequest;
import com.lostAndFind.project.model.vo.UserVO;
import com.lostAndFind.project.service.UserService;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;



/**
 * 用户接口
 *
 * @author djk
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://127.0.0.1:5173/"})
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisCache redisCache;


    private static final String SALT = "Djk";

    /**
     * 注册
     * @param userRegisterRequest
     * @return BaseResponse
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 手动添加用户
     * @param user
     * @return BaseResponse
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('perms:user:add')")
    @LogOperation("新增")
    public BaseResponse<String> save(@RequestBody UserUpdateRequest user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(user.getUserPassword());
        user.setUserPassword(encode);
        User newUser = new User();
        BeanUtils.copyProperties(user, newUser);
        newUser.setUserRole("user");
        userService.save(newUser);
        return ResultUtils.success("新增用户成功");
    }

    /**
     * 登录
     * @param userLoginRequest
     * @param request
     * @return ResponseResult
     */
    @PostMapping("/login")
    public ResponseResult userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    /**
     * 登出
     * @param request
     * @return ResponseResult
     */
    @PostMapping("/logout")
    public ResponseResult userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return userService.userLogout(request);
    }


    /**
     *  获取当前的路用户
     * @return UserVO
     */
    @GetMapping("/current")
    @PreAuthorize("hasAuthority('perms:user:query')")
    public BaseResponse<UserVO> getCurrentUser() {
        User user = userService.getLoginUser();
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ResultUtils.success(userVO);
    }

    @GetMapping("/search/safe")
    @PreAuthorize("hasAuthority('perms:user:query')")
    @LogOperation("模糊查询")
    public BaseResponse<List<User>> searchUser(String username, HttpServletRequest request) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }
    @GetMapping("/search/normal")
    @PreAuthorize("hasAuthority('perms:user:query')")
    public BaseResponse<List<User>> searchNormalUser(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        for (User user : userList) {
            user.setUserPassword(user.getUserPassword());
        }
        return ResultUtils.success(userList);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('perms:user:update')")
    @LogOperation("修改")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        boolean result = userService.updateInfo(userUpdateRequest);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        return ResultUtils.success(true);
    }
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('perms:user:delete')")
    @LogOperation("删除")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        userService.assertAdmin(request);
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    @PostMapping("/updateUrl")
    @PreAuthorize("hasAuthority('perms:user:update')")
    @LogOperation("修改")
    public BaseResponse<Boolean> updateUrl(@RequestBody String  url) {
        url = url.replaceAll("%3A", ":").replaceAll("%2F", "/")
                .replaceAll("%3F", "?").replaceAll("%3D", "=").replaceAll(
                        "%26", "&");
//        url = url.substring(0,url.length()-1);
        log.info("url{}",url);
        User loginUser = userService.getLoginUser();
        loginUser.setAvatarUrl(url);
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        BeanUtils.copyProperties(loginUser, userUpdateRequest);
        boolean result = userService.updateInfo(userUpdateRequest);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
        return ResultUtils.success(true);
    }
}
