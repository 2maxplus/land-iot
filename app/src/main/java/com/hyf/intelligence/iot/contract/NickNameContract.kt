package com.hyf.intelligence.iot.contract

import mvp.ljb.kt.contract.IPresenterContract
import mvp.ljb.kt.contract.IViewContract

/**
 * Created by L on 2017/7/18.
 */
interface NickNameContract {

    interface IView : IViewContract {
        fun modifySuccess()
        fun modifyError(errorMsg: String?)
    }

    interface IPresenter : IPresenterContract {
        fun modifyNickName(nickname: String)
    }
}