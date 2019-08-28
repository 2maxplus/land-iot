package com.hyf.iot.contract

import com.hyf.iot.domain.device.DeviceInfo
import com.hyf.iot.domain.devices.ValveUseTime
import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract

interface DeviceDetailContract {

    interface IView: IViewContract {
        fun showPage(data: MutableList<ValveUseTime>)
        fun showDetailPage(data: DeviceInfo)
        fun errorPage(msg: String?)
    }

    interface IPresenter : IPresenterContract{
        fun getValveUseTimesById(id: String)
        fun getDeviceDetailById(id: String)
    }

}