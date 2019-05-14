package com.hyf.iot.protocol.http

import com.hyf.iot.domain.Repository
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Author:Ljb
 * Time:2018/8/15
 * There is a lot of misery in life
 **/

interface IReposHttpProtocol {
    /**
     * @param url ReposUrl
     * @return Repos信息
     * */
    @GET()
    fun getReposFromUrl(@Url url: String): Observable<Repository>
}