package com.android.util.permission

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import com.android.basicproject.databinding.ActivityCommonLayoutBinding
import com.android.frame.mvc.BaseActivity
import com.android.util.AlertDialogUtil
import com.android.util.LogUtil
import com.android.util.initCommonLayout

/**
 * Created by xuzhb on 2020/12/16
 * Desc:
 */
class TestPermissionActivity : BaseActivity<ActivityCommonLayoutBinding>() {

    companion object {
        private const val TAG = "PermissionTAG"
        private val REQUEST_PERMISSION: Array<String> = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        private const val REQUEST_PERMISSION_CODE = 1
        private const val REQUEST_SETTINGS_CODE = 1111
    }

    override fun handleView(savedInstanceState: Bundle?) {
        initCommonLayout(this, "权限工具", "申请权限", "应用设置")
    }

    override fun initListener() {
        binding.btn1.setOnClickListener {
            //申请权限
            if (PermissionUtil.requestPermissions(this, REQUEST_PERMISSION_CODE, *REQUEST_PERMISSION)) {
                executeNextLogic()
            }
        }
        binding.btn2.setOnClickListener {
            //打开应用设置页面
            PermissionUtil.openSettings(this, packageName, 2222)
        }
    }

    override fun getViewBinding() = ActivityCommonLayoutBinding.inflate(layoutInflater)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //处理权限申请结果
        PermissionUtil.onRequestPermissionsResult(
            this,
            requestCode,
            permissions,
            grantResults,
            object : PermissionUtil.OnPermissionListener {
                override fun onPermissionGranted() {
                    executeNextLogic()
                }

                override fun onPermissionDenied(deniedPermissions: Array<String>) {
                    LogUtil.e(TAG, "onPermissionDenied：" + deniedPermissions.contentToString())
                    LogUtil.e(
                        TAG,
                        "isPermissionDenied：" + PermissionUtil.isPermissionDenied(
                            this@TestPermissionActivity,
                            *REQUEST_PERMISSION
                        )
                    )
                    LogUtil.e(
                        TAG,
                        "isPermissionDeniedForever：" + PermissionUtil.isPermissionDeniedForever(
                            this@TestPermissionActivity,
                            *REQUEST_PERMISSION
                        )
                    )
                    AlertDialogUtil.showDialog(this@TestPermissionActivity, "权限申请",
                        "请授予应用相应的权限，否则app可能无法正常工作", false,
                        positiveClickListener = DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                            PermissionUtil.requestPermissions(
                                this@TestPermissionActivity,
                                REQUEST_PERMISSION_CODE,
                                *REQUEST_PERMISSION
                            )
                        },
                        negativeClickListener = DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                            showToast("权限申请失败！")
                        }
                    )

                }

                override fun onPermissionDeniedForever(deniedForeverPermissions: Array<String>) {
                    AlertDialogUtil.showDialog(this@TestPermissionActivity, "权限申请",
                        "请到应用设置页面-权限中开启相应权限，保证app的正常使用", false, "去设置", "取消",
                        positiveClickListener = DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                            PermissionUtil.openSettings(
                                this@TestPermissionActivity,
                                packageName,
                                REQUEST_SETTINGS_CODE
                            )
                        },
                        negativeClickListener = DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                            showToast(
                                "权限申请失败！"
                            )
                        }
                    )
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SETTINGS_CODE) {
            //是否已申请所有权限
            if (PermissionUtil.isPermissionGranted(this, *REQUEST_PERMISSION)) {
                executeNextLogic()
            } else {
                showToast("权限开启失败！")
            }
        }
    }

    private fun executeNextLogic() {
        showToast("权限申请成功，执行接下来的逻辑！")
    }

}