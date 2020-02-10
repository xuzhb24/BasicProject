package com.android.util.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Create by xuzhb on 2019/10/17
 * Desc:信息摘要加密，不可逆
 * 在线加解密http://tool.oschina.net/encrypt?type=2、https://www.cmd5.com/
 */
public class MessageDigestUtil {

    //MD5加密(32位）
    public static byte[] md5(byte[] data) {
        return digest(data, "MD5");
    }

    //MD5加密(32位）
    public static String md5(String plaintext) {
        try {
            byte[] result = md5(plaintext.getBytes("UTF-8"));
            if (result != null) {
                return byte2Hex(result);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return plaintext;
    }

    public static byte[] sha1(byte[] data) {
        return digest(data, "SHA-1");
    }

    public static String sha1(String plaintext) {
        try {
            byte[] result = sha1(plaintext.getBytes("UTF-8"));
            if (result != null) {
                return byte2Hex(result);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return plaintext;
    }

    public static byte[] sha256(byte[] data) {
        return digest(data, "SHA-256");
    }

    public static String sha256(String plaintext) {
        try {
            byte[] result = sha256(plaintext.getBytes("UTF-8"));
            if (result != null) {
                return byte2Hex(result);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return plaintext;
    }

    private static byte[] digest(byte[] data, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            return md.digest(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    //byte数组转成16进制
    private static String byte2Hex(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String data = "加密工具aBc123";
        System.out.println(md5(data));
        System.out.println(sha1(data));
        System.out.println(sha256(data));
    }

}
