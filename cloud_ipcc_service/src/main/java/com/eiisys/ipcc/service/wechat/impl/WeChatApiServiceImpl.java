package com.eiisys.ipcc.service.wechat.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.eiisys.ipcc.constants.RedisConstants;
import com.eiisys.ipcc.core.util.RedisClient;
import com.eiisys.ipcc.core.utils.OkhttpClientUtils;
import com.eiisys.ipcc.core.utils.TimeUtils;
import com.eiisys.ipcc.service.wechat.WeChatApiService;

import lombok.extern.slf4j.Slf4j;

/**
 * 微信API - 告警机制
 * 
 * @author hujm
 */

@Service
@Slf4j
public class WeChatApiServiceImpl implements WeChatApiService {
    private final String apiURL = "https://api.weixin.qq.com/cgi-bin/";
    private final String redisKey = RedisConstants.WECHAT_ACCESS_TOKEN + "token";
    
    @Value("${wechat.access.key}")
    private String accessKey;
    @Value("${wechat.secret.key}")
    private String secretKey;
    @Value("${wechat.warn.openid}")
    private String openIds;
    @Value("${wechat.template.id}")
    private String templateId;
    
    @Autowired
    private RedisClient<String> redisClient;
    
    /**
     * 发送告警信息
     * 
     * @param errorFrom 产生错误平台，saas或者华为
     * @param functionName 产生错误原因
     * @param errorMessage 错误信息描述
     * @param companyName 产生错误的公司
     */
    @Override
    public String sendMessage(String errorFrom, String functionName, String errorMessage, String companyName) {
        String result = null;
        String token = redisClient.get(redisKey);
        if (token == null) {
            token = getToken();
        }
        
        if (token != null) {
            String url = apiURL.concat("message/template/send?access_token=" + token);
            
            Map<String, Map<String, String>> params = new HashMap<String, Map<String, String>>();
            
            Map<String, String> firstParam = new HashMap<>();
            firstParam.put("value", "请求异常，请尽快处理");
            firstParam.put("color", "#173177");
            params.put("first", firstParam);
            
            Map<String, String> key1Param = new HashMap<>();
            key1Param.put("value", errorFrom);
            key1Param.put("color", "#173177");
            params.put("keyword1", key1Param);
            
            Map<String, String> key2Param = new HashMap<>();
            key2Param.put("value", functionName);
            key2Param.put("color", "#173177");
            params.put("keyword2", key2Param);
            
            Map<String, String> key3Param = new HashMap<>();
            key3Param.put("value", TimeUtils.datetimeToString(new Date()));
            key3Param.put("color", "#173177");
            params.put("keyword3", key3Param);
            
            Map<String, String> key4Param = new HashMap<>();
            key4Param.put("value", errorMessage);
            key4Param.put("color", "#173177");
            params.put("keyword4", key4Param);
            
            Map<String, String> key5Param = new HashMap<>();
            key5Param.put("value", companyName);
            key5Param.put("color", "#173177");
            params.put("keyword5", key5Param);
            
            try {
                if (openIds.contains(",")) {
                    String[] openIdArr = openIds.split(",");
                    for(String openId : openIdArr) {
                        JSONObject json = new JSONObject();
                        json.put("touser", openId);
                        json.put("template_id", templateId);
                        json.put("data", params);
                        result += OkhttpClientUtils.syncPost(url, json, OkhttpClientUtils.TYPE_JSON);
                        log.info("[Wechat Service] send warn message to {} response: {}", openId, result);
                    }
                } else {
                    JSONObject json = new JSONObject();
                    json.put("touser", openIds);
                    json.put("template_id", templateId);
                    json.put("data", params);
                    result = OkhttpClientUtils.syncPost(url, json, OkhttpClientUtils.TYPE_JSON);
                    log.info("[Wechat Service] send warn message to {} response: {}", openIds, result);
                }
            } catch (Exception e) {
                log.error("[Wechat Service] send warn message failed, e: {}", e);
            }
        }
        return result;
    }
    
    private String getToken() {
        int retryCount = 3; // 最大重试次数，防止进入死循环
        String token = null;
        while (token == null && retryCount > 0) {
            try {
                token = getAccessToken();
            } catch (Exception e) {
                log.error("[Wechat Service] get wechat token fail. {}", e);
            } finally {
                retryCount--;
            }
        }
        
        if (token == null) {
            log.error("[Wechat Service] get access token failed");
        }
        return token;
    }

    /**
     * 获取微信token
     * @return
     */
    private String getAccessToken() {
        String url = apiURL.concat("token?grant_type=client_credential&appid=")
                .concat(accessKey).concat("&secret=").concat(secretKey);
        
        Map<String, String> headers = getHeaders();
        try {
            String result = OkhttpClientUtils.syncPost(url, headers, OkhttpClientUtils.TYPE_JSON);
            
            log.info("[Wechat Service] get token response: {}", result);
            
            JSONObject object = JSONObject.parseObject(result);
            String token = object.getString("access_token");
            Long expires = object.getLong("expires_in");
            
            if (!StringUtils.isEmpty(token)) {
                redisClient.set(redisKey, token, expires - 200);
            } 
            
            return token;
        }catch (Exception e) {
            log.error("[Wechat Service] get access token exception, e:{}", e);
        }
        
        return null;
    }
    
    /**
     * 封装头部信息
     */
    private Map<String, String> getHeaders(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        headers.put("strictSSL", "false");
        headers.put("rejectUnauthorized", "false");
        headers.put("Connection", "Keep-Alive");
        headers.put("Accept", "*/*");
        
        return headers;
    }
}
