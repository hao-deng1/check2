package com.lostAndFind.project.model.query;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author yc_
 * @version 1.0
 */
@Data
public class PickQuery {
    private Long id;

    private Long userId;

    /**
     * 失物名称
     */
    private String pickname;

    /**
     * 物品描述
     */
    private String description;
    /**
     * 订单状态（0进行 1完成）
     */
    private String status;

    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 搜索内容
     */
    private String searchText;

}
