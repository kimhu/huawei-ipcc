package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 1.2.4 初始化服务
 * 
 * @author hujm
 */

@Getter
@Setter
public class AccountInitBean {
    /** 企业帐号，已通过“创建企业”获取 **/
    private String accountId;
    /** 接入码（客服热线号码），已通过“查询接 入码”获取 **/
    private String callNo;
    /** 城市编码 **/
    private String cityId;
}
