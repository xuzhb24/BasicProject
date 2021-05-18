package com.android.util.CapturePicture

import android.app.Activity
import android.graphics.*
import android.os.Build
import android.view.View
import android.webkit.WebView
import android.widget.GridView
import android.widget.HorizontalScrollView
import android.widget.ListView
import android.widget.ScrollView
import androidx.annotation.ColorInt
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.util.ScreenUtil
import com.android.util.StatusBar.StatusBarUtil
import kotlin.math.max
import kotlin.math.min

/**
 * Created by xuzhb on 2021/5/13
 * Desc:截图工具类
 */
object CapturePictureUtil {

    private var mBitmapConfig: Bitmap.Config = Bitmap.Config.RGB_565
    private var mBackgroundColor: Int = Color.TRANSPARENT
    private var mPaint: Paint = Paint()

    /**
     * 配置Bitmap Config
     *
     * @param config {@link Bitmap.Config}
     */
    fun setBitmapConfig(config: Bitmap.Config?) {
        if (config == null) {
            return
        }
        mBitmapConfig = config
    }

    /**
     * 设置Canvas背景色
     *
     * @param backgroundColor 背景色
     */
    fun setBackgroundColor(@ColorInt backgroundColor: Int) {
        mBackgroundColor = backgroundColor
    }

