package com.hyf.iot.presenter

import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.RESULT_SUCCESS
import com.hyf.iot.common.ex.subscribeEx
import com.hyf.iot.contract.DeviceDetailContract
import com.hyf.iot.domain.base.GenResult
import com.hyf.iot.domain.device.ValveUseTime
import com.hyf.iot.presenter.base.BaseRxLifePresenter
import com.hyf.iot.protocol.http.IUserHttpProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mvp.ljb.kt.client.HttpFactory

/**
 * 设备详情（阀控开关时长）
 */
class DeviceDetailPresenter : BaseRxLifePresenter<DeviceDetailContract.IView>(),
        DeviceDetailContract.IPresenter {

    override fun getDeviceDetailById(id: String) {
        HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .getDeviceDetailById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx(
                        {
                            when(it.code){
                                RESULT_SUCCESS -> getMvpView().showDetailPage(it.data)
                                214,215,216 -> {
                                    LoginUser.token = ""
                                    getMvpView().onTokenExpired(it.msg)
                                }
                            }
                        },
                        { getMvpView().errorPage(it.message)}
                ).bindRxLifeEx(RxLife.ON_DESTROY)
    }

    override fun getValveUseTimesById(id: String) {
        HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .getValveUseTimesById(id)
                .filter{ result: GenResult<MutableList<ValveUseTime>> -> result.code == 200 }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx(
                        {
                            when(it.code){
                                RESULT_SUCCESS -> {
                                    if(!it.data.isNullOrEmpty())
                                        getMvpView().showPage(it.data)
                                } else -> {
                                    getMvpView().errorPage(it.msg)
                                }
                            }
                        },
                        { getMvpView().errorPage(it.message)}
                ).bindRxLifeEx(RxLife.ON_DESTROY)
    }



}