package com.lostAndFind.project.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户视图
 *
 * @TableName user
 */
@Data
public class UserVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;
    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;
    /**
     * 积分
     */
    private Integer integral = 0;

    /**
     * 暂存积分
     */
    private Integer temporary = 0;

    /**
     * 用户角色: user, admin
     */
    private String userRole;

    /**
     * 创建时间
     */
    private Date createTime;


    private static final long serialVersionUID = 1L;
}