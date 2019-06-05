package com.axlecho.api.bangumi

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface BangumiNetwork {
    @GET("/book/list/{id}/do")
    fun collection(@Path("id") id: String, @Query("page") page: Int): Observable<ResponseBody>

    @GET("/subject/{gid}/comments")
    fun comments(@Path("gid") gid: Long, @Query("page") page: Int): Observable<ResponseBody>

    @GET("/book/browser?sort=rank")
    fun top(@Query("page") page: Int): Observable<ResponseBody>

    @GET("/subject_search/{keyword}?cat=1")
    fun search(@Path("keyword") keyword: String, @Query("page") page: Int): Observable<ResponseBody>

    @GET("/login")
    fun preLogin(): Observable<okhttp3.Response>

    @FormUrlEncoded
    @POST("/FollowTheRabbit")
    fun login(@Header("Cookie") chii_sid: String,
              @Field("formhash") formhash: String,
              @Field("email") email: String,
              @Field("password") password: String,
              @Field("captcha_challenge_field") captcha: String,
              @Field("referer") referer: String = "http://bangumi.tv/",
              @Field("dreferer") dreferer: String = "http://bangumi.tv/",
              @Field("loginsubmit") loginsubmit: String = "loginsubmit"): Observable<Response<ResponseBody>>

    @GET("/login")
    fun genSid(): Observable<Response<ResponseBody>>

    @GET("/signup/captcha")
    fun captcha(@Query("") time: String, @Header("Cookie") chii_sid: String): Observable<ResponseBody>

    @GET
    fun raw(@Url url: String): Observable<ResponseBody>
}