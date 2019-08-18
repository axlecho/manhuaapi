package com.axlecho.api

class TestMHContext : MHContext {
    var auth = ""
    var topTime = ""
    var topCategory = ""

    override fun loadTopTime(source: MHApiSource): String = topTime

    override fun saveTopTime(time: String, source: MHApiSource) {
        topTime = time
    }

    override fun loadTopCategory(source: MHApiSource): String = topCategory

    override fun saveTopCategory(category: String, source: MHApiSource) {
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