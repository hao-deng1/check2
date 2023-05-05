package com.lostAndFind.project.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录日志
 * @TableName sys_log_login
 */
@Data
public class SysLogLogin implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户操作   0：用户登录   1：用户退出
     */
    private Integer operation;

    /**
     * 状态  0：失败    1：成功    2：账号已锁定
     */
    private Integer status;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 操作IP
     */
    private String ip;

    /**
     * 用户名
     */
    private String creatorName;

    /**
     * 创建者
     */
    private Long creator;

    /**
     * 创建时间
     */
    private String createDate;

    private static final long serialVersionUID = 1L;


    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SysLogLogin other = (SysLogLogin) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOperation() == null ? other.getOperation() == null : this.getOperation().equals(other.getOperation()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getUserAgent() == null ? other.getUserAgent() == null : this.getUserAgent().equals(other.getUserAgent()))
            && (this.getIp() == null ? other.getIp() == null : this.getIp().equals(other.getIp()))
            && (this.getCreatorName() == null ? other.getCreatorName() == null : this.getCreatorName().equals(other.getCreatorName()))
            && (this.getCreator() == null ? other.getCreator() == null : this.getCreator().equals(other.getCreator()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOperation() == null) ? 0 : getOperation().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getUserAgent() == null) ? 0 : getUserAgent().hashCode());
        result = prime * result + ((getIp() == null) ? 0 : getIp().hashCode());
        result = prime * result + ((getCreatorName() == null) ? 0 : getCreatorName().hashCode());
        result = prime * result + ((getCreator() == null) ? 0 : getCreator().hashCode());
        result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", operation=").append(operation);
        sb.append(", status=").append(status);
        sb.append(", userAgent=").append(userAgent);
        sb.append(", ip=").append(ip);
        sb.append(", creatorName=").append(creatorName);
        sb.append(", creator=").append(creator);
        sb.append(", createDate=").append(createDate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}