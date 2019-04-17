package com.eiisys.ipcc.controller;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eiisys.ipcc.bean.GenericResponse;
import com.eiisys.ipcc.bean.IpccDemoBean;
import com.eiisys.ipcc.bean.api.PaasBillCallBackBean;
import com.eiisys.ipcc.bean.huawei.AccountAgentsInfoGetBean;
import com.eiisys.ipcc.bean.huawei.AccountBaseRequest;
import com.eiisys.ipcc.bean.huawei.AccountBoardNumberGetBean;
import com.eiisys.ipcc.bean.huawei.AccountIvrFlowGetBean;
import com.eiisys.ipcc.bean.huawei.AccountRolesInfoGetBean;
import com.eiisys.ipcc.bean.huawei.AccountSkillsInfoGetBean;
import com.eiisys.ipcc.bean.huawei.AgentForceLogoutBean;
import com.eiisys.ipcc.bean.huawei.AgentGatewayEnum;
import com.eiisys.ipcc.bean.huawei.AgentHeartbeatBean;
import com.eiisys.ipcc.bean.huawei.AgentLoginBean;
import com.eiisys.ipcc.bean.huawei.AgentLogoutBean;
import com.eiisys.ipcc.bean.huawei.AgentStatusGetBean;
import com.eiisys.ipcc.bean.huawei.CallBillDataTypeEnum;
import com.eiisys.ipcc.bean.huawei.CallBillDownloadBean;
import com.eiisys.ipcc.bean.huawei.CallBillQueryBean;
import com.eiisys.ipcc.constants.HuaweiConstants;
import com.eiisys.ipcc.constants.RedisConstants;
import com.eiisys.ipcc.core.huawei.utils.AuthorizationUtils;
import com.eiisys.ipcc.core.util.RedisClient;
import com.eiisys.ipcc.core.utils.ResponseUtils;
import com.eiisys.ipcc.service.IpccDemoService;
import com.eiisys.ipcc.service.paas.AccountApiService;
import com.eiisys.ipcc.service.paas.AgentApiService;
import com.eiisys.ipcc.service.paas.CallBillApiService;
import com.eiisys.ipcc.service.wechat.WeChatApiService;
import com.google.common.collect.Maps;
import com.opencsv.CSVReader;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Api(value = "免费开通接口API", tags = {
                                   "前台接口：免费开通填写资料接口API" }, description = "需要关联控制台审核状态")
@Slf4j
@RestController
public class DemoController {

    @Autowired
    RedisClient redisClient;

    @Autowired
    IpccDemoService ipccDemoService;

    @Autowired
    AgentApiService agentService;

    @Autowired
    AccountApiService accountService;

    @Autowired
    CallBillApiService billService;

    @Autowired
    WeChatApiService wechatService;

    @ApiOperation(value = "测试controller接口--查询", notes = "测试测试测试测试测试", response = GenericResponse.class)
    @PostMapping(path = "queryDemoList")
    public GenericResponse queryDemoList() {
        GenericResponse genericResponse = ResponseUtils.genSuccessResponse(null);

        List<IpccDemoBean> demoList = ipccDemoService.getAllDemoData();
        genericResponse.setRows(demoList);

        redisClient.set("test_list", demoList);
        demoList = (List<IpccDemoBean>) redisClient.get("test_list");
        if (CollectionUtils.isEmpty(demoList)) {
            System.out.println("empty list");
        } else {
            System.out.println("list size = " + demoList.size());
        }

        return genericResponse;
    }

    @ApiOperation(value = "测试controller接口--插入", notes = "测试测试测试测试测试", response = GenericResponse.class)
    @PostMapping(path = "insert")
    public GenericResponse insert(@RequestBody IpccDemoBean bean) {
        GenericResponse genericResponse = ResponseUtils.genSuccessResponse(null);

        Integer insertCnt = ipccDemoService.insert(bean);
        genericResponse.setCount(insertCnt);

        return genericResponse;
    }

