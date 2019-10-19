package com.android.util.encrypt;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by xuzhb on 2019/10/17
 * Desc:非对称加密
 */
public class RSAUtil {

    private static final String TRANSFORMATION = "RSA";
    private static final int ENCRYPT_MAX_SIZE = 117;  //加密不能超过117
    private static final int DECRYPT_MAX_SIZE = 128;  //解密不能超过128
    private static final String CHARSET = "UTF-8";

    //密钥长度，DH算法的默认密钥长度是1024，密钥长度必须是64的倍数，在512到65536位之间
    private static final int KEY_SIZE = 1024;
    //公钥
    private static final String PUBLIC_KEY = "RSAPublicKey";
    //私钥
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 私钥加密
     *
     * @param plaintext  明文
     * @param privateKey 私钥
     * @return 加密后的字符串
     */
    public static String encryptByPrivateKey(String plaintext, PrivateKey privateKey) throws Exception {
        byte[] bytes = plaintext.getBytes(CHARSET);
        //创建cipher
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        //初始化cipher
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        //加密解密  分段加密
//        byte[] encrypt = cipher.doFinal(plaintext.getBytes(CHARSET));
        byte[] temp;
        int offset = 0;  //当前偏移位置
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while (bytes.length - offset > 0) {  //没加密完
            //每次最大加密117个字节
            if (bytes.length - offset >= ENCRYPT_MAX_SIZE) {
                //剩余部分大于117
                //加密完整的117字节
                temp = cipher.doFinal(bytes, offset, ENCRYPT_MAX_SIZE);
                //重新计算偏移位置
                offset += ENCRYPT_MAX_SIZE;
            } else {
                //加密最后一块
                temp = cipher.doFinal(bytes, offset, bytes.length - offset);
                //重新计算偏移位置
                offset = bytes.length;
            }
            //存储到临时缓存区
            bos.write(temp);
        }
        bos.close();
        return Base64.encode(bos.toByteArray());
    }

    /**
     * 公钥解密
     *
     * @param ciphertext 密文
     * @param publicKey  公钥
     * @return 解密得到的字符串
     */
    public static String decryptByPublicKey(String ciphertext, PublicKey publicKey) throws Exception {
        byte[] bytes = Base64.decode(ciphertext);
        //创建cipher
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        //初始化cipher
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        //加密解密  分段解密
//        byte[] encrypt = cipher.doFinal(ciphertext.getBytes(CHARSET));
        byte[] temp;
        int offset = 0;  //当前偏移位置
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while (bytes.length - offset > 0) {  //没加密完
            //每次最大加密128个字节
            if (bytes.length - offset >= DECRYPT_MAX_SIZE) {
                //剩余部分大于128
                //加密完整的128字节
                temp = cipher.doFinal(bytes, offset, DECRYPT_MAX_SIZE);
                //重新计算偏移位置
                offset += DECRYPT_MAX_SIZE;
            } else {
                //加密最后一块
                temp = cipher.doFinal(bytes, offset, bytes.length - offset);
                //重新计算偏移位置
                offset = bytes.length;
            }
            //存储到临时缓存区
            bos.write(temp);
        }
        bos.close();
        return new String(bos.toByteArray(), CHARSET);
    }

    /**
     * 公钥加密
     *
     * @param plaintext 明文
     * @param publicKey 公钥
     * @return 加密后的字符串
     */
    public static String encryptByPublicKey(String plaintext, PublicKey publicKey) throws Exception {
        byte[] bytes = plaintext.getBytes(CHARSET);
        //创建cipher
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        //初始化cipher
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        //加密解密  分段加密
//        byte[] encrypt = cipher.doFinal(plaintext.getBytes(CHARSET));
        byte[] temp;
        int offset = 0;  //当前偏移位置
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while (bytes.length - offset > 0) {  //没加密完
            //每次最大加密117个字节
            if (bytes.length - offset >= ENCRYPT_MAX_SIZE) {
                //剩余部分大于117
                //加密完整的117字节
                temp = cipher.doFinal(bytes, offset, ENCRYPT_MAX_SIZE);
                //重新计算偏移位置
                offset += ENCRYPT_MAX_SIZE;
            } else {
                //加密最后一块
                temp = cipher.doFinal(bytes, offset, bytes.length - offset);
                //重新计算偏移位置
                offset = bytes.length;
            }
            //存储到临时缓存区
            bos.write(temp);
        }
        bos.close();
        return Base64.encode(bos.toByteArray());
    }

    /**
     * 私钥解密
     *
     * @param ciphertext 密文
     * @param privateKey 私钥
     * @return 解密得到的字符串
     */
    public static String decryptByPrivateKey(String ciphertext, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.decode(ciphertext);
        //创建cipher
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        //初始化cipher
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        //加密解密  分段解密
//        byte[] encrypt = cipher.doFinal(ciphertext.getBytes(CHARSET));
        byte[] temp;
        int offset = 0;  //当前偏移位置
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while (bytes.length - offset > 0) {  //没加密完
            //每次最大加密128个字节
            if (bytes.length - offset >= DECRYPT_MAX_SIZE) {
                //剩余部分大于128
                //加密完整的128字节
                temp = cipher.doFinal(bytes, offset, DECRYPT_MAX_SIZE);
                //重新计算偏移位置
                offset += DECRYPT_MAX_SIZE;
            } else {
                //加密最后一块
                temp = cipher.doFinal(bytes, offset, bytes.length - offset);
                //重新计算偏移位置
                offset = bytes.length;
            }
            //存储到临时缓存区
            bos.write(temp);
        }
        bos.close();
        return new String(bos.toByteArray(), CHARSET);
    }

