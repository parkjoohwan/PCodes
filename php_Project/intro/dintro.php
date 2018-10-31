<?

	extract($_SESSION);
	extract($_COOKIE);
	extract($_POST);
	extract($_GET);


	session_start();

	include "../lib/dbconn.php";

	$sql ="select * from survey";
	$result = mysql_query($sql, $connect);
	$row = mysql_fetch_array($result);

	$total = $row[ans1] + $row[ans2] + $row[ans3] + $row[ans4];

	$ans1_percent = $row[ans1]/$total * 100;
	$ans2_percent = $row[ans2]/$total * 100;
	$ans3_percent = $row[ans3]/$total * 100;
	$ans4_percent = $row[ans4]/$total * 100;

	$ans1_percent = floor($ans1_percent);
	$ans2_percent = floor($ans2_percent);
	$ans3_percent = floor($ans3_percent);
	$ans4_percent = floor($ans4_percent);
?>

<style>
.checked {
    color: orange;
}

.card {
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
    max-width: 300px;
    margin: auto;
    text-align: center;
}

.title {
    color: grey;
    font-size: 18px;
}

#dbtn {
    border: none;
    outline: 0;
    display: inline-block;
    padding: 8px;
    color: white;
    background-color: #000;
    text-align: center;
    cursor: pointer;
    width: 100%;
    font-size: 18px;
}

#dbtn:hover, .checked:hover, .title:hover, .name:hover {
    opacity: 0.7;
}

</style>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="../css/common.css">
<link href="../css/memo.css" rel="stylesheet" type="text/css" media="all">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<!-- <script type="text/javascript" src="../js/project.js"></script> -->
<!-- <link href="../bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen"> -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>


