package com.axlecho.api

import com.axlecho.api.bangumi.BangumiApi
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
}
