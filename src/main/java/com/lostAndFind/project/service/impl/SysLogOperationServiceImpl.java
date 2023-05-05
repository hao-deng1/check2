package com.lostAndFind.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lostAndFind.project.model.entity.SysLogOperation;
import com.lostAndFind.project.service.SysLogOperationService;
import com.lostAndFind.project.mapper.SysLogOperationMapper;
import org.springframework.stereotype.Service;

/**
* @author Asus
* @description 针对表【sys_log_operation(操作日志)】的数据库操作Service实现
* @createDate 2022-11-13 20:39:07
*/
@Service
public class SysLogOperationServiceImpl extends ServiceImpl<SysLogOperationMapper, SysLogOperation>
implements SysLogOperationService{

}
