package com.hyf.iot.contract

import com.hyf.iot.domain.farm.Massif
import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract

/**
 * Created by L on 2017/7/18.
 */
interface MassifListContract {

    interface IView : IViewContract {
        fun onError(errorMsg: String?)
        fun showPageList(data: MutableList<Massif>)
    }

    interface IPresenter : IPresenterContract {
        fun getMassifList(farmId: String)
    }
}