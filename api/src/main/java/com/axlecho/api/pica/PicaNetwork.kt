package com.axlecho.api.pica


import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*


interface PicaNetwork {
    @GET("/rank/{path}")
    fun getRank(@Path("path") path: String): Observable<ResponseBody>

    @GET("/comics/search")
    fun search(@Header("authorization") authorization: String,
               @Query("q") keyword: String,
               @Query("page") page: Int): Observable<PicaSearchResult.Result>

    @GET("/comics/{gid}")
    fun info(@Header("authorization") authorization: String, @Path("gid") gid: String): Observable<PicaDetailResult.Result>

    @GET("/comics/{gid}/eps")
    fun chapter(@Header("authorization") authorization: String, @Path("gid") gid: String, @Query("page") page: Int): Observable<PicaChapterResult.Result>

    @GET("/comics/{gid}/order/{cid}/pages")
    fun data(@Header("authorization") authorization: String, @Path("gid") gid: String, @Path("cid") cid: String, @Query("page") page: Int): Observable<PicaDataResult.Result>


    @GET("/comics/leaderboard?ct=VC")
    fun top(@Header("authorization") authorization: String, @Query("tt") category: String): Observable<PicaTopResult.Result>

    @GET("/comics?s=ua")
    fun categyte(@Header("authorization") authorization: String, @Query("c") category: String, @Query("page") page: Int): Observable<PicaSearchResult.Result>

    @GET("/comics/random")
    fun random(@Header("authorization")authorization: String): Observable<PicaSearchResult.Result>

    @GET("/comics?s=ua")
    fun recent(@Header("authorization") authorization: String, @Query("page") page: Int): Observable<PicaSearchResult.Result>


    @GET("/comics/{gid}/comments")
    fun comment(@Header("authorization") authorization: String, @Path("gid") gid: String, @Query("page") page: Int): Observable<PicaCommentResult.Result>

    @POST("/auth/sign-in")
    fun login(@Body info: RequestBody): Observable<PicaLoginResult.Result>

}