package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.2.8 取消保持
 * 
 * @author hujm
 */

@Getter
@Setter
public class VoiceCallHoldCancelBean {
    /** 座席工号 **/
    private Integer agentid;
    /** 座席鉴权信息 **/
    private String guid;
    /** 呼叫唯一标识 **/
    private String callid;
}
