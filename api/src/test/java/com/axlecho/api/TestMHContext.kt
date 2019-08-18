package com.axlecho.api

import com.google.gson.Gson
import java.io.InputStream

class TestMHContext : MHContext {
    var auth = ""
    var topTime = ""
    var topCategory = ""

    override fun loadTopTime(source: String): String = topTime

    override fun saveTopTime(time: String, source: String) {
        topTime = time
    }

    override fun loadTopCategory(source: String): String = topCategory

    override fun saveTopCategory(category: String, source: String) {
        this.topCategory = category
    }

    override fun loadAuthorization(): String = auth

    override fun saveAuthorization(authorization: String) {
        auth = authorization
    }

    val plugin = MHPlugin("117", true, true, "src/test/resources/117.zip")

    override fun getPluginNames(): List<String> {
        return arrayListOf("117")
    }

    override fun loadPlugin(name: String): String {
        return Gson().toJson(plugin, MHPlugin::class.java)
    }

    override fun savePlugin(name: String, plugin: String) {

    }

    override fun getResourceAsStream(name: String): InputStream {
        return this.javaClass.classLoader.getResourceAsStream("raw/$name.js")
    }
}