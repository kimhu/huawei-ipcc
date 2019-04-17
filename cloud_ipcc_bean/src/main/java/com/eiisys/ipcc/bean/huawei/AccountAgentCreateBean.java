package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 1.3.6 增加座席
 * 
 * @author hujm
 */

@Getter
@Setter
public class AccountAgentCreateBean {
    /** 技能队列所属的企业帐号，已通过“创建企业”获取 **/
    private String accountId;
    /** 技能队列ID的数组，已通过“创建技能队列”获取 **/
    private Integer[] skillIds;
    /** 需申请的座席个数，取值范围：1~10 **/
    private Integer agentNum;
    /** 座席的角色ID，已通过“查询所有座席角色”获取 **/
    private Integer roleId;
    /** 座席是否具有质检员权限，仅当座席角色非质检员时有效 **/
    private Boolean inspector;
}
