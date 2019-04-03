package com.hyf.intelligence.kotlin.presenter

import com.hyf.intelligence.kotlin.common.ex.subscribeEx
import com.hyf.intelligence.kotlin.contract.DeviceDetailContract
import com.hyf.intelligence.kotlin.domain.base.GenResult
import com.hyf.intelligence.kotlin.domain.device.ValveUseTime
import com.hyf.intelligence.kotlin.presenter.base.BaseRxLifePresenter
import com.hyf.intelligence.kotlin.protocol.http.IUserHttpProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.ljb.kt.client.HttpFactory

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
                        { getMvpView().showDetailPage(it.data)},
                        { getMvpView().errorPage(it)}
                ).bindRxLifeEx(RxLife.ON_DESTROY)
    }

    override fun getValveUseTimesById(id: String) {
        HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .getValveUseTimesById(id)
                .filter{ result: GenResult<MutableList<ValveUseTime>> -> result.code == 200 }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx(
                        { getMvpView().showPage(it.data)},
                        { getMvpView().errorPage(it)}
                ).bindRxLifeEx(RxLife.ON_DESTROY)
    }



}