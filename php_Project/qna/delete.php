<?

	extract($_SESSION);
	extract($_COOKIE);
	extract($_POST);
	extract($_GET);
   session_start();

   include "../lib/dbconn.php";

   $sql = "delete from $table where num = $num";
   mysql_query($sql, $connect);

   mysql_close();

   echo "
	   <script>
	    location.href = 'list.php?table=$table';
	   </script>
	";
?>
