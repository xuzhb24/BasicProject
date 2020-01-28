package com.android.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.io.*

/**
 * Create by xuzhb on 2019/10/11
 * Desc:图片工具
 */
object BitmapUtil {

    //保存图片到系统相册，返回true表示保存成功，false表示保存失败
    fun saveImageToGallery(
        context: Context,
        bitmap: Bitmap?,
        bitmapName: String = System.currentTimeMillis().toString()  //存储的图片名称
    ): Boolean {
        if (bitmap == null) {
            return false
        }
        // 首先保存图片
        val appDir = File(Environment.getExternalStorageDirectory(), "extra_picture")  //图片存储路径
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val fileName = "${bitmapName}.jpg"
        val file = File(appDir, fileName)
        try {
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            // 其次把文件插入到系统图库
//            MediaStore.Images.Media.insertImage(context.contentResolver, file.absolutePath, fileName, null)  //使用这个方法会同时生成两张图片

            val values = ContentValues()
            values.put(MediaStore.Images.Media.DATA, file.absolutePath)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return false
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
        // 最后通知图库更新
        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))
        return true
    }

    //byte数组转换成Bitmap
    fun bytesToBitmap(bytes: ByteArray): Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

    //Bitmap转换成byte数组
    fun bitmapToBytes(bitmap: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }

    //质量压缩
    fun compressImage(bitmap: Bitmap, limitK: Int): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)  // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 90
        while (baos.toByteArray().size / 1024 > limitK) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos)  //这里压缩options%，把压缩后的数据存放到baos中
            options -= 10  //每次都减少10
        }
        val bytes = baos.toByteArray()
        recycleBitmap(bitmap)
        return bytes
    }

    //回收Bitmap
    fun recycleBitmap(vararg bitmaps: Bitmap) {
        for (bm in bitmaps) {
            if (bm != null && !bm.isRecycled) {
                bm.recycle()
            }
        }
    }

}