package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 1.2.12 删除企业被叫配置
 * 
 * @author hujm
 */

@Getter
@Setter
public class AccountCallConfigDeleteBean {
    /** 被叫配置所属的企业帐号，通过“创建企业”获取 **/
    private String accountId;
    /** 被叫配置ID **/
    private Integer dnId;
}
