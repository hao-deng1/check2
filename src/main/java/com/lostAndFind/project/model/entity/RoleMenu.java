package com.lostAndFind.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName sys_role_menu
 */
@TableName(value ="sys_role_menu")
@Data
public class RoleMenu implements Serializable {


    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色ID
     */

    private Long roleId;

    /**
     * 菜单id
     */
    private Long menuId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}