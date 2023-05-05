package com.lostAndFind.project.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 招领物品表
 * @author Asus
 * @TableName pick
 */
@Data
public class Pick implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * userId
     */
    private Long userid;

    /**
     * 失物名称
     */
    private String pickName;

    /**
     * 捡拾时间
     */
    private Date pickDate;

    /**
     * 捡拾地点
     */
    private String pickPlace;

    /**
     * 联系电话
     */
    private String contact;

    /**
     * 上报时间
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
     * 订单状态（0进行 1完成）
     */
    private String status;

    private static final long serialVersionUID = 1L;

}