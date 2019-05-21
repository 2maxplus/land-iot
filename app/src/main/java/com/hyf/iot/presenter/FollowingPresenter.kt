package com.hyf.iot.presenter

import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.ex.subscribeEx
import com.hyf.iot.contract.FollowingContract
import com.hyf.iot.presenter.base.BaseRxLifePresenter
import com.hyf.iot.protocol.http.IUserHttpProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.ljb.kt.client.HttpFactory

/**
 * Created by L on 2017/10/9.
 */
class FollowingPresenter : BaseRxLifePresenter<FollowingContract.IView>(),
        FollowingContract.IPresenter {


    private var mPage = 1

    override fun onLoadMore() {
        mPage++
        getDataFromNet(mPage)
    }

    override fun onRefresh() {
        mPage = 1
        getDataFromNet(mPage)
    }

    private fun getDataFromNet(page: Int) {
        HttpFactory.getProtocol(IUserHttpProtocol::class.java).getFollowingByName(LoginUser.name, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx(
                        { getMvpView().showPage(it, page) },
                        { getMvpView().errorPage(it, page) }
                ).bindRxLifeEx(RxLife.ON_DESTROY)
    }

}