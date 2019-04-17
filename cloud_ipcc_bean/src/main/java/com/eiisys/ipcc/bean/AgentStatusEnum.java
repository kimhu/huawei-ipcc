package com.eiisys.ipcc.bean;

/**
 * 坐席状态
 * 
 * @author hujm
 */
public enum AgentStatusEnum {
    OFFLINE(0, "未知状态"),
    FREE(1, "空闲状态"),
    REST(2, "休息状态"),
    BUSY(3, "忙碌状态"),
    WORKING(4, "工作态"),
    CHAT(5, "通话状态");

    /** 用户状态码. */
    private final int code;
    /** 对应的华为操作员状态 */
    private final String status;

    private AgentStatusEnum(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }
}
