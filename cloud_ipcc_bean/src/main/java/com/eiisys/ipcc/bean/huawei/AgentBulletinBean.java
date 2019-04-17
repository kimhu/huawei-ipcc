package com.eiisys.ipcc.bean.huawei;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;

/**
 * 2.1.16 座席发布公告
 * 
 * @author hujm
 */

@Getter
@Setter
public class AgentBulletinBean {
    /** 座席工号 **/
    @JSONField(serialize = false)
    private Integer agentid;
    /** 座席鉴权信息 **/
    @JSONField(serialize = false)
    private String guid;
    /** 公告发布类型。0: 班组；1: 技能队列 **/
    private Integer targettype;
    /** 公告数据。最大长度为900个字符 **/
    private String bulletindata;
    /** 班组名称或技能队列名称。最大长度为100个字符 **/
    private Integer targetname;
}
