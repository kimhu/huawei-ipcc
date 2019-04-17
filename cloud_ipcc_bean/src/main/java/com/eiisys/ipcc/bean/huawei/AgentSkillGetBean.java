package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.1.4 查询配置技能队列 座席登录后，查询自己配置的的技能队列信息
 * 
 * @author hujm
 */

@Getter
@Setter
public class AgentSkillGetBean {
    /** 座席工号 **/
    private Integer agentid;
    /** 座席鉴权信息 **/
    private String guid;
}
