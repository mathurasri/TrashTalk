<?php
$response = array();
//if (isset($_POST['name'] ))
//{
	$mysql_db = 'trashtalk';
	$conn = mysql_connect('127.0.0.1','root', '');
	if(!$conn)
	{
		die("Could not connect.");
	}
	$sql = "SELECT * FROM keyword";
	
	mysql_select_db($mysql_db) or die("Could not connect to DB.");
	$retval = mysql_query($sql, $conn);	
	if(!empty($retval))
	{
		if (mysql_num_rows($retval) > 0)
		{
			$response["keywords"] = array();
			while($row = mysql_fetch_array($retval))
			{
				$keyword = array();
				$keyword["keyword"] = $row["keyword"];				
				array_push($response["keywords"], $keyword);
			}
			$response["success"] = 1;
			echo json_encode($response);
			/*$retval = mysql_fetch_array($retval);	
			//echo $retval["imagePath"];
			$response["success"] = 1;
			$response["message"] = "User image downloaded";
			$response["imageUri"] = (String)$retval["imagePath"];
			//$response["imageUri"] = "Test";
			echo json_encode($response);			*/
		}
		else
		{
			$response["success"] = 0;
			$response["message"] = "Could not download user image";			
			echo json_encode($response);
			//echo "No rows found";
		}		
		
	}	
	else
	{
		$response["success"] = 0;
			$response["message"] = "Could not download the image";			
			echo json_encode($response);
			//echo "No rows found";
	}

	mysql_close($conn);
//}
/*else
{
	$response["success"] = 0;
	$response["message"] = "could not download the image";
	$response["imageUri"] = "No image present";
	echo json_encode($response);
}*/
?>