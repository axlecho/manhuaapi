package com.axlecho.api.manhuadui

import com.axlecho.api.*
import com.axlecho.api.untils.MHHttpsUtils
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


class ManhuaduiApi private constructor() : Api {
    companion object {
        val INSTANCE: ManhuaduiApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ManhuaduiApi()
        }
    }

    private var site: ManhuaduiNetwork = Retrofit.Builder().baseUrl(MHConstant.MANHUADUI_HOST).build().create(ManhuaduiNetwork::class.java)

    init {
        this.config(MHHttpsUtils.INSTANCE.standardBuilder()
                .addInterceptor(MHHttpsUtils.CHROME_HEADER)
                .build())
    }

    fun config(client: OkHttpClient) {
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(MHConstant.MANHUADUI_HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        site = retrofit.create(ManhuaduiNetwork::class.java)
    }

    override fun top(category: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return site.top(category).map { res -> ManhuaduiParser.parseTop(res.string()) }
    }

    override fun recent(page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return site.recent(page + 1).map { res -> ManhuaduiParser.parserRecentComicList(res.string()) }
    }

    override fun search(keyword: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        // fix page start with 1
        return site.search(keyword, page + 1).map { res -> ManhuaduiParser.parserSearchComicList(res.string()) }
    }

    override fun info(gid: String): Observable<MHComicDetail> {

        return site.info(gid).map { res ->
            ManhuaduiParser.parserInfo(res.string())
        }
    }

    override fun pageUrl(gid: String): String {
        return MHConstant.MANHUADUI_HOST + "/manhua/$gid/"
    }

    override fun data(gid: String, chapter: String): Observable<MHComicData> {
        return site.data(gid, chapter).map { res -> ManhuaduiParser.parserData(res.string()) }
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