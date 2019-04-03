package com.hyf.intelligence.kotlin.contract

import mvp.ljb.kt.contract.IPresenterContract
import mvp.ljb.kt.contract.IViewContract
import com.hyf.intelligence.kotlin.domain.user.UserInfo

interface FeedbackContract {

    interface IView : IViewContract {
        fun feedbackSuccess(msg:String)
        fun feedbackError(errorMsg: String?)
    }

    interface IPresenter : IPresenterContract {
        fun feedbackAdd(content: String)
    }
}