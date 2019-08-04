package com.axlecho.api.bangumi

import com.axlecho.api.Api
import com.axlecho.api.MHCategory

class BangumiCategory(_api: Api) : MHCategory(_api) {
    override fun loadCategory(): String {
        return ""
    }

    override fun loadTime(): String {
        return ""
    }

    override fun saveTime(time: String) {

    }

    override fun saveCategory(category: String) {

    }

    override fun build(): String {
        return ""
    }
}