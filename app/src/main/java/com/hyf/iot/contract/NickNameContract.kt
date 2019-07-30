package com.hyf.iot.contract

import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract

interface NickNameContract {

    interface IView : IViewContract {
        fun modifySuccess()
        fun modifyError(errorMsg: String?)
    }

    interface IPresenter : IPresenterContract {
        fun modifyNickName(nickname: String)
    }
}