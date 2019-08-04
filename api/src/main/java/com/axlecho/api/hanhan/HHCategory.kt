package com.axlecho.api.hanhan

import com.axlecho.api.Api
import com.axlecho.api.MHApi
import com.axlecho.api.MHApiSource
import com.axlecho.api.MHCategory

class HHCategory(_api: Api) : MHCategory(_api) {


    override fun loadTime(): String = ""

    override fun saveTime(time: String) {}

    override fun loadCategory(): String {
        val category = MHApi.context.loadTopCategory(MHApiSource.Hanhan)
        return if (category.isEmpty()) "最多人看" else category
    }

    override fun saveCategory(category: String) {
        MHApi.context.saveTopCategory(category, MHApiSource.Hanhan)
    }

    init {
        buildTimeMap()
        buildCategoryMap()
    }

    override fun build(): String {
        return categoryMap[loadCategory()] ?: ""
    }

    private fun buildTimeMap() {
    }

    private fun buildCategoryMap() {
        categoryMap["最多人看"] = "hotrating"
        categoryMap["评分最高"] = "toprating"
        categoryMap["最多评论"] = "hoorating"
    }
}