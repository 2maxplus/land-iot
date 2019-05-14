package com.hyf.iot.presenter

import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.RESULT_SUCCESS
import com.hyf.iot.common.ex.subscribeEx
import com.hyf.iot.contract.PumpRoomContract
import com.hyf.iot.presenter.base.BaseRxLifePresenter
import com.hyf.iot.protocol.http.IUserHttpProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mvp.ljb.kt.client.HttpFactory

/**
 * 泵站信息（变频柜/泵）
 */
class PumpRoomPresenter : BaseRxLifePresenter<PumpRoomContract.IView>(),
        PumpRoomContract.IPresenter {

    override fun getPumpInfo() {
        getDataFromNet()
    }

    private fun getDataFromNet() {
        HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .getPumpRoomByName()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx(
                        {
                            when (it.code) {
                                RESULT_SUCCESS -> getMvpView().showPage(it.data)
                                214,215,216 -> {
                                    LoginUser.token = ""
                                    getMvpView().onTokenExpired(it.msg)
                                }
                            }
                        },
                        { getMvpView().errorPage(it) }
                ).bindRxLifeEx(RxLife.ON_DESTROY)
    }


}