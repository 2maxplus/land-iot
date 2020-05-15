package com.hyf.iot.contract

import com.hyf.iot.domain.plan.Plan
import com.hyf.iot.domain.plan.PlanDetail
import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract

interface PlanParentsContract {

    interface IView : IViewContract {
        fun onError(errorMsg: String?)
        fun onSuccess(msg: String)
        fun showPageList(data: MutableList<Plan>)
    }

    interface IPresenter : IPresenterContract {
        fun getPlanList(farmId: String, state: String)
        fun setPlanStart(id: String)
        fun setPlanStop(id: String)
        fun setPlanSuspend(id: String)
        fun setPlanContinue(id: String)
    }
}