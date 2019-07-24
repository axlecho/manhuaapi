package com.axlecho.api

import com.axlecho.api.kuku.KuKuApi
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

class KukuApiTest {
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
        var result = KuKuApi.INSTANCE.info("2715").blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertEquals(result.info.title, "冷酷又可爱的未来新娘")

        result = KuKuApi.INSTANCE.info("2714").blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertEquals(result.info.title, "漆黑使的最强勇者")
    }

    @Test
    fun testSearch() {
        var result = KuKuApi.INSTANCE.search("幽灵与社畜", 0).blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertEquals(0, result.pages)
        Assert.assertEquals(0, result.currentPage)

        result = KuKuApi.INSTANCE.search("地下城", 0).blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertEquals(0, result.currentPage)
        Assert.assertEquals(1, result.pages)

        result = KuKuApi.INSTANCE.search("火", 1).blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertEquals(1, result.currentPage)
        Assert.assertEquals(2, result.pages)
    }


    @Test
    fun testTop() {
        val result = KuKuApi.INSTANCE.top("", 1).blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertTrue(result.datas.size == 30)
    }


    @Test
    fun testData() {
        var result = KuKuApi.INSTANCE.data("2715", "71835").blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertEquals(result.data.size, 25)
        Assert.assertEquals("http://comic.ikkdm.com/comiclist/2715/71835/1.htm", result.data[0])

        result = KuKuApi.INSTANCE.data("1953", "38891").blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertEquals(result.data.size, 32)
        Assert.assertEquals("http://comic.ikkdm.com/comiclist/1953/38891/1.htm", result.data[0])
    }

    @Test
    fun testRaw() {
        var result = KuKuApi.INSTANCE.raw("http://comic.ikkdm.com/comiclist/2715/71835/1.htm").blockingFirst()
        Logger.v(result)
        Assert.assertEquals("http://n9.1whour.com/newkuku/2019/06/11/n/冷酷而又可爱到不行的未来的新娘的麻烦的七天_第01话/00012CF.jpg", result)

        result = KuKuApi.INSTANCE.raw("http://comic.ikkdm.com/comiclist/1953/38891/1.htm").blockingFirst()
        Logger.v(result)
        Assert.assertEquals("http://n9.1whour.com/newkuku/2014/201403/20140316/深红累之渊][第1话/00103O.jpg", result)

    }

    @Test
    fun testGetUrl() {
        val result = KuKuApi.INSTANCE.pageUrl("2715")
        Logger.d(result)
        Assert.assertEquals(result, "http://comic.ikkdm.com/comiclist/2715/index.htm")
    }

}
