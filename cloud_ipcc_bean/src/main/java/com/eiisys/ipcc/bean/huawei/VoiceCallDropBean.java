package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.2.15 拆除指定callid呼叫
 * 
 * @author hujm
 */

@Getter
@Setter
public class VoiceCallDropBean {
    /** 座席工号 **/
    private Integer agentid;
    /** 座席鉴权信息 **/
    private String guid;
    /** 保持的语音呼叫的callid **/
    private String callid;
}
