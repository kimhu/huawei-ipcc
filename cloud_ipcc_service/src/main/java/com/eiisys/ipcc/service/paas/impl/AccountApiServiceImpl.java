package com.eiisys.ipcc.service.paas.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.eiisys.ipcc.bean.huawei.AccountAgentCreateBean;
import com.eiisys.ipcc.bean.huawei.AccountAgentDeleteBean;
import com.eiisys.ipcc.bean.huawei.AccountAgentsInfoGetBean;
import com.eiisys.ipcc.bean.huawei.AccountBaseRequest;
import com.eiisys.ipcc.bean.huawei.AccountBoardNumberGetBean;
import com.eiisys.ipcc.bean.huawei.AccountBoardNumberLockBean;
import com.eiisys.ipcc.bean.huawei.AccountCallConfigDeleteBean;
import com.eiisys.ipcc.bean.huawei.AccountCallConfigGetBean;
import com.eiisys.ipcc.bean.huawei.AccountCallConfigUpdateBean;
import com.eiisys.ipcc.bean.huawei.AccountCreateBean;
import com.eiisys.ipcc.bean.huawei.AccountGetBean;
import com.eiisys.ipcc.bean.huawei.AccountInitBean;
import com.eiisys.ipcc.bean.huawei.AccountIvrFlowGetBean;
import com.eiisys.ipcc.bean.huawei.AccountRoleInfoGetBean;
import com.eiisys.ipcc.bean.huawei.AccountRoleUpdateBean;
import com.eiisys.ipcc.bean.huawei.AccountRolesInfoGetBean;
import com.eiisys.ipcc.bean.huawei.AccountServerInfoGetBean;
import com.eiisys.ipcc.bean.huawei.AccountSkillAgentAddBean;
import com.eiisys.ipcc.bean.huawei.AccountSkillCreateBean;
import com.eiisys.ipcc.bean.huawei.AccountSkillDeleteBean;
import com.eiisys.ipcc.bean.huawei.AccountSkillsInfoGetBean;
import com.eiisys.ipcc.bean.huawei.AccountStatusUpdateBean;
import com.eiisys.ipcc.bean.huawei.AccountUpdateBean;
import com.eiisys.ipcc.bean.huawei.AccountsGetBean;
import com.eiisys.ipcc.constants.HuaweiConstants;
import com.eiisys.ipcc.core.huawei.utils.AuthorizationUtils;
import com.eiisys.ipcc.core.utils.OkhttpClientUtils;
import com.eiisys.ipcc.service.paas.AccountApiService;

import lombok.extern.slf4j.Slf4j;

/**
 * 华为运营管理接口实现类
 * 
 * @author hujm
 */

