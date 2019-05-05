package com.axlecho.api.bangumi

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface BangumiNetwork {
    @GET("/book/list/{id}/do")
    fun collection(@Path("id") id:String,@Query("page") page:Int): Observable<ResponseBody>

    @GET("subject/{gid}/comments")
    fun comments(@Path("gid") gid:Long,@Query("page") page:Int):Observable<ResponseBody>
}