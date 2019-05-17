package com.axlecho.api.manhuagui

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