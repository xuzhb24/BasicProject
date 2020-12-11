package com.android.util;

import android.os.Build;
import android.text.Html;
import android.util.Base64;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by xuzhb on 2020/4/19
 * Desc:编码解码相关工具类
 * URL在线编码解码：http://www.jsons.cn/urlencode/
 * Base64在线编码解码：https://base64.us/
 * Html在线编码解码：http://web.chacuo.net/charsethtmlentry
 */
public class EncodeUtil {

    //URL编码为UTF-8的字符串
    public static String urlEncode(String content) {
        return urlEncode(content, "UTF-8");
    }

    //URL编码，若系统不支持指定的编码字符集,则返回原内容
    public static String urlEncode(String content, String charset) {
        try {
            return URLEncoder.encode(content, charset);
        } catch (Exception e) {
            return content;
        }
    }

    //URL解码为UTF-8的字符串
    public static String urlDecode(String content) {
        return urlDecode(content, "UTF-8");
    }

    //URL解码，若系统不支持指定的编码字符集,则返回原内容
    public static String urlDecode(String content, String charset) {
        try {
            return URLDecoder.decode(content, charset);
        } catch (Exception e) {
            return content;
        }
    }

    //Base64编码
    public static byte[] base64Encode(String content) {
        try {
            return base64Encode(content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Base64编码
    public static byte[] base64Encode(byte[] bytes) {
        //Base64.DEFAULT：默认，当字符串过长（一般超过76）时会自动在中间加一个换行符，字符串最后也会加一个换行符
        //Base64.NO_WRAP：略去所有的换行符
        try {
            return Base64.encode(bytes, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Base64编码成字符串
    public static String base64EncodeToString(byte[] bytes) {
        try {
            return Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Base64 URL安全编码，不会转码特殊字符，如"+"、"/"、"="等
    public static byte[] base64UrlSafeEncode(String url) {
        try {
            return Base64.encode(url.getBytes(), Base64.URL_SAFE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Base64解码
    public static byte[] base64Decode(String content) {
        try {
            return Base64.decode(content, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Base64解码
    public static byte[] base64Decode(byte[] bytes) {
        try {
            return Base64.decode(bytes, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Html编码
    public static String htmlEncode(CharSequence content) {
        if (content == null) {
            return null;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return Html.escapeHtml(content);
        } else {
            // 参照Html.escapeHtml()中代码
            StringBuilder out = new StringBuilder();
            for (int i = 0, len = content.length(); i < len; i++) {
                char c = content.charAt(i);
                if (c == '<') {
                    out.append("&lt;");
                } else if (c == '>') {
                    out.append("&gt;");
                } else if (c == '&') {
                    out.append("&amp;");
                } else if (c >= 0xD800 && c <= 0xDFFF) {
                    if (c < 0xDC00 && i + 1 < len) {
                        char d = content.charAt(i + 1);
                        if (d >= 0xDC00 && d <= 0xDFFF) {
                            i++;
                            int codepoint = 0x010000 | (int) c - 0xD800 << 10 | (int) d - 0xDC00;
                            out.append("&#").append(codepoint).append(";");
                        }
                    }
                } else if (c > 0x7E || c < ' ') {
                    out.append("&#").append((int) c).append(";");
                } else if (c == ' ') {
                    while (i + 1 < len && content.charAt(i + 1) == ' ') {
                        out.append("&nbsp;");
                        i++;
                    }
                    out.append(' ');
                } else {
                    out.append(c);
                }
            }
            return out.toString();
        }
    }

    //Html解码
    public static CharSequence htmlDecode(String content) {
        if (content == null) {
            return null;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(content);
        }
    }

}
