package com.hyf.iot.contract

import com.hyf.iot.domain.plan.PlanDetail
import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract

interface PlanDetailContract {

    interface IView : IViewContract {
        fun onError(errorMsg: String?)
        fun showDetailList(data: PlanDetail)
    }

    interface IPresenter : IPresenterContract {
        fun getPlanDetail(id: String)
    }
}