package com.axlecho.api.manhuadui


import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


interface ManhuaduiNetwork {
    @GET("/search/")
    fun search(@Query("keywords") keyword: String, @Query("page") page: Int): Observable<ResponseBody>

    @GET("/manhua/{gid}/")
    fun info(@Path("gid") gid: String): Observable<ResponseBody>

    @GET("/manhua/{gid}/{cid}.html")
    fun data(@Path("gid") gid: String, @Path("cid") cid: String): Observable<ResponseBody>

    @GET("rank/{category}")
    fun top(@Path("category") category: String): Observable<ResponseBody>

    @GET("update/{page}/")
    fun recent(@Path("page") page: Int): Observable<ResponseBody>

    @GET
    fun raw(@Url url: String): Observable<ResponseBody>

}