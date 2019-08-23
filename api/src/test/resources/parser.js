function parseTitle() {
    return jsoup.text('ul.ar_list_coc li:eq(0)') + '';
};

function DomNode(node) {
    this.text = function(cssQuery) { return (cssQuery===undefined ? node.text() : node.text(cssQuery)) + ''};
    this.src  = function(cssQuery) { return (cssQuery===undefined ? node.src() : node.src(cssQuery)) + ''};
    this.attr = function(attr,cssQuery) { return (cssQuery===undefined ? node.attr(attr) : node.attr(cssQuery,attr)) + ''};
    this.herf = function(cssQuery) { return (cssQuery===undefined ? node.herf() : node.herf(cssQuery)) + ''};
    // Warning this function return a java object
    this.list = function(cssQuery) { 
        var result = [];
        for (var i = 0; i < node.list(cssQuery); i++) {
            result.push(new DomNode(node.list(cssQuery).get(i)));
        }
        return result;
    };
}

var doc = new DomNode(jsoup)

function info(gid) {
    var result = {};
    result.info = {};
    result.info.gid = gid + '';
    result.info.title =  doc.text('ul.ar_list_coc li:eq(0)');
    result.info.titleJpn = '';
    result.info.posted = doc.text('ul.ar_list_coc li:eq(4)').replace(/[^0-9]/ig,'');
    result.info.thumb = doc.src('div.ar_list_coc img');
    result.info.uploader =  doc.text('ul.ar_list_coc li:eq(1) a'); 
    result.intro = doc.text('i#det') + '';
    result.chapterCount = doc.text('ul.ar_list_coc li:eq(3)').replace(/[^0-9]/ig,'');
    result.favoriteCount = 0;
    result.chapters = [];

    doc.list('ul.ar_rlos_bor li').forEach(function(i,v) {
        var chapter = {};
        chapter.title = v.text('a');
        chapter.url = node.href('a');
        result.chapters.push(chapter);
    });
    
    result.comments = [];
    updateTime = 0;
    
    // console.log(result);
    return JSON.stringify(result);
};

function recent() {
    var result = {};
    result.datas = [];
    $('ul.new_hits_ul li').each(function(i,n){
        var item = {};
        item.gid = $('a',n).attr('href').replace(/[^0-9]/ig,'');
        item.title = $('a',n).text();
        item.titleJpn = '';
        item.thumb = $('img',n).attr('src');
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
    $('ul.new_hits_ul li').each(function(i,n){
        var item = {};
        item.gid = $('a',n).attr('href').replace(/[^0-9]/ig,'');
        item.title = $('a',n).text();
        item.titleJpn = '';
        item.thumb = $('img',n).attr('src');
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
    $('div.ar_list_co ul dl').each(function(i,n){
        var item = {};
        item.gid = $('a',n).attr('href').replace('https://www.177mh.net','').replace(/[^0-9]/ig,'');
        item.title = $('h1',n).text();
        item.titleJpn = '';
        item.thumb = $('img',n).attr('src');
        item.category = 0;
        item.uploader = $('i.author a:eq(1)',n).text()
        console.log(item);
        result.datas.push(item);
    });
    result.pages = $('div.pages_s').text().split('/')[1].replace(/[^0-9]/ig,'');
    result.currentPage = $('div.pages_s').text().split('/')[0].replace(/[^0-9]/ig,'');

    console.log(result);   
    return JSON.stringify(result);   
};

function data() {
    var result = {};
    result.data = msg.split('|');
    
    console.log(result);   
    return JSON.stringify(result);     
    
};