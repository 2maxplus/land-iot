package com.hyf.iot.protocol.http

import com.baidu.mapapi.model.LatLng
import com.hyf.iot.domain.base.GenResult
import com.hyf.iot.domain.farm.Farm
import com.hyf.iot.domain.farm.Massif
import com.hyf.iot.domain.plan.Plan
import com.hyf.iot.domain.plan.PlanDetail
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * 农场
 *
 **/
interface IReposHttpProtocol {
    /**
     * 农场新建
     *
     * @param Name
     * @return Repos信息
     * */
    @POST("/api/Farm/Add")
    fun farmAdd(@Query("Name") Name: String, @Query("Address") Address: String,
                @Query("LinkMan")LinkMan: String,@Query("LinkPhone") LinkPhone: String,
                 @Query("Latitude") Latitude: Double,@Query("Longitude") Longitude: Double,
                @Query("Province")Province: String,@Query("City")City: String,@Query("District")District: String): Observable<GenResult<Farm>>


    /**
     * 农场新建
     * @param info 农场信息
     *
     * */
    @POST("/api/Farm/Add")
    fun farmAdd(@Body info: RequestBody): Observable<GenResult<Farm>>

    /**
     * 农场更新
     *
     * @param Name
     * @return Repos信息
     * */
    @POST("/api/Farm/Edit")
    fun farmEdit(@Query("Name") Name: String, @Query("Address") Address: String,
                 @Query("LinkMan")LinkMan: String,@Query("LinkPhone") LinkPhone: String,
                  @Query("Latitude") Latitude: Double,@Query("Longitude") Longitude: Double,
                 @Query("Province")Province: String,@Query("City")City: String,@Query("District")District: String,
                 @Query("Id")Id: String): Observable<GenResult<String>>

    @POST("/api/Farm/Edit")
    fun farmEdit(@Body info: RequestBody): Observable<GenResult<String>>

    /**
     * 农场列表
     * */
    @POST("/api/Farm/List")
    fun getFarmList(@Query("name") name: String, @Query("companyName") companyName: String): Observable<GenResult<MutableList<Farm>>>
     /**
      * 农场详情
      * */
    @POST("/api/Farm/Detail")
    fun getFarmDetail(@Query("id") id: String): Observable<GenResult<Farm>>
    /**
     * 农场删除
     * */
    @POST("/api/Farm/Delete")
    fun farmDelete(@Query("id") id: String): Observable<GenResult<String>>

    /**
     * 地块列表
     *
     * */
    @POST("/api/Massif/List")
    fun getMassifList(@Query("farmId") farmId: String,@Query("name")name: String): Observable<GenResult<MutableList<Massif>>>


    @POST("/api/Massif/List")
    fun getMassifList(@Body info: RequestBody): Observable<GenResult<MutableList<Massif>>>


    /**
     * 地块新建
     *
     * */
    @POST("/api/Massif/Add")
    fun massifAdd(@Query("farmId") farmId: String,@Query("name")name: String,@Query("Size")Size: Float,
                  @Query("massifCoordinates")massifCoordinates: ArrayList<LatLng>): Observable<GenResult<String>>

    @POST("/api/Massif/Add")
    fun massifAdd(@Body data: RequestBody): Observable<GenResult<String>>

    /**
     * 地块详情
     *
     * */
    @POST("/api/Massif/Detail")
    fun getMassifDetail(@Query("id") id: String): Observable<GenResult<Massif>>

    /**
     * 地块编辑
     *
     * */
    @POST("/api/Massif/Edit")
    fun massifEdit(@Query("farmId") farmId: String,@Query("name")name: String,@Query("Size")Size: Float,
                   @Query("massifCoordinates")massifCoordinates: ArrayList<LatLng>,@Query("Id")Id: String): Observable<GenResult<String>>

    /**
     * 地块删除
     *
     * */
    @POST("/api/Massif/Delete")
    fun massifDelete(@Query("id") id: String): Observable<GenResult<String>>

    /**
     * 计划列表
     * @param state  不传为全部
     * 0:未执行,
     * 1:在执行,
     * 2:已暂停,
     * 3:已执行,
     * */
    @POST("/api/IrrigatePlan/List")
    fun getIrrigatePlanList(@Query("farmId") farmId: String,@Query("state") state: String): Observable<GenResult<MutableList<Plan>>>

    @POST("/api/IrrigatePlan/List")
    fun getIrrigatePlanList(@Body info: RequestBody): Observable<GenResult<MutableList<Plan>>>

    /**
     * 计划zu列表
     * @param id  plan id
     * */
    @POST("/api/IrrigatePlan/Detail")
    fun getIrrigatePlanDetail(@Query("id") id: String): Observable<GenResult<PlanDetail>>

    /**
     * 计划开始
     * @param id  plan id
     * */
    @POST("/api/IrrigatePlan/ExecuteStart")
    fun setPlanStart(@Query("id") id: String): Observable<GenResult<String>>

    /**
     * 计划停止
     * @param id  plan id
     * */
    @POST("/api/IrrigatePlan/ExecuteStop")
    fun setPlanStop(@Query("id") id: String): Observable<GenResult<String>>

    /**
     * 计划暂停
     * @param id  plan id
     * */
    @POST("/api/IrrigatePlan/ExecuteSuspend")
    fun setPlanSuspend(@Query("id") id: String): Observable<GenResult<String>>

    /**
     * 计划继续恢复
     * @param id  plan id
     * */
    @POST("/api/IrrigatePlan/ExecuteContinue")
    fun setPlanContinue(@Query("id") id: String): Observable<GenResult<String>>

}