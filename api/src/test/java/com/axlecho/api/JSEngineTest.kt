package com.axlecho.api

import com.axlecho.api.js.JSEngine
import com.axlecho.api.js.JSNetworkTool
import com.axlecho.api.js.JSScope
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog
import java.io.BufferedReader
import java.io.InputStreamReader

@RunWith(RobolectricTestRunner::class)
class JSEngineTest {

    private val engine: JSScope
    private val html = "<html><head><title>JSEngineTest</title></head><body> <a href=''>Test Header</a> </body></html>"
    private val code = "jsoup.text('a');"
    private val url  = "http://www.baidu.com"
    private val codeWithNetwork = "api.get('$url')"
    private val library = "function getTitle() { return jsoup.text('a'); };"


    init {
        MHApi.context = TestMHContext()
        engine  = JSEngine.INSTANCE.fork()
        ShadowLog.stream = System.out
    }


    @Test
    fun testBase() {
        var result = engine.execute("var a = 'ok'; a;")
        Assert.assertEquals("ok", result)
        result = engine.execute("jsoup")
        println(result)


    }

    @Test
    fun testParser() {
        engine.loadPage(html)
        val result = engine.execute(code)
        Assert.assertEquals("Test Header", result)
    }

    @Test
    fun  testNetwork() {
        val result = org.mozilla.javascript.Context.toString(engine.execute(codeWithNetwork))
        Assert.assertNotNull(result)
        println(result)
        Assert.assertTrue(result.isNotEmpty())
    }

    @Test fun testNetworkTool() {
        val result = JSNetworkTool().networkGET(url)
        Assert.assertNotNull(result)
        println(result)
        Assert.assertTrue(result.isNotEmpty())
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