package com.hyf.iot.presenter

import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.RESULT_SUCCESS
import com.hyf.iot.common.ex.subscribeEx
import com.hyf.iot.contract.FarmContract
import com.hyf.iot.presenter.base.BaseRxLifePresenter
import com.hyf.iot.protocol.http.IReposHttpProtocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.ljb.kt.client.HttpFactory

/**
 * 农场新建与更新
 */
class FarmAddOrEditPresenter : BaseRxLifePresenter<FarmContract.IView>(),
        FarmContract.IPresenter {

    override fun farmAdd(name: String, address: String,
                         linkMan: String,linkPhone:String,
                         longitude: Double, latitude: Double,
                         province: String,city: String,district: String) {
        HttpFactory.getProtocol(IReposHttpProtocol::class.java)
                .farmAdd(name,address,linkMan,linkPhone,longitude,latitude,province,city,district)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx(
                        {
                            when(it.code){
                                RESULT_SUCCESS -> {
                                    getMvpView().addSuccess()
                                }
                                214,215,216 -> {
                                    LoginUser.token = ""
                                    getMvpView().onTokenExpired(it.msg)
                                }
                                else -> getMvpView().onError(it.msg)
                            }
                        },
                        { getMvpView().onError(it.message)}
                ).bindRxLifeEx(RxLife.ON_DESTROY)
    }
    override fun farmEdit(name: String, address: String,
                          linkMan: String,linkPhone:String,
                          longitude: Double, latitude: Double,
                          province: String,city: String,district: String,id: String) {
        HttpFactory.getProtocol(IReposHttpProtocol::class.java)
                .farmEdit(name,address,linkMan,linkPhone,longitude,latitude,province,city,district,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx(
                        {
                            when(it.code){
                                RESULT_SUCCESS -> getMvpView().editSuccess()
                                214,215,216 -> {
                                    LoginUser.token = ""
                                    getMvpView().onTokenExpired(it.msg)
                                }
                                else -> getMvpView().onError(it.msg)
                            }
                        },
                        { getMvpView().onError(it.message)}
                ).bindRxLifeEx(RxLife.ON_DESTROY)
    }
}