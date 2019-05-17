package com.axlecho.api

import com.axlecho.api.bangumi.BangumiApi
import com.axlecho.api.hanhan.HHApi
import com.axlecho.api.manhuagui.ManhuaguiApi
import com.axlecho.api.untils.match
import io.reactivex.Observable

interface Api {
    /** 排行榜 **/
    fun top(category: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>>

    /** 搜索 **/
    fun search(keyword: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>>

    /** 详情 **/
    fun info(gid: Long): Observable<MHComicDetail>

    /** 详情页链接 **/
    fun pageUrl(gid: Long): String

    /** 漫画数据 **/
    fun data(gid: Long, chapter: String): Observable<MHComicData>

    /** 源解析 **/
    fun raw(url: String): Observable<String>

    /** 收藏 **/
    fun collection(id: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>>

    /** 评论 **/
    fun comment(gid: Long, page: Int): Observable<MHMutiItemResult<MHComicComment>>
}


class MHApi private constructor() : Api {
    var current: Api = BangumiApi.INSTANCE


    companion object {
        val INSTANCE: MHApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MHApi()
        }
    }

    fun select(type: MHApiSource): MHApi {
        when (type) {
            MHApiSource.Bangumi -> current = BangumiApi.INSTANCE
            MHApiSource.Hanhan -> current = HHApi.INSTANCE
            MHApiSource.Manhuagui -> current = ManhuaguiApi.INSTANCE
        }
        return this
    }

    override fun top(category: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return current.top(category, page)
    }

    override fun search(keyword: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return current.search(keyword, page)
    }

    override fun info(gid: Long): Observable<MHComicDetail> {
        return current.info(gid)
    }

    override fun pageUrl(gid: Long): String {
        return current.pageUrl(gid)
    }

    override fun data(gid: Long, chapter: String): Observable<MHComicData> {
        return current.data(gid, chapter)
    }

    override fun raw(url: String): Observable<String> {
        return current.raw(url)
    }

    override fun collection(id: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        return current.collection(id, page)
    }

    override fun comment(gid: Long, page: Int): Observable<MHMutiItemResult<MHComicComment>> {
        return current.comment(gid, page)
    }

    fun switchSource(info: MHComicInfo, source: MHApiSource): Observable<MHComicInfo> {
        // we only search for 1 page
        return this.select(source).search(info.title, 0).flatMap { (t) ->
            return@flatMap match(info, t)
        }
    }

    fun getAllCollection(id:String) :Observable<MHMutiItemResult<MHComicInfo>> {
        return this.collection(id,0).flatMap {
            return@flatMap Observable.range(0,it.pages)
                    .concatMap { page -> collection(id, page) }
        }
    }
}