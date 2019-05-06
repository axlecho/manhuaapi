package com.axlecho.api

import com.axlecho.api.hanhan.MHApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog
import com.axlecho.api.pica.api.PicaLoginApi
import com.axlecho.api.pica.utils.NetUtil
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import java.net.InetSocketAddress
import java.net.Proxy
import com.axlecho.api.pica.PicaHeader
import com.axlecho.api.pica.api.PicaSearchApi
import com.axlecho.api.pica.results.book.PicaBooksResult
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.PrettyFormatStrategy


@RunWith(RobolectricTestRunner::class)

class PicaApiTest {
    private val username = ""
    private val password = ""

    @Test
    fun testBase() {
        Assert.assertTrue("base test", 1 + 1 == 2)
    }

    @Before
    fun setup() {
        ShadowLog.stream = System.out
        NetUtil.config(MHApi.INSTANCE.standardBuilder()
                .proxy(Proxy(Proxy.Type.SOCKS, InetSocketAddress("127.0.0.1", 1080)))
                .build())
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(1)
                .tag("API_TEST")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))

    }

    @Test
    fun testLogin(){
        Assert.assertTrue("请手动指定你的Pica账号密码",username.isNotBlank() && password.isNotBlank())
        val lapi = PicaLoginApi(username, password)
        val result = lapi.login()
        Logger.json(Gson().toJson(result.resultJson))
    }

    @Test
    fun testSearch() {
        Assert.assertTrue("请手动指定你的Pica账号密码",username.isNotBlank() && password.isNotBlank())
        val lapi = PicaLoginApi(username, password)
        val result = lapi.login()

        val header = PicaHeader()
        header.authorization = result.token
        val sapi = PicaSearchApi(header)
        val sresult = sapi.search("神")
        Logger.json(Gson().toJson(sresult.comics))
    }
}
