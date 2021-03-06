package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.2.17 呼叫代答
 * 
 * @author hujm
 */

@Getter
@Setter
public class VoiceCallSnatchBean {
    /** 座席工号 **/
    private Integer agentid;
    /** 座席鉴权信息 **/
    private String guid;
    /** 被代答座席工号 **/
    private Integer destWorkNo;
}
