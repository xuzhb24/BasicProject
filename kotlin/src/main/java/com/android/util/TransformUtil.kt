package com.android.util

/**
 * Created by xuzhb on 2019/10/17
 * Desc:转换工具
 */
object TransformUtil {

    /**
     * byte转16进制
     *
     * @param b 需要进行转换的byte字节
     * @return 转换后的Hex字符串
     */
    fun byte2Hex(b: Byte): String {
        var hex = Integer.toHexString(b.toInt() and 0xFF)
        if (hex.length < 2) {
            hex = "0$hex"
        }
        return hex
    }

    /**
     * 16进制转byte
     *
     * @param hex 待转换的Hex字符串
     * @return 转换后的byte
     */
    fun hex2Byte(hex: String): Byte = Integer.parseInt(hex, 16).toByte()


    /**
     * byte数组转16进制
     *
     * @param bytes 需要转换的byte数组
     * @return 转换后的Hex字符串
     */
    fun byteArray2Hex(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (i in bytes.indices) {
            val hex = Integer.toHexString(bytes[i].toInt() and 0xFF)
            if (hex.length < 2) {
                sb.append(0)
            }
            sb.append(hex)
        }
        return sb.toString()
    }

    /**
     * 16进制转byte数组
     *
     * @param hex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    fun hex2ByteArray(hex: String): ByteArray {
        var hex = hex
        var hexlen = hex.length
        val result: ByteArray
        if (hexlen % 2 == 1) {  //奇数
            hexlen++
            result = ByteArray(hexlen / 2)
            hex = "0$hex"
        } else {  //偶数
            result = ByteArray(hexlen / 2)
        }
        var j = 0
        var i = 0
        while (i < hexlen) {
            result[j] = hex2Byte(hex.substring(i, i + 2))
            j++
            i += 2
        }
        return result
    }

    @JvmStatic
    fun main(args: Array<String>) {
        logTitlt("16进制和byte数组相互转换")
        val data = "HIJKLMN"
        val hex = byteArray2Hex(data.toByteArray(charset("UTF-8")))
        println(hex)
        println(String(hex2ByteArray(hex), Charsets.UTF_8))
    }

    private fun logTitlt(title: String) {
        println("===============$title===============")
    }

}