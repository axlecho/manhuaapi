package com.axlecho.api.bangumi

import com.axlecho.api.MHComicComment
import com.axlecho.api.MHComicDetail
import com.axlecho.api.MHConstant
import com.axlecho.api.MHComicInfo
import com.axlecho.api.hanhan.MHParser
import com.axlecho.api.untils.MHHttpsUtils
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class BangumiApi private constructor() {
    companion object {
        val INSTANCE: BangumiApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BangumiApi()
        }
    }

    private var site: BangumiNetwork = Retrofit.Builder().baseUrl(MHConstant.BGM_HOST).build().create(BangumiNetwork::class.java)
    private var api: BangumiNetworkByApi = Retrofit.Builder().baseUrl(MHConstant.BGM_API).build().create(BangumiNetworkByApi::class.java)

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

        val apiRetrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(MHConstant.BGM_API)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        api = apiRetrofit.create(BangumiNetworkByApi::class.java)
    }


    fun info(gid: Long): Observable<MHComicDetail> {
        return api.info(gid).map { res -> BangumiParser.parserInfo(res) }
    }

    fun search(keyword: String,page:Int): Observable<List<MHComicInfo>> {
        return api.search(keyword,page * 25).map { res -> BangumiParser.parserGirdComicListByApi(res) }
    }

    fun collection(id: String, page: Int): Observable<ArrayList<MHComicInfo>> {
        return site.collection(id, page).map { res -> BangumiParser.parserComicList(res.string()) }
    }

    fun collectionPages(id: String): Observable<Int> {
        return site.collection(id, 1).map { res -> BangumiParser.parserCollectionCount(res.string()) }
    }

    fun comment(gid:Long, page:Int) :Observable<List<MHComicComment>>  {
        return site.comments(gid,page).map{ res -> BangumiParser.parserComicComment(res.string())}
    }
}