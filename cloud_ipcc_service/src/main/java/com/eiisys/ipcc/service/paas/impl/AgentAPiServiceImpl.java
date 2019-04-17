package com.eiisys.ipcc.service.paas.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.eiisys.ipcc.bean.huawei.AgentAutoAnswerSetBean;
import com.eiisys.ipcc.bean.huawei.AgentAutoIdleSetBean;
import com.eiisys.ipcc.bean.huawei.AgentBulletinBean;
import com.eiisys.ipcc.bean.huawei.AgentBusyCancelBean;
import com.eiisys.ipcc.bean.huawei.AgentBusySetBean;
import com.eiisys.ipcc.bean.huawei.AgentForceLogoutBean;
import com.eiisys.ipcc.bean.huawei.AgentFreeSetBean;
import com.eiisys.ipcc.bean.huawei.AgentHeartbeatBean;
import com.eiisys.ipcc.bean.huawei.AgentLoginBean;
import com.eiisys.ipcc.bean.huawei.AgentLogoutBean;
import com.eiisys.ipcc.bean.huawei.AgentNoteSendBean;
import com.eiisys.ipcc.bean.huawei.AgentRestBean;
import com.eiisys.ipcc.bean.huawei.AgentSkillGetBean;
import com.eiisys.ipcc.bean.huawei.AgentSkillGetByNoBean;
import com.eiisys.ipcc.bean.huawei.AgentSkillResetBean;
import com.eiisys.ipcc.bean.huawei.AgentStatusGetBean;
import com.eiisys.ipcc.bean.huawei.AgentTalkRightSetBean;
import com.eiisys.ipcc.bean.huawei.AgentWorkBean;
import com.eiisys.ipcc.bean.huawei.AgentWorkCancelBean;
import com.eiisys.ipcc.constants.HuaweiConstants;
import com.eiisys.ipcc.core.utils.OkhttpClientUtils;
import com.eiisys.ipcc.service.paas.AgentApiService;

import lombok.extern.slf4j.Slf4j;

/**
 * 华为座席应用接口> 在线座席 实现类
 * 
 * @author hujm
 */
@Service
@Slf4j
public class AgentAPiServiceImpl implements AgentApiService {
    @Value("${huawei.server.version}")
    private String version;
    @Value("${huawei.access.key}")
    private String accessKey;
    @Value("${huawei.secret.key}")
    private String secretKey;

    private String host;
    private final String agentIdParam = "{agentid}";

    /** 2.1.1 登录uri **/
    private final String loginURI = "/agentgateway/resource/onlineagent/{agentid}";
    /** 2.1.2 强制登录uri **/
    private final String forceLoginURI = "/agentgateway/resource/onlineagent/{agentid}/forcelogin";
    /** 2.1.3 心跳检测uri **/
    private final String hearbeatURI = "/agentgateway/resource/onlineagent/{agentid}/heartbeat";
    /** 2.1.4 查询配置技能队列uri **/
    private final String getAgentSkillURI = "/agentgateway/resource/onlineagent/{agentid}/agentskills";
    /** 2.1.5 查询指定座席配置技能队列uri **/
    private final String getAgentSkillByNoURI = "/agentgateway/resource/onlineagent/{agentid}/agentskillsbyworkno/";
    /** 2.1.6 设置是否自应答uri **/
    private final String setAutoAnswerURI = "/agentgateway/resource/onlineagent/{agentid}/autoanswer/";
    /** 2.1.7 重置技能队列uri **/
    private final String resetSkillURI = "/agentgateway/resource/onlineagent/{agentid}/resetskill/";
    /** 2.1.8 示闲uri **/
    private final String setFreeURI = "/agentgateway/resource/onlineagent/{agentid}/sayfree";
    /** 2.1.9 示忙uri **/
    private final String setBusyURI = "/agentgateway/resource/onlineagent/{agentid}/saybusy";
    /** 2.1.10 取消示忙uri **/
    private final String cancelBusyURI = "/agentgateway/resource/onlineagent/{agentid}/cancelbusy";
    /** 2.1.11 请求休息uri **/
    private final String restURI = "/agentgateway/resource/onlineagent/{agentid}/rest/";
    /** 2.1.12 取消休息uri **/
    private final String cancelRestURI = "/agentgateway/resource/onlineagent/{agentid}/cancelrest";
    /** 2.1.13 进入工作态uri **/
    private final String requestWorkURI = "/agentgateway/resource/onlineagent/{agentid}/work";
    /** 2.1.14 退出工作态uri **/
    private final String cancelWorkURI = "/agentgateway/resource/onlineagent/{agentid}/cancelwork";
    /** 2.1.15 设置是否进入空闲态uri **/
    private final String setAutoIdleURI = "/agentgateway/resource/onlineagent/{agentid}/autoenteridle/";
    /** 2.1.16 座席发布公告uri **/
    private final String notifyBulletinURI = "/agentgateway/resource/onlineagent/{agentid}/notifybulletin";
    /** 2.1.17 发送便签(扩展)uri **/
    private final String sendNoteTextURI = "/agentgateway/resource/onlineagent/{agentid}/sendnoteletex";
    /** 2.1.18 签出uri **/
    private final String logoutURI = "/agentgateway/resource/onlineagent/{agentid}/logout";
    /** 2.1.19 强制签出带原因码uri **/
    private final String forceLogoutURI = "/agentgateway/resource/onlineagent/{agentid}/forcelogoutwithreason/";
    /** 2.1.20 设置是否接听来话uri **/
    private final String setTalkRightURI = "/agentgateway/resource/onlineagent/{agentid}/settalkright";
    /** 2.1.21 获取当前座席的状态uri **/
    private final String getAgentStatusURI = "/agentgateway/resource/onlineagent/{agentid}/agentstatus";

