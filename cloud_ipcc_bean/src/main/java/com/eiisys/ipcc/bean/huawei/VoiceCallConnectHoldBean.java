package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.2.13 连接保持 座席对正在通话中的通话和保持中的通话进行连接，该操作后，座席结束自己的通话，另外两方形成通话
 * 
 * @author hujm
 *
 */

@Getter
@Setter
public class VoiceCallConnectHoldBean {
    /** 座席工号 **/
    private Integer agentid;
    /** 座席鉴权信息 **/
    private String guid;
    /** 保持的语音呼叫的callid **/
    private String callid;
}
