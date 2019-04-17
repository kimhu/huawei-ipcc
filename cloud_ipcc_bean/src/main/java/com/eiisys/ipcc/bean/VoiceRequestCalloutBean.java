package com.eiisys.ipcc.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 语音电话呼出
 * 
 * @author hujm
 */

@Getter
@Setter
public class VoiceRequestCalloutBean {
    /** 被叫号码 **/
    private String callee;
}
