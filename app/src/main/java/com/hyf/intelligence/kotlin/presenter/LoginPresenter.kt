package com.hyf.intelligence.kotlin.presenter

import com.hyf.intelligence.kotlin.common.LoginUser
import com.hyf.intelligence.kotlin.common.ex.subscribeEx
import com.hyf.intelligence.kotlin.contract.LoginContract
import com.hyf.intelligence.kotlin.domain.base.GenResult
import com.hyf.intelligence.kotlin.domain.user.LoginInfo
import com.hyf.intelligence.kotlin.domain.user.VerifyBean
import com.hyf.intelligence.kotlin.presenter.base.BaseRxLifePresenter
import com.hyf.intelligence.kotlin.protocol.http.IUserHttpProtocol
import com.hyf.intelligence.kotlin.utils.RxUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.ljb.kt.HttpConfig
import net.ljb.kt.client.HttpFactory
import java.util.concurrent.TimeUnit

/**
 * 1、继承BaseRxLifePresenter
 * 2、通过泛型告诉Presenter层，当前View使用的通讯契约
 * 3、实现自身的通讯契约
 */
class LoginPresenter : BaseRxLifePresenter<LoginContract.IView>(), LoginContract.IPresenter {

    private var mLoginDisposable: Disposable? = null
    override fun getCode(phone: String) {
        RxUtils.dispose(mLoginDisposable)
        mLoginDisposable = HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .getSMSCode(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx(
                        { handlerCode(it) },
                        { getMvpView().getCodeError(it.message) })
                .bindRxLifeEx(RxLife.ON_DESTROY)
    }

    private fun handlerCode(result: GenResult<VerifyBean>){
        if (result.code == 200) {
            getMvpView().getCodeSuccess(result.data.id)
        } else {
            getMvpView().getCodeError(result.msg)
        }
    }

    override fun delayGoHomeTask() {
        Observable.timer(1500, TimeUnit.MILLISECONDS)
                .subscribe { getMvpView().goHome() }
                .bindRxLifeEx(RxLife.ON_DESTROY)
    }

    override fun login(loginId: String, code: String) {
        RxUtils.dispose(mLoginDisposable)
        mLoginDisposable = HttpFactory.getProtocol(IUserHttpProtocol::class.java)
                .userLogin(loginId, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx({ handlerUser(it) },
                        { getMvpView().loginError(it.message) })
                .bindRxLifeEx(RxLife.ON_DESTROY)
    }

    private fun handlerUser(result: GenResult<LoginInfo>){
        if (result.code == 200) {
            LoginUser.token = result.data.token
            val paramMap = mapOf("token" to LoginUser.token)
            HttpConfig.setParam(paramMap)
            getMvpView().loginSuccess()
        } else {
            getMvpView().loginError(result.msg)
        }
    }


}