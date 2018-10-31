<?

	extract($_SESSION);
	extract($_COOKIE);
	extract($_POST);
	extract($_GET);
	session_start();
	include "../lib/dbconn.php";

	$sql = "select * from $table where num=$num";
	$result = mysql_query($sql, $connect);
    $row = mysql_fetch_array($result);

	$item_num     = $row[num];
	$item_id      = $row[id];
	$item_name    = $row[name];
  	$item_nick    = $row[nick];
	$item_hit     = $row[hit];
    $item_date    = $row[regist_day];
	$item_subject = str_replace(" ", "&nbsp;", $row[subject]);
	$item_content = $row[content];
	$is_html      = $row[is_html];

	if ($is_html!="y")
	{
		$item_content = str_replace(" ", "&nbsp;", $item_content);
		$item_content = str_replace("\n", "<br>", $item_content);
	}

	$new_hit = $item_hit + 1;
	$sql = "update $table set hit=$new_hit where num=$num";   // 글 조회수 증가시킴
	mysql_query($sql, $connect);
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<link href="../css/common.css" rel="stylesheet" type="text/css" media="all">
<link href="../css/board1.css" rel="stylesheet" type="text/css" media="all">

<style>
.button {
		height:21px;
		width:51px;
		/* margin-left: 4px; */
    position: relative;
    background-color: #000000;
    border: none;
    font-size: 10px;
		font-weight: bold;
    color: #FFFFFF;
    /* padding: 20px; */

    text-align: center;
    -webkit-transition-duration: 0.4s; /* Safari */
    transition-duration: 0.4s;
    text-decoration: none;
    overflow: hidden;
    cursor: pointer;
}

.button:after {
    content: "";
    background: #eeeeee;
    display: block;
    position: absolute;
    padding-top: 300%;
    padding-left: 350%;
    margin-left: -20px!important;
    margin-top: -120%;
    opacity: 0;
    transition: all 0.8s
}

.button:active:after {
    padding: 0;
    margin: 0;
    opacity: 1;
    transition: 0s
}
</style>

<script>
    function del(href)
    {
        if(confirm("한번 삭제한 자료는 복구할 방법이 없습니다.\n\n정말 삭제하시겠습니까?")) {
                document.location.href = href;
        }
    }
</script>
</head>

<body>
<div id="wrap">
  <div id="header">
    <? include "../lib/top_login2.php"; ?>
  </div>  <!-- end of header -->
  <div id="menu">
	<? include "../lib/top_menu2.php"; ?>
  </div>  <!-- end of menu -->

  <div id="content">
	<div id="col1">
		<div id="left_menu">
<?
			include "../lib/left_menu.php";
?>
		</div>
	</div>

	<div id="col2">
		<img src=../img/boardhello.jpg>

		<div id="view_comment"> &nbsp;</div>

		<div id="view_title">
			<div id="view_title1"><?= $item_subject ?></div><div id="view_title2"><?= $item_nick ?> | 조회 : <?= $item_hit ?>
			                      | <?= $item_date ?> </div>
		</div>

		<div id="view_content">
			<?= $item_content ?>
		</div>

		<div id="view_button">
				<a href="list.php?table=<?=$table?>&page=<?=$page?>"><button class="button">목록</button></a></a>&nbsp;
<?
	if($userid && ($userid==$item_id) )
	{
?>
				<a href="write_form.php?table=<?=$table?>&mode=modify&num=<?=$num?>&page=<?=$page?>"><button class="button">수정</button></a></a>&nbsp;
				<a href="javascript:del('delete.php?table=<?=$table?>&num=<?=$num?>')"><button class="button">삭제</button></a></a>&nbsp;
<?
	}
?>
<?
	if($userid)
	{
?>
				<a href="write_form.php?table=<?=$table?>&mode=response&num=<?=$num?>&page=<?=$page?>"><img src="../img/response.png"></a>&nbsp;
				<a href="write_form.php?table=<?=$table?>"><button class="button">글쓰기</button></a></a>
<?
	}
?>
		</div>
		<div class="clear"></div>

	</div> <!-- end of col2 -->
  </div> <!-- end of content -->
	<div id="footer">
		<p><br>상호명 : 컷트의 달인 | 대표 : 마스터 | 개인정보보호관리자 : 마스터 | 주소: 충남 천안시 동남구 병천면 충절로 1600 한국기술교육대학교</p>
		<p>사업자등록번호: 100-20-12999 [사업자정보확인] | 통신판매업신고: 제2018-충남 천안-00000호 | 전화번호 : 041-000-0000 | 메일 : masterofcut@koreatech.ac.kr</P>
		<p>2018 Web Programing term-project by woo and park</p>
	</div>
</div> <!-- end of wrap -->

</body>
</html>
