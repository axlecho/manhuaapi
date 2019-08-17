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

    fun host(): String {
        return host
    }

    fun top(page: Int): String {
        val path = top.format(page)
        return if (path.startsWith("http")) path else host + path

    }

    fun search(keyword: String, page: Int): String {
        val path = search.format(keyword, page)
        return if (path.startsWith("http")) path else host + path

    }

    fun info(gid: String): String {
        val path = info.format(gid)
        return if (path.startsWith("http")) path else host + path

    }

    fun recent(page: Int): String {
        val path = recent
        return if (path.startsWith("http")) path else host + path

    }

    fun data(gid: String, cid: String): String {
        val path = data.format(cid)
        return if (path.startsWith("http")) path else host + path

    }

    fun raw(url: String): String {
        return url
    }

    fun comment(gid: String, page: Int): String {
        val path = comment.format(gid, page)
        return if (path.startsWith("http")) path else host + path

    }
}