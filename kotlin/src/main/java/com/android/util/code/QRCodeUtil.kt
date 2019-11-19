package com.android.util.code

import android.graphics.Bitmap
import android.graphics.Canvas
import android.text.TextUtils
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.*

/**
 * Created by xuzhb on 2019/11/18
 * Desc:二维码工具类
 */
object QRCodeUtil {

    //创建指定大小的二维码
    fun createQRCode(content: String, widthPix: Int, heightPix: Int): Bitmap? {
        try {
            if (TextUtils.isEmpty(content)) {
                return null
            }
            val hints = Hashtable<EncodeHintType, String>()
            hints[EncodeHintType.CHARACTER_SET] = "utf-8"
            val matrix = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix)
            val width = matrix.width
            val height = matrix.height
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = -0x1000000
                    }
                }
            }
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }

    //指定大小的二维码，带logo
    fun createQRCode(content: String, widthPix: Int, heightPix: Int, logoBm: Bitmap?): Bitmap? {
        try {
            if (TextUtils.isEmpty(content)) {
                return null
            }
            // 配置参数
            val hints = HashMap<EncodeHintType, Any>()
            hints[EncodeHintType.CHARACTER_SET] = "utf-8"
            // 容错级别
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.M
            // 图像数据转换，使用了矩阵转换
            val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints)
            val pixels = IntArray(widthPix * heightPix)
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (y in 0 until heightPix) {
                for (x in 0 until widthPix) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = -0x1000000
                    } else {
                        pixels[y * widthPix + x] = -0x1
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            var bitmap: Bitmap? = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888)
            bitmap!!.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix)
            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm)
            }
            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 在二维码中间添加Logo图案
     */
    private fun addLogo(src: Bitmap?, logo: Bitmap?): Bitmap? {
        if (src == null) {
            return null
        }
        if (logo == null) {
            return src
        }
        //获取图片的宽高
        val srcWidth = src.width
        val srcHeight = src.height
        val logoWidth = logo.width
        val logoHeight = logo.height
        if (srcWidth == 0 || srcHeight == 0) {
            return null
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return src
        }
        //logo大小为二维码整体大小的1/5
        val scaleFactor = srcWidth * 1.0f / 5f / logoWidth.toFloat()
        var bitmap: Bitmap? = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888)
        try {
            val canvas = Canvas(bitmap!!)
            canvas.drawBitmap(src, 0f, 0f, null)
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2f, srcHeight / 2f)
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2f, (srcHeight - logoHeight) / 2f, null)
            //			canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.save()
            canvas.restore()
        } catch (e: Exception) {
            bitmap = null
            e.stackTrace
        }
        return bitmap
    }

}
