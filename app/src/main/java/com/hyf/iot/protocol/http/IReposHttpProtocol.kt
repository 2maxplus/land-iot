package com.hyf.iot.protocol.http

import com.baidu.mapapi.model.LatLng
import com.hyf.iot.domain.base.GenResult
import io.reactivex.Observable
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
                 @Query("Latitude") Latitude: Double,@Query("Longitude") Longitude: Double,
                @Query("Province")Province: String,@Query("City")City: String,@Query("District")District: String): Observable<GenResult<String>>
    /**
     * 农场更新
     *
     * @param Name
     * @return Repos信息
     * */
    @POST("/api/Farm/Edit")
    fun farmEdit(@Query("Name") Name: String, @Query("Address") Address: String,
                  @Query("Latitude") Latitude: Double,@Query("Longitude") Longitude: Double,
                 @Query("Province")Province: String,@Query("City")City: String,@Query("District")District: String,
                 @Query("Id")Id: String): Observable<GenResult<String>>

    /**
     * 农场列表
     * */
    @POST("/api/Farm/List")
    fun getFarmList(@Query("name") name: String, @Query("companyName") companyName: String): Observable<GenResult<String>>
     /**
      * 农场详情
      * */
    @POST("/api/Farm/Detail")
    fun getFarmDetail(@Query("id") id: String): Observable<GenResult<String>>
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
    fun getMassifList(@Query("farmId") farmId: String,@Query("name")name: String): Observable<GenResult<String>>

    /**
     * 地块新建
     *
     * */
    @POST("/api/Massif/Add")
    fun massifAdd(@Query("farmId") farmId: String,@Query("name")name: String,@Query("Size")Size: String,
                  @Query("massifCoordinates")massifCoordinates: ArrayList<LatLng>): Observable<GenResult<String>>

    /**
     * 地块详情
     *
     * */
    @POST("/api/Massif/Detail")
    fun getMassifDetail(@Query("id") id: String): Observable<GenResult<String>>

    /**
     * 地块编辑
     *
     * */
    @POST("/api/Massif/Edit")
    fun massifEdit(@Query("farmId") farmId: String,@Query("name")name: String,@Query("Size")Size: String,
                   @Query("massifCoordinates")massifCoordinates: ArrayList<LatLng>,@Query("Id")Id: String): Observable<GenResult<String>>

    /**
     * 地块删除
     *
     * */
    @POST("/api/Massif/Delete")
    fun massifDelete(@Query("id") id: String): Observable<GenResult<String>>



}