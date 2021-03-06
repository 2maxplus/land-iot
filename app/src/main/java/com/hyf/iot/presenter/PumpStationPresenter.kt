package com.hyf.iot.presenter

import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.RESULT_SUCCESS
import com.hyf.iot.common.ex.subscribeEx
import com.hyf.iot.contract.PumpStationContract
import com.hyf.iot.presenter.base.BaseRxLifePresenter
import com.hyf.iot.protocol.http.IUserHttpProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.ljb.kt.client.HttpFactory

/**
 * 泵站信息（变频柜/泵）
 */
class PumpStationPresenter : BaseRxLifePresenter<PumpStationContract.IView>(),
        PumpStationContract.IPresenter {

    override fun getPumpStationListByFarmId(farmId: String) {
        HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .getPumpStationListByFarmId(farmId)
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
                                else -> getMvpView().errorPage(it.msg)
                            }
                        },
                        { getMvpView().errorPage(it.message) }
                ).bindRxLifeEx(RxLife.ON_DESTROY)
    }


}