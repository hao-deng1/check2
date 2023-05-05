package com.lostAndFind.project.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author yc_
 * @version 1.0
 */
@Data
public class LostDto {
    /**
     * id
     */
    private Long id;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 报失名称
     */
    private String lostName;

    /**
     * 丢失时间
     */
    private Date lostDate;

    /**
     * 联系电话
     */
    private String contact;

    /**
     * 报失时间
     */
    private Date reportTime;

    /**
     * 物品描述
     */
    private String description;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 物品图片
     */
    private String img;

    /**
     * 赏金
     */
    private Integer business;

    /**
     * 紧急程度 0-5
     */
    private Double urgent;

    /**
     * 订单状态（0进行 1完成）
     */
    private String status;
}
