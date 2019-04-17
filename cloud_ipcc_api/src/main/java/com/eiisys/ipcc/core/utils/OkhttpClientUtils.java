package com.eiisys.ipcc.core.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eiisys.ipcc.constants.HuaweiConstants;
import com.eiisys.ipcc.exception.IpccException;
import com.google.common.base.Throwables;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
public class OkhttpClientUtils {

    public static final MediaType TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType TYPE_FORM = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    private static final String PROTOCOL_TYPE = "TLSv1.2";

    private static OkHttpClient client;
    private static ConnectionPool connectionPool;

    static {
        connectionPool = new ConnectionPool(100, 1, TimeUnit.MINUTES);// 默认

        SSLSocketFactory socketFactory = createSSLSocketFactory();
        OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).followSslRedirects(true)
                .sslSocketFactory(socketFactory, new TrustAllCerts()).connectionPool(connectionPool)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
        client = clientBuilder.build();

    }

    /**
     * 发送异步http post 请求
     * 
     * @param url 请求地址
     * @param reqBodyObj 请求body对象，可空
     * @param mediaType OkhttpClientUtils.TYPE_JSON 请求报文以json形式发送 <BR>
     *            OkhttpClientUtils.TYPE_FORM 请求报文以 form 形式发送
     * @param responseCallback 异步回调接口
     * @return
     */
    public static String asyncPost(String url, Object reqBodyObj, MediaType mediaType, Callback responseCallback) {
        return asyncPost(url, null, null, reqBodyObj, mediaType, responseCallback);
    }

    /**
     * 发送异步http post 请求
     * 
     * @param url 请求地址
     * @param paramMap url参数，可空
     * @param headerMap header参数，可空
     * @param reqBodyObj 请求body对象，可空
     * @param mediaType OkhttpClientUtils.TYPE_JSON 请求报文以json形式发送 <BR>
     *            OkhttpClientUtils.TYPE_FORM 请求报文以 form 形式发送
     * @param responseCallback 异步回调接口
     * @return
     */
    public static String asyncPost(String url, Map<String, String> paramMap, Map<String, String> headerMap,
            Object reqBodyObj, MediaType mediaType, Callback responseCallback) {
        url = url.trim().replaceAll(" ", "%20").replaceAll("&amp;", "&");

        String result = "";

        Request request = genPostRequest(url, paramMap, headerMap, reqBodyObj, mediaType);

        try {
            client.newCall(request).enqueue(responseCallback);
        } catch (IllegalStateException e) {
            result = "Error happened. Check logs.";
            log.error("IOException happened when asyncPost request: \\r\\n " + request.url().uri().toString()
                    + " \\r\\n Headers: " + request.headers() + " \\r\\n Body: " + JSON.toJSONString(reqBodyObj)
                    + " \\r\\n Exception: " + Throwables.getStackTraceAsString(e));
            // throw new CallException("510002");// 系统异常
        } catch (Exception e) {
            result = "Exception happened. Check logs.";
            log.error("Exception happened when asyncPost request: \\r\\n " + request.url().uri().toString()
                    + " \\r\\n Headers: " + request.headers() + " \\r\\n Body: " + JSON.toJSONString(reqBodyObj)
                    + " \\r\\n Exception: " + Throwables.getStackTraceAsString(e));

            // throw new CallException("510002");// 系统异常
        }

        return result;
    }

    /**
     * 发送同步http post 请求
     * 
     * @param url 请求地址
     * @param reqBodyObj 请求body对象，可空
     * @param mediaType OkhttpClientUtils.TYPE_JSON 请求报文以json形式发送 <BR>
     *            OkhttpClientUtils.TYPE_FORM 请求报文以 form 形式发送
     * @return
     */
    public static String syncPost(String url, Object reqBodyObj, MediaType mediaType) {

        return syncPost(url, null, null, reqBodyObj, mediaType);
    }

    /**
     * 发送同步http post 请求
     * 
     * @param url 请求地址
     * @param paramMap url参数，可空
     * @param headerMap header参数，可空
     * @param reqBodyObj 请求body对象，可空
     * @param mediaType OkhttpClientUtils.TYPE_JSON 请求报文以json形式发送 <BR>
     *            OkhttpClientUtils.TYPE_FORM 请求报文以 form 形式发送
     * @return 响应字符串
     */
    public static String syncPost(String url, Map<String, String> paramMap, Map<String, String> headerMap,
            Object reqBodyObj, MediaType mediaType) {
        url = url.trim().replaceAll(" ", "%20").replaceAll("&amp;", "&");

        String result = "";

        Request request = genPostRequest(url, paramMap, headerMap, reqBodyObj, mediaType);

        try (Response response = client.newCall(request).execute();) {
            if (response.isSuccessful() || (response.code() >= 400 && response.code() < 500)
                    || (response.code() >= 500)) {
                result = response.body().string();
            } else {

                System.out.println("Unhandled response code received: code=" + response.code());
                // throw new CallException("510002", null);// 系统异常
            }

        } catch (IOException e) {
            result = "IOException happened. Check logs.";
            log.error("IOException happened when post request: \\r\\n " + request.url().uri().toString()
                    + " \\r\\n Headers: " + request.headers() + " \\r\\n Body: " + JSON.toJSONString(reqBodyObj)
                    + " \\r\\n Exception: " + Throwables.getStackTraceAsString(e));
            // throw new CallException("510002", null);// 系统异常
        } catch (Exception e) {
            result = "Exception happened. Check logs.";
            log.error("Exception happened when post request: \\r\\n " + request.url().uri().toString()
                    + " \\r\\n Headers: " + request.headers() + " \\r\\n Body: " + JSON.toJSONString(reqBodyObj)
                    + " \\r\\n Exception: " + Throwables.getStackTraceAsString(e));
            // throw new CallException("510002", null);// 系统异常
        }

        return result;
    }
    
    public static void syncDownloadPost(String url, Map<String, String> paramMap, Map<String, String> headerMap,
            Object reqBodyObj, MediaType mediaType) {
        url = url.trim().replaceAll(" ", "%20").replaceAll("&amp;", "&");


        Request request = genPostRequest(url, paramMap, headerMap, reqBodyObj, mediaType);

        try (Response response = client.newCall(request).execute();) {
            if (response.isSuccessful() || (response.code() >= 400 && response.code() < 500)
                    || (response.code() >= 500)) {
                InputStream in = response.body().byteStream();
                
                if (in != null) {
                    try {
                        // 将上面生成的文件格式字符串 fileStr，还原成文件显示
                        File file = new File("D:\\test\\record.zip");
                        FileOutputStream fos = new FileOutputStream(file);
                        try {
                            byte[] buffer = new byte[1024];
                            int reader = 0;
                            while ((reader = in.read(buffer)) != -1) {
                                fos.write(buffer, 0, reader);
                            }
                            fos.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            fos.close();
                            in.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {

                System.out.println("Unhandled response code received: code=" + response.code());
                // throw new CallException("510002", null);// 系统异常
            }

        } catch (IOException e) {
            log.error("IOException happened when post request: \\r\\n " + request.url().uri().toString()
                    + " \\r\\n Headers: " + request.headers() + " \\r\\n Body: " + JSON.toJSONString(reqBodyObj)
                    + " \\r\\n Exception: " + Throwables.getStackTraceAsString(e));
            // throw new CallException("510002", null);// 系统异常
        } catch (Exception e) {
            log.error("Exception happened when post request: \\r\\n " + request.url().uri().toString()
                    + " \\r\\n Headers: " + request.headers() + " \\r\\n Body: " + JSON.toJSONString(reqBodyObj)
                    + " \\r\\n Exception: " + Throwables.getStackTraceAsString(e));
            // throw new CallException("510002", null);// 系统异常
        }

    }


    /**
     * 发送get 请求
     * 
     * @param url 请求地址
     * @param paramMap url参数，可空
     * @param headerMap header参数，可空
     * @return 响应字符串
     */
    public static String get(String url, Map<String, String> paramMap, Map<String, String> headerMap) throws Exception {
        String result = null;

        Request request = genGetRequest(url, paramMap, headerMap);

        try (Response response = client.newCall(request).execute();) {
            if (response.isSuccessful() || (response.code() >= 400 && response.code() < 500)
                    || (response.code() >= 500)) {
                result = response.body().string();
            } else {

                System.out.println("Unhandled response code received: code=" + response.code());
                throw new IpccException("510002", null);// 系统异常
            }

        } catch (IOException e) {
            result = "IOException happened. Check logs.";
            log.error("IOException happened when post request: \\r\\n " + request.url().uri().toString()
                    + " \\r\\n Headers: " + request.headers() + " \\r\\n Exception: "
                    + Throwables.getStackTraceAsString(e));
            throw new IpccException("510002", null);// 系统异常
        } catch (Exception e) {
            result = "Exception happened. Check logs.";
            log.error("Exception happened when post request: \\r\\n " + request.url().uri().toString()
                    + " \\r\\n Headers: " + request.headers() + " \\r\\n Exception: "
                    + Throwables.getStackTraceAsString(e));
            throw new IpccException("510002", null);// 系统异常
        }

        return result;
    }

    /**
     * 发送put 请求
     * 
     * @param url 请求地址
     * @param paramMap url参数，可空
     * @param headerMap header参数，可空
     * @return 响应字符串
     */
    public static Map<String, String> put(String url, Map<String, String> paramMap, Map<String, String> headerMap, Object reqBodyObj,
            MediaType mediaType) {
        Map<String, String> result = new HashMap<>();

        Request request = genPutRequest(url, paramMap, headerMap, reqBodyObj, mediaType);

        try (Response response = client.newCall(request).execute();) {
            if (response.isSuccessful() || (response.code() >= 400 && response.code() < 500)
                    || (response.code() >= 500)) {
                String guid = response.header("Set-GUID");
                String body = response.body().string();
                result.put(HuaweiConstants.HEAD_GUID, guid);
                result.put(HuaweiConstants.RESPOSE_BODY, body);
            } else {

                System.out.println("Unhandled response code received: code=" + response.code());
                throw new IpccException("510002", null);// 系统异常
            }

        } catch (IOException e) {
            log.error("IOException happened when post request: \\r\\n " + request.url().uri().toString()
                    + " \\r\\n Headers: " + request.headers() + " \\r\\n Exception: "
                    + Throwables.getStackTraceAsString(e));
            throw new IpccException("510002", null);// 系统异常
        } catch (Exception e) {
            log.error("Exception happened when post request: \\r\\n " + request.url().uri().toString()
                    + " \\r\\n Headers: " + request.headers() + " \\r\\n Exception: "
                    + Throwables.getStackTraceAsString(e));
            throw new IpccException("510002", null);// 系统异常
        }

        return result;
    }

    /**
     * 发送delete 请求
     * 
     * @param url 请求地址
     * @param paramMap url参数，可空
     * @param headerMap header参数，可空
     * @return 响应字符串
     */
    public static String delete(String url, Map<String, String> paramMap, Map<String, String> headerMap,
            Object reqBodyObj) {
        String result = null;

        Request request = genDeleteRequest(url, paramMap, headerMap, reqBodyObj);

        try (Response response = client.newCall(request).execute();) {
            if (response.isSuccessful() || (response.code() >= 400 && response.code() < 500)
                    || (response.code() >= 500)) {
                result = response.body().string();
            } else {

                System.out.println("Unhandled response code received: code=" + response.code());
                throw new IpccException("510002", null);// 系统异常
            }

        } catch (IOException e) {
            result = "IOException happened. Check logs.";
            log.error("IOException happened when post request: \\r\\n " + request.url().uri().toString()
                    + " \\r\\n Headers: " + request.headers() + " \\r\\n Exception: "
                    + Throwables.getStackTraceAsString(e));
            throw new IpccException("510002", null);// 系统异常
        } catch (Exception e) {
            result = "Exception happened. Check logs.";
            log.error("Exception happened when post request: \\r\\n " + request.url().uri().toString()
                    + " \\r\\n Headers: " + request.headers() + " \\r\\n Exception: "
                    + Throwables.getStackTraceAsString(e));
            throw new IpccException("510002", null);// 系统异常
        }

        return result;
    }

    /**
     * 生成 post request 对象
     * 
     * @param url
     * @param paramMap
     * @param headerMap
     * @param reqBodyObj
     * @param mediaType
     * @return
     */
    private static Request genPostRequest(String url, Map<String, String> paramMap, Map<String, String> headerMap,
            Object reqBodyObj, MediaType mediaType) {
        Request.Builder requestBuilder = new Request.Builder();

        // path parameter
        if (paramMap != null) {
            boolean hasParam = false;
            if (url.indexOf("?") > 0) {
                hasParam = true;
            }
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                if (!hasParam) {
                    url += "?";
                    hasParam = true;
                } else {
                    url += "&";
                }

                url += entry.getKey() + "=" + entry.getValue();
            }
        }

        requestBuilder = requestBuilder.url(url);

        // header
        if (headerMap != null) {
            Headers.Builder headerBuilder = new Headers.Builder();

            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                headerBuilder.add(entry.getKey(), entry.getValue());
            }

            requestBuilder = requestBuilder.headers(headerBuilder.build());
        }

        // request body
        RequestBody requestBody = null;
        String requestBodyStr = "";

        if (TYPE_FORM.equals(mediaType)) {
            if (reqBodyObj != null) {

                Map<String, Object> bodyObject = null;

                if (reqBodyObj instanceof Map) {
                    bodyObject = (Map) reqBodyObj;
                } else {
                    bodyObject = (JSONObject) JSONObject.toJSON(reqBodyObj);
                }

                StringBuilder sbBuilder = new StringBuilder();
                for (Map.Entry<String, Object> entry : bodyObject.entrySet()) {
                    sbBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }

                requestBodyStr = sbBuilder.toString().substring(1);
            }

            requestBody = RequestBody.create(TYPE_FORM, requestBodyStr);
        } else {
            requestBodyStr = JSON.toJSONString(reqBodyObj);
            requestBody = RequestBody.create(TYPE_JSON, requestBodyStr);
        }

        requestBuilder = requestBuilder.post(requestBody);

        Request request = requestBuilder.build();

        return request;
    }

    /**
     * 生成 get request 对象
     * 
     * @param url
     * @param paramMap
     * @param headerMap
     * @return
     */
    private static Request genGetRequest(String url, Map<String, String> paramMap, Map<String, String> headerMap) {
        Request.Builder requestBuilder = new Request.Builder();

        // path parameter
        if (paramMap != null) {
            boolean hasParam = false;
            if (url.indexOf("?") > 0) {
                hasParam = true;
            }
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                if (!hasParam) {
                    url += "?";
                    hasParam = true;
                } else {
                    url += "&";
                }

                url += entry.getKey() + "=" + entry.getValue();
            }
        }

        requestBuilder = requestBuilder.url(url);

        // header
        if (headerMap != null) {
            Headers.Builder headerBuilder = new Headers.Builder();

            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                headerBuilder.add(entry.getKey(), entry.getValue());
            }

            requestBuilder = requestBuilder.headers(headerBuilder.build());
        }

        requestBuilder = requestBuilder.get();
        Request request = requestBuilder.build();

        return request;
    }

    /**
     * 生成 put request 对象
     * 
     * @param url
     * @param paramMap
     * @param headerMap
     * @return
     */
    private static Request genPutRequest(String url, Map<String, String> paramMap, Map<String, String> headerMap,
            Object reqBodyObj, MediaType mediaType) {
        Request.Builder requestBuilder = new Request.Builder();

        // path parameter
        if (paramMap != null) {
            boolean hasParam = false;
            if (url.indexOf("?") > 0) {
                hasParam = true;
            }
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                if (!hasParam) {
                    url += "?";
                    hasParam = true;
                } else {
                    url += "&";
                }

                url += entry.getKey() + "=" + entry.getValue();
            }
        }

        requestBuilder = requestBuilder.url(url);

        // header
        if (headerMap != null) {
            Headers.Builder headerBuilder = new Headers.Builder();

            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                headerBuilder.add(entry.getKey(), entry.getValue());
            }

            requestBuilder = requestBuilder.headers(headerBuilder.build());
        }

        // request body
        RequestBody requestBody = null;
        String requestBodyStr = "";

        if (TYPE_FORM.equals(mediaType)) {
            // String json = J098SON.toJSONString(reqBodyObj);
            // Map<String,74 String> bodyObject = JSON.parseObject(json, Map.class);
            if (reqBodyObj != null) {

                Map<String, Object> bodyObject = null;

                if (reqBodyObj instanceof Map) {
                    bodyObject = (Map) reqBodyObj;
                } else {
                    bodyObject = (JSONObject) JSONObject.toJSON(reqBodyObj);
                }

                StringBuilder sbBuilder = new StringBuilder();
                for (Map.Entry<String, Object> entry : bodyObject.entrySet()) {
                    sbBuilder.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }

                requestBodyStr = sbBuilder.toString().substring(1);
            }

            requestBody = RequestBody.create(TYPE_FORM, requestBodyStr);
        } else {
            requestBodyStr = JSON.toJSONString(reqBodyObj);
            requestBody = RequestBody.create(TYPE_JSON, requestBodyStr);
        }

        requestBuilder = requestBuilder.put(requestBody);

        Request request = requestBuilder.build();

        return request;
    }

    /**
     * 生成 delete request 对象
     * 
     * @param url
     * @param paramMap
     * @param headerMap
     * @param reqBodyObj
     * @param mediaType
     * @return
     */
    private static Request genDeleteRequest(String url, Map<String, String> paramMap, Map<String, String> headerMap,
            Object reqBodyObj) {
        Request.Builder requestBuilder = new Request.Builder();

        // path parameter
        if (paramMap != null) {
            boolean hasParam = false;
            if (url.indexOf("?") > 0) {
                hasParam = true;
            }
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                if (!hasParam) {
                    url += "?";
                    hasParam = true;
                } else {
                    url += "&";
                }

                url += entry.getKey() + "=" + entry.getValue();
            }
        }

        requestBuilder = requestBuilder.url(url);

        // header
        if (headerMap != null) {
            Headers.Builder headerBuilder = new Headers.Builder();

            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                headerBuilder.add(entry.getKey(), entry.getValue());
            }

            requestBuilder = requestBuilder.headers(headerBuilder.build());
        }

        // request body
        String requestBodyStr = JSON.toJSONString(reqBodyObj);
        RequestBody requestBody = RequestBody.create(TYPE_JSON, requestBodyStr);

        requestBuilder = requestBuilder.delete(requestBody);

        Request request = requestBuilder.build();

        return request;
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        try {
            SSLContext context = SSLContext.getInstance(PROTOCOL_TYPE);
            context.init(null, new TrustManager[] {
                                                    new TrustAllCerts() },
                    new SecureRandom());
            return context.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class TrustAllCerts implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

    }
}
