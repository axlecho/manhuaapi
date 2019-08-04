package com.axlecho.api

import com.axlecho.api.manhuagui.ManhuaguiApi
import com.axlecho.api.untils.MHHttpsUtils
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
                .addInterceptor(MHHttpsUtils.CHROME_HEADER)
                // .callTimeout(180000, TimeUnit.MILLISECONDS)
                // .readTimeout(180000, TimeUnit.MILLISECONDS)
                // .proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress("127.0.0.1", 1080)))
                .build())
        MHApi.context = TestMHContext()

    }

    @Test
    fun testSearch() {
        val a = ManhuaguiApi.INSTANCE.search("辉夜", 0).blockingFirst()
        Logger.json(gson.toJson(a))
        Assert.assertNotEquals(0, a.datas.size)

        val b = ManhuaguiApi.INSTANCE.search("表哥的搬家入住整理没法进行啦", 0).blockingFirst()
        Logger.json(gson.toJson(b))
        Assert.assertNotEquals(0, b.datas.size)

    }

    @Test
    fun testRating() {
        val result = ManhuaguiApi.INSTANCE.score("17332").blockingFirst()
        Logger.json(gson.toJson(result))
        Assert.assertTrue(result.success)
    }

    @Test
    fun testInfo() {
        val result = ManhuaguiApi.INSTANCE.info("17332").blockingFirst()
        Logger.json(gson.toJson(result))
        Assert.assertEquals("辉夜姬想让人告白~天才们的恋爱头脑战~", result.info.title)

    }

    @Test
    fun testComment() {
        val result = ManhuaguiApi.INSTANCE.comment("17332", 2).blockingFirst()
        Logger.json(gson.toJson(result))
        Assert.assertEquals(2, result.currentPage)
        Assert.assertEquals(30, result.datas.size)
    }

    @Test
    fun testData() {
        val result = ManhuaguiApi.INSTANCE.data("17332", "183592").blockingFirst()
        Logger.json(gson.toJson(result))
        Assert.assertEquals(20, result.data.size)

    }

    @Test
    fun testRaw() {
        val result = ManhuaguiApi.INSTANCE.raw("https://www.manhuagui.com/comic/17332/181375.html#p=1").blockingFirst()
        Logger.v(result)
        Assert.assertEquals("https://www.manhuagui.com/comic/17332/181375.html#p=1", result)
    }

    @Test
    fun testTop() {
        var result = ManhuaguiApi.INSTANCE.top("", -1).blockingFirst()
        Logger.json(gson.toJson(result))
        Assert.assertEquals(50, result.datas.size)
    }


    @Test
    fun testTopWithCategory() {
        val result = ManhuaguiApi.INSTANCE.category().time("周排行").category("其它").top(-1).blockingFirst()
        Logger.json(gson.toJson(result))
        Assert.assertEquals(17, result.datas.size)
    }

    @Test
    fun testCategory() {
        Assert.assertEquals("", ManhuaguiApi.INSTANCE.category().time("日排行").category("总").build())
        Assert.assertEquals("other_week.html", ManhuaguiApi.INSTANCE.category().time("周排行").category("其它").build())
        Assert.assertEquals("zhanzheng.html", ManhuaguiApi.INSTANCE.category().time("日排行").category("战争").build())
        Assert.assertEquals("total.html", ManhuaguiApi.INSTANCE.category().time("总排行").category("总").build())
    }


    @Test
    fun testGetCategory() {
        var ret = ManhuaguiApi.INSTANCE.category().getTimes()
        Logger.json(gson.toJson(ret))

        ret = ManhuaguiApi.INSTANCE.category().getCategorys()
        Logger.json(gson.toJson(ret))
    }
    @Test
    fun testRecent() {
        val result = ManhuaguiApi.INSTANCE.recent(-1).blockingFirst()
        Logger.json(gson.toJson(result))
        Assert.assertNotEquals(0, result.datas.size)
    }

    @Test
    fun testGetUrl() {
        val result = ManhuaguiApi.INSTANCE.pageUrl("17332")
        Logger.v(result)
        Assert.assertEquals("https://www.manhuagui.com/comic/17332", result)
    }
}
