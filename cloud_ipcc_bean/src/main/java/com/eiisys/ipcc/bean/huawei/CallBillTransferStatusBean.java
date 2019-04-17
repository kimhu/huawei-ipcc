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
public class CallBillTransferStatusBean {
    /** 录音转写任务唯一id，通过请求录音转写接口生成 **/
    private String taskId;
}
