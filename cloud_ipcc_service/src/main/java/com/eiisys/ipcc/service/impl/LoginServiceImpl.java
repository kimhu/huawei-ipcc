package com.eiisys.ipcc.service.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eiisys.ipcc.bean.LoginResponseConfigBean;
import com.eiisys.ipcc.bean.LoginResponseIndefierBean;
import com.eiisys.ipcc.bean.LoginResponseBean;
import com.eiisys.ipcc.bean.AgentStatusEnum;
import com.eiisys.ipcc.bean.LoginRequestTokenBean;
import com.eiisys.ipcc.bean.huawei.AgentBusySetBean;
import com.eiisys.ipcc.bean.huawei.AgentFreeSetBean;
import com.eiisys.ipcc.bean.huawei.AgentHeartbeatBean;
import com.eiisys.ipcc.bean.huawei.AgentLoginBean;
import com.eiisys.ipcc.bean.huawei.AgentLogoutBean;
import com.eiisys.ipcc.bean.huawei.AgentRestBean;
import com.eiisys.ipcc.bean.huawei.AgentStatusGetBean;
import com.eiisys.ipcc.bean.huawei.AgentWorkBean;
import com.eiisys.ipcc.constants.HuaweiConstants;
import com.eiisys.ipcc.constants.IpccConstants;
import com.eiisys.ipcc.constants.MsgConstants;
import com.eiisys.ipcc.constants.RedisConstants;
import com.eiisys.ipcc.core.util.RedisClient;
import com.eiisys.ipcc.core.utils.ResponseUtils;
import com.eiisys.ipcc.dao.CompanyConfigDao;
import com.eiisys.ipcc.dao.CompanyDao;
import com.eiisys.ipcc.entity.Company;
import com.eiisys.ipcc.entity.CompanyConfig;
import com.eiisys.ipcc.entity.User;
import com.eiisys.ipcc.exception.IpccException;
import com.eiisys.ipcc.service.LoginService;
import com.eiisys.ipcc.service.UserService;
import com.eiisys.ipcc.service.paas.AgentApiService;
import com.eiisys.ipcc.service.saas.SaasApiService;

import lombok.extern.slf4j.Slf4j;

