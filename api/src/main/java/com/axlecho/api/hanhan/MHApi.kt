package com.axlecho.api.hanhan

import com.axlecho.api.MHComicData
import com.axlecho.api.MHComicDetail
import com.axlecho.api.MHComicInfo
import com.axlecho.api.MHConstant
import com.axlecho.api.untils.MHHttpsUtils
import com.axlecho.api.untils.MHNode
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


class MHApi private constructor() {
    companion object {
        val INSTANCE: MHApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MHApi()
        }
    }

    private var site: MHNetwork = Retrofit.Builder().baseUrl(MHConstant.HOST).build().create(MHNetwork::class.java)

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
                    .addHeader("Referer", MHConstant.HOST)
                    .build()
            chain.proceed(newRequest)
        }
        builder.addInterceptor(headerInterceptor)

        // for https
        builder.hostnameVerifier(MHHttpsUtils.instance.createHostnameVerifier())
        builder.sslSocketFactory(MHHttpsUtils.instance.createSSLSocketFactory(), MHHttpsUtils.instance.trustManager)
        return builder;
    }

    fun config(client: OkHttpClient) {
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(MHConstant.HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        site = retrofit.create(MHNetwork::class.java)
    }

    fun search(keyword: String): Observable<List<MHComicInfo>> {
        return site.search(keyword).map { res -> MHParser.parserGirdComicList(res.string()) }
    }

    fun info(gid: String): Observable<MHComicDetail> {
        return site.info(gid).map { res -> MHParser.parserInfo(res.string()) }
    }

    fun data(gid: String, chapter: String): Observable<MHComicData> {
        val array = chapter.split("-")
        return site.data(array[0], array[1]).map { res -> MHParser.parserData(res.string())}
    }

    fun raw(url: String): Observable<String> {
        return site.raw(url).map { res -> MHParser.parserRaw(res.string()) }
    }

    fun top(category: String): Observable<List<MHComicInfo>> {
        return site.top(category).map { res -> MHParser.parseTop(res.string())}
    }

    fun getUrl(gid: Int): String {
        return MHConstant.HTTP_PROTOCOL_PREFIX + MHConstant.BASE_HOST + "/manhua$gid.html"
    }
}