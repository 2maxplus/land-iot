package com.hyf.intelligence.kotlin

import android.app.Application
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.hyf.intelligence.kotlin.common.HTTP_API_DOMAIN
import com.hyf.intelligence.kotlin.utils.SPUtils
import com.squareup.leakcanary.LeakCanary
import net.ljb.kt.HttpConfig

/**
 * Created by L on 2017/7/14.
 */
class App : Application() {

    //应避免创建全局的Application引用
    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this)
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL)
        LeakCanary.install(this)
        SPUtils.init(this)
        initNet()
    }

    private fun initNet() {
//        val paramMap = mapOf(
//                "client_id" to CLIENT_ID,
//                "client_secret" to CLIENT_SECRET)
        HttpConfig.init(HTTP_API_DOMAIN, null, null, true)
    }
}