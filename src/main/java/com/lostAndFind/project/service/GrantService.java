package com.lostAndFind.project.service;


import com.lostAndFind.project.common.ResponseResult;
import com.lostAndFind.project.model.vo.GrantMenuVo;
import com.lostAndFind.project.model.vo.GrantVo;

import java.util.List;

public interface GrantService {

    ResponseResult grant(GrantVo user);

    ResponseResult grantMenu(GrantMenuVo grantMenuVo);

    List<Long> listPermsId(List<String> perms);
}
