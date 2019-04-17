package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 1.3.10 查询所有座席信息
 * 
 * @author hujm
 */

@Getter
@Setter
public class AccountAgentsInfoGetBean {
    /** 技能队列所属的企业帐号，已通过“创建企业”获取 **/
    private String accountId;
}
