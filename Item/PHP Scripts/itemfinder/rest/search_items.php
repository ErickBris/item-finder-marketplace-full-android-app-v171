<?php

    require '../header_rest.php';
    $controllerRest = new ControllerRest();
    $jsonFormatter = new JSONFormatter();
        
    $seller_name_or_user_name = "";
    if(!empty($_POST['seller_name_or_user_name']))
        $seller_name_or_user_name = $_POST['seller_name_or_user_name'];

    $keywords = "";
    if(!empty($_POST['keywords']))
        $keywords = $_POST['keywords'];

    $api_key = "";
    if(!empty($_POST['api_key']))
        $api_key = $_POST['api_key'];

    $lat = 0;
    if(!empty($_POST['lat']))
        $lat = str_replace(",", ".", $_POST['lat']);

    $lon = 0;
    if(!empty($_POST['lon']))
        $lon = str_replace(",", ".", $_POST['lon']);

    $radius = 0;
    if(!empty($_POST['radius']))
        $radius = $_POST['radius'];

    $item_type = 0;
    if(!empty($_POST['item_type']))
        $item_type = $_POST['item_type'];

    $price_min = 0;
    if(!empty($_POST['price_min']))
        $price_min = $_POST['price_min'];

    $price_max = 0;
    if(!empty($_POST['price_max']))
        $price_max = $_POST['price_max'];

    $category_id = 0;
    if(!empty($_POST['category_id']))
        $category_id = $_POST['category_id'];

    $sort_by = 0;
    if(!empty($_POST['sort_by']))
        $sort_by = $_POST['sort_by'];

    if(Constants::API_KEY != $api_key) {
        $arrayJSON = array('status' => array('status_code' => '3', 'status_text' => 'Invalid Access.') );
        echo json_encode($arrayJSON);
        return;
    }

    $arrayJSON = array();
    $arrayJSON['status'] = array('status_code' => '-1', 'status_text' => 'Success.');

    $params = array(
        'seller_name_or_user_name' => $seller_name_or_user_name, 
        'keywords' => $keywords, 
        'lat' => $lat, 
        'lon' => $lon , 
        'radius' => $radius, 
        'category_id' => $category_id,
        'sort_by' => $sort_by,
        'item_type' => $item_type,
        'price_min' => $price_min,
        'price_max' => $price_max );

    $resultItems = $controllerRest->getResultItemsSearch($params);
    $no_of_rows = $resultItems->rowCount();
    $arrayJSON['result_count'] = ''.$no_of_rows.'';
    $arrayJSON['search_items'] = getItemsArray($resultItems);

    $max_distance = $controllerRest->getMaxDistanceFound($lat, $lon);
    $arrayJSON['max_distance'] = $max_distance;

    echo json_encode($arrayJSON);






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

    function getItemsArray($resultItems) {

        $controllerRest = new ControllerRest();
        $ind = 0;
        $arrayItems = array();
        foreach ($resultItems as $row) {
            $arrayItem = array();
            foreach ($row as $columnName => $field) {
                if(!is_numeric($columnName)) {
                    if($columnName == "created_at") {
                        $arrayItem[$columnName] = time() - intval($field);
                    }
                    else if($columnName == "lat" || $columnName == "lon") {
                        $field1 = str_replace(",", ".", $field);
                        $arrayItem[$columnName] = $field1;
                    }
                    else {
                        $arrayItem[$columnName] = $field;
                    }
                }
            }

            $item_id = $arrayItem['item_id'];
            if($item_id > 0) {
                $results = $controllerRest->getResultPhotoItemsWithParams($item_id);
                $arrayItem['photos'] = getPhotos($results);
            }

            $user_id = $arrayItem['user_id'];
            if($user_id > 0) {
                $results = $controllerRest->getResultUserWithParams($user_id);
                $arrayItem['user'] = getUser($results);
            }

            $arrayItems[$ind] = $arrayItem;
            $ind += 1;
        }

        return $arrayItems;
    }

?>