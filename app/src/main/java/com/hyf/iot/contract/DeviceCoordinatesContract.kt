package com.hyf.iot.contract

import com.hyf.iot.domain.DeviceCoordinates
import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract

class DeviceCoordinatesContract {
    interface IView: IViewContract {
        fun showPage(data: MutableList<DeviceCoordinates>)
        fun errorPage(msg: String?)
    }

    interface IPresenter : IPresenterContract {
        fun getCoordinates()
    }
}