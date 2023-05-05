package com.lostAndFind.project.model.query;

import lombok.Data;

import java.util.List;

/**
 * @author yc_
 * @version 1.0
 */
@Data
public class LostQuery {

    private Long id;

    private Long userId;
    /**
     * 报失名称
     */
    private String lostName;

    /**
     * 物品描述
     */
    private String description;

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
