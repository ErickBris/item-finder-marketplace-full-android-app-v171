<?php

    require '../header_rest.php';
    $controllerRest = new ControllerRest();
    $jsonFormatter = new JSONFormatter();
    
    $min_count = 0;
    if(!empty($_GET['min_count']))
        $min_count = $_GET['min_count'];

    $max_count = 0;
    if(!empty($_GET['max_count']))
        $max_count = $_GET['max_count'];

    $user_id1 = 0;
    if(!empty($_GET['user_id']))
        $user_id1 = $_GET['user_id'];

    $api_key = "";
    if(!empty($_GET['api_key']))
        $api_key = $_GET['api_key'];

    if(Constants::API_KEY == $api_key) {
        $max_count = 100000;
        $resultItems = $controllerRest->getResultItemsWithParams($min_count, $max_count);
        $no_of_rows = $resultItems->rowCount();
        $ind = 0;

        $resultItems = $controllerRest->getResultItemsWithParams($min_count, $max_count);
        $arrayJSON = array();
        $arrayJSON['min_count'] = ''.$min_count.'';
        $arrayJSON['max_count'] = ''.$max_count.'';
        $arrayJSON['result_count'] = ''.$no_of_rows.'';

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
        $arrayJSON['items'] = $arrayItems;


        // user items
        $ind = 0;
        $arrayUserItems = array();
        $resultItems = $controllerRest->getResultItemsByUserId($user_id1);
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

            $arrayUserItems[$ind] = $arrayItem;
            $ind += 1;
        }
        $arrayJSON['user_items'] = $arrayUserItems;

        echo json_encode($arrayJSON);
    }
    else {
        $arrayJSON = array('status' => array('status_code' => '3', 'status_text' => 'Invalid Access.') );
        echo json_encode($arrayJSON);
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

?>