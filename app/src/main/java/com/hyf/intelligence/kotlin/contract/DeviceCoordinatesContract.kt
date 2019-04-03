package com.hyf.intelligence.kotlin.contract

import com.hyf.intelligence.kotlin.domain.DeviceCoordinates
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