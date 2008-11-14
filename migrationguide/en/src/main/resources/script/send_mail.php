<?php
//define messages
$message_error = "Please, enter your message";
$email_error = "Please, specify your email address";
$subject_error = "Subject should be maximum 255 chars length";
$message_not_sent = "Error sending message!";
$message_success = "Message is sent successfully";

//get $_REQUEST Array
$_REQUEST = array_map('trim', $_REQUEST);
$email = $_REQUEST['email'];
$name= $_REQUEST['name'];
$subject = $_REQUEST['subject'];
$message = $_REQUEST['message'];
$path = $_REQUEST['path'];
//define patterns
$email_pat = "/^([A-z0-9|.|-|_]{2,256})@(([A-z0-9|.|-]{2,256}).([a-z]{2,4}))*$/"; 
$general_pat = "/^(\s)+$/";

// check if 'subject' field has been filled or not
if(!$subject){
	$subject="(no subject)";
}else{
	$subject = substr(htmlentities($subject), 0, 255);
}    

// check if 'name' field has been filled or not
if(!$name){
  $name="(no name)";
}else{
	$name = substr(htmlentities($name), 0, 255);
}        

// check if 'message' field has been filled or not
if(!$message || preg_match($general_pat, $message)){
  echo $message_error;
  exit();
}else{
	$message = "Feedback from ".$path."\n"."Message: ".substr(htmlentities($message), 0, 3000);
}        
 
// check if 'email' field has been filled or not
if(!preg_match($email_pat, $email)){
  echo $email_error;
  exit();
}

// define MIME headers
$headers="MIME-Version 1.0"."\r\n"."Content-Type: text/plain; charset=utf-8"."\r\n"."From: \"".$name."\" "."<".$email.">"."Reply-to: \"".$name."\" "."<".$email.">";

 //Load in the files we'll need
require_once "swift_lib/Swift.php";
require_once "swift_lib/Swift/Connection/SMTP.php";
require_once "swift_lib/Swift/Connection/Sendmail.php";
try{
//Start Swift
$sendmail =& new Swift_Connection_Sendmail();
$sendmail->setTimeout(3); //3 seconds

//$swift =& new Swift(new Swift_Connection_SMTP("localhost"));

$swift =& new Swift($sendmail);
$sender =& new Swift_Address($email, $name);
$me =& new Swift_Address("ggalkin@exadel.com");
$body =& new Swift_Message($subject, $message);
	if($swift->send($body, $me, $sender)){
		echo $message_success;
		exit();
	}else{
		echo $message_not_sent;
		exit();
	} 
}catch (Swift_ConnectionException $e) {
  echo "There was a problem communicating with SMTP: " . $e->getMessage();
} catch (Swift_Message_MimeException $e) {
  echo "There was an unexpected problem building the email:" . $e->getMessage();
}
?>