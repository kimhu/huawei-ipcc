package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.1.21 获取当前座席的状态
 * 
 * @author hujm
 */

@Getter
@Setter
public class AgentStatusGetBean {
    /** 座席工号 **/
    private Integer agentid;
    /** 座席鉴权信息 **/
    private String guid;
}
