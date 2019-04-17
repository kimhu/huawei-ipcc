package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.2.11 二次拨号(扩展)
 * 
 * @author hujm
 */

@Getter
@Setter
public class VoiceCallSecondDailBean {
    /** 座席工号 **/
    private Integer agentid;
    /** 座席鉴权信息 **/
    private String guid;
    /** 二次拨号号码（1-24位数字或*或#） **/
    private String number;
}
