package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.1.11 请求休息
 * 
 * @author hujm
 */

@Getter
@Setter
public class AgentRestBean {
    /** 座席工号 **/
    private Integer agentid;
    /** 座席鉴权信息 **/
    private String guid;
    /** 休息时长。（以秒为单位，范围：1~86399） **/
    private Integer time;
    /** 休息原因码。（取值含义自定义，范围：0-255） **/
    private Integer restcause;
}
