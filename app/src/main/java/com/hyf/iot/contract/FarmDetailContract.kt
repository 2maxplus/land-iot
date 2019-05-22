package com.hyf.iot.contract

import com.hyf.iot.domain.farm.Farm
import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract
import java.util.ArrayList

/**
 * Created by L on 2017/7/18.
 */
interface FarmDetailContract {

    interface IView : IViewContract {
        fun onError(errorMsg: String?)
        fun showDetail(data: Farm)
    }

    interface IPresenter : IPresenterContract {
        fun getFarmDetail(id: String)
    }
}