package com.android.util.encrypt

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by xuzhb on 2019/10/15
 * Desc:AES加解密工具，CBC模式，填充模式PKCS5Padding，CBC模式需要IV，AES密钥长度16位
 */
object AESCBCUtil {

    private val TRANSFORMATION = "AES/CBC/PKCS5Padding"
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
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(password.toByteArray(CHARSET)))
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
        cipher.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(password.toByteArray(CHARSET)))
        //解密
        val decrypt = cipher.doFinal(Base64.decode(ciphertext))
        return String(decrypt, CHARSET)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("===============AES对称加密(CBC模式)===============")
        val data = "加密工具aBc123"
        val pwd = "1234567812345678"
        val encryptData = encrypt(data, pwd)
        println("密文：$encryptData")
        println("明文：${decrypt(encryptData, pwd)}")
    }

}