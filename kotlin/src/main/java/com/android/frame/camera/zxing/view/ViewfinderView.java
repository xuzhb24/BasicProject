/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.frame.camera.zxing.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.android.basicproject.R;
import com.android.frame.camera.zxing.camera.CameraManager;
import com.google.zxing.ResultPoint;

import java.util.Collection;
import java.util.HashSet;


/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

    private static final long ANIMATION_DELAY = 10L;
    private static final int OPAQUE = 1;
    private static final int CORNER_INSIDE = 1;   //四个边角在扫描区内
    private static final int CORNER_OUTSIDE = 2;  //四个边角在扫描区外

    private Paint paint;
    //扫描区四个边角的颜色
    private int cornerColor;
    //扫描区边角的大小
    private float cornerSize;
    //扫描区边角的宽度
    private float cornerStrokeWidth;
    //边角的方向，在扫描区域内还是扫描区域外
    private int cornerPosition;
    //扫描线颜色
    private int lineColor;
    //扫描线高度
    private float lineHeight;
    //扫描线移动距离
    private float lineMoveDistance;
    //扫描区域宽度度
    private float frameWidth;
    //扫描区域高度
    private float frameHeight;
    //扫描区域中心位置的X坐标，默认正中间，在onLayout中设置
    private float frameCenterX;
    //扫描区域中心位置的Y坐标，默认正中间，在onLayout中设置
    private float frameCenterY;
    //扫描区域边框颜色
    private int frameColor;
    //扫描区域边框宽度
    private float frameStrokeWidth;
    //模糊区域颜色
    private int maskColor;
    //扫描点的颜色
    private int resultPointColor;
    //扫描区域提示文本
    private String labelText;
    //扫描区域提示文本颜色
    private int labelTextColor;
    //扫描区域提示文本字体大小
    private float labelTextSize;
    //扫描区域提示文本的边距
    private float labelTextMargin;

    public static int scannerStart = 0;
    public static int scannerEnd = 0;

    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;

    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化自定义属性信息
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ViewfinderView);
        cornerColor = ta.getColor(R.styleable.ViewfinderView_corner_color, getResources().getColor(R.color.colorPrimary));
        cornerSize = ta.getDimension(R.styleable.ViewfinderView_corner_size, dp2px(context, 28));
        cornerStrokeWidth = ta.getDimension(R.styleable.ViewfinderView_corner_stroke_width, dp2px(context, 4));
        cornerPosition = ta.getInt(R.styleable.ViewfinderView_corner_position, CORNER_INSIDE);
        lineColor = ta.getColor(R.styleable.ViewfinderView_line_color, getResources().getColor(R.color.colorPrimary));
        lineHeight = ta.getDimension(R.styleable.ViewfinderView_line_height, dp2px(context, 3));
        lineMoveDistance = ta.getDimension(R.styleable.ViewfinderView_line_move_distance, dp2px(context, 2));
        frameWidth = ta.getDimension(R.styleable.ViewfinderView_frame_width, dp2px(context, 220));
        frameHeight = ta.getDimension(R.styleable.ViewfinderView_frame_height, dp2px(context, 220));
        frameCenterX = ta.getDimension(R.styleable.ViewfinderView_frame_centerX, -1);
        frameCenterY = ta.getDimension(R.styleable.ViewfinderView_frame_centerY, -1);
        frameColor = ta.getColor(R.styleable.ViewfinderView_frame_color, Color.parseColor("#90FFFFFF"));
        frameStrokeWidth = ta.getDimension(R.styleable.ViewfinderView_frame_stroke_width, dp2px(context, 0.2f));
        maskColor = ta.getColor(R.styleable.ViewfinderView_mask_color, Color.parseColor("#60000000"));
        resultPointColor = ta.getColor(R.styleable.ViewfinderView_result_point_color, Color.TRANSPARENT);
        labelText = ta.getString(R.styleable.ViewfinderView_label_text);
        labelTextColor = ta.getColor(R.styleable.ViewfinderView_label_text_color, Color.WHITE);
        labelTextSize = ta.getDimension(R.styleable.ViewfinderView_label_text_size, sp2px(context, 15));
        labelTextMargin = ta.getDimension(R.styleable.ViewfinderView_label_text_margin, dp2px(context, 18));
        ta.recycle();
        paint = new Paint();
        paint.setAntiAlias(true);
        possibleResultPoints = new HashSet<ResultPoint>(5);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //如果没有设置frameCenterX和frameCenterY默认布局正中间的X、Y坐标
        frameCenterX = (frameCenterX == -1) ? getWidth() / 2f : frameCenterX;
        frameCenterY = (frameCenterY == -1) ? getHeight() / 2f : frameCenterY;
        //设置扫描区域位置
        int leftOffset = (int) (frameCenterX - frameWidth / 2f);
        int topOffset = (int) (frameCenterY - frameHeight / 2f);
        //设置扫描区不超过屏幕
        leftOffset = leftOffset > 0 ? leftOffset : 0;
        topOffset = topOffset > 0 ? topOffset : 0;
        Rect rect = new Rect();
        rect.left = leftOffset;
        rect.top = topOffset;
        rect.right = (int) (leftOffset + frameWidth);
        rect.bottom = (int) (topOffset + frameHeight);
        CameraManager.get().setFramingRect(rect);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Rect frame = CameraManager.get().getFramingRect();
        if (frame == null) {
            return;
        }
        if (scannerStart == 0 || scannerEnd == 0) {
            scannerStart = frame.top;
            scannerEnd = frame.bottom;
        }

        int width = canvas.getWidth();
        int height = canvas.getHeight();
        //绘制模糊区域
        drawExterior(canvas, frame, width, height);
        //绘制扫描区边框
        drawFrame(canvas, frame);
        //绘制边角
        drawCorner(canvas, frame);
        //绘制提示信息
        drawTextInfo(canvas, frame);
        //绘制扫描线
        drawScanLine(canvas, frame);
        //绘制闪烁点
        drawResultPoint(canvas, frame);

        // Request another update at the animation interval, but only repaint the laser line,
        // not the entire viewfinder mask.
        //指定重绘区域，该方法会在子线程中执行
        postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
    }

    // 绘制模糊区域 Draw the exterior (i.e. outside the framing rect) darkened
    private void drawExterior(Canvas canvas, Rect frame, int width, int height) {
        paint.setColor(maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom, paint);
        canvas.drawRect(frame.right, frame.top, width, frame.bottom, paint);
        canvas.drawRect(0, frame.bottom, width, height, paint);
    }

    // 绘制扫描区边框 Draw a two pixel solid black border inside the framing rect
    private void drawFrame(Canvas canvas, Rect frame) {
        if (frameStrokeWidth > 0) {
            paint.setColor(frameColor);
            if (cornerPosition == CORNER_INSIDE) {  //边角在扫描区内
                //左边
                canvas.drawRect(frame.left, frame.top, frame.left + frameStrokeWidth, frame.bottom, paint);
                //上边
                canvas.drawRect(frame.left, frame.top, frame.right, frame.top + frameStrokeWidth, paint);
                //右边
                canvas.drawRect(frame.right - frameStrokeWidth, frame.top, frame.right, frame.bottom, paint);
                //下边
                canvas.drawRect(frame.left, frame.bottom - frameStrokeWidth, frame.right, frame.bottom, paint);
            } else {  //边角在扫描区外
                //左边
                canvas.drawRect(frame.left - frameStrokeWidth, frame.top - frameStrokeWidth,
                        frame.left, frame.bottom + frameStrokeWidth, paint);
                //上边
                canvas.drawRect(frame.left - frameStrokeWidth, frame.top - frameStrokeWidth,
                        frame.right + frameStrokeWidth, frame.top, paint);
                //右边
                canvas.drawRect(frame.right, frame.top - frameStrokeWidth, frame.right + frameStrokeWidth,
                        frame.bottom + frameStrokeWidth, paint);
                //下边
                canvas.drawRect(frame.left - frameStrokeWidth, frame.bottom, frame.right + frameStrokeWidth,
                        frame.bottom + frameStrokeWidth, paint);
            }
        }
    }

    //绘制边角
    private void drawCorner(Canvas canvas, Rect frame) {
        if (cornerSize > 0 && cornerStrokeWidth > 0) {
            paint.setColor(cornerColor);
            if (cornerPosition == CORNER_INSIDE) {  //绘制在扫描区域内区
                //左上
                canvas.drawRect(frame.left, frame.top, frame.left + cornerSize, frame.top + cornerStrokeWidth, paint);
                canvas.drawRect(frame.left, frame.top, frame.left + cornerStrokeWidth, frame.top + cornerSize, paint);
                //右上
                canvas.drawRect(frame.right - cornerSize, frame.top, frame.right, frame.top + cornerStrokeWidth, paint);
                canvas.drawRect(frame.right - cornerStrokeWidth, frame.top, frame.right, frame.top + cornerSize, paint);
                //左下
                canvas.drawRect(frame.left, frame.bottom - cornerSize, frame.left + cornerStrokeWidth, frame.bottom, paint);
                canvas.drawRect(frame.left, frame.bottom - cornerStrokeWidth, frame.left + cornerSize, frame.bottom, paint);
                //右下
                canvas.drawRect(frame.right - cornerSize, frame.bottom - cornerStrokeWidth, frame.right, frame.bottom, paint);
                canvas.drawRect(frame.right - cornerStrokeWidth, frame.bottom - cornerSize, frame.right, frame.bottom, paint);
            } else {  //绘制在扫描区域外区
                //左上
                canvas.drawRect(frame.left - cornerStrokeWidth, frame.top - cornerStrokeWidth,
                        frame.left - cornerStrokeWidth + cornerSize, frame.top, paint);
                canvas.drawRect(frame.left - cornerStrokeWidth, frame.top - cornerStrokeWidth,
                        frame.left, frame.top - cornerStrokeWidth + cornerSize, paint);
                //右上
                canvas.drawRect(frame.right + cornerStrokeWidth - cornerSize, frame.top - cornerStrokeWidth,
                        frame.right + cornerStrokeWidth, frame.top, paint);
                canvas.drawRect(frame.right, frame.top - cornerStrokeWidth,
                        frame.right + cornerStrokeWidth, frame.top - cornerStrokeWidth + cornerSize, paint);
                //左下
                canvas.drawRect(frame.left - cornerStrokeWidth, frame.bottom, frame.left - cornerStrokeWidth + cornerSize,
                        frame.bottom + cornerStrokeWidth, paint);
                canvas.drawRect(frame.left - cornerStrokeWidth, frame.bottom + cornerStrokeWidth - cornerSize,
                        frame.left, frame.bottom + cornerStrokeWidth, paint);
                //右下
                canvas.drawRect(frame.right + cornerStrokeWidth - cornerSize, frame.bottom,
                        frame.right + cornerStrokeWidth, frame.bottom + cornerStrokeWidth, paint);
                canvas.drawRect(frame.right, frame.bottom + cornerStrokeWidth - cornerSize,
                        frame.right + cornerStrokeWidth, frame.bottom + cornerStrokeWidth, paint);
            }
        }
    }

    //绘制文本
    private void drawTextInfo(Canvas canvas, Rect frame) {
        if (!TextUtils.isEmpty(labelText)) {
            paint.setColor(labelTextColor);
            paint.setTextSize(labelTextSize);
            paint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetrics fm = paint.getFontMetrics();
            float baseY = frame.bottom + labelTextMargin - fm.ascent;
            canvas.drawText(labelText, frame.left + frame.width() / 2, baseY, paint);
        }
    }

    //绘制扫描线
    private void drawScanLine(Canvas canvas, Rect frame) {
        if (lineHeight > 0) {
            paint.setColor(lineColor);
            RadialGradient radialGradient = new RadialGradient(
                    (float) (frame.left + frame.width() / 2),
                    (float) (scannerStart + lineHeight / 2),
                    360f,
                    lineColor,
                    shadeColor(lineColor),
                    Shader.TileMode.MIRROR);

            paint.setShader(radialGradient);
            if (scannerStart <= scannerEnd) {
                //椭圆
                RectF rectF = new RectF(frame.left + 2 * lineHeight, scannerStart, frame.right - 2 * lineHeight,
                        scannerStart + lineHeight);
                canvas.drawOval(rectF, paint);
                scannerStart += lineMoveDistance;
            } else {
                scannerStart = frame.top;
            }
            paint.setShader(null);
        }
    }

    private void drawResultPoint(Canvas canvas, Rect frame) {
        if (resultPointColor != Color.TRANSPARENT) {
            Collection<ResultPoint> currentPossible = possibleResultPoints;
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new HashSet<ResultPoint>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(OPAQUE);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 6.0f, paint);
                }
            }
            if (currentLast != null) {
                paint.setAlpha(OPAQUE / 2);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 3.0f, paint);
                }
            }
        }
    }

    //处理颜色模糊
    public int shadeColor(int color) {
        String hax = Integer.toHexString(color);
        String result = "20" + hax.substring(2);
        return Integer.valueOf(result, 16);
    }

    public void drawViewfinder() {
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }

    private int dp2px(Context context, float dpValue) {
        float density = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    private int sp2px(Context context, float spValue) {
        float scaleDensity = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scaleDensity + 0.5f);
    }


}
