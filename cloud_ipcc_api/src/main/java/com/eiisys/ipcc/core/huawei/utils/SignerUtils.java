package com.eiisys.ipcc.core.huawei.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SignerUtils {
    private static final String CHARSET = "UTF-8";
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final String SHA_KEY = "HmacSHA256";
    
    /**
     * 摘要算法
     * @param key
     * @param toSigned
     * @return String
     * @throws NoSuchAlgorithmException 
     * @throws UnsupportedEncodingException 
     * @throws InvalidKeyException 
     */
    public static String sha256Hex(String key, String toSigned) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Mac mac = Mac.getInstance(SHA_KEY);
        mac.init(new SecretKeySpec(key.getBytes(SignerUtils.CHARSET), SHA_KEY));
        
        String digit = new String(SignerUtils.encodeHex(mac.doFinal(toSigned.getBytes(SignerUtils.CHARSET))));
        return digit;
    }
    
    private static char[] encodeHex(final byte[] data) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = SignerUtils.DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = SignerUtils.DIGITS_LOWER[0x0F & data[i]];
        }
        return out;
    }   
}
