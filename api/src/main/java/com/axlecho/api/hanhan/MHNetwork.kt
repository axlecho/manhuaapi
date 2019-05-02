package com.axlecho.api.hanhan

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


interface MHNetwork {
    @GET("/rank/{path}")
    fun getRank(@Path("path") path: String): Observable<ResponseBody>

    @GET("/comic/?act=search")
    fun search(@Query("st") keyword: String): Observable<ResponseBody>

    @GET("/manhua{path}.html")
    fun info(@Path("path") cid: String): Observable<ResponseBody>

    @GET("/page{path}/1.html")
    fun data(@Path("path") a: String, @Query("s") s: String): Observable<ResponseBody>

    @GET("/top/{path}.aspx")
    fun top(@Path("path") path: String): Observable<ResponseBody>

    @GET
    fun raw(@Url url: String): Observable<ResponseBody>

}