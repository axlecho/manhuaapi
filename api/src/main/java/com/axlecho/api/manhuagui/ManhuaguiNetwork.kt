package com.axlecho.api.manhuagui


import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


interface ManhuaguiNetwork {
    @GET("/s/{keyword}_p{page}.html")
    fun search(@Path("keyword") keyword: String, @Path("page") page: Int): Observable<ResponseBody>

    @GET("/comic/{gid}/")
    fun info(@Path("gid") gid: String): Observable<ResponseBody>

    @GET("/tools/vote.ashx?act=get")
    fun rating(@Query("bid") gid: String): Observable<ResponseBody>

    @GET("/comic/{gid}/{cid}.html")
    fun data(@Path("gid") gid: String, @Path("cid") cid: String): Observable<ResponseBody>

    @GET("rank/{category}")
    fun top(@Path("category") category:String): Observable<ResponseBody>

    @GET("/update/d30.html")
    fun recent():Observable<ResponseBody>

    @GET("https://www.manhuagui.com/tools/submit_ajax.ashx?action=comment_list")
    fun comment(@Query("book_id") gid: String, @Query("page_index") page: Int): Observable<ResponseBody>

    @GET
    fun raw(@Url url: String): Observable<ResponseBody>

}