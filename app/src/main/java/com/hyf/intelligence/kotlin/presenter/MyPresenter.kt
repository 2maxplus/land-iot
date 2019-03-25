package com.hyf.intelligence.kotlin.presenter

import com.hyf.intelligence.kotlin.common.LoginUser
import com.hyf.intelligence.kotlin.common.ex.subscribeEx
import com.hyf.intelligence.kotlin.contract.MyContract
import com.hyf.intelligence.kotlin.domain.User
import com.hyf.intelligence.kotlin.presenter.base.BaseRxLifePresenter
import com.hyf.intelligence.kotlin.protocol.dao.IUserDaoProtocol
import com.hyf.intelligence.kotlin.protocol.dao.base.DaoFactory
import com.hyf.intelligence.kotlin.protocol.http.IUserHttpProtocol
import mvp.ljb.kt.presenter.getContextEx
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.ljb.kt.client.HttpFactory

/**
 * Created by L on 2017/7/18.
 */
class MyPresenter : BaseRxLifePresenter<MyContract.IView>(),
        MyContract.IPresenter {

    override fun getUserInfo() {
        Observable.concat(
                DaoFactory.getProtocol(IUserDaoProtocol::class.java).findUserByName(getContextEx(), LoginUser.name),
                HttpFactory.getProtocol(IUserHttpProtocol::class.java).getUserInfoByName(LoginUser.name)
        ).filter { user: User? -> user != null }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx({ getMvpView().showUserInfo(it!!) })
                .bindRxLifeEx(RxLife.ON_DESTROY)
    }


    override fun logout() {
        LoginUser.name = ""
        getMvpView().logoutSuccess()
    }

}