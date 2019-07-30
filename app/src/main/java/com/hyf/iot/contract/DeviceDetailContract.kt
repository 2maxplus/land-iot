package com.hyf.iot.contract

import com.hyf.iot.domain.device.DeviceItem
import com.hyf.iot.domain.device.ValveUseTime
import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract

interface DeviceDetailContract {

    interface IView: IViewContract {
        fun showPage(data: MutableList<ValveUseTime>)
        fun showDetailPage(data: DeviceItem)
        fun errorPage(msg: String?)
    }

    interface IPresenter : IPresenterContract{
        fun getValveUseTimesById(id: String)
        fun getDeviceDetailById(id: String)
    }

}