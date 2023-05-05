package com.lostAndFind.project.model.request;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author yc_
 * @version 1.0
 */
@Data
public class LostAddRequest {

    /**
     * 报失名称
     */
    private String lostName;

    /**
     * 报失类型json
     */
    private List<String> lostType;

    /**
     * 丢失时间
     */
    private Date lostDate;

    /**
     * 联系电话
     */
    private String contact;

    /**
     * 物品描述
     */
    private String description;

    /**
     * 物品图片
     */
    private String img;

    /**
     * 赏金
     */
    private Double business;

    /**
     * 紧急程度
     */
    private Double urgent;

}
