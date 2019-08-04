package com.axlecho.api.manhuadui

import com.axlecho.api.Api
import com.axlecho.api.MHApi
import com.axlecho.api.MHApiSource
import com.axlecho.api.MHCategory

class ManhuaduiCategory(_api: Api) : MHCategory(_api) {
    override fun loadCategory(): String {
        return MHApi.context.loadTopCategory(MHApiSource.Manhuadui) ?: "人气排行榜"
    }

    override fun loadTime(): String {
        return MHApi.context.loadTopTime(MHApiSource.Manhuadui) ?: "总"
    }

    override fun saveTime(time: String) {
        MHApi.context.saveTopTime(time, MHApiSource.Manhuadui)
    }

    override fun saveCategory(category: String) {
        MHApi.context.saveTopCategory(category, MHApiSource.Manhuadui)
    }

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
        return "${categoryMap[getCurrentCategory()]}-${timeMap[getCurrentTime()]}/"
    }

}