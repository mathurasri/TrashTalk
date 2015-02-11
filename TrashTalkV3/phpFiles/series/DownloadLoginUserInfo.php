<?php
$response = array();
if (isset($_POST['name'] ))
{
	$mysql_db = 'trashtalk';
	$conn = mysql_connect('127.0.0.1','root', '');
	if(!$conn)
	{
		die("Could not connect.");
	}
	$response["imageUri"] = "No image present";
	$username = $_POST['name'];
	//$username = "test1";
	$sql = "SELECT * FROM loginuseractivityinfo WHERE loginUser = '$username'";
	//$sql = 'SELECT * FROM loginuseractivityinfo WHERE loginUser = "test1"';
	//$sql = 'SELECT * FROM users WHERE username="xxxx" AND password = "ttttt"';
	mysql_select_db($mysql_db) or die("Could not connect to DB.");
	$retval = mysql_query($sql, $conn);	
	if(!empty($retval))
	{
		if (mysql_num_rows($retval) > 0)
		{
			$retval = mysql_fetch_array($retval);	
			//echo $retval["imagePath"];
			$response["success"] = 1;
			$response["message"] = "User image downloaded";
			$response["imageUri"] = (String)$retval["imagePath"];
			//$response["imageUri"] = "Test";
			echo json_encode($response);			
		}
		else
		{
			$response["success"] = 0;
			$response["message"] = "Could not download user image";
			$response["imageUri"] = "No image present";
			echo json_encode($response);
			//echo "No rows found";
		}		
		
	}	
	else
	{
		$response["success"] = 0;
			$response["message"] = "Could not download the image";
			$response["imageUri"] = "No image present";
			echo json_encode($response);
			//echo "No rows found";
	}

	mysql_close($conn);
}
else
{
	$response["success"] = 0;
	$response["message"] = "could not download the image";
	$response["imageUri"] = "No image present";
	echo json_encode($response);
}
?>