package com.android.util;

/**
 * Created by xuzhb on 2019/10/17
 * Desc:转换工具
 */
public class TransformUtil {

    /**
     * byte转16进制
     *
     * @param b 需要进行转换的byte字节
     * @return 转换后的Hex字符串
     */
    public static String byte2Hex(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 16进制转byte
     *
     * @param hex 待转换的Hex字符串
     * @return 转换后的byte
     */
    public static byte hex2Byte(String hex) {
        return (byte) Integer.parseInt(hex, 16);
    }

    /**
     * byte数组转16进制
     *
     * @param bytes 需要转换的byte数组
     * @return 转换后的Hex字符串
     */
    public static String byteArray2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 16进制转byte数组
     *
     * @param hex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public static byte[] hex2ByteArray(String hex) {
        int hexlen = hex.length();
        byte[] result;
        if (hexlen % 2 == 1) {  //奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            hex = "0" + hex;
        } else {  //偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hex2Byte(hex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        logTitlt("16进制和byte数组相互转换");
        String data = "abcdefg";
        String hex = byteArray2Hex(data.getBytes("UTF-8"));
        System.out.println(hex);
        System.out.println(new String(hex2ByteArray(hex), "UTF-8"));
    }

    private static void logTitlt(String title) {
        System.out.println("===============" + title + "===============");
    }

}