    @ApiOperation(value = "测试controller接口--查询(使用缓存和排他锁)", notes = "排他锁排他锁排他锁排他锁", response = GenericResponse.class)
    @PostMapping(path = "queryDemoListWithLock")
    public GenericResponse queryDemoListWithLock() {
        GenericResponse genericResponse = ResponseUtils.genSuccessResponse(null);

        List<IpccDemoBean> demoList = ipccDemoService.getIpccDemoWithLock();
        genericResponse.setRows(demoList);

        if (CollectionUtils.isEmpty(demoList)) {
            System.out.println("empty list");
        } else {
            System.out.println("list size = " + demoList.size());
        }

        return genericResponse;
    }

    @ApiOperation(value = "测试使用排他锁注解来控制数据插入", notes = "测试测试测试测试测试", response = GenericResponse.class)
    @PostMapping(path = "insertWithLockAnnotation")
    public GenericResponse insertWithLockAnnotation(@RequestBody IpccDemoBean bean) {
        GenericResponse genericResponse = ResponseUtils.genSuccessResponse(null);

        String lockKey = RedisConstants.RKEY_DEMO_INSERT_LOCK + bean.getDemoName();
        Integer insertCnt = ipccDemoService.insertWithLockAnnotation(bean, lockKey);
        genericResponse.setCount(insertCnt);

        return genericResponse;
    }

    @ApiOperation(value = "测试排他锁注解，控制同时只有一个线程查数据库", notes = "排他锁排他锁排他锁排他锁", response = GenericResponse.class)
    @PostMapping(path = "queryDemoListWithLockAnnotation")
    public GenericResponse queryDemoListWithLockAnnotation() {
        GenericResponse genericResponse = ResponseUtils.genSuccessResponse(null);

        String lockKey = RedisConstants.RKEY_DEMO_LIST_LOCK + "all";
        String cacheKey = RedisConstants.RKEY_DEMO_LIST + "all";
        List<IpccDemoBean> demoList = ipccDemoService.getIpccDemoWithLockAnnotation(lockKey, cacheKey, 100L);
        genericResponse.setRows(demoList);

        if (CollectionUtils.isEmpty(demoList)) {
            System.out.println("empty list");
        } else {
            System.out.println("list size = " + demoList.size());
        }

        return genericResponse;
    }

