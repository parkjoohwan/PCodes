<?

	extract($_SESSION);
	extract($_COOKIE);
	extract($_POST);
	extract($_GET);
   include "../lib/dbconn.php";

   $sql = "update survey set $composer = $composer + 1";
   mysql_query($sql, $connect);

   mysql_close();

   Header("location:result.php");
?>
