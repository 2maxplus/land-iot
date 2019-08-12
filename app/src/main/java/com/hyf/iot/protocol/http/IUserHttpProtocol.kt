package com.hyf.iot.protocol.http

import com.hyf.iot.domain.*
import com.hyf.iot.domain.base.GenResult
import com.hyf.iot.domain.base.OperateData
import com.hyf.iot.domain.device.DeviceItem
import com.hyf.iot.domain.device.ValveUseTime
import com.hyf.iot.domain.device.WaterPump
import com.hyf.iot.domain.pumb.PumpControlStations
import com.hyf.iot.domain.user.LoginInfo
import com.hyf.iot.domain.user.UserInfo
import com.hyf.iot.domain.user.VerifyBean
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface IUserHttpProtocol {
    /**
     * @param loginId 验证码成功返回的ID
     * @return  用户登录
     * */
    @POST("/api/Account/SMSVerify")
    fun userLogin(@Query("id") loginId: String, @Query("code") code: String): Observable<GenResult<LoginInfo>>

    /**
     * @param phone 电话号码
     *
     * @return  短信验证码
     * */
    @POST("/api/Account/SMSLogin")
    fun getSMSCode(@Query("phone") phone: String): Observable<GenResult<VerifyBean>>

    /**
     * @return  短信验证码
     * */
    @POST("/api/Account/Logout")
    fun userLogout(): Observable<GenResult<String>>

    /**
     * @return  获取用户详情
     * */
    @POST("/api/Account/Get")
    fun getUserInfo(): Observable<GenResult<UserInfo>>
    /**
     * @return  修改用户头像
     * @param file 头像form
     * */
    @Multipart
    @POST("/api/Account/ModifyHeadPortrait")
    fun modifyPortrait(@Part file : MultipartBody.Part): Observable<GenResult<String>>

    /**
     * 获取头像
     * */
    @POST("/api/File/GetHeadPortrait")
    fun getPortrait(): Observable<GenResult<String>>

    /**
     * @param nickName 昵称
     * @return  修改用户昵称
     * */
    @POST("/api/Account/ModifyNickName")
    fun modifyNickName(@Query("nickName")nickName: String): Observable<GenResult<String>>

    /**
     * @param content 内容
     * @return  添加意见
     * */
    @POST("/api/Feedback/Add")
    fun feedbackAdd(@Query("content")content: String): Observable<GenResult<String>>

    /**
     * 泵站信息
     *
     * */
    @POST("/api/pumproom/detail")
    fun getPumpRoomByName(): Observable<GenResult<PumpControlStations>>

    /**
     * 水泵设备信息
     *
     * */
    @POST("/api/Device/GetWaterPump")
    fun getWaterPumpByFarmId(@Query("farmId")farmId: String): Observable<GenResult<MutableList<WaterPump>>>

    /**
     * 阀门开关
     *
     * */
    @POST("/api/device/{state}")
    fun setDeviceStateById(@Path("state") state: String,@Query("valveId") id: String): Observable<GenResult<OperateData>>

    /**
     * 获取阀门开关时长
     *
     * */
    @POST("/api/device/GetValveUseTimes")
    fun getValveUseTimesById(@Query("deviceId")deviceId: String): Observable<GenResult<MutableList<ValveUseTime>>>

    /**
     * 获取设备详情
     *
     * */
    @POST("/api/device/get")
    fun getDeviceDetailById(@Query("id")deviceId: String): Observable<GenResult<DeviceItem>>

    /***
     * 获取设备经纬度
     *
     * */
    @POST("/api/Device/GetCoordinates")
    fun getCoordinates(): Observable<GenResult<MutableList<DeviceCoordinates>>>




}