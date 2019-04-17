package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.1.7 重置技能队列
 * 
 * @author hujm
 */

@Getter
@Setter
public class AgentSkillResetBean {
    /** 座席工号 **/
    private Integer agentid;
    /** 座席鉴权信息 **/
    private String guid;
    /** 是否自动登录技能标志（true为自动登录座席所配置技能，false为手动登录技能队列） **/
    private String autoflag;
    /** 技能队列ID **/
    private String skillid;
    /** 是否话机联动（1是，0否） **/
    private Integer phonelinkage;
}
