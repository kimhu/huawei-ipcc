package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.1.9 示忙
 * 
 * @author hujm
 */

@Getter
@Setter
public class AgentBusySetBean {
    /** 座席工号 **/
    private Integer agentid;
    /** 座席鉴权信息 **/
    private String guid;
    /** 示忙原因码（范围200-250，如果为0或者不传递reason，则表示不设置示忙原因码） **/
    private Integer reason;
}
