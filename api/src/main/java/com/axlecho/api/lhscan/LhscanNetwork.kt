package com.axlecho.api.lhscan

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


interface LhscanNetwork {
    @GET("/manga-list.html")
    fun search(@Query("name", encoded = true) keyword: String, @Query("page") page: Int = 1): Observable<ResponseBody>

    @GET("/{gid}.html")
    fun info(@Path("gid") gid: String): Observable<ResponseBody>

    @GET("/manga-list.html")
    fun top(@Query("page") page: Int): Observable<ResponseBody>

    @GET("/{cid}.html")
    fun data(@Path("cid") cid: String): Observable<ResponseBody>

    @GET
    fun raw(@Url url: String): Observable<ResponseBody>

}