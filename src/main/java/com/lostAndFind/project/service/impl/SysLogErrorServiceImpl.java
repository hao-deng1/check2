package com.lostAndFind.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lostAndFind.project.model.entity.SysLogError;
import com.lostAndFind.project.service.SysLogErrorService;
import com.lostAndFind.project.mapper.SysLogErrorMapper;
import org.springframework.stereotype.Service;


/**
* @author Asus
* @description 针对表【sys_log_error(异常日志)】的数据库操作Service实现
* @createDate 2022-11-13 20:38:58
*/
@Service
public class SysLogErrorServiceImpl extends ServiceImpl<SysLogErrorMapper, SysLogError> implements SysLogErrorService{

}
