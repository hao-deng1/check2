package com.lostAndFind.project.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GrantVo implements Serializable {
    /**
     * id
     */
    private long userId;

    /**
     * 权限
     */
    private List<Long> roleIds;
}
