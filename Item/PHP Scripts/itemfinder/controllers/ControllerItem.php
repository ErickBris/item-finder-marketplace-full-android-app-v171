<?php

class ControllerItem
 {
    private $db;
    private $pdo;
    function __construct() 
    {
        // connecting to database
        $this->db = new DB_Connect();
        $this->pdo = $this->db->connect();
    }
 
    function __destruct() { }
 
    public function updateItem($itm) 
    {
        $stmt = $this->pdo->prepare('UPDATE tbl_itemfinder_items

                                        SET item_name = :item_name, 
                                            item_desc = :item_desc, 
                                            item_price = :item_price,
                                            user_id = :user_id,
                                            category_id = :category_id,
                                            created_at = :created_at, 
                                            updated_at = :updated_at, 
                                            featured = :featured, 
                                            is_deleted = :is_deleted,
                                            item_status = :item_status,
                                            item_type = :item_type, 
                                            lat = :lat, 
                                            lon = :lon,
                                            item_currency = :item_currency 

                                        WHERE item_id = :item_id');

        $result = $stmt->execute(
                            array('item_name' => $itm->item_name,
                                    'item_desc' => $itm->item_desc,  
                                    'item_price' => $itm->item_price,
                                    'user_id' => $itm->user_id,
                                    'category_id' => $itm->category_id,
                                    'created_at' => $itm->created_at,
                                    'updated_at' => $itm->updated_at,
                                    'featured' => $itm->featured,
                                    'is_deleted' => $itm->is_deleted,
                                    
                                    'item_status' => $itm->item_status,
                                    'item_type' => $itm->item_type,
                                    'item_id' => $itm->item_id,
                                    'lat' => $itm->lat,
                                    'lon' => $itm->lon,
                                    'item_currency' => $itm->item_currency  
                                    ) );
        
        return $result ? true : false;

    }

    public function updateItemNoFeatured($itm) 
    {
        $stmt = $this->pdo->prepare('UPDATE tbl_itemfinder_items

                                        SET item_name = :item_name, 
                                            item_desc = :item_desc, 
                                            item_price = :item_price,
                                            user_id = :user_id,
                                            category_id = :category_id,
                                            created_at = :created_at, 
                                            updated_at = :updated_at, 
                                            is_deleted = :is_deleted, 
                                            item_status = :item_status, 
                                            item_type = :item_type, 
                                            lat = :lat, 
                                            lon = :lon,
                                            item_currency = :item_currency  

                                        WHERE item_id = :item_id');

        $result = $stmt->execute(
                            array('item_name' => $itm->item_name,
                                    'item_desc' => $itm->item_desc,  
                                    'item_price' => $itm->item_price,
                                    'user_id' => $itm->user_id,
                                    'category_id' => $itm->category_id,
                                    'created_at' => $itm->created_at,
                                    'updated_at' => $itm->updated_at,
                                    'is_deleted' => $itm->is_deleted,
                                    
                                    'item_status' => $itm->item_status,
                                    'item_type' => $itm->item_type,
                                    'item_id' => $itm->item_id,
                                    'lat' => $itm->lat,
                                    'lon' => $itm->lon, 
                                    'item_currency' => $itm->item_currency  
                                     ) );
        
        return $result ? true : false;

    }


    public function deleteItem($item_id, $is_deleted) 
    {
        $stmt = $this->pdo->prepare('UPDATE tbl_itemfinder_items 
                                        SET is_deleted = :is_deleted 
                                        WHERE item_id = :item_id ');
        
        $result = $stmt->execute(
                            array('item_id' => $item_id, 
                                    'is_deleted' => $is_deleted) );

        return $result ? true : false;
    }

    public function soldItem($item_id, $item_status) 
    {
        $stmt = $this->pdo->prepare('UPDATE tbl_itemfinder_items 
                                        SET item_status = :item_status 
                                        WHERE item_id = :item_id ');
        
        $result = $stmt->execute(
                            array('item_id' => $item_id, 
                                    'item_status' => $item_status) );

        return $result ? true : false;
    }

    public function publishItem($item_id, $is_published) 
    {
        $stmt = $this->pdo->prepare('UPDATE tbl_itemfinder_items 
                                        SET is_published = :is_published 
                                        WHERE item_id = :item_id ');
        
        $result = $stmt->execute(
                            array('item_id' => $item_id, 
                                    'is_published' => $is_published) );

        return $result ? true : false;
    }

    public function updateItemFeatured($itm) 
    {
        $stmt = $this->pdo->prepare('UPDATE tbl_itemfinder_items 
                                        SET featured = :featured 
                                        WHERE item_id = :item_id ');
        
        $result = $stmt->execute(
                            array('item_id' => $itm->item_id, 
                                    'featured' => $itm->featured) );
        
        return $result ? true : false;
    }


    public function insertItem($itm) 
    {

        $stmt = $this->pdo->prepare('INSERT INTO tbl_itemfinder_items( 
                                        item_name,
                                        item_desc,
                                        item_price,
                                        user_id,
                                        category_id,
                                        created_at,
                                        updated_at,
                                        featured,
                                        is_deleted,
                                        item_status,
                                        item_type,
                                        lat,
                                        lon,
                                        item_currency 
                                        ) 

                                    VALUES(
                                        :item_name,
                                        :item_desc,
                                        :item_price,
                                        :user_id,
                                        :category_id,
                                        :created_at,
                                        :updated_at,
                                        :featured,
                                        :is_deleted,
                                        :item_status,
                                        :item_type,
                                        :lat,
                                        :lon,
                                        :item_currency  )');
        
        $result = $stmt->execute(
                                    array(
                                        'item_name' => $itm->item_name,
                                        'item_desc' => $itm->item_desc,  
                                        'item_price' => $itm->item_price,
                                        'user_id' => $itm->user_id,
                                        'category_id' => $itm->category_id,
                                        'created_at' => $itm->created_at,
                                        'updated_at' => $itm->updated_at,
                                        'featured' => $itm->featured,
                                        'is_deleted' => $itm->is_deleted,
                                        'item_status' => $itm->item_status,
                                        'item_type' => $itm->item_type,
                                        'lat' => $itm->lat,
                                        'lon' => $itm->lon, 
                                        'item_currency' => $itm->item_currency   
                                    ) 
                                );
        
        return $result ? true : false;
    }
 

    public function getItems() {
        $stmt = $this->pdo->prepare('SELECT * FROM tbl_itemfinder_items WHERE is_deleted = 0 ORDER BY updated_at DESC');
        $stmt->execute();

        $array = array();
        $ind = 0;
        foreach ($stmt as $row) {
            $itm = $this->formatItem($row);
            $array[$ind] = $itm;
            $ind++;
        } 
        
        return $array;
    }

    public function getItemsBySearching($search) {
        $stmt = $this->pdo->prepare('SELECT * FROM tbl_itemfinder_items WHERE is_deleted = 0 AND item_name LIKE :search ORDER BY item_name ASC');
        $stmt->execute( array('search' => '%'.$search.'%'));

        $array = array();
        $ind = 0;
        foreach ($stmt as $row) {
            $itm = $this->formatItem($row);
            $array[$ind] = $itm;
            $ind++;
        } 
        
        return $array;
    }


    public function getItemByItemId($item_id) {
        $stmt = $this->pdo->prepare('SELECT * FROM tbl_itemfinder_items WHERE item_id = :item_id');
        $stmt->execute( array('item_id' => $item_id));

        foreach ($stmt as $row) {
            $itm = $this->formatItem($row);
            return $itm;
        } 
        return null;
    }


    public function getItemFeatured() {
        $stmt = $this->pdo->prepare('SELECT * FROM tbl_itemfinder_items WHERE featured = 1 AND is_deleted = 0 ORDER BY item_name ASC');
        $stmt->execute();

        $array = array();
        $ind = 0;
        foreach ($stmt as $row) {
            $itm = $this->formatItem($row);
            $array[$ind] = $itm;
            $ind++;
        } 
        return $array;
    }

    public function getLastInsertedId() {

        return $this->pdo->lastInsertId(); 
    }

    public function getItemsAtRange($begin, $end) {
        $stmt = $this->pdo->prepare('SELECT * 
                                        FROM tbl_itemfinder_items 
                                        WHERE is_deleted = 0 ORDER BY item_id ASC LIMIT :beg, :end');
        
        $stmt->execute( array('beg' => $begin, 'end' => $end) );

        $array = array();
        $ind = 0;
        foreach ($stmt as $row) {
            $itm = $this->formatItem($row);
            $array[$ind] = $itm;
            $ind++;
        } 
        
        return $array;
    }

    public function formatItem($row) {
        $itm = new Item();
        $itm->item_id = $row['item_id'];
        $itm->item_name = $row['item_name'];
        $itm->item_desc = $row['item_desc'];
        $itm->user_id = $row['user_id'];
        $itm->category_id = $row['category_id'];
        $itm->created_at = $row['created_at'];
        $itm->updated_at = $row['updated_at'];
        $itm->featured = $row['featured'];
        $itm->is_deleted = $row['is_deleted'];
        $itm->item_status = $row['item_status'];
        $itm->item_type = $row['item_type'];
        $itm->item_price = $row['item_price'];
        $itm->lat = $row['lat'];
        $itm->lon = $row['lon'];
        $itm->is_published = $row['is_published'];
        $itm->item_currency = $row['item_currency'];
        return $itm;
    }

    public function getResultItemByItemId($item_id) {
        $stmt = $this->pdo->prepare('SELECT * FROM tbl_itemfinder_items WHERE item_id = :item_id');
        $stmt->execute( array('item_id' => $item_id));
        return $stmt;
    }

}
 
?>