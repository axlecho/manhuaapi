function parseTitle() {
    return $("ul.ar_list_coc li:eq(0)",doc).text();
};

function data() {

};

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
    var result = {};
    result.info = {};
    result.info.gid = gid;
    result.info.title = $("ul.ar_list_coc li:eq(0)",doc).text();
    result.info.titleJpn = "";
    result.info.posted = $("ul.ar_list_coc li:eq(4)",doc).text().replace(/[^0-9]/ig,"");
    result.info.thumb = $("div.ar_list_coc img",doc).attr("src");
    result.info.uploader =  $("ul.ar_list_coc li:eq(1) a",doc).text();
    result.intro = $("i#det",doc).text();
    result.chapterCount = $("ul.ar_list_coc li:eq(3)",doc).text().replace(/[^0-9]/ig,"");
    result.favoriteCount = 0;
    result.chapters = [];
    result.comments = [];
    updateTime = 0;
    
    console.log(result);
    return JSON.stringify(result);
};

//{
//   "datas": [
//     {  "gid": "23270",
//       "title": "Dr.STONE",
//       "titleJpn": "",
//       "thumb": "",
//       "category": 0,
//       "posted": "2019-08-17",
//       "uploader": "稻垣理一郎,Boichi",
//       "rating": 9.4,
//       "rated": false,
//       "source": "Manhuagui"
//     }],
//   "pages": 1,
//   "currentPage": 1
// }
function recent() {
     var result = {};
    result.datas = [];
    $("ul.new_hits_ul li",doc).each(function(i,n){
        var item = {};
        item.gid = $("a",n).attr("href").replace(/[^0-9]/ig,"");
        item.title = $("a",n).text();
        item.titleJpn = "";
        item.thumb = $("img",n).attr("src");
        item.category = 0;
        console.log(item);
        result.datas.push(item);
    });
    result.pages = 1;
    result.currentPage = 1;
    
    console.log(result);
    return JSON.stringify(result); 
}

function _top() {
    var result = {};
    result.datas = [];
    $("ul.new_hits_ul li",doc).each(function(i,n){
        var item = {};
        item.gid = $("a",n).attr("href").replace(/[^0-9]/ig,"");
        item.title = $("a",n).text();
        item.titleJpn = "";
        item.thumb = $("img",n).attr("src");
        item.category = 0;
        console.log(item);
        result.datas.push(item);
    });
    result.pages = 1;
    result.currentPage = 1;
    
    console.log(result);
    return JSON.stringify(result);
};

function search() {
    var result = {};
    result.datas = [];
    $("div.ar_list_co ul dl",doc).each(function(i,n){
        var item = {};
        item.gid = $("a",n).attr("href").replace("https://www.177mh.net","").replace(/[^0-9]/ig,"");
        item.title = $("h1",n).text();
        item.titleJpn = "";
        item.thumb = $("img",n).attr("src");
        item.category = 0;
        item.uploader = $("i.author a:eq(1)",n).text()
        console.log(item);
        result.datas.push(item);
    });
    result.pages = $("div.pages_s",doc).text().split("/")[1].replace(/[^0-9]/ig,"");
    result.currentPage = $("div.pages_s",doc).text().split("/")[0].replace(/[^0-9]/ig,"");
    
    console.log(result);   
    return JSON.stringify(result);   
};


 // {
// "data": [
//    "https:\/\/i.hamreus.com\/ps3\/h\/hydxjxrwgb\/第02回\/P035.jpg.webp?cid=183592&md5=1FjDMlNVXJKTULGyLOH5ow",
//    "https:\/\/i.hamreus.com\/ps3\/h\/hydxjxrwgb\/第02回\/zhaorenV1.jpg.webp?cid=183592&md5=1FjDMlNVXJKTULGyLOH5ow"
//  ],
//  "source": "Manhuagui"
// }

function data() {
    var result = {};
    result.data = msg.split("|");
    
    console.log(result);   
    return JSON.stringify(result);     
    
};