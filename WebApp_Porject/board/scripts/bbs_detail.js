var DETAIL = {};

DETAIL.display = function(data) {
    var area = $('.wrap');
    var html = COMM.dynamicExpression(area.html(), data);
    area.empty();
    area.html(html);
    area.show();
}


$(document).ready(function() {
    DETAIL.display(COMM.getPageData());
});
