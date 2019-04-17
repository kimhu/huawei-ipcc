package com.eiisys.ipcc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.eiisys.ipcc.bean.huawei.VoiceCallOutBean;
import com.eiisys.ipcc.constants.MsgConstants;
import com.eiisys.ipcc.constants.RedisConstants;
import com.eiisys.ipcc.core.util.RedisClient;
import com.eiisys.ipcc.core.utils.ResponseUtils;
import com.eiisys.ipcc.exception.IpccException;
import com.eiisys.ipcc.service.VoiceCallService;
import com.eiisys.ipcc.service.paas.VoiceCallApiService;

import lombok.extern.slf4j.Slf4j;

/**
 * 语音通话接口
 * 
 * @author hujm
 */

@Service
@Slf4j
public class VoiceCallImpl implements VoiceCallService{
    @Autowired
    private RedisClient<String> redisClient;
    
    @Autowired
    private VoiceCallApiService voiceService;

    @Override
    public void callOut(Integer id6d, String callee) throws IpccException {
        String redisGuidKey = RedisConstants.RKEY_AGENT_GUID + id6d;
        String redisAgentIdKey = RedisConstants.RKEY_AGNET_ID + id6d;
        if (!redisClient.hasKey(redisGuidKey) || !redisClient.hasKey(redisAgentIdKey)) {
            throw ResponseUtils.genIpccException(MsgConstants.MSG_PAAS_NOT_LOGINED);
        }
        
        String guid = redisClient.get(redisGuidKey);
        String agentId = redisClient.get(redisAgentIdKey);
        
        String paasResult;
        try {
            VoiceCallOutBean bean = new VoiceCallOutBean();
            bean.setAgentid(Integer.valueOf(agentId));
            bean.setGuid(guid);
            bean.setCalled(callee);
            
            paasResult = voiceService.callOut(bean);
        } catch (Exception e) {
            log.error("[Callout Service] exception: {}", e);
            throw ResponseUtils.genIpccException(MsgConstants.MSG_PAAS_CALLOUT_FAILED);
        }
        
        JSONObject jsonObject = JSONObject.parseObject(paasResult);
        String statusCode = jsonObject.getString("retcode");
        String errorCode = null;
        switch (statusCode) {
        case "0":
            break;
        case "000-002":
            /**参数不合法**/
            errorCode = MsgConstants.MSG_PAAS_PARAM_INVALID;
            break;
        case "000-003":
            errorCode = MsgConstants.MSG_PAAS_GUID_ERROR;
            break;
        case "100-006":
            /** 删除坐席guid和agentId信息 **/
            redisClient.delete(redisGuidKey);
            redisClient.delete(redisAgentIdKey);
            errorCode = MsgConstants.MSG_PAAS_NOT_LOGINED;
            break;
        case "200-001":
            /**号码错误**/
            errorCode = MsgConstants.MSG_PAAS_CALLOUT_NUM_ERROR;
            break;
        default:
            errorCode = MsgConstants.MSG_PAAS_CALLOUT_FAILED;
            break;
        }
        
        if (null != errorCode) {
            throw ResponseUtils.genIpccException(errorCode);
        }
    }
}
