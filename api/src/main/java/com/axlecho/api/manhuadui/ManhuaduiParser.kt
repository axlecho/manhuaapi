package com.axlecho.api.manhuadui

import android.util.Base64
import com.axlecho.api.*
import com.axlecho.api.MHConstant.Companion.MANHUADUI_HOST
import com.axlecho.api.MHConstant.Companion.UNKNOWN_TIME
import com.axlecho.api.untils.MHNode
import com.axlecho.api.untils.MHStringUtils
import com.axlecho.api.untils.tranferTimeManhuadui
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import java.nio.charset.Charset
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class ManhuaduiParser {
    companion object {
        private val Tag: String = ManhuaduiParser::javaClass.name
        private fun String.filterDigital(): String {
            return this.replace("[^0-9]".toRegex(), "");
        }

        private fun String.filterGid():String{
            return this.replace("$MANHUADUI_HOST/manhua/", "").replace("/", "").trim()
        }

        fun parserSearchComicList(html: String): MHMutiItemResult<MHComicInfo> {

            val body = MHNode(html)
            val currentPage = body.text("div.bottom_page > ul.pagination > li.active")?.toInt() ?: 1
            var pages = body.attr("div.bottom_page > ul.pagination > li.last > a", "data-page")?.toInt() ?: 0
            pages += 1

            val result = ArrayList<MHComicInfo>()
            for (node in body.list("ul.list_con_li > li.list-comic")) {
                val gid = node.href("a.image-link").filterGid()
                val title = node.attr("a.image-link", "title") ?: ""
                val titleJpn = ""
                val thumb = node.src("a.image-link > img") ?: ""
                val category = 0
                val posted = ""
                val uploader = node.text("p.auth") ?: ""
                val rating = 0.0f
                val rated = rating != 0.0f
                val info = MHComicInfo(gid, title, titleJpn, thumb, category, posted, uploader, rating, rated, MHApiSource.Manhuadui)
                result.add(info)
            }

            return MHMutiItemResult(result, pages, currentPage)
        }

        fun parserRecentComicList(html: String): MHMutiItemResult<MHComicInfo> {
            val body = MHNode(html)
            val currentPage = body.text("div.bottom_page > ul.pagination > li.active")?.toInt() ?: 1
            var pages = body.attr("div.bottom_page > ul.pagination > li.last > a", "data-page")?.toInt() ?: 0
            pages += 1

            val result = ArrayList<MHComicInfo>()
            for (node in body.list("li.list-comic")) {
                val gid = node.href("a.comic_img").filterGid()
                val title = node.attr("a.comic_img > img","alt") ?: ""
                val titleJpn = ""
                val thumb = node.src("a.comic_img > img") ?: ""
                val category = 0
                val posted = ""
                val uploader = ""
                val rating = 0.0f
                val rated = rating != 0.0f
                val info = MHComicInfo(gid, title, titleJpn, thumb, category, posted, uploader, rating, rated, MHApiSource.Manhuadui)
                result.add(info)
            }
            return MHMutiItemResult(result, pages, currentPage)
        }


        fun parseTop(html: String): MHMutiItemResult<MHComicInfo> {
            val body = MHNode(html)

            val datas = ArrayList<MHComicInfo>()
            for (node in body.list("div.con_li_content")) {
                val gid = node.href("a.dec_img")?.replace("$MANHUADUI_HOST/manhua/", "")?.replace("/", "") ?: "-1"
                if (gid == "-1") {
                    continue
                }

                val title = node.attr("a.dec_img", "title")
                val titleJpn = ""
                val thumb = node.src("a.dec_img > img.lazy")
                val posted = UNKNOWN_TIME
                val uploader = node.text("span.img_de > ul > li").split("：")[1]
                val rating = 0.0f
                val rated = rating == 0.0f
                val data = MHComicInfo(gid, title, titleJpn, thumb, 0, posted, uploader, rating, rated, MHApiSource.Manhuadui)
                datas.add(data)
            }
            return MHMutiItemResult(datas, 1, 1)
        }

        fun parserInfo(html: String): MHComicDetail {
            val body = MHNode(html)
            val gid = body.href("div.comic_deCon > a.beread_btn")?.replace(MANHUADUI_HOST, "")?.split("/")?.get(2) ?: ""
            val title = body.text("div.comic_deCon > h1")
            val titleJpn = body.text("div.comic_deCon > ul.comic_deCon_liO >  li:eq(6)")?.split("：")?.get(1) ?: ""
            val thumb = body.src("div.comic_i > div.comic_i_img  > img") ?: ""
            val category = 0
            val posted = body.text("span.zj_list_head_dat")?.split("：")?.get(1)?.replace("]", "")?.trim() ?: ""
            val uploader = body.text("div.comic_deCon > ul.comic_deCon_liO >  li:eq(1)")?.split("：")?.get(1) ?: ""
            // val rating = body.text("div.score > div#scoreRes > div.total > p.score-avg > em")?.toFloat() ?: 0.0f
            val rating = 0.0f
            val rated = false
            val into = body.text("div.comic_deCon > p.comic_deCon_d") ?: ""
            val chapterCount = 0
            val favoriteCount = 0
            val isFavorited = false
            val ratingCount = 0
            val updateTime = tranferTimeManhuadui(posted)

            val chapters = ArrayList<MHComicChapter>()
            for (c in body.list("ul#chapter-list-1 > li")) {
                val ctitle = c.attr("a", "title")
                val url = MHStringUtils.match("/\\d+.html", c.href("a"), 0).filterDigital()
                val chapter = MHComicChapter(ctitle, url, MHApiSource.Manhuadui)
                chapters.add(chapter)

            }

            val comments = ArrayList<MHComicComment>()
            return MHComicDetail(MHComicInfo(gid, title, titleJpn, thumb, category,
                    posted, uploader, rating, rated, MHApiSource.Manhuadui),
                    into, chapterCount, favoriteCount, isFavorited, ratingCount, chapters, comments, MHApiSource.Manhuadui, updateTime)
        }


        fun decode(data: String): String {
            val key = "123456781234567G".toByteArray(Charset.forName("UTF-8"))
            val ivs = "ABCDEF1G34123412".toByteArray(Charset.forName("UTF-8"))
            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            val secretKeySpec = SecretKeySpec(key, "AES")
            val paramSpec = IvParameterSpec(ivs)
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, paramSpec)
            return String(cipher.doFinal(Base64.decode(data, Base64.DEFAULT)))
        }


        fun parserData(html: String,chapter:String): MHComicData {
            val encryptedData = MHStringUtils.match("var chapterImages = \"(.*?)\"", html, 1)
            val dataStr  = decode(encryptedData)
            Logger.v(dataStr)
            val list = Gson().fromJson(dataStr, Array<String>::class.java)
            val data = ArrayList<String>()
            for(item in list) {
                if(item.startsWith("http")) {
                    data.add(item)
                } else {
                    data.add("https://mhcdn.manhuazj.com/images/comic/187/$chapter/$item")
                }
            }
            return MHComicData(data, MHApiSource.Manhuadui)
        }

        fun parserComment(html: String, currentPage: Int): MHMutiItemResult<MHComicComment> {
            val datas = ArrayList<MHComicComment>()
            return MHMutiItemResult(datas, 0, 0)
        }
    }
}
