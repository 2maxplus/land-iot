package com.hyf.iot.contract

import com.hyf.iot.domain.pumb.PumpStation
import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract

/**
 *
 */
interface PumpStationContract {

    interface IView:  IViewContract{
        fun showPage(data: MutableList<PumpStation>)
        fun errorPage(msg: String?)
    }

    interface IPresenter : IPresenterContract{
        fun getPumpStationListByFarmId(farmId: String)
    }

}