package com.axlecho.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MHMutiItemResult<T : Parcelable>(
        val datas: ArrayList<T>,
        val pages: Int,
        val currentPage: Int
) : Parcelable

@Parcelize
data class MHComicInfo(val gid: String = "",
                       val title: String = "",
                       val titleJpn: String = "",
                       val thumb: String = "",
                       val category: Int = 0,
                       val posted: String = "",
                       val uploader: String = "",
                       val rating: Float = 0.0f,
                       var rated: Boolean = false,
                       val source: MHApiSource = MHApiSource.JS) : Parcelable

@Parcelize
data class MHComicDetail(val info: MHComicInfo,
                         val intro: String = "",
                         val chapterCount: Int = 0,
                         val favoriteCount: Int = 0,
                         val isFavorited: Boolean = false,
                         var ratingCount: Int = 0,
                         val chapters: ArrayList<MHComicChapter> = arrayListOf(),
                         val comments: ArrayList<MHComicComment> = arrayListOf(),
                         val source: MHApiSource = MHApiSource.JS,
                         val updateTime: Long = 0L) : Parcelable

@Parcelize
data class MHComicChapterGroup(
        val groupName: String,
        val chapters: ArrayList<MHComicChapter>
) : Parcelable

@Parcelize
data class MHComicComment(
        val id: String = "",
        val score: Int = 0,
        val time: String = "",
        val user: String = "",
        val comment: String = "",
        val source: MHApiSource = MHApiSource.JS) : Parcelable

@Parcelize
data class MHComicChapter(val title: String = "",
                          val url: String = "",
                          val source: MHApiSource = MHApiSource.JS) : Parcelable

@Parcelize
data class MHComicData(val data: ArrayList<String> = arrayListOf(),
                       val source: MHApiSource = MHApiSource.JS) : Parcelable

@Parcelize
enum class MHApiSource : Parcelable {
    Bangumi, Hanhan,Manhuagui,Kuku,Pica,Manhuadui,JS
}
