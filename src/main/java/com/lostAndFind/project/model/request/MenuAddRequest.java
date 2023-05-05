package com.lostAndFind.project.model.request;

import lombok.Data;

/**
 * @author djk
 */
@Data
public class MenuAddRequest {

    private String menuName;

    private String perms;

    private String status;

    private Long parentId;
}
