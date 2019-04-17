package com.eiisys.ipcc.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 个人可配置项返回实体
 * 
 * @author hujm
 */

@Getter
@Setter
public class LoginResponseIndefierBean {
    /** 是否是试用账号 **/
    private Byte isTrial;
    /** 是否是华为账号 **/
    private Byte isHuawei;

    /** 是否绑定sip号码 **/
    private Byte isSip;
    /** 公司是否绑定隐私小号 **/
    private Byte hasPn;
    /** 个人是否绑定隐私小号 **/
    private Byte isPn;
}
