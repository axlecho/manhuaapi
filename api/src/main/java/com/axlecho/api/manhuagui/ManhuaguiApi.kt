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
        this.config(MHHttpsUtils.INSTANCE.client)
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
        return site.top().map { res -> ManhuaguiParser.parseTop(res.string()) }
    }

    override fun search(keyword: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        // fix page start with 1
        return site.search(keyword, page + 1).map { res -> ManhuaguiParser.parserSearchComicList(res.string()) }
    }

    override fun info(gid: Long): Observable<MHComicDetail> {
        return score(gid).flatMap { rankingInfo ->
            return@flatMap site.info(gid).map { res -> ManhuaguiParser.parserInfo(res.string(), rankingInfo) }
        }

    }

    override fun pageUrl(gid: Long): String {
        return MHConstant.HTTP_PROTOCOL_PREFIX + MHConstant.MANHUAGUI_BASE_HOST + "/comic/$gid"
    }

    override fun data(gid: Long, chapter: String): Observable<MHComicData> {
        return site.data(gid, chapter).map { res -> ManhuaguiParser.parserData(res.string()) }
    }

    override fun raw(url: String): Observable<String> {
        return Observable.just(url)
    }

    override fun collection(id: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return Observable.empty()
    }

    override fun comment(gid: Long, page: Int): Observable<MHMutiItemResult<MHComicComment>> {
        return Observable.empty()
    }

    fun score(gid: Long): Observable<ManhuaguiRankingInfo> {
        return site.rating(gid).flatMap {
            Observable.just(Gson().fromJson(it.string(), ManhuaguiRankingInfo::class.java))
        }.onExceptionResumeNext(Observable.just(ManhuaguiRankingInfo(Data(0, 0, 0, 0, 0), false)))
    }

}