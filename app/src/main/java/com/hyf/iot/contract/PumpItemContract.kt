package com.hyf.iot.contract

import com.hyf.iot.domain.device.WaterPump
import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract

interface PumpItemContract {

    interface IView: IViewContract {
        fun showPage(data: MutableList<WaterPump>)
        fun errorPage(msg: String?)
    }

    interface IPresenter : IPresenterContract{
        fun getPumpItemInfo(farmId: String)
    }

}