package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.1.15 设置是否进入空闲态 座席设置释放通话后是否进入空闲态
 * 
 * @author hujm
 */

@Getter
@Setter
public class AgentAutoIdleSetBean {
    /** 座席工号 **/
    private Integer agentid;
    /** 座席鉴权信息 **/
    private String guid;
    /** 是否进入空闲态标志位（true为自动进入空闲态，false为进入工作态 ） **/
    private String flag;
}
