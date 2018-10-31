  <link href="../css/login.css" rel="stylesheet" type="text/css" media="all">
  <script type="text/javascript" src="../js/login.js"></script>
  <div id="logo" ><a href="../index.php"><img src="../img/logo.gif" border="0" ></a></div>
	<div id="top_login">
<?

  extract($_SESSION);
  extract($_COOKIE);
  extract($_POST);
  extract($_GET);
    if(!$userid)
	{
?>
          <!-- <a href="../login/login_form.php">로그인</a> | <a href="../member/member_form.php">회원가입</a> -->
          <a href="#" onclick="document.getElementById('id01').style.display='block'">로그인</a> | <a href="../member/member_form.php">회원가입</a>

          <div id="id01" class="modal">
            <form class="modal-content animate" action="../login/login.php">
              <div class="imgcontainer">
                <span onclick="document.getElementById('id01').style.display='none'" class="close" title="Close Modal">&times;</span>
                <img src="../img/logo.gif" alt="Avatar" class="avatar" >
              </div>

              <div class="container">
                <label for="uname"><b>Username</b></label>
                <input id="inputtext" type="text" placeholder="Enter Username" name="id" required>

                <label for="psw"><b>Password</b></label>
                <input id="inputtext" type="password" placeholder="Enter Password" name="pass" required>
                <div id="login_button">
                  <button id="loginbtn" type="submit">Login</button>
                </div>
              </div>

              <div class="container" style="background-color:#f1f1f1" align="center">
                <button type="button" id="cancelbtn" onclick=location.href="../member/member_form.php">SignUp</button>
                <button type="button" onclick="document.getElementById('id01').style.display='none'" id="signbtn">Cancel</button>
                <!-- <span class="psw">Forgot <a href="#">password?</a></span> -->
              </div>
            </form>
          </div>
<?
	}
	else
	{
?>
		<?=$usernick?> (level:<?=$userlevel?>) |
		<a href="../login/logout.php">로그아웃</a> | <a href="../login/member_form_modify.php">정보수정</a>
<?
	}
?>
	 </div>
