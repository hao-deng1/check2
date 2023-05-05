package com.lostAndFind.project.model.request;

import lombok.Data;


/**
 * @author djk
 */
@Data
public class NormalUpdateRequest {
    private Long id;

    private String username;

    private String avatarUrl;

    private String phone;

    private String email;
}
