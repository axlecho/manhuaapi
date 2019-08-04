package com.axlecho.api

import com.axlecho.api.manhuadui.ManhuaduiApi
import com.axlecho.api.manhuadui.ManhuaduiParser
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

class ManhuaduiApiTest {

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



        ManhuaduiApi.INSTANCE.config(MHHttpsUtils.INSTANCE.standardBuilder()
                .addInterceptor(MHHttpsUtils.CHROME_HEADER)
                .build())
    }

    @Test
    fun testSearch() {
        val a = ManhuaduiApi.INSTANCE.search("辉夜", 0).blockingFirst()
        Logger.json(gson.toJson(a))
        Assert.assertNotEquals(0, a.datas.size)

        val b = ManhuaduiApi.INSTANCE.search("表哥的搬家入住整理没法进行啦", 0).blockingFirst()
        Logger.json(gson.toJson(b))
        Assert.assertNotEquals(0, b.datas.size)

        val c = ManhuaduiApi.INSTANCE.search("一", 0).blockingFirst()
        Logger.json(gson.toJson(c))
        Assert.assertNotEquals(0, c.datas.size)
    }

    @Test
    fun testTop() {
        val result = ManhuaduiApi.INSTANCE.top("", -1).blockingFirst()
        Logger.json(gson.toJson(result))
        Assert.assertEquals(10, result.datas.size)
    }

    @Test
    fun testTopWithCategory() {
        val result = ManhuaduiApi.INSTANCE.category().time("周").category("点击排行榜").top(-1).blockingFirst()
        Logger.json(gson.toJson(result))
        Assert.assertEquals(10, result.datas.size)
    }

    @Test
    fun testCategory() {
        Assert.assertEquals("click-/", ManhuaduiApi.INSTANCE.category().time("总").category("点击排行榜").build())
        Assert.assertEquals("popularity-/", ManhuaduiApi.INSTANCE.category().time("总").category("人气排行榜").build())
        Assert.assertEquals("click-weekly/", ManhuaduiApi.INSTANCE.category().time("周").category("点击排行榜").build())
        Assert.assertEquals("popularity-monthly/", ManhuaduiApi.INSTANCE.category().time("月").category("人气排行榜").build())
    }

    @Test
    fun testGetCategory() {
        var ret = ManhuaduiApi.INSTANCE.category().getTime()
        Logger.json(gson.toJson(ret))

        ret = ManhuaduiApi.INSTANCE.category().getCategorys()
        Logger.json(gson.toJson(ret))
    }

    @Test
    fun testInfo() {
        val result = ManhuaduiApi.INSTANCE.info("huiyedaxiaojiexiangrangwogaobai").blockingFirst()
        Logger.json(gson.toJson(result))
        Assert.assertEquals("辉夜大小姐想让我告白", result.info.title)
        Assert.assertEquals("huiyedaxiaojiexiangrangwogaobai", result.info.gid)
    }


    @Test
    fun testData() {
        val result = ManhuaduiApi.INSTANCE.data("huiyedaxiaojiexiangrangwogaobai", "178538").blockingFirst()
        Logger.json(gson.toJson(result))
        Assert.assertEquals(23, result.data.size)

    }

    @Test
    fun testRaw() {
        val result = ManhuaduiApi.INSTANCE.raw("https://mhcdn.manhuazj.com/ManHuaKu/h/huiyedaxiaojiexiangrangwogaobaitiancaimendelianait/1/201944768.jpg").blockingFirst()
        Logger.v(result)
        Assert.assertEquals("https://mhcdn.manhuazj.com/ManHuaKu/h/huiyedaxiaojiexiangrangwogaobaitiancaimendelianait/1/201944768.jpg", result)
    }

    @Test
    fun testComment() {

    }


    @Test
    fun testRecent() {
        val result = ManhuaduiApi.INSTANCE.recent(0).blockingFirst()
        Logger.json(gson.toJson(result))
        Assert.assertNotEquals(0, result.datas.size)
    }

    @Test
    fun testGetUrl() {
        val result = ManhuaduiApi.INSTANCE.pageUrl("huiyedaxiaojiexiangrangwogaobai")
        Logger.v(result)
        Assert.assertEquals("https://www.manhuadui.com/manhua/huiyedaxiaojiexiangrangwogaobai/", result)
    }

    @Test
    fun testDecode() {
        val testdata = "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"
        val result = ManhuaduiParser.decode(testdata)
        Logger.v(result)
    }
}
