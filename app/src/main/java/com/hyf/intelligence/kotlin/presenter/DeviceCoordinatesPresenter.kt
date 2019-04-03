package com.hyf.intelligence.kotlin.presenter

import com.hyf.intelligence.kotlin.common.ex.subscribeEx
import com.hyf.intelligence.kotlin.contract.DeviceCoordinatesContract
import com.hyf.intelligence.kotlin.presenter.base.BaseRxLifePresenter
import com.hyf.intelligence.kotlin.protocol.http.IUserHttpProtocol
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
                        { getMvpView().showPage(it.data)},
                        { getMvpView().errorPage(it)}
                ).bindRxLifeEx(RxLife.ON_DESTROY)
    }
}