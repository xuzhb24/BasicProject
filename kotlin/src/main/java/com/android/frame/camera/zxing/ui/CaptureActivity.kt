package com.android.frame.camera.zxing.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.provider.MediaStore
import android.text.TextUtils
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.viewbinding.ViewBinding
import com.android.basicproject.databinding.ActivityCaptureBinding
import com.android.frame.camera.zxing.camera.CameraManager
import com.android.frame.camera.zxing.decode.CaptureActivityHandler
import com.android.frame.camera.zxing.decode.InactivityTimer
import com.android.frame.camera.zxing.sound.BeepManager
import com.android.frame.camera.zxing.util.QRCodeUtil
import com.android.frame.camera.zxing.util.QRConstant
import com.android.frame.camera.zxing.view.ViewfinderView
import com.android.frame.mvc.BaseActivity
import com.android.util.LogUtil
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_capture.*
import java.util.*
import kotlin.concurrent.thread

/**
 * Created by xuzhb on 2019/11/16
 * Desc:扫码的Activity类
 * 整个Activity最重要的两个控件是一个SurfaceView（摄像头）和一个ViewfinderView（扫描区）
 * 对于继承CaptureActivity的Activity子类来说，
 * 可以选择在自己的布局中定义和CaptureActivity的布局文件id相同的控件，
 * 这样即使它们在两个布局中表现不同也能执行相同的逻辑，包括其他控件
 * 或者选择重写getSurfaceView()和getViewfinderView()返回对应的两个控件，
 * 扫码最终是在handleDecode(result: Result?, bitmap: Bitmap?)处理扫描后的结果
 */
open class CaptureActivity<VB : ViewBinding> : BaseActivity<VB>(), SurfaceHolder.Callback {

    companion object {
        private const val TAG = "CaptureActivity"
        private const val IMAGE_PICKER = 1999
    }

    private var mBeepManager: BeepManager? = null
    private var mHandler: CaptureActivityHandler? = null
    private var mDecodeFormats: Vector<BarcodeFormat>? = null
    private var mCharacterSet: String? = null
    private var mInactivityTimer: InactivityTimer? = null
    private var hasSurface: Boolean = false
    private var isLightOn: Boolean = false  //是否打开闪光灯
    private var isPlayBeep: Boolean = true  //是否开启扫描后的滴滴声
    private var isVibrate: Boolean = true   //是否震动
    private var mPhotoPath: String = ""     //选中的图片路径

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CameraManager.init(applicationContext)
        mBeepManager = BeepManager(applicationContext)
        hasSurface = false
        mInactivityTimer = InactivityTimer(this)
    }

    override fun initViewBinding() {
        binding = ActivityCaptureBinding.inflate(layoutInflater) as VB
    }

    override fun handleView(savedInstanceState: Bundle?) {

    }

    override fun initListener() {
        //这里带"?"是因为继承CaptureActivity的Activity子类的布局不一定包含id为title_bar和light_ll的控件，
        //没有的话如果子类通过super.initListener()覆写时会因为找不到而报异常，所以这里加了一个判空；
        //如果子类的布局中包含id相同的控件，则不需要在子类中再重写相同的逻辑
        title_bar?.setOnLeftIconClickListener {
            finish()
        }
        title_bar?.setOnRightTextClickListener {
            openAlbum()  //打开相册选取图片扫描
        }
        light_ll?.setOnClickListener {
            switchLight()  //打开或关闭闪光灯
        }
    }

    //打开相册
    protected fun openAlbum() {
        val intent = Intent(Intent.ACTION_PICK, null)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intent, IMAGE_PICKER)
    }

    //开启/关闭闪光灯
    private fun switchLight() {
        if (CameraManager.get() != null) {
            if (isLightOn) {
                light_tv.text = "轻触点亮"
                CameraManager.get().turnLightOffFlashLight()
            } else {
                light_tv.text = "轻触关闭"
                CameraManager.get().turnOnFlashLight()
            }
            isLightOn = !isLightOn
            light_iv.isSelected = isLightOn
        }
    }

    override fun onResume() {
        super.onResume()
        val holder = getSurfaceView().holder
        if (hasSurface) {
            initCamera(holder)
        } else {
            holder.addCallback(this)
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        }
        mDecodeFormats = null
        mCharacterSet = null
    }

    override fun onPause() {
        super.onPause()
        mHandler?.quitSynchronously()
        mHandler = null
        CameraManager.get().closeDriver()
    }

    override fun onDestroy() {
        mInactivityTimer?.shutdown()
        mBeepManager?.releaseRing()
        super.onDestroy()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        hasSurface = false
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        if (!hasSurface) {
            hasSurface = true
            initCamera(holder!!)
        }
    }

    private fun initCamera(holder: SurfaceHolder) {
        try {
            CameraManager.get().openDriver(holder)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (mHandler == null) {
            mHandler = CaptureActivityHandler(this, mDecodeFormats, mCharacterSet)
        }
    }

    //继承CaptureActivity的Activity类，如果SurfaceView的id和CaptureActivity布局中SurfaceView的id不同
    //需要重写这个方法，返回自己布局中的SurfaceView
    open fun getSurfaceView(): SurfaceView = surfaceView

    //继承CaptureActivity的Activity类，如果ViewfinderView的id和CaptureActivity布局中ViewfinderView的id不同
    //需要重写这个方法，返回自己布局中的ViewfinderView
    open fun getViewfinderView(): ViewfinderView = viewfinderView

    fun getHandler(): Handler = mHandler as Handler

    fun drawViewfinder() {
        getViewfinderView().drawViewfinder()
    }

    //处理扫描后的结果
    open fun handleDecode(result: Result?, bitmap: Bitmap?) {
        mInactivityTimer?.onActivity()
        if (result != null) {
            val text = result.text
            LogUtil.i(TAG, "识别的结果：$text")
            if (!TextUtils.isEmpty(text)) {  //识别成功
                playBeepSoundAndVibrate()
                returnQRCodeResult(text)
            } else {
                showToast("很抱歉，识别二维码失败！", true)
            }
        } else {
            showToast("未发现二维码！", true)
        }
    }

    private fun playBeepSoundAndVibrate() {
        if (isPlayBeep) {
            mBeepManager?.startRing()  //播放扫码的滴滴声
        }
        if (isVibrate) {
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(200L)  //震动200毫秒
        }
    }

    //返回扫描结果
    private fun returnQRCodeResult(result: String) {
        val intent = Intent()
        intent.putExtra(QRConstant.SCAN_QRCODE_RESULT, result)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
            data?.let {
                //获取选中图片的路径
                val cursor = contentResolver.query(it.data, null, null, null, null)
                cursor?.let {
                    if (it.moveToFirst()) {
                        mPhotoPath = it.getString(it.getColumnIndex(MediaStore.Images.Media.DATA))
                    }
                    it.close()
                    if (!TextUtils.isEmpty(mPhotoPath)) {
                        //可以加个提示正在扫描的加载框，如showLoadingDialog("正在扫描...")
                        thread(start = true) {
                            handleDecode(QRCodeUtil.decodeImage(mPhotoPath), null)
                            //取消加载框，dismissLoadingDialog()
                        }
                    } else {
                        LogUtil.e(TAG, "未找到图片！")
                    }
                }
            }
        }
    }

}