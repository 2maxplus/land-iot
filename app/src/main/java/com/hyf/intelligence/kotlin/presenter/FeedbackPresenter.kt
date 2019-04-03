package com.hyf.intelligence.kotlin.presenter

import com.hyf.intelligence.kotlin.common.ex.subscribeEx
import com.hyf.intelligence.kotlin.contract.FeedbackContract
import com.hyf.intelligence.kotlin.presenter.base.BaseRxLifePresenter
import com.hyf.intelligence.kotlin.protocol.http.IUserHttpProtocol
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
                    if (it.code == 200) {
                        getMvpView().feedbackSuccess(it.msg)
                    }else{
                        getMvpView().feedbackError(it.msg)
                    }
                })
                .bindRxLifeEx(RxLife.ON_DESTROY)
    }

}