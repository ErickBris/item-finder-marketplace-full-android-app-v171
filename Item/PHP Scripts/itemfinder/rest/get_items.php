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

    $lat = 0;
    if(!empty($_GET['lat']))
        $lat = str_replace(",", ".", $_GET['lat']);

    $lon = 0;
    if(!empty($_GET['lon']))
        $lon = str_replace(",", ".", $_GET['lon']);

    $radius = 0;
    if(!empty($_GET['radius']))
        $radius = $_GET['radius'];

    $category_id = 0;
    if(!empty($_GET['category_id']))
        $category_id = $_GET['category_id'];

    $latest_count = 0;
    if(!empty($_GET['latest_count']))
        $latest_count = $_GET['latest_count'];

    $home_fetch_with_radius = 0;
    if(!empty($_GET['home_fetch_with_radius']))
        $home_fetch_with_radius = $_GET['home_fetch_with_radius'];

    $featured = 0;
    if(!empty($_GET['featured']))
        $featured = $_GET['featured'];

    $get_categories = 0;
    if(!empty($_GET['get_categories']))
        $get_categories = $_GET['get_categories'];

    $default_items_count_to_find_distance = 10;
    if(!empty($_GET['default_items_count_to_find_distance']))
        $default_items_count_to_find_distance = $_GET['default_items_count_to_find_distance'];

    $user_id = 0;
    if(!empty($_GET['user_id']))
        $user_id = $_GET['user_id'];

    $max_count = 100000;

    if(Constants::API_KEY != $api_key) {
        $arrayJSON = array('status' => array('status_code' => '3', 'status_text' => 'Invalid Access.') );
        echo json_encode($arrayJSON);
        return;
    }

    $arrayJSON = array();
    $arrayJSON['min_count'] = ''.$min_count.'';
    $arrayJSON['max_count'] = ''.$max_count.'';


    if($user_id > 0 && $lat != 0 && $lon != 0  && $radius > 0) {
    	$resultItems = $controllerRest->getResultItemsUserRadius($lat, $lon, $user_id, $radius);
        $no_of_rows = $resultItems->rowCount();
        $arrayJSON['result_count'] = ''.$no_of_rows.'';
        $arrayJSON['user_items'] = getItemsArray($resultItems);
    }
    else if($user_id > 0 && $lat != 0 && $lon != 0 && $radius == 0) {
    	$resultItems = $controllerRest->getResultItemsUser($lat, $lon, $user_id);
        $no_of_rows = $resultItems->rowCount();
        $arrayJSON['result_count'] = ''.$no_of_rows.'';
        $arrayJSON['user_items'] = getItemsArray($resultItems);
    }
    else if($category_id > 0 && $lat != 0 && $lon != 0 && $radius != 0) {
        $resultItems = $controllerRest->getResultItemsNearbyByCategory($lat, $lon, $radius, $category_id);
        $no_of_rows = $resultItems->rowCount();
        $arrayJSON['result_count'] = ''.$no_of_rows.'';
        $arrayJSON['category_items'] = getItemsArray($resultItems);
    }

    else if($user_id1 > 0 && $lat != 0 && $lon != 0 && $radius != 0) {
        $resultItems = $controllerRest->getResultItemsByUserId($user_id1);
        $no_of_rows = $resultItems->rowCount();
        $arrayJSON['result_count'] = ''.$no_of_rows.'';
        $arrayJSON['user_items'] = getItemsArray($resultItems);
    }

    else if($featured == 1 && $lat != 0 && $lon != 0 && $radius != 0) {
        $resultItems = $controllerRest->getResultItemsNearbyFeatured($lat, $lon, $radius);
        $no_of_rows = $resultItems->rowCount();
        $arrayJSON['result_count'] = ''.$no_of_rows.'';
        $arrayJSON['featured_items'] = getItemsArray($resultItems);
    }

    else if($lat != 0 && $lon != 0 && $radius == 0 && $latest_count > 0) {
        $resultItems = $controllerRest->getResultItemsNearbyIgnoreRadius($lat, $lon, $latest_count);
        $no_of_rows = $resultItems->rowCount();
        $arrayJSON['result_count'] = ''.$no_of_rows.'';
        $arrayJSON['home_items'] = getItemsArray($resultItems);
    }
    else if($lat != 0 && $lon != 0 && $radius != 0 && $home_fetch_with_radius == 1) {
        $resultItems = $controllerRest->getResultItemsNearby($lat, $lon, $radius);
        $no_of_rows = $resultItems->rowCount();
        $arrayJSON['result_count'] = ''.$no_of_rows.'';
        $arrayJSON['home_items'] = getItemsArray($resultItems);
    }
    else if($lat != 0 && $lon != 0 && $radius != 0) {
        $resultItems = $controllerRest->getResultItemsNearby($lat, $lon, $radius);
        $no_of_rows = $resultItems->rowCount();
        $arrayJSON['result_count'] = ''.$no_of_rows.'';
        $arrayJSON['items'] = getItemsArray($resultItems);
    }
    else {
        $resultItems = $controllerRest->getResultItemsWithParams($min_count, $max_count);
        $no_of_rows = $resultItems->rowCount();
        $arrayJSON['result_count'] = ''.$no_of_rows.'';
        $arrayJSON['items'] = getItemsArray($resultItems);
    }


    if($get_categories == 1) {
        $results = $controllerRest->getResultCategories();
        $arrayJSON['categories'] = getPhotos($results);
    }

    $max_distance = $controllerRest->getMaxDistanceFound($lat, $lon);
    $default_distance = $controllerRest->getMaxDistanceFoundDefaultItem($lat, $lon, $default_items_count_to_find_distance);
    $arrayJSON['max_distance'] = $max_distance;
    $arrayJSON['default_distance'] = $default_distance;

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