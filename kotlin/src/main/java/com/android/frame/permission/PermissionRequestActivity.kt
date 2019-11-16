package com.android.frame.permission

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import com.android.basicproject.R
import com.android.util.BitmapUtil
import com.android.util.ToastUtil
import com.android.util.initCommonLayout
import com.android.util.jumpToAppSetting
import kotlinx.android.synthetic.main.activity_common_layout.*
import kotlin.concurrent.thread

/**
 * Created by xuzhb on 2019/9/3
 * Desc:动态权限申请，原生API实现
 */
class PermissionRequestActivity : Activity() {

    //检查权限：android.content.ContextWrapper#checkSelfPermission
    //申请授权：android.app.Activity#requestPermissions
    //处理权限申请回调：android.app.Activity#onRequestPermissionsResult
    //是否应该提示：android.app.Activity#shouldShowRequestPermissionRationale，当用户拒绝权限申请
    //并选择了Don't ask again选项，此方法会返回false，如果只拒绝权限申请返回true

    companion object {
        //要申请的权限组，要先在AndroidManifests.xml配置权限
        private val REQUEST_PERMISSION = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        //请求状态码
        private const val REQUEST_PERMISSION_CODE = 1
        private const val DEFAULT_SETTINGS_REQ_CODE = 16061

        private const val PERMISSION_GRANTED = 0  //权限申请成功
        private const val PERMISSION_DENIED = -1  //权限申请被拒绝，但未选择不再提示
        private const val PERMISSION_DENIED_NOT_REQUEST_FOREVER = -2  //权限申请被拒绝，而且选择不再提示
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

    private fun executeNextLogic() {
        ToastUtil.toast("权限申请成功，执行接下来的逻辑！")
    }

    private fun initPermission() {
        //检测是否已获取相应的权限
        if (!hasPermissions(*REQUEST_PERMISSION)) {
            //动态申请权限
            ActivityCompat.requestPermissions(this, REQUEST_PERMISSION, REQUEST_PERMISSION_CODE)
        } else {
            executeNextLogic()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

//        val sb = StringBuilder()
//        var index = 1
//        var hasAllPermission = true
//        if (requestCode == REQUEST_PERMISSION_CODE) {
//            for (i in grantResults.indices) {
//                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//                    sb.append("${index++}：").append(permissions[i]).append("\n")
//
//                    hasAllPermission = false
//                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
//                        //权限申请被拒绝，同时选择“不再提示”
//                    } else {
//                        //权限申请被拒绝，但用户未选择“不再提示”选项
//                    }
////                    break
//                }
//            }
//            if (hasAllPermission) {
//                executeNextLogic()
//            } else {
//                showPermissionDialog("缺乏以下权限：\n${sb}")
//            }
//        }

        when (getRequestPermissionsResult(requestCode, permissions, grantResults, this)) {
            PERMISSION_GRANTED -> {  //权限申请成功
                executeNextLogic()
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
                                this@PermissionRequestActivity,
                                REQUEST_PERMISSION,
                                REQUEST_PERMISSION_CODE
                            )
                        }
                    })
                    .setNegativeButton("取消", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            ToastUtil.toast("权限申请失败！")
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
                            ToastUtil.toast("权限申请失败！")
                        }
                    })
                    .show()
            }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DEFAULT_SETTINGS_REQ_CODE) {
            //当从软件设置界面，返回当前程序时候回调，点击应用设置对话框的取消按钮也会回调
            if (hasPermissions(*REQUEST_PERMISSION)) {  //已经授予了全部的权限
                executeNextLogic()
            } else {
                ToastUtil.toast("权限开启失败！")
            }
        }
    }

    //显示权限申请对话框
//    private fun showPermissionDialog(msg: String) {
//        AlertDialog.Builder(this)
//            .setTitle("权限申请")
//            .setMessage(msg)
//            .setCancelable(false)
//            .setPositiveButton("去设置", object : DialogInterface.OnClickListener {
//                override fun onClick(dialog: DialogInterface?, which: Int) {
//                    jumpToAppSetting(this@PermissionRequestActivity)
//                }
//            })
//            .setNegativeButton("取消", object : DialogInterface.OnClickListener {
//                override fun onClick(dialog: DialogInterface?, which: Int) {
//                    ToastUtil.toast("权限申请失败！")
//                }
//            })
//            .show()
//    }

    //测试方法：保存图片到系统相册，需要写入SD卡的权限，否则无法保存成功
    private fun testSaveToFile() {
        //IO操作放子线程
        thread(start = true) {
            val flag = BitmapUtil.saveImageToGallery(
                this,
                BitmapFactory.decodeResource(resources, R.mipmap.ic_logo)
            )
            runOnUiThread {
                ToastUtil.toast(if (flag) "保存成功！" else "保存失败！")
            }
        }
    }

}