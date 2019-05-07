package com.axlecho.api

import com.axlecho.api.bangumi.BangumiApi
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

class BangumiApiTest {
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
    fun testCollectionPages() {
        val result = BangumiApi.INSTANCE.collectionPages("axlecho").blockingFirst()
        Logger.d(result)
    }

    @Test
    fun testCollection() {
        var result = BangumiApi.INSTANCE.collection("axlecho",1).blockingFirst()
        Logger.d(result)
    }

    @Test
    fun testGetAllCollection() {
        var items = BangumiApi.INSTANCE.collectionPages("axlecho").blockingFirst()
        var pages = Math.ceil(items /  25.0).toInt()
        for (i in 1..pages) {
            val result  = BangumiApi.INSTANCE.collection("axlecho",i).blockingFirst()
            Logger.d(result)
        }
    }

    @Test
    fun testInfo() {
        Logger.json(Gson().toJson(BangumiApi.INSTANCE.info(208146).blockingFirst()))
        Logger.json(Gson().toJson(BangumiApi.INSTANCE.info(231626).blockingFirst()))
        Logger.json(Gson().toJson(BangumiApi.INSTANCE.info(231163).blockingFirst()))
        Logger.json(Gson().toJson(BangumiApi.INSTANCE.info(270199).blockingFirst()))
        Logger.json(Gson().toJson(BangumiApi.INSTANCE.info(242027).blockingFirst()))
        Logger.json(Gson().toJson(BangumiApi.INSTANCE.info(35412).blockingFirst()))
        Logger.json(Gson().toJson(BangumiApi.INSTANCE.info(110465).blockingFirst()))

    }

    @Test
    fun testSearch() {
        var result =BangumiApi.INSTANCE.search("幽灵与社畜",1).blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertTrue(result.pages == 1)

        result = BangumiApi.INSTANCE.search("地下城",1).blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertTrue(result.pages == 34)

        result = BangumiApi.INSTANCE.search("地下城",20).blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertTrue(result.currentPage == 20)
    }

    @Test
    fun testComments() {
        val result = BangumiApi.INSTANCE.comment(119393,1).blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertTrue(result.pages == 5)
    }

    @Test
    fun testTop() {
        val result = BangumiApi.INSTANCE.top("",1).blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertTrue(result.pages == 999)
    }
}
