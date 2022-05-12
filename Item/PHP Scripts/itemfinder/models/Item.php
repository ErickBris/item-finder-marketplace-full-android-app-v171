<?php
 
class Item
{
	public $item_id;
    public $item_name;
    public $item_desc;
    public $item_price;
    public $item_currency;
    public $item_last_price;
    public $item_tag;
    public $category_id;
    public $featured;
    public $user_id;

    public $created_at;
    public $updated_at;
    public $is_deleted;

    public $item_status;
    public $item_type;

    public $lat;
    public $lon;
    public $is_published;
    public $category;


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