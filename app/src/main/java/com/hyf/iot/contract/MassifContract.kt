package com.hyf.iot.contract

import com.baidu.mapapi.model.LatLng
import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract

/**
 *
 */
interface MassifContract {

    interface IView : IViewContract{
        fun onError(errorMsg: String?)
        fun addSuccess()
        fun editSuccess()
    }

    interface IPresenter : IPresenterContract{
        fun massifAdd(farmId: String, name: String,size: Float,massifCoordinates: ArrayList<LatLng>)
        fun massifEdit(farmId: String, name: String,size: Float,massifCoordinates: ArrayList<LatLng>,id:String)
    }
}