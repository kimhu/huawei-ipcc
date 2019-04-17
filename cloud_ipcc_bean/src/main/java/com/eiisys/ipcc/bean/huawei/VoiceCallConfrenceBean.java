package com.eiisys.ipcc.bean.huawei;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.2.12 三方通话
 * 
 * @author hujm
 */

@Getter
@Setter
public class VoiceCallConfrenceBean {
    /** 座席工号 **/
    @JSONField(serialize = false)
    private Integer agentid;
    /** 座席鉴权信息 **/
    @JSONField(serialize = false)
    private String guid;
    /** 已经保持住的语音呼叫的callid。不可为空 **/
    private String callid;
    /** 需设置的随路数据。内容可为空，最大长度为1024字节 **/
    private String callappdata;
}
