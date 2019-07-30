package com.hyf.iot.contract

import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract
import com.hyf.iot.domain.User

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