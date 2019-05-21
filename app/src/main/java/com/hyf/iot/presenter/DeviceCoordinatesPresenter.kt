package com.hyf.iot.presenter

import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.RESULT_SUCCESS
import com.hyf.iot.common.ex.subscribeEx
import com.hyf.iot.contract.DeviceCoordinatesContract
import com.hyf.iot.presenter.base.BaseRxLifePresenter
import com.hyf.iot.protocol.http.IUserHttpProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.ljb.kt.client.HttpFactory

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
                                RESULT_SUCCESS -> getMvpView().showPage(it.data)
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