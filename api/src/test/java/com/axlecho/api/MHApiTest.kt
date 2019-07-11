package com.axlecho.api

import com.axlecho.api.bangumi.BangumiApi
import com.axlecho.api.hanhan.HHApi
import com.axlecho.api.untils.MHHttpsUtils
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
import java.net.InetSocketAddress
import java.net.Proxy


@RunWith(RobolectricTestRunner::class)

class MHApiTest {
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
        HHApi.INSTANCE.config(MHHttpsUtils.INSTANCE.standardBuilder()
                .proxy(Proxy(Proxy.Type.SOCKS, InetSocketAddress("127.0.0.1", 1080)))
                .build())
    }

    @Test
    fun testSwitchSource() {
        val test = MHApi.INSTANCE.info("231626").blockingSingle()
        Logger.json(Gson().toJson(test))
        val target = MHApi.INSTANCE.switchSource(test.info,MHApiSource.Hanhan).blockingSingle()
        Logger.json(Gson().toJson(target))
    }

    @Test
    fun testGetAllCollection(){
        MHApi.INSTANCE.getAllCollection("axlecho").blockingForEach { Logger.v("fetch " + it.currentPage) }
    }

}
