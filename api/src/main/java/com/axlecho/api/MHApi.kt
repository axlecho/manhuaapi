package com.axlecho.api

import com.axlecho.api.bangumi.BangumiApi
import com.axlecho.api.hanhan.HHApi
import com.axlecho.api.js.JSApi
import com.axlecho.api.kuku.KuKuApi
import com.axlecho.api.manhuadui.ManhuaduiApi
import com.axlecho.api.manhuagui.ManhuaguiApi
import com.axlecho.api.pica.PicaApi
import com.axlecho.api.untils.match
import io.reactivex.Observable

interface MHContext {
    fun loadAuthorization(): String
    fun saveAuthorization(authorization: String)
    fun loadTopTime(source: MHApiSource): String
    fun saveTopTime(time: String, source: MHApiSource)
    fun loadTopCategory(source: MHApiSource): String
    fun saveTopCategory(category: String, source: MHApiSource)
}

class EmptyContext : MHContext {
    override fun loadAuthorization(): String {
        throw MHNotSupportException()
    }

    override fun saveAuthorization(authorization: String) {
        throw MHNotSupportException()
    }

    override fun saveTopCategory(category: String, source: MHApiSource) {
        throw MHNotSupportException()
    }

    override fun loadTopCategory(source: MHApiSource): String {
        throw MHNotSupportException()
    }

    override fun saveTopTime(time: String, source: MHApiSource) {
        throw MHNotSupportException()
    }

    override fun loadTopTime(source: MHApiSource): String {
        throw MHNotSupportException()
    }
}


class MHApi private constructor() : Api {
    var current: Api = BangumiApi.INSTANCE


    companion object {
        val INSTANCE: MHApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MHApi()
        }

        var context: MHContext = EmptyContext()
    }

    fun select(type: MHApiSource): MHApi {
        when (type) {
            MHApiSource.Bangumi -> current = BangumiApi.INSTANCE
            MHApiSource.Hanhan -> current = HHApi.INSTANCE
            MHApiSource.Manhuagui -> current = ManhuaguiApi.INSTANCE
            MHApiSource.Kuku -> current = KuKuApi.INSTANCE
            MHApiSource.Pica -> current = PicaApi.INSTANCE
            MHApiSource.Manhuadui -> current = ManhuaduiApi.INSTANCE
        }
        return this
    }

    fun get(type: MHApiSource): Api {
        return when (type) {
            MHApiSource.Bangumi -> BangumiApi.INSTANCE
            MHApiSource.Hanhan -> HHApi.INSTANCE
            MHApiSource.Manhuagui -> ManhuaguiApi.INSTANCE
            MHApiSource.Kuku -> KuKuApi.INSTANCE
            MHApiSource.Pica -> PicaApi.INSTANCE
            MHApiSource.Manhuadui -> ManhuaduiApi.INSTANCE
            MHApiSource.JS -> JSApi.loadFromString("","")
        }
    }

    override fun top(category: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return current.top(category, page)
    }

    override fun search(keyword: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return current.search(keyword, page)
    }

    override fun info(gid: String): Observable<MHComicDetail> {
        return current.info(gid)
    }

    override fun pageUrl(gid: String): String {
        return current.pageUrl(gid)
    }

    override fun data(gid: String, chapter: String): Observable<MHComicData> {
        return current.data(gid, chapter)
    }

    override fun raw(url: String): Observable<String> {
        return current.raw(url)
    }

    override fun collection(id: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return current.collection(id, page)
    }

    override fun comment(gid: String, page: Int): Observable<MHMutiItemResult<MHComicComment>> {
        return current.comment(gid, page)
    }

    override fun recent(page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return current.recent(page)
    }

    override fun login(username: String, password: String): Observable<String> {
        return current.login(username, password)
    }

    override fun category(): MHCategory {
        return current.category()
    }

    fun switchSource(info: MHComicInfo, source: MHApiSource): Observable<MHComicInfo> {
        // we only search for 1 page
        return this.select(source).search(info.title, 0).flatMap { (t) ->
            return@flatMap match(info, t)
        }
    }

    fun getAllCollection(id: String): Observable<MHMutiItemResult<MHComicInfo>> {
        return this.collection(id, 0).flatMap {
            return@flatMap Observable.range(0, it.pages)
                    .concatMap { page -> collection(id, page) }
        }
    }
}