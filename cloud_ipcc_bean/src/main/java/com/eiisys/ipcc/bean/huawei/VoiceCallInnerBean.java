package com.eiisys.ipcc.bean.huawei;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.2.3 内部呼叫
 * 
 * @author hujm
 */

@Getter
@Setter
public class VoiceCallInnerBean {
    /** 座席工号 **/
    @JSONField(serialize = false)
    private Integer agentid;
    /** 座席鉴权信息 **/
    @JSONField(serialize = false)
    private String guid;
    /** 被叫座席工号。内容不可为空，1-24位数字或*或# **/
    private String called;
}
