package com.hyf.iot.presenter

import com.hyf.iot.common.HttpDomain.HTTP_API_DOMAIN
import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.RESULT_SUCCESS
import com.hyf.iot.common.ex.subscribeEx
import com.hyf.iot.contract.UserContract
import com.hyf.iot.presenter.base.BaseRxLifePresenter
import com.hyf.iot.protocol.http.IUserHttpProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.ljb.kt.client.HttpFactory
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UserPresenter : BaseRxLifePresenter<UserContract.IView>(),
        UserContract.IPresenter {
    override fun getPortrait() {
        HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .getPortrait()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx({
                    when (it.code) {
                        RESULT_SUCCESS -> getMvpView().showPortrait(HTTP_API_DOMAIN + it.data)
                        214, 215, 216 -> {
                            LoginUser.token = ""
                            getMvpView().onTokenExpired(it.msg)
                        }
                        else -> getMvpView().onError(it.msg)
                    }
                })
                .bindRxLifeEx(RxLife.ON_DESTROY)
    }

    override fun modifyPortrait(path: String) {
        val fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), File(path))
        val requestImgPart = MultipartBody.Part.createFormData("file", File(path).name, fileRequestBody)
        HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .modifyPortrait(requestImgPart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx({
                    if (it.code == RESULT_SUCCESS) {
                        getMvpView().portraitModifySuccess(HTTP_API_DOMAIN + it.data)
                    } else {
                        getMvpView().onError(it.msg)
                    }
                })
                .bindRxLifeEx(RxLife.ON_DESTROY)
    }

    override fun getUserInfo() {
        HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx({
                    when (it.code) {
                        RESULT_SUCCESS -> getMvpView().showUserInfo(it.data)
                        214, 215, 216 -> {
                            LoginUser.token = ""
                            getMvpView().onTokenExpired(it.msg)
                        }
                        else -> getMvpView().onError(it.msg)
                    }
                })
                .bindRxLifeEx(RxLife.ON_DESTROY)
    }


    override fun logout() {
        HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .userLogout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx({
                    if (it.code == RESULT_SUCCESS) {
                        LoginUser.token = ""
                        getMvpView().logoutSuccess()
                    } else {
                        getMvpView().onError(it.msg)
                    }
                }, { getMvpView().onError(it.message) })
                .bindRxLifeEx(RxLife.ON_DESTROY)
    }


}