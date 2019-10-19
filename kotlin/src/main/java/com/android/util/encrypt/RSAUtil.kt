package com.android.util.encrypt

import java.io.ByteArrayOutputStream
import java.security.*
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.HashMap
import javax.crypto.Cipher

/**
 * Created by xuzhb on 2019/8/14
 * Desc:非对称加密
 */
object RSAUtil {

    private val TRANSFORMATION = "RSA"
    private val ENCRYPT_MAX_SIZE = 117  //加密不能超过117
    private val DECRYPT_MAX_SIZE = 128  //解密不能超过128
    private val CHARSET = Charsets.UTF_8

    //密钥长度，DH算法的默认密钥长度是1024，密钥长度必须是64的倍数，在512到65536位之间
    private val KEY_SIZE = 1024
    //公钥
    private val PUBLIC_KEY = "RSAPublicKey"
    //私钥
    private val PRIVATE_KEY = "RSAPrivateKey"

    /**
     * 私钥加密
     * @param plaintext 明文
     * @param privateKey 私钥
     * @return 加密后的字符串
     */
    fun encryptByPrivateKey(plaintext: String, privateKey: PrivateKey): String {
        val byteArray = plaintext.toByteArray(CHARSET)
        //创建cipher
        val cipher = Cipher.getInstance(TRANSFORMATION)
        //初始化cipher
        cipher.init(Cipher.ENCRYPT_MODE, privateKey)
        //加密解密  分段加密
        //val encrypt = cipher.doFinal(plaintext.toByteArray(CHARSET))
        var temp: ByteArray? = null
        var offset = 0  //当前偏移位置
        val bos = ByteArrayOutputStream()
        while (byteArray.size - offset > 0) {  //没加密完
            //每次最大加密117个字节
            if (byteArray.size - offset >= ENCRYPT_MAX_SIZE) {
                //剩余部分大于117
                //加密完整的117字节
                temp = cipher.doFinal(byteArray, offset, ENCRYPT_MAX_SIZE)
                //重新计算偏移位置
                offset += ENCRYPT_MAX_SIZE
            } else {
                //加密最后一块
                temp = cipher.doFinal(byteArray, offset, byteArray.size - offset)
                //重新计算偏移位置
                offset = byteArray.size
            }
            //存储到临时缓存区
            bos.write(temp)
        }
        bos.close()
        return Base64.encode(bos.toByteArray())
    }

    /**
     * 公钥解密
     * @param ciphertext 密文
     * @param publicKey 公钥
     * @return 解密得到的字符串
     */
    fun decryptByPublicKey(ciphertext: String, publicKey: PublicKey): String {
        val byteArray = Base64.decode(ciphertext)
        //创建cipher
        val cipher = Cipher.getInstance(TRANSFORMATION)
        //初始化cipher
        cipher.init(Cipher.DECRYPT_MODE, publicKey)
        //加密解密  分段解密
        //val encrypt = cipher.doFinal(ciphertext.toByteArray(CHARSET))
        var temp: ByteArray? = null
        var offset = 0  //当前偏移位置
        val bos = ByteArrayOutputStream()
        while (byteArray.size - offset > 0) {  //没加密完
            //每次最大加密128个字节
            if (byteArray.size - offset >= DECRYPT_MAX_SIZE) {
                //剩余部分大于128
                //加密完整的128字节
                temp = cipher.doFinal(byteArray, offset, DECRYPT_MAX_SIZE)
                //重新计算偏移位置
                offset += DECRYPT_MAX_SIZE
            } else {
                //加密最后一块
                temp = cipher.doFinal(byteArray, offset, byteArray.size - offset)
                //重新计算偏移位置
                offset = byteArray.size
            }
            //存储到临时缓存区
            bos.write(temp)
        }
        bos.close()
        return String(bos.toByteArray(), CHARSET)
    }

