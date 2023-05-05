package com.lostAndFind.project.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author yc_
 * @version 1.0
 */
@Data
public class PickDto {
    /**
     * id
     */
    private Long id;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 失物名称
     */
    private String pickname;

    /**
     * 捡拾时间
     */
    private Date pickdate;

    /**
     * 捡拾地点
     */
    private String pickplace;

    /**
     * 联系电话
     */
    private String contact;

    /**
     * 上报时间
     */
    private Date reporttime;

    /**
     * 物品描述
     */
    private String description;

    /**
     * 是否删除
     */
    private Integer isdelete;

    /**
     * 物品图片
     */
    private String img;

    /**
     * 订单状态（0进行 1完成）
     */
    private String status;
}
