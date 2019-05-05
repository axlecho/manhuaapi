package com.axlecho.api.bangumi

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface BangumiNetworkByApi {
    @GET("/subject/{gid}?responseGroup=large")
    fun info(@Path("gid") gid:Long): Observable<BangumiComicInfo>
}