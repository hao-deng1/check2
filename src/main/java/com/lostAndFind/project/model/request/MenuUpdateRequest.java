package com.lostAndFind.project.model.request;

import lombok.Data;


/**
 * @author yc_
 * @version 1.0
 */
@Data
public class MenuUpdateRequest {
    private Long id;

    private String menuName;

    private String status;

    private String remark;
}
