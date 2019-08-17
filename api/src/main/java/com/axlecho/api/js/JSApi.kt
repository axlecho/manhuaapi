package com.axlecho.api.js

import com.axlecho.api.*
import com.axlecho.api.bangumi.BangumiApi
import com.axlecho.api.bangumi.BangumiCategory
import com.axlecho.api.untils.MHHttpsUtils
import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


class JSApi private constructor() : Api {

    private val siteInfo = JSSite("", "", "", "", "", "", "", "")

    companion object {
        val INSTANCE: JSApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            JSApi()
        }
    }

    private var site: JSNetwork = Retrofit.Builder().baseUrl(siteInfo.host).build().create(JSNetwork::class.java)
    private val parser: JSParser = JSParser()

    init {
        this.config(MHHttpsUtils.INSTANCE.standardBuilder()
                .addInterceptor(MHHttpsUtils.CHROME_HEADER)
                .build())
    }

    fun config(client: OkHttpClient) {
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(siteInfo.host)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        site = retrofit.create(JSNetwork::class.java)
    }

    override fun top(category: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return site.get(siteInfo.top).map { res -> parser.parseTop(res.string()) }
    }

    override fun category(): MHCategory {
        return BangumiCategory(this)
    }

    override fun recent(page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return site.get(siteInfo.recent).map { res -> parser.parserRecent(res.string()) }
    }

    override fun search(keyword: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        // fix page start with 1
        return site.get(siteInfo.search).map { res -> parser.parserSearch(res.string()) }
    }

    override fun info(gid: String): Observable<MHComicDetail> {
        return site.get(siteInfo.info).map { res -> parser.parserInfo(res.string()) }
    }

    override fun pageUrl(gid: String): String {
        return siteInfo.info
    }

    override fun data(gid: String, chapter: String): Observable<MHComicData> {
        return site.get(siteInfo.data).map { res -> parser.parserData(res.string()) }
    }

    override fun raw(url: String): Observable<String> {
        return site.get(url).map { res -> parser.parserRaw(res.string()) }
    }

    override fun comment(gid: String, page: Int): Observable<MHMutiItemResult<MHComicComment>> {
        return site.get(siteInfo.comment).map { res -> parser.parserComment(res.string()) }
    }

    override fun collection(id: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        throw MHNotSupportException()
    }


    override fun login(username: String, password: String): Observable<String> {
        throw MHNotSupportException()
    }
}