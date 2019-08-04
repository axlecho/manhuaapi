package com.axlecho.api

import io.reactivex.Observable

abstract class MHCategory(_api: Api) {
    private val api = _api
    protected val timeMap = mutableMapOf<String, String>()
    protected val categoryMap = mutableMapOf<String, String>()


    fun top(page: Int): Observable<MHMutiItemResult<MHComicInfo>> = api.top(this.build(), page)

    fun time(time: String): MHCategory = apply { saveTime(time) }

    fun category(category: String): MHCategory = apply { saveCategory(category) }

    fun getTimes(): Set<String> = timeMap.keys

    fun getCategorys(): Set<String> = categoryMap.keys

    fun getCurrentTime(): String = loadTime()

    fun getCurrentCategory(): String = loadCategory()

    abstract fun build(): String

    abstract fun loadCategory(): String

    abstract fun loadTime(): String

    abstract fun saveTime(time:String)

    abstract fun saveCategory(category: String)
}