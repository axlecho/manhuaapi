package com.axlecho.api.kuku

import com.axlecho.api.*
import com.axlecho.api.untils.MHNode
import com.axlecho.api.untils.MHStringUtils
import com.axlecho.api.untils.transferTime
import com.orhanobut.logger.Logger

class KuKuParser {
    companion object {
        private val Tag: String = KuKuParser::javaClass.name

        fun String.filterDigital(): String {
            return this.replace("[^0-9]".toRegex(), "");
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

        fun parserGirdComicList(html: String): MHMutiItemResult<MHComicInfo> {
            // Logger.v(html)
            val result = ArrayList<MHComicInfo>()
            val body = MHNode(html)
            for (node in body.list("dl#comicmain > dd")) {
                val gid = node.href("a").filterDigital().toLong()
                val title = node.src("img").split("/").last().split(".").first()
                val titleJpn = MHConstant.UNKNOWN_TITLE
                val thumb = node.src("img")
                val category = -1
                val posted = MHConstant.UNKNOWN_TIME
                val uploader = MHConstant.UNKNOWN_MAN
                val rating = 0.0f
                val rated = false
                result.add(MHComicInfo(gid, title, titleJpn, thumb, category, posted, uploader, rating, rated, MHApiSource.Kuku))
            }
            val info = body.text("td[rowspan*=3] > div[style]")
            val pages = MHStringUtils.match("分 \\d+ 页",info,0).filterDigital().toInt()
            val currentPage = MHStringUtils.match("第 \\d+ 页",info,0).filterDigital().toInt() - 1
            return MHMutiItemResult(result, pages, currentPage)
        }

        fun parseTop(html: String): MHMutiItemResult<MHComicInfo> {
            Logger.v(html)


            val body = MHNode(html)
            val result = ArrayList<MHComicInfo>()
            for (node in body.list("table#comiclist > tbody > tr")) {
                Logger.v(node.get().html())
                val title = node.text("td[width*=80%] > a")
                val gid = node.href("td[width*=80%] > a").filterDigital().toLong()
                val titleJpn = MHConstant.UNKNOWN_TITLE
                val thumb = ""
                val category = -1
                val posted = MHConstant.UNKNOWN_TIME
                val uploader = MHConstant.UNKNOWN_MAN
                val rating = 0.0f
                val rated = false
                result.add(MHComicInfo(gid, title, titleJpn, thumb, category, posted, uploader, rating, rated, MHApiSource.Kuku))
            }
            return MHMutiItemResult(result, 1, 1)
        }

        fun parserInfo(html: String,gid:Long): MHComicDetail {
            Logger.v(html)
            val body = MHNode(html)

            val title = body.text("table[cellpadding=7] > tbody > tr:eq(0)").replace("漫画","")
            val titleJpn = MHConstant.UNKNOWN_TITLE
            val thumb = body.src("table[cellpadding=7] > tbody > tr:eq(1) > td > img")
            val category = -1
            Logger.v(body.text("table[cellpadding=7] > tbody > tr:eq(4)"))
            var posted = body.text("table[cellpadding=7] > tbody > tr:eq(4)")
                    .split("|")[2].split("：")[1].trim()
            var updateTime = transferTime(posted)
            var uploader =  body.text("table[cellpadding=7] > tbody > tr:eq(4)")
                    .split("|")[0].split("：")[1].trim()
//            if (posted != null) {
//                updateTime = transferTime(posted)
//                val args = posted!!.split("\\D".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
//                posted = MHStringUtils.format("%4d-%02d-%02d", Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]))
//            }
//            val uploader = body.textWithSubstring("#about_kit > ul > li:eq(1)", 3)

            val rating = 0.0f
            val ratingCount = 0
            val rated = ratingCount > 0
            val info = MHComicInfo(gid, title, titleJpn, thumb, category, posted, uploader, rating, rated, MHApiSource.Kuku)

            val intro = body.text("div#ComicInfo")
            val favoriteCount = 0
            val isFavorited = false
            val chapters = ArrayList<MHComicChapter>()
            val name = body.text("#about_kit > ul > li:eq(0) > h1")
            for (node in body.list("dl#comiclistn > dd")) {
                val chapterTitle = node.text("a")
                val path = MHConstant.HTTP_PROTOCOL_PREFIX + node.href("a")
                chapters.add(MHComicChapter(chapterTitle.trim { it <= ' ' }, path, MHApiSource.Kuku))
            }
            val chapterCount = chapters.size
            val comments = ArrayList<MHComicComment>()
            return MHComicDetail(info, intro, chapterCount, favoriteCount, isFavorited, ratingCount, chapters, comments, MHApiSource.Kuku, updateTime)
        }

        fun parserData(html: String,baseUrl:String): MHComicData {
            Logger.v(html)
            val body = MHNode(html)
            val base = baseUrl.replace("1.htm","")
            val pages = body.text("td[valign*=top]").split("|")[1].filterDigital().toInt()
            val list = ArrayList<String>()
            for (i in 1..pages) {
                list.add("$base$i.htm")
            }
            return MHComicData(list, MHApiSource.Kuku)
        }

        fun parserRaw(html: String): String {
            Logger.v(html)
            val body = MHNode(html)
            val script = body.list("td[valign*=top] > script").first().get().html()
            Logger.v(script)

            val url = MHStringUtils.match("<IMG SRC='(.*)'></a>",script,1)

            return url.replace(Regex("\".*\""),"http://n9.1whour.com/")
        }
    }
}
