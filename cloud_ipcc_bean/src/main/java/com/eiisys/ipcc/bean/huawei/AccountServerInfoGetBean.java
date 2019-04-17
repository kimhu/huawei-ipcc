package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 1.2.8 查询服务器信息
 * 
 * @author hujm
 */

@Getter
@Setter
public class AccountServerInfoGetBean {
    /** 企业帐号，已通过“创建企业”获取 **/
    private String accountId;
}
