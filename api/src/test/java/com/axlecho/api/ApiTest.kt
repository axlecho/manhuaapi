package com.axlecho.api

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

class ApiTest {
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
        MHApi.INSTANCE.config(MHApi.INSTANCE.standardBuilder()
                .proxy(Proxy(Proxy.Type.SOCKS, InetSocketAddress("127.0.0.1", 1080)))
                .build())
    }

    @Test
    fun testSearch() {
        var result = MHApi.INSTANCE.search("辉夜").blockingFirst()
        Logger.d(result)
    }

    @Test
    fun textInfo() {
        var result = MHApi.INSTANCE.info("27519").blockingFirst()
        Logger.d(result)
    }

    @Test
    fun testData() {
        var result = MHApi.INSTANCE.data("27519", "277787-3").blockingFirst()
        Logger.d(result)
    }

    @Test
    fun testRaw() {
        var result = MHApi.INSTANCE.raw("http://www.hhmmoo.com/page277787/1.html?s=3").blockingFirst()
        Logger.d(result)
    }

    @Test
    fun testTop() {
        var result = MHApi.INSTANCE.top("hotrating").blockingFirst()
        Logger.d(result)
    }
}
