package com.lostAndFind.project.common;

/**
 * 错误码
 *
 * @author djk
 */
public enum ErrorCode {

    SUCCESS(0, "ok", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求数据为空", ""),
    SECRET_ERROR(40002, "用户名或密码错误", ""),
    NOT_LOGIN_ERROR(40100, "未登录", ""),
    NOT_USER_ERROR(40101, "用户不存在", ""),
    NO_AUTH_ERROR(40101, "无权限",""),
    SYSTEM_ERROR(50000, "系统内部异常", ""),
    OPERATION_ERROR(50001, "操作失败",""),
    SAVE_ERROR(50010, "新增失败", ""),
    UPDATE_ERROR(50020, "更新失败", ""),
    DELETE_ERROR(50030, "删除失败", "");


    private final int code;

    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码描述（详情）
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
