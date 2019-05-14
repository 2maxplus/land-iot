package com.hyf.iot.protocol.http

import com.hyf.iot.domain.*
import com.hyf.iot.domain.base.GenResult
import com.hyf.iot.domain.base.OperateData
import com.hyf.iot.domain.device.DeviceItem
import com.hyf.iot.domain.device.ValveUseTime
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
    @POST("/api/User/SMSVerify")
    fun userLogin(@Query("id") loginId: String, @Query("code") code: String): Observable<GenResult<LoginInfo>>

    /**
     * @param phone 电话号码
     *
     * @return  短信验证码
     * */
    @POST("/api/User/SMSLogin")
    fun getSMSCode(@Query("phone") phone: String): Observable<GenResult<VerifyBean>>

    /**
     * @return  短信验证码
     * */
    @POST("/api/User/Logout")
    fun userLogout(): Observable<GenResult<String>>

    /**
     * @return  获取用户详情
     * */
    @POST("/api/User/Get")
    fun getUserInfo(): Observable<GenResult<UserInfo>>
    /**
     * @return  修改用户头像
     * @param file 头像form
     * */
    @Multipart
    @POST("/api/User/ModifyHeadPortrait")
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
    @POST("/api/User/ModifyNickName")
    fun modifyNickName(@Query("nickName")nickName: String): Observable<GenResult<String>>
    /**
     * @param content 内容
     * @return  添加意见
     * */
    @POST("/api/Feedback/Add")
    fun feedbackAdd(@Query("content")content: String): Observable<GenResult<String>>

    /**
     * @param userName 用户名
     * @param page 页码
     * @return Events列表数据
     * */
    @GET("/users/{userName}/events")
    fun getEventsByName(@Path("userName") userName: String, @Query("page") page: Int): Observable<MutableList<Event>>

    /**
     * @param userName 用户名
     * @param page 页码
     * @return Starred列表数据
     * */
    @GET("/users/{userName}/starred")
    fun getStarredByName(@Path("userName") userName: String, @Query("page") page: Int): Observable<MutableList<Starred>>

    /**
     * @param userName 用户名
     * @param page 页码
     * @returnt Followers列表数据
     * */
    @GET("/users/{userName}/followers")
    fun getFollowersByName(@Path("userName") userName: String, @Query("page") page: Int): Observable<MutableList<Follower>>

    /**
     * @param userName 用户名
     * @param page 页码
     * @returnt Repositories列表数据
     * */
    @GET("/users/{userName}/repos")
    fun getRepositoriesByName(@Path("userName") userName: String, @Query("page") page: Int): Observable<MutableList<Repository>>

    /**
     * @param userName 用户名
     * @param page 页码
     * @returnt Following列表数据
     * */
    @GET("/users/{userName}/following")
    fun getFollowingByName(@Path("userName") userName: String, @Query("page") page: Int): Observable<MutableList<Following>>

    /**
     * 泵站信息
     *
     * */
    @POST("/api/pumproom/detail")
    fun getPumpRoomByName(): Observable<GenResult<PumpControlStations>>
    /**
     * 设备信息
     *
     * */
    @POST("/api/device/getall")
    fun getDevice(): Observable<GenResult<MutableList<DeviceItem>>>

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