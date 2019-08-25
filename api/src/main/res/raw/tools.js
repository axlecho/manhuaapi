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
};

var api = {};
api.doc = new DomNode(jsoup);
api.console = {
    log : function(msg) {log.println(msg)}
};
module = {};
