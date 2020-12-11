package com.android.util

import android.os.Build
import android.text.Html
import android.util.Base64
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * Created by xuzhb on 2020/12/11
 * Desc:编码解码相关工具类
 * URL在线编码解码：http://www.jsons.cn/urlencode/
 * Base64在线编码解码：https://base64.us/
 * Html在线编码解码：http://web.chacuo.net/charsethtmlentry
 */
object EncodeUtil {

    //URL编码，若系统不支持指定的编码字符集,则返回原内容
    fun urlEncode(content: String, charset: String = "UTF-8"): String {
        return try {
            URLEncoder.encode(content, charset)
        } catch (e: Exception) {
            content
        }
    }

    //URL解码，若系统不支持指定的编码字符集,则返回原内容
    fun urlDecode(content: String, charset: String = "UTF-8"): String {
        return try {
            URLDecoder.decode(content, charset)
        } catch (e: Exception) {
            content
        }
    }

    //Base64编码
    fun base64Encode(content: String?): ByteArray? {
        return try {
            base64Encode(content!!.toByteArray())
        } catch (e: Exception) {
            null
        }
    }

    //Base64编码
    fun base64Encode(bytes: ByteArray?): ByteArray? {
        //Base64.DEFAULT：默认，当字符串过长（一般超过76）时会自动在中间加一个换行符，字符串最后也会加一个换行符
        //Base64.NO_WRAP：略去所有的换行符
        return try {
            Base64.encode(bytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            null
        }
    }

    //Base64编码成字符串
    fun base64EncodeToString(bytes: ByteArray?): String? {
        return try {
            Base64.encodeToString(bytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            null
        }
    }

    //Base64 URL安全编码，不会转码特殊字符，如"+"、"/"、"="等
    fun base64UrlSafeEncode(url: String?): ByteArray? {
        return try {
            Base64.encode(url!!.toByteArray(), Base64.URL_SAFE)
        } catch (e: Exception) {
            null
        }
    }

    //Base64解码
    fun base64Decode(content: String?): ByteArray? {
        return try {
            Base64.decode(content, Base64.NO_WRAP)
        } catch (e: Exception) {
            null
        }
    }

    //Base64解码
    fun base64Decode(bytes: ByteArray?): ByteArray? {
        return try {
            Base64.decode(bytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            null
        }
    }

    //Html编码
    fun htmlEncode(content: CharSequence?): String? {
        return when {
            content == null -> null
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN -> {
                Html.escapeHtml(content)
            }
            else -> {
                // 参照Html.escapeHtml()中代码
                val out = StringBuilder()
                var i = 0
                val len = content.length
                while (i < len) {
                    val c = content[i]
                    if (c == '<') {
                        out.append("&lt;")
                    } else if (c == '>') {
                        out.append("&gt;")
                    } else if (c == '&') {
                        out.append("&amp;")
                    } else if (c.toInt() in 0xD800..0xDFFF) {
                        if (c.toInt() < 0xDC00 && i + 1 < len) {
                            val d = content[i + 1]
                            if (d.toInt() in 0xDC00..0xDFFF) {
                                i++
                                val codepoint = 0x010000 or (c.toInt() - 0xD800 shl 10) or d.toInt() - 0xDC00
                                out.append("&#").append(codepoint).append(";")
                            }
                        }
                    } else if (c.toInt() > 0x7E || c < ' ') {
                        out.append("&#").append(c.toInt()).append(";")
                    } else if (c == ' ') {
                        while (i + 1 < len && content[i + 1] == ' ') {
                            out.append("&nbsp;")
                            i++
                        }
                        out.append(' ')
                    } else {
                        out.append(c)
                    }
                    i++
                }
                out.toString()
            }
        }
    }

    //Html解码
    fun htmlDecode(content: String?): CharSequence? {
        return when {
            content == null -> null
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY)
            }
            else -> Html.fromHtml(content)
        }
    }

}