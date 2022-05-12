<?php
  require '../header_rest.php';
  $controllerRest = new ControllerRest();
  $controllerUser = new ControllerUser();
  $controllerReview = new ControllerReview();

  $user_id = 0;
  if(!empty($_POST['user_id']) )
    $user_id = $_POST['user_id'];

  $parent_user_id = 0;
  if(!empty($_POST['parent_user_id']) )
    $parent_user_id = $_POST['parent_user_id'];

  $login_hash = 0;
  if(!empty($_POST['login_hash']) )
    $login_hash = $_POST['login_hash'];

  $review = "";
  if(!empty($_POST['review']) ) {
      $review = $_POST['review'];
      $review = str_replace('<p dir="ltr">', "", $review);
      $review = str_replace('</p>', "", $review);
      $review = str_replace('\n', "", $review);
      $review = str_replace('\\n', "", $review);
  }

  if( !empty($user_id) && !empty($parent_user_id) && !empty($login_hash) && !empty($review) ) {

      if(!$controllerUser->isUserIdExistAndHash($user_id, $login_hash)) {
          $arrayStatus = array( "status_code" => "3", "status_text" => "Invalid Access");
          $arrayJSON['status'] = $arrayStatus;
          echo json_encode($arrayJSON);
      }
      else {

          $itm = new Review();
          $itm->review = $review;
          $itm->parent_user_id = $parent_user_id; 
          $itm->user_id = $user_id;
          $itm->created_at = time();
          $itm->updated_at = time();
          $controllerReview->insertReview($itm);

          $resultItems = $controllerRest->getResultReviewsByParentStoreId($parent_user_id);

          $ind = 0;
          $arrayJSON = array();
          $arrayObjs = array();
          foreach ($resultItems as $row) {
              $arrayObj = array();
              foreach ($row as $columnName => $field) {
                  if(!is_numeric($columnName)) {
                      $val = trim(strip_tags($field));
                      if($columnName == "review") {
                          $val = preg_replace('~[\r\n]+~', '', $val);
                          $val = htmlspecialchars(trim(strip_tags($val)));
                      }
                      $arrayObj[$columnName] = $field;
                  }
              }
              $arrayObjs[$ind] = $arrayObj;
              $ind += 1;
          }

          $arrayJSON['user_reviews'] = $arrayObjs;
          $arrayStatus = array( "status_code" => "-1", "status_text" => "Success.");
          $arrayJSON['status'] = $arrayStatus;
          echo json_encode($arrayJSON);
      }
  }
  else {
      $arrayStatus = array( "status_code" => "3", "status_text" => "Invalid Access");
      $arrayJSON['status'] = $arrayStatus;
      echo json_encode($arrayJSON);
  }
?>