package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 1.2.3 锁定接入码
 * 
 * @author hujm
 */

@Getter
@Setter
public class AccountBoardNumberLockBean {
    /** 企业帐号，已通过“创建企业”获取 **/
    private String accountId;
    /** 接入码，已通过“查询接 入码”获取 **/
    private String callNo;
    /** 锁号截止时间，格式为：YYYY-MM-DD hh:mm:ss， 锁号时间最长1小时 **/
    private String lockTime;
}
