<?php

 
class JSONFormatter
{
 
    
    function __construct() 
    {
        // connecting to database
    }
 
    function __destruct() { }
 
    function getPhotos($resultItems) {
        $ind = 0;
        $count = $resultItems->columnCount();   
        $no_of_rows = $resultItems->rowCount();
        $str = "";
        foreach ($resultItems as $row) {
            $str .= "{";
            $inner_ind = 0;
            foreach ($row as $columnName => $field) {
                if(!is_numeric($columnName)) {
                    $str .= "\"$columnName\" : \"$field\"";
                    if($inner_ind < $count - 1)
                      $str .= ",";

                    ++$inner_ind;
                }
            }
            $str .= "}";
            if($ind < $no_of_rows - 1)
                $str .= ",";

            ++$ind;
        }
        return $str;
    }

    function getUser($resultItems) {
        $ind = 0;
        $count = $resultItems->columnCount();   
        $no_of_rows = $resultItems->rowCount();
        $str = "";
        foreach ($resultItems as $row) {
            $str .= "{";
            $inner_ind = 0;
            foreach ($row as $columnName => $field) {
                if(!is_numeric($columnName)) {
                    if($columnName == "created_at" || $columnName == "last_logged") {
                        $tmp = time() - intval($field);
                        $str .= "\"$columnName\" : \"$tmp\"";
                    }
                    else {
                        $str .= "\"$columnName\" : \"$field\"";
                    }
                    
                    if($inner_ind < $count - 1)
                      $str .= ",";

                    ++$inner_ind;
                }
            }
            $str .= "}";
            break;
        }
        return $str;
    }

    function formatItemModel($item) {

        $created_at = time() - intval($item->created_at);
        $json = "{
                    \"item_id\" : \"$item->item_id\",
                    \"item_name\" : \"$item->item_name\",
                    \"item_desc\" : \"$item->item_desc\",
                    \"item_price\" : \"$item->item_price\",
                    \"category_id\" : \"$item->category_id\",
                    \"featured\" : \"$item->featured\",
                    \"user_id\" : \"$item->user_id\",
                    \"created_at\" : \"$created_at\",
                    \"updated_at\" : \"$item->updated_at\",
                    \"is_deleted\" : \"$item->is_deleted\",
                    \"item_status\" : \"$item->item_status\",
                    \"item_type\" : \"$item->item_type\",
                    \"lat\" : \"$item->lat\",
                    \"lon\" : \"$item->lon\",
                    \"is_published\" : \"$item->is_published\", 
                    \"item_currency\" : \"$item->item_currency\"
                }";

        return $json;
    }

    function getPhotosAndroid($resultItems) {
        $ind = 0;
        $arrayPhotos = array();
        foreach ($resultItems as $row) {
            $arrayPhoto = array();
            foreach ($row as $columnName => $field) {
                if(!is_numeric($columnName)) {
                    $arrayPhoto[$columnName] = $field;
                }
            }
            $arrayPhotos[$ind] = $arrayPhoto;
            $ind += 1;
        }
        return json_encode($arrayPhotos);
    }

    function getUserAndroid($resultItems) {
        $arrUser = array();
        foreach ($resultItems as $row) {
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
        return json_encode($arrUser);
    }


    function formatItemModelAndroid($item) {
        $array = array();
        $array['item_id'] = $item->item_id;
        $array['item_name'] = $item->item_name;
        $array['item_desc'] = $item->item_desc;
        $array['item_price'] = $item->item_price;
        $array['category_id'] = $item->category_id;
        $array['featured'] = $item->featured;
        $array['user_id'] = $item->user_id;
        $array['created_at'] = time() - intval($item->created_at);
        $array['updated_at'] = $item->updated_at;
        $array['is_deleted'] = $item->is_deleted;
        $array['item_status'] = $item->item_status;
        $array['item_type'] = $item->item_type;
        $array['lat'] = $item->lat;
        $array['lon'] = $item->lon;
        $array['is_published'] = $item->is_published;
        $array['item_currency'] = $item->item_currency;
        return json_encode($array);
    }

    function getCategoryAndroid($resultCategory) {
        $arrCat = array();
        foreach ($resultCategory as $row) {
            foreach ($row as $columnName => $field) {
                if(!is_numeric($columnName)) {
                    $arrCat[$columnName] = $field;
                }
            }
            break;
        }
        return json_encode($arrCat);
    }
}
 
?>