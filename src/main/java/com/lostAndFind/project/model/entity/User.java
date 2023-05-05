package com.lostAndFind.project.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户名
     */
    private String username;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 状态 0 - 正常
     */
    private Integer userStatus;

    /**
     * 积分
     */
    private Integer integral = 0;

    /**
     * 暂存积分
     */
    private Integer temporary = 0;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户角色 0 - 普通用户 1 - 管理员
     */
    private String userRole;

    /**
     * openid
     */
    private String openid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}