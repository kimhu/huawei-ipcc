package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.1.20 设置是否接听来话
 * 
 * @author hujm
 */

@Getter
@Setter
public class AgentTalkRightSetBean {
    /** 座席工号 **/
    private Integer agentid;
    /** 座席鉴权信息 **/
    private String guid;
    /** 是否接听来话（1表示接听来话，0 表示不接听来话） **/
    private Integer flag;
}
