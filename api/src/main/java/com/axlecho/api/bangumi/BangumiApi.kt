package com.axlecho.api.bangumi

import com.axlecho.api.MHConstant
import com.axlecho.api.module.comic.MHComic
import com.axlecho.api.module.comic.MHComicInfo
import com.axlecho.api.untils.MHHttpsUtils
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class BangumiApi private constructor() {
    companion object {
        val INSTANCE: BangumiApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BangumiApi()
        }
    }

    private var site: BangumiNetwork = Retrofit.Builder().baseUrl(MHConstant.BGM_HOST).build().create(BangumiNetwork::class.java)

    init {
        this.config(standardBuilder().build())
    }

    fun standardBuilder(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.HEADERS

        builder.addNetworkInterceptor(logging)
        // builder.addInterceptor(logging)

        val headerInterceptor = Interceptor { chain ->
            val newRequest = chain.request().newBuilder()
                    .addHeader("user-agent", MHConstant.USER_AGENT)
                    .addHeader("Referer", MHConstant.BGM_HOST)
                    .build()
            chain.proceed(newRequest)
        }
        builder.addInterceptor(headerInterceptor)

        // for https
        builder.hostnameVerifier(MHHttpsUtils.instance.createHostnameVerifier())
        builder.sslSocketFactory(MHHttpsUtils.instance.createSSLSocketFactory(), MHHttpsUtils.instance.trustManager)
        return builder
    }

    fun config(client: OkHttpClient) {
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(MHConstant.BGM_HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        site = retrofit.create(BangumiNetwork::class.java)
    }

    fun collection(id: String,page:Int): Observable<ArrayList<MHComic>> {
        return site.collection(id,page).map { res -> BangumiParser.parserComicList(res.string()) }
    }
}