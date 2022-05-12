<?php
 
class Photo
{
	public $photo_id;
    public $photo_url;
    public $thumb_url;
    public $item_id;
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