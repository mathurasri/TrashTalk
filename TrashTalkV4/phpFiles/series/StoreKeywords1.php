<?php
  
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['keyword'])) {
 
	$keyword = json_decode($_POST['keyword']);
    
    // connecting to db
    $dbconn = mysql_connect('127.0.0.1','root', '');;
	$sql = "INSERT INTO keyword(keyword) VALUES ('$keyword')";
	mysql_select_db('trashtalk') or die("Could not connect to DB.");
    // mysql inserting a new row
    $result = mysql_query($sql, $dbconn);
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Product successfully created.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>