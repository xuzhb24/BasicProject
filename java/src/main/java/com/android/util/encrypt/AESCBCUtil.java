package com.android.util.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by xuzhb on 2019/10/15
 * Desc:AES加解密工具，CBC模式，填充模式PKCS5Padding，CBC模式需要IV，AES密钥长度16位
 */
public class AESCBCUtil {

    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";
    private static final String CHARSET = "UTF-8";

    /**
     * 对字符串进行AES加密
     *
     * @param plaintext 明文
     * @param password  密钥
     * @return 加密后的字符串
     */
    public static String encrypt(String plaintext, String password) {
        try {
            //创建cipher对象
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            //初始化cipher
            //通过密钥工厂生产密钥
            SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(CHARSET), ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(password.getBytes(CHARSET)));
            //加密
            byte[] encrypt = cipher.doFinal(plaintext.getBytes(CHARSET));
            return Base64.encode(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对字符串进行AES解密
     *
     * @param ciphertext 密文
     * @param password   密钥
     * @return 解密得到的字符串
     */
    public static String decrypt(String ciphertext, String password) {
        try {
            //创建cipher对象
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            //初始化cipher
            //通过密钥工厂生产密钥
            SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(CHARSET), ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(password.getBytes(CHARSET)));
            //解密
            byte[] decrypt = cipher.doFinal(Base64.decode(ciphertext));
            return new String(decrypt, CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println("===============AES对称加密(CBC模式)===============");
        String data = "加密工具aBc123";
        String pwd = "1234567812345678";
        String encryptData = encrypt(data, pwd);
        System.out.println("密文：" + encryptData);
        System.out.println("明文：" + decrypt(encryptData, pwd));
    }

}
