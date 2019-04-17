package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 1.3.4 查询所有技能队列
 * @author hujm
 */

@Getter
@Setter
public class AccountSkillsInfoGetBean {
    /** 技能队列所属的企业帐号，已通过“创建企业”获取 **/
    private String accountId;
}
