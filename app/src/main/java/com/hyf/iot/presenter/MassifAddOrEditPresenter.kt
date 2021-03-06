package com.hyf.iot.presenter

import com.baidu.mapapi.model.LatLng
import com.hyf.iot.common.LoginUser
import com.hyf.iot.common.RESULT_SUCCESS
import com.hyf.iot.common.ex.subscribeEx
import com.hyf.iot.contract.MassifContract
import com.hyf.iot.presenter.base.BaseRxLifePresenter
import com.hyf.iot.protocol.http.IReposHttpProtocol
import com.ljb.kt.client.HttpFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject

/**
 * 农场新建与更新
 */
class MassifAddOrEditPresenter : BaseRxLifePresenter<MassifContract.IView>(),
        MassifContract.IPresenter {

    override fun massifAdd(farmId: String, name: String, size: Float, massifCoordinates: ArrayList<LatLng>) {
        val jsonObject = JSONObject()
        val jsonObj = JSONObject()
        jsonObj.put("farmId",farmId)
        jsonObj.put("name",name)
        jsonObj.put("size",size)
        val jsonArray = JSONArray()
        for(item in massifCoordinates){
            val json = JSONObject()
            json.put("latitude",item.latitude)
            json.put("longitude",item.longitude)
            jsonArray.put(json)
        }
        jsonObject.put("info",jsonObj)
        jsonObject.put("massifCoordinates",jsonArray)
        val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())

        HttpFactory.getProtocol(IReposHttpProtocol::class.java)
                .massifAdd(requestBody)
//                .massifAdd(farmId,name,size,massifCoordinates)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx(
                        {
                            when(it.code){
                                RESULT_SUCCESS -> getMvpView().addSuccess()
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

    override fun massifEdit(farmId: String, name: String, size: Float, massifCoordinates: ArrayList<LatLng>, id: String) {
        HttpFactory.getProtocol(IReposHttpProtocol::class.java)
                .massifEdit(farmId,name,size,massifCoordinates,id)
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

    override fun getFarmDetail(id: String) {
        HttpFactory.getProtocol(IReposHttpProtocol::class.java)
                .getFarmDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeEx(
                        {
                            when(it.code){
                                RESULT_SUCCESS -> getMvpView().showFarmDetail(it.data)
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