package com.android.util.encrypt

import java.security.Key
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.IvParameterSpec

/**
 * Created by xuzhb on 2019/8/14
 * Desc:DES加解密工具，CBC模式，CBC模式需要IV，DES密钥长度8位
 */
object DESCBCUtil {

    private val TRANSFORMATION = "DES/CBC/PKCS5Padding"
    private val ALGORITHM = "DES"
    private val CHARSET = Charsets.UTF_8

    /**
     * 对字符串进行DES加密
     * @param plaintext 明文
     * @param password 密钥
     * @return 加密后的字符串
     */
    fun encrypt(plaintext: String, password: String): String {
        //创建cipher对象
        val cipher = Cipher.getInstance(TRANSFORMATION)
        //初始化cirpher
        val kf = SecretKeyFactory.getInstance(ALGORITHM)
        val keySpe = DESKeySpec(password.toByteArray(CHARSET))
        val key: Key = kf.generateSecret(keySpe)
        //加密模式
        val iv = IvParameterSpec(password.toByteArray(CHARSET))
        cipher.init(Cipher.ENCRYPT_MODE, key, iv)//CBC需要更多参数
        //加密
        val encrypt = cipher.doFinal(plaintext.toByteArray(CHARSET))
        //通过Base64解决乱码问题
        return Base64.encode(encrypt)
    }

    /**
     * 对字符串进行DES解密
     * @param ciphertext 密文
     * @param password  密钥
     * @return 解密得到的字符串
     */
    fun decrypt(ciphertext: String, password: String): String {
        //创建cipher对象
        val cipher = Cipher.getInstance(TRANSFORMATION)
        //初始化cirpher
        val kf = SecretKeyFactory.getInstance(ALGORITHM)
        val keySpe = DESKeySpec(password.toByteArray(CHARSET))
        val key: Key = kf.generateSecret(keySpe)
        //解密模式
        val iv = IvParameterSpec(password.toByteArray(CHARSET))
        cipher.init(Cipher.DECRYPT_MODE, key, iv)
        //解密
        val decrypt = cipher.doFinal(Base64.decode(ciphertext))
        return String(decrypt, CHARSET)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("===============DES对称加密(CBC模式)===============")
        val data = "加密工具aBc123"
        val pwd = "12345678"  //DES密码长度8位
        val encryptData = encrypt(data, pwd)
        println("密文：$encryptData")
        println("明文：${decrypt(encryptData, pwd)}")
    }

}