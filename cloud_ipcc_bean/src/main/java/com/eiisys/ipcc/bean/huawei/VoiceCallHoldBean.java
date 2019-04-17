package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.2.7 呼叫保持
 * 
 * @author hujm
 */

@Getter
@Setter
public class VoiceCallHoldBean {
    /** 座席工号 **/
    private Integer agentid;
    /** 座席鉴权信息 **/
    private String guid;
}
