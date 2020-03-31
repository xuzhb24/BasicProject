package com.android.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IntRange;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Create by xuzhb on 2019/10/11
 * Desc:图片工具
 */
public class BitmapUtil {

    //根据采样率高效加载图片
    public static Bitmap decodeBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    //计算采样率
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    //保存图片到系统相册，返回true表示保存成功，false表示保存失败
    public static boolean saveImageToGallery(Context context, Bitmap bitmap, String bitmapName) {
        if (bitmap == null) {
            return false;
        }
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "extra_picture");  //图片存储路径
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = bitmapName + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            // 其次把文件插入到系统图库
//            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);  //使用这个方法会同时生成两张图片

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        return true;
    }

    //Bitmap转化byte数组
    public static byte[] bitmap2Bytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    //byte数组转化Bitmap
    public static Bitmap bytesToBitmap(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    //Bitmap转化Drawable
    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        return new BitmapDrawable(bitmap);
    }

    //Drawable转化Bitmap
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        //取Drawable的长宽
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        //取Drawable的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE
                ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        //建立对应Bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        //建立对应Bitmap的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        //把Dawable内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 保存Bitmap到文件中
     *
     * @param bitmap   保存的图片
     * @param filePath 保存的文件路径，如sdcard/AAAA/test.png
     * @param quality  图片压缩质量，取值范围0-100
     */
    public static boolean saveBitmapToFile(Bitmap bitmap, String filePath, @IntRange(from = 0, to = 100) int quality) {
        if (bitmap == null) {
            return false;
        }
        String dirPath = filePath.substring(0, filePath.lastIndexOf(File.separator));
        try {
            if (FileUtil.createOrExistsDirectory(dirPath)) {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
                bitmap.compress(Bitmap.CompressFormat.PNG, quality, bos);
                bos.flush();
                bos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //通过BitmapFactory.decodeFile从文件中获取Bitmap
    public static Bitmap getBitmapByDecodeFile(String filePath) {
        if (!FileUtil.isFileExists(filePath)) {
            return null;
        }
        return BitmapFactory.decodeFile(filePath);
    }

    //通过BitmapFactory.decodeStream从文件中获取Bitmap
    public static Bitmap getBitmapByDecodeStream(String filePath) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //通过BitmapFactory.decodeResource从资源文件中获取Bitmap
    public static Bitmap getBitmapByDecodeResource(Resources res, int resId) {
        return BitmapFactory.decodeResource(res, resId);
    }

    /**
     * 图片按比例大小压缩方法
     *
     * @param srcPath （根据路径获取图片并压缩）
     * @return
     */
    public static byte[] compressImageToLimitBytes(String srcPath, int limitK) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了，只读取图片的大小，不分配内存
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是1920*1080分辨率，所以高和宽我们设置为
        float hh = 1920f;// 这里设置高度为1920f
        float ww = 1080f;// 这里设置宽度为1080f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        //检测并旋转图片
        Bitmap bitmap1 = changeImageLocate(srcPath, bitmap);
        return compressImage(bitmap1, limitK);// 压缩好比例大小后再进行质量压缩
    }

    //检测并旋转图片
    public static Bitmap changeImageLocate(String filepath, Bitmap bitmap) {
        //根据图片的filepath获取到一个ExifInterface的对象
        int degree = 0;
        try {
            ExifInterface exif = new ExifInterface(filepath);
            if (exif != null) {
                // 读取图片中相机方向信息
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                Log.e("degree========ori====", ori + "");
                // 计算旋转角度
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                    default:
                        degree = 0;
                        break;
                }
                Log.e("degree============", degree + "");
                if (degree != 0) {
                    Log.e("degree============", "degree != 0");
                    // 旋转图片
                    Matrix m = new Matrix();
//                    m.setScale(0.5f,0.5f);
                    m.postRotate(degree);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                    return bitmap;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //质量压缩
    public static byte[] compressImage(Bitmap bitmap, int limitK) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);  //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (baos.toByteArray().length / 1024 > limitK) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();  //重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);  //这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;  //每次都减少10
        }
        byte[] bytes = baos.toByteArray();
        recycleBitmap(bitmap);
        return bytes;
    }

    //回收Bitmap
    public static void recycleBitmap(Bitmap... bitmaps) {
        for (Bitmap bm : bitmaps) {
            if (bm != null && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }

}
