package com.lostAndFind.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lostAndFind.project.model.entity.SysLogLogin;
import com.lostAndFind.project.service.SysLogLoginService;
import com.lostAndFind.project.mapper.SysLogLoginMapper;
import org.springframework.stereotype.Service;

/**
* @author Asus
* @description 针对表【sys_log_login(登录日志)】的数据库操作Service实现
* @createDate 2022-11-13 20:39:02
*/
@Service
public class SysLogLoginServiceImpl extends ServiceImpl<SysLogLoginMapper, SysLogLogin>
implements SysLogLoginService{

}
