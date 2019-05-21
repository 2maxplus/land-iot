package com.hyf.iot.contract

import com.hyf.iot.domain.device.DeviceItem
import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract

/**
 * Created 2019/03/25
 *
 */
interface PumpItemContract {

    interface IView: IViewContract {
        fun showPage(data: MutableList<DeviceItem>)
        fun errorPage(msg: String?)
    }

    interface IPresenter : IPresenterContract{
        fun getPumpItemInfo()
    }

}