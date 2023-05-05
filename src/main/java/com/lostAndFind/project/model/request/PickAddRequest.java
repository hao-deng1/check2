package com.lostAndFind.project.model.request;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author yc_
 * @version 1.0
 */
@Data
public class PickAddRequest {
    private String pickName;

    private List<String> pickType;

    private Date pickDate;

    private String pickPlace;

    private String contact;

    private String description;

    private String img;
}
