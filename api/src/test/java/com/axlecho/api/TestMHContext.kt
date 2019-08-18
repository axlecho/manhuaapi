package com.axlecho.api

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

    override fun getPluginPath(name: String): String {
        return "src/test/resources/117.zip"
    }

    override fun setPluginPath(name: String, path: String) {

    }
}