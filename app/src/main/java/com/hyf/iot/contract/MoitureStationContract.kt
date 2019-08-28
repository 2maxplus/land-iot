package com.hyf.iot.contract

import com.hyf.iot.domain.devices.MoistureStationMassif
import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract

/**
 *
 */
interface MoitureStationContract {

    interface IView:  IViewContract{
        fun showPage(data: MutableList<MoistureStationMassif>)
        fun errorPage(msg: String?)
    }

    interface IPresenter : IPresenterContract{
        fun getMoistureMassifListByFarmId(farmId: String)
    }

}