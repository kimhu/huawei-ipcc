package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.1.19 强制签出带原因码
 * 
 * @author hujm
 */

@Getter
@Setter
public class AgentForceLogoutBean {
    /** 座席工号 **/
    private Integer agentid;
    /** 座席鉴权信息 **/
    private String guid;
    /** 座席签出原因码（范围1-255） **/
    private Integer reason;
}
