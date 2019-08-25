
var console = {
    log : function(msg) { log.println(msg)}
}

function DomNode(node) {
    this.html = function(cssQuery) { return (cssQuery === undefined ? node.html() : node.html(cssQuery)) + ''};
    this.text = function(cssQuery) { return (cssQuery === undefined ? node.text() : node.text(cssQuery)) + ''};
    this.src  = function(cssQuery) { return (cssQuery === undefined ? node.src() : node.src(cssQuery)) + ''};
    this.href = function(cssQuery) { return (cssQuery === undefined ? node.href() : node.href(cssQuery)) + ''};
    this.attr = function(attr,cssQuery) { return (cssQuery ===undefined ? node.attr(attr) : node.attr(cssQuery,attr)) + ''};
    // Warning this function return a java object
    this.list = function(cssQuery) { 
        var result = [];
        for (var i = 0; i < node.list(cssQuery).size(); i++) {
            result.push(new DomNode(node.list(cssQuery).get(i)));
        }
        return result;
    };
}

var doc = new DomNode(jsoup)

function parseTitle() {
    return jsoup.text('ul.ar_list_coc li:eq(0)') + '';
};


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

    doc.list('ul.ar_rlos_bor li').forEach(function(v,i) {
        var chapter = {};
        chapter.title = v.text('a');
        chapter.url = v.href('a');
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
    doc.list('ul.new_hits_ul li').forEach(function(v,i){
        var item = {};
        item.gid = v.href('a').replace(/[^0-9]/ig,'');
        item.title = v.text('a');
        item.thumb = v.src('img');
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
    doc.list('ul.new_hits_ul li').forEach(function(v,i){
        var item = {};
        item.gid = v.href('a').replace(/[^0-9]/ig,'');
        item.title = v.text('a');
        item.thumb = v.src('img');
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
    doc.list('div.ar_list_co ul dl').forEach(function(v,i){
        var item = {};
        item.gid = v.href('a').replace('https://www.177mh.net','').replace(/[^0-9]/ig,'');
        item.title = v.text('h1');
        item.thumb = v.src('img');
        item.uploader = v.text('i.author a:eq(1)')
        console.log(item);
        result.datas.push(item);
    });
    result.pages = doc.text('div.pages_s').split('/')[1].replace(/[^0-9]/ig,'');
    result.currentPage = doc.text('div.pages_s').split('/')[0].replace(/[^0-9]/ig,'');

    console.log(result);   
    return JSON.stringify(result);   
};

function data() {
    var result = {};
    result.data = [];
    console.log('=================')
    var script = doc.html('script')
    console.log(script)

    eval(script);
    var paths = msg.split('|');
    for (var i = 0; i < paths.length; i++) {
        result.data.push('https://hws.readingbox.net/h' + img_s + '/' + paths[i] + '.webp')
    }
    // console.log(result);   
    return JSON.stringify(result);
};