package com.hyf.intelligence.iot.presenter

import com.hyf.intelligence.iot.common.LoginUser
import com.hyf.intelligence.iot.contract.MyContract
import com.hyf.intelligence.iot.presenter.base.BaseRxLifePresenter

/**
 * Created by L on 2017/7/18.
 */
class MyPresenter : BaseRxLifePresenter<MyContract.IView>(),
        MyContract.IPresenter {

    override fun getUserInfo() {
//        Observable.concat(
//                DaoFactory.getProtocol(IUserDaoProtocol::class.java).findUserByName(getContextEx(), LoginUser.name),
//                HttpFactory.getProtocol(IUserHttpProtocol::class.java).userLogin(LoginUser.name,"")
//        ).filter { user: User? -> user != null }
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeEx({ getMvpView().showUserInfo(it!!) })
//                .bindRxLifeEx(RxLife.ON_DESTROY)
    }


    override fun logout() {
        LoginUser.name = ""
        getMvpView().logoutSuccess()
    }

}