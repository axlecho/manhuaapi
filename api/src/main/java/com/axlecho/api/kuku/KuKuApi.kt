package com.axlecho.api.kuku

import com.axlecho.api.*
import com.axlecho.api.untils.MHHttpsUtils
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.net.URLEncoder
import java.nio.charset.Charset


class KuKuApi private constructor() : Api {
    companion object {
        val INSTANCE: KuKuApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            KuKuApi()
        }
    }

    private var site: KuKuNetwork = Retrofit.Builder().baseUrl(MHConstant.KUKU_HOST).build().create(KuKuNetwork::class.java)

    init {
        this.config(MHHttpsUtils.INSTANCE.client)
    }

    fun config(client: OkHttpClient) {
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(MHConstant.KUKU_HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        site = retrofit.create(KuKuNetwork::class.java)
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
        return site.top().map { res ->
            KuKuParser.parseTop(String(res.bytes(), Charset.forName("GBK")))
        }
    }

    override fun search(keyword: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return site.search(URLEncoder.encode(keyword, "gbk"), page + 1).map { res ->
            KuKuParser.parserGirdComicList(String(res.bytes(), Charset.forName("GBK")))
        }
    }

    override fun info(gid: Long): Observable<MHComicDetail> {
        return site.info(gid).map { res ->
            KuKuParser.parserInfo(String(res.bytes(), Charset.forName("GBK")), gid)
        }
    }

    override fun pageUrl(gid: Long): String {
        return MHConstant.KUKU_HOST + "/comiclist/$gid/index.htm"
    }

    override fun data(gid: Long, chapter: String): Observable<MHComicData> {
        return site.data(gid, chapter).map { res ->
            KuKuParser.parserData(String(res.bytes(), Charset.forName("GBK")), chapter)
        }
    }

    override fun raw(url: String): Observable<String> {
        return site.raw(url).map { res ->
            KuKuParser.parserRaw(String(res.bytes(), Charset.forName("GBK")))
        }
    }

    override fun collection(id: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return Observable.empty()
    }

    override fun comment(gid: Long, page: Int): Observable<MHMutiItemResult<MHComicComment>> {
        return Observable.empty()
    }
}