@Service
@Slf4j
public class AccountApiServiceImpl implements AccountApiService {
    /** 1.2.1 创建企业uri **/
    private final String createAccountURI = "/SAAS/resource/cloud/account/initAccount";
    /** 1.2.2 查询接入码uri **/
    private final String getNumberURI = "/SAAS/resource/cloud/account/get400No";
    /** 1.2.3 锁定接入码uri **/
    private final String lockNumberURI = "/SAAS/resource/cloud/account/lock400No";
    /** 1.2.4 初始化服务uri **/
    private final String initAccountURI = "/SAAS/resource/cloud/account/initOrder";
    /** 1.2.5 变更服务uri **/
    private final String updateAccountURI = "/SAAS/resource/cloud/account/updateOrder";
    /** 1.2.6 查询基本信息uri **/
    private final String getAccountURI = "/SAAS/resource/cloud/account/getAccountInfo";
    /** 1.2.7 更改服务状态uri **/
    private final String updateStatusURI = "/SAAS/resource/cloud/account/updateAccountStatus";
    /** 1.2.8 查询服务器信息uri **/
    private final String getServerInfoURI = "/SAAS/resource/cloud/account/getServerInfo";
    /** 1.2.9 查询企业帐号uri **/
    private final String getAccoutsURI = "/SAAS/resource/cloud/account/getAllAccount";
    /** 1.2.10 查询被叫配置uri **/
    private final String getCallConfigURI = "/SAAS/resource/cloud/dn/queryAccessCodeDn";
    /** 1.2.11 更新被叫配置uri **/
    private final String updateCallConfigURI = "/SAAS/resource/cloud/dn/updateAccessCodeDn";
    /** 1.2.12 删除企业被叫配置uri **/
    private final String deleteCallConfigURI = "/SAAS/resource/cloud/dn/deleteAccessCodeDn";
    /** 1.3.1 查询指定座席角色uri **/
    private final String getRoleInfoURI = "/SAAS/resource/cloud/agent/getRoleInfo";
    /** 1.3.2 查询所有座席角色uri **/
    private final String getRolesInfoURI = "/SAAS/resource/cloud/agent/getAllRoleInfo";
    /** 1.3.3 创建技能队列uri **/
    private final String createSkillURI = "/SAAS/resource/cloud/agent/createSkill";
    /** 1.3.4 查询所有技能队列uri **/
    private final String getSkillsInfoURI = "/SAAS/resource/cloud/agent/getSkillInfo";
    /** 1.3.5 删除指定技能队列uri **/
    private final String deleteSkillURI = "/SAAS/resource/cloud/agent/deleteSkill";
    /** 1.3.6 增加座席uri **/
    private final String createAgentURI = "/SAAS/resource/cloud/agent/createAgent";
    /** 1.3.7 设置座席技能队列uri **/
    private final String setAgentSkillURI = "/SAAS/resource/cloud/agent/setAgentSkill";
    /** 1.3.8 更换座席角色uri **/
    private final String setAgentRoleURI = "/SAAS/resource/cloud/agent/setAgentRole";
    /** 1.3.9 删除座席uri **/
    private final String deleteAgentURI = "/SAAS/resource/cloud/agent/deleteAgent";
    /** 1.3.10 查询所有座席信息uri **/
    private final String getAgentsInfoURI = "/SAAS/resource/cloud/agent/getAllAgentInfo";
    /** 1.4.1 查询 IVR流程uri **/
    private final String getIvrFlowURI = "/SAAS/resource/cloud/ivr/queryIvrFlowInfo";

    @Value("${huawei.server.account.header}")
    private String serverHeader;
    @Value("${huawei.server.version}")
    private String version;
    @Value("${huawei.access.key}")
    private String accessKey;
    @Value("${huawei.secret.key}")
    private String secretKey;

    /** host要参与鉴权算法，只能ip：port的格式 **/
    private String host;

    public AccountApiServiceImpl(@Value("${huawei.server.account.ip}") String serverIp,
            @Value("${huawei.server.account.port}") String port) {
        super();
        this.host = serverIp.concat(":").concat(port);
    }

