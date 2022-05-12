<?php

  require '../header_rest.php';
  $controllerRest = new ControllerRest();
  $controllerItem = new ControllerItem();
  $controllerUser = new ControllerUser();
  $jsonFormatter = new JSONFormatter();

  $item_name = "";
  if( !empty($_POST['item_name']) )
      $item_name = $_POST['item_name'];

  $item_price = "";
  if( !empty($_POST['item_price']) )
      $item_price = $_POST['item_price'];

    $item_currency = "";
  if( !empty($_POST['item_currency']) )
      $item_currency = $_POST['item_currency'];

  $item_desc = "";
  if( !empty($_POST['item_desc']) )
      $item_desc = $_POST['item_desc'];

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
          // $item_descReplace = preg_replace('~[\r\n]+~', '\n', $item_desc);
          // $itm->item_desc = htmlspecialchars(trim(strip_tags($item_desc)));
          $itm->item_desc = $item_desc;
          $itm->item_price = $item_price;
          $itm->user_id = $user_id;
          $itm->item_status = $item_status;
          $itm->item_type = $item_type;
          $itm->category_id = $category_id;
          $itm->lat = $lat;
          $itm->lon = $lon;
          $itm->item_currency = $item_currency;
          $itm->updated_at = time();

          $controllerItem->updateItemNoFeatured($itm);
          $item = $controllerItem->getItemByItemId($item_id);
          $itemJson = $jsonFormatter->formatItemModel($item);

          $photoResult = $controllerRest->getResultPhotoItemsWithParams($item->item_id);
          $no_of_rows_photo = $photoResult->rowCount();
          $strPhotos = $jsonFormatter->getPhotos($photoResult);

          $resultUsers = $controllerRest->getResultUserWithParams($item->user_id);
          $no_of_rows_user = $resultUsers->rowCount();
          $strUser = $jsonFormatter->getUser($resultUsers);
          $json = "{
                    \"status\" : {
                                  \"status_code\" : \"-1\",
                                  \"status_text\" : \"Success.\"
                                },
                    \"item_details\" : {
                                        \"item_info\" : $itemJson,
                                        \"photos\" : [$strPhotos],
                                        \"user\" : $strUser
                                    }
                }";
          echo $json;
      }
      else {
          $itm = new Item();
          $itm->item_name = $item_name;
          // $itm->item_desc = htmlspecialchars(trim(strip_tags($item_desc)));
          $itm->item_desc = $item_desc;
          $itm->item_price = $item_price;
          $itm->user_id = $user_id;
          $itm->category_id = $category_id;
          $itm->created_at = time();
          $itm->updated_at = time();
          $itm->featured = 0;
          $itm->is_deleted = 0;
          $itm->item_status = $item_status;
          $itm->item_type = $item_type;
          $itm->item_currency = $item_currency;
          $itm->lat = $lat;
          $itm->lon = $lon;
          $controllerItem->insertItem($itm);
          $item = $controllerItem->getItemByItemId($controllerItem->getLastInsertedId());
          $itemJson = $jsonFormatter->formatItemModel($item);

          $photoResult = $controllerRest->getResultPhotoItemsWithParams($item->item_id);
          $no_of_rows_photo = $photoResult->rowCount();
          $strPhotos = $jsonFormatter->getPhotos($photoResult);

          $resultUsers = $controllerRest->getResultUserWithParams($item->user_id);
          $no_of_rows_user = $resultUsers->rowCount();
          $strUser = $jsonFormatter->getUser($resultUsers);

          $json = "{
                    \"status\" : {
                                  \"status_code\" : \"-1\",
                                  \"status_text\" : \"Success.\"
                                },
                    \"item_details\" : {
                                        \"item_info\" : $itemJson,
                                        \"photos\" : [$strPhotos],
                                        \"user\" : $strUser
                                    }
                }";
          echo $json;
      }
  }

?>