    public AgentAPiServiceImpl(@Value("${huawei.server.agent.header}") String serverHeader,
            @Value("${huawei.server.agent.ip}") String serverIp, @Value("${huawei.server.agent.port}") String port) {
        super();
        this.host = serverHeader.concat(serverIp).concat(":").concat(port);
    }

    /**
     * 2.1.1 坐席登录
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public Map<String, String> login(AgentLoginBean bean) throws Exception {
        String loginUri = loginURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(loginUri);

        log.info("[Login] param: {}", JSON.toJSONString(bean));
        
        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);

        try {
            Map<String, String> result = OkhttpClientUtils.put(url, null, headers, bean, OkhttpClientUtils.TYPE_JSON);
            log.info("[Login] response: {}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("[Login] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.1.2 强制席登录
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public Map<String, String> forceLogin(AgentLoginBean bean) throws Exception {
        String forceLoginUri = forceLoginURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(forceLoginUri);
        
        log.info("[Force Login] param: {}", JSON.toJSONString(bean));
        
        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);

        try {
            Map<String, String> result = OkhttpClientUtils.put(url, null, headers, bean, OkhttpClientUtils.TYPE_JSON);
            log.info("[Force Login] response: {}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("[Force Login] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.1.3 心跳检测
     * 
     * @param guid
     * @throws Exception
     */
    @Override
    public String heartbeat(AgentHeartbeatBean bean) throws Exception {
        String heartbeatUri = hearbeatURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(heartbeatUri);
        
        log.info("[Heart Beat] param: {}", JSON.toJSONString(bean));
        
        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());

