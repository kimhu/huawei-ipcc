package com.eiisys.ipcc.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 个人固定配置项返回实体
 * 
 * @author hujm
 */

@Getter
@Setter
public class LoginResponseConfigBean {
    private Byte isSms;
    private Byte globalNumberCover;
    /** 允许发送短信的客户标签 **/
    private String smsPost;
    /** 短信开关 **/
    private Byte smsSwitch;
}
