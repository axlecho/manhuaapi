package com.axlecho.api.js


import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


interface JSNetwork {
    @GET
    fun get(@Url url: String): Observable<ResponseBody>

}