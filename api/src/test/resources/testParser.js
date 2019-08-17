function parseTitle() {
    return $("ul.ar_list_coc li:eq(0)",doc).text()
}

function data() {

}
// {
// "info": {
//    "gid": "208146",
//    "title": "五等分的新娘",
//    "titleJpn": "五等分の花嫁",
//    "thumb": "http:\/\/lain.bgm.tv\/pic\/cover\/c\/51\/85\/208146_E98lC.jpg",
//    "category": -1,
//    "posted": "",
//    "uploader": "春场葱",
//    "rating": 7.3,
//    "rated": true,
//    "source": "Bangumi"
//  },
//  "intro": "　　一直過著貧窮生活的資優生上杉風太郎，有一天居然得到了一個從天而降的大好打工機會，那就是當有錢人家女兒的家教。可是對象居然是自己曾經得罪過的女同學！不只如此，風太郎還發現了一個驚人的事實，他的學生不是一個人，而是如假包換的五胞胎！面對超有個性、瀕臨留級卻抗拒唸書的五胞胎，風太郎有辦法完成任務，讓她們五人順利畢業嗎？",
//  "chapterCount": 0,
//  "favoriteCount": 714,
//  "isFavorited": false,
//  "ratingCount": 316,
//  "chapters": [],
//  "comments": [
//    {
//      "id": "481595",
//      "score": 0,
//      "time": "2d 18h ago",
//      "user": "TeammateBane",
//      "comment": "希望最终结局是一个好结局吧。除了最终获得胜利的那位，其他四人都可以找到自己真正想要的。",
//      "source": "Bangumi"
//    }],
//  "source": "Bangumi",
//  "updateTime": 0
//}
function info(gid) {
    var result = {}
    result.info = {}
    result.info.gid = gid
    result.info.title = $("ul.ar_list_coc li:eq(0)",doc).text()
    result.info.titleJpn = ""
    result.info.thumb = $("div.ar_list_coc dl dt img").attr("img")
    return JSON.stringify(result)
}