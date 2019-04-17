package com.eiisys.ipcc.service.paas;

import com.eiisys.ipcc.bean.huawei.AccountAgentCreateBean;
import com.eiisys.ipcc.bean.huawei.AccountAgentDeleteBean;
import com.eiisys.ipcc.bean.huawei.AccountAgentsInfoGetBean;
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

/**
 * 华为运营管理接口
 * 
 * @author hujm
 */
public interface AccountApiService {
    /** 1.2.1 创建企业 **/
    public String createAccount(AccountCreateBean bean) throws Exception;

    /** 1.2.2 查询接入码 **/
    public String getBoardNumber(AccountBoardNumberGetBean bean) throws Exception;

    /** 1.2.3 锁定接入码 **/
    public String lockBoardNumber(AccountBoardNumberLockBean bean) throws Exception;

    /** 1.2.4 初始化服务 **/
    public String initAccount(AccountInitBean bean) throws Exception;

    /** 1.2.5 变更服务 **/
    public String updateAccount(AccountUpdateBean bean) throws Exception;

    /** 1.2.6 根据企业帐号查询企业基本信息 **/
    public String getAccountInfo(AccountGetBean bean) throws Exception;

    /** 1.2.7 更改服务状态 **/
    public String updateAccountStatus(AccountStatusUpdateBean bean) throws Exception;

    /** 1.2.8 查询服务器信息 **/
    public String getServerInfo(AccountServerInfoGetBean bean) throws Exception;

    /** 1.2.9 查询企业帐号 **/
    public String getAccountsInfo() throws Exception;

    /** 1.2.10 查询被叫配置 **/
    public String getCallConfigInfo(AccountCallConfigGetBean bean) throws Exception;

    /** 1.2.11 更新被叫配置 **/
    public String updateCallConfig(AccountCallConfigUpdateBean bean) throws Exception;

    /** 1.2.12 删除企业被叫配置 **/
    public String deleteCallConfig(AccountCallConfigDeleteBean bean) throws Exception;
    
    /** 1.3.1 查询指定座席角色 **/
    public String getRoleInfo(AccountRoleInfoGetBean bean) throws Exception;
    
    /** 1.3.2 查询所有座席角色 **/
    public String getRolesInfo(AccountRolesInfoGetBean bean) throws Exception;
    
    /** 1.3.3 创建技能队列 **/
    public String createSkill(AccountSkillCreateBean bean) throws Exception;
    
    /** 1.3.4 查询所有技能队列 **/
    public String getSkillsInfo(AccountSkillsInfoGetBean bean) throws Exception;
    
    /** 1.3.5 删除指定技能队列 **/
    public String deleteSkill(AccountSkillDeleteBean bean) throws Exception;
    
    /** 1.3.6 增加座席 **/
    public String createAgent(AccountAgentCreateBean bean) throws Exception;
    
    /** 1.3.7 设置座席技能队列 **/
    public String addAgentToSkill(AccountSkillAgentAddBean bean) throws Exception;
    
    /** 1.3.8 更换座席角色 **/
    public String setAgentRole(AccountRoleUpdateBean bean) throws Exception;
    
    /** 1.3.9 删除座席 **/
    public String deleteAgent(AccountAgentDeleteBean bean) throws Exception;
    
    /** 1.3.10 查询所有座席信息 **/
    public String getAgentsInfo(AccountAgentsInfoGetBean bean) throws Exception;

    /** 1.4.1 查询 IVR流程 **/
    public String getIvrFlowInfo(AccountIvrFlowGetBean bean) throws Exception;
}
