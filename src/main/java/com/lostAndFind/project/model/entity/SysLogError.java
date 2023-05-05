package com.lostAndFind.project.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 异常日志
 * @TableName sys_log_error
 */
@Data
public class SysLogError implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 请求URI
     */
    private String requestUri;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 操作IP
     */
    private String ip;

    /**
     * 异常信息
     */
    private String errorInfo;

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
        SysLogError other = (SysLogError) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getRequestUri() == null ? other.getRequestUri() == null : this.getRequestUri().equals(other.getRequestUri()))
            && (this.getRequestMethod() == null ? other.getRequestMethod() == null : this.getRequestMethod().equals(other.getRequestMethod()))
            && (this.getRequestParams() == null ? other.getRequestParams() == null : this.getRequestParams().equals(other.getRequestParams()))
            && (this.getUserAgent() == null ? other.getUserAgent() == null : this.getUserAgent().equals(other.getUserAgent()))
            && (this.getIp() == null ? other.getIp() == null : this.getIp().equals(other.getIp()))
            && (this.getErrorInfo() == null ? other.getErrorInfo() == null : this.getErrorInfo().equals(other.getErrorInfo()))
            && (this.getCreator() == null ? other.getCreator() == null : this.getCreator().equals(other.getCreator()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRequestUri() == null) ? 0 : getRequestUri().hashCode());
        result = prime * result + ((getRequestMethod() == null) ? 0 : getRequestMethod().hashCode());
        result = prime * result + ((getRequestParams() == null) ? 0 : getRequestParams().hashCode());
        result = prime * result + ((getUserAgent() == null) ? 0 : getUserAgent().hashCode());
        result = prime * result + ((getIp() == null) ? 0 : getIp().hashCode());
        result = prime * result + ((getErrorInfo() == null) ? 0 : getErrorInfo().hashCode());
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
        sb.append(", requestUri=").append(requestUri);
        sb.append(", requestMethod=").append(requestMethod);
        sb.append(", requestParams=").append(requestParams);
        sb.append(", userAgent=").append(userAgent);
        sb.append(", ip=").append(ip);
        sb.append(", errorInfo=").append(errorInfo);
        sb.append(", creator=").append(creator);
        sb.append(", createDate=").append(createDate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}