package com.hyf.iot.contract

import com.hyf.iot.domain.pumb.PumpControlStations
import mvp.ljb.kt.contract.IPresenterContract
import mvp.ljb.kt.contract.IViewContract

/**
 *
 */
interface PumpRoomContract {

    interface IView:  IViewContract{
        fun showPage(data: PumpControlStations)
        fun errorPage(t: Throwable)
    }

    interface IPresenter : IPresenterContract{
        fun getPumpInfo()
    }

}