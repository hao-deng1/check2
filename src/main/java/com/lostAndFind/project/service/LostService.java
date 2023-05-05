package com.lostAndFind.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lostAndFind.project.common.BaseResponse;
import com.lostAndFind.project.common.ResponseResult;
import com.lostAndFind.project.model.entity.Lost;
import com.lostAndFind.project.model.query.LostQuery;
import com.lostAndFind.project.model.vo.LostVo;

import java.util.List;

public interface LostService extends IService<Lost> {

    void removeWithLost(List<Long> ids);

    ResponseResult queryAll(LostQuery lostQuery);

    ResponseResult getUrgent();
}
