<?

	extract($_SESSION);
	extract($_COOKIE);
	extract($_POST);
	extract($_GET);

    $connect=mysql_connect( "localhost", "root", "234567") or
        die( "SQL server에 연결할 수 없습니다.");

    mysql_select_db("project",$connect);
?>
