package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.1.5 查询指定座席配置技能队列
 * 
 * @author hujm
 */

@Getter
@Setter
public class AgentSkillGetByNoBean {
    /** 座席工号 **/
    private Integer agentid;
    /** 座席鉴权信息 **/
    private String guid;
    /** 待查询的坐席工号 **/
    private String workNo;
}
