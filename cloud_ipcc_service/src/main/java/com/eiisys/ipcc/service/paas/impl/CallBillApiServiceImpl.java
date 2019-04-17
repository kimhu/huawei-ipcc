package com.eiisys.ipcc.service.paas.impl;

import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.eiisys.ipcc.bean.huawei.AccountBaseRequest;
import com.eiisys.ipcc.bean.huawei.CallBillDownloadBean;
import com.eiisys.ipcc.bean.huawei.CallBillDownloadRecordBean;
import com.eiisys.ipcc.bean.huawei.CallBillDownloadRecordFileBean;
import com.eiisys.ipcc.bean.huawei.CallBillDownloadRecordTextBean;
import com.eiisys.ipcc.bean.huawei.CallBillDownloadTrandferFileBean;
import com.eiisys.ipcc.bean.huawei.CallBillQueryBean;
import com.eiisys.ipcc.bean.huawei.CallBillTransferBean;
import com.eiisys.ipcc.bean.huawei.CallBillTransferStatusBean;
import com.eiisys.ipcc.constants.HuaweiConstants;
import com.eiisys.ipcc.core.huawei.utils.AuthorizationUtils;
import com.eiisys.ipcc.core.utils.OkhttpClientUtils;
import com.eiisys.ipcc.service.paas.CallBillApiService;

import lombok.extern.slf4j.Slf4j;

/**
 * 话单数据接口实现类
 * 
 * @author hujm
 */

@Service
@Slf4j
public class CallBillApiServiceImpl implements CallBillApiService {
    /** 4.2.1 生成话单和录音 **/
    private final String queryBillDataURI = "/CCFS/resource/ccfs/queryBillData";
    /** 4.2.2 下载话单和录音 **/
    private final String downloadBillURI = "/CCFS/resource/ccfs/downloadBillFile";
    /** 4.2.3 下载录音文件 **/
    private final String downloadRecordFileURI = "/CCFS/resource/ccfs/downloadRecordFile";
    /** 4.2.4 下载录音转成文字的文件（废弃） **/
    private final String downloadRecordTextURI = "/CCFS/resource/ccfs/downloadTransferFile";
    /** 4.2.5 查询并下载录音文件 **/
    private final String downloadRecordURI = "/CCFS/resource/ccfs/downloadRecord";
    /** 4.2.6 请求录音转写 **/
    private final String recordTransferURI = "/CCFS/resource/ccfs/requestRecordAnalysis";
    /** 4.2.7 查询录音转写任务状态 **/
    private final String recordTransferStatusURI = "/CCFS/resource/ccfs/queryRecordAnalysisStatus";
    /** 4.2.8 下载录音转写文件 **/
    private final String downloadTransferFileURI = "/CCFS/resource/ccfs/downloadRecordAnalysisFile";

    @Value("${huawei.server.bill.header}")
    private String serverHeader;
    @Value("${huawei.server.version}")
    private String version;
    @Value("${huawei.access.key}")
    private String accessKey;
    @Value("${huawei.secret.key}")
    private String secretKey;

    /** host要参与鉴权算法，只能ip：port的格式 **/
    private String host;

    public CallBillApiServiceImpl(@Value("${huawei.server.bill.ip}") String serverIp,
            @Value("${huawei.server.bill.port}") String port) {
        super();
        this.host = serverIp.concat(":").concat(port);
    }

    /**
     * 4.2.1 生成话单和录音
     * 
     * @param bean
     * @throws Excpetion
     */
    @Override
    public String queryBillData(CallBillQueryBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(queryBillDataURI);
        
        log.info("[Query Bill Data] param: {}", JSON.toJSONString(bean));

        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, queryBillDataURI, null, request);

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Query Bill Data] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Query Bill Data] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 4.2.2 下载话单和录音
     * 
     * @param bean
     * @throws Excpetion
     */
    @Override
    public void downloadBill(CallBillDownloadBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(downloadBillURI);
        
        log.info("[Download Bill] param: {}", JSON.toJSONString(bean));

        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, downloadBillURI, null, request);

        try {
            OkhttpClientUtils.syncDownloadPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
        } catch (Exception e) {
            log.error("[Download Bill] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 4.2.3 下载录音文件
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String downloadRecordFile(CallBillDownloadRecordFileBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(downloadRecordFileURI);
        
        log.info("[Download Record File] param: {}", JSON.toJSONString(bean));

        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, downloadRecordFileURI, null, request);

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Download Record File] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Download Record File] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 4.2.4 下载录音转成文字的文件（废弃）
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String downloadRecodText(CallBillDownloadRecordTextBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(downloadRecordTextURI);
        
        log.info("[Download Record Transfer] param: {}", JSON.toJSONString(bean));

        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, downloadRecordFileURI, null, request);

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Download Record Transfer] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Download Record Transfer] download record transfer file exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 4.2.5 查询并下载录音文件
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String downloadRecord(CallBillDownloadRecordBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(downloadRecordURI);
        
        log.info("[Download Record] param: {}", JSON.toJSONString(bean));

        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, downloadRecordFileURI, null, request);

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Download Record] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Download Record] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 4.2.6 请求录音转写
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String recordTransfer(CallBillTransferBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(recordTransferURI);
        
        log.info("[Record Transfer] param: {}", JSON.toJSONString(bean));

        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, recordTransferURI, null, request);

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Record Transfer] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Record Transfer] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 4.2.7 查询录音转写任务状态
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String recordTransferStatus(CallBillTransferStatusBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(recordTransferStatusURI);
        
        log.info("[Record Transfer Status] param: {}", JSON.toJSONString(bean));

        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, recordTransferStatusURI, null, request);

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Record Transfer Status] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Record Transfer Status] exception, e: {}", e);
            throw e;
        }
    }

    /**
     * 4.2.8 下载录音转写文
     * 
     * @param bean
     * @throws Exception
     */
    @Override
    public String downloadTransferFile(CallBillDownloadTrandferFileBean bean) throws Exception {
        String url = serverHeader.concat(host).concat(downloadTransferFileURI);
        
        log.info("[Downlaod Transfer Fil] param: {}", JSON.toJSONString(bean));

        AccountBaseRequest request = AuthorizationUtils.getBaseRequest(version, bean);
        Map<String, String> headers = AuthorizationUtils.getSignedHeader(host, accessKey, secretKey,
                HuaweiConstants.METHOD_POST, downloadTransferFileURI, null, request);

        try {
            String result = OkhttpClientUtils.syncPost(url, null, headers, request, OkhttpClientUtils.TYPE_JSON);
            log.info("[Downlaod Transfer File] response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("[Downlaod Transfer File] exception, e: {}", e);
            throw e;
        }
    }
}
