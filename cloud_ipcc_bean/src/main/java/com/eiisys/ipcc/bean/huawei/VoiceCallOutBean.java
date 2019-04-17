package com.eiisys.ipcc.bean.huawei;

import lombok.Setter;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;

/**
 * 2.2.1 外呼
 * 
 * @author hujm
 */

@Getter
@Setter
public class VoiceCallOutBean {
    /** 座席工号 **/
    @JSONField(serialize = false)
    private Integer agentid;
    /** 座席鉴权信息 **/
    @JSONField(serialize = false)
    private String guid;
    /** 主叫号码。内容可为空，为空时为平台默认主叫号码，0-24位数字 **/
    private String caller;
    /** 被叫号码。内容不可为空，1-24位数字或*或# **/
    private String called;
    /** 技能ID **/
    private Integer skillid;
    /** 随路数据信息。可为空，最大长度为16K位字符 **/
    private String callappdata;
    /** 媒体能力，默认为0。0：音频，1：视频，2：以缺省能力呼叫，由座席与用户侧话机进行协商 **/
    private String mediaability;
}
