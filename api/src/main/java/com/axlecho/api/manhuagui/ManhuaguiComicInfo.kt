package com.axlecho.api.manhuagui

import com.google.gson.JsonElement
import com.google.gson.JsonObject

data class ManhuaguiRankingInfo(
        val data: Data,
        val success: Boolean
)

data class Data(
        val s1: Int,
        val s2: Int,
        val s3: Int,
        val s4: Int,
        val s5: Int
)

data class ManhuaguiImageInfo(
    val bid: Int,
    val block_cc: String,
    val bname: String,
    val bpic: String,
    val cid: Int,
    val cname: String,
    val files: List<String>,
    val finished: Boolean,
    val len: Int,
    val nextId: Int,
    val path: String,
    val prevId: Int,
    val sl: Sl,
    val status: Int
)

data class Sl(
    val md5: String
)


data class ManhuaguiCommentInfo(
    val commentIds: List<String>,
    val comments: JsonObject,
    val total: Int
)


data class CommentsData(
    val add_time: String,
    val avatar: String,
    val content: String,
    val id: Int,
    val reply_count: Int,
    val sex: Int,
    val support_count: Int,
    val user_id: Int,
    val user_name: String
)