<body>
<div id="wrap">
	<div id="header">
    <? include "../lib/top_login2.php"; ?>
	</div>  <!-- end of header -->

	<div id="menu">
	<? include "../lib/top_menu4.php"; ?>
	</div>  <!-- end of menu -->
  <div id="content">
  <div id="col1">
    <div id="left_menu">
  <?
      include "../lib/left_menu_design.php";
  ?>
    </div>
  </div>




  <div id="col2">


		<div align="center" style="width:1150px;">
				<div style="margin-top:50px; margin-bottom:100px">
					<img src="../img/hairsylelist.jpg">
				</div>
			</div>

		<div style="margin-top:10px; margin-left:190px;width:1150px;">

			<div class="card" style="float:left">
			  <img src="../img/d1_1.jpg" alt="John" style="width:100%">
			  <h1 class="name">이희은</h1>
			  <p class="title">수석디자이너</p>
			  <p>Harvard University</p>
				<?
					$cnt = 0;
					while($cnt < 5){
						if($ans1_percent > 0)
							echo "<span class='fa fa-star checked'></span>&nbsp";
						else
							echo "<span class='fa fa-star'></span>&nbsp";
						$ans1_percent = $ans1_percent - 20;
						$cnt++;
					}
				?>
			  <p><button id="dbtn">Call : 010-1234-5555</button></p>
			</div>

			<div class="card" >
			  <img src="../img/d2_1.jpg" alt="John" style="width:100%">
			  <h1 class="name">여동희</h1>
			  <p class="title">차석디자이너</p>
			  <p>Harvard University</p>
				<?
					$cnt = 0;
					while($cnt < 5){
						if($ans2_percent > 0)
							echo "<span class='fa fa-star checked'></span>&nbsp";
						else
							echo "<span class='fa fa-star'></span>&nbsp";
						$ans2_percent = $ans2_percent - 20;
						$cnt++;
					}
				?>
			  <p><button id="dbtn">Call : 010-7777-8859</button></p>
			</div>

			<div class="card" style="float:left">
			  <img src="../img/d3_1.gif" alt="John" style="width:100%">
			  <h1 class="name">오영주</h1>
			  <p class="title">디자이너</p>
			  <p>Harvard University</p>
				<?
					$cnt = 0;
					while($cnt < 5){
						if($ans3_percent > 0)
							echo "<span class='fa fa-star checked'></span>&nbsp";
						else
							echo "<span class='fa fa-star'></span>&nbsp";
						$ans3_percent = $ans3_percent - 20;
						$cnt++;
					}
				?>
			  <p><button id="dbtn">Call : 010-3434-1212</button></p>
			</div>

			<div class="card">
			  <img src="../img/d4_1.jpg" alt="John" style="width:100%">
			  <h1 class="name">김하온</h1>
			  <p class="title">디자이너</p>
			  <p>Harvard University</p>
				<?
					$cnt = 0;
					while($cnt < 5){
						if($ans4_percent > 0)
							echo "<span class='fa fa-star checked'></span>&nbsp";
						else
							echo "<span class='fa fa-star'></span>&nbsp";
						$ans4_percent = $ans4_percent - 20;
						$cnt++;
					}
				?>
			  <p><button id="dbtn">Call : 010-1354-5142</button></p>
			</div>
			<!-- <ul style="width:1150px; height:686.7px;" >
			  <li class="span4" id="desigener">
			    <div class="thumbnail" align="center">
			      <img src="../img/d1_1.jpg" alt="">
			      <h3>이희은</h3>
			      <p>수석디자이너</p>
						<span class="fa fa-star checked"></span>
						<span class="fa fa-star checked"></span>
						<span class="fa fa-star checked"></span>
						<span class="fa fa-star"></span>
						<span class="fa fa-star"></span>
			    </div>
			  </li>

				<li class="span4" id="desigener">
			    <div class="thumbnail" align="center">
			      <img src="../img/d2_1.jpg" alt="">
			      <h3>여동희</h3>
			      <p>차석디자이너</p>
						<span class="fa fa-star checked"></span>
						<span class="fa fa-star checked"></span>
						<span class="fa fa-star checked"></span>
						<span class="fa fa-star"></span>
						<span class="fa fa-star"></span>
			    </div>
			  </li>

				<li class="span4" id="desigener">
			    <div class="thumbnail" align="center">
			      <img src="../img/d3_1.gif" alt="">
			      <h3>오영주</h3>
			      <p>디자이너</p>
						<span class="fa fa-star checked"></span>
						<span class="fa fa-star checked"></span>
						<span class="fa fa-star checked"></span>
						<span class="fa fa-star"></span>
						<span class="fa fa-star"></span>
			    </div>
			  </li>

				<li class="span4" id="desigener">
			    <div class="thumbnail" align="center">
			      <img src="../img/d4_1.jpg" alt="">
			      <h3>김하온</h3>
			      <p>디자이너</p>
						<span class="fa fa-star checked"></span>
						<span class="fa fa-star checked"></span>
						<span class="fa fa-star checked"></span>
						<span class="fa fa-star"></span>
						<span class="fa fa-star"></span>
			    </div>
			  </li>

				<li class="span4" id="desigener">
			    <div class="thumbnail" align="center">
			      <img src="../img/d5.jpg" alt="">
			      <h3>이지은</h3>
			      <p>보조스태프</p>
						<span class="fa fa-star checked"></span>
						<span class="fa fa-star checked"></span>
						<span class="fa fa-star checked"></span>
						<span class="fa fa-star"></span>
						<span class="fa fa-star"></span>
			    </div>
			  </li>

				<li class="span4" id="desigener">
			    <div class="thumbnail" align="center">
			      <img src="../img/d6.jpg" alt="">
			      <h3>김현아</h3>
			      <p>보조스태프</p>
						<span class="fa fa-star checked"></span>
						<span class="fa fa-star checked"></span>
						<span class="fa fa-star checked"></span>
						<span class="fa fa-star"></span>
						<span class="fa fa-star"></span>
			    </div>
			  </li>
			</ul> -->
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
