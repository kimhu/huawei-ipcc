package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 1.3.5 删除指定技能队列
 * 
 * @author hujm
 */

@Getter
@Setter
public class AccountSkillDeleteBean {
    /** 技能队列所属的企业帐号，已通过“创建企业”获取 **/
    private String accountId;
    /** 技能队列ID，已通过“创建技能队列”获取 **/
    private Integer skillId;
}
