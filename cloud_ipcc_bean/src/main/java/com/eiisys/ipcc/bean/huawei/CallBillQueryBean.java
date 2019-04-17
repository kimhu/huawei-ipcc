package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 4.2.1 生成话单和录音
 * 
 * @author hujm
 */

@Getter
@Setter
public class CallBillQueryBean {
    /** 话单和录音记录的起始时间，起始时间和截止时间需要做范围限制，缺省为3天内。格式为：yyyy-MM-dd HH:mm:ss **/
    private String beginTime;
    /** 话单和录音记录的截止时间，起始时间和截止时间需要做范围限制，缺省为3天内。格式为：yyyy-MM-dd HH:mm:ss **/
    private String endTime;
    /** 企业帐号，可调用“查询企业帐号”查询 **/
    private String accountId;
    /** 座席ID，可调用“查询座席组”查询座席组中的座席ID **/
    private String agentId;
    /** 呼叫标识，可从已调用的“下载话单和录音索引”返回的文件中获取 **/
    private String callId;
    /** 主叫号码 **/
    private String callerNo;
    /** 被叫号码 **/
    private String calleeNo;
    /** 需生成的数据记录文件类型。 call：话单记录 record：录音记录索引 call_record：话单记录和录音记录索引 **/
    private String dataType;
    /** 回调地址 **/
    private String callBackURL;
}
