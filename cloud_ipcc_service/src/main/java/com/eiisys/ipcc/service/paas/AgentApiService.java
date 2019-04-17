package com.eiisys.ipcc.service.paas;

import java.util.Map;

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

/**
 * 座席应用接口> 在线座席
 * 
 * @author hujm
 */
public interface AgentApiService {
    /** 2.1.1 坐席登录 **/
    public Map<String, String> login(AgentLoginBean bean) throws Exception;

    /** 2.1.2 强制登录 **/
    public Map<String, String> forceLogin(AgentLoginBean bean) throws Exception;

    /** 2.1.3 心跳检测 **/
    public String heartbeat(AgentHeartbeatBean bean) throws Exception;

    /** 2.1.4 查询配置技能队列 **/
    public String getAgentSkill(AgentSkillGetBean bean) throws Exception;

    /** 2.1.5 查询指定座席配置技能队列 **/
    public String getAgentSkillByNo(AgentSkillGetByNoBean bean) throws Exception;

    /** 2.1.6 设置是否自应答 **/
    public String setAutoAnswer(AgentAutoAnswerSetBean bean) throws Exception;

    /** 2.1.7 重置技能队列 **/
    public String resetSkill(AgentSkillResetBean bean) throws Exception;

    /** 2.1.8 示闲 **/
    public String setFree(AgentFreeSetBean bean) throws Exception;

    /** 2.1.9 示忙 **/
    public String setBusy(AgentBusySetBean bean) throws Exception;

    /** 2.1.10 取消示忙 **/
    public String cancelBusy(AgentBusyCancelBean bean) throws Exception;

    /** 2.1.11 请求休息 **/
    public String requetRest(AgentRestBean bean) throws Exception;

    /** 2.1.12 取消休息 **/
    public String cancelRest(AgentBusyCancelBean bean) throws Exception;

    /** 2.1.13 进入工作态 **/
    public String requestWork(AgentWorkBean bean) throws Exception;

    /** 2.1.14 退出工作态 **/
    public String cancelWork(AgentWorkCancelBean bean) throws Exception;

    /** 2.1.15 设置是否进入空闲态 **/
    public String setAutoIdle(AgentAutoIdleSetBean bean) throws Exception;
    
    /** 2.1.16 座席发布公告 **/
    public String notifyBulletin(AgentBulletinBean bean) throws Exception;

    /** 2.1.17 发送便签(扩展) **/
    public String sendNoteText(AgentNoteSendBean bean) throws Exception;

    /** 2.1.18 签出 **/
    public String logout(AgentLogoutBean bean) throws Exception;

    /** 2.1.19 强制签出带原因码 **/
    public String forceLogout(AgentForceLogoutBean bean) throws Exception;

    /** 2.1.20 设置是否接听来话 **/
    public String setTalkRight(AgentTalkRightSetBean bean) throws Exception;

    /** 2.1.21 获取当前座席的状态 **/
    public String getAgentStatus(AgentStatusGetBean bean) throws Exception;
}
