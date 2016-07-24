<?php  
$con=mysqli_connect("127.0.0.1","root","next1004","data");  
 
mysqli_set_charset($con,"utf8");
  
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  
$name = $_POST['name'];  
$id = $_POST['id']; 
$score = $_POST['score'];
$time = $_POST['time'];  
$mode = $_POST['mode'];  
$result = array(); 
  
mysqli_query($con,"insert into Person (name,id,score,install) values ('$name','$id','$score','$time')");
if($mode==2)
{
 $idcheck = mysqli_query($con,"UPDATE Person SET login='$time' where id = '$id'");
}
$idcheck = mysqli_query($con,"UPDATE Person SET lasttime='$time' where id = '$id'");
if($idcheck)
{
    $res = mysqli_query($con,"select * from Person where id = '$id' ");
    $row = mysqli_fetch_array($res);
    
    
    if($row[2]<$score)
    {
     mysqli_query($con,"UPDATE Person SET score='$score' where id = '$id' ");
     echo "GOOD! New Record : ",$score;
    }
    else
    {
     echo "Your Record : ",$row[2];
    }
    
 }
 
   
mysqli_close($con);  
?> 
