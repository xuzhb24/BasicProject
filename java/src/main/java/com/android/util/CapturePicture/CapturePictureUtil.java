package com.android.util.CapturePicture;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.annotation.ColorInt;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.util.ScreenUtil;
import com.android.util.StatusBar.StatusBarUtil;

/**
 * Created by xuzhb on 2021/5/10
 * Desc:截图工具类
 */
public class CapturePictureUtil {

    private static Bitmap.Config mBitmapConfig = Bitmap.Config.RGB_565;
    private static int mBackgroundColor = Color.TRANSPARENT;
    private static Paint mPaint = new Paint();

    /**
     * 配置Bitmap Config
     *
     * @param config {@link Bitmap.Config}
     */
    public static void setBitmapConfig(Bitmap.Config config) {
        if (config == null) {
            return;
        }
        mBitmapConfig = config;
    }

    /**
     * 设置Canvas背景色
     *
     * @param backgroundColor 背景色
     */
    public static void setBackgroundColor(@ColorInt int backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    /**
     * 设置画笔
     */
    public static void setPaint(Paint paint) {
        if (paint == null) {
            return;
        }
        mPaint = paint;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     */
    public static Bitmap captureWithStatusBar(Activity activity) {
        try {
            View view = activity.getWindow().getDecorView();
            view.setDrawingCacheEnabled(true);
            //重新创建绘图缓存，此时的背景色是黑色
            view.buildDrawingCache();
            //获取绘图缓存，注意这里得到的只是一个图像的引用
            Bitmap cacheBitmap = view.getDrawingCache();
            if (cacheBitmap == null) {
                return null;
            }
            //获取屏幕宽高
            int width = ScreenUtil.getScreenWidth();
            int height = ScreenUtil.getScreenHeight();
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            //创建新的图片
            Bitmap bitmap = Bitmap.createBitmap(cacheBitmap, 0, 0, width, height);
            //释放绘图资源所使用的缓存
            view.destroyDrawingCache();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     */
    public static Bitmap captureWithoutStatusBar(Activity activity) {
        try {
            View view = activity.getWindow().getDecorView();
            view.setDrawingCacheEnabled(true);
            //重新创建绘图缓存，此时的背景色是黑色
            view.buildDrawingCache();
            //获取绘图缓存，注意这里得到的只是一个图像的引用
            Bitmap cacheBitmap = view.getDrawingCache();
            if (cacheBitmap == null) {
                return null;
            }
            //获取屏幕宽高
            int width = ScreenUtil.getScreenWidth();
            int height = ScreenUtil.getScreenHeight();
            //获取状态栏高度
            int statusBarHeight = StatusBarUtil.getStatusBarHeight(activity.getApplicationContext());
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            //创建新的图片
            Bitmap bitmap = Bitmap.createBitmap(cacheBitmap, 0, statusBarHeight, width, height - statusBarHeight);
            //释放绘图资源所使用的缓存
            view.destroyDrawingCache();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭WebView优化，推荐在setContentView前调用
     */
    public static void enableSlowWholeDocumentDraw() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }
    }

    /**
     * WebView截图
     */
    public static Bitmap captureByWebView(WebView webView) {
        return captureByWebView(webView, Integer.MAX_VALUE, mBitmapConfig, 0f);
    }

    /**
     * WebView截图
     *
     * @param maxHeight 最大高度
     */
    public static Bitmap captureByWebView(WebView webView, int maxHeight) {
        return captureByWebView(webView, maxHeight, mBitmapConfig, 0f);
    }

    /**
     * WebView截图
     *
     * @param scale 缩放比例
     */
    public static Bitmap captureByWebView(WebView webView, float scale) {
        return captureByWebView(webView, Integer.MAX_VALUE, mBitmapConfig, scale);
    }

    /**
     * WebView截图
     */
    public static Bitmap captureByWebView(WebView webView, int maxHeight, Bitmap.Config config) {
        return captureByWebView(webView, maxHeight, config, 0f);
    }

    /**
     * WebView截图
     * 在Android 5.0及以上版本，Android对WebView进行了优化，为了减少内存使用和提高性能
     * 使用WebView加载网页时只绘制显示部分，如果我们不做处理，就会出现只截到屏幕内显示的WebView内容，其它部分是空白的情况
     * 通过调用WebView.enableSlowWholeDocumentDraw()方法可以关闭这种优化，但要注意的是，该方法需要在WebView实例被创建前就要调用，
     * 否则没有效果，所以我们在WebView实例被创建前加入代码
     *
     * @param webView   {@link WebView}
     * @param maxHeight 最大高度
     * @param config    {@link Bitmap.Config}
     * @param scale     缩放比例
     */
    public static Bitmap captureByWebView(WebView webView, int maxHeight, Bitmap.Config config, float scale) {
        if (webView != null && config != null) {
            //Android 5.0以上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                try {
                    float newScale = scale;
                    if (newScale <= 0) {
                        newScale = webView.getScale();
                    }
                    int width = webView.getWidth();
                    int height = (int) (webView.getContentHeight() * newScale + 0.5);
                    //重新设置高度
                    height = Math.min(height, maxHeight);
                    //创建位图
                    Bitmap bitmap = Bitmap.createBitmap(width, height, config);
                    Canvas canvas = new Canvas(bitmap);
                    canvas.drawColor(mBackgroundColor);
                    webView.draw(canvas);
                    return bitmap;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Picture picture = webView.capturePicture();
                    int width = picture.getWidth();
                    int height = picture.getHeight();
                    if (width > 0 && height > 0) {
                        //重新设置高度
                        height = Math.min(height, maxHeight);
                        //创建位图
                        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
                        Canvas canvas = new Canvas(bitmap);
                        canvas.drawColor(mBackgroundColor);
                        picture.draw(canvas);
                        return bitmap;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 通过View绘制为Bitmap
     */
    public static Bitmap captureByView(View view) {
        return captureByView(view, mBitmapConfig);
    }

    /**
     * 通过View绘制为Bitmap
     */
    public static Bitmap captureByView(View view, Bitmap.Config config) {
        if (view == null || config == null) {
            return null;
        }
        try {
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), config);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(mBackgroundColor);
            view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            view.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过View Cache绘制为Bitmap
     */
    public static Bitmap captureByViewCache(View view) {
        if (view == null) {
            return null;
        }
        try {
            //清除视图焦点
            view.clearFocus();
            //将视图设为不可点击
            view.setPressed(false);
            //获取视图是否可以保存画图缓存
            boolean willNotCache = view.willNotCacheDrawing();
            view.setWillNotCacheDrawing(false);
            //获取绘制缓存位图的背景颜色
            int color = view.getDrawingCacheBackgroundColor();
            //设置绘图背景颜色
            view.setDrawingCacheBackgroundColor(0);
            if (color != 0) {  //获取的背景不是黑色的则释放以前的绘图缓存
                view.destroyDrawingCache();  //释放绘图资源所使用的缓存
            }
            //重新创建绘图缓存，此时的背景色是黑色
            view.buildDrawingCache();
            //获取绘图缓存，注意这里得到的只是一个图像的引用
            Bitmap cacheBitmap = view.getDrawingCache();
            if (cacheBitmap == null) {
                return null;
            }
            Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
            //释放位图内存
            view.destroyDrawingCache();
            //回滚以前的缓存设置、缓存颜色设置
            view.setWillNotCacheDrawing(willNotCache);
            view.setDrawingCacheBackgroundColor(color);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过ScrollView绘制为Bitmap，ScrollView容器中不能有诸如ListView、GridView、WebView这样的高度可变的控件
     */
    public static Bitmap captureByScrollView(ScrollView scrollView) {
        return captureByScrollView(scrollView, mBitmapConfig);
    }

    /**
     * 通过ScrollView绘制为Bitmap，ScrollView容器中不能有诸如ListView、GridView、WebView这样的高度可变的控件
     */
    public static Bitmap captureByScrollView(ScrollView scrollView, Bitmap.Config config) {
        if (scrollView == null || config == null) {
            return null;
        }
        try {
            View view = scrollView.getChildAt(0);
            int width = view.getWidth();
            int height = view.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, config);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(mBackgroundColor);
            scrollView.layout(scrollView.getLeft(), scrollView.getTop(), scrollView.getRight(), scrollView.getBottom());
            scrollView.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过HorizontalScrollView绘制为Bitmap
     */
    public static Bitmap captureByHorizontalScrollView(HorizontalScrollView scrollView) {
        return captureByHorizontalScrollView(scrollView, mBitmapConfig);
    }

    /**
     * 通过HorizontalScrollView绘制为Bitmap
     */
    public static Bitmap captureByHorizontalScrollView(HorizontalScrollView scrollView, Bitmap.Config config) {
        if (scrollView == null || config == null) {
            return null;
        }
        try {
            View view = scrollView.getChildAt(0);
            int width = view.getWidth();
            int height = view.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, config);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(mBackgroundColor);
            scrollView.layout(scrollView.getLeft(), scrollView.getTop(), scrollView.getRight(), scrollView.getBottom());
            scrollView.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过NestedScrollView绘制为Bitmap
     */
    public static Bitmap captureByNestedScrollView(NestedScrollView scrollView) {
        return captureByNestedScrollView(scrollView, mBitmapConfig);
    }

    /**
     * 通过NestedScrollView绘制为Bitmap
     */
    public static Bitmap captureByNestedScrollView(NestedScrollView scrollView, Bitmap.Config config) {
        if (scrollView == null || config == null) {
            return null;
        }
        try {
            View view = scrollView.getChildAt(0);
            int width = view.getWidth();
            int height = view.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, config);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(mBackgroundColor);
            scrollView.layout(scrollView.getLeft(), scrollView.getTop(), scrollView.getRight(), scrollView.getBottom());
            scrollView.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过ListView绘制为Bitmap
     */
    public static Bitmap captureByListView(ListView listView) {
        return captureByListView(listView, mBitmapConfig);
    }

    /**
     * 通过ListView绘制为Bitmap
     */
    public static Bitmap captureByListView(ListView listView, Bitmap.Config config) {
        if (listView == null || config == null) {
            return null;
        }
        try {
            ListAdapter listAdapter = listView.getAdapter();
            //Item总条数
            int itemCount = listAdapter.getCount();
            //没数据则直接跳过
            if (itemCount == 0) {
                return null;
            }
            //高度
            int height = 0;
            //获取子项间分隔符占用的高度
            int dividerHeight = listView.getDividerHeight();
            //View Bitmaps
            Bitmap[] bitmaps = new Bitmap[itemCount];
            //循环绘制每个Item并保存Bitmap
            for (int i = 0; i < itemCount; i++) {
                View childView = listAdapter.getView(i, null, listView);
                measureView(childView, listView.getWidth(), 0);
                bitmaps[i] = createBitmap(childView, config);
                height += childView.getMeasuredHeight();
            }
            //追加子项间分隔符占用的高度
            height += dividerHeight * (itemCount - 1);
            int width = listView.getMeasuredWidth();
            //创建位图
            Bitmap bitmap = Bitmap.createBitmap(width, height, config);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(mBackgroundColor);
            //累加高度
            int appendHeight = 0;
            for (int i = 0, len = bitmaps.length; i < len; i++) {
                Bitmap bmp = bitmaps[i];
                canvas.drawBitmap(bmp, 0, appendHeight, mPaint);
                appendHeight += bmp.getHeight() + dividerHeight;
                //释放资源
                bmp.recycle();
                bmp = null;
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过GridView绘制为Bitmap
     */
    public static Bitmap captureByGridView(GridView gridView) {
        return captureByGridView(gridView, mBitmapConfig);
    }

    /**
     * 通过GridView绘制为Bitmap
     */
    public static Bitmap captureByGridView(GridView gridView, Bitmap.Config config) {
        if (gridView == null || config == null) {
            return null;
        }
        try {
            ListAdapter listAdapter = gridView.getAdapter();
            //Item总条数
            int itemCount = listAdapter.getCount();
            //没数据则直接跳过
            if (itemCount == 0) {
                return null;
            }
            //高度
            int height = 0;
            //获取一共多少列
            int numColumns = gridView.getNumColumns();
            //每列之间的间隔
            int horizontalSpacing = gridView.getHorizontalSpacing();
            //每行之间的间隔
            int verticalSpacing = gridView.getVerticalSpacing();
            //View Bitmaps
            Bitmap[] bitmaps = new Bitmap[itemCount];
            //获取倍数(行数)
            int lineNumber = getMultiple(itemCount, numColumns);
            //计算总共的宽度(GridView宽度-列分割间距)/numColumns
            int childWidth = (gridView.getWidth() - (numColumns - 1) * horizontalSpacing) / numColumns;
            //记录每行最大高度
            int[] rowHeightArrays = new int[lineNumber];
            //临时高度(保存行中最高的列高度)
            int tempHeight;
            //循环每一行绘制每个Item并保存Bitmap
            for (int i = 0; i < lineNumber; i++) {
                //清空高度
                tempHeight = 0;
                //循环列数
                for (int j = 0; j < numColumns; j++) {
                    //获取对应的索引
                    int position = i * numColumns + j;
                    //小于总数才处理
                    if (position < itemCount) {
                        View childView = listAdapter.getView(position, null, gridView);
                        measureView(childView, childWidth, 0);
                        bitmaps[position] = createBitmap(childView, config);
                        int itemHeight = childView.getMeasuredHeight();
                        //保留最大高度
                        tempHeight = Math.max(itemHeight, tempHeight);
                    }
                    //记录高度并累加
                    if (j == numColumns - 1) {
                        height += tempHeight;
                        rowHeightArrays[i] = tempHeight;
                    }
                }
            }
            //追加子项间分隔符占用的高度
            height += verticalSpacing * (lineNumber - 1);
            int width = gridView.getMeasuredWidth();
            //创建位图
            Bitmap bitmap = Bitmap.createBitmap(width, height, config);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(mBackgroundColor);
            //累加高度
            int appendHeight = 0;
            //循环每一行绘制每个Item Bitmap
            for (int i = 0; i < lineNumber; i++) {
                //获取每一行最长列的高度
                int itemHeight = rowHeightArrays[i];
                //循环列数
                for (int j = 0; j < numColumns; j++) {
                    //获取对应的索引
                    int position = i * numColumns + j;
                    //小于总数才处理
                    if (position < itemCount) {
                        Bitmap bmp = bitmaps[position];
                        //计算边距
                        int left = j * (horizontalSpacing + childWidth);
                        Matrix matrix = new Matrix();
                        matrix.postTranslate(left, appendHeight);
                        //绘制到Bitmap
                        canvas.drawBitmap(bmp, matrix, mPaint);
                        //释放资源
                        bmp.recycle();
                        bmp = null;
                    }
                    //记录高度并累加
                    if (j == numColumns - 1) {
                        appendHeight += itemHeight + verticalSpacing;
                    }
                }
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过RecyclerView绘制为Bitmap，不支持含ItemDecoration截图
     */
    public static Bitmap captureByRecyclerView(RecyclerView recyclerView) {
        return captureByRecyclerView(recyclerView, mBitmapConfig, 0, 0);
    }

    /**
     * 通过RecyclerView绘制为Bitmap，不支持含ItemDecoration截图
     */
    public static Bitmap captureByRecyclerView(RecyclerView recyclerView, Bitmap.Config config) {
        return captureByRecyclerView(recyclerView, config, 0, 0);
    }

    /**
     * 通过RecyclerView绘制为Bitmap，不支持含ItemDecoration截图
     */
    public static Bitmap captureByRecyclerView(RecyclerView recyclerView, int spacing) {
        return captureByRecyclerView(recyclerView, mBitmapConfig, spacing, spacing);
    }

    /**
     * 通过RecyclerView绘制为Bitmap，不支持含ItemDecoration截图
     */
    public static Bitmap captureByRecyclerView(RecyclerView recyclerView, Bitmap.Config config, int spacing) {
        return captureByRecyclerView(recyclerView, config, spacing, spacing);
    }

    /**
     * 通过RecyclerView绘制为Bitmap，不支持含ItemDecoration截图
     *
     * @param verticalSpacing   每行之间的间隔
     * @param horizontalSpacing 每列之间的间隔
     */
    public static Bitmap captureByRecyclerView(RecyclerView recyclerView, Bitmap.Config config, int verticalSpacing, int horizontalSpacing) {
        if (recyclerView == null || config == null) {
            return null;
        }
        try {
            //获取适配器
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            //获取布局管理器
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager != null && adapter != null) {
                //判断布局类型
                if (layoutManager instanceof GridLayoutManager) {
                    return captureByRecyclerViewGridLayoutManager(recyclerView, config, verticalSpacing, horizontalSpacing);
                } else if (layoutManager instanceof LinearLayoutManager) {
                    return captureByRecyclerViewLinearLayoutManager(recyclerView, config, verticalSpacing, horizontalSpacing);
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    return captureByRecyclerViewStaggeredGridLayoutManager(recyclerView, config, verticalSpacing, horizontalSpacing);
                }
                throw new Exception(String.format("Not Supported %s LayoutManager", layoutManager.getClass().getSimpleName()));
            } else {
                throw new Exception("Adapter or LayoutManager is Null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过RecyclerView GridLayoutManager绘制为Bitmap
     *
     * @param verticalSpacing   每行之间的间隔
     * @param horizontalSpacing 每列之间的间隔
     */
    private static Bitmap captureByRecyclerViewGridLayoutManager(RecyclerView recyclerView, Bitmap.Config config, int verticalSpacing, int horizontalSpacing) {
        //计算思路
        //竖屏
        //每个Item宽度最大值固定为(RecyclerView宽度-(列数-1)*每列边距)/列数
        //循环保存每行最大高度，并累加每行之间的间隔，用于Bitmap高度，宽度用RecyclerView宽度
        //横屏
        //循环保存每一行宽度以及每一行(横着一行)最大高度，并且累加每行、每列之间的间隔
        //用于Bitmap高度，宽度用(每一行宽度累加值)最大值
        try {
            //获取适配器
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            //Item总条数
            int itemCount = adapter.getItemCount();
            //没数据则直接跳过
            if (itemCount == 0) {
                return null;
            }
            //宽高
            int width = 0, height = 0;
            //View Bitmaps
            Bitmap[] bitmaps = new Bitmap[itemCount];
            //获取布局管理器(判断横竖布局)
            GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            boolean vertical = gridLayoutManager.getOrientation() == RecyclerView.VERTICAL;
            //获取一共多少列
            int spanCount = gridLayoutManager.getSpanCount();
            //获取倍数(行数)
            int lineNumber = getMultiple(itemCount, spanCount);
            if (vertical) {
                //竖向滑动
                //计算总共的宽度(GridView宽度-列分割间距)/spanCount
                int childWidth = (recyclerView.getWidth() - (spanCount - 1) * horizontalSpacing) / spanCount;
                //记录每行最大高度
                int[] rowHeightArrays = new int[lineNumber];
                //临时高度(保存行中最高的列高度)
                int tempHeight;
                for (int i = 0; i < lineNumber; i++) {
                    //清空高度
                    tempHeight = 0;
                    //循环列数
                    for (int j = 0; j < spanCount; j++) {
                        //获取对应的索引
                        int position = i * spanCount + j;
                        //小于总数才处理
                        if (position < itemCount) {
                            RecyclerView.ViewHolder holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(position));
                            adapter.onBindViewHolder(holder, position);
                            View childView = holder.itemView;
                            measureView(childView, childWidth, 0);
                            bitmaps[position] = createBitmap(childView, config);
                            int itemHeight = childView.getMeasuredHeight();
                            //保留最大高度
                            tempHeight = Math.max(itemHeight, tempHeight);
                        }
                        //记录高度并累加
                        if (j == spanCount - 1) {
                            height += tempHeight;
                            rowHeightArrays[i] = tempHeight;
                        }
                    }
                }
                //追加子项间分隔符占用的高度
                height += verticalSpacing * (lineNumber - 1);
                width = recyclerView.getMeasuredWidth();
                //创建位图
                Bitmap bitmap = Bitmap.createBitmap(width, height, config);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawColor(mBackgroundColor);
                //累加高度
                int appendHeight = 0;
                for (int i = 0; i < lineNumber; i++) {
                    //获取每行中最高的列高度
                    int rowHeight = rowHeightArrays[i];
                    //循环列数
                    for (int j = 0; j < spanCount; j++) {
                        //获取对应的索引
                        int position = i * spanCount + j;
                        //小于总数才处理
                        if (position < itemCount) {
                            Bitmap bmp = bitmaps[position];
                            //计算边距
                            int left = j * (horizontalSpacing + childWidth);
                            Matrix matrix = new Matrix();
                            matrix.postTranslate(left, appendHeight);
                            //绘制到Bitmap
                            canvas.drawBitmap(bmp, matrix, mPaint);
                            //释放资源
                            bmp.recycle();
                            bmp = null;
                        }
                        //记录高度并累加
                        if (j == spanCount - 1) {
                            appendHeight += rowHeight + verticalSpacing;
                        }
                    }
                }
                return bitmap;
            } else {
                //横向滑动
                //获取行数
                lineNumber = Math.min(spanCount, itemCount);
                //记录每一行宽度
                int[] rowWidthArrays = new int[lineNumber];
                //记录每一行高度
                int[] rowHeightArrays = new int[lineNumber];
                //获取一共多少列
                int numColumns = getMultiple(itemCount, lineNumber);
                //临时高度(保存行中最高的列高度)
                int tempHeight;
                for (int i = 0; i < lineNumber; i++) {
                    //清空高度
                    tempHeight = 0;
                    //循环列数
                    for (int j = 0; j < numColumns; j++) {
                        //获取对应的索引
                        int position = j * lineNumber + i;
                        //小于总数才处理
                        if (position < itemCount) {
                            RecyclerView.ViewHolder holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(position));
                            adapter.onBindViewHolder(holder, position);
                            View childView = holder.itemView;
                            measureView(childView, 0, 0);
                            bitmaps[position] = createBitmap(childView, config);
                            rowWidthArrays[i] += childView.getMeasuredWidth();
                            int itemHeight = childView.getMeasuredHeight();
                            //保留最大高度
                            tempHeight = Math.max(itemHeight, tempHeight);
                        }
                        //最后记录处理
                        if (j == numColumns - 1) {
                            height += tempHeight;
                            width = Math.max(width, rowWidthArrays[i]);
                            rowHeightArrays[i] = tempHeight;
                        }
                    }
                }
                //追加子项间分隔符占用的高、宽
                height += verticalSpacing * (lineNumber - 1);
                width += horizontalSpacing * (numColumns - 1);
                //创建位图
                Bitmap bitmap = Bitmap.createBitmap(width, height, config);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawColor(mBackgroundColor);
                //累加宽、高
                int appendWidth = 0, appendHeight = 0;
                for (int i = 0; i < lineNumber; i++) {
                    //获取每行中最高的列高度
                    int rowHeight = rowHeightArrays[i];
                    //循环列数
                    for (int j = 0; j < numColumns; j++) {
                        //获取对应的索引
                        int position = j * lineNumber + i;
                        //小于总数才处理
                        if (position < itemCount) {
                            Bitmap bmp = bitmaps[position];
                            //计算边距
                            int left = appendWidth + (j * horizontalSpacing);
                            Matrix matrix = new Matrix();
                            matrix.postTranslate(left, appendHeight);
                            //绘制到Bitmap
                            canvas.drawBitmap(bmp, matrix, mPaint);
                            //累加Bitmap宽度
                            appendWidth += bmp.getWidth();
                            //释放资源
                            bmp.recycle();
                            bmp = null;
                        }
                        //记录高度并累加
                        if (j == numColumns - 1) {
                            appendWidth = 0;
                            appendHeight += rowHeight + verticalSpacing;
                        }
                    }
                }
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过RecyclerView LinearLayoutManager绘制为Bitmap
     *
     * @param verticalSpacing   每行之间的间隔
     * @param horizontalSpacing 每列之间的间隔
     */
    private static Bitmap captureByRecyclerViewLinearLayoutManager(RecyclerView recyclerView, Bitmap.Config config, int verticalSpacing, int horizontalSpacing) {
        //计算思路
        //竖屏
        //循环保存每一个ItemView高度，并累加每行之间的间隔,
        //用于Bitmap高度，宽度用RecyclerView宽度
        //横屏
        //循环保存每一个ItemView宽度，并累加每列之间的间隔，且记录最高的列
        //用于Bitmap高度，宽度用累加出来的值
        try {
            //获取适配器
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            //Item总条数
            int itemCount = adapter.getItemCount();
            //没数据则直接跳过
            if (itemCount == 0) {
                return null;
            }
            //宽高
            int width = 0, height = 0;
            //View Bitmaps
            Bitmap[] bitmaps = new Bitmap[itemCount];
            //获取布局管理器(判断横竖布局)
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            boolean vertical = (linearLayoutManager.getOrientation() == RecyclerView.VERTICAL);
            if (vertical) {
                //竖向滑动
                for (int i = 0; i < itemCount; i++) {
                    RecyclerView.ViewHolder holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i));
                    adapter.onBindViewHolder(holder, i);
                    View childView = holder.itemView;
                    measureView(childView, recyclerView.getWidth(), 0);
                    bitmaps[i] = createBitmap(childView, config);
                    height += childView.getMeasuredHeight();
                }
                //追加子项间分隔符占用的高度
                height += verticalSpacing * (itemCount - 1);
                width = recyclerView.getMeasuredWidth();
                //创建位图
                Bitmap bitmap = Bitmap.createBitmap(width, height, config);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawColor(mBackgroundColor);
                //累加高度
                int appendHeight = 0;
                for (int i = 0; i < itemCount; i++) {
                    Bitmap bmp = bitmaps[i];
                    canvas.drawBitmap(bmp, 0, appendHeight, mPaint);
                    appendHeight += bmp.getHeight() + verticalSpacing;
                    //释放资源
                    bmp.recycle();
                    bmp = null;
                }
                return bitmap;
            } else {
                //横向滑动
                //临时高度(保存行中最高的列高度)
                int tempHeight = 0;
                for (int i = 0; i < itemCount; i++) {
                    RecyclerView.ViewHolder holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i));
                    adapter.onBindViewHolder(holder, i);
                    View childView = holder.itemView;
                    measureView(childView, 0, 0);
                    bitmaps[i] = createBitmap(childView, config);
                    width += childView.getMeasuredWidth();
                    int itemHeight = childView.getMeasuredHeight();
                    //保留最大高度
                    tempHeight = Math.max(itemHeight, tempHeight);
                }
                //追加子项间分隔符占用的宽度
                width += (horizontalSpacing * (itemCount - 1));
                height = tempHeight;
                //创建位图
                Bitmap bitmap = Bitmap.createBitmap(width, height, config);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawColor(mBackgroundColor);
                //累加宽度
                int appendWidth = 0;
                for (int i = 0; i < itemCount; i++) {
                    Bitmap bmp = bitmaps[i];
                    canvas.drawBitmap(bmp, appendWidth, 0, mPaint);
                    appendWidth += (bmp.getWidth() + horizontalSpacing);
                    //释放资源
                    bmp.recycle();
                    bmp = null;
                }
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过RecyclerView StaggeredGridLayoutManager绘制为Bitmap
     *
     * @param verticalSpacing   每行之间的间隔
     * @param horizontalSpacing 每列之间的间隔
     */
    private static Bitmap captureByRecyclerViewStaggeredGridLayoutManager(RecyclerView recyclerView, Bitmap.Config config, int verticalSpacing, int horizontalSpacing) {
        //计算思路
        //竖屏
        //每个Item宽度最大值固定为(RecyclerView宽度-(列数-1)*每列边距)/列数
        //循环保存每一个ItemView高度，并创建数组记录每一列待绘制高度，实现瀑布流高度补差
        //并通过该数组(每列待绘制高度数组)获取最大值，用做Bitmap高度，绘制则还是按以上规则高度补差累加
        //横屏
        //循环保存每一个ItemView宽度、高度，并创建数组记录每一列待绘制宽度，实现瀑布流高度补差
        //并通过该数组(每列待绘制宽度数组)获取最大值，用做Bitmap高度，绘制则还是按以上规则宽度补差累加
        try {
            //获取适配器
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            //Item总条数
            int itemCount = adapter.getItemCount();
            //没数据则直接跳过
            if (itemCount == 0) {
                return null;
            }
            //宽高
            int width, height = 0;
            //View Bitmaps
            Bitmap[] bitmaps = new Bitmap[itemCount];
            //获取布局管理器(判断横竖布局)
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            boolean vertical = (staggeredGridLayoutManager.getOrientation() == 1);
            //获取一共多少列
            int spanCount = staggeredGridLayoutManager.getSpanCount();
            //获取倍数(行数)
            int lineNumber = getMultiple(itemCount, spanCount);
            if (vertical) {
                //竖向滑动
                //计算总共的宽度-(GridView宽度-列分割间距)/spanCount
                int childWidth = (recyclerView.getWidth() - (spanCount - 1) * horizontalSpacing) / spanCount;
                //记录每个Item高度
                int[] itemHeightArrays = new int[itemCount];
                for (int i = 0; i < itemCount; i++) {
                    RecyclerView.ViewHolder holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i));
                    adapter.onBindViewHolder(holder, i);
                    View childView = holder.itemView;
                    measureView(childView, childWidth, 0);
                    bitmaps[i] = createBitmap(childView, config);
                    itemHeightArrays[i] = childView.getMeasuredHeight();
                }
                //记录每列Item个数
                int[] columnsItemNumberArrays = new int[spanCount];
                //记录每列总高度
                int[] columnsHeightArrays = new int[spanCount];
                //循环高度，计算绘制位置
                for (int i = 0; i < itemCount; i++) {
                    //获取最小高度索引
                    int minIndex = getMinimumIndex(columnsHeightArrays);
                    //累加高度
                    columnsHeightArrays[minIndex] += itemHeightArrays[i];
                    //累加数量
                    columnsItemNumberArrays[minIndex] += 1;
                }
                //计算高度(追加子项间分隔符占用的高度)
                if (lineNumber >= 2) {
                    //循环追加子项间分隔符占用的高度
                    for (int i = 0; i < spanCount; i++) {
                        columnsHeightArrays[i] += (columnsItemNumberArrays[i] - 1) * verticalSpacing;
                    }
                }
                //获取列最大高度索引
                int columnsHeightMaxIndex = getMaximumIndex(columnsHeightArrays);
                //获取最大高度值
                int maxColumnsHeight = columnsHeightArrays[columnsHeightMaxIndex];
                //使用最大值
                height = maxColumnsHeight;
                width = recyclerView.getMeasuredWidth();
                //清空绘制时累加计算
                columnsHeightArrays = new int[spanCount];
                //创建位图
                Bitmap bitmap = Bitmap.createBitmap(width, height, config);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawColor(mBackgroundColor);
                //循环绘制
                for (int i = 0; i < itemCount; i++) {
                    //获取最小高度索引
                    int minIndex = getMinimumIndex(columnsHeightArrays);
                    //计算边距
                    int left = minIndex * (horizontalSpacing + childWidth);
                    Matrix matrix = new Matrix();
                    matrix.postTranslate(left, columnsHeightArrays[minIndex]);
                    //绘制到Bitmap
                    Bitmap bmp = bitmaps[i];
                    canvas.drawBitmap(bmp, matrix, mPaint);
                    //累加高度
                    columnsHeightArrays[minIndex] += (itemHeightArrays[i] + verticalSpacing);
                    //释放资源
                    bmp.recycle();
                    bmp = null;
                }
                return bitmap;
            } else {
                //横向滑动
                //获取行数
                lineNumber = Math.min(spanCount, itemCount);
                //记录每个Item宽度
                int[] itemWidthArrays = new int[itemCount];
                //记录每个Ite高度
                int[] itemHeightArrays = new int[itemCount];
                for (int i = 0; i < itemCount; i++) {
                    RecyclerView.ViewHolder holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i));
                    adapter.onBindViewHolder(holder, i);
                    View childView = holder.itemView;
                    measureView(childView, 0, 0);
                    bitmaps[i] = createBitmap(childView, config);
                    itemWidthArrays[i] = childView.getMeasuredWidth();
                    itemHeightArrays[i] = childView.getMeasuredHeight();
                }
                //记录每行向上距离
                int[] columnsTopArrays = new int[lineNumber];
                //记录每行 Item 个数
                int[] columnsItemNumberArrays = new int[lineNumber];
                //记录每行总宽度
                int[] columnsWidthArrays = new int[lineNumber];
                //记录每行最大高度
                int[] columnsHeightArrays = new int[lineNumber];
                //循环宽度, 计算绘制位置
                for (int i = 0; i < itemCount; i++) {
                    //获取最小宽度索引
                    int minIndex = getMinimumIndex(columnsWidthArrays);
                    //累加宽度
                    columnsWidthArrays[minIndex] += itemWidthArrays[i];
                    //累加数量
                    columnsItemNumberArrays[minIndex] += 1;
                    //保存每行最大高度
                    columnsHeightArrays[minIndex] = Math.max(itemHeightArrays[i], columnsHeightArrays[minIndex]);
                }
                //循环追加子项间分隔符占用的宽度
                for (int i = 0; i < lineNumber; i++) {
                    if (columnsItemNumberArrays[i] > 1) {
                        columnsWidthArrays[i] += (columnsItemNumberArrays[i] - 1) * horizontalSpacing;
                    }
                    if (i > 0) {
                        columnsTopArrays[i] = height + (i * verticalSpacing);
                    }
                    // 累加每行高度
                    height += columnsHeightArrays[i];
                }
                //获取最大宽值
                int maxColumnsWidth = columnsWidthArrays[getMaximumIndex(columnsWidthArrays)];
                //使用最大值
                height += (lineNumber - 1) * verticalSpacing;
                width = maxColumnsWidth;
                //清空绘制时累加计算
                columnsWidthArrays = new int[lineNumber];
                //创建位图
                Bitmap bitmap = Bitmap.createBitmap(width, height, config);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawColor(mBackgroundColor);
                //循环绘制
                for (int i = 0; i < itemCount; i++) {
                    //获取最小宽度索引
                    int minIndex = getMinimumIndex(columnsWidthArrays);
                    Matrix matrix = new Matrix();
                    matrix.postTranslate(columnsWidthArrays[minIndex], columnsTopArrays[minIndex]);
                    //绘制到Bitmap
                    Bitmap bmp = bitmaps[i];
                    canvas.drawBitmap(bmp, matrix, mPaint);
                    //累加宽度
                    columnsWidthArrays[minIndex] += (itemWidthArrays[i] + horizontalSpacing);
                    //释放资源
                    bmp.recycle();
                    bmp = null;
                }
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean measureView(View view, int specifiedWidth, int specifiedHeight) {
        try {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            //MeasureSpec
            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            //如果大于0
            if (specifiedWidth > 0) {
                widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(specifiedWidth, View.MeasureSpec.EXACTLY);
            }
            //如果大于0
            if (specifiedHeight > 0) {
                heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(specifiedHeight, View.MeasureSpec.EXACTLY);
            }
            //判断是否存在自定义宽高
            if (layoutParams != null) {
                int width = layoutParams.width;
                int height = layoutParams.height;
                if (width > 0 && height > 0) {
                    widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
                    heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
                } else if (width > 0) {
                    widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
                } else if (height > 0) {
                    heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
                }
            }
            view.measure(widthMeasureSpec, heightMeasureSpec);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Bitmap createBitmap(View childView, Bitmap.Config config) {
        Bitmap bitmap = Bitmap.createBitmap(childView.getMeasuredWidth(), childView.getMeasuredHeight(), config);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(mBackgroundColor);
        childView.draw(canvas);
        return bitmap;
    }

    private static int getMultiple(int value, int divisor) {
        if (value <= 0 || divisor <= 0) {
            return 0;
        }
        if (value <= divisor) {
            return 1;
        }
        return (value % divisor == 0) ? (value / divisor) : (value / divisor) + 1;
    }

    private static int getMinimumIndex(int[] data) {
        if (data != null) {
            int len = data.length;
            if (len > 0) {
                int index = 0;
                int temp = data[index];
                for (int i = 1; i < len; i++) {
                    int value = data[i];
                    if (value < temp) {
                        index = i;
                        temp = value;
                    }
                }
                return index;
            }
        }
        return -1;
    }

    private static int getMaximumIndex(int[] data) {
        if (data != null) {
            int len = data.length;
            if (len > 0) {
                int index = 0;
                int temp = data[index];
                for (int i = 1; i < len; i++) {
                    int value = data[i];
                    if (value > temp) {
                        index = i;
                        temp = value;
                    }
                }
                return index;
            }
        }
        return -1;
    }

}
