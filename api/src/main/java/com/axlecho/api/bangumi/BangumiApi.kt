package com.axlecho.api.bangumi

import android.graphics.Bitmap
import com.axlecho.api.*
import com.axlecho.api.bangumi.module.Captcha
import com.axlecho.api.untils.MHHttpsUtils
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class BangumiApi private constructor() : Api {
    companion object {
        val INSTANCE: BangumiApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BangumiApi()
        }
    }

    private var site: BangumiNetwork = Retrofit.Builder().baseUrl(MHConstant.BGM_HOST).build().create(BangumiNetwork::class.java)
    private var api: BangumiNetworkByApi = Retrofit.Builder().baseUrl(MHConstant.BGM_API).build().create(BangumiNetworkByApi::class.java)

    init {
        this.config(MHHttpsUtils.INSTANCE.standardBuilder().followRedirects(false).build())
    }


    fun config(client: OkHttpClient) {
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(MHConstant.BGM_HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        site = retrofit.create(BangumiNetwork::class.java)

        val apiRetrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(MHConstant.BGM_API)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        api = apiRetrofit.create(BangumiNetworkByApi::class.java)
    }

    override fun top(category: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        // fix page with +1 for bangumi start from 1
        return site.top(page + 1).map { res -> BangumiParser.parserComicList(res.string()) }
    }

    override fun search(keyword: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        // fix page with +1 for bangumi start from 1
        return site.search(keyword, page + 1).map { res -> BangumiParser.parserComicList(res.string()) }
    }

    override fun info(gid: Long): Observable<MHComicDetail> {
        return api.info(gid).map { res -> BangumiParser.parserInfo(res) }
    }

    override fun pageUrl(gid: Long): String {
        return MHConstant.HTTP_PROTOCOL_PREFIX + MHConstant.BGM_HOST + "/subject/" + gid
    }

    override fun data(gid: Long, chapter: String): Observable<MHComicData> {
        return Observable.empty()
    }

    override fun raw(url: String): Observable<String> {
        return Observable.empty()
    }

    override fun collection(id: String, page: Int): Observable<MHMutiItemResult<MHComicInfo>> {
        // fix page with +1 for bangumi start from 1
        return site.collection(id, page + 1).map { res -> BangumiParser.parserComicList(res.string()) }
    }

    fun collectionPages(id: String): Observable<Int> {
        return site.collection(id, 1).map { res -> BangumiParser.parserCollectionCount(res.string()) }
    }

    override fun comment(gid: Long, page: Int): Observable<MHMutiItemResult<MHComicComment>> {
        // fix page with +1 for bangumi start from 1
        return site.comments(gid, page + 1).map { res -> BangumiParser.parserComicComment(res.string()) }
    }

    fun login(email: String, password: String, captcha: Captcha, formhash: String): Observable<String> {
        return site.login("chii_sid=${captcha.chii_sid}", formhash, email, password, captcha.captcha).map { res -> BangumiParser.parserLogin(res) }
    }

    fun genSid(): Observable<String> {
        return site.genSid().map { res -> BangumiParser.parserSid(res) }
    }

    fun captcha(sid: String): Observable<Bitmap> {
        return captchaRaw(sid).map { res -> BangumiParser.parserCaptcha(res) }
    }

    fun captchaRaw(sid:String):Observable<ResponseBody> {
        val number = 1 + Math.floor(Math.random() * 6)
        val time = System.currentTimeMillis().toString() + number.toInt().toString()
        return site.captcha(time, "chii_sid=$sid")
    }
}