    /**
     * 公钥加密
     * @param plaintext 明文
     * @param publicKey 公钥
     * @return 加密后的字符串
     */
    fun encryptByPublicKey(plaintext: String, publicKey: PublicKey): String {
        val byteArray = plaintext.toByteArray(CHARSET)
        //创建cipher
        val cipher = Cipher.getInstance(TRANSFORMATION)
        //初始化cipher
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        //加密解密  分段加密
        //val encrypt = cipher.doFinal(plaintext.toByteArray(CHARSET))
        var temp: ByteArray? = null
        var offset = 0  //当前偏移位置
        val bos = ByteArrayOutputStream()
        while (byteArray.size - offset > 0) {  //没加密完
            //每次最大加密117个字节
            if (byteArray.size - offset >= ENCRYPT_MAX_SIZE) {
                //剩余部分大于117
                //加密完整的117字节
                temp = cipher.doFinal(byteArray, offset, ENCRYPT_MAX_SIZE)
                //重新计算偏移位置
                offset += ENCRYPT_MAX_SIZE
            } else {
                //加密最后一块
                temp = cipher.doFinal(byteArray, offset, byteArray.size - offset)
                //重新计算偏移位置
                offset = byteArray.size
            }
            //存储到临时缓存区
            bos.write(temp)
        }
        bos.close()
        return Base64.encode(bos.toByteArray())
    }

    /**
     * 私钥解密
     * @param ciphertext 密文
     * @param privateKey 私钥
     * @return 解密得到的字符串
     */
    fun decryptByPrivateKey(ciphertext: String, privateKey: PrivateKey): String {
        val byteArray = Base64.decode(ciphertext)
        //创建cipher
        val cipher = Cipher.getInstance(TRANSFORMATION)
        //初始化cipher
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        //加密解密  分段解密
        //val encrypt = cipher.doFinal(ciphertext.toByteArray(CHARSET))
        var temp: ByteArray? = null
        var offset = 0  //当前偏移位置
        val bos = ByteArrayOutputStream()
        while (byteArray.size - offset > 0) {  //没加密完
            //每次最大加密128个字节
            if (byteArray.size - offset >= DECRYPT_MAX_SIZE) {
                //剩余部分大于128
                //加密完整的128字节
                temp = cipher.doFinal(byteArray, offset, DECRYPT_MAX_SIZE)
                //重新计算偏移位置
                offset += DECRYPT_MAX_SIZE
            } else {
                //加密最后一块
                temp = cipher.doFinal(byteArray, offset, byteArray.size - offset)
                //重新计算偏移位置
                offset = byteArray.size
            }
            //存储到临时缓存区
            bos.write(temp)
        }
        bos.close()
        return String(bos.toByteArray(), CHARSET)
    }

    //从字符串中加载公钥
    fun loadPublicKeyByStr(publicKeyStr: String): PublicKey {
        val buffer = Base64.decode(publicKeyStr)
        val keyFactory = KeyFactory.getInstance("RSA")
        val keySpec = X509EncodedKeySpec(buffer)
        return keyFactory.generatePublic(keySpec) as PublicKey
    }

    //从字符串加载私钥
    fun loadPrivateKeyByStr(privateKeyStr: String): PrivateKey {
        val buffer = Base64.decode(privateKeyStr)
        val keyFactory = KeyFactory.getInstance("RSA")
        val keySpec = PKCS8EncodedKeySpec(buffer)
        return keyFactory.generatePrivate(keySpec) as PrivateKey
    }

