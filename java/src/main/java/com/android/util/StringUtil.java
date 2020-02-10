package com.android.util;

/**
 * Created by xuzhb on 2020/2/8
 * Desc:字符串工具类
 */
public class StringUtil {

    //纯Java判断是否是空字符，相当于Android中的TextUtils.isEmpty
    public static boolean isEmpty(String s) {
        if (s != null) {
            for (int i = 0, len = s.length(); i < len; ++i) {
                if (!Character.isWhitespace(s.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

}
