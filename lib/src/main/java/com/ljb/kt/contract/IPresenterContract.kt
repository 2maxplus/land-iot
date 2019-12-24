package com.ljb.kt.contract

/**
 * 公共Presenter层契约接口
 * */
interface IPresenterContract {
    fun onCreate()

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onDestroy()

    fun registerMvpView(mvpView: IViewContract)
}