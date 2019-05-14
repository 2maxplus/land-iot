package com.hyf.iot.contract

import mvp.ljb.kt.contract.IPresenterContract
import mvp.ljb.kt.contract.IViewContract
import com.hyf.iot.domain.User

/**
 * Created by L on 2017/7/18.
 */
interface MyContract {

    interface IView : IViewContract {
        fun logoutSuccess()
        fun showUserInfo(user: User)
    }

    interface IPresenter : IPresenterContract {
        fun logout()
        fun getUserInfo()
    }
}