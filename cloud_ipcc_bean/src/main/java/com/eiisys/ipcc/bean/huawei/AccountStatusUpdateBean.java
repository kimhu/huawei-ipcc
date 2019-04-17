package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 1.2.7 更改服务状态
 * 
 * @author hujm
 */

@Getter
@Setter
public class AccountStatusUpdateBean {
    /** 企业帐号，已通过“创建企业”获取 **/
    private String accountId;
    /** 操作状态。 1：恢复（正常使用） 2：冻结（停止计费）**/
    private Integer status;
}
