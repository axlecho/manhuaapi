package com.axlecho.api

import com.axlecho.api.manhuagui.ManhuaguiApi
import com.axlecho.api.untils.MHHttpsUtils
import com.google.gson.Gson
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
import java.util.concurrent.TimeUnit


@RunWith(RobolectricTestRunner::class)

class ManhuaguiApiTest {

    val gson = GsonBuilder().setPrettyPrinting().create()
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
        ManhuaguiApi.INSTANCE.config(MHHttpsUtils.INSTANCE.standardBuilder()
                .callTimeout(180000,TimeUnit.MILLISECONDS)
                .readTimeout(180000,TimeUnit.MILLISECONDS)
                .proxy(Proxy(Proxy.Type.SOCKS, InetSocketAddress("127.0.0.1", 1080)))
                .build())
    }

    @Test
    fun testSearch() {
        val result = ManhuaguiApi.INSTANCE.search("辉夜", 0).blockingFirst()
        Logger.json(gson.toJson(result))
        val ret = ManhuaguiApi.INSTANCE.search("表哥的搬家入住整理没法进行啦",0).blockingFirst()
        Logger.json(gson.toJson(ret))
    }

    @Test
    fun testRating() {
        val result = ManhuaguiApi.INSTANCE.score(17332).blockingFirst()
        Logger.json(gson.toJson(result))
    }

    @Test
    fun testInfo() {
        val result = ManhuaguiApi.INSTANCE.info(17332).blockingFirst()
        Logger.json(gson.toJson(result))
        
        val ret = ManhuaguiApi.INSTANCE.info(25388).blockingFirst()
        Logger.json(gson.toJson(ret))
    }

    @Test
    fun testComment() {
        val result = ManhuaguiApi.INSTANCE.comment(17332,1).blockingFirst()
        Logger.json(gson.toJson(result))
    }
    
    @Test
    fun testData() {
        val result = ManhuaguiApi.INSTANCE.data(17332, "183592").blockingFirst()
        Logger.json(gson.toJson(result))
    }

    @Test
    fun testRaw() {
        val result = ManhuaguiApi.INSTANCE.raw("https://www.manhuagui.com/comic/17332/181375.html#p=1").blockingFirst()
        // Logger.json(gson.toJson(result))
        Logger.v(result)
    }

    @Test
    fun testTop() {
        val result = ManhuaguiApi.INSTANCE.top("hotrating", -1).blockingFirst()
        Logger.json(gson.toJson(result))
    }

    @Test
    fun testGetUrl() {
        val result = ManhuaguiApi.INSTANCE.pageUrl(27519)
        Logger.json(gson.toJson(result))
    }
}
