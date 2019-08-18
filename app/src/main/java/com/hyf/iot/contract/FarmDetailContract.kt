package com.hyf.iot.contract

import com.hyf.iot.domain.farm.Massif

interface FarmDetailContract {

    interface IView : FarmDetailByIdContract.IView {
        fun onError(errorMsg: String?)
//        fun showFarmDetail(data: Farm)
        fun showMassifList(data: MutableList<Massif>)
    }

    interface IPresenter : FarmDetailByIdContract.IPresenter {
//        fun getFarmDetail(id: String)
        fun getMassifList(farmId: String)
    }
}