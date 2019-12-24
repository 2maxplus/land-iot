package com.ljb.kt.presenter

import com.ljb.kt.contract.IViewContract

interface IBasePresenter<out V : IViewContract> {
    fun getMvpView(): V
}




