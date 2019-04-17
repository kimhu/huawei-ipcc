package com.eiisys.ipcc.core.huawei.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import com.eiisys.ipcc.core.huawei.utils.PathUtils;
import com.eiisys.ipcc.core.huawei.utils.SignerUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * 华为鉴权算法实体
 * 
 * @author hujm
 */

@Getter
@Setter
public class SignInfo {
    private static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private static final String AUTH_VERSION = "auth-v2";

    private String httpMethod;

    private String uri;

    private Map<String, String> queryParameters;

    private Map<String, String> signedHeaders;

    private String payload;

    private String accessKey;

    private String secretKey;

    private Date timestamp;

    /**
     * 生成鉴权信息
     * 
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     */
    public String authString() throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String authStringPrefix = this.authStringPrefix();
        String signingKey = SignerUtils.sha256Hex(secretKey, authStringPrefix);
        String canonicalRequest = this.canonicalRequest();
        String signature = SignerUtils.sha256Hex(signingKey, canonicalRequest);
        String authString = authStringPrefix + '/' + signature;
        return authString;
    }

    private String authStringPrefix() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(AUTH_VERSION);
        buffer.append('/').append(this.accessKey);
        buffer.append('/').append(this.formatTimestamp());
        buffer.append('/');
        this.appendSignedHeaders(buffer);

        return buffer.toString();
    }

    private String canonicalRequest() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.httpMethod).append('\n');
        buffer.append(this.uri).append('\n');

        if (this.isNotEmpty(this.queryParameters)) {
            this.appendCanonicalQueryString(buffer);
            buffer.append('\n');
        }

        this.appendSignedHeaders(buffer);
        buffer.append('\n');

        this.appendCanonicalHeaders(buffer);
        buffer.append('\n');

        if (this.isNotEmpty(this.payload)) {
            buffer.append(PathUtils.normalize(this.payload));
        }
        return buffer.toString();
    }

    private void appendSignedHeaders(StringBuilder buffer) {
        if (null == this.signedHeaders) {
            return;
        }
        Set<String> headerNames = new TreeSet<>(this.signedHeaders.keySet());
        for (String name : headerNames) {
            buffer.append(this.toLowerCase(name)).append(';');
        }
        buffer.deleteCharAt(buffer.length() - 1);
    }

    private void appendCanonicalHeaders(StringBuilder buffer) {
        if (null == this.signedHeaders) {
            return;
        }
        Set<String> headers = new TreeSet<>();
        for (Map.Entry<String, String> entry : this.signedHeaders.entrySet()) {
            String header = PathUtils.normalize(this.toLowerCase(entry.getKey())) + ':'
                    + PathUtils.normalize(entry.getValue());
            headers.add(header);
        }

        for (String header : headers) {
            buffer.append(header).append('\n');
        }
        buffer.deleteCharAt(buffer.length() - 1);
    }

    private void appendCanonicalQueryString(StringBuilder buffer) {
        if (null == this.queryParameters) {
            return;
        }
        // 编码并排序
        Set<String> sortedSet = new TreeSet<>();
        String uriEncodeKey;
        String uriEncodeValue;
        for (Map.Entry<String, String> e : this.queryParameters.entrySet()) {
            uriEncodeKey = PathUtils.normalize(e.getKey());
            uriEncodeValue = this.isNotEmpty(e.getValue()) ? PathUtils.normalize(e.getValue()) : "";
            sortedSet.add(uriEncodeKey + "=" + uriEncodeValue);
        }

        for (String e : sortedSet) {
            buffer.append(e).append('&');
        }
        buffer.deleteCharAt(buffer.length() - 1);
    }

    private String formatTimestamp() {
        SimpleDateFormat format = new SimpleDateFormat(SignInfo.TIMESTAMP_FORMAT);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(this.timestamp);
    }

    private boolean isNotEmpty(String str) {
        if ((null == str) || str.isEmpty()) {
            return false;
        }
        return true;
    }

    private <K, V> boolean isNotEmpty(Map<K, V> map) {
        if ((null == map) || map.isEmpty()) {
            return false;
        }
        return true;
    }

    private String toLowerCase(String str) {
        if (this.isNotEmpty(str)) {
            return str.toLowerCase(Locale.ENGLISH);
        }

        return str;
    }
}
