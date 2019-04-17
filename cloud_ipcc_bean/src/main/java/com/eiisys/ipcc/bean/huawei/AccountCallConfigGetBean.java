package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 1.2.10 查询被叫配置
 * 
 * @author hujm
 */

@Getter
@Setter
public class AccountCallConfigGetBean {
    /** 企业帐号，已通过“创建企业”获取 **/
    private String accountId;
}
