package com.android.util.code

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException

/**
 * Created by xuzhb on 2019/11/18
 * Desc:条形码工具类
 */
object BarCodeUtil {

    fun creatBarcode(contents: String, widthPix: Int, heightPix: Int): Bitmap? {
        try {
            val writer = MultiFormatWriter()
            val result = writer.encode(contents, BarcodeFormat.CODE_128, widthPix, heightPix)
            val width = result.width
            val height = result.height
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] = if (result.get(x, y)) -0x1000000 else -0x1
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

}
