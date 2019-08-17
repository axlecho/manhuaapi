package com.axlecho.api

import com.axlecho.api.bangumi.BangumiApi
import com.axlecho.api.hanhan.HHApi
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

class MHApi private constructor() {


    companion object {
        val INSTANCE: MHApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MHApi()
        }

        var context: MHContext = EmptyContext()
    }


    fun get(type: MHApiSource): Api {
        return when (type) {
            MHApiSource.Bangumi -> BangumiApi.INSTANCE
            MHApiSource.Hanhan -> HHApi.INSTANCE
            MHApiSource.Manhuagui -> ManhuaguiApi.INSTANCE
            MHApiSource.Kuku -> KuKuApi.INSTANCE
            MHApiSource.Pica -> PicaApi.INSTANCE
            MHApiSource.Manhuadui -> ManhuaduiApi.INSTANCE
        }
    }


    fun switchSource(info: MHComicInfo, source: MHApiSource): Observable<MHComicInfo> {
        // we only search for 1 page
        return this.get(source).search(info.title, 0).flatMap { (t) ->
            return@flatMap match(info, t)
        }
    }

    fun getAllCollection(id: String): Observable<MHMutiItemResult<MHComicInfo>> {
        return this.get(MHApiSource.Bangumi).collection(id, 0).flatMap {
            return@flatMap Observable.range(0, it.pages)
                    .concatMap { page ->
                        get(MHApiSource.Bangumi).collection(id, page)
                    }
        }
    }
}