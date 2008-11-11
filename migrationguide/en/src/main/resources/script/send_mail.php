<?php
//define messages
$message_error = "Please, enter your message";
$email_error = "Please, specify your email address";
$subject_error = "Subject should be maximum 255 chars length";
$message_not_sent = "Error sending message!";
$message_success = "Message is sent successfully";

//define pattern
$email_pat = "/^([A-z0-9|.|-|_]{2,256})@(([A-z0-9|.|-]{2,256}).([a-z]{2,4}))*$/"; 
$general_pat = "/^(\s)+$/";
// clean up POST data
$_POST = array_map('trim', $_POST);
   
// check if 'subject' field has been filled or not
if(!$_POST['subject']){
  $subject="(no subject)";
}else{
	$subject = substr($POST['subject'], 0, 255);
	echo $subject;
}    

// check if 'name' field has been filled or not
if(!$_POST['name']){
  $name="(no name)";
}else{
	$name = substr($POST['name'], 0, 255);
}        

// check if 'message' field has been filled or not
if(!$_POST['message'] || preg_match($general_pat, $_POST['message'])){
  echo $message_error;
  exit();
}else{
	$message = substr($_POST['message'], 0, 3000);
}        
 
// check if 'email' field has been filled or not
if(!$_POST['email']){
  echo $email_error;
  exit();
}elseif(!preg_match($email_pat, $_POST['email'])){
echo $email_error;
  exit();
}else{
	$email = substr($POST['email'], 0, 255);
}

// get neccessary fields
$to='smukhina@exadel.com';

// define MIME headers
$headers="MIME-Version 1.0"."\r\n"."Content-Type: text/plain; charset=utf-8"."\r\n"."From: \"".$name."\" "."<".$email.">"."Reply-to: \"".$name."\" "."<".$email.">";

// send email
if(!@mail($to, $subject, $message, $headers)){
  echo $message_not_sent;
  exit();
}else{
  echo $message_success;
  exit();
}
?>