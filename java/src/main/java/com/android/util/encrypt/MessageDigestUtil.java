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
    public static String md5(String plaintext) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return byte2Hex(digest.digest(plaintext.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return plaintext;
    }

    public static String sha1(String plaintext) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            return byte2Hex(digest.digest(plaintext.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return plaintext;
    }

    public static String sha256(String plaintext) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return byte2Hex(digest.digest(plaintext.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return plaintext;
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
