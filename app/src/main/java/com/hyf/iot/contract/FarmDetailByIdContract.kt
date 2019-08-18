package com.hyf.iot.contract

import com.hyf.iot.domain.farm.Farm
import com.hyf.iot.domain.farm.Massif
import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract
import java.util.ArrayList

interface FarmDetailByIdContract {

    interface IView : IViewContract {
        fun showFarmDetail(data: Farm)
    }

    interface IPresenter : IPresenterContract {
        fun getFarmDetail(id: String)
    }
}