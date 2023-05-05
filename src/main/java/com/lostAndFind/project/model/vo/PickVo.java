package com.lostAndFind.project.model.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PickVo {

    /**
     * id
     */
    private Long id;

    /**
     * userId
     */
    private String  username;

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

    private List<String> pickType;
}
