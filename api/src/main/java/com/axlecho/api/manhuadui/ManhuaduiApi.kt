package com.axlecho.api.manhuadui

import com.axlecho.api.*
import com.axlecho.api.untils.MHHttpsUtils
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

abstract class MHCategory(val _api: Api) {
    private val api = _api
    protected val timeMap = mutableMapOf<String, String>()
    protected val categoryMap = mutableMapOf<String, String>()


    protected var category = ""
    protected var time = ""
    fun top(page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return api.top(this.build(), page)
    }

    fun time(time: String): MHCategory {
        this.time = time
        return this
    }

    fun category(category: String): MHCategory {
        this.category = category
        return this
    }

    fun getTime(): Set<String> {
        return timeMap.keys
    }

    fun getCategorys(): Set<String> {
        return categoryMap.keys
    }

    abstract fun build(): String
}

class ManhuaduiCategory(_api: Api) : MHCategory(_api) {
    init {
        buildTimeMap()
        buildCategoryMap()
    }

    private fun buildCategoryMap() {
        categoryMap["点击排行榜"] = "click"
        categoryMap["人气排行榜"] = "popularity"
        // categoryMap["订阅排行榜"] = "subscribe"
        // categoryMap["评论排行榜"] = "comment"
        // categoryMap["吐槽排行榜"] = "criticism"
    }

    private fun buildTimeMap() {
        timeMap["总"] = ""
        timeMap["日"] = "daily"
        timeMap["周"] = "weekly"
        timeMap["月"] = "monthly"
    }

    override fun build(): String {
        return "${categoryMap[category]}-${timeMap[time]}/"
    }

}

class ManhuaduiApi private constructor() : Api {
    companion object {
        val INSTANCE: ManhuaduiApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ManhuaduiApi()
        }
    }


    private var site: ManhuaduiNetwork = Retrofit.Builder().baseUrl(MHConstant.MANHUADUI_HOST).build().create(ManhuaduiNetwork::class.java)
    private val categorys = ManhuaduiCategory(this)

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

    fun getCategory(): MHCategory {
        return categorys
    }

    override fun top(category: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return site.top(categorys.build()).map { res -> ManhuaduiParser.parseTop(res.string()) }
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