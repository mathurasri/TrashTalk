<?php
$response = array();
if (isset ($_POST['name']))
{
	$mysql_db = 'trashtalk';
	$conn = mysql_connect('127.0.0.1','root', '');
	if(! $conn)
	{
		die("Could not connect.");
	}
	$name = $_POST['name'];
	$password = $_POST['password'];
	$email = $_POST['email'];
	$sql = "INSERT INTO users(username, password, email) VALUES ('$name', '$password', '$email')";
	//$sql = "INSERT INTO users(username, password, email) VALUES ('aaaaa', 'aaaaa', 'a@a.com')";
	mysql_select_db($mysql_db) or die("Could not connect to DB.");
	$retval = mysql_query($sql, $conn);
	if($retval)
	{
		$response["success"] = 1;
		$response["message"] = "User account entered";
		echo json_encode($response);
		//echo 'Data entered';
	}
	else
	{
		$response["success"] = 0;
		$response["message"] = "Could not insert the user account";
		echo json_encode($response);
		//echo 'Data not entered';
	}	
	mysql_close($conn);
}
else
{
	$response["success"] = 0;
	$response["message"] = "Required field(s) is missing";
	echo json_encode($response);
}
?>