<?php
 
class Message
{
	public $msg_id;
    public $msg;
    public $item_id;
    public $user_id;

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