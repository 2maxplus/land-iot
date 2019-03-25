package com.hyf.intelligence.kotlin.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.hyf.intelligence.kotlin.R
import com.hyf.intelligence.kotlin.common.activity.BaseActivity
import com.hyf.intelligence.kotlin.utils.newIntent
import com.hyf.intelligence.kotlin.utils.showToast
import kotlinx.android.synthetic.main.activity_flash.*


class FlashActivity : BaseActivity(){
    private var BAIDU_READ_PHONE_STATE = 100

    override fun getLayoutId(): Int = R.layout.activity_flash

    override fun initSaveInstanceState(savedInstanceState: Bundle?) {
    }

    override fun initView() {
        flash.postDelayed( {
            if(Build.VERSION.SDK_INT >= 23){
                showPermissions()
            }
        },2000)
    }

    override fun initData() {

    }

    fun showPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            this.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.CAMERA
                    ),
                    BAIDU_READ_PHONE_STATE
                )
            }
        }else{
            newIntent<MainActivity>()
            finish()
        }
    }

    //Android6.0申请权限的回调方法
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            BAIDU_READ_PHONE_STATE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                newIntent<MainActivity>()
                finish()
            } else {
                // 没有获取到权限，做特殊处理
                showToast("没有获取到权限")
                newIntent<MainActivity>()
                finish()
            }
        }
    }
}