    /**
     * 设置画笔
     */
    fun setPaint(paint: Paint?) {
        if (paint == null) {
            return
        }
        mPaint = paint
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     */
    fun captureWithStatusBar(activity: Activity): Bitmap? {
        kotlin.runCatching {
            val view = activity.window.decorView
            view.isDrawingCacheEnabled = true
            //重新创建绘图缓存，此时的背景色是黑色
            view.buildDrawingCache()
            //获取绘图缓存，注意这里得到的只是一个图像的引用
            val cacheBitmap = view.drawingCache ?: return null
            //获取屏幕宽高
            val width = ScreenUtil.getScreenWidth()
            val height = ScreenUtil.getScreenHeight()
            val frame = Rect()
            activity.window.decorView.getWindowVisibleDisplayFrame(frame)
            //创建新的图片
            val bitmap = Bitmap.createBitmap(cacheBitmap, 0, 0, width, height)
            //释放绘图资源所使用的缓存
            view.destroyDrawingCache()
            return bitmap
        }
        return null
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     */
    fun captureWithoutStatusBar(activity: Activity): Bitmap? {
        kotlin.runCatching {
            val view = activity.window.decorView
            view.isDrawingCacheEnabled = true
            //重新创建绘图缓存，此时的背景色是黑色
            view.buildDrawingCache()
            //获取绘图缓存，注意这里得到的只是一个图像的引用
            val cacheBitmap = view.drawingCache ?: return null
            //获取屏幕宽高
            val width = ScreenUtil.getScreenWidth()
            val height = ScreenUtil.getScreenHeight()
            //获取状态栏高度
            val statusBarHeight = StatusBarUtil.getStatusBarHeight(activity.applicationContext)
            val frame = Rect()
            activity.window.decorView.getWindowVisibleDisplayFrame(frame)
            //创建新的图片
            val bitmap = Bitmap.createBitmap(cacheBitmap, 0, statusBarHeight, width, height - statusBarHeight)
            //释放绘图资源所使用的缓存
            view.destroyDrawingCache()
            return bitmap
        }
        return null
    }

    /**
     * 关闭WebView优化，推荐在setContentView前调用
     */
    fun enableSlowWholeDocumentDraw() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw()
        }
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
    fun captureByWebView(
        webView: WebView?,
        maxHeight: Int = Int.MAX_VALUE,
        config: Bitmap.Config? = mBitmapConfig,
        scale: Float = 0f
    ): Bitmap? {
        if (webView != null && config != null) {
            //Android 5.0以上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                kotlin.runCatching {
                    var newScale = scale
                    if (newScale <= 0) {
                        newScale = webView.scale
                    }
                    val width = webView.width
                    var height = (webView.contentHeight * newScale + 0.5).toInt()
                    //重新设置高度
                    height = min(height, maxHeight)
                    //创建位图
                    val bitmap = Bitmap.createBitmap(width, height, config)
                    val canvas = Canvas(bitmap)
                    canvas.drawColor(mBackgroundColor)
                    webView.draw(canvas)
                    return bitmap
                }
            } else {
                kotlin.runCatching {
                    val picture = webView.capturePicture()
                    val width = picture.width
                    var height = picture.height
                    if (width > 0 && height > 0) {
                        //重新设置高度
                        height = min(height, maxHeight)
                        //创建位图
                        val bitmap = Bitmap.createBitmap(width, height, config)
                        val canvas = Canvas(bitmap)
                        canvas.drawColor(mBackgroundColor)
                        webView.draw(canvas)
                        return bitmap
                    }
                }
            }
        }
        return null
    }

    /**
     * 通过View绘制为Bitmap
     */
    fun captureByView(view: View?, config: Bitmap.Config? = mBitmapConfig): Bitmap? {
        if (view == null || config == null) {
            return null
        }
        kotlin.runCatching {
            val bitmap = Bitmap.createBitmap(view.width, view.height, config)
            val canvas = Canvas(bitmap)
            canvas.drawColor(mBackgroundColor)
            view.layout(view.left, view.top, view.right, view.bottom)
            view.draw(canvas)
            return bitmap
        }
        return null
    }

    /**
     * 通过View Cache绘制为Bitmap
     */
    fun captureByViewCache(view: View?): Bitmap? {
        if (view == null) {
            return null
        }
        kotlin.runCatching {
            //清除视图焦点
            view.clearFocus()
            //将视图设为不可点击
            view.isPressed = false
            //获取视图是否可以保存画图缓存
            val willNotCache = view.willNotCacheDrawing()
            view.setWillNotCacheDrawing(false)
            //获取绘制缓存位图的背景颜色
            val color = view.drawingCacheBackgroundColor
            //设置绘图背景颜色
            view.drawingCacheBackgroundColor = 0
            if (color != 0) {  //获取的背景不是黑色的则释放以前的绘图缓存
                view.destroyDrawingCache()  //释放绘图资源所使用的缓存
            }
            //重新创建绘图缓存，此时的背景色是黑色
            view.buildDrawingCache()
            //获取绘图缓存，注意这里得到的只是一个图像的引用
            val cacheBitmap = view.drawingCache ?: return null
            val bitmap = Bitmap.createBitmap(cacheBitmap)
            //释放位图内存
            view.destroyDrawingCache()
            //回滚以前的缓存设置、缓存颜色设置
            view.setWillNotCacheDrawing(willNotCache)
            view.drawingCacheBackgroundColor = color
            return bitmap
        }
        return null
    }

    /**
     * 通过ScrollView绘制为Bitmap，ScrollView容器中不能有诸如ListView、GridView、WebView这样的高度可变的控件
     */
    fun captureByScrollView(scrollView: ScrollView?, config: Bitmap.Config? = mBitmapConfig): Bitmap? {
        if (scrollView == null || config == null) {
            return null
        }
        kotlin.runCatching {
            val view = scrollView.getChildAt(0)
            val width = view.width
            val height = view.height
            val bitmap = Bitmap.createBitmap(width, height, config)
            val canvas = Canvas(bitmap)
            canvas.drawColor(mBackgroundColor)
            scrollView.layout(scrollView.left, scrollView.top, scrollView.right, scrollView.bottom)
            scrollView.draw(canvas)
            return bitmap
        }
        return null
    }

    /**
     * 通过HorizontalScrollView绘制为Bitmap
     */
    fun captureByHorizontalScrollView(scrollView: HorizontalScrollView?, config: Bitmap.Config? = mBitmapConfig): Bitmap? {
        if (scrollView == null || config == null) {
            return null
        }
        kotlin.runCatching {
            val view = scrollView.getChildAt(0)
            val width = view.width
            val height = view.height
            val bitmap = Bitmap.createBitmap(width, height, config)
            val canvas = Canvas(bitmap)
            canvas.drawColor(mBackgroundColor)
            scrollView.layout(scrollView.left, scrollView.top, scrollView.right, scrollView.bottom)
            scrollView.draw(canvas)
            return bitmap
        }
        return null
    }

    /**
     * 通过NestedScrollView绘制为Bitmap
     */
    fun captureByNestedScrollView(scrollView: NestedScrollView?, config: Bitmap.Config? = mBitmapConfig): Bitmap? {
        if (scrollView == null || config == null) {
            return null
        }
        kotlin.runCatching {
            val view = scrollView.getChildAt(0)
            val width = view.width
            val height = view.height
            val bitmap = Bitmap.createBitmap(width, height, config)
            val canvas = Canvas(bitmap)
            canvas.drawColor(mBackgroundColor)
            scrollView.layout(scrollView.left, scrollView.top, scrollView.right, scrollView.bottom)
            scrollView.draw(canvas)
            return bitmap
        }
        return null
    }

    /**
     * 通过ListView绘制为Bitmap
     */
    fun captureByListView(listView: ListView?, config: Bitmap.Config? = mBitmapConfig): Bitmap? {
        if (listView == null || config == null) {
            return null
        }
        kotlin.runCatching {
            val listAdapter = listView.adapter
            //Item总条数
            val itemCount = listAdapter.count
            //没数据则直接跳过
            if (itemCount == 0) {
                return null
            }
            //高度
            var height = 0
            //获取子项间分隔符占用的高度
            val dividerHeight = listView.dividerHeight
            //View Bitmaps
            val bitmaps = arrayOfNulls<Bitmap>(itemCount)
            //循环绘制每个Item并保存Bitmap
            for (i in 0 until itemCount) {
                val childView = listAdapter.getView(i, null, listView)
                measureView(childView, listView.width, 0)
                bitmaps[i] = createBitmap(childView, config)
                height += childView.measuredHeight
            }
            //追加子项间分隔符占用的高度
            height += dividerHeight * (itemCount - 1)
            val width = listView.measuredWidth
            //创建位图
            val bitmap = Bitmap.createBitmap(width, height, config)
            val canvas = Canvas(bitmap)
            canvas.drawColor(mBackgroundColor)
            //累加高度
            var appendHeight = 0f
            var i = 0
            val len = bitmaps.size
            while (i < len) {
                var bmp = bitmaps[i]
                bmp?.let {
                    canvas.drawBitmap(it, 0f, appendHeight, mPaint)
                    appendHeight += it.height + dividerHeight
                    //释放资源
                    it.recycle()
                }
                bmp = null
                i++
            }
            return bitmap
        }
        return null
    }

    /**
     * 通过GridView绘制为Bitmap
     */
    fun captureByGridView(gridView: GridView?, config: Bitmap.Config? = mBitmapConfig): Bitmap? {
        if (gridView == null || config == null) {
            return null
        }
        kotlin.runCatching {
            val listAdapter = gridView.adapter
            //Item总条数
            val itemCount = listAdapter.count
            //没数据则直接跳过
            if (itemCount == 0) {
                return null
            }
            //高度
            var height = 0
            //获取一共多少列
            val numColumns = gridView.numColumns
            //每列之间的间隔
            val horizontalSpacing = gridView.horizontalSpacing
            //每行之间的间隔
            val verticalSpacing = gridView.verticalSpacing
            //View Bitmaps
            val bitmaps = arrayOfNulls<Bitmap>(itemCount)
            //获取倍数(行数)
            val lineNumber = getMultiple(itemCount, numColumns)
            //计算总共的宽度(GridView宽度-列分割间距)/numColumns
            val childWidth = (gridView.width - (numColumns - 1) * horizontalSpacing) / numColumns
            //记录每行最大高度
            val rowHeightArrays = IntArray(lineNumber)
            //临时高度(保存行中最高的列高度)
            var tempHeight: Int
            //循环每一行绘制每个Item并保存Bitmap
            for (i in 0 until lineNumber) {
                //清空高度
                tempHeight = 0
                //循环列数
                for (j in 0 until numColumns) {
                    //获取对应的索引
                    val position = i * numColumns + j
                    //小于总数才处理
                    if (position < itemCount) {
                        val childView = listAdapter.getView(position, null, gridView)
                        measureView(childView, childWidth, 0)
                        bitmaps[position] = createBitmap(childView, config)
                        val itemHeight = childView.measuredHeight
                        //保留最大高度
                        tempHeight = max(itemHeight, tempHeight)
                    }
                    //记录高度并累加
                    if (j == numColumns - 1) {
                        height += tempHeight
                        rowHeightArrays[i] = tempHeight
                    }
                }
            }
            //追加子项间分隔符占用的高度
            height += verticalSpacing * (lineNumber - 1)
            val width = gridView.measuredWidth
            //创建位图
            val bitmap = Bitmap.createBitmap(width, height, config)
            val canvas = Canvas(bitmap)
            canvas.drawColor(mBackgroundColor)
            //累加高度
            var appendHeight = 0f
            //循环每一行绘制每个Item Bitmap
            for (i in 0 until lineNumber) {
                //获取每一行最长列的高度
                val itemHeight = rowHeightArrays[i]
                //循环列数
                for (j in 0 until numColumns) {
                    //获取对应的索引
                    val position = i * numColumns + j
                    //小于总数才处理
                    if (position < itemCount) {
                        var bmp = bitmaps[position]
                        //计算边距
                        val left = j * (horizontalSpacing + childWidth).toFloat()
                        val matrix = Matrix()
                        matrix.postTranslate(left, appendHeight)
                        //绘制到Bitmap
                        bmp?.let {
                            canvas.drawBitmap(it, matrix, mPaint)
                            //释放资源
                            it.recycle()
                        }
                        bmp = null
                    }
                    //记录高度并累加
                    if (j == numColumns - 1) {
                        appendHeight += itemHeight + verticalSpacing
                    }
                }
            }
            return bitmap
        }
        return null
    }

    /**
     * 通过RecyclerView绘制为Bitmap，不支持含ItemDecoration截图
     *
     * @param verticalSpacing   每行之间的间隔
     * @param horizontalSpacing 每列之间的间隔
     */
    fun captureByRecyclerView(
        recyclerView: RecyclerView?,
        config: Bitmap.Config? = mBitmapConfig,
        verticalSpacing: Int = 0,
        horizontalSpacing: Int = 0
    ): Bitmap? {
        if (recyclerView == null || config == null) {
            return null
        }
        kotlin.runCatching {
            //获取适配器
            val adapter = recyclerView.adapter
            //获取布局管理器
            val layoutManager = recyclerView.layoutManager
            if (layoutManager != null && adapter != null) {
                //判断布局类型
                when (layoutManager) {
                    is GridLayoutManager -> {
                        return captureByRecyclerViewGridLayoutManager(recyclerView, config, verticalSpacing, horizontalSpacing)
                    }
                    is LinearLayoutManager -> {
                        return captureByRecyclerViewLinearLayoutManager(recyclerView, config, verticalSpacing, horizontalSpacing)
                    }
                    is StaggeredGridLayoutManager -> {
                        return captureByRecyclerViewStaggeredGridLayoutManager(
                            recyclerView, config, verticalSpacing, horizontalSpacing
                        )
                    }
                    else -> {
                        throw Exception("Not Supported ${layoutManager.javaClass.simpleName} LayoutManager")
                    }
                }
            } else {
                throw Exception("Adapter or LayoutManager is Null")
            }
        }
        return null
    }

    /**
     * 通过RecyclerView GridLayoutManager绘制为Bitmap
     *
     * @param verticalSpacing   每行之间的间隔
     * @param horizontalSpacing 每列之间的间隔
     */
    private fun captureByRecyclerViewGridLayoutManager(
        recyclerView: RecyclerView,
        config: Bitmap.Config,
        verticalSpacing: Int,
        horizontalSpacing: Int
    ): Bitmap? {
        //计算思路
        //竖屏
        //每个Item宽度最大值固定为(RecyclerView宽度-(列数-1)*每列边距)/列数
        //循环保存每行最大高度，并累加每行之间的间隔，用于Bitmap高度，宽度用RecyclerView宽度
        //横屏
        //循环保存每一行宽度以及每一行(横着一行)最大高度，并且累加每行、每列之间的间隔
        //用于Bitmap高度，宽度用(每一行宽度累加值)最大值
        kotlin.runCatching {
            //获取适配器
            val adapter = recyclerView.adapter
            //Item总条数
            val itemCount = adapter!!.itemCount
            //没数据则直接跳过
            if (itemCount == 0) {
                return null
            }
            //宽高
            var width = 0
            var height = 0
            //View Bitmaps
            val bitmaps = arrayOfNulls<Bitmap>(itemCount)
            //获取布局管理器(判断横竖布局)
            val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager
            val vertical = gridLayoutManager.orientation == RecyclerView.VERTICAL
            //获取一共多少列
            val spanCount = gridLayoutManager.spanCount
            //获取倍数(行数)
            var lineNumber = getMultiple(itemCount, spanCount)
            if (vertical) {
                //竖向滑动
                //计算总共的宽度(GridView宽度-列分割间距)/spanCount
                val childWidth = (recyclerView.width - (spanCount - 1) * horizontalSpacing) / spanCount
                //记录每行最大高度
                val rowHeightArrays = IntArray(lineNumber)
                //临时高度(保存行中最高的列高度)
                var tempHeight: Int
                for (i in 0 until lineNumber) {
                    //清空高度
                    tempHeight = 0
                    //循环列数
                    for (j in 0 until spanCount) {
                        //获取对应的索引
                        val position = i * spanCount + j
                        //小于总数才处理
                        if (position < itemCount) {
                            val holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(position))
                            adapter.onBindViewHolder(holder, position)
                            val childView = holder.itemView
                            measureView(childView, childWidth, 0)
                            bitmaps[position] = createBitmap(childView, config)
                            val itemHeight = childView.measuredHeight
                            //保留最大高度
                            tempHeight = max(itemHeight, tempHeight)
                        }
                        //记录高度并累加
                        if (j == spanCount - 1) {
                            height += tempHeight
                            rowHeightArrays[i] = tempHeight
                        }
                    }
                }
                //追加子项间分隔符占用的高度
                height += verticalSpacing * (lineNumber - 1)
                width = recyclerView.measuredWidth
                //创建位图
                val bitmap = Bitmap.createBitmap(width, height, config)
                val canvas = Canvas(bitmap)
                canvas.drawColor(mBackgroundColor)
                //累加高度
                var appendHeight = 0f
                for (i in 0 until lineNumber) {
                    //获取每行中最高的列高度
                    val rowHeight = rowHeightArrays[i]
                    //循环列数
                    for (j in 0 until spanCount) {
                        //获取对应的索引
                        val position = i * spanCount + j
                        //小于总数才处理
                        if (position < itemCount) {
                            var bmp = bitmaps[position]
                            //计算边距
                            val left = j * (horizontalSpacing + childWidth).toFloat()
                            val matrix = Matrix()
                            matrix.postTranslate(left, appendHeight)
                            bmp?.let {
                                //绘制到Bitmap
                                canvas.drawBitmap(it, matrix, mPaint)
                                //释放资源
                                it.recycle()
                            }
                            bmp = null
                        }
                        //记录高度并累加
                        if (j == spanCount - 1) {
                            appendHeight += rowHeight + verticalSpacing
                        }
                    }
                }
                return bitmap
            } else {
                //横向滑动
                //获取行数
                lineNumber = min(spanCount, itemCount)
                //记录每一行宽度
                val rowWidthArrays = IntArray(lineNumber)
                //记录每一行高度
                val rowHeightArrays = IntArray(lineNumber)
                //获取一共多少列
                val numColumns = getMultiple(itemCount, lineNumber)
                //临时高度(保存行中最高的列高度)
                var tempHeight: Int
                for (i in 0 until lineNumber) {
                    //清空高度
                    tempHeight = 0
                    //循环列数
                    for (j in 0 until numColumns) {
                        //获取对应的索引
                        val position = j * lineNumber + i
                        //小于总数才处理
                        if (position < itemCount) {
                            val holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(position))
                            adapter.onBindViewHolder(holder, position)
                            val childView = holder.itemView
                            measureView(childView, 0, 0)
                            bitmaps[position] = createBitmap(childView, config)
                            rowWidthArrays[i] += childView.measuredWidth
                            val itemHeight = childView.measuredHeight
                            //保留最大高度
                            tempHeight = max(itemHeight, tempHeight)
                        }
                        //最后记录处理
                        if (j == numColumns - 1) {
                            height += tempHeight
                            width = max(width, rowWidthArrays[i])
                            rowHeightArrays[i] = tempHeight
                        }
                    }
                }
                //追加子项间分隔符占用的高、宽
                height += verticalSpacing * (lineNumber - 1)
                width += horizontalSpacing * (numColumns - 1)
                //创建位图
                val bitmap = Bitmap.createBitmap(width, height, config)
                val canvas = Canvas(bitmap)
                canvas.drawColor(mBackgroundColor)
                //累加宽、高
                var appendWidth = 0f
                var appendHeight = 0f
                for (i in 0 until lineNumber) {
                    //获取每行中最高的列高度
                    val rowHeight = rowHeightArrays[i]
                    //循环列数
                    for (j in 0 until numColumns) {
                        //获取对应的索引
                        val position = j * lineNumber + i
                        //小于总数才处理
                        if (position < itemCount) {
                            var bmp = bitmaps[position]
                            //计算边距
                            val left = appendWidth + (j * horizontalSpacing).toFloat()
                            val matrix = Matrix()
                            matrix.postTranslate(left, appendHeight)
                            bmp?.let {
                                //绘制到Bitmap
                                canvas.drawBitmap(it, matrix, mPaint)
                                //累加Bitmap宽度
                                appendWidth += it.width
                                //释放资源
                                it.recycle()
                            }
                            bmp = null
                        }
                        //记录高度并累加
                        if (j == numColumns - 1) {
                            appendWidth = 0f
                            appendHeight += rowHeight + verticalSpacing
                        }
                    }
                }
                return bitmap
            }
        }
        return null
    }

    /**
     * 通过RecyclerView LinearLayoutManager绘制为Bitmap
     *
     * @param verticalSpacing   每行之间的间隔
     * @param horizontalSpacing 每列之间的间隔
     */
    private fun captureByRecyclerViewLinearLayoutManager(
        recyclerView: RecyclerView,
        config: Bitmap.Config,
        verticalSpacing: Int,
        horizontalSpacing: Int
    ): Bitmap? {
        //计算思路
        //竖屏
        //循环保存每一个ItemView高度，并累加每行之间的间隔,
        //用于Bitmap高度，宽度用RecyclerView宽度
        //横屏
        //循环保存每一个ItemView宽度，并累加每列之间的间隔，且记录最高的列
        //用于Bitmap高度，宽度用累加出来的值
        kotlin.runCatching {
            //获取适配器
            val adapter = recyclerView.adapter
            //Item总条数
            val itemCount = adapter!!.itemCount
            //没数据则直接跳过
            if (itemCount == 0) {
                return null
            }
            //宽高
            var width = 0
            var height = 0
            //View Bitmaps
            val bitmaps = arrayOfNulls<Bitmap>(itemCount)
            //获取布局管理器(判断横竖布局)
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            val vertical = linearLayoutManager.orientation == RecyclerView.VERTICAL
            if (vertical) {
                //竖向滑动
                for (i in 0 until itemCount) {
                    val holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i))
                    adapter.onBindViewHolder(holder, i)
                    val childView = holder.itemView
                    measureView(childView, recyclerView.width, 0)
                    bitmaps[i] = createBitmap(childView, config)
                    height += childView.measuredHeight
                }
                //追加子项间分隔符占用的高度
                height += verticalSpacing * (itemCount - 1)
                width = recyclerView.measuredWidth
                //创建位图
                val bitmap = Bitmap.createBitmap(width, height, config)
                val canvas = Canvas(bitmap)
                canvas.drawColor(mBackgroundColor)
                //累加高度
                var appendHeight = 0f
                for (i in 0 until itemCount) {
                    var bmp = bitmaps[i]
                    bmp?.let {
                        canvas.drawBitmap(it, 0f, appendHeight, mPaint)
                        appendHeight += it.height + verticalSpacing
                        //释放资源
                        it.recycle()
                    }
                    bmp = null
                }
                return bitmap
            } else {
                //横向滑动
                //临时高度(保存行中最高的列高度)
                var tempHeight = 0
                for (i in 0 until itemCount) {
                    val holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i))
                    adapter.onBindViewHolder(holder, i)
                    val childView = holder.itemView
                    measureView(childView, 0, 0)
                    bitmaps[i] = createBitmap(childView, config)
                    width += childView.measuredWidth
                    val itemHeight = childView.measuredHeight
                    //保留最大高度
                    tempHeight = max(itemHeight, tempHeight)
                }
                //追加子项间分隔符占用的宽度
                width += horizontalSpacing * (itemCount - 1)
                height = tempHeight
                //创建位图
                val bitmap = Bitmap.createBitmap(width, height, config)
                val canvas = Canvas(bitmap)
                canvas.drawColor(mBackgroundColor)
                //累加宽度
                var appendWidth = 0
                for (i in 0 until itemCount) {
                    var bmp = bitmaps[i]
                    bmp?.let {
                        canvas.drawBitmap(it, appendWidth.toFloat(), 0f, mPaint)
                        appendWidth += it.width + horizontalSpacing
                        //释放资源
                        it.recycle()
                    }
                    bmp = null
                }
                return bitmap
            }
        }
        return null
    }

    /**
     * 通过RecyclerView StaggeredGridLayoutManager绘制为Bitmap
     *
     * @param verticalSpacing   每行之间的间隔
     * @param horizontalSpacing 每列之间的间隔
     */
    private fun captureByRecyclerViewStaggeredGridLayoutManager(
        recyclerView: RecyclerView,
        config: Bitmap.Config,
        verticalSpacing: Int,
        horizontalSpacing: Int
    ): Bitmap? {
        //计算思路
        //竖屏
        //每个Item宽度最大值固定为(RecyclerView宽度-(列数-1)*每列边距)/列数
        //循环保存每一个ItemView高度，并创建数组记录每一列待绘制高度，实现瀑布流高度补差
        //并通过该数组(每列待绘制高度数组)获取最大值，用做Bitmap高度，绘制则还是按以上规则高度补差累加
        //横屏
        //循环保存每一个ItemView宽度、高度，并创建数组记录每一列待绘制宽度，实现瀑布流高度补差
        //并通过该数组(每列待绘制宽度数组)获取最大值，用做Bitmap高度，绘制则还是按以上规则宽度补差累加
        kotlin.runCatching {
            //获取适配器
            val adapter = recyclerView.adapter
            //Item总条数
            val itemCount = adapter!!.itemCount
            //没数据则直接跳过
            if (itemCount == 0) {
                return null
            }
            //宽高
            var width = 0
            var height = 0
            //View Bitmaps
            val bitmaps = arrayOfNulls<Bitmap>(itemCount)
            //获取布局管理器(判断横竖布局)
            val staggeredGridLayoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
            val vertical = staggeredGridLayoutManager.orientation == 1
            //获取一共多少列
            val spanCount = staggeredGridLayoutManager.spanCount
            //获取倍数(行数)
            var lineNumber = getMultiple(itemCount, spanCount)
            if (vertical) {
                //竖向滑动
                //计算总共的宽度-(GridView宽度-列分割间距)/spanCount
                val childWidth = (recyclerView.width - (spanCount - 1) * horizontalSpacing) / spanCount
                //记录每个Item高度
                val itemHeightArrays = IntArray(itemCount)
                for (i in 0 until itemCount) {
                    val holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i))
                    adapter.onBindViewHolder(holder, i)
                    val childView = holder.itemView
                    measureView(childView, childWidth, 0)
                    bitmaps[i] = createBitmap(childView, config)
                    itemHeightArrays[i] = childView.measuredHeight
                }
                //记录每列Item个数
                val columnsItemNumberArrays = IntArray(spanCount)
                //记录每列总高度
                var columnsHeightArrays = IntArray(spanCount)
                //循环高度，计算绘制位置
                for (i in 0 until itemCount) {
                    //获取最小高度索引
                    val minIndex = getMinimumIndex(columnsHeightArrays)
                    //累加高度
                    columnsHeightArrays[minIndex] += itemHeightArrays[i]
                    //累加数量
                    columnsItemNumberArrays[minIndex] += 1
                }
                //计算高度(追加子项间分隔符占用的高度)
                if (lineNumber >= 2) {
                    //循环追加子项间分隔符占用的高度
                    for (i in 0 until spanCount) {
                        columnsHeightArrays[i] += (columnsItemNumberArrays[i] - 1) * verticalSpacing
                    }
                }
                //获取列最大高度索引
                val columnsHeightMaxIndex = getMaximumIndex(columnsHeightArrays)
                //获取最大高度值
                val maxColumnsHeight = columnsHeightArrays[columnsHeightMaxIndex]
                //使用最大值
                height = maxColumnsHeight
                width = recyclerView.measuredWidth
                //清空绘制时累加计算
                columnsHeightArrays = IntArray(spanCount)
                //创建位图
                val bitmap = Bitmap.createBitmap(width, height, config)
                val canvas = Canvas(bitmap)
                canvas.drawColor(mBackgroundColor)
                //循环绘制
                for (i in 0 until itemCount) {
                    //获取最小高度索引
                    val minIndex = getMinimumIndex(columnsHeightArrays)
                    //计算边距
                    val left = minIndex * (horizontalSpacing + childWidth).toFloat()
                    val matrix = Matrix()
                    matrix.postTranslate(left.toFloat(), columnsHeightArrays[minIndex].toFloat())
                    //绘制到Bitmap
                    var bmp = bitmaps[i]
                    bmp?.let {
                        canvas.drawBitmap(it, matrix, mPaint)
                        //累加高度
                        columnsHeightArrays[minIndex] += itemHeightArrays[i] + verticalSpacing
                        //释放资源
                        it.recycle()
                    }
                    bmp = null
                }
                return bitmap
            } else {
                //横向滑动
                //获取行数
                lineNumber = Math.min(spanCount, itemCount)
                //记录每个Item宽度
                val itemWidthArrays = IntArray(itemCount)
                //记录每个Ite高度
                val itemHeightArrays = IntArray(itemCount)
                for (i in 0 until itemCount) {
                    val holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i))
                    adapter.onBindViewHolder(holder, i)
                    val childView = holder.itemView
                    measureView(childView, 0, 0)
                    bitmaps[i] = createBitmap(childView, config)
                    itemWidthArrays[i] = childView.measuredWidth
                    itemHeightArrays[i] = childView.measuredHeight
                }
                //记录每行向上距离
                val columnsTopArrays = IntArray(lineNumber)
                //记录每行 Item 个数
                val columnsItemNumberArrays = IntArray(lineNumber)
                //记录每行总宽度
                var columnsWidthArrays = IntArray(lineNumber)
                //记录每行最大高度
                val columnsHeightArrays = IntArray(lineNumber)
                //循环宽度, 计算绘制位置
                for (i in 0 until itemCount) {
                    //获取最小宽度索引
                    val minIndex = getMinimumIndex(columnsWidthArrays)
                    //累加宽度
                    columnsWidthArrays[minIndex] += itemWidthArrays[i]
                    //累加数量
                    columnsItemNumberArrays[minIndex] += 1
                    //保存每行最大高度
                    columnsHeightArrays[minIndex] = max(itemHeightArrays[i], columnsHeightArrays[minIndex])
                }
                //循环追加子项间分隔符占用的宽度
                for (i in 0 until lineNumber) {
                    if (columnsItemNumberArrays[i] > 1) {
                        columnsWidthArrays[i] += (columnsItemNumberArrays[i] - 1) * horizontalSpacing
                    }
                    if (i > 0) {
                        columnsTopArrays[i] = height + i * verticalSpacing
                    }
                    // 累加每行高度
                    height += columnsHeightArrays[i]
                }
                //获取最大宽值
                val maxColumnsWidth =
                    columnsWidthArrays[getMaximumIndex(columnsWidthArrays)]
                //使用最大值
                height += (lineNumber - 1) * verticalSpacing
                width = maxColumnsWidth
                //清空绘制时累加计算
                columnsWidthArrays = IntArray(lineNumber)
                //创建位图
                val bitmap = Bitmap.createBitmap(width, height, config)
                val canvas = Canvas(bitmap)
                canvas.drawColor(mBackgroundColor)
                //循环绘制
                for (i in 0 until itemCount) {
                    //获取最小宽度索引
                    val minIndex = getMinimumIndex(columnsWidthArrays)
                    val matrix = Matrix()
                    matrix.postTranslate(columnsWidthArrays[minIndex].toFloat(), columnsTopArrays[minIndex].toFloat())
                    //绘制到Bitmap
                    var bmp = bitmaps[i]
                    bmp?.let {
                        canvas.drawBitmap(it, matrix, mPaint)
                        //累加宽度
                        columnsWidthArrays[minIndex] += itemWidthArrays[i] + horizontalSpacing
                        //释放资源
                        it.recycle()
                    }
                    bmp = null
                }
                return bitmap
            }
        }
        return null
    }

    private fun measureView(view: View, specifiedWidth: Int, specifiedHeight: Int): Boolean {
        kotlin.runCatching {
            val layoutParams = view.layoutParams
            //MeasureSpec
            var widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            var heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            //如果大于0
            if (specifiedWidth > 0) {
                widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(specifiedWidth, View.MeasureSpec.EXACTLY)
            }
            //如果大于0
            if (specifiedHeight > 0) {
                heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(specifiedHeight, View.MeasureSpec.EXACTLY)
            }
            //判断是否存在自定义宽高
            if (layoutParams != null) {
                val width = layoutParams.width
                val height = layoutParams.height
                if (width > 0 && height > 0) {
                    widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
                    heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
                } else if (width > 0) {
                    widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
                } else if (height > 0) {
                    heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
                }
            }
            view.measure(widthMeasureSpec, heightMeasureSpec)
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)
            return true
        }
        return false
    }

    private fun createBitmap(childView: View, config: Bitmap.Config): Bitmap {
        val bitmap = Bitmap.createBitmap(childView.measuredWidth, childView.measuredHeight, config)
        val canvas = Canvas(bitmap)
        canvas.drawColor(mBackgroundColor)
        childView.draw(canvas)
        return bitmap
    }

    private fun getMultiple(value: Int, divisor: Int): Int {
        if (value <= 0 || divisor <= 0) {
            return 0
        }
        if (value <= divisor) {
            return 1
        }
        return if (value % divisor == 0) value / divisor else (value / divisor) + 1
    }

    private fun getMinimumIndex(data: IntArray?): Int {
        if (data != null) {
            val len = data.size
            if (len > 0) {
                var index = 0
                var temp = data[index]
                for (i in 1 until len) {
                    val value = data[i]
                    if (value < temp) {
                        index = i
                        temp = value
                    }
                }
                return index
            }
        }
        return -1
    }

    private fun getMaximumIndex(data: IntArray?): Int {
        if (data != null) {
            val len = data.size
            if (len > 0) {
                var index = 0
                var temp = data[index]
                for (i in 1 until len) {
                    val value = data[i]
                    if (value > temp) {
                        index = i
                        temp = value
                    }
                }
                return index
            }
        }
        return -1
    }

}