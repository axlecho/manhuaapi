package com.axlecho.api.manhuagui

import com.axlecho.api.*
import com.axlecho.api.untils.MHNode
import com.axlecho.api.untils.MHStringUtils
import com.axlecho.api.untils.tranferTimeManhuagui
import com.google.gson.Gson


class ManhuaguiParser {
    companion object {
        private val Tag: String = ManhuaguiParser::javaClass.name
        fun String.filterDigital(): String {
            return this.replace("[^0-9]".toRegex(), "");
        }

        fun parserSearchComicList(html: String): MHMutiItemResult<MHComicInfo> {

            val body = MHNode(html)
            val currentPage = body.text("div.pager-cont > div.pager > span.current")?.toInt() ?: 1
            var pages = currentPage

            for (node in body.list("div.pager-cont > div.pager > a.prev")) {
                if (node.text() == "尾頁" || node.text() == "尾页") {
                    pages = MHStringUtils.match("_p\\d+", node.href(), 0).filterDigital().toInt()
                }
            }
            val result = ArrayList<MHComicInfo>()
            for (node in body.list("div.book-result > ul > li.cf")) {
                val gid = node.href("div.book-detail > dl > dt > a").filterDigital().toLong()
                val title = node.text("div.book-detail > dl > dt > a") ?: ""
                val titleJpn = node.text("div.book-detail > dl > dt > small > a") ?: ""
                val thumb = node.src("div.book-cover > a.bcover > img") ?: ""
                val category = 0
                val posted = node.text("div.book-detail > dl > dd.tags:eq(1) > span > span:eq(2)")
                        ?: ""
                val uploader = node.text("div.book-detail > dl > dd.tags:eq(3) > span > a") ?: ""
                val rating = node.text("div.book-score > p.score-avg > strong")?.toFloat() ?: 0.0f
                val rated = rating != 0.0f
                val info = MHComicInfo(gid, title, titleJpn, thumb, category, posted, uploader, rating, rated, MHApiSource.Manhuagui)
                result.add(info)
            }

            return MHMutiItemResult(result, pages, currentPage)
        }

        fun parserRecentComicList(html: String): MHMutiItemResult<MHComicInfo> {
            val body = MHNode(html)
            val currentPage = 1
            var pages = currentPage

            val result = ArrayList<MHComicInfo>()
            for (list in body.list("div.latest-list")) {
                for(node in list.list("ul > li")) {
                    val gid = node.href("a.cover").filterDigital().toLong()
                    val title = node.text("p.ell") ?: ""
                    val titleJpn = ""
                    val thumb = node.src("a.cover > img") ?: ""
                    val category = 0
                    val posted = node.text("sapn.dt") ?: ""
                    val uploader = ""
                    val rating = 0.0f
                    val rated = rating != 0.0f
                    val info = MHComicInfo(gid, title, titleJpn, thumb, category, posted, uploader, rating, rated, MHApiSource.Manhuagui)
                    result.add(info)
                }
            }

            return MHMutiItemResult(result, pages, currentPage)
        }


        fun parseTop(html: String): MHMutiItemResult<MHComicInfo> {
            val body = MHNode(html)

            val datas = ArrayList<MHComicInfo>()
            for (node in body.list("tbody > tr")) {
                val gid = node.href("td.rank-title > h5 > a")?.filterDigital()?.toLong() ?: -1
                if (gid == -1L) {
                    continue
                }

                val title = node.text("td.rank-title > h5 > a")
                val titleJpn = ""
                val thumb = ""
                val posted = node.text("td.rank-time")
                val uploader = node.text("td > div.rank-author")
                val rating = node.text("td.rank-score")?.toFloat() ?: 0.0f
                val rated = rating == 0.0f
                val data = MHComicInfo(gid, title, titleJpn, thumb, 0, posted, uploader, rating, rated, MHApiSource.Manhuagui)
                datas.add(data)
            }
            return MHMutiItemResult(datas, 1, 1)
        }

        fun parserInfo(html: String, rankingInfo: ManhuaguiRankingInfo?,commentInfo: MHMutiItemResult<MHComicComment>): MHComicDetail {
            val body = MHNode(html)
            val gid = body.href("div.crumb > a[href^=/comic/]").filterDigital().toLong()
            val title = body.text("div.book-cont > div.book-detail > div.book-title > h1")
            val titleJpn = body.text("div.book-cont > div.book-detail >  div.book-title > h2") ?: ""
            val thumb = body.src("div.book-cont > div.book-cover > p.hcover > img") ?: ""
            val category = 0
            val posted = body.text("div.book-cont > div.book-detail > ul.detail-list > li.status > span > span:eq(2)")
                    ?: ""
            val uploader = body.text("div.book-cont > div.book-detail > ul.detail-list > li:eq(1) > span:eq(1) > a")
                    ?: ""
            // val rating = body.text("div.score > div#scoreRes > div.total > p.score-avg > em")?.toFloat() ?: 0.0f
            var rating = 0.0f
            var rated = false
            val into = body.text("div.book-cont > div.book-detail > div.book-intro > div#intro-all")
                    ?: ""
            val chapterCount = body.text("div.book-cont > div.book-detail > ul.detail-list > li.status > span > a")?.filterDigital()?.toInt()
                    ?: 0
            val favoriteCount = 0
            val isFavorited = false
            var ratingCount = 0
            val updateTime = tranferTimeManhuagui(posted)

            val chapters = ArrayList<MHComicChapter>()
            for (typeNode in body.list("div.chapter > div.chapter-list")) {
                for (list in typeNode.list("ul").reversed()) {
                    for (c in list.list("li")) {
                        val ctitle = c.attr("a", "title")
                        val url = MHStringUtils.match("/\\d+.html", c.href("a"), 0).filterDigital()
                        val chapter = MHComicChapter(ctitle, url, MHApiSource.Manhuagui)
                        chapters.add(chapter)
                    }
                }
            }

            val comments = ArrayList<MHComicComment>()
            if(commentInfo.pages >= 0) {
                comments.addAll(commentInfo.datas)
            }

            if (rankingInfo != null && rankingInfo.success) {
                rated = true
                ratingCount = rankingInfo.data.s1 + rankingInfo.data.s2 + rankingInfo.data.s3 + rankingInfo.data.s4 + rankingInfo.data.s5
                rating = (rankingInfo.data.s1 * 2 + rankingInfo.data.s2 * 4 + rankingInfo.data.s3 * 6
                        + rankingInfo.data.s4 * 8 + rankingInfo.data.s5 * 10) / ratingCount.toFloat()
            }
            return MHComicDetail(MHComicInfo(gid, title, titleJpn, thumb, category,
                    posted, uploader, rating, rated, MHApiSource.Manhuagui),
                    into, chapterCount, favoriteCount, isFavorited, ratingCount, chapters, comments, MHApiSource.Manhuagui, updateTime)
        }


        fun parserRaw(url: String): String {
            return url
        }

        fun parserData(html: String): MHComicData {
            // Logger.v(html)
            val code = MHStringUtils.match("window\\[\"\\\\x65\\\\x76\\\\x61\\\\x6c\"\\](.*?)</script>", html, 1)
                    ?: return MHComicData(arrayListOf(), MHApiSource.Manhuagui)
            // Logger.v(code)

            var result = ""
            // do the magic
            val cx = org.mozilla.javascript.Context.enter()
            cx.optimizationLevel = -1
            try {
                val scope = cx.initStandardObjects()
                val lib = "var LZString=(function(){var f=String.fromCharCode;var keyStrBase64=\"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=\";var baseReverseDic={};function getBaseValue(alphabet,character){if(!baseReverseDic[alphabet]){baseReverseDic[alphabet]={};for(var i=0;i<alphabet.length;i++){baseReverseDic[alphabet][alphabet.charAt(i)]=i}}return baseReverseDic[alphabet][character]}var LZString={decompressFromBase64:function(input){if(input==null){return\"\"}if(input==\"\"){return null}return LZString._0(input.length,32,function(index){return getBaseValue(keyStrBase64,input.charAt(index))})},_0:function(length,resetValue,getNextValue){var dictionary=[],next,enlargeIn=4,dictSize=4,numBits=3,entry=\"\",result=[],i,w,bits,resb,maxpower,power,c,data={val:getNextValue(0),position:resetValue,index:1};for(i=0;i<3;i+=1){dictionary[i]=i}bits=0;maxpower=Math.pow(2,2);power=1;while(power!=maxpower){resb=data.val&data.position;data.position>>=1;if(data.position==0){data.position=resetValue;data.val=getNextValue(data.index++)}bits|=(resb>0?1:0)*power;power<<=1}switch(next=bits){case 0:bits=0;maxpower=Math.pow(2,8);power=1;while(power!=maxpower){resb=data.val&data.position;data.position>>=1;if(data.position==0){data.position=resetValue;data.val=getNextValue(data.index++)}bits|=(resb>0?1:0)*power;power<<=1}c=f(bits);break;case 1:bits=0;maxpower=Math.pow(2,16);power=1;while(power!=maxpower){resb=data.val&data.position;data.position>>=1;if(data.position==0){data.position=resetValue;data.val=getNextValue(data.index++)}bits|=(resb>0?1:0)*power;power<<=1}c=f(bits);break;case 2:return\"\"}dictionary[3]=c;w=c;result.push(c);while(true){if(data.index>length){return\"\"}bits=0;maxpower=Math.pow(2,numBits);power=1;while(power!=maxpower){resb=data.val&data.position;data.position>>=1;if(data.position==0){data.position=resetValue;data.val=getNextValue(data.index++)}bits|=(resb>0?1:0)*power;power<<=1}switch(c=bits){case 0:bits=0;maxpower=Math.pow(2,8);power=1;while(power!=maxpower){resb=data.val&data.position;data.position>>=1;if(data.position==0){data.position=resetValue;data.val=getNextValue(data.index++)}bits|=(resb>0?1:0)*power;power<<=1}dictionary[dictSize++]=f(bits);c=dictSize-1;enlargeIn--;break;case 1:bits=0;maxpower=Math.pow(2,16);power=1;while(power!=maxpower){resb=data.val&data.position;data.position>>=1;if(data.position==0){data.position=resetValue;data.val=getNextValue(data.index++)}bits|=(resb>0?1:0)*power;power<<=1}dictionary[dictSize++]=f(bits);c=dictSize-1;enlargeIn--;break;case 2:return result.join(\"\")}if(enlargeIn==0){enlargeIn=Math.pow(2,numBits);numBits++}if(dictionary[c]){entry=dictionary[c]}else{if(c===dictSize){entry=w+w.charAt(0)}else{return null}}result.push(entry);dictionary[dictSize++]=w+entry.charAt(0);enlargeIn--;w=entry;if(enlargeIn==0){enlargeIn=Math.pow(2,numBits);numBits++}}}};return LZString})();String.prototype.splic=function(f){return LZString.decompressFromBase64(this).split(f)};"
                val ret = cx.evaluateString(scope, lib + "\n" + code, "<cmd>", 1, null)
                if (ret is String) {
                    result = ret
                }
            } finally {
                // Exit from the context.
                org.mozilla.javascript.Context.exit()
            }
            result = result.replace("SMH.imgData(", "").replace(").preInit();", "")
            val info = Gson().fromJson(result, ManhuaguiImageInfo::class.java)
            val token = "?cid=${info.cid}&md5=${info.sl.md5}"
            val data = ArrayList<String>()
            for (file in info.files) {
                var url = "https://i.hamreus.com" + info.path + file + token
                data.add(url)
            }
            return MHComicData(data, MHApiSource.Manhuagui)
        }

        fun parserComment(html: String, currentPage: Int): MHMutiItemResult<MHComicComment> {
            val comments = Gson().fromJson<ManhuaguiCommentInfo>(html, ManhuaguiCommentInfo::class.java)
            // Logger.json(Gson().toJson(comments))

            val datas = ArrayList<MHComicComment>()
            for (comment in comments.commentIds) {

                val subComments = comment.split(",")
                if (subComments[0].isEmpty()) {
                    continue
                }

                val main = Gson().fromJson<CommentsData>(comments.comments.getAsJsonObject(subComments[0]), CommentsData::class.java)
                val id = main.user_id.toString()
                val score = 0
                val time = main.add_time
                val user = main.user_name
                var commentContent = ""
                for (i in 0 until subComments.size) {
                    val item = Gson().fromJson<CommentsData>(comments.comments.getAsJsonObject(subComments[i]), CommentsData::class.java)
                    commentContent = if (i == 0) item.content
                    else "$commentContent\n\t> ${item.content} @ ${item.user_name}"
                }

                datas.add(MHComicComment(id, score, time, user, commentContent, MHApiSource.Manhuagui))
            }
            val pages = comments.commentIds.size / 30

            return MHMutiItemResult(datas, pages, currentPage)
        }
    }
}
