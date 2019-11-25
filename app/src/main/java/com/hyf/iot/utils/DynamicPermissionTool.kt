package com.hyf.iot.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import java.util.ArrayList

class DynamicPermissionTool {
    private var context: Context? = null
    private lateinit var permissionGrantedFactory: PermissionGrantedFactory

    var permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    var deniedHints = arrayOf("没有相机权限将不能拍照", "没有存储设备的读写权限将不能存储照片到本地")

    /**
     * 如果不需要判断是否之前被拒绝过，调用该构造
     */
    constructor(context: Context) {
        this.context = context
    }

    /**
     * 如果需要判断之前是否被拒绝过，调用该构造，
     *
     * @param context                  context
     * @param permissionGrantedFactory permissionGrantedFactory
     */
    constructor(context: Context, permissionGrantedFactory: PermissionGrantedFactory) {
        this.context = context
        this.permissionGrantedFactory = permissionGrantedFactory
    }

    /**
     * 检查单个权限是否已经被允许
     *
     * @param permission 要申请的权限
     */
    private fun checkCurPermissionStatus(permission: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context!!, permission)
    }

    /**
     * 检查一组权限的授权状态。
     *
     * @param permissions 权限数组
     * @return 权限的状态数组
     */
    private fun checkCurPermissionsStatus(permissions: Array<String>?): BooleanArray {
        if (null != permissions && permissions.size > 0) {
            val permissionsStatus = BooleanArray(permissions.size)

            for (i in permissions.indices) {
                val permissionStatus = checkCurPermissionStatus(permissions[i])
                permissionsStatus[i] = permissionStatus
            }
            return permissionsStatus
        } else {
            throw IllegalArgumentException("参数不能为空，且必须有元素")
        }
    }

    /**
     * 获取被拒绝的权限
     *
     * @param permissions 要申请的全部权限
     */
    private fun getDeniedPermissions(permissions: Array<String>?): Array<String> {
        if (null != permissions && permissions.size > 0) {
            val deniedPermissionList = ArrayList<String>()

            val permissionsStatus = checkCurPermissionsStatus(permissions)
            for (i in permissions.indices) {
                if (!permissionsStatus[i]) {
                    deniedPermissionList.add(permissions[i])
                }
            }

            val deniedPermissions = arrayOfNulls<String>(deniedPermissionList.size)
            return deniedPermissionList.toTypedArray()
        } else {
            throw IllegalArgumentException("参数不能为空，且必须有元素")
        }
    }

    /**
     * 获取被拒绝的权限对应的提示文本数组
     *
     * @param permissions 要申请的全部权限
     * @param hints       权限被拒绝时的提示文本
     */
    fun getDeniedHint(permissions: Array<String>?, hints: Array<String>?): Array<String> {
        if (null == permissions || null == hints || permissions.size == 0 || hints.size == 0 || permissions
                        .size != hints.size) {
            throw IllegalArgumentException("参数不能为空、必须有元素，且两个参数的长度必须一致")
        } else {
            val deniedHintList = ArrayList<String>()

            val permissionsStatus = checkCurPermissionsStatus(permissions)
            for (i in permissions.indices) {
                if (!permissionsStatus[i]) {
                    deniedHintList.add(hints[i])
                }
            }

            val deniedPermissions = arrayOfNulls<String>(deniedHintList.size)
            return deniedHintList.toTypedArray()
        }
    }

    /**
     * 获取被拒绝的权限对应的提示文本字符串
     *
     * @param permissions 要申请的全部权限
     * @param hints       权限被拒绝时的提示文本
     */
    private fun getDeniedHintStr(permissions: Array<String>?, hints: Array<String>?): String {
        if (null == permissions || null == hints || permissions.size == 0 || hints.size == 0 || permissions
                        .size != hints.size) {
            throw IllegalArgumentException("参数不能为空、必须有元素，且两个参数的长度必须一致")
        } else {
            val hintStr = StringBuilder()
            val permissionsStatus = checkCurPermissionsStatus(permissions)
            for (i in permissions.indices) {
                if (!permissionsStatus[i]) {
                    hintStr.append(hints[i]).append("\n")
                }
            }
            return hintStr.toString()
        }
    }

    /**
     * 是否所有权限都已经被允许
     *
     * @param permissions 申请的权限
     * @return true 全被允许，false 有没有被允许的权限
     */
    fun isAllPermissionGranted(permissions: Array<String>): Boolean {
        return getDeniedPermissions(permissions).size == 0
    }

    /**
     * 是否所有权限都已经被允许
     *
     * @param grantResults 权限申请的结果
     * @return true 全被允许，false 有没有被允许的权限
     */
    fun isAllPermissionGranted(grantResults: IntArray): Boolean {
        var isAllGranted = PackageManager.PERMISSION_GRANTED
        for (grantResult in grantResults) {
            isAllGranted = isAllGranted or grantResult
        }
        return isAllGranted == 0
    }

    /**
     * 请求权限
     *
     * @param activity    Activity
     * @param permissions 权限
     * @param requestCode 请求码
     */
    fun requestNecessaryPermissions(activity: Activity, permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }


    /**
     * 检测之前是否已经拒绝过。如果已经拒绝过，那么再次请求权限的时候就需要给出原因
     *
     * @param activity    activity
     * @param permissions 请求的权限
     */
    private fun shouldShowRequestReason(activity: Activity, permissions: Array<String>): Boolean {
        var showReason = false
        for (i in permissions.indices) {
            showReason = showReason or ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])
        }
        return showReason
    }

    /**
     * 展示被拒绝的弹窗
     */
    private fun showDeniedDialog(context: Context, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("提示")
        builder.setMessage(message)
        builder.setNegativeButton("就不给你权限", null)
        builder.setPositiveButton("我要重新开启权限") { dialog, which -> openSysSettingPage(context) }
        builder.setCancelable(false)
        builder.show()
    }

    /**
     * 打开系统设置界面
     */
    private fun openSysSettingPage(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    /**
     * 如果需要判断之前是否被拒绝过，则创建该类的实例，然后将具体的事件处理放在该类的方法中
     */
    interface PermissionGrantedFactory {
        /**
         * 处理事件或者请求权限：如果权限已经被允许，执行事件，否则请求权限
         */
        fun handleEventOrRequestPermission()
    }

    /**
     * 判断是否需要展示为什么二次请求权限，如果不需要执行相应的操作
     *
     *
     * 该方法中首先会检测之前是否被拒绝过，如果已经被拒绝过则展示为什么需要再次申请这个权限，并引导用户去设置中开启权限。
     *
     * @param activity    activity
     * @param permissions 请求的权限
     * @param hints       权限被拒绝时的提示
     */
    fun showRequestReasonOrHandlePermissionEvent(activity: Activity, permissions: Array<String>, hints: Array<String>) {
        val hadBeanDenied = shouldShowRequestReason(activity, permissions)
        if (hadBeanDenied) {
            showDeniedDialog(activity, getDeniedHintStr(permissions, hints))
        } else {
            permissionGrantedFactory?.handleEventOrRequestPermission()
        }
    }
}