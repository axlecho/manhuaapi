package com.axlecho.api.hanhan

import com.axlecho.api.*
import com.axlecho.api.untils.MHHttpsUtils
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


class HHApi private constructor() :Api {
    companion object {
        val INSTANCE: HHApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            HHApi()
        }
    }

    private var site: HHNetwork = Retrofit.Builder().baseUrl(MHConstant.HANHAN_HOST).build().create(HHNetwork::class.java)
    private val categorys = HHCategory(this)

    init {
        this.config(MHHttpsUtils.INSTANCE.client)
    }

    fun config(client: OkHttpClient) {
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(MHConstant.HANHAN_HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        site = retrofit.create(HHNetwork::class.java)
    }

    override fun top(category: String,page:Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return site.top("hotrating").map { res -> HHParser.parseTop(res.string())}
    }

    override fun category(): MHCategory {
        return categorys
    }

    override fun recent(page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return site.top("newrating").map{res -> HHParser.parseTop(res.string())}
    }
    override fun search(keyword: String,page:Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return site.search(keyword).map { res -> HHParser.parserGirdComicList(res.string()) }
    }

    override fun info(gid: String): Observable<MHComicDetail> {
        return site.info(gid).map { res -> HHParser.parserInfo(res.string()) }
    }

    override fun pageUrl(gid: String): String {
        return MHConstant.HTTP_PROTOCOL_PREFIX + MHConstant.HANHAN_BASE_HOST + "/manhua/$gid.html"
    }

    override fun data(gid: String, chapter: String): Observable<MHComicData> {
        val array = chapter.split("-")
        return site.data(array[0], array[1]).map { res -> HHParser.parserData(res.string())}
    }

    override fun raw(url: String): Observable<String> {
        return site.raw(url).map { res -> HHParser.parserRaw(res.string()) }
    }

    override fun collection(id: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return Observable.empty()
    }

    override fun comment(gid:String, page:Int):Observable<MHMutiItemResult<MHComicComment>> {
        return Observable.empty()
    }

    override fun login(username: String, password: String): Observable<String>  {
        throw MHNotSupportException()
    }
}