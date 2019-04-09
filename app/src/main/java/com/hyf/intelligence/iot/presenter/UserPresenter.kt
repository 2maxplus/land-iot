package com.hyf.intelligence.iot.presenter

import com.hyf.intelligence.iot.common.LoginUser
import com.hyf.intelligence.iot.common.ex.subscribeEx
import com.hyf.intelligence.iot.contract.UserContract
import com.hyf.intelligence.iot.presenter.base.BaseRxLifePresenter
import com.hyf.intelligence.iot.protocol.http.IUserHttpProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.ljb.kt.client.HttpFactory
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by L on 2017/7/18.
 */
class UserPresenter : BaseRxLifePresenter<UserContract.IView>(),
        UserContract.IPresenter {

    override fun modifyPortrait(path: String) {
        val fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), File(path))
        val requestImgPart = MultipartBody.Part.createFormData("file", File(path).name, fileRequestBody)
        HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .modifyPortrait(requestImgPart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx({
                    if (it.code == 200) {
                        getMvpView().portraitModifySuccess(it.data)
                    }else{
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
                    when(it.code){
                        200 -> getMvpView().showUserInfo(it.data)
                            214,215,216 ->{LoginUser.token = ""
                            getMvpView().onTokenExpired(it.msg)}
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
                    if (it.code == 200) {
                        LoginUser.token = ""
                        getMvpView().logoutSuccess()
                    }else{
                        getMvpView().onError(it.msg)
                    }
                },{getMvpView().onError(it.message)})
                .bindRxLifeEx(RxLife.ON_DESTROY)
    }


}