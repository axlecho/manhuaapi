package com.axlecho.api.module.comic

data class MHComic(val cid: String, val title: String, val cover: String)

data class MHComicInfo(val title: String, val cover: String, val update: String, val intro: String, val author: String, val status: Boolean, val chapters: ArrayList<MHComicChapter>)

data class MHComicChapter(val title: String, val source: String)

data class MHComicData(val data: ArrayList<String>)
