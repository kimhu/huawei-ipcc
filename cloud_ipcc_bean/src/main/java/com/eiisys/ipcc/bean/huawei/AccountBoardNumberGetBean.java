package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 1.2.2 查询接入码
 * 
 * @author hujm
 */

@Getter
@Setter
public class AccountBoardNumberGetBean {
    /** 企业帐号，已通过“创建企业”获取 **/
    private String accountId;
    /** 城市区号 **/
    private String cityId;
    /** 接入码查询数量 **/
    private Integer count;
}
