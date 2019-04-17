package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 1.3.1 查询指定座席角色
 * 
 * @author hujm
 */

@Getter
@Setter
public class AccountRoleInfoGetBean {
    /** 座席角色所属的企业帐号，已通过“创建企业”获取  **/
    private String accountId;
    /** 座席的角色ID，已通过“查询所有座席角色”获取 **/
    private Integer roleId;
}
