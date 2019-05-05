package com.axlecho.api.bangumi

data class BangumiComicInfo(
    val air_date: String,
    val air_weekday: Int,
    val blog: List<Blog>,
    val collection: Collection,
    val crt: List<Crt>,
    val eps: Any,
    val id: Long,
    val images: Images,
    val name: String,
    val name_cn: String,
    val rank: Int,
    val rating: Rating,
    val staff: List<Staff>,
    val summary: String,
    val topic: List<Topic>,
    val type: Int,
    val url: String
)

data class Images(
    val common: String,
    val grid: String,
    val large: String,
    val medium: String,
    val small: String
)

data class Rating(
    // val count: Count,
    val score: Float,
    val total: Int
)

//data class Count(
//    val 1: Int,
//    val 10: Int,
//    val 2: Int,
//    val 3: Int,
//    val 4: Int,
//    val 5: Int,
//    val 6: Int,
//    val 7: Int,
//    val 8: Int,
//    val 9: Int
//)

data class Topic(
    val id: Int,
    val lastpost: Int,
    val main_id: Int,
    val replies: Int,
    val timestamp: Int,
    val title: String,
    val url: String,
    val user: User
)

data class User(
    val avatar: Avatar,
    val id: Int,
    val nickname: String,
    val sign: Any,
    val url: String,
    val username: String
)

data class Avatar(
    val large: String,
    val medium: String,
    val small: String
)

data class Crt(
    val actors: Any,
    val collects: Int,
    val comment: Int,
    val id: Int,
    val images: ImagesX,
    val info: Info,
    val name: String,
    val name_cn: String,
    val role_name: String,
    val url: String
)

data class ImagesX(
    val grid: String,
    val large: String,
    val medium: String,
    val small: String
)

data class Info(
    val name_cn: String
)

data class Staff(
    val collects: Int,
    val comment: Int,
    val id: Int,
    val images: ImagesXX,
    val info: InfoX,
    val jobs: List<String>,
    val name: String,
    val name_cn: String,
    val role_name: String,
    val url: String
)

data class ImagesXX(
    val grid: String,
    val large: String,
    val medium: String,
    val small: String
)

data class InfoX(
    val alias: Alias,
    val birth: String,
    val name_cn: String,
    val source: String,
    val 出版者: String,
    val 创刊国家: String,
    val 发行周期: String,
    val 官方网站: String,
    val 类别: String,
    val 语言: String
)

data class Alias(
    val en: String,
    val jp: String,
    val kana: String,
    val nick: String
)

data class Blog(
    val dateline: String,
    val id: Int,
    val image: String,
    val replies: Int,
    val summary: String,
    val timestamp: Int,
    val title: String,
    val url: String,
    val user: UserX
)

data class UserX(
    val avatar: AvatarX,
    val id: Int,
    val nickname: String,
    val sign: Any,
    val url: String,
    val username: String
)

data class AvatarX(
    val large: String,
    val medium: String,
    val small: String
)

data class Collection(
    val collect: Int,
    val doing: Int,
    val dropped: Int,
    val on_hold: Int,
    val wish: Int
)