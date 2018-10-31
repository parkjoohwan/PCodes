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
<link rel="stylesheet" type="text/css" href="css/common.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript" src="js/project.js"></script>
<script>
	window.onkeydown = function() {
		if(event.keyCode == 27){
			document.getElementById('aa');
			aa.remove();
		}
	}
</script>
</head>

<body>
	<embed src="music.mp3" hidden="true" id="aa">
<div id="wrap">
	<div id="header">
    <? include "./lib/top_login1.php"; ?>
	</div>  <!-- end of header -->

	<div id="menu">
		<? include "./lib/top_menu1.php"; ?>
	</div>  <!-- end of menu -->

  <div id="content">
		<div id="main_img">
		<!-- <div id="main_img"><img src="./img/main_img.jpg"> -->
			<div id = "slider_container">

				<div id = "slider">
					<img src="./img/sli3.jpg" >
					<img src="./img/sli1.jpg" class = "page" >
					<img src="./img/sli2.jpg" class = "page" >
					<img src="./img/sli3.jpg" class = "page" >
					<img src="./img/sli1.jpg" >
				</div>

				<div id = "slider_navi">
					<div class = "btn" id = "btn_left">◀</div>
					<div class = "btn" id = "btn_right">▶</div>
				</div>
			</div>
		</div>
		<!-- </div> -->

<!-- 최근 글 불러오기 시작 -->
        <? include "./lib/func.php"; ?>

		<div id="latest">

			<div id="latest1">
				<div id="title_latest2"><a href="./free/notice.php"><img src="./img/notice.gif"></div>
	  			<div class="latest_box">
				<? latest_article("notice", 9, 30); ?>
				</div>
			</div>

			<div id="latest3">
				<div id="title_latest2"><a href="./free/list.php"><img src="./img/title_free.gif"></div>
	  			<div class="latest_box">
				<? latest_article("free", 9, 30); ?>
				</div>
			</div>

			<div id="latest4">
				<div id="title_latest2"><img src="./img/shopreser.gif"></div>
				<div id = "shopreser">
					<a href="./qna/list.php"><img src="./img/shopreser.jpg" ></a>
				</div>
			</div>

			<div id="latest2">
				<div id="title_latest1"><img src="./img/event.gif"></div>
	  			<div class="latest_box">
						<div id = "slider_container2">

							<div id = "slider2">
								<a href="./free/view.php?table=event&num=3&page=1"><img src="./img/event3.jpg" ></a>
								<a href="./free/view.php?table=event&num=1&page=1"><img src="./img/event1.jpg" class = "page2" ></a>
								<a href="./free/view.php?table=event&num=2&page=1"><img src="./img/event2.jpg" class = "page2" ></a>
								<a href="./free/view.php?table=event&num=3&page=1"><img src="./img/event3.jpg" class = "page2" ></a>
								<a href="./free/view.php?table=event&num=1&page=1"><img src="./img/event1.jpg" ></a>
							</div>

							<div id = "slider_navi2">
								<div class = "btn2" id = "btn_left2">◀</div>
								<div class = "btn2" id = "btn_right2">▶</div>
							</div>
						</div>
				</div>
			</div>

		</div>
<!-- 최근 글 불러오기 끝 -->



  </div> <!-- end of content -->
	<div id="footer">
		<p><br>상호명 : 컷트의 달인 | 대표 : 마스터 | 개인정보보호관리자 : 마스터 | 주소: 충남 천안시 동남구 병천면 충절로 1600 한국기술교육대학교</p>
		<p>사업자등록번호: 100-20-12999 [사업자정보확인] | 통신판매업신고: 제2018-충남 천안-00000호 | 전화번호 : 041-000-0000 | 메일 : masterofcut@koreatech.ac.kr</P>
		<p>2018 Web Programing term-project by woo and park</p>
	</div>
</div> <!-- end of wrap -->




</body>
</html>
