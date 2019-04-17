package com.eiisys.ipcc.core.utils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * AES加密工具类
 * 
 * @author hujm
 */
public class AESUtils {
	private static byte[] password = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
	                                  'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 
	                                  'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 
	                                  'z', 'x', 'c', 'v', 'b', 'n', 'n', 'm'};
	
    public static String encrypt(String content) throws Exception {
        // 创建AES的Key生产者
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        // 利用用户密码作为随机数初始化出 ,128位的key生产者
        kgen.init(128, new SecureRandom(password));
        // 根据用户密码，生成一个密钥
        SecretKey secretKey = kgen.generateKey();
        // 返回基本编码格式的密钥，如果此密钥不支持编码，则返回 null。
        byte[] enCodeFormat = secretKey.getEncoded();
        // 转换为AES专用密钥
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        // 创建密码器
        Cipher cipher = Cipher.getInstance("AES");

        byte[] byteContent = content.getBytes("utf-8");
        // 初始化为加密模式的密码器
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // 加密
        byte[] result = cipher.doFinal(byteContent);
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(result);
    }

    public static String decrypt(String content) throws Exception {
        // 创建AES的Key生产者
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        // 利用用户密码作为随机数初始化出 ,128位的key生产者
        kgen.init(128, new SecureRandom(password));
        // 根据用户密码，生成一个密钥
        SecretKey secretKey = kgen.generateKey();
        // 返回基本编码格式的密钥
        byte[] enCodeFormat = secretKey.getEncoded();
        // 转换为AES专用密钥
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        // 创建密码器
        Cipher cipher = Cipher.getInstance("AES");
        // 初始化为解密模式的密码器
        cipher.init(Cipher.DECRYPT_MODE, key);
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] result = cipher.doFinal(decoder.decodeBuffer(content));
        return new String(result, StandardCharsets.UTF_8);
    }
}
