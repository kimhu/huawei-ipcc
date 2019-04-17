package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 4.2.3 下载录音文件
 * 
 * @author hujm
 */

@Getter
@Setter
public class CallBillDownloadRecordFileBean {
    /** 话单和录音索引文件名称，已调用“生成话单和录音索引”获取 **/
    private String billFileName;
}
