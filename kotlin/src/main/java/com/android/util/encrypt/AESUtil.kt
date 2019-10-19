package com.android.util.encrypt

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Created by xuzhb on 2019/8/13
 * Desc:AES加解密工具，ECB模式（不推荐），ECB模式不需要IV，AES密钥长度16位
 * AES在线加密：http://www.seacha.com/tools/aes.html
 * 在线加解密http://tool.chacuo.net/cryptaes，加密模式ECB、填充pkcs5padding、字符集utf8
 */
object AESUtil {

    private val TRANSFORMATION = "AES"  //等价于"AES/ECB/PKCS5Padding"
    //    private val TRANSFORMATION = "AES/ECB/PKCS5Padding"
    private val ALGORITHM = "AES"
    private val CHARSET = Charsets.UTF_8

    /**
     * 对字符串进行AES加密
     * @param plaintext 明文
     * @param password 密钥
     * @return 加密后的字符串
     */
    fun encrypt(plaintext: String, password: String): String {
        //创建cipher对象
        val cipher = Cipher.getInstance(TRANSFORMATION)
        //初始化cipher
        //通过密钥工厂生产密钥
        val keySpec = SecretKeySpec(password.toByteArray(CHARSET), ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        //加密
        val encrypt = cipher.doFinal(plaintext.toByteArray(CHARSET))
        return Base64.encode(encrypt)
    }

    /**
     * 对字符串进行AES解密
     * @param ciphertext 密文
     * @param password  密钥
     * @return 解密得到的字符串
     */
    fun decrypt(ciphertext: String, password: String): String {
        //创建cipher对象
        val cipher = Cipher.getInstance(TRANSFORMATION)
        //初始化cipher
        //通过密钥工厂生产密钥
        val keySpec = SecretKeySpec(password.toByteArray(CHARSET), ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, keySpec)
        //解密
        val decrypt = cipher.doFinal(Base64.decode(ciphertext))
        return String(decrypt, CHARSET)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("===============AES对称加密(ECB模式)===============")
        val data = "加密工具aBc123"
        val pwd = "1234567812345678"
        val encryptData = encrypt(data, pwd)
        println("密文：$encryptData")
        println("明文：${decrypt(encryptData, pwd)}")
    }

}