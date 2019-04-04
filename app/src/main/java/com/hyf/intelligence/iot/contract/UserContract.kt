package com.hyf.intelligence.iot.contract

import mvp.ljb.kt.contract.IPresenterContract
import mvp.ljb.kt.contract.IViewContract
import com.hyf.intelligence.iot.domain.user.UserInfo

/**
 * Created by L on 2017/7/18.
 */
interface UserContract {

    interface IView : IViewContract {
        fun logoutSuccess()
        fun logoutError(errorMsg: String?)
        fun showUserInfo(user: UserInfo)
    }

    interface IPresenter : IPresenterContract {
        fun logout()
        fun getUserInfo()
    }
}