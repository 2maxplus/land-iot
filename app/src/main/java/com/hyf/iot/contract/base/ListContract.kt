package com.hyf.iot.contract.base

import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract


interface ListContract {

    interface IView<T> : IViewContract {
        fun showPage(data: MutableList<T>, page: Int)
        fun errorPage(t: Throwable, page: Int)
    }

    interface IPresenter : IPresenterContract {
        fun onRefresh()
        fun onLoadMore()
    }

}

