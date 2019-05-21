package com.hyf.iot.contract

import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract

/**
 * 登录页View层\Presenter层通讯接口
 * Created by L on 2017/7/13.
 */
interface LoginContract {

    interface IView : IViewContract {
        fun loginSuccess()
        fun loginError(errorMsg: String?)
        fun goHome()
        fun getCodeSuccess(loginId: String)
        fun getCodeError(errorMsg: String?)
    }

    interface IPresenter : IPresenterContract {
        fun login(loginId: String,code: String)
        fun delayGoHomeTask()
        fun getCode(phone: String)
    }
}