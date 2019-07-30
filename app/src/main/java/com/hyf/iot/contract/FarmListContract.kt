package com.hyf.iot.contract

import com.hyf.iot.domain.farm.Farm
import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract

interface FarmListContract {

    interface IView : IViewContract {
        fun onError(errorMsg: String?)
        fun showPageList(data: MutableList<Farm>)
    }

    interface IPresenter : IPresenterContract {
        fun getFarmList()
    }
}