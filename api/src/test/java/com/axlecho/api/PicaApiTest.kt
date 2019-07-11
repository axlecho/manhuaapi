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

    val gson = GsonBuilder().setPrettyPrinting().create()
    val email = "Your test email"
    val password = "Your test password"

    @Test
    fun testBase() {
        Assert.assertTrue("base test", 1 + 1 == 2)
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
                .proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress("127.0.0.1", 8888)))
                .build())
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
    fun testComment() {
        val authorization = PicaApi.INSTANCE.login(email, password).blockingFirst()
        Logger.v(authorization)
        val result = PicaApi.INSTANCE.comment(authorization, "5cf2a9026ca8f8704c63f250", 2).blockingFirst()
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
    fun testRaw() {
        val result = PicaApi.INSTANCE.raw("abcd").blockingFirst()
        Assert.assertEquals("abcd", result)
    }

    @Test
    fun testTop() {
        val authorization = PicaApi.INSTANCE.login(email, password).blockingFirst()
        Logger.v(authorization)
        val b = PicaApi.INSTANCE.top(authorization, "", -1).blockingFirst()
        Logger.json(gson.toJson(b))
        Assert.assertEquals(40, b.datas.size)
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
    fun testSearch() {
        val authorization = PicaApi.INSTANCE.login(email, password).blockingFirst()
        Logger.v(authorization)
        val b = PicaApi.INSTANCE.search(authorization, "辉夜", 0).blockingFirst()
        Logger.json(gson.toJson(b))

        Assert.assertNotNull(b.datas)
        Assert.assertNotEquals(0, b.datas.size)
    }
}
