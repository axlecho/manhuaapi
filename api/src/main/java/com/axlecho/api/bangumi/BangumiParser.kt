package com.axlecho.api.bangumi

import android.text.TextUtils
import com.axlecho.api.MHComicInfo
import com.axlecho.api.MHConstant
import com.axlecho.api.untils.MHNode
import com.orhanobut.logger.Logger

class BangumiParser {
    companion object {
        private val Tag: String = BangumiParser::javaClass.name
        fun parserComicList(html: String): ArrayList<MHComicInfo> {
            // Logger.v(html)
            val result = ArrayList<MHComicInfo>()

            val body = MHNode(html)
            for (node in body.list("ul#browserItemList > li")) {
                // Logger.v(node.get().html())
                val gid = node.attr("id").replace("[^0-9]".toRegex(),"")
                val title = node.text("div.inner > h3 > a.l")
                val titleJpn = node.text("div.inner > h3 > small.grey")
                val thumb = node.src("a.subjectCover > span.image > img.cover" )
                val category = -1
                val posted = node.text("div.inner > p.collectInfo > span.tip_j")
                val uploader = node.text("div.inner > p.info")
                val rating = 0.0f
                val rated = false
                result.add(MHComicInfo(gid, title, titleJpn, thumb, category, posted, uploader, rating, rated))
            }
            return result
        }

        fun parserCollectionCount(html:String):Int {
            val body = MHNode(html)
            val countString = body.text("div#headerProfile > div.subjectNav > div.navSubTabsWrapper > ul.navSubTabs > li:eq(2)")
                    .replace("[^0-9]".toRegex(),"")

            // Logger.v(countString)

            if(TextUtils.isDigitsOnly(countString)) {
                return countString.toInt()
            }
            return -1
        }
    }
}