    /**
     * 1.2.1 创建企业
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String createAccount(AccountCreateBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(createAccountURI);

        log.info("[Create company] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, createAccountURI, null, request);
        
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Create company] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Create company] create company {} exception, e: {}", bean.getCompanyName(), e);
            throw e;
        }
    }

    /**
     * 1.2.2 查询接入码
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String getBoardNumber(AccountBoardNumberGetBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(getNumberURI);

        log.info("[Get Board Number] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, getNumberURI, null, request);
        
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Get Board Numbe] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Get Board Number] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 1.2.3 锁定接入码
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String lockBoardNumber(AccountBoardNumberLockBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(lockNumberURI);

        log.info("[Lock Board Number] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, lockNumberURI, null, request);
        
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Lock Board Number] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Lock Board Number] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 1.2.4 初始化服务
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String initAccount(AccountInitBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(initAccountURI);

        log.info("[Init Company] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, initAccountURI, null, request);
        
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Init Company] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Init Company] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 1.2.5 变更服务
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String updateAccount(AccountUpdateBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(updateAccountURI);

        log.info("[Update Board Number] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, updateAccountURI, null, request);
        
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Update Board Number] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Update Board Number] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 1.2.6 根据企业帐号查询企业基本信息
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String getAccountInfo(AccountGetBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(getAccountURI);

        log.info("[Get Company Inf] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, getAccountURI, null, request);
        
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Get Company Info] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Get Company Info] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 1.2.7 更改服务状态
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String updateAccountStatus(AccountStatusUpdateBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(updateStatusURI);

        log.info("[Update Company Status] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, updateStatusURI, null, request);
        
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Update Company Status] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Update Company Status] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 1.2.8 查询服务器信息
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String getServerInfo(AccountServerInfoGetBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(getServerInfoURI);

        log.info("[Get Server Info] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, getServerInfoURI, null, request);
        
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Get Server Info] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Get Server Info] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 1.2.9 查询企业帐号
     * 
     * @throws Exception
     */
    @Override
    public String getAccountsInfo() throws Exception {
        String url = serverHeader.concat(host).concat(getAccoutsURI);

        AccountsGetBean bean = new AccountsGetBean();
        bean.setDeveloperId(accessKey);
        
        log.info("[Get All Company] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, getAccoutsURI, null, request);
        
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Get All Company] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Get All Company] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 1.2.10 查询被叫配置
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String getCallConfigInfo(AccountCallConfigGetBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(getCallConfigURI);

        log.info("[Get Call Config] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, getCallConfigURI, null, request);
        
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Get Call Config] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Get Call Config] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 1.2.11 更新被叫配置
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String updateCallConfig(AccountCallConfigUpdateBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(updateCallConfigURI);

        log.info("[Update Call Config] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, updateCallConfigURI, null, request);
        
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Update Call Config] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Update Call Config] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 1.2.12 删除企业被叫配置
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String deleteCallConfig(AccountCallConfigDeleteBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(deleteCallConfigURI);

        log.info("[Delete Call Config] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, deleteCallConfigURI, null, request);

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Delete Call Config] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Delete Call Config] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 1.3.1 查询指定座席角色
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String getRoleInfo(AccountRoleInfoGetBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(getRoleInfoURI);

        log.info("[Get Role Info] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, getRoleInfoURI, null, request);

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Get Role Info] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Get Role Info] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 1.3.2 查询所有座席角色
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String getRolesInfo(AccountRolesInfoGetBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(getRolesInfoURI);

        log.info("[Get Roles Info] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, getRolesInfoURI, null, request);
        
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Get Roles Inf] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Get Roles Info] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 1.3.3 创建技能队列
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String createSkill(AccountSkillCreateBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(createSkillURI);

        log.info("[Create Skill] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, createSkillURI, null, request);

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Create Skill] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Create Skill] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 1.3.4 查询所有技能队列
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String getSkillsInfo(AccountSkillsInfoGetBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(getSkillsInfoURI);

        log.info("[Get Skills] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, getSkillsInfoURI, null, request);

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Get Skills] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Get Skills] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 1.3.5 删除指定技能队列
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String deleteSkill(AccountSkillDeleteBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(deleteSkillURI);

        log.info("[Delete Skill] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, deleteSkillURI, null, request);

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Delete Skill] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Delete Skill] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 1.3.6 增加座席
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String createAgent(AccountAgentCreateBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(createAgentURI);

        log.info("[Create Agent] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, createAgentURI, null, request);

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Create Agent] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Create Agent] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 1.3.7 设置座席技能队列
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String addAgentToSkill(AccountSkillAgentAddBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(setAgentSkillURI);

        log.info("[Add Agent To Skill] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, setAgentSkillURI, null, request);

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Add Agent To Skill] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Add Agent To Skill] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 1.3.8 更换座席角色
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String setAgentRole(AccountRoleUpdateBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(setAgentRoleURI);

        log.info("[Set Agent Role] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, setAgentRoleURI, null, request);

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Set Agent Role] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Set Agent Role] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 1.3.9 删除座席
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String deleteAgent(AccountAgentDeleteBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(deleteAgentURI);

        log.info("[Delete Agent] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, deleteAgentURI, null, request);

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Delete Agent] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Delete Agent] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 1.3.10 查询所有座席信息
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String getAgentsInfo(AccountAgentsInfoGetBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(getAgentsInfoURI);

        log.info("[Get Agents Info] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, getAgentsInfoURI, null, request);
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Get Agents Info] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Get Agents Info] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 1.4.1 查询 IVR流程uri
     * 
     * @param bean
     */
    @Override
    public String getIvrFlowInfo(AccountIvrFlowGetBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(getIvrFlowURI);

        log.info("[Get IVR Flow] param: {}", JSON.toJSONString(bean));
        
        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, getIvrFlowURI, null, request);
        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Get IVR Flow] reasponse: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Get IVR Flow] exception, e: {}", e);
            throw e;
        }
    }
}
