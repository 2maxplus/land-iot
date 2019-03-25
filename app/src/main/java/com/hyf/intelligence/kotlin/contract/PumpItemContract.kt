package com.hyf.intelligence.kotlin.contract

import com.hyf.intelligence.kotlin.domain.pumb.WaterPump
import mvp.ljb.kt.contract.IPresenterContract
import mvp.ljb.kt.contract.IViewContract

/**
 * Created 2019/03/25
 *
 */
interface PumpItemContract {

    interface IView:  IViewContract{
        fun showPage(data: WaterPump)
        fun errorPage(t: Throwable)
    }

    interface IPresenter : IPresenterContract{
        fun getPumpItemInfo()
    }

}