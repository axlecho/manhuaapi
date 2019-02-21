package com.axlecho.api.parser

import com.axlecho.api.module.MHModuleCategoryTime
import com.axlecho.api.module.rank.MHModuleRankPage
import com.orhanobut.logger.Logger
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class MHParserRank {
    companion object {
        var page = MHModuleRankPage()
        fun parse(html: String): MHModuleRankPage {
            val doc = Jsoup.parse(html)
            parseCategoryTime(doc)
            return page
        }

        private fun parseCategorys(doc:Document) {
            val categoryList  = doc.select("category-list").first()
            categoryList ?: return
            Logger.v(categoryList.html())

            val h_3 = categoryList.select("h3")
            h_3 ?:return


            val it = h_3.iterator()
            while (it.hasNext()) {
                val element = it.next() as Element
                when(element.text()){
                    "按周期" -> parseCategoryTime(element)
                }
            }
        }

        private fun parseCategoryTime(e:Element) {
            val list = e.nextElementSibling()
            list ?: return
            Logger.v(list.html())

            val categoryObjects = list.select("a[href]")
            val it = categoryObjects.iterator()
            while(it.hasNext()) {
                val categoryObject = it.next() as Element
                val url = categoryObject.attr("href")
                var text = categoryObject.text()
                if(url != null && text != null) {
                    val category = MHModuleCategoryTime(url,text)
                    page.frame.timeCategorys.add(category)
                }
            }
        }
    }
}