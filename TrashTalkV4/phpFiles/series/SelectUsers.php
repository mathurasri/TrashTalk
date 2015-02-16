<?php
$response = array();
if (isset($_POST['name'] )&& isset($_POST['password']))
{
	$mysql_db = 'trashtalk';
	$conn = mysql_connect('127.0.0.1','root', '');
	if(!$conn)
	{
		die("Could not connect.");
	}
	$username = $_POST['name'];
	$userpassword = $_POST['password'];
	
	
	$sql = "SELECT * FROM users WHERE username = '$username' AND password = '$userpassword'";
	//$sql = 'SELECT * FROM users WHERE username="xxxx" AND password = "ttttt"';
	mysql_select_db($mysql_db) or die("Could not connect to DB.");
	$retval = mysql_query($sql, $conn);
	
	if(!empty($retval))
	{
		if (mysql_num_rows($retval) > 0)
		{
			$response["success"] = 1;
			$response["message"] = "User account entered";
			echo json_encode($response);
			//echo 'User login right';
		}
		else
		{
			$response["success"] = 0;
			$response["message"] = "Could not insert the user account";
			echo json_encode($response);
			//echo "No rows found";
		}		
		
	}	
	else
	{
		$response["success"] = 0;
			$response["message"] = "Could not insert the user account";
			echo json_encode($response);
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