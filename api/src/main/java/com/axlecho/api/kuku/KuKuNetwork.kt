package com.axlecho.api.kuku

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


interface KuKuNetwork {
    @GET("/")
    fun getRank(): Observable<ResponseBody>

    @GET("http://so.kukudm.com/search.asp")
    fun search(@Query("kw", encoded = true) keyword: String, @Query("page") page: Int = 1): Observable<ResponseBody>

    @GET("/comiclist/{gid}/index.htm")
    fun info(@Path("gid") gid: String): Observable<ResponseBody>

    @GET("/page{path}/1.html")
    fun data(@Path("path") a: String, @Query("s") s: String): Observable<ResponseBody>

    @GET("/")
    fun top(): Observable<ResponseBody>

    @GET("/comiclist/{gid}/{cid}/1.htm")
    fun data(@Path("gid") gid: Long, @Path("cid") cid: String) :Observable<ResponseBody>

    @GET
    fun raw(@Url url: String): Observable<ResponseBody>

}