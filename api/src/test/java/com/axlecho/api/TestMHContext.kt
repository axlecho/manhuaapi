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
}