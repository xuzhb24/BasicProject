package com.android.widget.PhotoViewer.widget

import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.GestureDetector
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatImageView
import com.android.widget.PhotoViewer.listener.*

/**
 * Created by xuzhb on 2021/3/30
 * Desc:一个可以缩放的ImageView，具体用法参考PhotoViewAttacher
 */
class PhotoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var mAttacher: PhotoViewAttacher = PhotoViewAttacher(this)
    private var mPendingScaleType: ScaleType? = null

    init {
        super.setScaleType(ScaleType.MATRIX)
        if (mPendingScaleType != null) {
            scaleType = mPendingScaleType!!
            mPendingScaleType = null
        }
    }

    fun getAttacher(): PhotoViewAttacher = mAttacher

    fun setOnMatrixChangeListener(listener: OnMatrixChangedListener) {
        mAttacher.setOnMatrixChangeListener(listener)
    }

    fun setOnOutsidePhotoTapListener(listener: OnOutsidePhotoTapListener) {
        mAttacher.setOnOutsidePhotoTapListener(listener)
    }

    fun setOnPhotoTapListener(listener: OnPhotoTapListener) {
        mAttacher.setOnPhotoTapListener(listener)
    }

    fun setOnScaleChangeListener(listener: OnScaleChangedListener) {
        mAttacher.setOnScaleChangeListener(listener)
    }

    fun setOnSingleFlingListener(listener: OnSingleFlingListener) {
        mAttacher.setOnSingleFlingListener(listener)
    }

    fun setOnViewDragListener(listener: OnViewDragListener) {
        mAttacher.setOnViewDragListener(listener)
    }

    fun setOnViewTapListener(listener: OnViewTapListener) {
        mAttacher.setOnViewTapListener(listener)
    }

    override fun setOnLongClickListener(listener: OnLongClickListener?) {
        listener?.let { mAttacher.setOnLongClickListener(it) }
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        listener?.let { mAttacher.setOnClickListener(it) }
    }

    fun setOnDoubleTapListener(listener: GestureDetector.OnDoubleTapListener) {
        mAttacher.setOnDoubleTapListener(listener)
    }

    override fun getScaleType(): ScaleType {
        return mAttacher.getScaleType()
    }

    override fun setScaleType(scaleType: ScaleType) {
        mAttacher.setScaleType(scaleType)
    }

    fun setRotationTo(rotationDegree: Float) {
        mAttacher.setRotationTo(rotationDegree)
    }

    fun setRotationBy(rotationDegree: Float) {
        mAttacher.setRotationBy(rotationDegree)
    }

    fun getMinimumScale(): Float = mAttacher.getMinimumScale()

    fun setMinimumScale(minimumScale: Float) {
        mAttacher.setMinimumScale(minimumScale)
    }

    fun getMediumScale(): Float = mAttacher.getMediumScale()

    fun setMediumScale(mediumScale: Float) {
        mAttacher.setMediumScale(mediumScale)
    }

    fun getMaximumScale(): Float = mAttacher.getMaximumScale()

    fun setMaximumScale(maximumScale: Float) {
        mAttacher.setMaximumScale(maximumScale)
    }

    fun setScaleLevels(minimumScale: Float, mediumScale: Float, maximumScale: Float) {
        mAttacher.setScaleLevels(minimumScale, mediumScale, maximumScale)
    }

    fun getScale(): Float = mAttacher.getScale()

    fun setScale(scale: Float, animate: Boolean = false) {
        mAttacher.setScale(scale, animate)
    }

    fun setScale(scale: Float, focalX: Float, focalY: Float, animate: Boolean) {
        mAttacher.setScale(scale, focalX, focalY, animate)
    }

    fun isZoomable(): Boolean = mAttacher.isZoomable()

    fun setZoomable(zoomable: Boolean) {
        mAttacher.setZoomable(zoomable)
    }

    fun setZoomTransitionDuration(milliseconds: Int) {
        mAttacher.setZoomTransitionDuration(milliseconds)
    }

    fun setAllowParentInterceptOnEdge(allow: Boolean) {
        mAttacher.setAllowParentInterceptOnEdge(allow)
    }

    fun getDisplayRect(): RectF? = mAttacher.getDisplayRect()

    override fun getImageMatrix(): Matrix = mAttacher.getImageMatrix()

    fun setDisplayMatrix(finalRectangle: Matrix) = mAttacher.setDisplayMatrix(finalRectangle)

    fun setDrawMatrix(matrix: Matrix) {
        mAttacher.setDrawMatrix(matrix)
    }

    fun setSuppMatrix(matrix: Matrix) {
        mAttacher.setSuppMatrix(matrix)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        mAttacher.update()
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        mAttacher.update()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        mAttacher.update()
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        val change = super.setFrame(l, t, r, b)
        if (change) {
            mAttacher.update()
        }
        return change
    }

}