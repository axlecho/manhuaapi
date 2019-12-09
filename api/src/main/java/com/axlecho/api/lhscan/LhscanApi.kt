package com.axlecho.api.lhscan

import com.axlecho.api.*
import com.axlecho.api.untils.MHHttpsUtils
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.nio.charset.Charset


class LhscanApi private constructor() : Api {

    companion object {
        val INSTANCE: LhscanApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LhscanApi()
        }
    }

    private var site: LhscanNetwork = Retrofit.Builder().baseUrl(MHConstant.LHSCAN_HOST).build().create(LhscanNetwork::class.java)
    private val categories = LhscanCategory(this)

    init {
        this.config(MHHttpsUtils.INSTANCE.client)
    }

    fun config(client: OkHttpClient) {
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(MHConstant.LHSCAN_HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        site = retrofit.create(LhscanNetwork::class.java)
    }

    class ResponseInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val response = chain.proceed(chain.request())
            val modified = response.newBuilder()
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .build()

            return modified
        }
    }

    override fun top(category: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return site.top().map { res -> LhscanParser.parserComicList(res.string()) }
    }

    override fun category(): MHCategory {
        return categories
    }

    override fun recent(page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return top("", page)
    }

    override fun search(keyword: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return site.search(keyword, page + 1).map { res -> LhscanParser.parserComicList(res.string()) }
    }

    override fun info(gid: String): Observable<MHComicDetail> {
        return site.info(gid).map { res ->
            LhscanParser.parserInfo(String(res.bytes(), Charset.forName("GBK")), gid)
        }
    }

    override fun pageUrl(gid: String): String {
        return MHConstant.LHSCAN_HOST + "/$gid.html"
    }

    override fun data(gid: String, chapter: String): Observable<MHComicData> {
        return site.data(chapter).map { res -> LhscanParser.parserData(res.string()) }
    }

    override fun raw(url: String): Observable<String> {
        return Observable.just(url)
    }

    override fun collection(id: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return Observable.empty()
    }

    override fun comment(gid: String, page: Int): Observable<MHMutiItemResult<MHComicComment>> {
        return Observable.empty()
    }

    override fun login(username: String, password: String): Observable<String> {
        throw MHNotSupportException()
    }
}