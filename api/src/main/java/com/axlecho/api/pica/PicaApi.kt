package com.axlecho.api.pica

import com.axlecho.api.*
import com.axlecho.api.untils.HMACSHA256
import com.axlecho.api.untils.MHHttpsUtils
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class PicaApi private constructor() : Api {
    companion object {
        val INSTANCE: PicaApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            PicaApi()
        }
    }

    private var site: PicaNetwork = Retrofit.Builder().baseUrl(MHConstant.PICA_HOST).build().create(PicaNetwork::class.java)
    private val categorys = PicaCategory(this)

    init {
        this.config(headerbuild().build())
    }

    fun headerbuild(): OkHttpClient.Builder {
        val headerInterceptor = Interceptor { chain ->

            val moethod = chain.request().method()
            val time = (System.currentTimeMillis() / 1000).toString()
            val nonce = UUID.randomUUID().toString().replace("-", "")
            val api_key = "C69BAF41DA5ABD1FFEDC6D2FEA56B"
            val secret_key = "~d}\$Q7\$eIni=V)9\\RK/P.RM4;9[7|@/CA}b~OW!3?EV`:<>M7pddUBL5n|0/*Cn"
            var url = chain.request().url().toString()
            url = url.replace(MHConstant.PICA_HOST + '/', "")
            url = url + time + nonce + moethod + api_key
            url = url.toLowerCase()
            val signature = HMACSHA256(url.toByteArray(), secret_key.toByteArray())

            val newRequest = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json; charset=UTF-8")
                    .addHeader("Host", MHConstant.PICA_BASE_HOST)
                    .addHeader("User-Agent", "okhttp/3.8.1")
                    .addHeader("accept", "application/vnd.picacomic.com.v1+json")
                    .addHeader("api-key", api_key)
                    .addHeader("app-build-version", "42")
                    .addHeader("app-version", "2.2.0.0.1.1")
                    .addHeader("app-channel", "1")
                    .addHeader("app-platform", "android")
                    .addHeader("app-uuid", UUID.randomUUID().toString())
                    .addHeader("nonce", nonce)
                    .addHeader("sources", "MHViewer0.0.3")
                    .addHeader("time", time)
                    .addHeader("signature", signature)
                    .addHeader("image-quality", "original")
                    .build()
            chain.proceed(newRequest)
        }

        return MHHttpsUtils.INSTANCE.standardBuilder()
                .addInterceptor(headerInterceptor)
    }

    fun config(client: OkHttpClient) {
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(MHConstant.PICA_HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        site = retrofit.create(PicaNetwork::class.java)
    }

    override fun top(category: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return top(MHApi.context.loadAuthorization(), category, page)
    }

    override fun category(): MHCategory {
        return categorys
    }

    fun top(authorization: String, category: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return when {
            category.startsWith("_") -> site.top(authorization, category.trim('_')).map { res -> PicaParser.parserTopComicList(res) }
            category.startsWith("*") -> site.random(authorization).map { res -> PicaParser.parserSearchComicList(res) }
            else -> site.categyte(authorization, category, page + 1).map { res -> PicaParser.parserSearchComicList(res) }
        }
    }

    override fun recent(page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return recent(MHApi.context.loadAuthorization(), page)
    }

    fun recent(authorization: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return site.recent(authorization, page + 1).map { res -> PicaParser.parserSearchComicList(res) }
    }

    override fun search(keyword: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        // fix page start with 1
        return search(MHApi.context.loadAuthorization(), keyword, page)
    }

    fun search(authorization: String, keyword: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return site.search(authorization, keyword, page + 1)
                .map { res -> PicaParser.parserSearchComicList(res) }
    }

    override fun info(gid: String): Observable<MHComicDetail> {
        return info(MHApi.context.loadAuthorization(), gid)
    }

    fun info(authorization: String, gid: String): Observable<MHComicDetail> {
        return site.chapter(authorization, gid, 1).flatMap { chapters ->
            comment(authorization, gid, 1).flatMap { comments ->
                site.info(authorization, gid).map { res -> PicaParser.parserInfo(res, chapters, comments) }
            }
        }

    }

    override fun pageUrl(gid: String): String {
        return ""
    }

    override fun data(gid: String, chapter: String): Observable<MHComicData> {
        return data(MHApi.context.loadAuthorization(), gid, chapter)
    }

    fun data(authorization: String, gid: String, chapter: String): Observable<MHComicData> {
        return site.data(authorization, gid, chapter, 1).concatMap {
            return@concatMap Observable.range(1, it.data.pages.pages).concatMap { page ->
                site.data(authorization, gid, chapter, page).map { result ->
                    PicaParser.parserData(result)
                }
            }
        }.reduce { t1: MHComicData, t2: MHComicData ->
            t1.data.addAll(t2.data)
            return@reduce t1
        }.toObservable()
    }

    override fun raw(url: String): Observable<String> {
        return Observable.just(url)
    }

    override fun collection(id: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return Observable.empty()
    }

    override fun comment(gid: String, page: Int): Observable<MHMutiItemResult<MHComicComment>> {
        return comment(MHApi.context.loadAuthorization(), gid, page)
    }

    fun comment(authorization: String, gid: String, page: Int): Observable<MHMutiItemResult<MHComicComment>> {
        return site.comment(authorization, gid, page + 1).map { res -> PicaParser.parserComment(res) }
    }

    override fun login(email: String, password: String): Observable<String> {
        val param = JSONObject()
        param.put("email", email)
        param.put("password", password)
        return site.login(RequestBody.create(null, param.toString())).map { res ->
            MHApi.context.saveAuthorization(res.data.token)
            res.data.token
        }
    }
}