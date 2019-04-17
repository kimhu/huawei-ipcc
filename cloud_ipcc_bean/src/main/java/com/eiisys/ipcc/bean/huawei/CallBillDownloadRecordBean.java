package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 4.2.5 查询并下载录音文件
 * 
 * @author hujm
 */

@Getter
@Setter
public class CallBillDownloadRecordBean {
    /** 录音文件名，fileName和callId参数值必须至少选填一个 **/
    private String billFileName;
    /** 呼叫标识Id **/
    private String callId;
    /** 呼叫中心Id **/
    private String ccId;
}
