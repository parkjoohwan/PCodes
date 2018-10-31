<?


	extract($_SESSION);
	extract($_COOKIE);
	extract($_POST);
	extract($_GET);
  session_start();
  unset($_SESSION['userid']);
  unset($_SESSION['username']);
  unset($_SESSION['usernick']);
  unset($_SESSION['userlevel']);

  echo("
       <script>
          location.href = '../index.php';
         </script>
       ");
?>
