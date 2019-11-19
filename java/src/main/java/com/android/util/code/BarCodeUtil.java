package com.android.util.code;

import android.graphics.Bitmap;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Created by xuzhb on 2019/11/18
 * Desc:条形码工具类
 */
public class BarCodeUtil {

    public static Bitmap creatBarcode(String contents, int widthPix, int heightPix) {
        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix result = writer.encode(contents, BarcodeFormat.CODE_128, widthPix, heightPix);
            int width = result.getWidth();
            int height = result.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = result.get(x, y) ? 0xff000000 : 0xffffffff;
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

}
