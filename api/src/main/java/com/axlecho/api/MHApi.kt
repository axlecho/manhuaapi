package com.axlecho.api

import com.axlecho.api.module.comic.MHComic
import com.axlecho.api.module.comic.MHComicChapter
import com.axlecho.api.module.comic.MHComicData
import com.axlecho.api.module.comic.MHComicInfo
import com.axlecho.api.module.rank.MHModuleRankPage
import com.axlecho.api.parser.MHParser
import com.axlecho.api.soup.Node
import com.axlecho.api.untils.MHHttpsUtils
import com.axlecho.api.untils.StringUtils
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


class MHApi private constructor() {
    companion object {
        val INSTANCE: MHApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MHApi()
        }
    }

    private var site: MHNetwork = Retrofit.Builder().baseUrl(MHConstant.HOST).build().create(MHNetwork::class.java)

    init {
        this.config(standardBuilder().build())
    }

    fun standardBuilder(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.HEADERS

        builder.addNetworkInterceptor(logging)
        // builder.addInterceptor(logging)

        val headerInterceptor = Interceptor { chain ->
            val newRequest = chain.request().newBuilder()
                    .addHeader("user-agent", MHConstant.USER_AGENT)
                    .addHeader("Referer", MHConstant.HOST)
                    .build()
            chain.proceed(newRequest)
        }
        builder.addInterceptor(headerInterceptor)

        // for https
        builder.hostnameVerifier(MHHttpsUtils.instance.createHostnameVerifier())
        builder.sslSocketFactory(MHHttpsUtils.instance.createSSLSocketFactory(), MHHttpsUtils.instance.trustManager)
        return builder;
    }

    fun config(client: OkHttpClient) {
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(MHConstant.HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        site = retrofit.create(MHNetwork::class.java)
    }


    private fun unsuan(str: String): String {
        var str = str
        val num = str.length - str[str.length - 1].toInt() + 'a'.toInt()
        var code = str.substring(num - 13, num - 2)
        val cut = code.substring(code.length - 1)
        str = str.substring(0, num - 13)
        code = code.substring(0, code.length - 1)
        for (i in 0 until code.length) {
            str = str.replace(code[i], ('0'.toInt() + i).toChar())
        }
        val builder = StringBuilder()
        val array = str.split(cut)
        for (i in array.indices) {
            builder.append(Integer.parseInt(array[i]).toChar())
        }
        return builder.toString()
    }

    private fun isFinish(text: String?): Boolean {
        return text != null && (text.contains("完结") || text.contains("Completed"))
    }


    fun rank(category: String): Observable<MHModuleRankPage> {
        var url = category
        if (category != "") {
            url = "$category.html"
        }

        return site.getRank(url).map { res ->
            return@map MHParser.parseRank(res.string())
        }
    }

    fun search(keyword: String): Observable<List<MHComic>> {
        return site.search(keyword).map { res ->
            val html = res.string()
            Logger.v(html)
            val result = ArrayList<MHComic>()
            val body = Node(html)
            for (node in body.list("#list > div.cComicList > li > a")) {
                val cid = node.hrefWithSubString(7, -6)
                val title = node.text()
                val cover = node.src("img")
                result.add(MHComic(cid, title, cover))
//            val cover = node.src("a.pic > img")
//            val update = node.textWithSubstring("a.pic > div > p:eq(4) > span", 0, 10)
//            val author = node.text("a.pic > div > p:eq(1)")
            }
            return@map result
        }
    }

    fun info(cid: String): Observable<MHComicInfo> {
        return site.info(cid).map { res ->
            val html = res.string()
            Logger.v(html)
            val body = Node(html)

            val title = body.text("#about_kit > ul > li:eq(0) > h1")
            val cover = body.src("#about_style > img")
            var update = body.textWithSubstring("#about_kit > ul > li:eq(4)", 3)
            if (update != null) {
                val args = update!!.split("\\D".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                update = StringUtils.format("%4d-%02d-%02d", Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]))
            }
            val author = body.textWithSubstring("#about_kit > ul > li:eq(1)", 3)
            val intro = body.textWithSubstring("#about_kit > ul > li:eq(7)", 3)
            val status = isFinish(body.text("#about_kit > ul > li:eq(2)"))

            val list = ArrayList<MHComicChapter>()
            val name = body.text("#about_kit > ul > li:eq(0) > h1")
            for (node in body.list("#permalink > div.cVolList > ul.cVolUl > li > a")) {
                var chapterTitle = node.text()
                chapterTitle = chapterTitle.replaceFirst(name.toRegex(), "").trim { it <= ' ' }
                val array = StringUtils.match("/page(\\d+).*s=(\\d+)", node.attr("href"), 1, 2)
                //String path = array != null ? array[0].concat(" ").concat(array[1]) : "";
                val path = if (array != null) array[0] + "-" + array[1] else ""
                list.add(MHComicChapter(chapterTitle.trim { it <= ' ' }, path))
            }
            return@map MHComicInfo(title, cover, update, intro, author, status, list)
        }
    }

    fun data(cid: String, chapter: String): Observable<MHComicData> {
        val array = chapter.split("-")
        return site.data(array[0], array[1]).map { res ->
            val html = res.string()
            Logger.v(html)
            val list = ArrayList<String>()
            val body = Node(html)
            val page = Integer.parseInt(body.attr("#hdPageCount", "value"))
            val path = body.attr("#hdVolID", "value")
            val server = body.attr("#hdS", "value")
            for (i in 1..page) {
                list.add("http://www.hhmmoo.com/page$path/$i.html?s=$server")
            }
            return@map MHComicData(list)
        }
    }

    fun raw(url: String): Observable<String> {
        return site.raw(url).map { res ->
            val html = res.string()
            Logger.v(html)
            val body = Node(html)
            var server = body.attr("#hdDomain", "value")
            if (server != null) {
                server = server.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                val name = body.attr("#iBodyQ > img", "name")
                val result = unsuan(name).substring(1)
                return@map server + result
            }
            return@map ""
        }
    }


    fun top(category: String): Observable<List<MHComic>> {
        return site.top(category).map { res ->
            val html = res.string()
            Logger.v(html)
            val body = Node(html)
            val list = ArrayList<MHComic>()

            for (node in body.list("#list > div.cTopComicList > div.cComicItem")) {
                Logger.v(node.get().html())
                val title = node.text("span.cComicTitle")
                val cid = node.hrefWithSubString("div.cListSlt > a",7, -6)
                val cover = node.attr("img","src")
                list.add(MHComic(cid, title, cover))
            }
            return@map list
        }
    }
}