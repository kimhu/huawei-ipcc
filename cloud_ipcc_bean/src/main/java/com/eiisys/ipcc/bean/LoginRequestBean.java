package com.eiisys.ipcc.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录请求实体
 * 
 * @author hujm
 */

@Getter
@Setter
public class LoginRequestBean {
    /** 用户名 **/
    private String userName;
    /** 密码 **/
    private String password;
}
