package com.hyf.iot

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.hyf.iot.common.HttpDomain.HTTP_API_DOMAIN
import com.hyf.iot.common.LoginUser
import com.hyf.iot.protocol.http.OKHttpUpdateHttpService
import com.hyf.iot.utils.SPUtils
import com.squareup.leakcanary.LeakCanary
import com.ljb.kt.HttpConfig
import com.xuexiang.xupdate.XUpdate
import com.xuexiang.xupdate.utils.UpdateUtils
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
//        LeakCanary.install(this)
        SPUtils.init(this)
        closeAndroidPDialog()
        initNet()
        initXUpdate()
        registerActivity()

    }

    private fun initNet() {
        val headerMap: MutableMap<String,String> = mutableMapOf("X-Requested-With" to "XMLHttpRequest")
        if(LoginUser.token.isNotBlank()) {
            headerMap["token"] = LoginUser.token
        }
        Log.i("token=====",LoginUser.token)
        HttpConfig.init(HTTP_API_DOMAIN, headerMap, null, true)
    }

    fun initXUpdate(){
        XUpdate.get()
                .isWifiOnly(false)     //默认设置只在wifi下检查版本更新
                .isGet(false)          //默认设置使用get请求检查版本
//                .isAutoMode(true)    //默认设置非自动模式，可根据具体使用配置
                .param("VersionCode", UpdateUtils.getVersionCode(this)) //设置默认公共请求参数
                .param("AppKey", packageName)
                .setOnUpdateFailureListener { //设置版本更新出错的监听
//                    showToast(it.toString())
                }
                .setIUpdateHttpService(OKHttpUpdateHttpService()) //这个必须设置！实现网络请求功能。
                .init(this)
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

    /**
     * 禁止弹窗
     * */
    @SuppressLint("PrivateApi")
    private fun closeAndroidPDialog() {
        if (Build.VERSION.SDK_INT < 28)return
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