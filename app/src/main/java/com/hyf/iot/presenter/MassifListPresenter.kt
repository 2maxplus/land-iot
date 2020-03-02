package com.hyf.iot.presenter

import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.RESULT_SUCCESS
import com.hyf.iot.common.ex.subscribeEx
import com.hyf.iot.contract.MassifListContract
import com.hyf.iot.presenter.base.BaseRxLifePresenter
import com.hyf.iot.protocol.http.IReposHttpProtocol
import com.ljb.kt.client.HttpFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

/**
 * 地块新建与更新
 */
class MassifListPresenter : BaseRxLifePresenter<MassifListContract.IView>(),
        MassifListContract.IPresenter {
    override fun getMassifList(id: String) {
        val jsonObject = JSONObject()
        jsonObject.put("farmId",id)
        val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())

        HttpFactory.getProtocol(IReposHttpProtocol::class.java)
                .getMassifList(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx(
                        {
                            when(it.code){
                                RESULT_SUCCESS -> getMvpView().showPageList(it.data)
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