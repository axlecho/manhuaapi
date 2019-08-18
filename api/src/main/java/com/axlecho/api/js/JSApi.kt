package com.axlecho.api.js

import com.axlecho.api.*
import com.axlecho.api.bangumi.BangumiCategory
import com.axlecho.api.untils.MHHttpsUtils
import com.axlecho.api.untils.MHZip
import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.File
import java.io.FileReader


class JSApi private constructor(routeInfo: String, parserInfo: String, private val plugin: String) : Api {

    companion object {
        private val map = mutableMapOf<String, JSApi>()

        fun loadFromString(routeInfo: String, parserInfo: String, plugin: String): JSApi {
            return JSApi(routeInfo, parserInfo, plugin)
        }

        fun loadFromPlugin(pluginName: String): JSApi {
            if (map.containsKey(pluginName)) {
                val api = map[pluginName]
                if (api != null) {
                    return api
                }
            }

            val plugin = MHPluginManager.INSTANCE.loadPlugin(pluginName)
            val api = loadFromPlugin(plugin)
            map[pluginName] = api
            return api
        }

        private fun loadFromPlugin(plugin: MHPlugin): JSApi {
            val zip = MHZip(plugin.path)
            val info = Gson().fromJson<JSPluginInfo>(zip.text("package.json"), JSPluginInfo::class.java)
            return JSApi.loadFromString(zip.text(info.routeFile), zip.text(info.parserFile), plugin.name)
        }
    }

    private val parser = JSParser.loadFromJS(parserInfo,plugin)
    private val route = JSRoute.loadFromJson(routeInfo)

    private var site: JSNetwork = Retrofit.Builder().baseUrl(route.host).build().create(JSNetwork::class.java)

    init {
        this.config(MHHttpsUtils.INSTANCE.standardBuilder()
                .addInterceptor(MHHttpsUtils.CHROME_HEADER)
                .build())
    }

    fun config(client: OkHttpClient) {
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(route.host)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        site = retrofit.create(JSNetwork::class.java)
    }

    override fun top(category: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return site.get(route.top(page)).map { res -> parser.parseTop(res.string()) }
    }

    override fun category(): MHCategory {
        return BangumiCategory(this)
    }

    override fun recent(page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return site.get(route.recent(page + 1)).map { res -> parser.parserRecent(res.string()) }
    }

    override fun search(keyword: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        // fix page start with 1
        return site.get(route.search(keyword, page + 1)).map { res -> parser.parserSearch(res.string()) }
    }

    override fun info(gid: String): Observable<MHComicDetail> {
        return site.get(route.info(gid)).map { res -> parser.parserInfo(res.string(), gid) }
    }

    override fun pageUrl(gid: String): String {
        return route.info(gid)
    }

    override fun data(gid: String, chapter: String): Observable<MHComicData> {
        return site.get(route.data(gid, chapter)).map { res -> parser.parserData(res.string()) }
    }

    override fun raw(url: String): Observable<String> {
        return Observable.just(url)
    }

    override fun comment(gid: String, page: Int): Observable<MHMutiItemResult<MHComicComment>> {
        return site.get(route.comment(gid, page + 1)).map { res -> parser.parserComment(res.string()) }
    }

    override fun collection(id: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        throw MHNotSupportException()
    }

    override fun login(username: String, password: String): Observable<String> {
        throw MHNotSupportException()
    }
}