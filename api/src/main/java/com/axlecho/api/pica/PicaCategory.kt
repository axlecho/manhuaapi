package com.axlecho.api.pica

import com.axlecho.api.Api
import com.axlecho.api.MHApi
import com.axlecho.api.MHApiSource
import com.axlecho.api.MHCategory

class PicaCategory(_api: Api) : MHCategory(_api) {
    override fun loadCategory(): String {
        val category = MHApi.context.loadTopCategory(MHApiSource.Pica)
        return if (category.isEmpty()) "过去24小时" else category
    }

    override fun loadTime(): String = ""

    override fun saveTime(time: String) {}

    override fun saveCategory(category: String) {
        MHApi.context.saveTopCategory(category, MHApiSource.Pica)
    }


    override fun build(): String {
        return categoryMap[getCurrentCategory()] ?: ""

    }

    init {
        buildCategoryMap()
    }

    private fun buildCategoryMap() {
        categoryMap["过去24小时"] = "_H24"
        categoryMap["过去7天"] = "_D7"
        categoryMap["过去30天"] = "_D30"

        // categoryMap["随机本子"] = "*"
        categoryMap["大家都在看"] = "大家都在看"
        categoryMap["來打針囉❤"] = "來打針囉❤"
        categoryMap["嗶咔AI推薦"] = "嗶咔AI推薦"
        categoryMap["那年今天"] = "那年今天"
        categoryMap["官方都在看"] = "官方都在看"
        categoryMap["嗶咔漢化"] = "嗶咔漢化"
        categoryMap["全彩"] = "全彩"
        categoryMap["長篇"] = "長篇"
        categoryMap["同人"] = "同人"
        categoryMap["短篇"] = "短篇"
        categoryMap["圓神領域"] = "圓神領域"
        categoryMap["碧藍幻想"] = "碧藍幻想"
        categoryMap["CG雜圖"] = "CG雜圖"
        categoryMap["英語 ENG"] = "英語 ENG"
        categoryMap["生肉"] = "生肉"
        categoryMap["純愛"] = "純愛"
        categoryMap["百合花園"] = "百合花園"
        categoryMap["耽美花園"] = "耽美花園"
        categoryMap["偽娘哲學"] = "偽娘哲學"
        categoryMap["後宮閃光"] = "後宮閃光"
        categoryMap["扶他樂園"] = "扶他樂園"
        categoryMap["單行本"] = "單行本"
        categoryMap["姐姐系"] = "姐姐系"
        categoryMap["妹妹系"] = "妹妹系"
        categoryMap["SM"] = "SM"
        categoryMap["性轉換"] = "性轉換"
        categoryMap["足の恋"] = "足の恋"
        categoryMap["重口地帶"] = "重口地帶"
        categoryMap["人妻"] = "人妻"
        categoryMap["NTR"] = "NTR"
        categoryMap["強暴"] = "強暴"
        categoryMap["非人類"] = "非人類"
        categoryMap["艦隊收藏"] = "艦隊收藏"
        categoryMap["Love Live"] = "Love Live"
        categoryMap["SAO 刀劍神域"] = "SAO 刀劍神域"
        categoryMap["Fate"] = "Fate"
        categoryMap["東方"] = "東方"
        categoryMap["WEBTOON"] = "WEBTOON"
        categoryMap["禁書目錄"] = "禁書目錄"
        categoryMap["歐美"] = "歐美"
    }
}