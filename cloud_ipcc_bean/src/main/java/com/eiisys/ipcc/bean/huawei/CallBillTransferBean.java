package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 4.2.6 请求录音转写
 * 
 * @author hujm
 */

@Getter
@Setter
public class CallBillTransferBean {
    /** 企业帐号，可调用“查询企业帐号”接口查询 **/
    private String accountId;
    /** 呼叫标识Id列表，最多20个 **/
    private String[] callIds;
    /** 回调地址 **/
    private String callBackURL;
}
