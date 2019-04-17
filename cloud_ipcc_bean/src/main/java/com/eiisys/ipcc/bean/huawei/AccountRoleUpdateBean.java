package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 1.3.8 更换座席角色
 * 
 * @author hujm
 */

@Getter
@Setter
public class AccountRoleUpdateBean {
    /** 技能队列所属的企业帐号，已通过“创建企业”获取 **/
    private String accountId;
    /** 座席工号，已通过“增加座席”获取 **/
    private String agentId;
    /** 座席的角色ID，已通过“查询所有座席角色”获取 **/
    private Integer roleId;
    /** 座席是否具有质检员权限，仅当座席角色非质检员时有效 **/
    private Boolean inspector;
}
