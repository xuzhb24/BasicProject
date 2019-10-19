package com.android.util.encrypt

import java.security.MessageDigest

/**
 * Created by xuzhb on 2019/8/14
 * Desc:信息摘要加密，不可逆
 * 在线加解密http://tool.oschina.net/encrypt?type=2、https://www.cmd5.com/
 */
object MessageDigestUtil {

    //MD5加密(32位）
    fun md5(plaintext: String): String {
        val digest = MessageDigest.getInstance("MD5")
        val result = digest.digest(plaintext.toByteArray(Charsets.UTF_8))
        return byte2Hex(result)
    }

    fun sha1(plaintext: String): String {
        val digest = MessageDigest.getInstance("SHA-1")
        val result = digest.digest(plaintext.toByteArray(Charsets.UTF_8))
        return byte2Hex(result)
    }

    fun sha256(plaintext: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val result = digest.digest(plaintext.toByteArray(Charsets.UTF_8))
        return byte2Hex(result)
    }

    //byte数组转成16进制
    private fun byte2Hex(byteArray: ByteArray): String {
        val result = with(StringBuilder()) {
            byteArray.forEach {
                val value = it
                val hex = value.toInt() and (0xFF)
                val hexStr = Integer.toHexString(hex)
                //println(hexStr)
                if (hexStr.length == 1) {
                    //this.append("0").append(hexStr)
                    append("0").append(hexStr)
                } else {
                    //this.append(hexStr)
                    append(hexStr)
                }
            }
            this.toString()
        }
        return result
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val data = "加密工具aBc123"
        println(md5(data))
        println(sha1(data))
        println(sha256(data))
    }

}