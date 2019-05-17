package com.axlecho.api.hanhan

import com.axlecho.api.*
import com.axlecho.api.untils.MHNode
import com.axlecho.api.untils.MHStringUtils
import com.axlecho.api.untils.transferTime
import com.orhanobut.logger.Logger

class HHParser {
    companion object {
        private val Tag: String = HHParser::javaClass.name

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

        fun parserGirdComicList(html: String): MHMutiItemResult<MHComicInfo> {
            // Logger.v(html)
            val result = ArrayList<MHComicInfo>()
            val body = MHNode(html)
            for (node in body.list("#list > div.cComicList > li > a")) {
                val gid = node.hrefWithSubString(7, -6).toLong()
                val title = node.text()
                val titleJpn = MHConstant.UNKNOWN_TITLE
                val thumb = node.src("img")
                val category = -1
                val posted = MHConstant.UNKNOWN_TIME
                val uploader = MHConstant.UNKNOWN_MAN
                val rating = 0.0f
                val rated = false
                result.add(MHComicInfo(gid, title, titleJpn, thumb, category, posted, uploader, rating, rated, MHApiSource.Hanhan))
            }
            return MHMutiItemResult(result, 1, 1)
        }

        fun parseTop(html: String): MHMutiItemResult<MHComicInfo> {
            Logger.v(html)
            val body = MHNode(html)
            val result = ArrayList<MHComicInfo>()
            for (node in body.list("#list > div.cTopComicList > div.cComicItem")) {
                Logger.v(node.get().html())
                val gid = node.hrefWithSubString("div.cListSlt > a", 7, -6).toLong()
                val title = node.text("span.cComicTitle")
                val titleJpn = MHConstant.UNKNOWN_TITLE
                val thumb = node.src("img")
                val category = -1
                val posted = MHConstant.UNKNOWN_TIME
                val uploader = MHConstant.UNKNOWN_MAN
                val rating = 0.0f
                val rated = false
                result.add(MHComicInfo(gid, title, titleJpn, thumb, category, posted, uploader, rating, rated, MHApiSource.Hanhan))
            }
            return MHMutiItemResult(result, 1, 1)
        }

        fun parserInfo(html: String): MHComicDetail {
            Logger.v(html)
            val body = MHNode(html)

            val gid = body.attr("input#hdComicID", "value").toLong()
            val title = body.text("#about_kit > ul > li:eq(0) > h1")
            val titleJpn = MHConstant.UNKNOWN_TITLE
            val thumb = body.src("#about_style > img")
            val category = -1
            var posted = body.textWithSubstring("#about_kit > ul > li:eq(4)", 3)
            var updateTime = 0L
            if (posted != null) {
                updateTime = transferTime(posted)
                val args = posted!!.split("\\D".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                posted = MHStringUtils.format("%4d-%02d-%02d", Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]))
            }
            val uploader = body.textWithSubstring("#about_kit > ul > li:eq(1)", 3)
            val ratingString = body.text("#about_kit > ul > li:eq(6)")
            val rating = ratingString.substring(3).split("分(")[0].toFloat()
            val ratingCount = ratingString.split("分(")[1].replace("人评)", "").toInt()
            val rated = ratingCount > 0
            val info = MHComicInfo(gid, title, titleJpn, thumb, category, posted, uploader, rating, rated, MHApiSource.Hanhan)

            val intro = body.textWithSubstring("#about_kit > ul > li:eq(7)", 3)
            val chapterCount = body.textWithSubstring("#about_kit > ul > li:eq(3)", 3).split("集(卷)")[0].toInt()
            val favoriteCount = body.textWithSubstring("#about_kit > ul > li:eq(5)", 3).replace("人收藏本漫画", "").toInt()
            val isFavorited = false
            val status = if (isFinish(body.text("#about_kit > ul > li:eq(2)"))) "完结" else "连载中"
            val chapters = ArrayList<MHComicChapter>()
            val name = body.text("#about_kit > ul > li:eq(0) > h1")
            for (node in body.list("#permalink > div.cVolList > ul.cVolUl > li > a")) {
                var chapterTitle = node.text()
                chapterTitle = chapterTitle.replaceFirst(name.toRegex(), "").trim { it <= ' ' }
                val array = MHStringUtils.match("/page(\\d+).*s=(\\d+)", node.attr("href"), 1, 2)
                //String path = array != null ? array[0].concat(" ").concat(array[1]) : "";
                val path = if (array != null) array[0] + "-" + array[1] else ""
                chapters.add(MHComicChapter(chapterTitle.trim { it <= ' ' }, path, MHApiSource.Hanhan))
            }
            val comments = ArrayList<MHComicComment>()
            return MHComicDetail(info, intro, chapterCount, favoriteCount, isFavorited, ratingCount, chapters, comments, MHApiSource.Hanhan, updateTime)
        }

        fun parserData(html: String): MHComicData {
            Logger.v(html)
            val list = ArrayList<String>()
            val body = MHNode(html)
            val page = Integer.parseInt(body.attr("#hdPageCount", "value"))
            val path = body.attr("#hdVolID", "value")
            val server = body.attr("#hdS", "value")
            for (i in 1..page) {
                list.add("http://www.hhmmoo.com/page$path/$i.html?s=$server")
            }
            return MHComicData(list, MHApiSource.Hanhan)
        }

        fun parserRaw(html: String): String {
            Logger.v(html)
            val body = MHNode(html)
            var server = body.attr("#hdDomain", "value")
            if (server != null) {
                server = server.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                val name = body.attr("#iBodyQ > img", "name")
                val result = unsuan(name).substring(1)
                return server + result
            }
            return ""
        }
    }
}
