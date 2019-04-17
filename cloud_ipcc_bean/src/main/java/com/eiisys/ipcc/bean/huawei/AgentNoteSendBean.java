package com.eiisys.ipcc.bean.huawei;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.1.17 发送便签(扩展)
 * 
 * @author hujm
 */

@Getter
@Setter
public class AgentNoteSendBean {
    /** 座席工号 **/
    @JSONField(serialize = false)
    private Integer agentid;
    /** 座席鉴权信息 **/
    @JSONField(serialize = false)
    private String guid;
    /** 便签要发送给的座席工号。最大100个座席工号 **/
    private Integer[] agentIds;
    /** 便签内容。长度0~1024位字符 **/
    private String content;
}
