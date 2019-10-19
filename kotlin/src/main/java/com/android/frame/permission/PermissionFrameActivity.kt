package com.android.frame.permission

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.android.basicproject.R
import com.android.util.ToastUtil
import com.android.util.initCommonLayout
import com.android.util.jumpToAppSetting
import kotlinx.android.synthetic.main.activity_common_layout.*
import org.jetbrains.anko.toast
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

/**
 * Created by xuzhb on 2019/9/3
 * Desc:权限动态申请，EasyPermission实现
 */
class PermissionFrameActivity : Activity(), EasyPermissions.PermissionCallbacks {

    companion object {
        private val REQUEST_PERMISSION = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        private const val REQUEST_PERMISSION_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_layout)
        initCommonLayout(this, "动态权限申请", "申请权限", "应用设置")
        btn1.setOnClickListener {
            initPermission()
        }
        btn2.setOnClickListener {
            jumpToAppSetting(this)
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_CODE) //加上该标签，权限全部申请成功后回调
    private fun executeNextLogic() {
        ToastUtil.toast("权限申请成功，执行接下来的逻辑！")
    }

    //申请权限
    private fun initPermission() {
        if (EasyPermissions.hasPermissions(this, *REQUEST_PERMISSION)) {
            executeNextLogic()
        } else {
            //申请权限
            EasyPermissions.requestPermissions(
                this,
                "请求必要的权限，拒绝权限可能会无法使用app",
                REQUEST_PERMISSION_CODE,
                *REQUEST_PERMISSION
            )
        }
    }

    /**
     * 重写要申请权限的Activity或者Fragment的onRequestPermissionsResult()方法，
     * 在里面调用EasyPermissions.onRequestPermissionsResult()，实现回调。
     *
     * @param requestCode  权限请求的识别码
     * @param permissions  申请的权限
     * @param grantResults 授权结果
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * 当权限申请失败的时候执行的回调
     *
     * @param requestCode 权限请求的识别码
     * @param perms       申请的权限的名字
     */
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {  //拒绝权限且点击了不再提示
            //跳转应用设置页面
            AppSettingsDialog.Builder(this)
                .setTitle("权限申请")
                .setRationale("请到应用设置页面-权限中开启相应权限，保证app的正常使用")  //设置提示内容
                .setNegativeButton("取消")
                .setPositiveButton("去设置")
                .build()
                .show()
        } else {
            ToastUtil.toast("申请权限失败！")
//            EasyPermissions.requestPermissions(
//                this,
//                "请求必要的权限，拒绝权限可能会无法使用app",
//                REQUEST_PERMISSION_CODE,
//                *perms.toTypedArray()
//            )
        }
    }

    /**
     * 当权限被成功申请的时候执行回调，只要有一项权限申请成功就会被回调，并非所有权限申请成功才回调
     *
     * @param requestCode 权限请求的识别码
     * @param perms       申请的权限的名字
     */
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            //当从软件设置界面，返回当前程序时候回调，点击应用设置对话框的取消按钮也会回调
            if (EasyPermissions.hasPermissions(this, *REQUEST_PERMISSION)) {
                executeNextLogic()
            } else {
                ToastUtil.toast("权限开启失败！")
            }
        }
    }
}