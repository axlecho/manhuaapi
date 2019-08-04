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
}