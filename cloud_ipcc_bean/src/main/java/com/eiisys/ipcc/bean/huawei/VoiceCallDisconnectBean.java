package com.eiisys.ipcc.bean.huawei;

import lombok.Setter;

import lombok.Getter;

/**
 * 2.2.14 释放指定号码连接
 * 
 * @author hujm
 */

@Getter
@Setter
public class VoiceCallDisconnectBean {
    /** 座席工号 **/
    private Integer agentid;
    /** 座席鉴权信息 **/
    private String guid;
    /** 待释放的电话号码（1-24位数字） **/
    private String number;
}
