package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 1.3.3 创建技能队列
 * 
 * @author hujm
 */

@Getter
@Setter
public class AccountSkillCreateBean {
    /** 技能队列所属的企业帐号，已通过“创建企业”获取 **/
    private String accountId;
    /** 技能名称。只能由数字，字母，下划线，汉字组成。长度不超过20个字符，一个汉字占两个字符 **/
    private String skillName;
    /** 媒体类型。 WebChat：文字交谈，Voice：普通语音 **/
    private AccountSkillMediaType mediaType;
}