/**
 * 登录接口实现类
 * 
 * @author hujm
 */

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Value("${server.file.path}")
    private String filePath;

    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private CompanyConfigDao configDao;

    @Autowired
    private UserService userService;
    @Autowired
    private SaasApiService saasService;
    @Autowired
    private AgentApiService agentService;

    @Autowired
    private RedisClient<String> redisClient;

    /**
     * token登录
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public LoginResponseBean tokenLogin(LoginRequestTokenBean bean) throws IpccException {
        LoginResponseBean responseBean = new LoginResponseBean();
        Integer id6d;
        Integer companyId;
        try {
            String saasTokenLogin = saasService.tokenLogin(bean.getToken());

            log.info("[Token Login Service] token login response: {}", saasTokenLogin);

            if (!saasTokenLogin.contains("server_response") && !saasTokenLogin.contains("id6d")
                    && !saasTokenLogin.contains("company_id")) {
                throw ResponseUtils.genIpccException(MsgConstants.MSG_SAAS_LOGIN_FAILED);
            }

            JSONObject object = JSONObject.parseObject(saasTokenLogin);
            Integer statusCode = object.getJSONObject("server_response").getInteger("status_code");
            String errorCode = null;
            switch (statusCode) {
            case 201:
                break;
            case 406:
                errorCode = MsgConstants.MSG_SAAS_TOKEN_ERROR;
                break;
            default:
                errorCode = MsgConstants.MSG_SAAS_LOGIN_FAILED;
                break;
            }

            if (null != errorCode) {
                throw ResponseUtils.genIpccException(errorCode);
            }

            id6d = object.getJSONObject("server_response").getInteger("id6d");
            companyId = object.getJSONObject("server_response").getInteger("company_id");

            if (null == id6d || null == companyId) {
                log.error("[Token Login Service] no id6d or company from saas");
                throw ResponseUtils.genIpccException(MsgConstants.MSG_SAAS_LOGIN_FAILED);
            }

        } catch (Exception e) {
            log.error("[Token Login Service] login saas exception: {}", e);
            throw ResponseUtils.genIpccException(MsgConstants.MSG_SAAS_LOGIN_FAILED);
        }

        /** 登录华为坐席 **/
        paasLogin(id6d);

        /** 封装用户信息返回给前台 **/
        getUserInfo(responseBean, id6d, companyId);
        return responseBean;
    }

    /**
     * 用户名密码登录
     * 
     * @param userName
     * @param password
     * @throws Exception
     */
    @Override
    public LoginResponseBean login(String userName, String password) throws IpccException {
        LoginResponseBean responseBean = new LoginResponseBean();
        Integer id6d;
        Integer companyId;
        String loginResult;
        try {
            loginResult = saasService.login(userName, password);

            log.info("[Login Service] login response: {}", loginResult);
        } catch (Exception e) {
            log.error("[Login Service] login exception: {}", e);
            throw ResponseUtils.genIpccException(MsgConstants.MSG_SAAS_LOGIN_FAILED);
        }

        if (!loginResult.contains("server_response") || !loginResult.contains("id6d")
                || !loginResult.contains("company_id")) {
            throw ResponseUtils.genIpccException(MsgConstants.MSG_SAAS_LOGIN_FAILED);
        }

        JSONObject object = JSONObject.parseObject(loginResult);
        Integer statusCode = object.getJSONObject("server_response").getInteger("status_code");
        String errorCode = null;
        switch (statusCode) {
        case 201:
            break;
        case 403:
            errorCode = MsgConstants.MSG_SAAS_LOGIN_FREQUENTLY;
            break;
        case 404:
            errorCode = MsgConstants.MSG_SAAS_USER_NOT_EXIST;
            break;
        case 406:
            errorCode = MsgConstants.MSG_SAAS_PASSWORD_NOT_MATCH;
            break;
        default:
            errorCode = MsgConstants.MSG_SAAS_LOGIN_FAILED;
            break;
        }

        if (null != errorCode) {
            throw ResponseUtils.genIpccException(errorCode);
        }

        id6d = object.getJSONObject("server_response").getInteger("id6d");
        companyId = object.getJSONObject("server_response").getInteger("company_id");

        if (null == id6d || null == companyId) {
            log.error("[Login Service] no id6d or company from saas");
            throw ResponseUtils.genIpccException(MsgConstants.MSG_SAAS_LOGIN_FAILED);
        }

        /** 登录华为坐席 **/
        paasLogin(id6d);

        /** 封装用户信息返回给前台 **/
        getUserInfo(responseBean, id6d, companyId);
        return responseBean;
    }

    /**
     * 华为坐席登录
     * 
     * @param id6d
     * @throws Exception
     */
    @Override
    public void paasLogin(Integer id6d) throws IpccException {
        User user = userService.getUserById6d(id6d);
        String errorCode = null;
        if (null == user) {
            errorCode = MsgConstants.MSG_SAAS_USER_NOT_EXIST;
        }

        if (null == user.getAgentId()) {
            errorCode = MsgConstants.MSG_AGENT_ID_NULL;
        }

        if (StringUtils.isEmpty(user.getAgentPhone())) {
            errorCode = MsgConstants.MSG_AGENT_PHONE_NULL;
        }

        if (null != errorCode) {
            throw ResponseUtils.genIpccException(errorCode);
        }

        Map<String, String> paasResult;
        try {
            AgentLoginBean agentBean = new AgentLoginBean();
            agentBean.setAgentid(user.getAgentId());
            agentBean.setPhonenum(user.getAgentPhone());
            agentBean.setPassword(user.getAgentPwd());
            // agentBean.setAutoanswer(true);

            paasResult = agentService.login(agentBean);

            log.info("[Paas Login] response: {}", JSON.toJSONString(paasResult));
        } catch (Exception e) {
            log.error("[Paas Login] exception: {}", e);
            throw ResponseUtils.genIpccException(MsgConstants.MSG_PAAS_LOGIN_FAILED);
        }

        String guid = paasResult.get(HuaweiConstants.HEAD_GUID);
        String content = paasResult.get(HuaweiConstants.RESPOSE_BODY);

        if (null == content) {
            throw ResponseUtils.genIpccException(MsgConstants.MSG_PAAS_LOGIN_FAILED);
        }

        JSONObject object = JSONObject.parseObject(content);

        String paasCode = object.getString("retcode");
        switch (paasCode) {
        case "0":
            break;
        case "100-002":
            errorCode = MsgConstants.MSG_PAAS_LOGIN_REPEAT;
            break;
        case "100-004":
            errorCode = MsgConstants.MSG_PAAS_PASSWORD_NOT_MATCH;
            break;
        case "100-013":
            errorCode = MsgConstants.MSG_PAAS_PHONE_NOT_MATCH;
            break;
        default:
            errorCode = MsgConstants.MSG_PAAS_LOGIN_FAILED;
            break;
        }

        if (null != errorCode) {
            throw ResponseUtils.genIpccException(errorCode);
        }

        /** 将guid放入缓存中 */
        redisClient.set(RedisConstants.RKEY_AGENT_GUID + id6d, guid.replace("JSESSIONID=", ""));
        redisClient.set(RedisConstants.RKEY_AGNET_ID + id6d, String.valueOf(user.getAgentId()));
    }

    /**
     * 坐席签出
     * 
     * @param id6d
     * @param agentId
     * @throws Exception
     */
    @Override
    public void paasLogout(Integer id6d) throws IpccException {
        String redisGuidKey = RedisConstants.RKEY_AGENT_GUID + id6d;
        String redisAgentIdKey = RedisConstants.RKEY_AGNET_ID + id6d;
        if (!redisClient.hasKey(redisGuidKey) || !redisClient.hasKey(redisAgentIdKey)) {
            throw ResponseUtils.genIpccException(MsgConstants.MSG_PAAS_NOT_LOGINED);
        }

        String guid = redisClient.get(redisGuidKey);
        String agentId = redisClient.get(redisAgentIdKey);

        /** 先获取坐席状态，如果坐席处于通话状态，不能签出 **/
        String statusResult;
        try {
            AgentStatusGetBean bean = new AgentStatusGetBean();
            bean.setGuid(guid);
            bean.setAgentid(Integer.valueOf(agentId));

            statusResult = agentService.getAgentStatus(bean);

            log.info("[Logout Service] get paas status response: {}", statusResult);
        } catch (Exception e) {
            log.error("[Logout Service] paas logout exception: {}", e);
            throw ResponseUtils.genIpccException(MsgConstants.MSG_PAAS_LOGOUT_FAILED);
        }

        JSONObject jsonObject = JSONObject.parseObject(statusResult);
        String statusCode = jsonObject.getString("retcode");

        String errorCode = null;
        switch (statusCode) {
        case "0":
            break;
        case "000-002":
            /** 参数不合法 **/
            errorCode = MsgConstants.MSG_PAAS_PARAM_INVALID;
            break;
        case "000-003":
            errorCode = MsgConstants.MSG_PAAS_GUID_ERROR;
            break;
        case "100-006":
            errorCode = MsgConstants.MSG_PAAS_NOT_LOGINED;
            /** 删除坐席guid和agentId信息 **/
            redisClient.delete(redisGuidKey);
            redisClient.delete(redisAgentIdKey);
            break;
        default:
            errorCode = MsgConstants.MSG_PAAS_LOGOUT_FAILED;
            break;
        }

        if (null != errorCode) {
            throw ResponseUtils.genIpccException(errorCode);
        }

        /** 是否在通话中 **/
        boolean isWorking = jsonObject.getJSONObject("result").getBooleanValue("isWorking");
        if (isWorking) {
            throw ResponseUtils.genIpccException(MsgConstants.MSG_PAAS_LOGOUT_WORKING);
        }

        /** 签出坐席 **/
        String logoutResult;
        try {
            AgentLogoutBean bean = new AgentLogoutBean();
            bean.setAgentid(Integer.valueOf(agentId));
            bean.setGuid(guid);

            logoutResult = agentService.logout(bean);

            log.info("[Logout Service] paas logout response: {}", logoutResult);
        } catch (Exception e) {
            log.error("[Logout Service] paas logout exception: {}", e);
            throw ResponseUtils.genIpccException(MsgConstants.MSG_PAAS_LOGOUT_FAILED);
        }

        jsonObject = JSONObject.parseObject(logoutResult);
        statusCode = jsonObject.getString("retcode");
        errorCode = null;
        switch (statusCode) {
        case "0":
            break;
        case "000-002":
            /** 参数不合法 **/
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
        default:
            errorCode = MsgConstants.MSG_PAAS_LOGOUT_FAILED;
            break;
        }

        if (null != errorCode) {
            throw ResponseUtils.genIpccException(errorCode);
        }

        /** 删除坐席guid和agentId信息 **/
        redisClient.delete(redisGuidKey);
        redisClient.delete(redisAgentIdKey);
    }

    /**
     * 华为坐席心跳检测
     * 
     * @param id6d
     * @param agentId
     * @throws Exception
     */
    @Override
    public void paasHeartbeat(Integer id6d) throws IpccException {
        String redisGuidKey = RedisConstants.RKEY_AGENT_GUID + id6d;
        String redisAgentIdKey = RedisConstants.RKEY_AGNET_ID + id6d;
        if (!redisClient.hasKey(redisGuidKey) || !redisClient.hasKey(redisAgentIdKey)) {
            throw ResponseUtils.genIpccException(MsgConstants.MSG_PAAS_NOT_LOGINED);
        }

        String guid = redisClient.get(redisGuidKey);
        String agentId = redisClient.get(redisAgentIdKey);

        String paasResult;
        try {
            AgentHeartbeatBean bean = new AgentHeartbeatBean();
            bean.setAgentid(Integer.valueOf(agentId));
            bean.setGuid(guid);

            paasResult = agentService.heartbeat(bean);

            log.info("[Paas Heartbeat] response: {}", paasResult);
        } catch (Exception e) {
            log.error("[Paas Heartbeat] exception: {}", e);
            throw ResponseUtils.genIpccException(MsgConstants.MSG_PAAS_NOT_LOGINED);
        }

        JSONObject jsonObject = JSONObject.parseObject(paasResult);
        String statusCode = jsonObject.getString("retcode");
        String errorCode = null;
        switch (statusCode) {
        case "0":
            break;
        case "000-002":
            /** 参数不合法 **/
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
        default:
            errorCode = MsgConstants.MSG_PAAS_HEARTBEAT_ERROR;
            break;
        }

        if (null != errorCode) {
            throw ResponseUtils.genIpccException(errorCode);
        }
    }

    /**
     * 坐席状态切换
     * 
     * @param id6d
     * @param status
     */
    @Override
    public void paasStatusChange(Integer id6d, Integer status) throws IpccException {
        String redisGuidKey = RedisConstants.RKEY_AGENT_GUID + id6d;
        String redisAgentIdKey = RedisConstants.RKEY_AGNET_ID + id6d;
        if (!redisClient.hasKey(redisGuidKey) || !redisClient.hasKey(redisAgentIdKey)) {
            throw ResponseUtils.genIpccException(MsgConstants.MSG_PAAS_NOT_LOGINED);
        }

        String guid = redisClient.get(redisGuidKey);
        String agentId = redisClient.get(redisAgentIdKey);

        String paasResult;
        try {
            switch (status) {
            case 1:
                /** 空闲状态 **/
                AgentFreeSetBean freeBean = new AgentFreeSetBean();
                freeBean.setAgentid(Integer.valueOf(agentId));
                freeBean.setGuid(guid);
                paasResult = agentService.setFree(freeBean);
                break;
            case 2:
                /** 休息状态 **/
                AgentRestBean restBean = new AgentRestBean();
                restBean.setAgentid(Integer.valueOf(agentId));
                restBean.setGuid(guid);
                paasResult = agentService.requetRest(restBean);
                break;
            case 3:
                /** 忙碌状态 **/
                AgentBusySetBean busyBean = new AgentBusySetBean();
                busyBean.setAgentid(Integer.valueOf(agentId));
                busyBean.setGuid(guid);
                paasResult = agentService.setBusy(busyBean);
                break;
            case 4:
                /** 工作态 **/
                AgentWorkBean workBean = new AgentWorkBean();
                workBean.setAgentid(Integer.valueOf(agentId));
                workBean.setGuid(guid);
                paasResult = agentService.requestWork(workBean);
                break;
            default:
                throw ResponseUtils.genIpccException(MsgConstants.MSG_PAAS_STATUS_ERROR);
            }

            log.info("[Statuc Change] response: {}", paasResult);
        } catch (Exception e) {
            log.error("[Statuc Change] exception: {}", e);
            throw ResponseUtils.genIpccException(MsgConstants.MSG_PAAS_STATUS_CHANGE_FAILED);
        }

        JSONObject jsonObject = JSONObject.parseObject(paasResult);
        String statusCode = jsonObject.getString("retcode");
        String errorCode = null;
        switch (statusCode) {
        case "0":
            break;
        case "000-002":
            /** 参数不合法 **/
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
        default:
            errorCode = MsgConstants.MSG_PAAS_STATUS_CHANGE_FAILED;
            break;
        }

        if (null != errorCode) {
            throw ResponseUtils.genIpccException(errorCode);
        }
    }

    private void getUserInfo(LoginResponseBean bean, Integer id6d, Integer companyId) {
        String userName;
        boolean isAdmin;
        Integer gender;
        String account;
        try {
            String userInfo = saasService.getUserInfo(id6d, companyId);

            log.info("[Get Saas Info] get saas userinfo response: {}", userInfo);

            if (!userInfo.contains("server_response") && !userInfo.contains("sex") && !userInfo.contains("is_admin")
                    && !userInfo.contains("name")) {
                throw ResponseUtils.genIpccException(MsgConstants.MSG_SAAS_LOGIN_FAILED);
            }

            JSONObject jsonObject = JSONObject.parseObject(userInfo);
            userName = jsonObject.getJSONObject("server_response").getString("name");
            isAdmin = jsonObject.getJSONObject("server_response").getByte("is_admin") == 1 ? true : false;
            gender = jsonObject.getJSONObject("server_response").getInteger("sex");
            account = jsonObject.getJSONObject("server_response").getString("account");
        } catch (Exception e) {
            log.error("[Login Service] login exception: {}", e);
            throw ResponseUtils.genIpccException(MsgConstants.MSG_SAAS_LOGIN_FAILED);
        }

        /** 获取权限列表 **/
        // List<String> codeList = userDao.getPermissionCodes(id6d);
        User user = userService.getUserById6d(id6d);
        if (user == null) {
            log.error("[Get User Info] no user found based on id6d");
            throw ResponseUtils.genIpccException(MsgConstants.MSG_SAAS_USER_NOT_EXIST);
        }

        /** 处理所属分组，如果没有分组，返回"" **/
        /*
         * String groupName = user.getGroupName(); if(StringUtils.isEmpty(groupName)) {
         * groupName = null; }
         */
        /** 处理坐席等级，如果没有坐席，返回"" **/
        /*
         * String roleName = user.getRoleName(); if(StringUtils.isEmpty(roleName)) {
         * roleName = null; }
         */

        /** 组装页面需要的内容 **/
        Company company = companyDao.getCompanyByCompanyId(companyId);
        if (company == null) {
            log.error("[Get User Info] no company found based on id6d {}", id6d);
            throw ResponseUtils.genIpccException(MsgConstants.MSG_SAAS_USER_NOT_EXIST);
        }

        try {
            /** 短信全局设置 **/
            LoginResponseConfigBean configBean = new LoginResponseConfigBean();
            configBean.setIsSms(company.getIsSms() == null ? 0 : company.getIsSms());

            /** 号码遮蔽全局设置 **/
            CompanyConfig numCoverConfig = configDao.getConfigByConfigId(companyId,
                    IpccConstants.IPCC_GLOBAL_NUMBER_COVER);
            configBean.setGlobalNumberCover(numCoverConfig == null ? 0 : Byte.valueOf(numCoverConfig.getConfigValue()));
            configBean.setSmsSwitch(company.getSmsSwitch());

            if (!userName.equals(user.getUserName())) {
                User tmpUser = new User();
                tmpUser.setId6d(id6d);
                tmpUser.setUserName(userName);
                userService.updateUser(user);
            }
            bean.setId6d(id6d);
            bean.setCompanyId(companyId);
            bean.setAdmin(isAdmin);
            bean.setUserName(userName);
            // bean.setCodeList(codeList);
            bean.setGender(gender);
            bean.setGroupId(user.getGroupId());
            // bean.setGroupName(groupName);
            // bean.setRoleName(roleName);
            bean.setUserId(account);
            /** 头像路径 **/
            String imagePath = StringUtils.isEmpty(user.getPortraitPath()) ? null
                    : filePath.concat(user.getPortraitPath());
            bean.setImagePath(imagePath);

            /** 呼叫转移号码 **/
            bean.setPhone(user.getPhone());
            /** 是否开启短信 **/
            // respDTO.setIsSms(user.getIsSms());
            bean.setConfigs(configBean);

            LoginResponseIndefierBean indefierBean = new LoginResponseIndefierBean();
            /** 是否是试用用户 **/
            /*
             * TrialAccount account = trialDao.getTrialAccountByUserId(user.getUserId()); if
             * (account != null) { indefierBean.setIsTrial((byte) 1); } else {
             * indefierBean.setIsTrial((byte) 0); }
             */

            /** 是否是华为用户 **/
            CompanyConfig huaweiConfig = configDao.getConfigByConfigId(companyId, IpccConstants.IPCC_IS_HUAWEI);
            if (huaweiConfig != null) {
                indefierBean.setIsHuawei((byte) 1);
            } else {
                indefierBean.setIsHuawei((byte) 0);
            }

            /** 是否绑定sip号码 **/
            /*
             * SipNumber sipNumber = sipDao.getSipNumberById6d(Integer.valueOf(id6d)); if
             * (sipNumber == null) { indefierBean.setIsSip((byte) 0); } else {
             * indefierBean.setIsSip((byte) 1); }
             */

            /** 是否绑定隐私小号 **/
            /*
             * int isPnOpen = pnService.getCompanyPnStatus(user.getCompanyId()); if
             * (isPnOpen == 1) { indefierBean.setHasPn((byte) 1);
             * indefierBean.setIsPn((byte) 0); int count =
             * pnDao.getNumberCount(user.getCompanyId()); if (count != 0) {
             * indefierBean.setIsPn((byte) 1); CompanyConfig config =
             * configDao.getCompanyConfigById(String.valueOf(user.getCompanyId()),
             * CallCenterConstant.GLOBAL_PN_BIND_TYPE); if (config != null &&
             * Integer.valueOf(config.getConfigValue()) == 1) { PrivateNumber number =
             * pnDao.getPrivateNumberById6d(user.getCompanyId(), user.getId6d()); if (number
             * == null) { indefierBean.setIsPn((byte) 0); } } } } else {
             * indefierBean.setHasPn((byte) 0); } bean.setIndefier(indefierBean);
             */
        } catch (Exception e) {
            log.error("[Get User Info] exception: {}", e);
            throw ResponseUtils.genIpccException(MsgConstants.MSG_LOGIN_FAILED);
        }
    }
}
