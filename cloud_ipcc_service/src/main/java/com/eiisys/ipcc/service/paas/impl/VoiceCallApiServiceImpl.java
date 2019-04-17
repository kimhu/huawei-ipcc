package com.eiisys.ipcc.service.paas.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.javassist.bytecode.annotation.StringMemberValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.eiisys.ipcc.bean.huawei.VoiceCallAnswerBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallConfrenceBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallConnectHoldBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallDisconnectBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallDropBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallHoldBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallHoldCancelBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallInnerBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallInnerHelpBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallMuteBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallMuteCancelBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallOutBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallReleaseBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallSecondDailBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallSnatchBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallTransferBean;
import com.eiisys.ipcc.bean.huawei.VoiceCallTransferCancelBean;
import com.eiisys.ipcc.constants.HuaweiConstants;
import com.eiisys.ipcc.core.utils.OkhttpClientUtils;
import com.eiisys.ipcc.service.paas.VoiceCallApiService;

import lombok.extern.slf4j.Slf4j;

/**
 * 座席应用接口> 语音通话 实现类
 * 
 * @author hujm
 */

@Service
@Slf4j
public class VoiceCallApiServiceImpl implements VoiceCallApiService {
    @Value("${huawei.server.version}")
    private String version;
    @Value("${huawei.access.key}")
    private String accessKey;
    @Value("${huawei.secret.key}")
    private String secretKey;

    private String host;
    private final String agentIdParam = "{agentid}";
    
    /** 2.2.1 外呼uri **/
    private final String callOutURI = "/agentgateway/resource/voicecall/{agentid}/callout";
    /** 2.2.2 呼叫应答uri **/
    private final String callAnswerURI = "/agentgateway/resource/voicecall/{agentid}/answer";
    /** 2.2.3 内部呼叫uri **/
    private final String callInnerURI = "/agentgateway/resource/voicecall/{agentid}/callinner";
    /** 2.2.4 内部求助uri **/
    private final String callInnerHelpURI = "/agentgateway/resource/voicecall/{agentid}/innerhelp";
    /** 2.2.5 静音uri **/
    private final String callMuteURI = "/agentgateway/resource/voicecall/{agentid}/beginmute";
    /** 2.2.6 取消静音uri **/
    private final String callMuteCancelURI = "/agentgateway/resource/voicecall/{agentid}/endmute";
    /** 2.2.7 呼叫保持uri **/
    private final String callHoldURI = "/agentgateway/resource/voicecall/{agentid}/hold";
    /** 2.2.8 取消保持uri **/
    private final String callHoldCancelURI = "/agentgateway/resource/voicecall/{agentid}/gethold";
    /** 2.2.9 呼叫转移uri **/
    private final String callTransferURI = "/agentgateway/resource/voicecall/{agentid}/transfer";
    /** 2.2.10 取消转移uri **/
    private final String callTransferCancelURI = "/agentgateway/resource/voicecall/{agentid}/canceltransfer";
    /** 2.2.11 二次拨号(扩展)uri **/
    private final String callSecondDialURI = "/agentgateway/resource/voicecall/{agentid}/seconddialex";
    /** 2.2.12 三方通话uri **/
    private final String callConfrenceURI = "/agentgateway/resource/voicecall/{agentid}/confjoin";
    /** 2.2.13 连接保持uri **/
    private final String callConnectHoldURI = "/agentgateway/resource/voicecall/{agentid}/connecthold/";
    /** 2.2.14 释放指定号码连接uri **/
    private final String callDisconnectURI = "/agentgateway/resource/voicecall/{agentid}/disconnect/";
    /** 2.2.15 拆除指定callid呼叫uri **/
    private final String callDropURI = "/agentgateway/resource/voicecall/{agentid}/dropcall/";
    /** 2.2.16 挂断呼叫uri **/
    private final String callReleaseURI = "/agentgateway/resource/voicecall/{agentid}/release";
    /** 2.2.17 呼叫代答uri **/
    public final String callSnatchURI = "/agentgateway/resource/voicecall/{agentid}/snatchpickup";
    
    public VoiceCallApiServiceImpl(@Value("${huawei.server.agent.header}") String serverHeader,
            @Value("${huawei.server.agent.ip}") String serverIp, @Value("${huawei.server.agent.port}") String port) {
        super();
        this.host = serverHeader.concat(serverIp).concat(":").concat(port);
    }

