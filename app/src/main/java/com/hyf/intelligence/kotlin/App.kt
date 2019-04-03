package com.hyf.intelligence.kotlin

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.hyf.intelligence.kotlin.common.HTTP_API_DOMAIN
import com.hyf.intelligence.kotlin.utils.SPUtils
import com.squareup.leakcanary.LeakCanary
import net.ljb.kt.HttpConfig
import java.util.HashSet

class App : Application() {
    private var allActivities: HashSet<Activity>? = null

    companion object {
        lateinit var instance: App
    }

    //应避免创建全局的Application引用
    override fun onCreate() {
        super.onCreate()
        instance = this
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
        registerActivity()
    }

    private fun initNet() {
        HttpConfig.init(HTTP_API_DOMAIN, null, null, true)
    }

    private fun registerActivity(){
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks{
            override fun onActivityPaused(p0: Activity?) {
            }

            override fun onActivityResumed(p0: Activity?) {
            }

            override fun onActivityStarted(p0: Activity?) {
            }

            override fun onActivityDestroyed(p0: Activity?) {
                p0?.let { removeActivity(it) }
            }

            override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
            }

            override fun onActivityStopped(p0: Activity?) {
            }

            override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
                p0?.let { addActivity(it) }
            }
        })
    }

    fun addActivity(act: Activity) {
        if (allActivities == null) {
            allActivities = HashSet()
        } else {
            allActivities?.add(act)
        }
    }

    fun removeAllActivity(){
        allActivities?.forEach {
            it.finish()
        }
    }

    fun removeActivity(act: Activity) {
        allActivities?.remove(act)
    }

    @Synchronized fun exitApp() {
        allActivities?.let {
            for (act in it) {
                act.finish()
            }
        }
        allActivities?.clear()
//        aCache.clear()
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(0)
    }
}