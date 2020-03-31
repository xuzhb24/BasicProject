package com.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xuzhb on 2020/3/21
 * Desc:磁盘缓存工具类
 */
public class CacheUtil {

    private static final String DEFAULT_CACHE_NAME = "CacheUtil";    //默认缓存文件名
    private static final int DEFAULT_MAX_SIZE = 50 * 1024 * 1024;    //50MB
    private static final int DEFAULT_MAX_COUNT = Integer.MAX_VALUE;  //不限制存放数据的数量
    private static Map<String, CacheUtil> mInstanceMap = new HashMap<>();
    private CacheManager mCacheManager;

    public static CacheUtil get(Context context) {
        return get(context, DEFAULT_CACHE_NAME);
    }

    public static CacheUtil get(Context context, String cacheName) {
        File file = new File(context.getCacheDir(), cacheName);
        return get(file, DEFAULT_MAX_SIZE, DEFAULT_MAX_COUNT);
    }

    public static CacheUtil get(Context context, long maxSise, int maxCount) {
        return get(context, DEFAULT_CACHE_NAME, maxSise, maxCount);
    }

    public static CacheUtil get(Context context, String cacheName, long maxSise, int maxCount) {
        File file = new File(context.getCacheDir(), cacheName);
        return get(file, maxSise, maxCount);
    }

    public static CacheUtil get(File cacheDir) {
        return get(cacheDir, DEFAULT_MAX_SIZE, DEFAULT_MAX_COUNT);
    }

    /**
     * 获取缓存工具实例
     *
     * @param cacheDir 缓存文件的存储路径
     * @param maxSise  缓存的总大小
     * @param maxCount 缓存数据的最大数量
     */
    public static CacheUtil get(File cacheDir, long maxSise, int maxCount) {
        CacheUtil manager = mInstanceMap.get(cacheDir.getAbsoluteFile() + myPid());
        if (manager == null) {
            manager = new CacheUtil(cacheDir, maxSise, maxCount);
            mInstanceMap.put(cacheDir.getAbsolutePath() + myPid(), manager);
        }
        return manager;
    }

    private static String myPid() {
        return "_" + android.os.Process.myPid();
    }

