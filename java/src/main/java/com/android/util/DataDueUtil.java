package com.android.util;

/**
 * Created by xuzhb on 2020/3/18
 * Desc:数据缓存是否过期
 */
public class DataDueUtil {

    private static final char SEPARATOR = ' ';

    //判断缓存的字符串数据是否到期，true：到期，false：还处于有效期内
    public static boolean isDue(String content) {
        return isDue(content.getBytes());
    }

    //判断缓存的byte数据是否到期，true：到期，false：还处于有效期内
    public static boolean isDue(byte[] data) {
        String[] strs = getTimeInfoFromData(data);
        if (strs != null && strs.length == 2) {
            String saveTimeStr = strs[0];
            while (saveTimeStr.startsWith("0")) {
                saveTimeStr = saveTimeStr.substring(1);
            }
            long saveTime = Long.parseLong(saveTimeStr);
            long saveDuration = Long.parseLong(strs[1]);
            if (System.currentTimeMillis() > saveTime + saveDuration * 1000) {
                return true;
            }
        }
        return false;
    }

    /**
     * 在byte数组开头加上缓存时间标志，使数据具有时效性
     *
     * @param content 缓存的数据
     * @param second  缓存的时长
     */
    public static String newStringWithTimeInfo(String content, int second) {
        return createTimeInfo(second) + content;
    }

    /**
     * 在byte数组开头加上缓存时间标志，使数据具有时效性
     *
     * @param data   缓存的数据
     * @param second 缓存的时长
     */
    public static byte[] newBytesWithTimeInfo(byte[] data, int second) {
        byte[] timeInfo = createTimeInfo(second).getBytes();
        byte[] result = new byte[timeInfo.length + data.length];
        System.arraycopy(timeInfo, 0, result, 0, timeInfo.length);
        System.arraycopy(data, 0, result, timeInfo.length, data.length);
        return result;
    }

    //创建缓存时间标志，格式"System.currentTimeMillis()(13位)-缓存时长"，如"1584629349290-120 "，表示缓存120秒
    private static String createTimeInfo(int second) {
        StringBuilder currentTime = new StringBuilder(System.currentTimeMillis() + "");
        while (currentTime.length() < 13) {
            currentTime.insert(0, "0");
        }
        return currentTime + "-" + second + SEPARATOR;
    }

    //清除缓存时间标志并获取值
    public static String clearTimeInfo(String content) {
        if (content != null && hasTimeInfo(content.getBytes())) {
            content = content.substring(content.indexOf(SEPARATOR) + 1);
        }
        return content;
    }

    //清除缓存时间标志并获取值
    public static byte[] clearTimeInfo(byte[] data) {
        if (hasTimeInfo(data)) {
            return copyOfRange(data, indexOf(data, SEPARATOR) + 1, data.length);
        }
        return data;
    }

    //获取缓存时间标志，返回长度为2的字符串数组，分别是数据缓存的开始时间和缓存的时长
    private static String[] getTimeInfoFromData(byte[] data) {
        if (hasTimeInfo(data)) {
            String saveTime = new String(copyOfRange(data, 0, 13));
            String saveDuration = new String(copyOfRange(data, 14, indexOf(data, SEPARATOR)));
            return new String[]{saveTime, saveDuration};
        }
        return null;
    }

    /*
     * byte数组中是否有缓存时间标志
     * 对于具有时效性的数据，会在对应的byte数组开头加上缓存的开始时间和缓存时长
     * 即"System.currentTimeMillis()(13位)-缓存时长 缓存数据"，如"1584629349290-120 xxxxx"，表示缓存120秒
     */
    private static boolean hasTimeInfo(byte[] data) {
        return data != null && data.length > 15 && data[13] == '-'
                && indexOf(data, SEPARATOR) > 14;
    }

    private static int indexOf(byte[] data, char c) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == c) {
                return i;
            }
        }
        return -1;
    }

    //复制byte数组中指定位置的数据，不包括to下标对应的数据
    private static byte[] copyOfRange(byte[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        byte[] copy = new byte[newLength];
        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
        return copy;
    }

}
