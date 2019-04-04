package com.hyf.intelligence.iot.presenter

import com.hyf.intelligence.iot.common.LoginUser
import com.hyf.intelligence.iot.common.ex.subscribeEx
import com.hyf.intelligence.iot.contract.UserContract
import com.hyf.intelligence.iot.presenter.base.BaseRxLifePresenter
import com.hyf.intelligence.iot.protocol.http.IUserHttpProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.ljb.kt.client.HttpFactory

/**
 * Created by L on 2017/7/18.
 */
class UserPresenter : BaseRxLifePresenter<UserContract.IView>(),
        UserContract.IPresenter {

    override fun getUserInfo() {
        HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx({
                    if (it.code == 200) {
                        getMvpView().showUserInfo(it.data)
                    }else{
//                        getMvpView().logoutError(it.msg)
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
                        getMvpView().logoutError(it.msg)
                    }
                },{getMvpView().logoutError(it.message)})
                .bindRxLifeEx(RxLife.ON_DESTROY)
    }


}