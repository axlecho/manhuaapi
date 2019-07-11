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
data class MHComicInfo(val gid: String,
                       val title: String,
                       val titleJpn: String,
                       val thumb: String,
                       val category: Int,
                       val posted: String,
                       val uploader: String,
                       val rating: Float,
                       var rated: Boolean,
                       val source: MHApiSource) : Parcelable

@Parcelize
data class MHComicDetail(val info: MHComicInfo,
                         val intro: String,
                         val chapterCount: Int,
                         val favoriteCount: Int,
                         val isFavorited: Boolean,
                         var ratingCount: Int,
                         val chapters: ArrayList<MHComicChapter>,
                         val comments: ArrayList<MHComicComment>,
                         val source: MHApiSource,
                         val updateTime: Long) : Parcelable

@Parcelize
data class MHComicChapterGroup(
        val groupName: String,
        val chapters: ArrayList<MHComicChapter>
) : Parcelable

@Parcelize
data class MHComicComment(
        val id: String,
        val score: Int,
        val time: String,
        val user: String,
        val comment: String,
        val source: MHApiSource) : Parcelable

@Parcelize
data class MHComicChapter(val title: String, val url: String, val source: MHApiSource) : Parcelable

@Parcelize
data class MHComicData(val data: ArrayList<String>, val source: MHApiSource) : Parcelable

@Parcelize
enum class MHApiSource : Parcelable {
    Bangumi, Hanhan,Manhuagui,Kuku,Pica
}
