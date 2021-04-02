package com.android.widget.PhotoViewer.widget

import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.ImageView
import android.widget.OverScroller
import com.android.widget.PhotoViewer.helper.CustomGestureDetector
import com.android.widget.PhotoViewer.listener.*
import kotlin.math.*

/**
 * Created by xuzhb on 2021/3/30
 * Desc:辅助PhotoView进行缩放、拖曳等操作，
 */
class PhotoViewAttacher constructor(private val mImageView: ImageView) : View.OnTouchListener, View.OnLayoutChangeListener {

    companion object {
        private const val DEFAULT_MAX_SCALE = 3.0f
        private const val DEFAULT_MID_SCALE = 1.75f
        private const val DEFAULT_MIN_SCALE = 1.0f
        private const val DEFAULT_ZOOM_DURATION = 200
        private const val HORIZONTAL_EDGE_NONE = -1
        private const val HORIZONTAL_EDGE_LEFT = 0
        private const val HORIZONTAL_EDGE_RIGHT = 1
        private const val HORIZONTAL_EDGE_BOTH = 2
        private const val VERTICAL_EDGE_NONE = -1
        private const val VERTICAL_EDGE_TOP = 0
        private const val VERTICAL_EDGE_BOTTOM = 1
        private const val VERTICAL_EDGE_BOTH = 2
        private const val SINGLE_TOUCH = 1
    }

    private var mZoomDuration: Int = DEFAULT_ZOOM_DURATION
    private var mMinScale: Float = DEFAULT_MIN_SCALE
    private var mMidScale: Float = DEFAULT_MID_SCALE
    private var mMaxScale: Float = DEFAULT_MAX_SCALE
    private var mAllowParentInterceptOnEdge: Boolean = true
    private var mBlockParentIntercept: Boolean = false

    private var mInterpolator: Interpolator = AccelerateDecelerateInterpolator()
    private var mGestureDetector: GestureDetector? = null
    private var mScaleDragDetector: CustomGestureDetector? = null

    private val mBaseMatrix: Matrix = Matrix()
    private val mDrawMatrix: Matrix = Matrix()
    private val mSuppMatrix: Matrix = Matrix()
    private val mDisplayRect: RectF = RectF()
    private val mMatrixValues: FloatArray = FloatArray(9)

    private var mOnMatrixChangeListener: OnMatrixChangedListener? = null
    private var mOnOutsidePhotoTapListener: OnOutsidePhotoTapListener? = null
    private var mOnPhotoTapListener: OnPhotoTapListener? = null
    private var mOnScaleChangeListener: OnScaleChangedListener? = null
    private var mOnSingleFlingListener: OnSingleFlingListener? = null
    private var mOnViewDragListener: OnViewDragListener? = null
    private var mOnViewTapListener: OnViewTapListener? = null
    private var mOnClickListener: View.OnClickListener? = null
    private var mOnLongClickListener: View.OnLongClickListener? = null

    private var mCurrentFlingRunnable: FlingRunnable? = null
    private var mHorizontalScrollEdge: Int = HORIZONTAL_EDGE_BOTH
    private var mVerticalScrollEdge: Int = VERTICAL_EDGE_BOTH
    private var mBaseRotation: Float = 0f

    private var mZoomEnabled: Boolean = true
    private var mScaleType: ImageView.ScaleType = ImageView.ScaleType.FIT_CENTER

