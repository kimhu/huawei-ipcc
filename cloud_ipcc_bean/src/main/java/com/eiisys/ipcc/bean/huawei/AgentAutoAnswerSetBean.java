package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.1.6 设置是否自应答
 * 
 * @author hujm
 */
@Getter
@Setter
public class AgentAutoAnswerSetBean {
    /** 座席工号 **/
    private Integer agentid;
    /** 是否自动应答标志。true为自动应答，false为手动应答 **/
    private String isautoanswer;
    /** 座席鉴权信息 **/
    private String guid;
}
