<?php

  require '../header_rest.php';
  $controllerRest = new ControllerRest();
  $controllerItem = new ControllerItem();
  $controllerUser = new ControllerUser();
  $controllerCategory = new ControllerCategory();
  $jsonFormatter = new JSONFormatter();

  $item_name = "";
  if( !empty($_POST['item_name']) ) {
      $item_name = $_POST['item_name'];
      $item_name = str_replace('<p dir="ltr">', "", $item_name);
      $item_name = str_replace('</p>', "", $item_name);
      $item_name = str_replace('\n', "", $item_name);
      $item_name = str_replace('\\n', "", $item_name);
      $item_name = preg_replace( "/\r\n/", "", $item_name );
      $item_name = trim(preg_replace('/\s+/', ' ', $item_name));
  }

  $item_price = "";
  if( !empty($_POST['item_price']) )
      $item_price = $_POST['item_price'];

  $item_desc = "";
  if( !empty($_POST['item_desc']) ) {
      $item_desc = $_POST['item_desc'];
      $item_desc = str_replace('<p dir="ltr">', "", $item_desc);
      $item_desc = str_replace('</p>', "", $item_desc);
      $item_desc = str_replace('\n', "", $item_desc);
      $item_desc = str_replace('\\n', "", $item_desc);
      $item_desc = preg_replace( "/\r\n/", "", $item_desc );
      $item_desc = trim(preg_replace('/\s+/', ' ', $item_desc));
  }

  $item_currency = "";
  if( !empty($_POST['item_currency']) ) {
      $item_currency = $_POST['item_currency'];
      $item_currency = str_replace('<p dir="ltr">', "", $item_currency);
      $item_currency = str_replace('</p>', "", $item_currency);
      $item_currency = str_replace('\n', "", $item_currency);
      $item_currency = str_replace('\\n', "", $item_currency);
      $item_currency = preg_replace( "/\r\n/", "", $item_currency );
      $item_currency = trim(preg_replace('/\s+/', ' ', $item_currency));
  }

  $category_id = 0;
  if( !empty($_POST['category_id']) )
      $category_id = $_POST['category_id'];

  $user_id ="";
  if( !empty($_POST['user_id']) )
      $user_id = $_POST['user_id'];

  $login_hash ="";
  if( !empty($_POST['login_hash']) )
      $login_hash = $_POST['login_hash'];

  $api_key = "";
  if(!empty($_POST['api_key']))
      $api_key = $_POST['api_key'];

  $item_status = 0;
  if(!empty($_POST['item_status']))
      $item_status = $_POST['item_status'];

  $item_type = 0;
  if(!empty($_POST['item_type']))
      $item_type = $_POST['item_type'];

  $item_id = 0;
  if(!empty($_POST['item_id']))
      $item_id = $_POST['item_id'];

  $lat1 = "";
  if(!empty($_POST['lat']))
      $lat1 = $_POST['lat'];

  $lon1 = "";
  if(!empty($_POST['lon']))
      $lon1 = $_POST['lon'];

  $lat = str_replace(",", ".", $lat1);
  $lon = str_replace(",", ".", $lon1);

  if(!$controllerUser->isUserIdExistAndHash($user_id, $login_hash) || Constants::API_KEY != $api_key) {
      $json = "{
              \"status\" : {
                            \"status_code\" : \"3\",
                            \"status_text\" : \"Invalid Access. Please relogin.\"
                          }
              }";

      echo $json;
  }
  else {
      if($item_id > 0) {
          $itm = $controllerItem->getItemByItemId($item_id);
          $itm->item_name = $item_name;
          // $item_descReplace = str_replace(['\r\n', '\r', '\n'], '\n', $item_desc);
          // $itm->item_desc = htmlspecialchars(trim(strip_tags($item_desc)));
          $itm->item_desc = $item_desc;
          $itm->item_price = $item_price;
          $itm->user_id = $user_id;
          $itm->item_status = $item_status;
          $itm->item_type = $item_type;
          $itm->category_id = $category_id;
          $itm->lat = $lat;
          $itm->lon = $lon;
          $itm->updated_at = time();
          $itm->item_currency = $item_currency;
          $controllerItem->updateItemNoFeatured($itm);

          // $item = $controllerItem->getItemByItemId($item_id);
          // $itemJson = $jsonFormatter->formatItemModelAndroid($item);

          // $photoResult = $controllerRest->getResultPhotoItemsWithParams($itm->item_id);
          // $no_of_rows_photo = $photoResult->rowCount();
          // $strPhotos = $jsonFormatter->getPhotosAndroid($photoResult);

          // $resultUsers = $controllerRest->getResultUserWithParams($itm->user_id);
          // $no_of_rows_user = $resultUsers->rowCount();
          // $strUser = $jsonFormatter->getUserAndroid($resultUsers);

          // $cat = $controllerRest->getResultCategoryByCategoryId($category_id);
          // $catJson = $jsonFormatter->getCategoryAndroid($cat);

          // $json = "{
          //           \"status\":{
          //                   \"status_code\":\"-1\",
          //                   \"status_text\":\"Success.\"},
          //           \"item_details\":{
          //                   \"item_info\":$itemJson,
          //                   \"photos\":$strPhotos,
          //                   \"user\":$strUser}
          //           }";

          // echo json;

          $item = $controllerItem->getResultItemByItemId($item_id);
          $photoResult = $controllerRest->getResultPhotoItemsWithParams($itm->item_id);
          $resultUsers = $controllerRest->getResultUserWithParams($itm->user_id);
          $cat = $controllerRest->getResultCategoryByCategoryId($category_id);
          $objCat = getObj($cat);
          $objItem = getObj($item);
          $objItem['category'] = $objCat['category'];
          
          $objsPhotos = getPhotos($photoResult);
          $objUser = getUser($resultUsers);

          $arrayJSON = array();
          $arrayJSON['status'] = array('status_code' => -1, 'status_text' => 'Success.' );
          $arrayJSON['item_details'] = array('item_info' => $objItem, 'photos' => $objsPhotos, 'user' => $objUser );
          echo json_encode($arrayJSON);
      }
      
      else {
          $itm = new Item();
          $itm->item_name = $item_name;
          $itm->item_desc = $item_desc;
          // $itm->item_desc = htmlspecialchars(trim(strip_tags($item_desc)));
          $itm->item_price = $item_price;
          $itm->user_id = $user_id;
          $itm->category_id = $category_id;
          $itm->created_at = time();
          $itm->updated_at = time();
          $itm->featured = 0;
          $itm->is_deleted = 0;
          $itm->item_status = $item_status;
          $itm->item_type = $item_type;
          $itm->lat = $lat;
          $itm->lon = $lon;
          $itm->item_currency = $item_currency;
          $controllerItem->insertItem($itm);

          // $item = $controllerItem->getItemByItemId($controllerItem->getLastInsertedId());
          // $itemJson = $jsonFormatter->formatItemModelAndroid($item);

          // $photoResult = $controllerRest->getResultPhotoItemsWithParams($item->item_id);
          // $no_of_rows_photo = $photoResult->rowCount();
          // $strPhotos = $jsonFormatter->getPhotosAndroid($photoResult);

          // $resultUsers = $controllerRest->getResultUserWithParams($item->user_id);
          // $no_of_rows_user = $resultUsers->rowCount();
          // $strUser = $jsonFormatter->getUserAndroid($resultUsers);

          // $cat = $controllerRest->getResultCategoryByCategoryId($category_id);
          // $catJson = $jsonFormatter->getCategoryAndroid($cat);

          // $json = "{
          //         \"status\":{
          //                 \"status_code\":\"-1\",
          //                 \"status_text\":\"Success.\"},
          //         \"item_details\":{
          //                 \"item_info\":$itemJson,
          //                 \"photos\":$strPhotos,
          //                 \"user\":$strUser}
          //         }";
          // echo $json;

          $item = $controllerItem->getResultItemByItemId($item_id);
          $photoResult = $controllerRest->getResultPhotoItemsWithParams($itm->item_id);
          $resultUsers = $controllerRest->getResultUserWithParams($itm->user_id);
          $cat = $controllerRest->getResultCategoryByCategoryId($category_id);
          $objCat = getObj($cat);
          $objItem = getObj($item);
          $objItem['category'] = $objCat['category'];
          
          $objsPhotos = getPhotos($photoResult);
          $objUser = getUser($resultUsers);

          $arrayJSON = array();
          $arrayJSON['status'] = array('status_code' => -1, 'status_text' => 'Success.' );
          $arrayJSON['item_details'] = array('item_info' => $objItem, 'photos' => $objsPhotos, 'user' => $objUser );
          echo json_encode($arrayJSON);
      }
  }

    function getUser($results) {
        $arrUser = array();
        foreach ($results as $row) {
            foreach ($row as $columnName => $field) {
                if(!is_numeric($columnName)) {
                    if($columnName == "created_at" || $columnName == "last_logged") {
                        $arrUser[$columnName] = time() - intval($field);
                    }
                    else {
                        $arrUser[$columnName] = $field;
                    }
                }
            }
            break;
        }
        return $arrUser;
    }

    function getObj($results) {
        $arrUser = array();
        foreach ($results as $row) {
            foreach ($row as $columnName => $field) {
                if(!is_numeric($columnName)) {
                    $arrUser[$columnName] = $field;
                }
            }
            break;
        }
        return $arrUser;
    }

    function getPhotos($results) {
        $ind = 0;
        $arrayPhotos = array();
        foreach ($results as $row) {
            $arrayPhoto = array();
            foreach ($row as $columnName => $field) {
                if(!is_numeric($columnName)) {
                    $arrayPhoto[$columnName] = $field;
                }
            }
            $arrayPhotos[$ind] = $arrayPhoto;
            $ind += 1;
        }
        return $arrayPhotos;
    }


?>