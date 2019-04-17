package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.1.13 进入工作态
 * 
 * @author hujm
 */

@Getter
@Setter
public class AgentWorkBean {
    /** 座席工号 **/
    private Integer agentid;
    /** 座席鉴权信息 **/
    private String guid;
}
