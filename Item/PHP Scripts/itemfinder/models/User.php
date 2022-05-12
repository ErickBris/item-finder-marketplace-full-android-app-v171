<?php
 
class User
{
	public $user_id;
    public $full_name;

    public $username;
    public $password;
    public $login_hash;
    public $facebook_id;
    public $twitter_id;
    public $email;
    public $deny_access;
    public $created_at;
    public $updated_at;
    public $last_logged;

    public $sms_no;
    public $phone_no;

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