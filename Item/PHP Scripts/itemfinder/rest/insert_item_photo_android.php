<?php

    require '../header_rest.php';
    $controllerPhoto = new ControllerPhoto();
    $extras = new Extras();
    
    $item_id = 0;
    $count = 0;
    $photo_id = 0;

    if( !empty($_GET['photo_id'])  )
        $photo_id = $_GET['photo_id']; 

    if( !empty($_GET['item_id'])  )
        $item_id = $_GET['item_id']; 

    if(!empty($_FILES["uploaded_file"]["name"]))
         $count = count($_FILES["uploaded_file"]["name"]);

    if($item_id > 0 && $count > 0 && $photo_id == 0) {
        $file_path = "../".Constants::IMAGE_UPLOAD_DIR."/";
        $file_name = $_FILES['uploaded_file']['name'];
        // $file_path = $file_path . basename($file_name);

        $split = explode(".", $file_name);
        $ext = end( $split );

        $new_file_name = basename(uniqid()) . "." . $ext;
        $file_path = $file_path . $new_file_name;

        $photo_path = "../".Constants::IMAGE_UPLOAD_DIR."/";
        $thumb_path = "../".Constants::IMAGE_UPLOAD_DIR."/".Constants::IMAGE_UPLOAD_DIR."/";
        if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
            $itm = new Photo();
            $itm->item_id = $item_id;
            $itm->created_at = time();
            $itm->updated_at = time();
            $itm->photo_url = Constants::ROOT_URL."".Constants::IMAGE_UPLOAD_DIR."/".$new_file_name;
            $itm->thumb_url = Constants::ROOT_URL."".Constants::IMAGE_UPLOAD_DIR."/".$new_file_name;

            $controllerPhoto->insertPhoto($itm);
            $itm = $controllerPhoto->getPhotosByItemId($item_id);
            echo json_encode(formatArrayToJSONFriendly($itm));
        } else{
            $arr = array();
            $arr['status'] = array('status_code' => '403', 
                            'status_text' => 'Invalid Access.', 'count' => $count );
            echo json_encode( $arr );
        }
    }
    else if($item_id == 0 && $count == 0 && $photo_id > 0){
        $controllerPhoto->deletePhoto($photo_id, 1);
        $array = array( );
        $array['status'] = array('status_code' => '-1', 
                                    'status_text' => 'Success');
        echo json_encode( $array );
    }
    else {
        $arr = array();
        $arr['status'] = array('status_code' => '403', 
                        'status_text' => 'Invalid Access. Outside', 'count' => $count );
        echo json_encode( $arr );
    }

    function formatArrayToJSONFriendly($itms) {

        $array = array( );
        $array['status'] = array('status_code' => '-1', 
                                    'status_text' => 'Success');
        $arrItems = array();
        $ind = 0;
        foreach($itms as $row) {
            $arr = array(
                        'photo_id' => $row->photo_id, 
                        'photo_url' => $row->photo_url,
                        'thumb_url' => $row->thumb_url,
                        'item_id' => $row->item_id,
                        'created_at' => $row->created_at,
                        'created_at' => $row->created_at,
                        'updated_at' => $row->updated_at );

            $arrItems[$ind] = $arr;
            $ind += 1;
        }
        $array['photos'] = $arrItems;
        return $array;
    }


?>