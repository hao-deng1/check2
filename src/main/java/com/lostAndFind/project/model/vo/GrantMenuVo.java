package com.lostAndFind.project.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GrantMenuVo implements Serializable {
    /**
     * id
     */
    private long roleId;

    /**
    * 权限列表
    */
    private List<Long> menuId;
}
