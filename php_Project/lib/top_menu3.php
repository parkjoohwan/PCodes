<style>
  .dropbtn {
      background-color: #000000;
      color: white;
      font-size: 16px;
      border: none;
  }

  .dropdown {
      position: relative;
      display: inline-block;
  }

  .dropdown-content {
      display: none;
      position: absolute;
      background-color: #ffffff;
      min-width: 168px;
      box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
      z-index: 1;
  }

  .dropdown-content a {
      color: #666666;
      padding: 12px 16px;
      text-decoration: none;
      display: block;
  }

  .dropdown-content a:hover {background-color: #000000; color: #ffffff }

  .dropdown:hover .dropdown-content {
      display: block;
  }

  /* 반짝이는거 */
  .dropdown:hover .dropbtn {
      background-color: #000000;
  }
</style>

<div class="dropdown" style="margin-left: 15%;">
  <button class="dropbtn"><a href="../intro/intro.php"><img src="../img/shop.gif" border="0"></a></button>
  <div class="dropdown-content">
    <h2 style="margin-left:5px; margin-right:5px; border-bottom:solid 1px #cccccc; font-family:'TimeNewRoman';"><b>SHOP<b></h2>
    <a href="../intro/intro.php"><b>CEO인사말<b></a>
    <a href="../intro/cintro.php"><b>오시는 길</b></a>
  </div>
</div>

<div class="dropdown">
  <button class="dropbtn"><a href="../intro/dintro.php"><img src="../img/designer.gif" border="0"></a></button>
  <div class="dropdown-content">
    <h2 style="margin-left:5px; margin-right:5px; border-bottom:solid 1px #cccccc; font-family:'TimeNewRoman';"><b>DESIGNER<b></h2>
    <a href="../intro/dintro.php"><b>디자이너소개<b></a>
  </div>
</div>

<div class="dropdown">
  <button class="dropbtn"><a href="../service/serviceintro.php"><img src="../img/service.gif" border="0"></a></button>
  <div class="dropdown-content">
    <h2 style="margin-left:5px; margin-right:5px; border-bottom:solid 1px #cccccc; font-family:'TimeNewRoman';"><b>SERVICE<b></h2>
    <a href="../service/serviceintro.php"><b>서비스소개<b></a>
    <a href="../service/cut.php"><b>헤어컷트<b></a>
    <a href="../service/styling.php"><b>헤어스타일링<b></a>
    <a href="../service/dye.php"><b>헤어염색<b></a>
    <a href="../service/perm.php"><b>헤어펌<b></a>
  </div>
</div>

<div class="dropdown">
  <button class="dropbtn"><a href="../free/list.php"><img src="../img/board.gif" border="0"></a></button>
  <div class="dropdown-content">
    <h2 style="margin-left:5px; margin-right:5px; border-bottom:solid 1px #cccccc; font-family:'TimeNewRoman';"><b>BOARD<b></h2>
    <a href="../free/list.php"><b>자유게시판<b></a>
    <a href="../free/event.php"><b>이벤트<b></a>
    <a href="../free/notice.php"><b>공지사항<b></a>
  </div>
</div>

<div class="dropdown">
  <button class="dropbtn"><a href="../qna/list.php"><img src="../img/q&a.gif" border="0"></a></button>
  <div class="dropdown-content">
    <h2 style="margin-left:5px; margin-right:5px; border-bottom:solid 1px #cccccc; font-family:'TimeNewRoman';"><b>Q&A<b></h2>
    <a href="../qna/list.php"><b>예약문의<b></a>
  </div>
</div>


<div class="dropdown"><a href="#"><img src="../img/survey.gif" onclick="window.open('../survey/survey.php', '','scrollbars=no, toolbars=no,width=180,height=230')" border="0"></a></div>
