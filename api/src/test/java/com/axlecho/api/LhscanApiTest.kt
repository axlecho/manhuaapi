package com.axlecho.api

import com.axlecho.api.lhscan.LhscanApi
import com.google.gson.Gson
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog


@RunWith(RobolectricTestRunner::class)

class LhscanApiTest {
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
    }

    @Test
    fun testInfo() {
        var result = LhscanApi.INSTANCE.info("manga-kaguya-sama-wa-kokurasetai-tensai-tachi-no-renai-zunousen-raw").blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertEquals(result.info.title, "KAGUYA-SAMA WA KOKURASETAI - TENSAI-TACHI NO RENAI ZUNOUSEN")
    }

    @Test
    fun testSearch() {
        var result = LhscanApi.INSTANCE.search("KAGUYA", 0).blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertEquals(1, result.pages)
        Assert.assertEquals(1, result.currentPage)


        result = LhscanApi.INSTANCE.search("world", 0).blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertEquals(2, result.pages)
        Assert.assertEquals(1, result.currentPage)
    }


    @Test
    fun testTop() {
        val result = LhscanApi.INSTANCE.top("", 4).blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertTrue(result.datas.size == 20)
    }


    @Test
    fun testData() {
        var result = LhscanApi.INSTANCE.data("manga-kaguya-sama-wa-kokurasetai-tensai-tachi-no-renai-zunousen-raw",
                "read-kaguya-sama-wa-kokurasetai-tensai-tachi-no-renai-zunousen-raw-chapter-161").blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertEquals(19,result.data.size )
        Assert.assertEquals("https://s4.lhscanlation.club/images/20191205/2020No1-p072_5de83e5d7b781.jpg", result.data[0])
    }

    @Test
    fun testRaw() {
        var result = LhscanApi.INSTANCE.raw("a").blockingFirst()
        Assert.assertEquals(result, "a")

    }

    @Test
    fun testGetUrl() {
        val result = LhscanApi.INSTANCE.pageUrl("manga-kaguya-sama-wa-kokurasetai-tensai-tachi-no-renai-zunousen-raw")
        Logger.d(result)
        Assert.assertEquals(result, "https://lhscan.net/manga-kaguya-sama-wa-kokurasetai-tensai-tachi-no-renai-zunousen-raw.html")
    }

}
