package com.hyf.intelligence.iot.contract

import com.hyf.intelligence.iot.domain.device.DeviceItem
import com.hyf.intelligence.iot.domain.device.ValveUseTime
import mvp.ljb.kt.contract.IPresenterContract
import mvp.ljb.kt.contract.IViewContract

/**
 * Created 2019/03/25
 *
 */
interface DeviceDetailContract {

    interface IView: IViewContract {
        fun showPage(data: MutableList<ValveUseTime>)
        fun showDetailPage(data: DeviceItem)
        fun errorPage(t: Throwable)
    }

    interface IPresenter : IPresenterContract{
        fun getValveUseTimesById(id: String)
        fun getDeviceDetailById(id: String)
    }

}