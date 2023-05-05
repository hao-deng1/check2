package com.lostAndFind.project.model.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author djk
 */
@Data
public class RoleAddRequest {

    private String name;

    private String status;

    private String remark;

    private ArrayList<String> perms;
}