    @ApiOperation(value = "测试调用华为查询接入码接口", notes = "测试调用华为查询接入码接口", response = GenericResponse.class)
    @PostMapping(path = "getBoardNumber")
    public GenericResponse getBoardNumber() {
        GenericResponse response = ResponseUtils.genSuccessResponse(null);

        try {
            AccountBoardNumberGetBean bean = new AccountBoardNumberGetBean();
            bean.setAccountId("1010606");
            bean.setCount(10);

            String result = accountService.getBoardNumber(bean);
            System.out.println(result);
            response.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();

        return response;
    }

    @ApiOperation(value = "测试调用华为获取企业接口", notes = "测试调用华为获取企业接口", response = GenericResponse.class)
    @PostMapping(path = "getAccounts")
    public GenericResponse getAccounts() {
        GenericResponse response = ResponseUtils.genSuccessResponse(null);

        try {
            String result = accountService.getAccountsInfo();
            System.out.println(result);
            response.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();

        return response;
    }

    @ApiOperation(value = "测试调用华为查询所有坐席角色接口", notes = "测试调用华为查询所有坐席角色接口", response = GenericResponse.class)
    @PostMapping(path = "getRolesInfo")
    public GenericResponse getRolesInfo() {
        GenericResponse response = ResponseUtils.genSuccessResponse(null);

        try {
            AccountRolesInfoGetBean bean = new AccountRolesInfoGetBean();
            bean.setAccountId("1010606");

            String result = accountService.getRolesInfo(bean);
            System.out.println(result);
            response.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();

        return response;
    }

    @ApiOperation(value = "测试调用华为查询所有技能队列接口", notes = "测试调用华为查询所有技能队列接口", response = GenericResponse.class)
    @PostMapping(path = "getSkillsInfo")
    public GenericResponse getSkillsInfo() {
        GenericResponse response = ResponseUtils.genSuccessResponse(null);

        try {
            AccountSkillsInfoGetBean bean = new AccountSkillsInfoGetBean();
            bean.setAccountId("1010667");

            String result = accountService.getSkillsInfo(bean);
            System.out.println(result);
            response.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();

        return response;
    }

    @ApiOperation(value = "测试调用华为查询所有坐席接口", notes = "测试调用华为查询所有坐席接口", response = GenericResponse.class)
    @PostMapping(path = "getAgentsInfo")
    public GenericResponse getAgentsInfo() {
        GenericResponse response = ResponseUtils.genSuccessResponse(null);

        try {
            AccountAgentsInfoGetBean bean = new AccountAgentsInfoGetBean();
            bean.setAccountId("1010628");

            String result = accountService.getAgentsInfo(bean);
            System.out.println(result);
            response.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();

        return response;
    }

    @ApiOperation(value = "测试调用华为获取ivr流程接口", notes = "测试调用华为获取ivr流程接口", response = GenericResponse.class)
    @PostMapping(path = "getIvrFlow")
    public GenericResponse getIvrFlow() {
        GenericResponse response = ResponseUtils.genSuccessResponse(null);

        try {
            AccountIvrFlowGetBean bean = new AccountIvrFlowGetBean();
            bean.setAccountId("1010628");

            String result = accountService.getIvrFlowInfo(bean);
            System.out.println(result);
            response.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();

        return response;
    }

    @ApiOperation(value = "测试调用华为登录接口", notes = "测试调用华为登录接口", response = GenericResponse.class)
    @PostMapping(path = "passLogin")
    public GenericResponse login() {
        GenericResponse response = ResponseUtils.genSuccessResponse(null);

        try {
            AgentLoginBean bean = new AgentLoginBean();
            bean.setAgentid(42723);
            bean.setPassword("Chengxb1");
            bean.setPhonenum("999142723");
            bean.setAutoanswer(true);
            bean.setAutoenteridle(true);
            bean.setReleasephone(true);
            bean.setStatus(4);
            bean.setCheckInWebm(false);
            bean.setPushUrl(null);

            Map<String, String> result = agentService.login(bean);
            System.out.println(result.get(HuaweiConstants.HEAD_GUID));
            response.setMessage(result.get(HuaweiConstants.HEAD_GUID));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @ApiOperation(value = "测试调用华为强制登录接口", notes = "测试调用华为强制登录接口", response = GenericResponse.class)
    @PostMapping(path = "forceLogin")
    public GenericResponse froceLogin() {
        GenericResponse response = ResponseUtils.genSuccessResponse(null);

        try {
            AgentLoginBean bean = new AgentLoginBean();
            bean.setAgentid(42723);
            bean.setPassword("Chengxb1");
            bean.setPhonenum("999142723");
            bean.setAutoanswer(true);
            bean.setAutoenteridle(true);
            bean.setReleasephone(true);
            bean.setStatus(4);
            bean.setCheckInWebm(false);
            bean.setPushUrl(null);

            Map<String, String> result = agentService.forceLogin(bean);
            System.out.println(result.get(HuaweiConstants.HEAD_GUID));
            response.setMessage(result.get(HuaweiConstants.HEAD_GUID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();

        return response;
    }

    @ApiOperation(value = "测试调用华为心跳检测接口", notes = "测试调用华为心跳检测接口", response = GenericResponse.class)
    @PostMapping(path = "heartbeat")
    public GenericResponse heartbeat() {
        GenericResponse response = ResponseUtils.genSuccessResponse(null);

        try {
            AgentHeartbeatBean bean = new AgentHeartbeatBean();
            bean.setAgentid(42723);
            bean.setGuid("aa8eeb703e6396698a23cf4869995edf1dd1e4c931d6c92f.AgentGateway0");

            String result = agentService.heartbeat(bean);
            System.out.println(result);
            response.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();

        return response;
    }

    @ApiOperation(value = "测试调用华为获取当前坐席状态接口", notes = "测试调用华为当前坐席状态接口", response = GenericResponse.class)
    @PostMapping(path = "getStatus")
    public GenericResponse getStatus() {
        GenericResponse response = ResponseUtils.genSuccessResponse(null);

        try {
            AgentStatusGetBean bean = new AgentStatusGetBean();
            bean.setAgentid(42723);
            bean.setGuid("c0f396e866f5ebfbe7ad556a949d0b61b710aecbc0c567e0.AgentGateway0");

            String result = agentService.getAgentStatus(bean);
            System.out.println(result);
            response.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();

        return response;
    }

    @ApiOperation(value = "测试调用华为签出接口", notes = "测试调用华为签出接口", response = GenericResponse.class)
    @PostMapping(path = "paasLogout")
    public GenericResponse logout() {
        GenericResponse response = ResponseUtils.genSuccessResponse(null);

        try {
            AgentLogoutBean bean = new AgentLogoutBean();
            bean.setAgentid(42723);
            bean.setGuid("c0f396e866f5ebfbe7ad556a949d0b61b710aecbc0c567e0.AgentGateway0");

            String result = agentService.logout(bean);
            System.out.println(result);
            response.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();

        return response;
    }

    @ApiOperation(value = "测试调用华为强制签出接口", notes = "测试调用华为强制签出接口", response = GenericResponse.class)
    @PostMapping(path = "forceLogout")
    public GenericResponse froceLogout() {
        GenericResponse response = ResponseUtils.genSuccessResponse(null);

        try {
            AgentForceLogoutBean bean = new AgentForceLogoutBean();
            bean.setAgentid(42723);
            bean.setGuid("24b16a49b380dfffd2f6caaeea7c9080d89a5f864740dd6c.AgentGateway0");
            bean.setReason(1);

            String result = agentService.forceLogout(bean);
            System.out.println(result);
            response.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();

        return response;
    }

    @ApiOperation(value = "测试华为话单回调接口", notes = "测试华为话单回调接", response = GenericResponse.class)
    @PostMapping(path = "getBillData")
    public GenericResponse getBillData(HttpServletRequest request,
            @RequestBody(required = false) PaasBillCallBackBean bean)
            throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        GenericResponse response = ResponseUtils.genSuccessResponse(null);

        System.out.println("Authoriz3ation: " + request.getHeader("Authorization"));
        System.out.println("Host: " + request.getHeader("Host"));
        System.out.println("remote: " + request.getRemoteHost());
        System.out.println("http://122.235.164.99:8101/ipcc/getBillData");
        System.out.println(JSONObject.toJSONString(bean));

        /*
         * AccountBaseRequest request1 = AuthorizationUtils.getBaseRequest("2.0", bean);
         * try { Map<String, String> headers =
         * AuthorizationUtils.getSignedHeader("117.78.52.74:8442",
         * "279a08bbd8d949a08a82d9d90db5fd47", "Ln&7qy6b5I6nE@m331",
         * HuaweiConstants.METHOD_POST, "/CCFS/resource/ccfs/queryBillData", null,
         * request1); System.out.println(headers.get("Authorization")); } catch
         * (InvalidKeyException | NoSuchAlgorithmException |
         * UnsupportedEncodingException e) { // TODO Auto-generated catch block
         * e.printStackTrace(); }
         */

        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(bean));
        CallBillDownloadBean bean1 = new CallBillDownloadBean();
        bean1.setBillFileName(object.getJSONObject("msgBody").getString("billFileName"));

        /*
         * AccountBaseRequest request1 = AuthorizationUtils.getBaseRequest("2.0",
         * bean1); Map<String, String> headers =
         * AuthorizationUtils.getSignedHeader("117.78.52.74:8443",
         * "279a08bbd8d949a08a82d9d90db5fd47", "Ln&7qy6b5I6nE@m331",
         * HuaweiConstants.METHOD_POST, "/CCFS/resource/ccfs/downloadBillFile", null,
         * request1);
         * 
         * okhttp3.Request.Builder requestBuilder = new Request.Builder();
         * requestBuilder = requestBuilder.url(
         * "https://117.78.52.74:8443/CCFS/resource/ccfs/downloadBillFile");
         * 
         * if (headers != null) { Headers.Builder headerBuilder = new Headers.Builder();
         * 
         * for (Map.Entry<String, String> entry : headers.entrySet()) {
         * headerBuilder.add(entry.getKey(), entry.getValue()); } requestBuilder =
         * requestBuilder.headers(headerBuilder.build()); }
         * 
         * okhttp3.RequestBody requestBody =
         * okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"
         * ), JSON.toJSONString(bean1)); requestBuilder =
         * requestBuilder.post(requestBody); okhttp3.Request request2 =
         * requestBuilder.build();
         * 
         * try (okhttp3.Response response1 = client.newCall(request2).execute();) {
         * InputStream inputStream = response1.body().byteStream(); byte[] buffer = new
         * byte[1024]; int len = 0; ByteArrayOutputStream bos = new
         * ByteArrayOutputStream(); while((len = inputStream.read(buffer)) != -1) {
         * bos.write(buffer, 0, len); } bos.close(); File file = new
         * File("D:\\test\\record.csv"); FileOutputStream fos = new
         * FileOutputStream(file); fos.write(bos.toByteArray()); if(fos != null){
         * fos.close(); } if(inputStream != null){ inputStream.close(); } } catch
         * (IOException e) { e.printStackTrace(); }
         */

        try {
            billService.downloadBill(bean1);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return response;
    }

    @ApiOperation(value = "测试调用华为生成话单接口", notes = "测试调用华为生成话单接口", response = GenericResponse.class)
    @PostMapping(path = "queryBillData")
    public GenericResponse queryBillData() {
        GenericResponse response = ResponseUtils.genSuccessResponse(null);

        try {
            CallBillQueryBean bean = new CallBillQueryBean();
            bean.setBeginTime("2019-04-03 09:00:00");
            bean.setEndTime("2019-04-03 12:00:00");
            bean.setDataType(CallBillDataTypeEnum.getDataType(0));
            bean.setCallBackURL("http://ipcc.1shen7.com/ipcc/getBillData");

            String result = billService.queryBillData(bean);
            System.out.println(result);
            response.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();

        return response;
    }

    @ApiOperation(value = "测试调用微信发送模板消息接口", notes = "测试调用微信发送模板消息接口", response = GenericResponse.class)
    @PostMapping(path = "sendMessage")
    public GenericResponse sendMessage() {
        GenericResponse response = ResponseUtils.genSuccessResponse(null);

        try {
            String errorFrom = "saas平台";
            String functionName = "扣费功能";
            String errorMessage = "隐私小号话费扣费失败";
            String companyName = "测试";
            String result = wechatService.sendMessage(errorFrom, functionName, errorMessage, companyName);
            System.out.println(result);
            response.setMessage(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();

        return response;
    }

    public static void main(String[] args) {
        try {
            String path = "D:\\test\\record.zip";
            ZipFile zf = new ZipFile(path);
            InputStream in = new BufferedInputStream(new FileInputStream(path));
            Charset gbk = Charset.forName("UTF-8");
            ZipInputStream zin = new ZipInputStream(in, gbk);
            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.toString().endsWith("csv")) {
                    /*
                     * BufferedReader br = new BufferedReader( new
                     * InputStreamReader(zf.getInputStream(ze))); String line; while((line =
                     * br.readLine()) != null){ System.out.println(line.toString()); } br.close();
                     */

                    BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                    CSVReader reader = new CSVReader(br);

                    // t把内容添加到list中
                    List<String[]> li = reader.readAll();
                    System.out.println("总共行数是：  " + li.size());
                    //Iterator<String[]> i1 = li.iterator();
                    for(int i = 1; i < li.size(); i++) {
                    // I遍历每个值
                    //while (i1.hasNext()) {

                        String[] str = li.get(i);

                        System.out.print(i + "行的值为 ");

                        for (int j = 0; j < str.length; j++) {

                            System.out.print("  " + str[j].trim());

                        }
                        //System.out.println();
                        System.out.println();
                    }
                    reader.close();
                    //}
                }
            }
            zin.closeEntry();
            zin.close();
            zf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
