package com.hyf.intelligence.kotlin.presenter

import com.hyf.intelligence.kotlin.common.ex.subscribeEx
import com.hyf.intelligence.kotlin.contract.PumpItemContract
import com.hyf.intelligence.kotlin.presenter.base.BaseRxLifePresenter
import com.hyf.intelligence.kotlin.protocol.http.IUserHttpProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.ljb.kt.client.HttpFactory

/**
 * 泵站信息item
 */
class PumpItemPresenter : BaseRxLifePresenter<PumpItemContract.IView>(),
        PumpItemContract.IPresenter {

    override fun getPumpItemInfo() {
        getDataFromNet()
    }

    private fun getDataFromNet() {
        HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .getPumpRoomByName()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx(
                        { },
                        { }
                ).bindRxLifeEx(RxLife.ON_DESTROY)
    }


}