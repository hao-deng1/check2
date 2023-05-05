package com.lostAndFind.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lostAndFind.project.common.ResponseResult;
import com.lostAndFind.project.model.entity.Pick;
import com.lostAndFind.project.model.query.PickQuery;

import java.util.List;

/**
 * @author Asus
 */
public interface PickService extends IService<Pick> {

    void removeWithPick(List<Long> ids);

    ResponseResult queryAll(PickQuery pickQuery);
}