    /**
     * 2.2.1 外呼
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String callOut(VoiceCallOutBean bean) throws Exception {
        String callOutUri = callOutURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(callOutUri);
        
        log.info("[Call Out] param: {}", JSON.toJSONString(bean));

        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());

        try {
            Map<String, String> resultMap = OkhttpClientUtils.put(url, null, headers, bean, OkhttpClientUtils.TYPE_JSON);
            String result = resultMap.get(HuaweiConstants.RESPOSE_BODY);
            log.info("[Call Out] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Call Out] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.2.2 呼叫应答
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String callAnswer(VoiceCallAnswerBean bean) throws Exception {
        String callAnswerUri = callAnswerURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(callAnswerUri);
        
        log.info("[Call Answer] param: {}", JSON.toJSONString(bean));

        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());

        try {
            Map<String, String> resultMap = OkhttpClientUtils.put(url, null, headers, null, OkhttpClientUtils.TYPE_JSON);
            String result = resultMap.get(HuaweiConstants.RESPOSE_BODY);
            log.info("[Call Answer] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Call Answer] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.2.3 内部呼叫
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String callInner(VoiceCallInnerBean bean) throws Exception {
        String callInnerUri = callInnerURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(callInnerUri);
        
        log.info("[Call Inner] param: {}", JSON.toJSONString(bean));

        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());

        try {
            Map<String, String> resultMap = OkhttpClientUtils.put(url, null, headers, bean, OkhttpClientUtils.TYPE_JSON);
            String result = resultMap.get(HuaweiConstants.RESPOSE_BODY);
            log.info("[Call Inner] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Call Inner] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.2.4 内部求助
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String callInnerHelp(VoiceCallInnerHelpBean bean) throws Exception {
        String callInnerHelpUri = callInnerHelpURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(callInnerHelpUri);
        
        log.info("[Call Inner Help] param: {}", JSON.toJSONString(bean));

        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, bean, OkhttpClientUtils.TYPE_JSON);
            log.info("[Call Inner Help] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Call Inner Help] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.2.5 静音
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String callMute(VoiceCallMuteBean bean) throws Exception {
        String callMuteUri = callMuteURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(callMuteUri);
        
        log.info("[Call Mute] param: {}", JSON.toJSONString(bean));

        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, null, OkhttpClientUtils.TYPE_JSON);
            log.info("[Call Mute] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Call Mute] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.2.6 取消静音
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String callMuteCancel(VoiceCallMuteCancelBean bean) throws Exception {
        String callMuteCancelUri = callMuteCancelURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(callMuteCancelUri);
        
        log.info("[Call Mute Cancel] param: {}", JSON.toJSONString(bean));

        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, null, OkhttpClientUtils.TYPE_JSON);
            log.info("[Call Mute Cancel] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Call Mute Cancel] cancel exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.2.7 呼叫保持
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String callHold(VoiceCallHoldBean bean) throws Exception {
        String callHoldUri = callHoldURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(callHoldUri);
        
        log.info("[Call Hold] param: {}", JSON.toJSONString(bean));

        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, null, OkhttpClientUtils.TYPE_JSON);
            log.info("[Call Hold] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Call Hold] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.2.8 取消保持
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String callHoldCancel(VoiceCallHoldCancelBean bean) throws Exception {
        String callHoldCancelUri = callHoldCancelURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(callHoldCancelUri);
        
        log.info("[Call Hold Cancel] param: {}", JSON.toJSONString(bean));

        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());
        
        /** 请求url参数 **/
        Map<String, String> params = new HashMap<>();
        params.put("callid", bean.getCallid());

