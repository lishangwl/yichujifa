package com.xieqing.codeutils.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class 加密操作 {
    private 加密操作() {
    }

    public static String DES加密(String encryptString, String encryptKey) {
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
            SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes("GBK"), "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(1, key, zeroIv);
            return BASE64编码(cipher.doFinal(encryptString.getBytes("GBK")));
        } catch (Exception e) {
            return "";
        }
    }

    public static String DES解密(String decryptString, String decryptKey) {
        try {
            byte[] byteMi = BASE64解码(decryptString);
            IvParameterSpec zeroIv = new IvParameterSpec(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
            SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes("GBK"), "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(2, key, zeroIv);
            return new String(cipher.doFinal(byteMi), "GBK");
        } catch (Exception e) {
            return "";
        }
    }

    public static String BASE64编码(byte[] data) {
        try {
            return Base64.encodeToString(data,0);
        } catch (Exception e) {
            return "";
        }
    }

    public static byte[] BASE64解码(String data) {
        return Base64.decode(data,0);
    }

    public static String Authcode加密(String source, String key) {
        try {
            return Authcode.Encode(source, key);
        } catch (Exception e) {
            return "";
        }
    }

    public static String Authcode解密(String source, String key) {
        try {
            return Authcode.Decode(source, key);
        } catch (Exception e) {
            return "";
        }
    }
}