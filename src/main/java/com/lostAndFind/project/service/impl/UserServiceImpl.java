package com.lostAndFind.project.service.impl;

import static com.lostAndFind.project.constant.UserConstant.ADMIN_ROLE;
import static com.lostAndFind.project.constant.UserConstant.USER_LOGIN_STATE;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.lostAndFind.project.Utils.JwtUtil;
import com.lostAndFind.project.Utils.IpUtils;
import com.lostAndFind.project.Utils.JwtUtil;
import com.lostAndFind.project.common.ErrorCode;
//import com.lostAndFind.project.config.RedisCache;
import com.lostAndFind.project.common.ResponseResult;
import com.lostAndFind.project.config.RedisCache;
import com.lostAndFind.project.constant.LoginOperationEnum;
import com.lostAndFind.project.constant.LoginStatusEnum;
import com.lostAndFind.project.exception.BusinessException;
import com.lostAndFind.project.mapper.MenuMapper;
import com.lostAndFind.project.mapper.RoleMapper;
import com.lostAndFind.project.mapper.UserMapper;
//import com.lostAndFind.project.model.entity.LoginUser;
import com.lostAndFind.project.mapper.UserRoleMapper;
import com.lostAndFind.project.model.entity.*;
import com.lostAndFind.project.model.request.NormalUpdateRequest;
import com.lostAndFind.project.model.request.UserUpdateRequest;
import com.lostAndFind.project.service.SysLogLoginService;
import com.lostAndFind.project.service.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 *
 * @author djk
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private UserDetailsServiceImpl userDetailsService;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RedisCache redisCache;

    @Autowired
    SysLogLoginService sysLogLoginService;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "Djk";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 5 || checkPassword.length() < 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        // 2. 加密
//        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(userPassword);
        // 3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encode);
        user.setUserRole("user");
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("userAccount",userAccount);
        User user1 = userMapper.selectOne(queryWrapper1);
        UserRole userRole = new UserRole();
        userRole.setUserId(user1.getId());
        userRole.setRoleId(1L);
        userRoleMapper.insert(userRole);
        return user.getId();
    }

    @Override
    public ResponseResult userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        //日志
        SysLogLogin log = new SysLogLogin();
        log.setOperation(LoginOperationEnum.LOGIN.value());
        DateFormat bf = new SimpleDateFormat("yyyy-MM-dd E a HH:mm:ss");
        log.setCreateDate(bf.format(new Date()));
        log.setIp(IpUtils.getInstance().getIpAddr(request));
        log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        log.setIp(IpUtils.getInstance().getIpAddr(request));

        //使用session存储userAccount
        request.getSession().setAttribute("userAccount",userAccount);

        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            log.setStatus(LoginStatusEnum.FAIL.value());
            log.setCreatorName(userAccount);
            sysLogLoginService.save(log);

            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 6) {

            log.setStatus(LoginStatusEnum.FAIL.value());
            log.setCreatorName(userAccount);
            sysLogLoginService.save(log);
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userAccount,userPassword);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码");
        }
        // 使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String role = loginUser.getUser().getUserRole();
        String jwt = JwtUtil.createJWT(userId);
        //authenticate存入redis
        redisCache.setCacheObject("login:"+userId,loginUser);
        //把token响应给前端
        HashMap<String,String> map = new HashMap<>();
        map.put("token",jwt);
        map.put("role",role);

        log.setStatus(LoginStatusEnum.SUCCESS.value());
        log.setCreatorName(userAccount);
        sysLogLoginService.save(log);
        return new ResponseResult(200,"登陆成功",map);
    }

    @Override
    public ResponseResult miniLogin(String openid) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenid,openid);
        User user = userMapper.selectOne(wrapper);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserAccount(),null,userDetailsService.loadUserByUsername(user.getUsername()).getAuthorities());
//        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
//        if(Objects.isNull(authenticate)){
//            throw new RuntimeException("用户名或密码错误");
//        }
//        // 使用userid生成token

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        String userId = user.getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        LoginUser loginUser = new LoginUser(user, menuMapper.selectPermsByUserId(user.getId()));
        //authenticate存入redis
        redisCache.setCacheObject("login:"+userId,loginUser);
        //把token响应给前端
        HashMap<String,Object> map = new HashMap<>();
        map.put("token",jwt);
        map.put("userInfo",loginUser);
        map.put("openid",openid);
        if (redisCache.getCacheObject("lostList") != null) {
            redisCache.deleteObject("lostList");
        }
        if (redisCache.getCacheObject("pickList") != null) {
            redisCache.deleteObject("pickList");
        }
        return new ResponseResult(200,"登陆成功",map);
    }

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public ResponseResult userLogout(HttpServletRequest request) {
//        //获取SecurityContextHolder中的用户id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取id
        Long id = loginUser.getUser().getId();
        redisCache.deleteObject("login:" + id);


        SysLogLogin log = new SysLogLogin();
        log.setOperation(LoginOperationEnum.LOGOUT.value());
        log.setIp(IpUtils.getInstance().getIpAddr(request));
        log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        log.setStatus(LoginStatusEnum.SUCCESS.value());
        log.setCreatorName(loginUser.getUser().getUsername());
        DateFormat bf = new SimpleDateFormat("yyyy-MM-dd E a HH:mm:ss");
        log.setCreateDate(bf.format(new Date()));
        sysLogLoginService.save(log);


        return new ResponseResult<>(200,"退出成功");
    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && ADMIN_ROLE.equals(user.getUserRole());
    }


    @Override
    public void assertAdmin(HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
    }

    @Override
    public User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取id
        Long id = loginUser.getUser().getId();
        // 先判断是否已登录
        if (id == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        User currentUser = this.getById(id);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    @Override
    public boolean updateInfo(UserUpdateRequest userUpdateRequest) {
        User oldUser = userMapper.selectById(userUpdateRequest.getId());
        BeanUtils.copyProperties(userUpdateRequest, oldUser);
        return this.updateById(oldUser);
    }

    @Override
    public boolean updateNormalInfo(NormalUpdateRequest normalUpdateRequest) {
        User oldUser = userMapper.selectById(normalUpdateRequest.getId());
        BeanUtils.copyProperties(normalUpdateRequest, oldUser);
        return this.updateById(oldUser);
    }

}




