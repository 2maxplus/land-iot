package com.hyf.intelligence.iot.contract

import com.hyf.intelligence.iot.domain.DeviceCoordinates
import mvp.ljb.kt.contract.IPresenterContract
import mvp.ljb.kt.contract.IViewContract

class DeviceCoordinatesContract {
    interface IView: IViewContract {
        fun showPage(data: MutableList<DeviceCoordinates>)
        fun errorPage(t: Throwable)
    }

    interface IPresenter : IPresenterContract {
        fun getCoordinates()
    }
}