package com.hyf.iot.contract

import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract

/**
 *
 */
interface FarmContract {

    interface IView : IViewContract{
        fun onError(errorMsg: String?)
        fun addSuccess()
        fun editSuccess()
    }

    interface IPresenter : IPresenterContract{
        fun farmAdd(name: String,address: String,linkMan: String,linkPhone:String,latitude: Double,longitude: Double,province: String,city: String,district: String)
        fun farmEdit(name: String,address: String,linkMan: String,linkPhone:String,latitude: Double,longitude: Double,province: String,city: String,district: String,id:String)
    }
}