package com.axlecho.api.bangumi

import com.axlecho.api.*
import com.axlecho.api.untils.MHHttpsUtils
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class BangumiApi private constructor(): Api  {
    companion object {
        val INSTANCE: BangumiApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BangumiApi()
        }
    }

    private var site: BangumiNetwork = Retrofit.Builder().baseUrl(MHConstant.BGM_HOST).build().create(BangumiNetwork::class.java)
    private var api: BangumiNetworkByApi = Retrofit.Builder().baseUrl(MHConstant.BGM_API).build().create(BangumiNetworkByApi::class.java)

    init {
        this.config(MHHttpsUtils.INSTANCE.client)
    }


    fun config(client: OkHttpClient) {
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(MHConstant.BGM_HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        site = retrofit.create(BangumiNetwork::class.java)

        val apiRetrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(MHConstant.BGM_API)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        api = apiRetrofit.create(BangumiNetworkByApi::class.java)
    }


    override fun top(category: String): Observable<List<MHComicInfo>> {
        return site.top().map { res -> BangumiParser.parserComicList(res.string())}
    }

    override fun search(keyword: String,page:Int): Observable<List<MHComicInfo>> {
        return api.search(keyword,page * 25).map { res -> BangumiParser.parserComicListByApi(res) }
    }

    override fun info(gid: Long): Observable<MHComicDetail> {
        return api.info(gid).map { res -> BangumiParser.parserInfo(res) }
    }

    override fun pageUrl(gid: Long): String {
        return MHConstant.HTTP_PROTOCOL_PREFIX + MHConstant.BGM_HOST + "/subject/" + gid
    }

    override fun data(gid: Long, chapter: String): Observable<MHComicData> {
        return Observable.empty()
    }

    override fun raw(url: String): Observable<String> {
        return Observable.empty()
    }

    override fun collection(id: String, page: Int): Observable<ArrayList<MHComicInfo>> {
        return site.collection(id, page).map { res -> BangumiParser.parserComicList(res.string()) }
    }

    fun collectionPages(id: String): Observable<Int> {
        return site.collection(id, 1).map { res -> BangumiParser.parserCollectionCount(res.string()) }
    }

    override fun comment(gid:Long, page:Int) :Observable<List<MHComicComment>>  {
        return site.comments(gid,page).map{ res -> BangumiParser.parserComicComment(res.string())}
    }
}