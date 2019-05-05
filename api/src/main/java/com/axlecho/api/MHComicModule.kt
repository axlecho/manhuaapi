package com.axlecho.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MHComicInfo(val gid: Long,
                       val title: String,
                       val titleJpn: String,
                       val thumb: String,
                       val category: Int,
                       val posted: String,
                       val uploader: String,
                       val rating: Float,
                       val rated: Boolean) : Parcelable

@Parcelize
data class MHComicDetail(val info: MHComicInfo,
                         val intro: String,
                         val chapterCount: Int,
                         val favoriteCount: Int,
                         val isFavorited: Boolean,
                         val ratingCount: Int,
                         val chapters: ArrayList<MHComicChapter>,
                         val comments: ArrayList<MHComicComment>) : Parcelable

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
        val comment: String) : Parcelable

@Parcelize
data class MHComicChapter(val title: String, val source: String) : Parcelable

@Parcelize
data class MHComicData(val data: ArrayList<String>) : Parcelable
