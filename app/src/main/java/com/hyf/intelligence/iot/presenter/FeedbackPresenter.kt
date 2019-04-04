package com.hyf.intelligence.iot.presenter

import com.hyf.intelligence.iot.common.LoginUser
import com.hyf.intelligence.iot.common.ex.subscribeEx
import com.hyf.intelligence.iot.contract.FeedbackContract
import com.hyf.intelligence.iot.presenter.base.BaseRxLifePresenter
import com.hyf.intelligence.iot.protocol.http.IUserHttpProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.ljb.kt.client.HttpFactory

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
                        200 -> getMvpView().feedbackSuccess(it.msg)
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