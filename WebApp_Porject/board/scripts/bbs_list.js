var BBS = {};

BBS.getTestData = function() {
    var rows = [], row = {};
    row.idx = 1;
    row.subject = '테스트 게시물 입니다.';
    row.readCount = 1;
    row.content = '테스트 게시물 본문 입니다.';
    row.inputUserName = '홍길동';
    row.inputDate = '2015-01-02';
    row.updateDate = '2015-01-02';

    rows.push(row);
    rows.push(row);

    return rows;
}


BBS.display = function(rows) {
    var listArea = $('#bbsListArea');
    var html = null;

    COMM.loadTemplate('bbs_list_line', function() {
        for (var inx=0; inx<rows.length; inx++) {
            rows[inx].additionalInfo = rows[inx].updateDate + ' | ' +   rows[inx].inputUserName + ' (' + rows[inx].readCount + ')'
            html = COMM.getHTMLObj('bbs_list_line', rows[inx]);
            html.find('.bbs_list_txt').attr('data', JSON.stringify(rows[inx]));
            html.find('.bbs_list_txt').on('click', function(event) {
                COMM.movePage('./board_detail.html', $(this).attr('data'));
            });
            listArea.append(html);
        }
    });
}


$(document).ready(function() {
    BBS.display(BBS.getTestData());
});
