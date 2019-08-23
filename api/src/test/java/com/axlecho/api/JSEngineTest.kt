package com.axlecho.api

import com.axlecho.api.js.JSEngine
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

class JSEngineTest {
    private val engine = JSEngine.INSTANCE.fork()
    private val html = "<html><head><title>JSEngineTest</title></head><body> <a href=''>Test Header</a> </body></html>"
    private val code = "jsoup.text('a');"
    private val library = "function getTitle() { return jsoup.text('a'); };"


    @Test
    fun testBase() {
        var result = engine.execute("var a = 'ok'; a;")
        Assert.assertEquals("ok", result)
        result = engine.execute("jsoup")
        println(result)


    }

    @Test
    fun testJQuery() {
        engine.loadPage(html)
        val result = engine.execute(code)
        Assert.assertEquals("Test Header", result)
    }

    @Test
    fun testCallFunc() {
        engine.loadLibrary(library)
        engine.loadPage(html)
        val result = engine.callFunction("getTitle")
        Assert.assertEquals("Test Header", result)
    }

    @Test
    fun testRealParser() {
        val library = BufferedReader(InputStreamReader(this.javaClass.classLoader.getResourceAsStream("parser.js"))).readText()
        val page = BufferedReader(InputStreamReader(this.javaClass.classLoader.getResourceAsStream("test.html"))).readText()

        engine.loadLibrary(library)
        engine.loadPage(page)
        val result = engine.callFunction("parseTitle")
        Assert.assertEquals("想讲讲辉夜大小姐的事", result)
       //  println(engine.callFunction("info", "colist_243051"))
    }
}