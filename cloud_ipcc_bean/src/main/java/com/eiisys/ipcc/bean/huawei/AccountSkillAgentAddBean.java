package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 1.3.7 设置座席技能队列
 * 
 * @author hujm
 */

@Getter
@Setter
public class AccountSkillAgentAddBean {
    /** 技能队列所属的企业帐号，已通过“创建企业”获取 **/
    private String accountId;
    /** 座席工号，已通过“增加座席”获取 **/
    private String agentId;
    /** 技能队列的ID数组，通过“创建技能队列API”创建并返回 **/
    private Integer[] skillIds;
}
