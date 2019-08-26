package com.axlecho.api.js

import com.axlecho.api.untils.MHHttpsUtils
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class JSNetworkTool {
    private val client = MHHttpsUtils.INSTANCE.standardBuilder()
    .addInterceptor(MHHttpsUtils.CHROME_HEADER)
    .build()
    private val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("http://www")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    private val site = retrofit.create(JSNetwork::class.java)

    fun networkGET(url:String):String {
        return site.get(url).blockingFirst()?.string() ?:""
    }
}