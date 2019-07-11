package com.axlecho.api.bangumi

import com.axlecho.api.bangumi.module.BangumiComicInfo
import com.axlecho.api.bangumi.module.BangumiSearchInfo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface BangumiNetworkByApi {
    @GET("/subject/{gid}?responseGroup=large")
    fun info(@Path("gid") gid:String): Observable<BangumiComicInfo>

    @GET("/search/subject/{keyword}?responseGroup=large&type=1&max_results=25")
    fun search(@Path("keyword") keyword:String,@Query("start") start:Int):Observable<BangumiSearchInfo>
}