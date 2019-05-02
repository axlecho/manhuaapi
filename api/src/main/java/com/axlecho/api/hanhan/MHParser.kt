package com.axlecho.api.hanhan

import com.axlecho.api.module.comic.MHComic
import com.axlecho.api.module.comic.MHComicChapter
import com.axlecho.api.module.comic.MHComicInfo
import com.axlecho.api.untils.MHNode
import com.axlecho.api.untils.MHStringUtils
import com.orhanobut.logger.Logger

class MHParser {
    companion object {
        private val Tag: String = MHParser::javaClass.name

        private fun isFinish(text: String?): Boolean {
            return text != null && (text.contains("完结") || text.contains("Completed"))
        }

        fun parserGirdComicList(html: String): ArrayList<MHComic> {
            Logger.v(html)
            val result = ArrayList<MHComic>()
            val body = MHNode(html)
            for (node in body.list("#list > div.cComicList > li > a")) {
                val cid = node.hrefWithSubString(7, -6)
                val title = node.text()
                val cover = node.src("img")
                result.add(MHComic(cid, title, cover))
//            val cover = node.src("a.pic > img")
//            val update = node.textWithSubstring("a.pic > div > p:eq(4) > span", 0, 10)
//            val author = node.text("a.pic > div > p:eq(1)")
            }
            return result
        }

        fun parserInfo(html: String): MHComicInfo {
            Logger.v(html)
            val body = MHNode(html)

            val title = body.text("#about_kit > ul > li:eq(0) > h1")
            val cover = body.src("#about_style > img")
            var update = body.textWithSubstring("#about_kit > ul > li:eq(4)", 3)
            if (update != null) {
                val args = update!!.split("\\D".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                update = MHStringUtils.format("%4d-%02d-%02d", Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]))
            }
            val author = body.textWithSubstring("#about_kit > ul > li:eq(1)", 3)
            val intro = body.textWithSubstring("#about_kit > ul > li:eq(7)", 3)
            val status = if (isFinish(body.text("#about_kit > ul > li:eq(2)"))) "完结" else "连载中"

            val ratingString = body.text("#about_kit > ul > li:eq(6)")
            val rating = ratingString.substring(3).split("分(")[0].toFloat()
            val ratingCount = ratingString.split("分(")[1].replace("人评)", "").toInt()

            val favorites = body.textWithSubstring("#about_kit > ul > li:eq(5)", 3).replace("人收藏本漫画", "").toInt()
            val chapterCount = body.textWithSubstring("#about_kit > ul > li:eq(3)", 3).split("集(卷)")[0].toInt()

            val list = ArrayList<MHComicChapter>()
            val name = body.text("#about_kit > ul > li:eq(0) > h1")
            for (node in body.list("#permalink > div.cVolList > ul.cVolUl > li > a")) {
                var chapterTitle = node.text()
                chapterTitle = chapterTitle.replaceFirst(name.toRegex(), "").trim { it <= ' ' }
                val array = MHStringUtils.match("/page(\\d+).*s=(\\d+)", node.attr("href"), 1, 2)
                //String path = array != null ? array[0].concat(" ").concat(array[1]) : "";
                val path = if (array != null) array[0] + "-" + array[1] else ""
                list.add(MHComicChapter(chapterTitle.trim { it <= ' ' }, path))
            }
            return MHComicInfo(title, cover, update, intro, author, status, rating, ratingCount, favorites, chapterCount, list)
        }

    }
}
