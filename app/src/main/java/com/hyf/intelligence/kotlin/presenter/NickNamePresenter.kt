package com.hyf.intelligence.kotlin.presenter

import com.hyf.intelligence.kotlin.common.ex.subscribeEx
import com.hyf.intelligence.kotlin.contract.NickNameContract
import com.hyf.intelligence.kotlin.presenter.base.BaseRxLifePresenter
import com.hyf.intelligence.kotlin.protocol.http.IUserHttpProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.ljb.kt.client.HttpFactory

/**
 * Created by L on 2017/7/18.
 */
class NickNamePresenter : BaseRxLifePresenter<NickNameContract.IView>(),
        NickNameContract.IPresenter {
    override fun modifyNickName(nickname: String) {
        HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .modifyNickName(nickname)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx({
                    if (it.code == 200) {
                        getMvpView().modifySuccess()
                    }else{
//                        getMvpView().logoutError(it.msg)
                    }
                })
                .bindRxLifeEx(RxLife.ON_DESTROY)
    }

}