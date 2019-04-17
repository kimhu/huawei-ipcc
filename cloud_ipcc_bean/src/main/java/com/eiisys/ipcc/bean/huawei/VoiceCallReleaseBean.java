package com.eiisys.ipcc.bean.huawei;

import lombok.Setter;

import lombok.Getter;

/**
 * 2.2.16 挂断呼叫
 * 
 * @author hujm
 */

@Getter
@Setter
public class VoiceCallReleaseBean {
    /** 座席工号 **/
    private Integer agentid;
    /** 座席鉴权信息 **/
    private String guid;
}
