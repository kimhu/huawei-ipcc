package com.eiisys.ipcc.bean.huawei;

import lombok.Getter;
import lombok.Setter;

/**
 * 4.2.4 下载录音转成文字的文件（废弃）
 * 
 * @author hujm
 */

@Getter
@Setter
public class CallBillDownloadRecordTextBean {
    /** 话单和录音索引文件名称，已调用“生成话单和录音索引”获取 **/
    private String billFileName;
}
