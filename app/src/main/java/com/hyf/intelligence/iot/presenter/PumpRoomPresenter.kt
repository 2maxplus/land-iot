package com.hyf.intelligence.iot.presenter

import com.hyf.intelligence.iot.common.LoginUser
import com.hyf.intelligence.iot.common.ex.subscribeEx
import com.hyf.intelligence.iot.contract.PumpRoomContract
import com.hyf.intelligence.iot.presenter.base.BaseRxLifePresenter
import com.hyf.intelligence.iot.protocol.http.IUserHttpProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.ljb.kt.client.HttpFactory

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
                                200 -> getMvpView().showPage(it.data)
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