    private CacheUtil(File cacheDir, long maxSize, int maxCount) {
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            throw new RuntimeException("can't make dirs in " + cacheDir.getAbsolutePath());
        }
        mCacheManager = new CacheManager(cacheDir, maxSize, maxCount);
    }

    //保存字符串数据到缓存中
    public void putString(String key, String value) {
        File file = mCacheManager.newFile(key);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file), 1024);
            out.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mCacheManager.put(file);
        }
    }

    //保存字符串数据到缓存中，saveTime：保存的时间，以秒为单位
    public void putString(String key, String value, int saveTime) {
        putString(key, DataDueUtil.newStringWithTimeInfo(value, saveTime));
    }

    //读取字符串数据
    public String getString(String key) {
        File file = mCacheManager.get(key);
        if (!file.exists()) {
            return null;
        }
        boolean removeFile = false;  //是否在读取后移除文件
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String currentLine;
            while ((currentLine = in.readLine()) != null) {
                sb.append(currentLine);
            }
            if (!DataDueUtil.isDue(sb.toString())) {  //对于无时效性的数据和尚未过期的数据，isDue返回false
                return DataDueUtil.clearTimeInfo(sb.toString());
            } else {  //数据已经过期
                removeFile = true;
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (removeFile) {
                mCacheManager.remove(key);  //删除已经过期的数据
            }
        }
    }

    //保存JSONObject数据到缓存中
    public void putJSONObject(String key, JSONObject value) {
        putString(key, value.toString());
    }

    //保存JSONObject数据到缓存中，saveTime：保存的时间，以秒为单位
    public void putJSONObject(String key, JSONObject value, int saveTime) {
        putString(key, value.toString(), saveTime);
    }

    //读取JSONObject数据
    public JSONObject getJSONObject(String key) {
        try {
            return new JSONObject(getString(key));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //保存JSONArray数据到缓存中
    public void putJSONArray(String key, JSONArray value) {
        putString(key, value.toString());
    }

    //保存JSONArray数据到缓存中，saveTime：保存的时间，以秒为单位
    public void putJSONArray(String key, JSONArray value, int saveTime) {
        putString(key, value.toString(), saveTime);
    }

    //读取JSONArray数据
    public JSONArray getJSONArray(String key) {
        try {
            return new JSONArray(getString(key));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //保存byte数组到缓存中
    public void putBytes(String key, byte[] value) {
        File file = mCacheManager.newFile(key);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mCacheManager.put(file);
        }
    }

    //保存byte数组到缓存中，saveTime：保存的时间，以秒为单位
    public void putBytes(String key, byte[] value, int saveTime) {
        putBytes(key, DataDueUtil.newBytesWithTimeInfo(value, saveTime));
    }

    //获取byte数组
    public byte[] getBytes(String key) {
        RandomAccessFile randomAccessFile = null;
        boolean removeFile = false;  //是否在读取后移除文件
        try {
            File file = mCacheManager.get(key);
            if (!file.exists()) {
                return null;
            }
            randomAccessFile = new RandomAccessFile(file, "r");
            byte[] bytes = new byte[(int) randomAccessFile.length()];
            randomAccessFile.read(bytes);
            if (!DataDueUtil.isDue(bytes)) {  //对于无时效性的数据和尚未过期的数据，isDue返回false
                return DataDueUtil.clearTimeInfo(bytes);
            } else {  //数据已经过期
                removeFile = true;
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (removeFile) {
                mCacheManager.remove(key);  //删除已经过期的数据
            }
        }
    }

    //保存Serializable数据到缓存中
    public void putObject(String key, Serializable value) {
        putObject(key, value, -1);
    }

    //保存Serializable数据到缓存中，saveTime：保存的时间，以秒为单位
    public void putObject(String key, Object value, int saveTime) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            byte[] data = baos.toByteArray();
            if (saveTime != -1) {  //保存有时效性的数据
                putBytes(key, data, saveTime);
            } else {
                putBytes(key, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //读取Serializable数据
    public Object getObject(String key) {
        byte[] data = getBytes(key);
        if (data != null) {
            ByteArrayInputStream bais = null;
            ObjectInputStream ois = null;
            try {
                bais = new ByteArrayInputStream(data);
                ois = new ObjectInputStream(bais);
                return ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    //保存Bitmap到缓存中
    public void putBitmap(String key, Bitmap value) {
        putBytes(key, BitmapUtil.bitmap2Bytes(value));
    }

    //保存Bitmap到缓存中，saveTime：保存的时间，以秒为单位
    public void putBitmap(String key, Bitmap value, int saveTime) {
        putBytes(key, BitmapUtil.bitmap2Bytes(value), saveTime);
    }

    //读取Bitmap数据
    public Bitmap getBitmap(String key) {
        return BitmapUtil.bytesToBitmap(getBytes(key));
    }

    //保存Drawable到缓存中
    public void putDrawable(String key, Drawable value) {
        putBitmap(key, BitmapUtil.drawable2Bitmap(value));
    }

    //保存Drawable到缓存中，saveTime：保存的时间，以秒为单位
    public void putDrawable(String key, Drawable value, int saveTime) {
        putBitmap(key, BitmapUtil.drawable2Bitmap(value), saveTime);
    }

    //读取Drawable数据
    public Drawable getDrawable(String key) {
        return BitmapUtil.bitmap2Drawable(BitmapUtil.bytesToBitmap(getBytes(key)));
    }

    //读取缓存文件
    public File getFile(String key) {
        File file = mCacheManager.newFile(key);
        if (file.exists()) {
            return file;
        }
        return null;
    }

    //移除指定key对应的缓存内容
    public boolean remove(String key) {
        return mCacheManager.remove(key);
    }

    //清除所有缓存内容
    public void delete() {
        mCacheManager.delete();
    }

    public static class CacheManager {

        private final AtomicLong mCacheSize;
        private final AtomicInteger mCacheCount;
        private final long mSizeLimit;
        private final int mCountLimit;
        private final Map<File, Long> mLastUsageDates = Collections.synchronizedMap(new HashMap<File, Long>());
        protected File mCacheDir;

        private CacheManager(File cacheDir, long sizeLimit, int countLimit) {
            mCacheDir = cacheDir;
            mSizeLimit = sizeLimit;
            mCountLimit = countLimit;
            mCacheSize = new AtomicLong();
            mCacheCount = new AtomicInteger();
            calculateCacheSizeAndCacheCount();
        }

        //计算cacheSize和cacheCount
        private void calculateCacheSizeAndCacheCount() {
            new Thread(() -> {
                int size = 0;
                int count = 0;
                File[] cachedFiles = mCacheDir.listFiles();
                if (cachedFiles != null) {
                    for (File cachedFile : cachedFiles) {
                        size += cachedFile.length();
                        count += 1;
                        mLastUsageDates.put(cachedFile, cachedFile.lastModified());
                    }
                    mCacheSize.set(size);
                    mCacheCount.set(count);
                }
            }).start();
        }

        private void put(File file) {
            int curCacheCount = mCacheCount.get();
            while (curCacheCount + 1 > mCountLimit) {
                long freedSize = removeNext();
                mCacheSize.addAndGet(-freedSize);
                curCacheCount = mCacheCount.addAndGet(-1);
            }
            mCacheCount.addAndGet(1);

            long valueSize = file.length();
            long curCacheSize = mCacheSize.get();
            while (curCacheSize + valueSize > mSizeLimit) {
                long freedSize = removeNext();
                curCacheSize = mCacheSize.addAndGet(-freedSize);
            }
            mCacheSize.addAndGet(valueSize);

            Long currentTime = System.currentTimeMillis();
            file.setLastModified(currentTime);
            mLastUsageDates.put(file, currentTime);
        }

        private File get(String key) {
            File file = newFile(key);
            Long currentTime = System.currentTimeMillis();
            file.setLastModified(currentTime);
            mLastUsageDates.put(file, currentTime);
            return file;
        }

        private File newFile(String key) {
            return new File(mCacheDir, key.hashCode() + "");
        }

        private boolean remove(String key) {
            File file = get(key);
            return file.delete();
        }

        private void delete() {
            mLastUsageDates.clear();
            mCacheSize.set(0);
            File[] files = mCacheDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    f.delete();
                }
            }
        }

        //移除旧的文件
        private long removeNext() {
            if (mLastUsageDates.isEmpty()) {
                return 0;
            }
            Long oldestUsage = null;
            File mostLongUsedFile = null;
            Set<Map.Entry<File, Long>> entries = mLastUsageDates.entrySet();
            synchronized (mLastUsageDates) {
                for (Map.Entry<File, Long> entry : entries) {
                    if (mostLongUsedFile == null) {
                        mostLongUsedFile = entry.getKey();
                        oldestUsage = entry.getValue();
                    } else {
                        Long lastValueUsage = entry.getValue();
                        if (lastValueUsage < oldestUsage) {
                            oldestUsage = lastValueUsage;
                            mostLongUsedFile = entry.getKey();
                        }
                    }
                }
            }
            long fileSize = mostLongUsedFile.length();
            if (mostLongUsedFile.delete()) {
                mLastUsageDates.remove(mostLongUsedFile);
            }
            return fileSize;
        }
    }

}
