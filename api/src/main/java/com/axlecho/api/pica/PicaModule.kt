package com.axlecho.api.pica

data class PicaUser(val email: String, val password: String)

class PicaLoginResult {
    data class Result(
            val data: Data,
            val code: Int,
            val message: String
    )

    data class Data(
            val token: String
    )
}

class PicaSearchResult {
    data class Result(
            val data: Data,
            val code: Int,
            val message: String
    )

    data class Data(
            val comics: Comics
    )

    data class Comics(
            val docs: List<Doc>,
            val limit: Int,
            val page: Int,
            val pages: Int,
            val total: Int
    )

    data class Doc(
            val _id: String,
            val author: String,
            val categories: List<String>,
            val epsCount: Int,
            val finished: Boolean,
            val id: String,
            val likesCount: Int,
            val pagesCount: Int,
            val thumb: Thumb,
            val title: String
    )

    data class Thumb(
            val fileServer: String,
            val originalName: String,
            val path: String
    )
}

class PicaTopResult {
    data class Result(
            val data: Data,
            val code: Int,
            val message: String
    )

    data class Data(
            val comics: List<Comic>
    )

    data class Comic(
            val _id: String,
            val author: String,
            val categories: List<String>,
            val epsCount: Int,
            val finished: Boolean,
            val leaderboardCount: Int,
            val pagesCount: Int,
            val thumb: Thumb,
            val title: String,
            val viewsCount: Int
    )

    data class Thumb(
            val fileServer: String,
            val originalName: String,
            val path: String
    )
}

class PicaDetailResult {
    data class Result(
            val data: Data,
            val code: Int,
            val message: String
    )

    data class Data(
            val comic: Comic
    )

    data class Comic(
            val _creator: Creator,
            val _id: String,
            val allowDownload: Boolean,
            val author: String,
            val categories: List<String>,
            val chineseTeam: String,
            val commentsCount: Int,
            val created_at: String,
            val description: String,
            val epsCount: Int,
            val finished: Boolean,
            val isFavourite: Boolean,
            val isLiked: Boolean,
            val likesCount: Int,
            val pagesCount: Int,
            val tags: List<Any>,
            val thumb: Thumb,
            val title: String,
            val updated_at: String,
            val viewsCount: Int
    )

    data class Creator(
            val _id: String,
            val avatar: Avatar,
            val character: String,
            val characters: List<String>,
            val exp: Int,
            val gender: String,
            val level: Int,
            val name: String,
            val role: String,
            val slogan: String,
            val title: String,
            val verified: Boolean
    )

    data class Avatar(
            val fileServer: String,
            val originalName: String,
            val path: String
    )

    data class Thumb(
            val fileServer: String,
            val originalName: String,
            val path: String
    )
}

class PicaChapterResult {
    data class Result(
            val data: Data,
            val code: Int,
            val message: String
    )

    data class Data(
            val eps: Eps
    )

    data class Eps(
            val docs: List<Doc>,
            val limit: Int,
            val page: Int,
            val pages: Int,
            val total: Int
    )

    data class Doc(
            val _id: String,
            val id: String,
            val order: Int,
            val title: String,
            val updated_at: String
    )
}

class PicaDataResult {
    data class Result(
            val data: Data,
            val code: Int,
            val message: String
    )

    data class Data(
            val ep: Ep,
            val pages: Pages
    )

    data class Pages(
            val docs: List<Doc>,
            val limit: Int,
            val page: Int,
            val pages: Int,
            val total: Int
    )

    data class Doc(
            val _id: String,
            val id: String,
            val media: Media
    )

    data class Media(
            val fileServer: String,
            val originalName: String,
            val path: String
    )

    data class Ep(
            val _id: String,
            val title: String
    )
}

class PicaCommentResult {
    data class Result(
            val data: Data,
            val code: Int,
            val message: String
    )

    data class Data(
            val comments: Comments,
            val topComments: List<TopComment>
    )

    data class Comments(
            val docs: List<Doc>,
            val limit: Int,
            val page: String,
            val pages: Int,
            val total: Int
    )

    data class Doc(
            val _comic: String,
            val _id: String,
            val _user: User,
            val commentsCount: Int,
            val content: String,
            val created_at: String,
            val hide: Boolean,
            val ip: String,
            val isLiked: Boolean,
            val isTop: Boolean,
            val likesCount: Int
    )

    data class User(
            val _id: String,
            val avatar: Avatar,
            val characters: List<Any>,
            val exp: Int,
            val gender: String,
            val level: Int,
            val name: String,
            val role: String,
            val title: String,
            val verified: Boolean
    )

    data class Avatar(
            val fileServer: String,
            val originalName: String,
            val path: String
    )

    data class TopComment(
            val _comic: String,
            val _id: String,
            val _user: UserX,
            val commentsCount: Int,
            val content: String,
            val created_at: String,
            val hide: Boolean,
            val ip: String,
            val isLiked: Boolean,
            val isTop: Boolean,
            val likesCount: Int
    )

    data class UserX(
            val _id: String,
            val avatar: AvatarX,
            val character: String,
            val characters: List<String>,
            val exp: Int,
            val gender: String,
            val level: Int,
            val name: String,
            val role: String,
            val slogan: String,
            val title: String,
            val verified: Boolean
    )

    data class AvatarX(
            val fileServer: String,
            val originalName: String,
            val path: String
    )
}