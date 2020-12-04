package com.android.util.encrypt

import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by xuzhb on 2019/8/14
 * Desc:信息摘要加密，不可逆
 * 在线加解密http://tool.oschina.net/encrypt?type=2、https://www.cmd5.com/
 */
object MessageDigestUtil {

    //MD5加密(32位）
    fun md5(data: ByteArray?) = digest(data, "MD5")

    //MD5加密(32位）
    fun md5(plaintext: String): String? {
        try {
            md5(plaintext.toByteArray(Charsets.UTF_8))?.let {
                return byte2Hex(it)
            }
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return plaintext
    }

    fun sha1(data: ByteArray?) = digest(data, "SHA-1")

    fun sha1(plaintext: String): String? {
        try {
            sha1(plaintext.toByteArray(Charsets.UTF_8))?.let {
                return byte2Hex(it)
            }
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return plaintext
    }

    fun sha256(data: ByteArray?) = digest(data, "SHA-256")

    fun sha256(plaintext: String): String? {
        try {
            sha256(plaintext.toByteArray(Charsets.UTF_8))?.let {
                return byte2Hex(it)
            }
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return plaintext
    }

    fun digest(data: ByteArray?, algorithm: String?): ByteArray? {
        if (data == null || data.isEmpty() || algorithm.isNullOrBlank()) {
            return null
        }
        try {
            val md = MessageDigest.getInstance(algorithm)
            return md.digest(data)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return null
    }

    //byte数组转成16进制
    private fun byte2Hex(array: ByteArray?): String? {
        if (array == null) {
            return null
        }
        val sb = StringBuilder()
        for (b in array) {
            var hex = Integer.toHexString(b.toInt() and 0xFF)
            if (hex.length == 1) {
                hex = "0$hex"
            }
            sb.append(hex)
        }
        return sb.toString()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val data = "加密工具aBc123"
        println(md5(data))
        println(sha1(data))
        println(sha256(data))
    }

}