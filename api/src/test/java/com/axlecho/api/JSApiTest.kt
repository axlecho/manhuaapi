package com.axlecho.api

import com.axlecho.api.js.JSApi
import com.axlecho.api.js.JSRoute
import com.google.gson.GsonBuilder
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.BufferedReader
import java.io.InputStreamReader

@RunWith(RobolectricTestRunner::class)

class JSApiTest {

    val gson = GsonBuilder().setPrettyPrinting().create()
    var api: JSApi


    init {
        MHApi.context = TestMHContext()
        api = JSApi.loadFromPlugin("117")
    }

    @Test
    fun testLoadSite() {
        val testSite = BufferedReader(InputStreamReader(this.javaClass.classLoader.getResourceAsStream("site.json"))).readText()
        val site = JSRoute.loadFromJson(testSite)
        Assert.assertNotNull(site)
        print(gson.toJson(site))
    }

    @Test
    fun testSite() {
        val testSite = BufferedReader(InputStreamReader(this.javaClass.classLoader.getResourceAsStream("site.json"))).readText()
        val site = JSRoute.loadFromJson(testSite)
        Assert.assertNotNull(site)
        print(gson.toJson(site))

        val host = site.host()
        val top = site.top("",1)
        val search = site.search("辉夜", 1)
        val recent = site.recent(1)
        val info = site.info("237146")
        val data = site.data("237146", "/201902/408323.html")
        val raw = site.raw("https://a16d.zgxhxxmh.com:60443/h51/201902/08/2135362083.jpg.webp")
        val comment = site.comment("237146", 1)

        Assert.assertEquals("https://www.177mh.net", host)
        Assert.assertEquals("https://so.177mh.net/k.php?k=辉夜&p=1", search)
        Assert.assertEquals("https://www.177mh.net/", top)
        Assert.assertEquals("https://www.177mh.net/", recent)
        Assert.assertEquals("https://www.177mh.net/colist_237146.html", info)
        Assert.assertEquals("https://www.177mh.net/201902/408323.html", data)
        Assert.assertEquals("https://a16d.zgxhxxmh.com:60443/h51/201902/08/2135362083.jpg.webp", raw)
        Assert.assertEquals("https://i.readingbox.net/ajax/utf8_json_cmlist_v2.php?page=1&cid=237146", comment)
    }

    @Test
    fun testInitApi() {
        Assert.assertNotNull(api)
    }

    @Test
    fun testInfo() {
        val result = api.info("237146").blockingFirst()
        Assert.assertNotNull(result)
        println(result)
        Assert.assertEquals("辉夜大小姐想让我告白 ~天才们的恋爱头脑战~", result.info.title)
    }

    @Test
    fun testTop() {
        val result = api.top("", 0).blockingFirst()
        Assert.assertNotNull(result)
        println(result)
        Assert.assertNotEquals(0, result.datas.size)
    }

    @Test
    fun testSearch() {
        val result = api.search("辉夜", 0).blockingFirst()
        Assert.assertNotNull(result)
        println(result)
        Assert.assertNotEquals(0, result.datas.size)
    }

    @Test
    fun testData() {
        val result = api.data("237146", "201902/408323").blockingFirst()
        Assert.assertNotNull(result)
        println(result)
        Assert.assertNotEquals(0, result.data.size)
    }
}