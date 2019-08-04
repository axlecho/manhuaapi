package com.axlecho.api

import io.reactivex.Observable

interface Api {
    /** 排行榜 **/
    fun top(category: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>>

    /** 排行榜分类 **/
    fun category(): MHCategory

    /** 最近更新 **/
    fun recent(page: Int): Observable<MHMutiItemResult<MHComicInfo>>

    /** 搜索 **/
    fun search(keyword: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>>

    /** 详情 **/
    fun info(gid: String): Observable<MHComicDetail>

    /** 详情页链接 **/
    fun pageUrl(gid: String): String

    /** 漫画数据 **/
    fun data(gid: String, chapter: String): Observable<MHComicData>

    /** 源解析 **/
    fun raw(url: String): Observable<String>

    /** 收藏 **/
    fun collection(id: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>>

    /** 评论 **/
    fun comment(gid: String, page: Int): Observable<MHMutiItemResult<MHComicComment>>

    /** 登录 **/
    fun login(username: String, password: String): Observable<String>
}