    private val mOnGestureListener = object : OnGestureListener {
        override fun onDrag(dx: Float, dy: Float) {
            if (mScaleDragDetector != null && mScaleDragDetector!!.isScaling()) {
                return
            }
            mOnViewDragListener?.onDrag(dx, dy)
            mSuppMatrix.postTranslate(dx, dy)
            checkAndDisplayMatrix()
            //是否让父容器拦截点击事件
            val parent = mImageView.parent
            if (mAllowParentInterceptOnEdge && !mScaleDragDetector!!.isScaling()) {
                if (mHorizontalScrollEdge == HORIZONTAL_EDGE_BOTH
                    || (mHorizontalScrollEdge == HORIZONTAL_EDGE_LEFT && dx >= 1f)
                    || mHorizontalScrollEdge == HORIZONTAL_EDGE_RIGHT && dx <= -1f
                    || mVerticalScrollEdge == VERTICAL_EDGE_TOP && dy >= 1f
                    || mVerticalScrollEdge == VERTICAL_EDGE_BOTTOM && dy <= -1f
                ) {
                    parent?.requestDisallowInterceptTouchEvent(false)
                }
            } else {
                parent?.requestDisallowInterceptTouchEvent(true)
            }
        }

        override fun onFling(startX: Float, startY: Float, velocityX: Float, velocityY: Float) {
            mCurrentFlingRunnable = FlingRunnable(mImageView.context)
            mCurrentFlingRunnable?.fling(
                getImageViewWidth(mImageView), getImageViewHeight(mImageView),
                velocityX.toInt(), velocityY.toInt()
            )
            mImageView.post(mCurrentFlingRunnable)
        }

        override fun onScale(scaleFactor: Float, focusX: Float, focusY: Float) {
            if (getScale() < mMaxScale || scaleFactor < 1f) {
                mOnScaleChangeListener?.onScaleChange(scaleFactor, focusX, focusY)
                mSuppMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY)
                checkAndDisplayMatrix()
            }
        }
    }

    init {
        mImageView.setOnTouchListener(this)
        mImageView.addOnLayoutChangeListener(this)
        if (!mImageView.isInEditMode) {
            mBaseRotation = 0.0f
            mScaleDragDetector = CustomGestureDetector(mImageView.context, mOnGestureListener)
            mGestureDetector = GestureDetector(mImageView.context, object : GestureDetector.SimpleOnGestureListener() {

                override fun onLongPress(e: MotionEvent?) {
                    mOnLongClickListener?.onLongClick(mImageView)
                }

                override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                    mOnSingleFlingListener?.let {
                        if (getScale() > DEFAULT_MIN_SCALE) {
                            return false
                        }
                        if (e1 == null || e2 == null) {
                            return false
                        }
                        if (e1.pointerCount > SINGLE_TOUCH || e2.pointerCount > SINGLE_TOUCH) {
                            return false
                        }
                        return it.onFling(e1, e2, velocityX, velocityY)
                    }
                    return false
                }
            })
            mGestureDetector?.setOnDoubleTapListener(object : GestureDetector.OnDoubleTapListener {
                override fun onDoubleTap(e: MotionEvent?): Boolean {
                    if (e == null) {
                        return true
                    }
                    try {
                        val scale = getScale()
                        val x = e.x
                        val y = e.y
                        if (scale < getMediumScale()) {
                            setScale(getMediumScale(), x, y, true)
                        } else if (scale >= getMediumScale() && scale < getMaximumScale()) {
                            setScale(getMaximumScale(), x, y, true)
                        } else {
                            setScale(getMinimumScale(), x, y, true)
                        }
                    } catch (e: Exception) {
                        // Can sometimes happen when getX() and getY() is called
                    }
                    return true
                }

                override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
                    // Wait for the confirmed onDoubleTap() instead
                    return false
                }

                override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                    if (e == null) {
                        return false
                    }
                    mOnClickListener?.onClick(mImageView)
                    val x = e.x
                    val y = e.y
                    mOnViewTapListener?.onViewTap(mImageView, x, y)
                    getDisplayRect()?.let {
                        if (it.contains(x, y)) {
                            val xResult = (x - it.left) / it.width()
                            val yResult = (y - it.top) / it.height()
                            mOnPhotoTapListener?.onPhotoTap(mImageView, x, y, xResult, yResult)
                            return true
                        } else {
                            mOnOutsidePhotoTapListener?.onOutsidePhotoTap(mImageView)
                        }
                    }
                    return false
                }
            })
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        var handled = false
        if (mZoomEnabled && event != null && v != null && (v as ImageView).drawable != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    //禁止父容器拦截触摸事件
                    v.parent?.requestDisallowInterceptTouchEvent(true)
                    cancelFling()
                }
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                    if (getScale() < mMinScale) {  //如果缩放比例小于最小比例，缩放回最小比例
                        getDisplayRect()?.let {
                            v.post(AnimatedZoomRunnable(getScale(), mMinScale, it.centerX(), it.centerY()))
                            handled = true
                        }
                    } else if (getScale() > mMaxScale) {  //如果缩放比例小于最大比例，缩放回最大比例
                        getDisplayRect()?.let {
                            v.post(AnimatedZoomRunnable(getScale(), mMaxScale, it.centerX(), it.centerY()))
                            handled = true
                        }
                    }
                }
            }
            mScaleDragDetector?.let {
                val wasScaling = it.isScaling()
                val wasDragging = it.isDragging()
                handled = it.onTouchEvent(event)
                val didntScale = !wasScaling && !it.isScaling()
                val didntDrag = !wasDragging && !it.isDragging()
                mBlockParentIntercept = didntScale && didntDrag
            }
            if (mGestureDetector != null && mGestureDetector!!.onTouchEvent(event)) {
                handled = true
            }
        }
        return handled
    }

    override fun onLayoutChange(
        v: View?,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        oldLeft: Int,
        oldTop: Int,
        oldRight: Int,
        oldBottom: Int
    ) {
        //更新Matrix信息
        if (left != oldLeft || top != oldTop || right != oldRight || bottom != oldBottom) {
            updateBaseMatrix(mImageView.drawable)
        }
    }

    fun setOnMatrixChangeListener(listener: OnMatrixChangedListener) {
        mOnMatrixChangeListener = listener
    }

    fun setOnOutsidePhotoTapListener(listener: OnOutsidePhotoTapListener) {
        mOnOutsidePhotoTapListener = listener
    }

    fun setOnPhotoTapListener(listener: OnPhotoTapListener) {
        mOnPhotoTapListener = listener
    }

    fun setOnScaleChangeListener(listener: OnScaleChangedListener) {
        mOnScaleChangeListener = listener
    }

    fun setOnSingleFlingListener(listener: OnSingleFlingListener) {
        mOnSingleFlingListener = listener
    }

    fun setOnViewDragListener(listener: OnViewDragListener) {
        mOnViewDragListener = listener
    }

    fun setOnViewTapListener(listener: OnViewTapListener) {
        mOnViewTapListener = listener
    }

    fun setOnClickListener(listener: View.OnClickListener) {
        mOnClickListener = listener
    }

    fun setOnLongClickListener(listener: View.OnLongClickListener) {
        mOnLongClickListener = listener
    }

    fun setOnDoubleTapListener(listener: GestureDetector.OnDoubleTapListener) {
        mGestureDetector?.setOnDoubleTapListener(listener)
    }

    fun getScaleType(): ImageView.ScaleType = mScaleType

    fun setScaleType(scaleType: ImageView.ScaleType) {
        if (isSupportedScaleType(scaleType) && scaleType != mScaleType) {
            mScaleType = scaleType
            update()
        }
    }

    private fun isSupportedScaleType(scaleType: ImageView.ScaleType?): Boolean {
        if (scaleType == null) {
            return false
        }
        if (scaleType == ImageView.ScaleType.MATRIX) {
            throw IllegalStateException("Matrix scale type is not supported")
        }
        return true
    }

    fun setBaseRotation(degrees: Float) {
        mBaseRotation = degrees % 360
        update()
        setRotationBy(mBaseRotation)
        checkAndDisplayMatrix()
    }

    fun setRotationTo(degrees: Float) {
        mSuppMatrix.setRotate(degrees % 360)
        checkAndDisplayMatrix()
    }

    fun setRotationBy(degrees: Float) {
        mSuppMatrix.postRotate(degrees % 360)
        checkAndDisplayMatrix()
    }

    fun getMinimumScale(): Float = mMinScale

    fun setMinimumScale(minimumScale: Float) {
        checkZoomLevels(minimumScale, mMidScale, mMaxScale)
        mMinScale = minimumScale
    }

    fun getMediumScale(): Float = mMidScale

    fun setMediumScale(mediumScale: Float) {
        checkZoomLevels(mMinScale, mediumScale, mMaxScale)
        mMidScale = mediumScale
    }

    fun getMaximumScale(): Float = mMaxScale

    fun setMaximumScale(maximumScale: Float) {
        checkZoomLevels(mMinScale, mMidScale, maximumScale)
        mMaxScale = maximumScale
    }

    fun setScaleLevels(minimumScale: Float, mediumScale: Float, maximumScale: Float) {
        checkZoomLevels(minimumScale, mediumScale, maximumScale)
        mMinScale = minimumScale
        mMidScale = mediumScale
        mMaxScale = maximumScale
    }

    fun getScale(): Float {
        return sqrt(
            getValue(mSuppMatrix, Matrix.MSCALE_X).toDouble().pow(2.0)
                    + getValue(mSuppMatrix, Matrix.MSKEW_Y).toDouble().pow(2.0)
        ).toFloat()
    }

    fun setScale(scale: Float, animate: Boolean = false) {
        setScale(scale, mImageView.right / 2f, mImageView.bottom / 2f, animate)
    }

    fun setScale(scale: Float, focalX: Float, focalY: Float, animate: Boolean) {
        if (scale < mMinScale || scale > mMaxScale) {
            throw IllegalArgumentException("Scale must be within the range of minScale and maxScale")
        }
        if (animate) {
            mImageView.post(AnimatedZoomRunnable(getScale(), scale, focalX, focalY))
        } else {
            mSuppMatrix.setScale(scale, scale, focalX, focalY)
            checkAndDisplayMatrix()
        }
    }

    fun setZoomInterpolator(interpolator: Interpolator) {
        mInterpolator = interpolator
    }

    fun isZoomable(): Boolean = mZoomEnabled

    fun setZoomable(zoomable: Boolean) {
        mZoomEnabled = zoomable
        update()
    }

    fun setZoomTransitionDuration(milliseconds: Int) {
        mZoomDuration = milliseconds
    }

    fun setAllowParentInterceptOnEdge(allow: Boolean) {
        mAllowParentInterceptOnEdge = allow
    }

    fun getDisplayRect(): RectF? {
        checkMatrixBounds()
        return getDisplayRect(getDrawMatrix())
    }

    fun getImageMatrix(): Matrix = mDrawMatrix

    fun setDisplayMatrix(finalMatrix: Matrix?): Boolean {
        if (finalMatrix == null) {
            throw IllegalArgumentException("Matrix cannot be null")
        }
        if (mImageView.drawable == null) {
            return false
        }
        mSuppMatrix.set(finalMatrix)
        checkAndDisplayMatrix()
        return true
    }

    fun setDrawMatrix(matrix: Matrix) {
        matrix.set(getDrawMatrix())
    }

    fun setSuppMatrix(matrix: Matrix) {
        matrix.set(mSuppMatrix)
    }

    fun update() {
        if (mZoomEnabled) {
            //更新Matrix
            updateBaseMatrix(mImageView.drawable)
        } else {
            //重置Matrix
            resetMatrix()
        }
    }

    private fun getValue(matrix: Matrix, whichValue: Int): Float {
        matrix.getValues(mMatrixValues)
        return mMatrixValues[whichValue]
    }

    private fun getDrawMatrix(): Matrix {
        mDrawMatrix.set(mBaseMatrix)
        mDrawMatrix.postConcat(mSuppMatrix)
        return mDrawMatrix
    }

    private fun getDisplayRect(matrix: Matrix): RectF? {
        val d = mImageView.drawable
        if (d != null) {
            mDisplayRect.set(0f, 0f, d.intrinsicWidth.toFloat(), d.intrinsicHeight.toFloat())
            matrix.mapRect(mDisplayRect)
            return mDisplayRect
        }
        return null
    }

    private fun resetMatrix() {
        mSuppMatrix.reset()
        setRotationBy(mBaseRotation)
        setImageViewMatrix(getDrawMatrix())
        checkMatrixBounds()
    }

    private fun setImageViewMatrix(matrix: Matrix) {
        mImageView.imageMatrix = matrix
        getDisplayRect(matrix)?.let {
            mOnMatrixChangeListener?.onMatrixChanged(it)
        }
    }

    private fun checkAndDisplayMatrix() {
        if (checkMatrixBounds()) {
            setImageViewMatrix(getDrawMatrix())
        }
    }

    private fun updateBaseMatrix(drawable: Drawable?) {
        if (drawable == null) {
            return
        }
        val viewWidth = getImageViewWidth(mImageView)
        val viewHeight = getImageViewHeight(mImageView)
        val drawableWidth = drawable.intrinsicWidth
        val drawableHeight = drawable.intrinsicHeight
        mBaseMatrix.reset()
        val widthScale = viewWidth / drawableWidth
        val heightScale = viewHeight / drawableHeight
        when (mScaleType) {
            ImageView.ScaleType.CENTER -> {
                mBaseMatrix.postTranslate((viewWidth - drawableWidth) / 2f, (viewHeight - drawableHeight) / 2f)
            }
            ImageView.ScaleType.CENTER_CROP -> {
                val scale = max(widthScale, heightScale).toFloat()
                mBaseMatrix.postScale(scale, scale)
                mBaseMatrix.postTranslate(
                    (viewWidth - drawableWidth * scale) / 2f,
                    (viewHeight - drawableHeight * scale) / 2f
                )
            }
            ImageView.ScaleType.CENTER_INSIDE -> {
                val scale = min(1.0f, min(widthScale, heightScale).toFloat())
                mBaseMatrix.postScale(scale, scale)
                mBaseMatrix.postTranslate(
                    (viewWidth - drawableWidth * scale) / 2f,
                    (viewHeight - drawableHeight * scale) / 2f
                )
            }
            else -> {
                var mTempSrc = RectF(0f, 0f, drawableWidth.toFloat(), drawableHeight.toFloat())
                val mTempDst = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
                if ((mBaseRotation % 180).toInt() != 0) {
                    mTempSrc = RectF(0f, 0f, drawableHeight.toFloat(), drawableWidth.toFloat())
                }
                when (mScaleType) {
                    ImageView.ScaleType.FIT_CENTER -> {
                        mBaseMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.CENTER)
                    }
                    ImageView.ScaleType.FIT_START -> {
                        mBaseMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.START)
                    }
                    ImageView.ScaleType.FIT_END -> {
                        mBaseMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.END)
                    }
                    ImageView.ScaleType.FIT_XY -> {
                        mBaseMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.FILL)
                    }
                }
            }
        }
        resetMatrix()
    }

    private fun checkMatrixBounds(): Boolean {
        val rectF = getDisplayRect(getDrawMatrix()) ?: return false
        val width = rectF.width()
        val height = rectF.height()
        var deltaX = 0f
        var deltaY = 0f
        val viewWidth = getImageViewWidth(mImageView)
        when {
            width <= viewWidth -> {
                deltaX = when (mScaleType) {
                    ImageView.ScaleType.FIT_START -> -rectF.left
                    ImageView.ScaleType.FIT_END -> viewWidth - width - rectF.left
                    else -> (viewWidth - width) / 2 - rectF.left
                }
                mHorizontalScrollEdge = HORIZONTAL_EDGE_BOTH
            }
            rectF.left > 0 -> {
                mHorizontalScrollEdge = HORIZONTAL_EDGE_LEFT
                deltaX = -rectF.left
            }
            rectF.right < viewWidth -> {
                deltaX = viewWidth - rectF.right
                mHorizontalScrollEdge = HORIZONTAL_EDGE_RIGHT
            }
            else -> {
                mHorizontalScrollEdge = HORIZONTAL_EDGE_NONE
            }
        }
        val viewHeight = getImageViewHeight(mImageView)
        when {
            height <= viewHeight -> {
                deltaY = when (mScaleType) {
                    ImageView.ScaleType.FIT_START -> -rectF.top
                    ImageView.ScaleType.FIT_END -> viewHeight - height - rectF.top
                    else -> (viewHeight - height) / 2 - rectF.top
                }
                mVerticalScrollEdge = VERTICAL_EDGE_BOTH
            }
            rectF.top > 0 -> {
                mVerticalScrollEdge = VERTICAL_EDGE_TOP
                deltaY = -rectF.top
            }
            rectF.bottom < viewHeight -> {
                mVerticalScrollEdge = VERTICAL_EDGE_BOTTOM
                deltaY = viewHeight - rectF.bottom
            }
            else -> {
                mVerticalScrollEdge = VERTICAL_EDGE_NONE
            }
        }
        mSuppMatrix.postTranslate(deltaX, deltaY)
        return true
    }

    private fun getImageViewWidth(imageView: ImageView): Int {
        return imageView.width - imageView.paddingLeft - imageView.paddingRight
    }

    private fun getImageViewHeight(imageView: ImageView): Int {
        return imageView.height - imageView.paddingTop - imageView.paddingBottom
    }

    private fun cancelFling() {
        mCurrentFlingRunnable?.cancelFling()
        mCurrentFlingRunnable = null
    }

    private inner class AnimatedZoomRunnable constructor(
        private val mZoomStart: Float,
        private val mZoomEnd: Float,
        private val mFocalX: Float,
        private val mFocalY: Float
    ) : Runnable {

        private var mStartTime: Long = 0

        init {
            mStartTime = System.currentTimeMillis()
        }

        override fun run() {
            val t = getInterpolate()
            val scale = mZoomStart + t * (mZoomEnd - mZoomStart)
            val deltaScale = scale / getScale()
            mOnGestureListener.onScale(deltaScale, mFocalX, mFocalY)
            if (t < 1f) {
                postOnAnimation(mImageView, this)
            }
        }

        private fun getInterpolate(): Float {
            var t = 1f * (System.currentTimeMillis() - mStartTime) / mZoomDuration
            t = min(1f, t)
            t = mInterpolator.getInterpolation(t)
            return t
        }

    }

    private inner class FlingRunnable constructor(private val mContext: Context) : Runnable {

        private val mScroller: OverScroller = OverScroller(mContext)
        private var mCurrentX: Int = 0
        private var mCurrentY: Int = 0

        fun cancelFling() {
            mScroller.forceFinished(true)
        }

        fun fling(viewWidth: Int, viewHeight: Int, velocityX: Int, velocityY: Int) {
            val rectF = getDisplayRect() ?: return
            var minX = 0f
            var maxX = 0f
            var minY = 0f
            var maxY = 0f
            val startX = round(-rectF.left)
            if (viewWidth < rectF.width()) {
                minX = 0f
                maxX = round(rectF.width() - viewWidth)
            } else {
                minX = startX
                maxX = startX
            }
            val startY = round(-rectF.top)
            if (viewHeight < rectF.height()) {
                minY = 0f
                maxY = round(rectF.height() - viewHeight)
            } else {
                minY = startY
                maxY = startY
            }
            mCurrentX = startX.toInt()
            mCurrentY = startY.toInt()
            if (startX != maxX || startY != maxY) {
                mScroller.fling(
                    startX.toInt(), startY.toInt(),
                    velocityX, velocityY,
                    minX.toInt(), maxX.toInt(),
                    minY.toInt(), maxY.toInt(),
                    0, 0
                )
            }
        }

        override fun run() {
            if (mScroller.isFinished) {
                return
            }
            if (mScroller.computeScrollOffset()) {
                val newX = mScroller.currX
                val newY = mScroller.currY
                mSuppMatrix.postTranslate((mCurrentX - newX).toFloat(), (mCurrentY - newY).toFloat())
                checkAndDisplayMatrix()
                mCurrentX = newX
                mCurrentY = newY
                postOnAnimation(mImageView, this)
            }
        }
    }

    private fun checkZoomLevels(minZoom: Float, midZoom: Float, maxZoom: Float) {
        if (minZoom >= midZoom) {
            throw IllegalArgumentException("Minimum zoom has to be less than Medium zoom. Call setMinimumZoom() with a more appropriate value")
        } else if (midZoom >= maxZoom) {
            throw IllegalArgumentException("Medium zoom has to be less than Maximum zoom. Call setMaximumZoom() with a more appropriate value")
        }
    }

    private fun postOnAnimation(view: View, runnable: Runnable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.postOnAnimation(runnable)
        } else {
            view.postDelayed(runnable, 1000 / 60)
        }
    }

}