package com.axlecho.api.hanhan

import com.axlecho.api.Api
import com.axlecho.api.MHCategory

class HHCategory(_api: Api) : MHCategory(_api) {

    init {
        buildTimeMap()
        buildCategoryMap()
    }
    override fun build(): String {
        return categoryMap[category] ?: ""
    }

    private fun buildTimeMap() {
    }

    private fun buildCategoryMap() {
        categoryMap["最多人看"] = "hotrating"
        categoryMap["评分最高"] = "toprating"
        categoryMap["最多评论"] = "hoorating"
    }
}