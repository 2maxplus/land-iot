package com.hyf.iot.presenter

import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.RESULT_SUCCESS
import com.hyf.iot.common.ex.subscribeEx
import com.hyf.iot.contract.FeedbackContract
import com.hyf.iot.presenter.base.BaseRxLifePresenter
import com.hyf.iot.protocol.http.IUserHttpProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import mvp.ljb.kt.client.HttpFactory

/**
 * Created by L on 2017/7/18.
 */
class FeedbackPresenter : BaseRxLifePresenter<FeedbackContract.IView>(),
        FeedbackContract.IPresenter {
    override fun feedbackAdd(content: String) {
        HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .feedbackAdd(content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx({
                    when(it.code){
                        RESULT_SUCCESS -> getMvpView().feedbackSuccess(it.msg)
                        214,215,216 -> {
                            LoginUser.token = ""
                            getMvpView().onTokenExpired(it.msg)
                        }
                        else -> getMvpView().feedbackError(it.msg)
                    }
                })
                .bindRxLifeEx(RxLife.ON_DESTROY)
    }

}