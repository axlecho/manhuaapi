package com.axlecho.api

import com.axlecho.api.bangumi.BangumiApi
import com.axlecho.api.bangumi.module.Captcha
import com.google.gson.Gson
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.shadows.ShadowLog
import java.io.File
import java.io.FileOutputStream


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
        var result = BangumiApi.INSTANCE.collection("axlecho", 1).blockingFirst()
        Logger.d(result)
    }

    @Test
    fun testGetAllCollection() {
        var items = BangumiApi.INSTANCE.collectionPages("axlecho").blockingFirst()
        var pages = Math.ceil(items / 25.0).toInt()
        for (i in 1..pages) {
            val result = BangumiApi.INSTANCE.collection("axlecho", i).blockingFirst()
            Logger.d(result)
        }
    }

    @Test
    fun testInfo() {
        Logger.json(Gson().toJson(BangumiApi.INSTANCE.info(208146).blockingFirst()))

    }

    @Test
    fun testSearch() {
        var result = BangumiApi.INSTANCE.search("幽灵与社畜", 1).blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertTrue(result.pages == 1)

        result = BangumiApi.INSTANCE.search("地下城", 1).blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertTrue(result.pages == 34)

        result = BangumiApi.INSTANCE.search("地下城", 20).blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertTrue(result.currentPage == 20)
    }

    @Test
    fun testComments() {
        val result = BangumiApi.INSTANCE.comment(119393, 1).blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertTrue(result.pages == 5)
    }

    @Test
    fun testTop() {
        val result = BangumiApi.INSTANCE.top("", 1).blockingFirst()
        Logger.json(Gson().toJson(result))
        Assert.assertTrue(result.pages == 999)
    }

    @Test
    fun testLogin() {
        val captcha = Captcha("i8as8", "FvL211")
        val formhash = "7f5b70f7"
        val chiiAuth = BangumiApi.INSTANCE.login("axlecho@126.com", "bangumi123", captcha, formhash).blockingFirst()
        Logger.v(chiiAuth)
        Assert.assertTrue(chiiAuth.isNotBlank())
    }

    // @Test
    // decodeStream in robolectric seem is not work
    fun testCaptcha() {
        val sid = BangumiApi.INSTANCE.genSid().blockingFirst()
        val captcha = BangumiApi.INSTANCE.captcha(sid).blockingFirst()
    }

    @Test
    fun testCaptchaRaw() {
        val sid = BangumiApi.INSTANCE.genSid().blockingFirst()
        val captcha = BangumiApi.INSTANCE.captchaRaw(sid).blockingFirst()
        val f = File(RuntimeEnvironment.application.cacheDir, "captcha.png")
        Logger.v(f.absolutePath)
        val fos = FileOutputStream(f)
        val buffer = ByteArray(1024)
        var read = captcha.byteStream().read(buffer)

        while (read != -1) {
            fos.write(buffer, 0, read)
            read = captcha.byteStream().read(buffer)
        }
        fos.flush()
    }

    @Test
    fun testCheckLogin() {
        var result = BangumiApi.INSTANCE.checkLogin("").blockingFirst()
        Assert.assertFalse(result)

        val sid = "2WQwH8"
        val captcha = Captcha("abvvx", sid)
        val formhash = "7f5b70f7"
        val chiiAuth = BangumiApi.INSTANCE.login("axlecho@126.com", "bangumi123", captcha, formhash).blockingFirst()
        Logger.v(chiiAuth)
        Assert.assertTrue(chiiAuth.isNotBlank())

        result = BangumiApi.INSTANCE.checkLogin(sid).blockingFirst()
        Assert.assertTrue(result)
    }
}
