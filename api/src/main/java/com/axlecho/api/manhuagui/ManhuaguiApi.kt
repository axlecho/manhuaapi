package com.axlecho.api.manhuagui

import com.axlecho.api.*
import com.axlecho.api.untils.MHHttpsUtils
import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


class ManhuaguiApi private constructor() : Api {
    companion object {
        val INSTANCE: ManhuaguiApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ManhuaguiApi()
        }
    }

    private var site: ManhuaguiNetwork = Retrofit.Builder().baseUrl(MHConstant.MANHUAGUI_HOST).build().create(ManhuaguiNetwork::class.java)

    init {
        this.config(MHHttpsUtils.INSTANCE.standardBuilder()
                .addInterceptor(MHHttpsUtils.CHROME_HEADER)
                .build())
    }

    fun config(client: OkHttpClient) {
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(MHConstant.MANHUAGUI_HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        site = retrofit.create(ManhuaguiNetwork::class.java)
    }

    override fun top(category: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return site.top(category).map { res -> ManhuaguiParser.parseTop(res.string()) }
    }

    override fun recent(page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return site.recent().map { res -> ManhuaguiParser.parserRecentComicList(res.string()) }
    }

    override fun search(keyword: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        // fix page start with 1
        return site.search(keyword, page + 1).map { res -> ManhuaguiParser.parserSearchComicList(res.string()) }
    }

    override fun info(gid: String): Observable<MHComicDetail> {

        return score(gid)
                .onExceptionResumeNext(Observable.just(ManhuaguiRankingInfo(Data(0, 0, 0, 0, 0), false)))
                .flatMap { rankingInfo ->
                    comment(gid,1)
                            .onExceptionResumeNext(Observable.just(MHMutiItemResult(arrayListOf<MHComicComment>(), -1, -1)))
                            .flatMap { commentInfo ->
                                site.info(gid).map { res ->
                                    ManhuaguiParser.parserInfo(res.string(), rankingInfo, commentInfo)
                                }
                            }
                }

    }

    override fun pageUrl(gid: String): String {
        return MHConstant.HTTPS_PROTOCOL_PREFIX + MHConstant.MANHUAGUI_BASE_HOST + "/comic/$gid"
    }

    override fun data(gid: String, chapter: String): Observable<MHComicData> {
        return site.data(gid, chapter).map { res -> ManhuaguiParser.parserData(res.string()) }
    }

    override fun raw(url: String): Observable<String> {
        return Observable.just(url)
    }

    override fun collection(id: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return Observable.empty()
    }

    override fun comment(gid: String, page: Int): Observable<MHMutiItemResult<MHComicComment>> {
        return site.comment(gid, page).map { res -> ManhuaguiParser.parserComment(res.string(), page) }
    }

    override fun login(username: String, password: String): Observable<String> {
        throw MHNotSupportException()
    }

    fun score(gid: String): Observable<ManhuaguiRankingInfo> {
        return site.rating(gid).flatMap {
            Observable.just(Gson().fromJson(it.string(), ManhuaguiRankingInfo::class.java))
        }.onExceptionResumeNext(Observable.just(ManhuaguiRankingInfo(Data(0, 0, 0, 0, 0), false)))
    }
}