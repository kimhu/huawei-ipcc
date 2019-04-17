package com.eiisys.ipcc.service.wechat;

/**
 * 微信API - 告警机制
 * 
 * @author hujm
 */
public interface WeChatApiService {
    /** 发送告警信息 **/
    public String sendMessage(String errorFrom, String functionName, String errorMessage, String companyName);
}
