package com.axlecho.api.js

import com.google.gson.Gson

data class JSRoute(val host: String,
                   val top: String,
                   val search: String,
                   val recent: String,
                   val info: String,
                   val data: String,
                   val raw: String,
                   val comment: String) {
    companion object {
        fun loadFromJson(json: String): JSRoute {
            return Gson().fromJson(json, JSRoute::class.java)
        }
    }

    private fun String.reader(map: Map<String, String>): String {
        val sets = map.entries
        var result = this
        for ((key, value) in sets) {
            result = result.replace("\\$\\{$key\\}".toRegex(), value)
        }
        return result
    }

    fun host(): String {
        return host
    }

    fun top(category: String, page: Int): String {
        val path = top.reader(mapOf(Pair("category", category), Pair("page", page.toString())))
        return if (path.startsWith("http")) path else host + path

    }

    fun search(keyword: String, page: Int): String {
        val path = search.reader(mapOf(Pair("keyword", keyword), Pair("page", page.toString())))
        return if (path.startsWith("http")) path else host + path

    }

    fun info(gid: String): String {
        val path = info.reader(mapOf(Pair("gid", gid)))
        return if (path.startsWith("http")) path else host + path

    }

    fun recent(page: Int): String {
        val path = recent.reader(mapOf(Pair("page", page.toString())))
        return if (path.startsWith("http")) path else host + path

    }

    fun data(gid: String, cid: String): String {
        val path = data.reader(mapOf(Pair("gid", gid), Pair("cid", cid)))
        return if (path.startsWith("http")) path else host + path

    }

    fun raw(url: String): String {
        return url
    }

    fun comment(gid: String, page: Int): String {
        val path = comment.reader(mapOf(Pair("gid", gid), Pair("page", page.toString())))
        return if (path.startsWith("http")) path else host + path

    }
}

data class JSPluginInfo(
        val name: String,
        val version: Int = 0,
        val versionCode: String = "",
        val author: String = "",
        val parserFile: String,
        val routeFile: String
)