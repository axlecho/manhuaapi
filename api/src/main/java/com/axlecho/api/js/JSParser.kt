package com.axlecho.api.js

import com.axlecho.api.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class JSParser private constructor(script: String) {

    companion object {
        private val Tag = JSParser::javaClass.name
        fun loadFromJS(script: String): JSParser {
            return JSParser(script)
        }
    }

    private val engine = JSEngine()
    private val gson = Gson()

    init {
        engine.init()
        engine.loadLibrary(script)
    }

    fun parserSearch(html: String): MHMutiItemResult<MHComicInfo> {
        engine.loadPage(html)
        val result = engine.callFunction("search")
        val jsonType = object : TypeToken<MHMutiItemResult<MHComicInfo>>() {}.type
        return gson.fromJson(result, jsonType)
    }

    fun parserRecent(html: String): MHMutiItemResult<MHComicInfo> {
        engine.loadPage(html)
        val result = engine.callFunction("recent")
        val jsonType = object : TypeToken<MHMutiItemResult<MHComicInfo>>() {}.type
        return gson.fromJson(result, jsonType)
    }


    fun parseTop(html: String): MHMutiItemResult<MHComicInfo> {
        engine.loadPage(html)
        val result = engine.callFunction("recent")
        val jsonType = object : TypeToken<MHMutiItemResult<MHComicInfo>>() {}.type
        return gson.fromJson(result, jsonType)
    }

    fun parserInfo(html: String): MHComicDetail {
        engine.loadPage(html)
        val result = engine.callFunction("info")
        val jsonType = object : TypeToken<MHComicDetail>() {}.type
        return gson.fromJson(result, jsonType)
    }


    fun parserRaw(html: String): String {
        engine.loadPage(html)
        return engine.callFunction("raw")
    }

    fun parserData(html: String): MHComicData {
        engine.loadPage(html)
        val result = engine.callFunction("data")
        val jsonType = object : TypeToken<MHComicData>() {}.type
        return gson.fromJson(result, jsonType)
    }

    fun parserComment(html: String): MHMutiItemResult<MHComicComment> {
        engine.loadPage(html)
        val result = engine.callFunction("comment")
        val jsonType = object : TypeToken<MHMutiItemResult<MHComicComment>>() {}.type
        return gson.fromJson(result, jsonType)
    }
}
