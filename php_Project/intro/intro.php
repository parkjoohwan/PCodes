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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="../css/common.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript" src="../js/project.js"></script>
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
		      include "../lib/left_menu_shop.php";
		  ?>
    </div>
  </div>

	<div id="col2">
    <div id="title">
      <!-- <img src="../img/intro.gif"> -->
    </div>

    <div align="center">
				<div style="margin-top:100px; margin-bottom:100px">
					<img src="../img/ceohello.jpg">
				</div>
				<div style="margin-bottom:100px">
					<img src="../img/ceo.jpg">
				</div>

				<div style="margin-bottom:100px">
					<img src="../img/intromessage.jpg">
				</div>

    </div>

  </div>



</div>
<div id="footer">
	<p><br>상호명 : 컷트의 달인 | 대표 : 마스터 | 개인정보보호관리자 : 마스터 | 주소: 충남 천안시 동남구 병천면 충절로 1600 한국기술교육대학교</p>
	<p>사업자등록번호: 100-20-12999 [사업자정보확인] | 통신판매업신고: 제2018-충남 천안-00000호 | 전화번호 : 041-000-0000 | 메일 : masterofcut@koreatech.ac.kr</P>
	<p>2018 Web Programing term-project by woo and park</p>
</div>
</div>

  </body>
</html>
