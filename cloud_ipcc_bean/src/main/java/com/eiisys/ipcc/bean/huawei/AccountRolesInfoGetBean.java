package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 1.3.2 查询所有座席角色
 * @author hujm
 */

@Getter
@Setter
public class AccountRolesInfoGetBean {
    /** 座席角色所属的企业帐号，已通过“创建企业”获取 **/
    private String accountId;
}
