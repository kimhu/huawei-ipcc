package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 1.2.1 创建企业
 * 
 * @author hujm
 */

@Getter
@Setter
public class AccountCreateBean {
    /** 企业昵称 **/
    private String accountNick;
    /** 企业公司名称 **/
    private String companyName;
    /** 企业联系人姓名 **/
    private String linkMan;
    /** 企业联系人电话 **/
    private String phone;
}
