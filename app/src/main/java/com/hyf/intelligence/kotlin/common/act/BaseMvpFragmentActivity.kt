package com.hyf.intelligence.kotlin.common.act

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import mvp.ljb.kt.contract.IPresenterContract
import com.hyf.intelligence.kotlin.common.listener.FragmentOnBackListener
import mvp.ljb.kt.view.MvpFragmentActivity

abstract class BaseMvpFragmentActivity<out P : IPresenterContract> : MvpFragmentActivity<P>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        init(savedInstanceState)
        initView()
        initData()
    }

    protected abstract fun getLayoutId(): Int

    protected open fun init(savedInstanceState: Bundle?) {}

    protected open fun initView() {}

    protected open fun initData() {}

    override fun onBackPressed() {
        val fragmentList = supportFragmentManager.fragments
        if (fragmentList != null && fragmentList.size != 0) {
            for (fragment in fragmentList) {
                if (fragment is FragmentOnBackListener && fragment.isVisible && fragment.onBackPressed()) {
                    return
                }
            }
        }
        onBack()
    }

    protected open fun onBack() {
        super.onBackPressed()
    }

    override fun getResources(): Resources {
        val res = super.getResources()
        if (res.configuration.fontScale != 1.0f) {
            val newConfig = Configuration()
            newConfig.setToDefaults()
            res.updateConfiguration(newConfig, res.displayMetrics)
        }
        return res
    }


}