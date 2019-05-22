package com.hyf.iot.presenter

import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.RESULT_SUCCESS
import com.hyf.iot.common.ex.subscribeEx
import com.hyf.iot.contract.MassifListContract
import com.hyf.iot.presenter.base.BaseRxLifePresenter
import com.hyf.iot.protocol.http.IReposHttpProtocol
import com.ljb.kt.client.HttpFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 地块新建与更新
 */
class MassifListPresenter : BaseRxLifePresenter<MassifListContract.IView>(),
        MassifListContract.IPresenter {
    override fun getMassifList(id: String) {
        HttpFactory.getProtocol(IReposHttpProtocol::class.java)
                .getMassifList(id,"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx(
                        {
                            when(it.code){
                                RESULT_SUCCESS -> getMvpView().showPageList(it.data)
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