package com.hyf.intelligence.iot.presenter

import com.hyf.intelligence.iot.common.LoginUser
import com.hyf.intelligence.iot.common.ex.subscribeEx
import com.hyf.intelligence.iot.contract.DeviceCoordinatesContract
import com.hyf.intelligence.iot.presenter.base.BaseRxLifePresenter
import com.hyf.intelligence.iot.protocol.http.IUserHttpProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.ljb.kt.client.HttpFactory

class DeviceCoordinatesPresenter: BaseRxLifePresenter<DeviceCoordinatesContract.IView>()
        ,DeviceCoordinatesContract.IPresenter {

    override fun getCoordinates() {
        HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .getCoordinates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx(
                        {
                            when(it.code){
                                200 -> getMvpView().showPage(it.data)
                                214,215,216 -> {
                                    LoginUser.token = ""
                                    getMvpView().onTokenExpired(it.msg)
                                }
                            }
                        },
                        { getMvpView().errorPage(it)}
                ).bindRxLifeEx(RxLife.ON_DESTROY)
    }
}