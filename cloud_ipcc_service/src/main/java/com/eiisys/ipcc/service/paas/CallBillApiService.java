package com.eiisys.ipcc.service.paas;

import java.io.InputStream;

import com.eiisys.ipcc.bean.huawei.CallBillDownloadBean;
import com.eiisys.ipcc.bean.huawei.CallBillDownloadRecordBean;
import com.eiisys.ipcc.bean.huawei.CallBillDownloadRecordFileBean;
import com.eiisys.ipcc.bean.huawei.CallBillDownloadRecordTextBean;
import com.eiisys.ipcc.bean.huawei.CallBillDownloadTrandferFileBean;
import com.eiisys.ipcc.bean.huawei.CallBillQueryBean;
import com.eiisys.ipcc.bean.huawei.CallBillTransferBean;
import com.eiisys.ipcc.bean.huawei.CallBillTransferStatusBean;

/**
 * 话单数据接口
 * 
 * @author hujm
 */
public interface CallBillApiService {
    /** 4.2.1 生成话单和录音 **/
    public String queryBillData(CallBillQueryBean bean) throws Exception;

    /** 4.2.2 下载话单和录音 **/
    public void downloadBill(CallBillDownloadBean bean) throws Exception;

    /** 4.2.3 下载录音文件 **/
    public String downloadRecordFile(CallBillDownloadRecordFileBean bean) throws Exception;

    /** 4.2.4 下载录音转成文字的文件（废弃） **/
    public String downloadRecodText(CallBillDownloadRecordTextBean bean) throws Exception;

    /** 4.2.5 查询并下载录音文件 **/
    public String downloadRecord(CallBillDownloadRecordBean bean) throws Exception;

    /** 4.2.6 请求录音转写 **/
    public String recordTransfer(CallBillTransferBean bean) throws Exception;

    /** 4.2.7 查询录音转写任务状态 **/
    public String recordTransferStatus(CallBillTransferStatusBean bean) throws Exception;

    /** 4.2.8 下载录音转写文件 **/
    public String downloadTransferFile(CallBillDownloadTrandferFileBean bean) throws Exception;
}
