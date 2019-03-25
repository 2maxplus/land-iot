package com.hyf.intelligence.kotlin.presenter

import com.hyf.intelligence.kotlin.common.LoginUser
import com.hyf.intelligence.kotlin.common.ex.subscribeEx
import com.hyf.intelligence.kotlin.contract.LoginContract
import com.hyf.intelligence.kotlin.domain.User
import com.hyf.intelligence.kotlin.presenter.base.BaseRxLifePresenter
import com.hyf.intelligence.kotlin.protocol.dao.IUserDaoProtocol
import com.hyf.intelligence.kotlin.protocol.dao.base.DaoFactory
import com.hyf.intelligence.kotlin.protocol.http.IUserHttpProtocol
import com.hyf.intelligence.kotlin.utils.RxUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import mvp.ljb.kt.presenter.getContextEx
import net.ljb.kt.client.HttpFactory
import java.util.concurrent.TimeUnit

/**
 * 1、继承BaseRxLifePresenter
 * 2、通过泛型告诉Presenter层，当前View使用的通讯契约
 * 3、实现自身的通讯契约
 */
class LoginPresenter : BaseRxLifePresenter<LoginContract.IView>(), LoginContract.IPresenter {

    private var mLoginDisposable: Disposable? = null

    override fun delayGoHomeTask() {
        Observable.timer(1500, TimeUnit.MILLISECONDS)
                .subscribe { getMvpView().goHome() }
                .bindRxLifeEx(RxLife.ON_DESTROY)
    }

    override fun login(userName: String) {
        RxUtils.dispose(mLoginDisposable)
        mLoginDisposable = HttpFactory.getProtocol(IUserHttpProtocol::class.java).getUserInfoByName(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { handlerUser(it) }
                .filter { it.message.isNullOrBlank() }
                .observeOn(Schedulers.io())
                .flatMap { DaoFactory.getProtocol(IUserDaoProtocol::class.java).saveUser(getContextEx(), it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx(onError = {
                    getMvpView().loginError(it.message)
                }).bindRxLifeEx(RxLife.ON_DESTROY)
    }

    private fun handlerUser(user: User): User {
        if (user.message.isNullOrBlank()) {
            LoginUser.name = user.login
            getMvpView().loginSuccess()
        } else {
            getMvpView().loginError(user.message)
        }
        return user
    }

}