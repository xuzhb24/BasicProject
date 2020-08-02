package com.android.frame.camera.zxing.AATest

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.android.basicproject.R
import com.android.frame.camera.zxing.ui.CaptureActivity
import com.android.frame.camera.zxing.util.QRConstant
import com.android.frame.mvc.BaseActivity
import com.android.util.initCommonLayout
import kotlinx.android.synthetic.main.activity_common_layout.*

/**
 * Created by xuzhb on 2019/11/16
 * Desc:测试扫码，要注意权限的申请
 */
class TestZXingActivity : BaseActivity() {

    companion object {
        private const val REQUEST_CODE = 1

        //集成权限申请（原生实现）
        //要申请的权限组，要先在AndroidManifests.xml配置权限
        private val REQUEST_PERMISSION = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,   //从相册选取照片需要该权限
            Manifest.permission.WRITE_EXTERNAL_STORAGE,  //从相册选取照片需要该权限
            Manifest.permission.CAMERA
        )

        //请求状态码
        private const val REQUEST_PERMISSION_CODE = 1
        private const val DEFAULT_SETTINGS_REQ_CODE = 16061

        private const val PERMISSION_GRANTED = 0  //权限申请成功
        private const val PERMISSION_DENIED = -1  //权限申请被拒绝，但未选择不再提示
        private const val PERMISSION_DENIED_NOT_REQUEST_FOREVER = -2  //权限申请被拒绝，而且选择不再提示
    }

    private var mCurrentPosition: Int = 1

    override fun handleView(savedInstanceState: Bundle?) {
        initCommonLayout(this, "zxing扫码", "CaptureActivity", "CaptureActivity子类", showTextView = true)
    }

    override fun initListener() {
        btn1.setOnClickListener {
            tv.text = ""
            mCurrentPosition = 1
            initPermission()
        }
        btn2.setOnClickListener {
            tv.text = ""
            mCurrentPosition = 2
            initPermission()
        }
    }

    private fun startScanCode() {
        if (mCurrentPosition == 1) {
            val intent = Intent(this, CaptureActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        } else {
            val intent = Intent(this, ScanActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_common_layout

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //扫码的结果
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringExtra(QRConstant.SCAN_QRCODE_RESULT)
            tv.text = result ?: "未能识别二维码"
        }
        //集成权限申请（原生实现）
        if (requestCode == DEFAULT_SETTINGS_REQ_CODE) {
            //当从软件设置界面，返回当前程序时候回调，点击应用设置对话框的取消按钮也会回调
            if (hasPermissions(*REQUEST_PERMISSION)) {  //已经授予了全部的权限
                startScanCode()
            } else {
                showToast("权限开启失败！")
            }
        }
    }

    //集成权限申请（原生实现）
    private fun initPermission() {
        //检测是否已获取相应的权限
        if (!hasPermissions(*REQUEST_PERMISSION)) {
            //动态申请权限
            ActivityCompat.requestPermissions(
                this,
                REQUEST_PERMISSION,
                REQUEST_PERMISSION_CODE
            )
        } else {
            startScanCode()
        }
    }

    //是否已申请权限
    private fun hasPermissions(vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  //API 23后权限要动态申请
            for (p in permissions) {
                //PackageManager.PERMISSION_GRANTED(0):权限申请被允许
                //PackageManager.PERMISSION_DENIED(-1):权限申请被拒绝
                if (ActivityCompat.checkSelfPermission(this, p)
                    != PackageManager.PERMISSION_GRANTED
                ) {  //权限未被授予
                    return false
                }
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (getRequestPermissionsResult(requestCode, permissions, grantResults, this)) {
            PERMISSION_GRANTED -> {  //全部权限申请成功
                startScanCode()
            }
            PERMISSION_DENIED -> {  //权限申请被拒绝，但未选择不再提示
                AlertDialog.Builder(this)
                    .setTitle("权限申请")
                    .setMessage("请授予应用相应的权限，否则app可能无法正常工作")
                    .setCancelable(false)
                    .setPositiveButton("确定", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            //动态申请权限
                            ActivityCompat.requestPermissions(
                                this@TestZXingActivity,
                                REQUEST_PERMISSION,
                                REQUEST_PERMISSION_CODE
                            )
                        }
                    })
                    .setNegativeButton("取消", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            showToast("权限申请失败！")
                        }
                    })
                    .show()
            }
            PERMISSION_DENIED_NOT_REQUEST_FOREVER -> {  //权限申请被拒绝，而且选择不再提示
                AlertDialog.Builder(this)
                    .setTitle("权限申请")
                    .setMessage("请到应用设置页面-权限中开启相应权限，保证app的正常使用")
                    .setCancelable(false)
                    .setPositiveButton("去设置", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
//                            jumpToAppSetting(this@PermissionRequestActivity)
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.setData(Uri.fromParts("package", packageName, null))
                            startActivityForResult(intent, DEFAULT_SETTINGS_REQ_CODE)
                        }
                    })
                    .setNegativeButton("取消", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            showToast("权限申请失败！")
                        }
                    })
                    .show()
            }
        }
    }

    //获取权限申请结果
    private fun getRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
        activity: Activity
    ): Int {
        var result = PERMISSION_GRANTED
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (i in grantResults.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {  //申请权限被拒绝
                    result = PERMISSION_DENIED
                    //拒绝权限且点击了不再提示
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            permissions[i]
                        )
                    ) {
                        result = PERMISSION_DENIED_NOT_REQUEST_FOREVER
                        break
                    }
                }
            }
        }
        return result
    }

}