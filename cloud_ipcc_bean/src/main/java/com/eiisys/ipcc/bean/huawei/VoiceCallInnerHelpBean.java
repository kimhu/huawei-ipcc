package com.eiisys.ipcc.bean.huawei;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.2.4 内部求助
 * 
 * @author hujm
 */

@Getter
@Setter
public class VoiceCallInnerHelpBean {
    /** 座席工号 **/
    @JSONField(serialize = false)
    private Integer agentid;
    /** 座席鉴权信息 **/
    @JSONField(serialize = false)
    private String guid;
    /** 求助对象，座席工号或技能队列ID **/
    private String dstaddress;
    /** 求助设备类型，技能队列为1，座席工号为2，默认为2 **/
    private Integer devicetype;
    /** 求助模式，两方求助为1，三方求助为2，默认为1 **/
    private Integer mode;
    /** 需设置的随路数据。内容可为空，最大长度为16k字符 **/
    private String callappdata;
}
