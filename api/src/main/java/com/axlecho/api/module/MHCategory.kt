package com.axlecho.api.module

open class CategoryBase(url: String, text: String) {
    open fun getCateGoryFromUrl(): String {
        return ""
    }
}

class MHCategoryAge(val url: String, val text: String) : CategoryBase(url, text) {
    val category = getCateGoryFromUrl()

    override fun getCateGoryFromUrl(): String {
        return ""
    }
}

class MHCategoryArea(val url: String, val text: String) : CategoryBase(url, text) {
    val category = getCateGoryFromUrl()

    override fun getCateGoryFromUrl(): String {
        return ""
    }
}

class MHCategoryGenre(val url: String, val text: String) : CategoryBase(url, text) {
    val category = getCateGoryFromUrl()

    override fun getCateGoryFromUrl(): String {
        return ""
    }
}

class MHCategoryLetter(val url: String, val text: String) : CategoryBase(url, text) {
    val category = getCateGoryFromUrl()

    override fun getCateGoryFromUrl(): String {
        return ""
    }
}

class MHCategoryStatus(val url: String, val text: String) : CategoryBase(url, text) {
    val category = getCateGoryFromUrl()

    override fun getCateGoryFromUrl(): String {
        return ""
    }
}

class MHModuleCategoryTime(val url: String, val text: String) : CategoryBase(url, text) {
    val category = getCateGoryFromUrl()

    override fun getCateGoryFromUrl(): String {
        return ""
    }
}

class MHCategoryYear(val url: String, val text: String) : CategoryBase(url, text) {
    val category = getCateGoryFromUrl()

    override fun getCateGoryFromUrl(): String {
        return ""
    }
}