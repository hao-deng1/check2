package com.lostAndFind.project.model.request;

import lombok.Data;

/**
 * @author djk
 */
@Data
public class UserUpdateRequest {

    private Long id;

    private String username;

    private String userAccount;

    private String userPassword;

    private Integer gender;

    private Integer userStatus;

    private Integer temporary = 0;

    private String avatarUrl;

}
