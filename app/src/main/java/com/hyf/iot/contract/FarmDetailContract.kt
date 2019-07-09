package com.hyf.iot.contract

import com.hyf.iot.domain.farm.Farm
import com.hyf.iot.domain.farm.Massif
import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract
import java.util.ArrayList

interface FarmDetailContract {

    interface IView : IViewContract {
        fun onError(errorMsg: String?)
        fun showFarmDetail(data: Farm)
        fun showMassifList(data: MutableList<Massif>)
    }

    interface IPresenter : IPresenterContract {
        fun getFarmDetail(id: String)
        fun getMassifList(farmId: String)
    }
}