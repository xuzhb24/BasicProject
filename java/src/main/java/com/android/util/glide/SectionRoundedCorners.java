package com.android.util.glide;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Created by xuzhb on 2020/11/17
 * Desc:Glide加载部分圆角
 */
public class SectionRoundedCorners extends BitmapTransformation {

    private byte[] ID_BYTES;

    private float topLeftRadius;
    private float topRightRadius;
    private float bottomLeftRadius;
    private float bottomRightRadius;

    public float getTopLeftRadius() {
        return topLeftRadius;
    }

    public float getTopRightRadius() {
        return topRightRadius;
    }

    public float getBottomLeftRadius() {
        return bottomLeftRadius;
    }

    public float getBottomRightRadius() {
        return bottomRightRadius;
    }

    public SectionRoundedCorners(float topLeftRadius, float topRightRadius, float bottomLeftRadius, float bottomRightRadius) {
        this.topLeftRadius = topLeftRadius;
        this.topRightRadius = topRightRadius;
        this.bottomLeftRadius = bottomLeftRadius;
        this.bottomRightRadius = bottomRightRadius;
        ID_BYTES = ("com.android.util.glide.RoundCorner" + topLeftRadius + topRightRadius + bottomLeftRadius + bottomRightRadius).getBytes(Key.CHARSET);
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();
        Bitmap bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setHasAlpha(true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        RectF rectF = new RectF(0, 0, width, height);
        float[] radii = new float[]{
                topLeftRadius, topLeftRadius, topRightRadius, topRightRadius,
                bottomRightRadius, bottomRightRadius, bottomLeftRadius, bottomLeftRadius
        };
        Path path = new Path();
        path.addRoundRect(rectF, radii, Path.Direction.CW);
        canvas.drawPath(path, paint);
        return bitmap;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

    @Override
    public boolean equals(Object other) {
        if (other != null && other instanceof SectionRoundedCorners) {
            return topLeftRadius == ((SectionRoundedCorners) other).getTopLeftRadius() &&
                    topRightRadius == ((SectionRoundedCorners) other).getTopRightRadius() &&
                    bottomLeftRadius == ((SectionRoundedCorners) other).getBottomLeftRadius() &&
                    bottomRightRadius == ((SectionRoundedCorners) other).getBottomRightRadius();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ID_BYTES.hashCode() + Float.valueOf(topLeftRadius).hashCode() + Float.valueOf(topRightRadius).hashCode()
                + Float.valueOf(bottomLeftRadius).hashCode() + Float.valueOf(bottomRightRadius).hashCode();
    }
}
