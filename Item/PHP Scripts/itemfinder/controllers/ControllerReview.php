<?php
 
class ControllerReview
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
 
    public function insertReview($itm) 
    {
        $stmt = $this->pdo->prepare('INSERT INTO tbl_itemfinder_reviews( 
                                        review, 
                                        parent_user_id, 
                                        user_id, 
                                        created_at, 
                                        updated_at ) 

                                    VALUES(
                                        :review, 
                                        :parent_user_id, 
                                        :user_id, 
                                        :created_at, 
                                        :updated_at)');

        $result = $stmt->execute(
                            array('review' => $itm->review,
                                    'parent_user_id' => $itm->parent_user_id,
                                    'user_id' => $itm->user_id,
                                    'created_at' => $itm->created_at,
                                    'updated_at' => $itm->updated_at) );
        
        return $result ? true : false;

    }
 
    public function getReviewsByUserId($parent_user_id) 
    {
        $stmt = $this->pdo->prepare('SELECT * 
                                        FROM tbl_itemfinder_reviews 
                                        WHERE is_deleted = 0 AND parent_user_id = :parent_user_id ORDER BY review_id DESC');
        
        $result = $stmt->execute(
                            array('parent_user_id' => $parent_user_id) );

        $array = array();
        $ind = 0;
        foreach ($stmt as $row) 
        {
            $itm = new Review();
            $itm->review_id = $row['review_id'];
            $itm->review = $row['review'];
            $itm->parent_user_id = $row['parent_user_id'];
            $itm->user_id = $row['user_id'];
            $itm->created_at = $row['created_at'];
            $itm->updated_at = $row['updated_at'];
            $itm->is_deleted = $row['is_deleted'];


            $array[$ind] = $itm;
            $ind++;
        } 
        
        return $array;
    }

    public function deleteReview($review_id, $is_deleted) 
    {
        $stmt = $this->pdo->prepare('UPDATE tbl_itemfinder_reviews 
                                        SET is_deleted = :is_deleted 
                                        WHERE review_id = :review_id ');
        
        $result = $stmt->execute(
                            array('review_id' => $review_id, 
                                    'is_deleted' => $is_deleted) );

        return $result ? true : false;
    }

}
 
?>