    //生成密钥对
    fun createKeyPair(): Map<String, Any> {
        //实例化密钥生成器
        val keyPairGenerator = KeyPairGenerator.getInstance(TRANSFORMATION)
        //初始化密钥生成器
        keyPairGenerator.initialize(KEY_SIZE)
        //生成密钥对
        val keyPair = keyPairGenerator.generateKeyPair()
        //公钥
        val publicKey = keyPair.public as RSAPublicKey
        //私钥
        val privateKey = keyPair.private as RSAPrivateKey
        //将密钥存储在map中
        val keyMap = HashMap<String, Any>()
        keyMap[PUBLIC_KEY] = publicKey
        keyMap[PRIVATE_KEY] = privateKey
        return keyMap
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println("===============非对称加密===============")
        val data = "加密工具aBc123"
//        val publicKeyStr =
//            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDymOuE7z9SfGv3Czq1ZAwzukNknR1iVTBZurJ6BTh9cBiQUczZ9U4HgBtgIWMRaLNeFjkFMszQz/uDmWQXMJbcsYuSAEJNxUn5KuHAp2YEWfV2CXTj/3I/Q9rhGVq+aLs0rgfCvVXoxFjJV+uGPtzXxhVZTahCcwUo20MSPqI0WwIDAQAB"
//        val privateKeyStr =
//            "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAPKY64TvP1J8a/cLOrVkDDO6Q2SdHWJVMFm6snoFOH1wGJBRzNn1TgeAG2AhYxFos14WOQUyzNDP+4OZZBcwltyxi5IAQk3FSfkq4cCnZgRZ9XYJdOP/cj9D2uEZWr5ouzSuB8K9VejEWMlX64Y+3NfGFVlNqEJzBSjbQxI+ojRbAgMBAAECgYAT4bpzk5Px86Z5gZ8XHJLvblV1mna9B1RGFknoPCNMDHLG6R1Lw5HYhYQ41aOj2pvQmyujJG2qs1DekSSlzeKfHMgoiHiKO/LPLCe4QgbkCsxtTy+SloVM/EirMBCwoPjMwYPqoxVm1a38kCqNgiitMpGooTXflCR0jgGvR9ej+QJBAPwTrCkk6SupWERxQG7yBdCIO9lyWzer9DPa2+JmMcoNydov4bq2ZUCgpum8JkAkmhonM1Oz8x6OFOwcbzHnyZ0CQQD2X3qpvF1Iyc3M/gqZcOSTORQTRkZVSt5ctPnJw6lIGax+HMr9YNccuiwlfi2UtSA0NOtC5yozDS8s9Xt2yHBXAkAzfKkrdjiSDHLU9/TbNF/vqgPfdDYhduPYO5mx8oG07YAPKryGcH7Z5nZxQ1bkvxUixmL7c8Pyt76aQ2yK2vcZAkBLoDFR6t0jm7aNhymPwiSXwHyWEgtC4TFyeab3NRVAaYkWSRZSQqilS8yDUcECFbsl61yP889zTke94Die1JYPAkEAjy3tzsbUeVCMF5CTfAklXSoJbJMZiWvW0S5lolAV9TOVmXFsW+itONAivg7k6HYkxGQF78u6JdcCf5y5p1DqSQ=="
//
//        val privateKey = loadPrivateKeyByStr(privateKeyStr)
//        val publicKey = loadPublicKeyByStr(publicKeyStr)

        val map = createKeyPair()
        //公钥
        val publicKey = map[PUBLIC_KEY] as PublicKey
        //私钥
        val privateKey = map[PRIVATE_KEY] as PrivateKey
        println("公钥：" + Base64.encode(publicKey!!.encoded))
        println("私钥：" + Base64.encode(privateKey!!.encoded))
        println()

        //加密不能超过117个字节  可以采取分段加密方式解决这个问题
        val encryptByPublicKey = encryptByPublicKey(data, publicKey)
        println("RSA公钥加密:" + encryptByPublicKey)
        val decryptByPrivateKey = decryptByPrivateKey(encryptByPublicKey, privateKey)
        println("RSA私钥解密:" + decryptByPrivateKey)
        println()
        val encryptByPrivateKey = encryptByPrivateKey(data, privateKey)
        println("RSA私钥加密:" + encryptByPrivateKey)
        val decryptByPublicKey = decryptByPublicKey(encryptByPrivateKey, publicKey)
        println("RSA公钥解密:" + decryptByPublicKey)
    }

}