<?

	extract($_SESSION);
	extract($_COOKIE);
	extract($_POST);
	extract($_GET);
	session_start();
?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
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

<meta charset="utf-8">
</head>
<?
	if ($mode=="modify" || $mode=="response")
	{
		include "../lib/dbconn.php";

		$sql = "select * from $table where num=$num";
		$result = mysql_query($sql, $connect);
		$row = mysql_fetch_array($result);

		$item_subject     = $row[subject];
		$item_content     = $row[content];

		if ($mode=="response")
		{
			$item_subject = "[re]".$item_subject;
			$item_content = ">".$item_content;
			$item_content = str_replace("\n", "\n>", $item_content);
			$item_content = "\n\n".$item_content;
		}
		mysql_close();
	}
?>
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
			include "../lib/left_menu_w.php";
?>
		</div>
	</div>

	<div id="col2">
		<div id="title">
			<p>※궁금한점은 글을 남겨주시면 답변해드립니다.^^</p>
		</div>
		<div class="clear"></div>

		<div id="write_form_title">
			<img src="../img/write_form_title.gif">
		</div>
		<div class="clear"></div>
<?
	if($mode=="modify")
	{
?>
		<form  name="board_form" method="post" action="insert.php?mode=modify&num=<?=$num?>&page=<?=$page?>&table=<?=$table?>">
<?
	}
	elseif ($mode=="response")
	{
?>
		<form  name="board_form" method="post" action="insert.php?mode=response&num=<?=$num?>&page=<?=$page?>&table=<?=$table?>">
<?
	}
	else
	{
?>
		<form  name="board_form" method="post" action="insert.php?table=<?=$table?>">
<?
	}
?>
		<div id="write_form">
			<div class="write_line"></div>
			<div id="write_row1">
				<div class="col1"> 닉네임 </div>
				<div class="col2"><?=$usernick?></div>
<?
	if( $userid && ($mode != "modify")  && ($mode != "response") )
	{
?>
				<div class="col3"><input type="checkbox" name="html_ok" value="y"> HTML 쓰기</div>
<?
	}
?>
			</div>
			<div class="write_line"></div>
			<div id="write_row2"><div class="col1"> 제목   </div>
			                     <div class="col2"><input type="text" name="subject" value="<?=$item_subject?>" ></div>
			</div>
			<div class="write_line"></div>
			<div id="write_row3"><div class="col1"> 내용   </div>
			                     <div class="col2"><textarea rows="15" cols="79" name="content"><?=$item_content?></textarea></div>
			</div>
			<div class="write_line"></div>
		</div>

		<div id="write_button"><button class="button">완료</button>&nbsp;
								<a href="list.php?table=<?=$table?>&page=<?=$page?>"><button class="button">목록</button></a>
		</div>
		</form>

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
