package com.eiisys.ipcc.core.huawei.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.eiisys.ipcc.bean.huawei.AccountBaseRequest;
import com.eiisys.ipcc.bean.huawei.AccountBaseRequestHeader;
import com.eiisys.ipcc.constants.HuaweiConstants;

public class AuthorizationUtils {
    /**
     * 根据参数构造华为http请求头
     * 
     * @param bean
     * @return
     */
    public static AccountBaseRequest getBaseRequest(String version, Object bean) {
        AccountBaseRequest request = new AccountBaseRequest();
        request.setRequest(new AccountBaseRequestHeader(version));
        request.setMsgBody(bean);
        return request;
    }
    
    /**
     * 构造华为鉴权字段信息
     * 
     * @param method 定义为GET POST PUT DELETE这四种，固定值
     * @param uri 请求的uri
     * @param queryMap 如果是get请求，传入查询参数(传入的原始参数，非编码后的)
     * @param params post等请求的时候带的body体
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static Map<String, String> getSignedHeader(String host, String accessKey, String secretKey, String method,
            String uri, Map<String, String> queryMap, Object params)
            throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String authString = null;

        Map<String, String> headers = new HashMap<>();
        headers.put("host", host);

        SignInfo signInfo = new SignInfo();
        signInfo.setAccessKey(accessKey);
        signInfo.setSecretKey(secretKey);
        signInfo.setTimestamp(new Date());
        signInfo.setHttpMethod(method);
        signInfo.setUri(uri);
        signInfo.setQueryParameters(queryMap);
        if (null != params) {
            signInfo.setPayload(JSON.toJSONString(params));
        }
        signInfo.setSignedHeaders(headers);

        authString = signInfo.authString();

        Map<String, String> signedHeaders = new HashMap<>();
        signedHeaders.put(HuaweiConstants.HEAD_CONTENT_TYPE, HuaweiConstants.TYPE_JSON);
        signedHeaders.put(HuaweiConstants.HEAD_HOST, host);
        signedHeaders.put(HuaweiConstants.HEAD_AUTHORIZATION, authString);
        return signedHeaders;
    }
}
