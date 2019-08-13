package com.axlecho.api.bangumi

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import com.axlecho.api.*
import com.axlecho.api.bangumi.module.BangumiComicInfo
import com.axlecho.api.bangumi.module.BangumiSearchInfo
import com.axlecho.api.untils.MHNode
import com.axlecho.api.untils.MHStringUtils
import com.orhanobut.logger.Logger
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.*


class BangumiParser {
    companion object {
        private val Tag: String = BangumiParser::javaClass.name

        fun String.filterDigital(): String {
            return this.replace("[^0-9]".toRegex(), "");
        }

        fun parserPages(body: MHNode): Int {
            var pages = 1
            for (node in body.list("div.page_inner > a.p")) {
                val pagesString = node.text()
                if (pagesString.toIntOrNull() != null) {
                    if (pages < pagesString.toInt()) {
                        pages = pagesString.toInt()
                    }
                }

                if (pagesString == "›|") {
                    val end = MHStringUtils.match("page=(\\d+)", node.href(), 1).toInt()
                    if (pages < end) {
                        pages = end
                    }
                }
            }
            return pages
        }

        fun parserCurrentPage(body: MHNode): Int {
            // Logger.v(body.text("div.page_inner > strong.p_cur") ?: "没有分页条")
            return body.text("div.page_inner > strong.p_cur")?.toInt() ?: 1
        }

        fun parserComicList(html: String): MHMutiItemResult<MHComicInfo> {
            // Logger.v(html)
            val result = ArrayList<MHComicInfo>()

            val body = MHNode(html)
            for (node in body.list("ul#browserItemList > li")) {
                // Logger.v(node.get().html())
                val gid = node.attr("id").filterDigital()
                val title = node.text("div.inner > h3 > a.l") ?: ""
                val titleJpn = node.text("div.inner > h3 > small.grey") ?: ""
                val thumb = "http:" + node.src("a.subjectCover > span.image > img.cover")
                val category = -1
                val posted = node.text("div.inner > p.collectInfo > span.tip_j") ?: ""
                val uploader = node.text("div.inner > p.info") ?: ""
                val rating = node.text("div.inner > p.rateInfo > small.fade")?.toFloatOrNull() ?: 0.0f
                val rated = rating == 0.0f
                result.add(MHComicInfo(gid, title, titleJpn, thumb, category, posted,
                        uploader, rating, rated, MHApiSource.Bangumi))
            }

            var pages = parserPages(body)
            var currentPage = parserCurrentPage(body) - 1
            return MHMutiItemResult(result, pages, currentPage)
        }

        fun parserCollectionCount(html: String): Int {
            val body = MHNode(html)
            val countString = body.text("div#headerProfile > div.subjectNav > div.navSubTabsWrapper > ul.navSubTabs > li:eq(2)")
                    .filterDigital()
            // Logger.v(countString)

            if (TextUtils.isDigitsOnly(countString)) {
                return countString.toInt()
            }
            return -1
        }

        fun parserComicComment(html: String): MHMutiItemResult<MHComicComment> {
            val result = ArrayList<MHComicComment>()
            val body = MHNode(html)
            for (node in body.list("div#comment_box > div.item")) {
                // Logger.v(node.get().html())
                val id = node.attr("a.avatar", "href").replace("/user/", "")
                var score = 0
                if (node.attr("div.text > span.starsinfo", "class") != null) {
                    score = node.attr("div.text > span.starsinfo", "class").filterDigital().toInt()
                }
                val time = node.text("div.text > small.grey").replace("@", "").trim()
                val user = node.text("div.text > a.l")
                val comment = node.text("div.text > p")
                result.add(MHComicComment(id, score, time, user, comment, MHApiSource.Bangumi))
            }
            var pages = parserPages(body)
            var currentPage = parserCurrentPage(body) - 1
            return MHMutiItemResult(result, pages, currentPage)
        }

        fun parserInfo(info: BangumiComicInfo,comment: MHMutiItemResult<MHComicComment>): MHComicDetail {
            val gid = info.id.toString()
            val title = info.name_cn
            val titleJpn = info.name
            val thumb = info.images.common
            val category = -1
            val posted = ""
            var uploader = ""
            info.staff
            if (info.staff != null && info.staff.isNotEmpty()) {
                uploader = if (info.staff[0].name_cn.isNotEmpty()) info.staff[0].name_cn else info.staff[0].name
            }

            val rating = info.rating.score
            val ratingCount = info.rating.total
            val rated = ratingCount > 0
            val mhinfo = MHComicInfo(gid, title, titleJpn, thumb, category, posted, uploader, rating, rated, MHApiSource.Bangumi)

            val intro = info.summary
            val chapterCount = 0
            val favoriteCount = info.collection.wish + info.collection.collect + info.collection.doing
            val isFavorited = false
            val chapters = ArrayList<MHComicChapter>()
            val comments = ArrayList<MHComicComment>()
            comments.addAll(comment.datas)
            val updateTime = 0L
            return MHComicDetail(mhinfo, intro, chapterCount, favoriteCount, isFavorited, ratingCount, chapters, comments, MHApiSource.Bangumi, updateTime)
        }

        fun parserComicListByApi(info: BangumiSearchInfo): MHMutiItemResult<MHComicInfo> {
            val result = ArrayList<MHComicInfo>()
            for (i in info.list) {
                val gid = i.id.toString()
                val title = if (i.name_cn.isNotBlank()) i.name_cn else i.name
                val titleJpn = i.name
                val thumb = i.images.common
                val category = -1
                val posted = i.air_date
                val uploader = ""
                var rating = 0.0f
                var rated = false
                if (i.rating != null) {
                    rating = i.rating.score
                    rated = i.rating.total > 0
                }
                result.add(MHComicInfo(gid, title, titleJpn, thumb, category, posted, uploader, rating, rated, MHApiSource.Bangumi))
            }

            var pages = Math.ceil(info.results / 25.0).toInt()
            var currentPage = 1

            return MHMutiItemResult(result, pages, currentPage)
        }

        fun parserLogin(res: Response<ResponseBody>): String {
            Logger.d(res.code())
            Logger.d(res.headers().get("Set-Cookie"))
            Logger.d(res.body()?.string() ?: "空Body")

            for (cookie in res.headers().values("Set-Cookie")) {
                if (cookie.contains("chii_auth")) {
                    return cookie
                }
            }
            return ""
        }

        fun parserSid(res: Response<ResponseBody>): String {
            for (cookie in res.headers().values("Set-Cookie")) {
                if (cookie.contains("chii_sid")) {
                    return cookie.split(";")[0].split("=")[1]
                }
            }
            return ""
        }

        fun parserIsLogin(res: Response<ResponseBody>): Boolean {
            return res.code() != 200
        }

        fun parserCaptcha(res: ResponseBody): Bitmap {
            return BitmapFactory.decodeStream(res.byteStream())
        }
    }
}