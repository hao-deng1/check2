package com.lostAndFind.project.model.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author djk
 */
@Data
public class RoleUpdateRequest {
    private Long id;

    private String name;

    private String status;

    private String remark;

    private List<String> perms;
}
