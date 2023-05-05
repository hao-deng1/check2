package com.lostAndFind.project.model.request;

import lombok.Data;

import java.util.Date;

/**
 * @author yc_
 * @version 1.0
 */
@Data
public class PickUpdateRequest {
    private Long id;

    private Long userid;

    private String pickname;

    private String picktype;

    private Date pickdate;

    private String pickplace;

    private String contact;

    private String description;

    private String img;
}
