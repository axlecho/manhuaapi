package com.axlecho.api.manhuagui

import com.axlecho.api.Api
import com.axlecho.api.MHApi
import com.axlecho.api.MHApiSource
import com.axlecho.api.MHCategory

class ManhuaguiCategory(_api: Api) : MHCategory(_api) {
    override fun loadCategory(): String {
        return MHApi.context.loadTopCategory(MHApiSource.Manhuagui) ?: "总"
    }

    override fun loadTime(): String {
        return MHApi.context.loadTopCategory(MHApiSource.Manhuagui) ?: "日排行"
    }

    override fun saveTime(time: String) {
        MHApi.context.saveTopTime(time, MHApiSource.Manhuagui)
    }

    override fun saveCategory(category: String) {
        MHApi.context.saveTopCategory(category, MHApiSource.Manhuagui)
    }

    init {
        buildTimeMap()
        buildCategoryMap()
    }

    override fun build(): String {
        val base = "${categoryMap[getCurrentCategory()]}_${timeMap[getCurrentTime()]}".trim('_')
        return if (base.isEmpty()) base else "$base.html"
    }

    private fun buildTimeMap() {
        timeMap["日排行"] = ""
        timeMap["周排行"] = "week"
        timeMap["月排行"] = "month"
        timeMap["总排行"] = "total"
    }

    private fun buildCategoryMap() {
        categoryMap["总"] = ""
        categoryMap["日本"] = "japan"
        categoryMap["港台"] = "hongkong"
        categoryMap["其它"] = "other"
        categoryMap["欧美"] = "europe"
        categoryMap["内地"] = "china"
        categoryMap["韩国"] = "korea"

        categoryMap["少女"] = "shaonv"
        categoryMap["少年"] = "shaonian"
        categoryMap["青年"] = "qingnian"
        categoryMap["儿童"] = "ertong"
        categoryMap["通用"] = "tongyong"

        categoryMap["热血"] = "rexue"
        categoryMap["冒险"] = "maoxian"
        categoryMap["魔幻"] = "mohuan"
        categoryMap["神鬼"] = "shengui"
        categoryMap["搞笑"] = "gaoxiao"
        categoryMap["萌系"] = "mengxi"
        categoryMap["爱情"] = "aiqing"
        categoryMap["科幻"] = "kehuan"
        categoryMap["魔法"] = "mofa"
        categoryMap["格斗"] = "gedou"
        categoryMap["武侠"] = "wuxia"
        categoryMap["机战"] = "jizhan"
        categoryMap["战争"] = "zhanzheng"
        categoryMap["竞技"] = "jingji"
        categoryMap["体育"] = "tiyu"
        categoryMap["校园"] = "xiaoyuan"
        categoryMap["生活"] = "shenghuo"
        categoryMap["励志"] = "lizhi"
        categoryMap["历史"] = "lishi"
        categoryMap["伪娘"] = "weiniang"
        categoryMap["宅男"] = "zhainan"
        categoryMap["腐女"] = "funv"
        categoryMap["耽美"] = "danmei"
        categoryMap["百合"] = "baihe"
        categoryMap["后宫"] = "hougong"
        categoryMap["治愈"] = "zhiyu"
        categoryMap["美食"] = "meishi"
        categoryMap["推理"] = "tuili"
        categoryMap["悬疑"] = "xuanyi"
        categoryMap["恐怖"] = "kongbu"
        categoryMap["四格"] = "sige"
        categoryMap["职场"] = "zhichang"
        categoryMap["侦探"] = "zhentan"
        categoryMap["社会"] = "shehui"
        categoryMap["音乐"] = "yinyue"
        categoryMap["舞蹈"] = "wudao"
        categoryMap["杂志"] = "zazhi"
        categoryMap["黑道"] = "heidao"
    }
}