package com.hyf.intelligence.iot.contract

import mvp.ljb.kt.contract.IPresenterContract
import mvp.ljb.kt.contract.IViewContract
import com.hyf.intelligence.iot.domain.user.UserInfo
import okhttp3.MultipartBody

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