package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 1.3.9 删除座席
 * 
 * @author hujm
 */

@Getter
@Setter
public class AccountAgentDeleteBean {
    /** 座席所属的企业帐号，已通过“创建企业”获取 **/
    private String accountId;
    /** 座席工号，已通过“增加座席”获取 **/
    private String agentId;
}
