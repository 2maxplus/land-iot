package com.hyf.iot.contract

import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract

interface FeedbackContract {

    interface IView : IViewContract {
        fun feedbackSuccess(msg:String)
        fun feedbackError(errorMsg: String?)
    }

    interface IPresenter : IPresenterContract {
        fun feedbackAdd(content: String)
    }
}