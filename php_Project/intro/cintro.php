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

<script type="text/javascript" src="https://openapi.map.naver.com/openapi/v3/maps.js?clientId=BTv0lmDnWE1D8aVeRqsW&submodules=geocoder"></script>

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
					<img src="../img/cintrohello.jpg">
				</div>
        <img src="../img/test.jpg" alt="" align= "center" height="500px" width="1150px">
				<img src="../img/inf.jpg" alt="" align= "center"style="margin-top:50px;" >
        <div id="map" style="width:82%; height:500px; margin-top:50px; margin-bottom:150px;"></div>

        <!-- <script>
          var map = new naver.maps.Map('map', {center: new naver.maps.LatLng(36.7637515,127.2819829)});
        </script> -->


        <script>
          var map = new naver.maps.Map('map');
          var myaddress = '충청남도 천안시 동남구 병천면 충절로 1600';// 도로명 주소나 지번 주소만 가능 (건물명 불가!!!!)
          naver.maps.Service.geocode({address: myaddress}, function(status, response) {
              if (status !== naver.maps.Service.Status.OK) {
                  return alert(myaddress + '의 검색 결과가 없거나 기타 네트워크 에러');
              }
              var result = response.result;
              // 검색 결과 갯수: result.total
              // 첫번째 결과 결과 주소: result.items[0].address
              // 첫번째 검색 결과 좌표: result.items[0].point.y, result.items[0].point.x
              var myaddr = new naver.maps.Point(result.items[0].point.x, result.items[0].point.y);
              map.setCenter(myaddr); // 검색된 좌표로 지도 이동
              // 마커 표시
              var marker = new naver.maps.Marker({
                position: myaddr,
                map: map
              });
              // 마커 클릭 이벤트 처리
              naver.maps.Event.addListener(marker, "click", function(e) {
                if (infowindow.getMap()) {
                    infowindow.close();
                } else {
                    infowindow.open(map, marker);
                }
              });
              // 마크 클릭시 인포윈도우 오픈
              var infowindow = new naver.maps.InfoWindow({
                  content: '<h4> [네이버 개발자센터]</h4><a href="https://developers.naver.com" target="_blank"><img src="https://developers.naver.com/inc/devcenter/images/nd_img.png"></a>'
              });
          });
      </script>

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


<!-- <?php
  $client_id = "BTv0lmDnWE1D8aVeRqsW";
  $client_secret = "hxJCOXP0V_";
  $encText = urlencode("충절로 1600");
  $url = "https://openapi.naver.com/v1/map/geocode?query=".$encText; // json
  // $url = "https://openapi.naver.com/v1/map/geocode.xml?query=".$encText; // xml

  $is_post = false;
  $ch = curl_init();
  curl_setopt($ch, CURLOPT_URL, $url);
  curl_setopt($ch, CURLOPT_POST, $is_post);
  curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
  $headers = array();
  $headers[] = "X-Naver-Client-Id: ".$client_id;
  $headers[] = "X-Naver-Client-Secret: ".$client_secret;
  curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
  $response = curl_exec ($ch);
  $status_code = curl_getinfo($ch, CURLINFO_HTTP_CODE);
  echo "status_code:".$status_code."<br>";
  curl_close ($ch);
  if($status_code == 200) {
    echo $response;
  } else {
    echo "Error 내용:".$response;
  }
?> -->
