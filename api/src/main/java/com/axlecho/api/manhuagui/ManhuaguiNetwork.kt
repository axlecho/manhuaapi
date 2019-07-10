package com.axlecho.api.manhuagui


import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


interface ManhuaguiNetwork {
    @GET("/rank/{path}")
    fun getRank(@Path("path") path: String): Observable<ResponseBody>

    @GET("/s/{keyword}_p{page}.html")
    fun search(@Path("keyword") keyword: String, @Path("page") page: Int): Observable<ResponseBody>

    @GET("/comic/{gid}/")
    fun info(@Path("gid") gid: Long): Observable<ResponseBody>

    @GET("/tools/vote.ashx?act=get")
    fun rating(@Query("bid") gid: Long): Observable<ResponseBody>

    @GET("/comic/{gid}/{cid}.html")
    fun data(@Path("gid") gid: Long, @Path("cid") cid: String): Observable<ResponseBody>

    @GET("rank/")
    fun top(): Observable<ResponseBody>

    @GET("update/")
    fun recent():Observable<ResponseBody>

    @GET("https://www.manhuagui.com/tools/submit_ajax.ashx?action=comment_list")
    fun comment(@Query("book_id") gid: Long, @Query("page_index") page: Int): Observable<ResponseBody>

    @GET
    fun raw(@Url url: String): Observable<ResponseBody>

}