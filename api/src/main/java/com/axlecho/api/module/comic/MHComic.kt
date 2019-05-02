package com.axlecho.api.module.comic

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MHComic(val cid: String, val title: String, val cover: String) : Parcelable

@Parcelize
data class MHComicInfo(val title: String, val cover: String, val update: String, val intro: String,
                       val author: String, val status: String, val rating: Float, val ratingCount: Int,
                       val chapters: ArrayList<MHComicChapter>) : Parcelable

@Parcelize
data class MHComicChapter(val title: String, val source: String) : Parcelable

@Parcelize
data class MHComicData(val data: ArrayList<String>) : Parcelable
