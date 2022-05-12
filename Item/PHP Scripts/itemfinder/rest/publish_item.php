<?php

  require '../header_rest.php';
  $controllerRest = new ControllerRest();
  $controllerItem = new ControllerItem();
  $controllerUser = new ControllerUser();
  $jsonFormatter = new JSONFormatter();

  $item_id = 0;
  if( !empty($_POST['item_id']) )
      $item_id = $_POST['item_id'];

  $is_published = 0;
  if( !empty($_POST['is_published']) )
      $is_published = $_POST['is_published'];

  $user_id ="";
  if( !empty($_POST['user_id']) )
      $user_id = $_POST['user_id'];

  $login_hash ="";
  if( !empty($_POST['login_hash']) )
      $login_hash = $_POST['login_hash'];

  $api_key = "";
  if(!empty($_POST['api_key']))
      $api_key = $_POST['api_key'];

  if(!$controllerUser->isUserIdExistAndHash($user_id, $login_hash) || Constants::API_KEY != $api_key) {
      $json = "{\"status\":{\"status_code\":\"3\",\"status_text\":\"Invalid Access\"}}";
      echo $json;
  }
  else {
      $controllerItem->publishItem($item_id, $is_published);
      $item = $controllerItem->getItemByItemId($item_id);
      $itemJson = $jsonFormatter->formatItemModelAndroid($item);

      $photoResult = $controllerRest->getResultPhotoItemsWithParams($item->item_id);
      $strPhotos = $jsonFormatter->getPhotosAndroid($photoResult);

      $resultUsers = $controllerRest->getResultUserWithParams($item->user_id);
      $strUser = $jsonFormatter->getUserAndroid($resultUsers);

      $json = "{\"status\":{\"status_code\":\"-1\",\"status_text\":\"Success.\"},\"item_details\":{\"item_info\":$itemJson,\"photos\":$strPhotos,\"user\":$strUser}}";
      echo $json;
  
  }


?>