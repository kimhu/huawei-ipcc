package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 1.2.11 更新被叫配置
 * 
 * @author hujm
 */

@Getter
@Setter
public class AccountCallConfigUpdateBean {
    /** 被叫配置所属的企业帐号，已通过“创建企业”获取 **/
    private String accountId;
    /** 系统接入码 **/
    private String accessCode;
    /** 后续接入码 **/
    private String subCode;
    /** 目的设备类型。 IVR：IVR流程，SKILL：技能队列 **/
    private String deviceType;
    /** 目的设备ID **/
    private Integer deviceId;
}
