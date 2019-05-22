package com.hyf.iot.presenter

import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.RESULT_SUCCESS
import com.hyf.iot.common.ex.subscribeEx
import com.hyf.iot.contract.FarmDetailContract
import com.hyf.iot.contract.FarmListContract
import com.hyf.iot.presenter.base.BaseRxLifePresenter
import com.hyf.iot.protocol.http.IReposHttpProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.ljb.kt.client.HttpFactory

/**
 * 农场新建与更新
 */
class FarmDetailPresenter : BaseRxLifePresenter<FarmDetailContract.IView>(),
        FarmDetailContract.IPresenter {
    override fun getFarmDetail(id: String) {
        HttpFactory.getProtocol(IReposHttpProtocol::class.java)
                .getFarmDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx(
                        {
                            when(it.code){
                                RESULT_SUCCESS -> getMvpView().showDetail(it.data)
                                214,215,216 -> {
                                    LoginUser.token = ""
                                    getMvpView().onTokenExpired(it.msg)
                                }
                                else -> getMvpView().onError(it.msg)
                            }
                        },
                        { getMvpView().onError(it.message)}
                ).bindRxLifeEx(RxLife.ON_DESTROY)
    }


}