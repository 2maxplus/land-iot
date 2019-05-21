package com.hyf.iot.contract

import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract

/**
 * Created by L on 2017/7/18.
 */
interface FarmListContract {

    interface IView : IViewContract {
        fun onError(errorMsg: String?)
        fun showPageList()
    }

    interface IPresenter : IPresenterContract {
        fun getFarmList()
    }
}