    //从字符串中加载公钥
    public static PublicKey loadPublicKeyByStr(String publicKeyStr) throws Exception {
        byte[] bytes = Base64.decode(publicKeyStr);
        KeyFactory keyFactory = KeyFactory.getInstance(TRANSFORMATION);
        KeySpec keySpec = new X509EncodedKeySpec(bytes);
        return keyFactory.generatePublic(keySpec);
    }

    //从字符串加载私钥
    public static PrivateKey loadPrivateKeyByStr(String privateKeyStr) throws Exception {
        byte[] bytes = Base64.decode(privateKeyStr);
        KeyFactory keyFactory = KeyFactory.getInstance(TRANSFORMATION);
        KeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        return keyFactory.generatePrivate(keySpec);
    }

    //生成密钥对
    public static Map<String, Object> createKeyPair() throws Exception {
        //实例化密钥生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(TRANSFORMATION);
        //初始化密钥生成器
        keyPairGenerator.initialize(KEY_SIZE);
        //生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //将密钥存储在map中
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("===============非对称加密===============");
        String data = "加密工具aBc123";
//        String publicKeyStr =
//                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDymOuE7z9SfGv3Czq1ZAwzukNknR1iVTBZurJ6BTh9cBiQUczZ9U4HgBtgIWMRaLNeFjkFMszQz/uDmWQXMJbcsYuSAEJNxUn5KuHAp2YEWfV2CXTj/3I/Q9rhGVq+aLs0rgfCvVXoxFjJV+uGPtzXxhVZTahCcwUo20MSPqI0WwIDAQAB";
//        String privateKeyStr =
//                "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAPKY64TvP1J8a/cLOrVkDDO6Q2SdHWJVMFm6snoFOH1wGJBRzNn1TgeAG2AhYxFos14WOQUyzNDP+4OZZBcwltyxi5IAQk3FSfkq4cCnZgRZ9XYJdOP/cj9D2uEZWr5ouzSuB8K9VejEWMlX64Y+3NfGFVlNqEJzBSjbQxI+ojRbAgMBAAECgYAT4bpzk5Px86Z5gZ8XHJLvblV1mna9B1RGFknoPCNMDHLG6R1Lw5HYhYQ41aOj2pvQmyujJG2qs1DekSSlzeKfHMgoiHiKO/LPLCe4QgbkCsxtTy+SloVM/EirMBCwoPjMwYPqoxVm1a38kCqNgiitMpGooTXflCR0jgGvR9ej+QJBAPwTrCkk6SupWERxQG7yBdCIO9lyWzer9DPa2+JmMcoNydov4bq2ZUCgpum8JkAkmhonM1Oz8x6OFOwcbzHnyZ0CQQD2X3qpvF1Iyc3M/gqZcOSTORQTRkZVSt5ctPnJw6lIGax+HMr9YNccuiwlfi2UtSA0NOtC5yozDS8s9Xt2yHBXAkAzfKkrdjiSDHLU9/TbNF/vqgPfdDYhduPYO5mx8oG07YAPKryGcH7Z5nZxQ1bkvxUixmL7c8Pyt76aQ2yK2vcZAkBLoDFR6t0jm7aNhymPwiSXwHyWEgtC4TFyeab3NRVAaYkWSRZSQqilS8yDUcECFbsl61yP889zTke94Die1JYPAkEAjy3tzsbUeVCMF5CTfAklXSoJbJMZiWvW0S5lolAV9TOVmXFsW+itONAivg7k6HYkxGQF78u6JdcCf5y5p1DqSQ==";
//
//        PrivateKey privateKey = loadPrivateKeyByStr(privateKeyStr);
//        PublicKey publicKey = loadPublicKeyByStr(publicKeyStr);

        Map<String, Object> map = createKeyPair();
        //公钥
        PublicKey publicKey = (PublicKey) map.get(PUBLIC_KEY);
        //私钥
        PrivateKey privateKey = (PrivateKey) map.get(PRIVATE_KEY);
        System.out.println("公钥：" + Base64.encode(publicKey.getEncoded()));
        System.out.println("私钥：" + Base64.encode(privateKey.getEncoded()));
        System.out.println();

        String encryptByPublicKey = encryptByPublicKey(data, publicKey);
        System.out.println("RSA公钥加密:" + encryptByPublicKey);
        String decryptByPrivateKey = decryptByPrivateKey(encryptByPublicKey, privateKey);
        System.out.println("RSA私钥解密:" + decryptByPrivateKey);
        System.out.println();
        String encryptByPrivateKey = encryptByPrivateKey(data, privateKey);
        System.out.println("RSA私钥加密:" + encryptByPrivateKey);
        String decryptByPublicKey = decryptByPublicKey(encryptByPrivateKey, publicKey);
        System.out.println("RSA公钥解密:" + decryptByPublicKey);

    }

}
