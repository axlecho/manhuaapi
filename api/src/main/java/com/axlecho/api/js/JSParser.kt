package com.axlecho.api.js

import com.axlecho.api.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class JSParser private constructor(script: String, val plugin: String) {

    companion object {
        private val Tag = JSParser::javaClass.name
        fun loadFromJS(script: String, plugin: String): JSParser {
            return JSParser(script, plugin)
        }
    }

    private val engine = JSEngine.INSTANCE.fork()
    private val gson = Gson()

    init {
        engine.loadLibrary(script)
    }

    fun parserSearch(html: String): MHMutiItemResult<MHComicInfo> {
        engine.loadPage(html)
        val result = engine.callFunction("search")
        val jsonType = object : TypeToken<MHMutiItemResult<MHComicInfo>>() {}.type
        val data:MHMutiItemResult<MHComicInfo> =  gson.fromJson(result, jsonType)
        for(info in data.datas) {
            info.source = plugin
        }

        return data
    }

    fun parserRecent(html: String): MHMutiItemResult<MHComicInfo> {
        engine.loadPage(html)
        val result = engine.callFunction("recent")
        val jsonType = object : TypeToken<MHMutiItemResult<MHComicInfo>>() {}.type
        val data:MHMutiItemResult<MHComicInfo> =  gson.fromJson(result, jsonType)
        for(info in data.datas) {
            info.source = plugin
        }

        return data
    }


    fun parseTop(html: String): MHMutiItemResult<MHComicInfo> {
        engine.loadPage(html)
        val result = engine.callFunction("_top")
        val jsonType = object : TypeToken<MHMutiItemResult<MHComicInfo>>() {}.type
        val data:MHMutiItemResult<MHComicInfo> =  gson.fromJson(result, jsonType)
        for(info in data.datas) {
            info.source = plugin
        }

        return data
    }

    fun parserInfo(html: String, gid: String): MHComicDetail {
        engine.loadPage(html)
        val result = engine.callFunction("info", gid)
        val jsonType = object : TypeToken<MHComicDetail>() {}.type
        val data:MHComicDetail = gson.fromJson(result, jsonType)
        data.source = plugin
        return data
    }


    fun parserRaw(html: String): String {
        engine.loadPage(html)
        return engine.callFunction("raw")
    }

    fun parserData(html: String): MHComicData {
        engine.loadPage(html)
        val result = engine.callFunction("data")
        val jsonType = object : TypeToken<MHComicData>() {}.type
        val data:MHComicData =  gson.fromJson(result, jsonType)
        data.source = plugin
        return data
    }

    fun parserComment(html: String): MHMutiItemResult<MHComicComment> {
        engine.loadPage(html)
        val result = engine.callFunction("comment")
        val jsonType = object : TypeToken<MHMutiItemResult<MHComicComment>>() {}.type
        val data:MHMutiItemResult<MHComicComment> =  gson.fromJson(result, jsonType)
        for(comment in data.datas) {
            comment.source = plugin
        }
        return data
    }
}
