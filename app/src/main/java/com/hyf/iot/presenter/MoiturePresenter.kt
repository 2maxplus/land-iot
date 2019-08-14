package com.hyf.iot.presenter

import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.RESULT_SUCCESS
import com.hyf.iot.common.ex.subscribeEx
import com.hyf.iot.contract.MoitureStationContract
import com.hyf.iot.presenter.base.BaseRxLifePresenter
import com.hyf.iot.protocol.http.IUserHttpProtocol
import com.ljb.kt.client.HttpFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 阀控
 * 获取地块分类的设备列表
 *
 */
class MoiturePresenter : BaseRxLifePresenter<MoitureStationContract.IView>(),
        MoitureStationContract.IPresenter {

    override fun getMoistureMassifListByFarmId(farmId: String) {
        HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .getMoistureMassifListByFarmId(farmId)
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