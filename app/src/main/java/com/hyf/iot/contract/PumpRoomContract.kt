package com.hyf.iot.contract

import com.hyf.iot.domain.pumb.PumpRoom
import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract

/**
 *
 */
interface PumpRoomContract {

    interface IView:  IViewContract{
        fun showPage(data: PumpRoom)
        fun errorPage(msg: String?)
    }

    interface IPresenter : IPresenterContract{
        fun getPumpInfo(farmId: String)
    }

}