package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 1.2.6 查询基本信息
 * 
 * @author hujm
 */

@Getter
@Setter
public class AccountGetBean {
    /** 企业帐号，已通过“创建企业”获取 **/
    private String accountId;
}
