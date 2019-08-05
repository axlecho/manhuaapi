package com.axlecho.api

import com.axlecho.api.pica.PicaApi
import com.google.gson.GsonBuilder
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog
import java.net.InetSocketAddress
import java.net.Proxy


@RunWith(RobolectricTestRunner::class)

class PicaApiTest {

    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val email = "your email"
    private val password = "your password"
    private val context = object

    @Test
    fun testBase() {
        Assert.assertEquals(1 + 1, 2)
    }

    @Before
    fun setup() {
        ShadowLog.stream = System.out

        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(1)
                .tag("API_TEST")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
        PicaApi.INSTANCE.config(PicaApi.INSTANCE.headerbuild()
                // .callTimeout(180000, TimeUnit.MILLISECONDS)
                // .readTimeout(180000, TimeUnit.MILLISECONDS)
                .proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress("127.0.0.1", 1080)))
                .build())
        MHApi.context = TestMHContext()
    }

    @Test
    fun testSearch() {
        val authorization = PicaApi.INSTANCE.login(email, password).blockingFirst()
        Logger.v(authorization)
        val b = PicaApi.INSTANCE.search(authorization, "辉夜", 0).blockingFirst()
        Logger.json(gson.toJson(b))

        Assert.assertNotNull(b.datas)
        Assert.assertNotEquals(0, b.datas.size)
    }

    @Test
    fun testSearch2() {

        PicaApi.INSTANCE.login(email, password).blockingFirst()
        val b = PicaApi.INSTANCE.search("辉夜", 0).blockingFirst()
        Logger.json(gson.toJson(b))
        Assert.assertNotNull(b.datas)
        Assert.assertNotEquals(0, b.datas.size)
    }

    @Test
    fun testTop() {
        val authorization = PicaApi.INSTANCE.login(email, password).blockingFirst()
        Logger.v(authorization)
        val a = PicaApi.INSTANCE.top(authorization, "嗶咔AI推薦", 1).blockingFirst()
        Logger.json(gson.toJson(a))
        Assert.assertEquals(40, a.datas.size)

        val b = PicaApi.INSTANCE.top(authorization, "_D7", -1).blockingFirst()
        Logger.json(gson.toJson(b))
        Assert.assertEquals(20, b.datas.size)
    }

    @Test
    fun testRecent() {
        val authorization = PicaApi.INSTANCE.login(email, password).blockingFirst()
        Logger.v(authorization)
        val a = PicaApi.INSTANCE.recent(authorization, 0).blockingFirst()
        Logger.json(gson.toJson(a))
        Assert.assertNotEquals(0, a.datas.size)
    }

    @Test
    fun testTop2() {

        PicaApi.INSTANCE.login(email, password).blockingFirst()
        val a = PicaApi.INSTANCE.top("嗶咔AI推薦", 1).blockingFirst()
        Logger.json(gson.toJson(a))
        Assert.assertEquals(40, a.datas.size)

        val b = PicaApi.INSTANCE.top("_D7", -1).blockingFirst()
        Logger.json(gson.toJson(b))
        Assert.assertEquals(20, b.datas.size)
    }

    @Test
    fun testInfo() {
        val authorization = PicaApi.INSTANCE.login(email, password).blockingFirst()
        Logger.v(authorization)
        val result = PicaApi.INSTANCE.info(authorization, "5d2174f8a6fcef4c68750ce3").blockingFirst()
        Logger.json(gson.toJson(result))
        Assert.assertEquals("異世界ハーレム物語 異世界淫亂後宮物語Ⅰ", result.info.title)
    }

    @Test
    fun testInfo2() {

        PicaApi.INSTANCE.login(email, password).blockingFirst()
        val result = PicaApi.INSTANCE.info("5d2174f8a6fcef4c68750ce3").blockingFirst()
        Logger.json(gson.toJson(result))
        Assert.assertEquals("異世界ハーレム物語 異世界淫亂後宮物語Ⅰ", result.info.title)
    }

    @Test
    fun testComment() {
        val authorization = PicaApi.INSTANCE.login(email, password).blockingFirst()
        Logger.v(authorization)
        val result = PicaApi.INSTANCE.comment(authorization, "5cf2a9026ca8f8704c63f250", 2).blockingFirst()
        Logger.json(gson.toJson(result))

        Assert.assertEquals(22, result.pages)
        Assert.assertEquals(2, result.currentPage)
    }


    @Test
    fun testComment2() {

        PicaApi.INSTANCE.login(email, password).blockingFirst()
        val result = PicaApi.INSTANCE.comment("5cf2a9026ca8f8704c63f250", 2).blockingFirst()
        Logger.json(gson.toJson(result))

        Assert.assertEquals(22, result.pages)
        Assert.assertEquals(2, result.currentPage)

    }

    @Test
    fun testData() {
        val authorization = PicaApi.INSTANCE.login(email, password).blockingFirst()
        Logger.v(authorization)
        val a = PicaApi.INSTANCE.data(authorization, "5821aad15f6b9a4f93f443b5", "6").blockingFirst()
        Logger.json(gson.toJson(a))
        Assert.assertEquals(25, a.data.size)

        val b = PicaApi.INSTANCE.data(authorization, "5d2174f8a6fcef4c68750ce3", "1").blockingFirst()
        Logger.json(gson.toJson(b))
        Assert.assertEquals(177, b.data.size)
    }

    @Test
    fun testData2() {

        PicaApi.INSTANCE.login(email, password).blockingFirst()

        val a = PicaApi.INSTANCE.data("5821aad15f6b9a4f93f443b5", "6").blockingFirst()
        Logger.json(gson.toJson(a))
        Assert.assertEquals(25, a.data.size)

        val b = PicaApi.INSTANCE.data("5d2174f8a6fcef4c68750ce3", "1").blockingFirst()
        Logger.json(gson.toJson(b))
        Assert.assertEquals(177, b.data.size)
    }

    @Test
    fun testRaw() {
        val result = PicaApi.INSTANCE.raw("abcd").blockingFirst()
        Assert.assertEquals("abcd", result)
    }

    @Test
    fun testGetUrl() {
        val result = PicaApi.INSTANCE.pageUrl("")
        Assert.assertEquals("", result)
    }

    @Test
    fun testLogin() {
        val result = PicaApi.INSTANCE.login(email, password).blockingFirst()
        Logger.v(result)
        Assert.assertNotNull(result)
    }

    @Test
    fun testNoAuth() {
        val b = PicaApi.INSTANCE.top("", -1).blockingFirst()
    }
}
