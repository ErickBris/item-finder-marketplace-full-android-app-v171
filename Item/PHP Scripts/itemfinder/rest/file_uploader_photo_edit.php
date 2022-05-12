<?php
    require_once '../header_rest.php';
    $controllerPhoto = new ControllerPhoto();
    $controllerUser = new ControllerUser();
    $controllerItem = new ControllerItem();
    $controllerRest = new ControllerRest();
    $jsonFormatter = new JSONFormatter();

    $user_id = 0;
    if( !empty($_POST['user_id']) )
        $user_id = $_POST['user_id'];

    $login_hash = "";
    if( !empty($_POST['login_hash']) )
        $login_hash = $_POST['login_hash'];

    $api_key = "";
    if(!empty($_POST['api_key']))
        $api_key = $_POST['api_key'];

    $item_id = "";
    if(!empty($_POST['item_id']))
        $item_id = $_POST['item_id'];

    $photo_id = "";
    if(!empty($_POST['photo_id']))
        $photo_id = $_POST['photo_id'];

    $is_deleted = "";
    if(!empty($_POST['is_deleted']))
        $is_deleted = $_POST['is_deleted'];

    if( ( !empty($login_hash) && !empty($user_id) ) && Constants::API_KEY == $api_key ) {
        $user = $controllerUser->getUserByUserId($user_id);
        $login_hash = str_replace(" ", "+", $login_hash);
        if($user != null) {
            if($user->login_hash == $login_hash) {
                $isUploading = empty($_FILES["file"]["name"]);
                if( !$isUploading ) {
                    $count = count($_FILES["file"]["name"]);
                    $desired_dir = Constants::IMAGE_UPLOAD_DIR;
                    $errors = array();
                    for($key = 0; $key < $count; $key++){

                        $file_name = $_FILES['file']['name'][$key];
                        $file_size = $_FILES['file']['size'][$key];
                        $file_tmp = $_FILES['file']['tmp_name'][$key];
                        $file_type= $_FILES['file']['type'][$key];

                        // if($file_size > 2097152){
                        //     $errors[]='File size must be less than 2 MB';
                        // }    

                        $timestamp =  uniqid();
                        $temp = explode(".", $_FILES["file"]["name"][$key]);
                        $extension = end($temp);

                        $new_file_name = $desired_dir."/"."photo_".$timestamp.".".$extension;
                        $new_file_name1 = "../".$desired_dir."/"."photo_".$timestamp.".".$extension;
                        if(empty($errors)==true){
                          if(is_dir("../".$desired_dir)==false){
                              // Create directory if it does not exist
                              mkdir("$desired_dir", 0700);        
                          }
                          if(is_dir($file_name)==false){
                              // rename the file if another one exist
                              move_uploaded_file($file_tmp, $new_file_name1);
                          }else{                                  
                              $new_dir = $new_file_name1.time();
                              rename($file_tmp, $new_dir) ;               
                          }

                          $itm = new Photo();
                          $itm->photo_url = Constants::ROOT_URL.$new_file_name;
                          $itm->thumb_url = Constants::ROOT_URL.$new_file_name;
                          $itm->item_id = $item_id;
                          $itm->created_at = time();
                          $itm->updated_at = time();
                          $itm->is_deleted = 0;
                          $controllerPhoto->insertPhoto($itm);

                        }else{
                            print_r($errors);
                        }
                    }

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
                }
                else {
                    // UPDATE CODE HERE
                    $photo = $controllerPhoto->deletePhoto($photo_id, $is_deleted);

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
                }

            }
            else {
                $json = "{
                          \"status\" : {
                                        \"status_code\" : \"5\",
                                        \"status_text\" : \"It seems you are out of sync. Please relogin again.\"
                                      }
                          }";
            }
                
        }
        else {
            $json = "{
                          \"status\" : {
                                        \"status_code\" : \"5\",
                                        \"status_text\" : \"It seems you are out of sync. Please relogin again.\"
                                      }
                          }";
        }
        
    }
    else {

        $json = "{
                  \"status\" : {
                                \"status_code\" : \"3\",
                                \"status_text\" : \"Invalid Access.\"
                              }
                  }";

        
    }

    echo $json;



    

?>