        try {
            String result = OkhttpClientUtils.syncPost(url, params, headers, null, OkhttpClientUtils.TYPE_JSON);
            log.info("[Call Hold Cancel] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Call Hold Cancel] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.2.9 呼叫转移
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String callTransfer(VoiceCallTransferBean bean) throws Exception {
        String callTransferUri = callTransferURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(callTransferUri);
        
        log.info("[Call Transfer] param: {}", JSON.toJSONString(bean));

        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, bean, OkhttpClientUtils.TYPE_JSON);
            log.info("[Call Transfer] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Call Transfer] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.2.10 取消转移
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String callTransferCancel(VoiceCallTransferCancelBean bean) throws Exception {
        String callTransferCancelUri = callTransferCancelURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(callTransferCancelUri);
        
        log.info("[Call Transfer Cancel] param: {}", JSON.toJSONString(bean));

        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, null, OkhttpClientUtils.TYPE_JSON);
            log.info("[Call Transfer Cancel] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Call Transfer Cancel] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.2.11 二次拨号(扩展)
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String callSecondDial(VoiceCallSecondDailBean bean) throws Exception {
        String callSecondDialUri = callSecondDialURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(callSecondDialUri);
        
        log.info("[Call Second Dail] param: {}", JSON.toJSONString(bean));

        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());
        
        /** 请求url参数 **/
        Map<String, String> params = new HashMap<>();
        params.put("number", bean.getNumber());

        try {
            String result = OkhttpClientUtils.syncPost(url, params, headers, null, OkhttpClientUtils.TYPE_JSON);
            log.info("[Call Second Dail] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Call Second Dail] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.2.12 三方通话
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String callConfrence(VoiceCallConfrenceBean bean) throws Exception {
        String callConfrenceUri = callConfrenceURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(callConfrenceUri);
        
        log.info("[Call Confrence Join] param: {}", JSON.toJSONString(bean));

        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, bean, OkhttpClientUtils.TYPE_JSON);
            log.info("[Call Confrence Join] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Call Confrence Join] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.2.13 连接保持
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String callConnectHold(VoiceCallConnectHoldBean bean) throws Exception {
        String callConnectHoldUri = callConnectHoldURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        callConnectHoldUri = callConnectHoldUri.concat(bean.getCallid());
        String url = host.concat(callConnectHoldUri);
        
        log.info("[Call Connect Hold] param: {}", JSON.toJSONString(bean));

        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, null, OkhttpClientUtils.TYPE_JSON);
            log.info("[Call Connect Hold] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Call Connect Hold] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.2.14 释放指定号码连接
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String callDisconnect(VoiceCallDisconnectBean bean) throws Exception {
        String callDisconnectUri = callDisconnectURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        callDisconnectUri = callDisconnectUri.concat(bean.getNumber());
        String url = host.concat(callDisconnectUri);
        
        log.info("[Call Disconnect] param: {}", JSON.toJSONString(bean));

        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, null, OkhttpClientUtils.TYPE_JSON);
            log.info("[Call Disconnect] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Call Disconnect] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.2.15 拆除指定callid呼叫
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String callDrop(VoiceCallDropBean bean) throws Exception {
        String callDropUri = callDropURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        callDropUri = callDropUri.concat(bean.getCallid());
        String url = host.concat(callDropUri);
        
        log.info("[Call Drop] param: {}", JSON.toJSONString(bean));

        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, null, OkhttpClientUtils.TYPE_JSON);
            log.info("[Call Drop] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Call Drop] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.2.16 挂断呼叫
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String callRelease(VoiceCallReleaseBean bean) throws Exception {
        String callReleaseUri = callReleaseURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(callReleaseUri);
        
        log.info("[Call Release] param: {}", JSON.toJSONString(bean));

        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, null, OkhttpClientUtils.TYPE_JSON);
            log.info("[Call Release] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Call Release] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.2.17 呼叫代答
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String callSnatch(VoiceCallSnatchBean bean) throws Exception {
        String callSnatchUri = callSnatchURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(callSnatchUri);
        
        log.info("[Call Snatch Pickup] param: {}", JSON.toJSONString(bean));

        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());
        
        /** 请求url参数 **/
        Map<String, String> params = new HashMap<>();
        params.put("destWorkNo", String.valueOf(bean.getDestWorkNo()));

        try {
            Map<String, String> resultMap = OkhttpClientUtils.put(url, params, headers, null, OkhttpClientUtils.TYPE_JSON);
            String result = resultMap.get(HuaweiConstants.RESPOSE_BODY);
            log.info("[Call Snatch Pickup] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Call Snatch Pickup] exception, e: {}", e);
            throw e;
        }
    }
}
