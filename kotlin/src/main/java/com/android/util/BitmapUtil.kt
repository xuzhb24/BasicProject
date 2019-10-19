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
    private fun saveImageToGallery(
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
    fun byteArrayToBitmap(bytes: ByteArray): Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

    //Bitmap转换成byte数组
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }

    //

}