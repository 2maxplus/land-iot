package com.hyf.iot.contract

import com.ljb.kt.contract.IPresenterContract
import com.ljb.kt.contract.IViewContract
import com.hyf.iot.domain.user.UserInfo

interface UserContract {

    interface IView : IViewContract {
        fun logoutSuccess()
        fun showUserInfo(user: UserInfo)
        fun showPortrait(url: String)
        fun portraitModifySuccess(headData: String)
        fun onError(errorMsg: String?)
    }

    interface IPresenter : IPresenterContract {
        fun logout()
        fun getUserInfo()
        fun getPortrait()
        fun modifyPortrait(path: String)
    }
}