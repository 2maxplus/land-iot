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
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

/**
 * 农场新建与更新
 */
class FarmAddOrEditPresenter : BaseRxLifePresenter<FarmContract.IView>(),
        FarmContract.IPresenter {

    override fun farmAdd(name: String, address: String,
                         linkMan: String,linkPhone:String,
                         latitude: Double, longitude: Double,
                         province: String,city: String,district: String) {
        val jsonObject = JSONObject()
        jsonObject.put("name",name)
        jsonObject.put("address",address)
        jsonObject.put("linkMan",linkMan)
        jsonObject.put("linkPhone",linkPhone)
        jsonObject.put("latitude",latitude)
        jsonObject.put("longitude",longitude)

        val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
        HttpFactory.getProtocol(IReposHttpProtocol::class.java)
                .farmAdd(requestBody)
//                .farmAdd(name,address,linkMan,linkPhone,longitude,latitude,province,city,district)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx(
                        {
                            when(it.code){
                                RESULT_SUCCESS -> {
                                    val farmId = it.data.id
                                    LoginUser.farmId = farmId!!
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
                          latitude: Double, longitude: Double,
                          province: String,city: String,district: String,id: String) {

        val jsonObject = JSONObject()
        jsonObject.put("id",id)
        jsonObject.put("name",name)
        jsonObject.put("address",address)
        jsonObject.put("linkMan",linkMan)
        jsonObject.put("linkPhone",linkPhone)
        jsonObject.put("latitude",latitude)
        jsonObject.put("longitude",longitude)

        val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())

        HttpFactory.getProtocol(IReposHttpProtocol::class.java)
                .farmEdit(requestBody)
//                .farmEdit(name,address,linkMan,linkPhone,longitude,latitude,province,city,district,id)
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