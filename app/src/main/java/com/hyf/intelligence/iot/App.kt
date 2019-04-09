package com.hyf.intelligence.iot

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.hyf.intelligence.iot.common.HTTP_API_DOMAIN_DEBUG
import com.hyf.intelligence.iot.common.HTTP_API_DOMAIN_RELEASE
import com.hyf.intelligence.iot.common.LoginUser
import com.hyf.intelligence.iot.utils.SPUtils
import com.squareup.leakcanary.LeakCanary
import net.ljb.kt.HttpConfig
import java.util.HashSet



class App : Application() {
    private var allActivities: HashSet<Activity>? = null

    private val DEBUG = false

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
//        LeakCanary.install(this)
        SPUtils.init(this)
        closeAndroidPDialog()
        initNet()
        registerActivity()
    }

    private fun initNet() {
        var paramMap: Map<String,String>? = null
        if(LoginUser.token.isNotBlank()) {
            paramMap = mapOf("token" to LoginUser.token)
        }
        HttpConfig.init(if(DEBUG) HTTP_API_DOMAIN_DEBUG else HTTP_API_DOMAIN_RELEASE, null, paramMap, true)
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

    @SuppressLint("PrivateApi")
    private fun closeAndroidPDialog() {
        try {
            val aClass = Class.forName("android.content.pm.PackageParser\$Package")
            val declaredConstructor = aClass.getDeclaredConstructor(String::class.java)
            declaredConstructor.isAccessible = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            val cls = Class.forName("android.app.ActivityThread")
            val declaredMethod = cls.getDeclaredMethod("currentActivityThread")
            declaredMethod.isAccessible = true
            val activityThread = declaredMethod.invoke(null)
            val mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown")
            mHiddenApiWarningShown.isAccessible = true
            mHiddenApiWarningShown.setBoolean(activityThread, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}