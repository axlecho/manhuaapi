package com.axlecho.api.pica

import com.axlecho.api.*
import com.axlecho.api.untils.tranferTimePica


class PicaParser {
    companion object {
        private val Tag: String = PicaParser::javaClass.name
        fun String.filterDigital(): String {
            return this.replace("[^0-9]".toRegex(), "");
        }

        fun parserSearchComicList(src: PicaSearchResult.Result): MHMutiItemResult<MHComicInfo> {

            val currentPage = src.data.comics.page
            var pages = src.data.comics.pages

            val result = ArrayList<MHComicInfo>()
            src.data.comics.docs
            for (doc in src.data.comics.docs) {
                val gid = doc._id
                val title = doc.title
                val titleJpn = ""
                val thumb = MHConstant.PICA_MEDIA + doc.thumb.path
                val category = 0
                val posted = ""
                val uploader = doc.author
                val rating = 0.0f
                val rated = rating != 0.0f
                val info = MHComicInfo(gid, title, titleJpn, thumb, category, posted, uploader, rating, rated, MHApiSource.Pica)
                result.add(info)
            }
            return MHMutiItemResult(result, pages, currentPage)
        }

        fun parserTopComicList(src: PicaTopResult.Result): MHMutiItemResult<MHComicInfo> {

            val currentPage = 1
            var pages = 1

            val result = ArrayList<MHComicInfo>()

            for (doc in src.data.comics) {
                val gid = doc._id
                val title = doc.title
                val titleJpn = ""
                val thumb = MHConstant.PICA_MEDIA + doc.thumb.path
                val category = 0
                val posted = ""
                val uploader = doc.author
                val rating = 0.0f
                val rated = rating != 0.0f
                val info = MHComicInfo(gid, title, titleJpn, thumb, category, posted, uploader, rating, rated, MHApiSource.Pica)
                result.add(info)
            }
            return MHMutiItemResult(result, pages, currentPage)
        }

        fun parserInfo(src: PicaDetailResult.Result, chaptersSrc: PicaChapterResult.Result): MHComicDetail {
            src.data.comic._id
            val gid = src.data.comic._id
            val title = src.data.comic.title
            val titleJpn = ""
            val thumb = MHConstant.PICA_MEDIA + src.data.comic.thumb.path
            val category = 0
            val posted = src.data.comic.updated_at
            val uploader = src.data.comic._creator.name
            // val rating = body.text("div.score > div#scoreRes > div.total > p.score-avg > em")?.toFloat() ?: 0.0f
            val rating = 0.0f
            val rated = false
            val ratingCount = 0
            val into = src.data.comic.description
            val chapterCount = src.data.comic.epsCount
            val favoriteCount = src.data.comic.likesCount
            val isFavorited = false
            val updateTime = tranferTimePica(posted)

            val chapters = ArrayList<MHComicChapter>()
            for (c in chaptersSrc.data.eps.docs) {
                chapters.add(MHComicChapter(c.title, c.order.toString(), MHApiSource.Pica))
            }
            val comments = ArrayList<MHComicComment>()


            return MHComicDetail(MHComicInfo(gid, title, titleJpn, thumb, category,
                    posted, uploader, rating, rated, MHApiSource.Pica),
                    into, chapterCount, favoriteCount, isFavorited, ratingCount, chapters, comments, MHApiSource.Pica, updateTime)
        }


        fun parserRaw(url: String): String {
            return url
        }

        fun parserData(src: PicaDataResult.Result): MHComicData {
            val data = arrayListOf<String>()
            for (item in src.data.pages.docs) {
                val url = MHConstant.PICA_MEDIA + item.media.path
                data.add(url)
            }
            return MHComicData(data, MHApiSource.Pica)
        }

        fun parserComment(src: PicaCommentResult.Result): MHMutiItemResult<MHComicComment> {
            // Logger.json(Gson().toJson(comments))

            val datas = ArrayList<MHComicComment>()
            if (src != null && src.data != null && src.data.topComments != null) {
                for (comment in src.data.topComments) {
                    val id = comment._user._id
                    val score = comment.likesCount
                    val time = comment.created_at
                    val user = comment._user.name
                    val commentContent = comment.content
                    datas.add(MHComicComment(id, score, time, user, commentContent, MHApiSource.Pica))
                }
            }

            if (src != null && src.data != null && src.data.comments != null && src.data.comments.docs != null) {
                for (comment in src.data.comments.docs) {
                    val id = comment._user._id
                    val score = comment.likesCount
                    val time = comment.created_at
                    val user = comment._user.name
                    val commentContent = comment.content
                    datas.add(MHComicComment(id, score, time, user, commentContent, MHApiSource.Pica))
                }
            }
            val pages = src.data.comments.pages
            val page = src.data.comments.page.filterDigital().toInt() - 1
            return MHMutiItemResult(datas, pages, page)
        }
    }
}
