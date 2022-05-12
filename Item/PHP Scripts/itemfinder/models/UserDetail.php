<?php
 
class UserDetail
{
    public $userdetail_id;
	public $user_id;
    
    public $sms_no;
    public $email;
    public $phone_no;

    public $created_at;
    public $updated_at;
    public $is_deleted;

    // constructor
    function __construct() 
    {

    }
 
    // destructor
    function __destruct() 
    {
         
    }
}
 
?>