        try {
            Map<String, String> response = OkhttpClientUtils.put(url, null, headers, null, OkhttpClientUtils.TYPE_JSON);
            String result = response.get(HuaweiConstants.RESPOSE_BODY);
            log.info("[Heart Beat] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Heart Beat] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.1.4 查询配置技能队列
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String getAgentSkill(AgentSkillGetBean bean) throws Exception {
        String getAgentSkillUri = getAgentSkillURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(getAgentSkillUri);
        
        log.info("[Get Agent Skill] param: {}", JSON.toJSONString(bean));
        
        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());

        try {
            String result = OkhttpClientUtils.get(url, null, headers);
            log.info("[Get Agent Skill] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Get Agent Skill] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.1.5 查询指定座席配置技能队列
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String getAgentSkillByNo(AgentSkillGetByNoBean bean) throws Exception {
        String getAgentSkillByNoUri = getAgentSkillByNoURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        getAgentSkillByNoUri = getAgentSkillByNoUri.concat(bean.getWorkNo());
        String url = host.concat(getAgentSkillByNoUri);
        
        log.info("[Get Agent Skill By No] param: {}", JSON.toJSONString(bean));
        
        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());

        try {
            String result = OkhttpClientUtils.get(url, null, headers);
            log.info("[Get Agent Skill By No] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Get Agent Skill By No] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.1.6 设置是否自应答
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String setAutoAnswer(AgentAutoAnswerSetBean bean) throws Exception {
        String setAutoAnswerUri = setAutoAnswerURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        setAutoAnswerUri = setAutoAnswerUri.concat(bean.getIsautoanswer());
        String url = host.concat(setAutoAnswerUri);
        
        log.info("[Set Auto Answer] param: {}", JSON.toJSONString(bean));
        
        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, null, OkhttpClientUtils.TYPE_JSON);
            log.info("[Set Auto Answer] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Set Auto Answer] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.1.7 重置技能队列 
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String resetSkill(AgentSkillResetBean bean) throws Exception {
        String resetSkillUri = resetSkillURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        resetSkillUri = resetSkillUri.concat(bean.getAutoflag());
        String url = host.concat(resetSkillUri);
        
        log.info("[Reset Skill] param: {}", JSON.toJSONString(bean));
        
        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());
        
        /** 请求url参数 **/
        Map<String, String> params = new HashMap<>();
        params.put("skillid", bean.getSkillid());
        params.put("phonelinkage", String.valueOf(bean.getPhonelinkage()));

        try {
            String result = OkhttpClientUtils.syncPost(url, params, headers, null, OkhttpClientUtils.TYPE_JSON);
            log.info("[Reset Skill] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Reset Skill] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.1.8 示闲
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String setFree(AgentFreeSetBean bean) throws Exception {
        String setFreeUri = setFreeURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(setFreeUri);
        
        log.info("[Set Free] param: {}", JSON.toJSONString(bean));
        
        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());
        
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, null, OkhttpClientUtils.TYPE_JSON);
            log.info("[Set Free] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Set Free] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.1.9 示忙
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String setBusy(AgentBusySetBean bean) throws Exception {
        String setBusyUri = setBusyURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(setBusyUri);
        
        log.info("[Set Busy] param: {}", JSON.toJSONString(bean));
        
        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());
        
        /** 请求url参数 **/
        Map<String, String> params = new HashMap<>();
        params.put("reason", String.valueOf(bean.getReason()));
        
        try {
            String result = OkhttpClientUtils.syncPost(url, params, headers, null, OkhttpClientUtils.TYPE_JSON);
            log.info("[Set Busy] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Set Busy] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.1.10 取消示忙
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String cancelBusy(AgentBusyCancelBean bean) throws Exception {
        String cancelBusyUri = cancelBusyURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(cancelBusyUri);
        
        log.info("[Cancel Busy] param: {}", JSON.toJSONString(bean));
        
        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());
        
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, null, OkhttpClientUtils.TYPE_JSON);
            log.info("[Cancel Busy] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Cancel Busy] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.1.11 请求休息
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String requetRest(AgentRestBean bean) throws Exception {
        String restUri = restURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        restUri = restUri.concat(String.valueOf(bean.getTime())).concat("/").concat(String.valueOf(bean.getRestcause()));
        String url = host.concat(restUri);
        
        log.info("[Request Rest] param: {}", JSON.toJSONString(bean));
        
        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());
        
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, null, OkhttpClientUtils.TYPE_JSON);
            log.info("[Request Rest] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Request Rest] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.1.12 取消休息
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String cancelRest(AgentBusyCancelBean bean) throws Exception {
        String cancelRestUri = cancelRestURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(cancelRestUri);
        
        log.info("[Cancel Rest] param: {}", JSON.toJSONString(bean));
        
        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());
        
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, null, OkhttpClientUtils.TYPE_JSON);
            log.info("[Cancel Rest] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Cancel Rest] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.1.13 进入工作态 
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String requestWork(AgentWorkBean bean) throws Exception {
        String requestWorkUri = requestWorkURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(requestWorkUri);
        
        log.info("[Request Work] param: {}", JSON.toJSONString(bean));
        
        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());
        
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, null, OkhttpClientUtils.TYPE_JSON);
            log.info("[Request Work] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Request Work] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.1.14 退出工作态
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String cancelWork(AgentWorkCancelBean bean) throws Exception {
        String cancelWorkUri = cancelWorkURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(cancelWorkUri);
        
        log.info("[Cancel Work] param: {}", JSON.toJSONString(bean));
        
        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());
        
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, null, OkhttpClientUtils.TYPE_JSON);
            log.info("[Cancel Work] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Cancel Work] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.1.15 设置是否进入空闲态
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String setAutoIdle(AgentAutoIdleSetBean bean) throws Exception {
        String setAutoIdelUri = setAutoIdleURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        setAutoIdelUri = setAutoIdelUri.concat(bean.getFlag());
        String url = host.concat(setAutoIdelUri);
        
        log.info("[Set Auto Idle] param: {}", JSON.toJSONString(bean));
        
        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());
        
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, null, OkhttpClientUtils.TYPE_JSON);
            log.info("[Set Auto Idle] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Set Auto Idle] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.1.16 座席发布公告
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String notifyBulletin(AgentBulletinBean bean) throws Exception {
        String notifyBulletinUri = notifyBulletinURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(notifyBulletinUri);
        
        log.info("[Notify Bulletin] param: {}", JSON.toJSONString(bean));
        
        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());
        
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, bean, OkhttpClientUtils.TYPE_JSON);
            log.info("[Notify Bulletin] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Notify Bulletin] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.1.17 发送便签(扩展)
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String sendNoteText(AgentNoteSendBean bean) throws Exception {
        String sendNoteTextUri = sendNoteTextURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(sendNoteTextUri);
        
        log.info("[Send Note] param: {}", JSON.toJSONString(bean));
        
        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());
        
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, bean, OkhttpClientUtils.TYPE_JSON);
            log.info("[Send Note] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Send Note] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.1.18 签出
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String logout(AgentLogoutBean bean) throws Exception {
        String logoutUri = logoutURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(logoutUri);
        
        log.info("[Logout] param: {}", JSON.toJSONString(bean));
        
        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());
        
        try {
            String result = OkhttpClientUtils.delete(url, null, headers, null);
            log.info("[Logout] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Logout] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.1.19 强制签出带原因码
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String forceLogout(AgentForceLogoutBean bean) throws Exception {
        String forceLogoutUri = forceLogoutURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        forceLogoutUri = forceLogoutUri.concat(String.valueOf(bean.getReason()));
        String url = host.concat(forceLogoutUri);
        
        log.info("[Force Logout] param: {}", JSON.toJSONString(bean));
        
        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());
        
        try {
            String result = OkhttpClientUtils.delete(url, null, headers, null);
            log.info("[Force Logout] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Force Logout] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.1.20 设置是否接听来话
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String setTalkRight(AgentTalkRightSetBean bean) throws Exception {
        String setTalkRightUri = setTalkRightURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(setTalkRightUri);
        
        log.info("[Set Talk Righty] param: {}", JSON.toJSONString(bean));
        
        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());
        
        /** 请求url参数 **/
        Map<String, String> params = new HashMap<>();
        params.put("flag", String.valueOf(bean.getFlag()));
        
        try {
            String result = OkhttpClientUtils.syncPost(url, params, headers, null, OkhttpClientUtils.TYPE_JSON);
            log.info("[Set Talk Right] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Set Talk Right] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 2.1.21 获取当前座席的状态
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String getAgentStatus(AgentStatusGetBean bean) throws Exception {
        String getAgentStatusUri = getAgentStatusURI.replace(agentIdParam, String.valueOf(bean.getAgentid()));
        String url = host.concat(getAgentStatusUri);
        
        log.info("[Get Agent Status] param: {}", JSON.toJSONString(bean));
        
        /** 请求headers参数 **/
        Map<String, String> headers = new HashMap<>();
        headers.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        headers.put(HuaweiConstants.HEAD_GUID, bean.getGuid());
        
        try {
            String result = OkhttpClientUtils.get(url, null, headers);
            log.info("[Get Agent Status] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Get Agent Status] exception, e: {}", e);
            throw e;
        }
    }
}
