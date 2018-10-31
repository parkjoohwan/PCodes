<?
	extract($_SESSION);
	extract($_COOKIE);
	extract($_POST);
	extract($_GET);
	session_start();
?>
<style>
* {
    box-sizing: border-box;
}

/* Create three columns of equal width */
.columns {
    float: left;
    width: 25%;
    padding: 8px;
}

/* Style the list */
.price {
    list-style-type: none;
    border: 1px solid #eee;
    margin: 0;
    padding: 0;
    -webkit-transition: 0.3s;
    transition: 0.3s;
}

/* Add shadows on hover */
.price:hover {
    box-shadow: 0 8px 12px 0 rgba(0,0,0,0.2)
}

/* Pricing header */
.price .header {
    background-color: #111;
    color: white;
    font-size: 25px;
}

/* List items */
.price li {
    border-bottom: 1px solid #eee;
    padding: 20px;
    text-align: center;
}

/* Grey list item */
.price .grey {
    background-color: #eee;
    font-size: 20px;
}

/* The "Sign Up" button */
#cbtn {
    background-color: #4CAF50;
    border: none;
    color: white;
    padding: 10px 25px;
    text-align: center;
    text-decoration: none;
    font-size: 16px;
		font-weight: bold;
}

/* Change the width of the three columns to 100%
(to stack horizontally on small screens) */
@media only screen and (max-width: 600px) {
    .columns {
        width: 100%;
    }
}
</style>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<link rel="stylesheet" type="text/css" href="../css/common.css">
		<link href="../css/concert.css" rel="stylesheet" type="text/css" media="all">

		<link href="../css/login.css" rel="stylesheet" type="text/css" media="all">
	  <script type="text/javascript" src="../js/login.js"></script>

		<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script> -->

	</head>

	<body>
	<div id="wrap">

		<div id="header">
	    <? include "../lib/top_login2.php"; ?>
		</div>  <!-- end of header -->

		<div id="menu">
			<? include "../lib/top_menu3.php"; ?>
		</div>  <!-- end of menu -->

	  <div id="content">

			<!-- 사이드메뉴 -->
	  	<div id="col1">
	    	<div id="left_menu">
	  			<?
	      		include "../lib/left_menu_service.php";
	  			?>
	    	</div>
	  	</div>

			<div id="col2">
				<div align="center" style="width:1150px;">
						<div style="margin-top:50px; margin-bottom:100px">
							<img src="../img/servcehello.jpg">
						</div>
				</div>
				<script src="http://code.jquery.com/jquery.js"></script>
		    <script src="bootstrap/js/bootstrap.min.js"></script>

				<div style="margin-top:10px; width:1150px;" align="center">
		      <img src="../img/cutting.png" >
		      <img src="../img/styling.png" id="serviceintro">
		      <img src="../img/colour.png" id="serviceintro">
		      <img src="../img/re-texturizing.png"id="serviceintro">
				</div>

				<div style="margin-top:100px; width:1150px;" align="center">
					<div class="columns">
						<ul class="price">
							<li class="header">Cutting</li>
							<li class="grey">₩17.000</li>
							<li>Hair Cutting</li>
							<li>Dry & Styling</li>
							<li>Hair Massage</li>
							<li>Drink Coffee</li>
							<li class="grey"><a href="../service/cut.php" id="cbtn">View</a></li>
						</ul>
					</div>

					<div class="columns">
						<ul class="price">
							<li class="header">Styling</li>
							<li class="grey">₩15.000</li>
							<li>Simple Styling</li>
							<li>Hair MakeUp</li>
							<li>Hair Setting</li>
							<li>Curling Machine</li>
							<li class="grey"><a href="../service/styling.php" id="cbtn">View</a></li>
						</ul>
					</div>

					<div class="columns">
						<ul class="price">
							<li class="header">Dyeing</li>
							<li class="grey">₩20.000</li>
							<li>Nature Dye</li>
							<li>Colorful</li>
							<li>Scalp Care</li>
							<li>Recommend Best Color</li>
							<li class="grey"><a href="../service/dye.php" id="cbtn">View</a></li>
						</ul>
					</div>

					<div class="columns">
						<ul class="price">
							<li class="header">Perm</li>
							<li class="grey">₩40.000</li>
							<li>Heat Perm</li>
							<li>Texture Perm</li>
							<li>Setting Perm</li>
							<li>Scalp Care</li>
							<li class="grey"><a href="../service/perm.php" id="cbtn">View</a></li>
						</ul>
					</div>
					<!-- <div style="margin-top:50px;">
						<img src="../img/sintro2.jpg" style="height:300px">
					<div> -->
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
