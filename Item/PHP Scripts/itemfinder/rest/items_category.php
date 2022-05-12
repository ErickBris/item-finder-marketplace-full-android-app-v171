<?php

    require '../header_rest.php';
    $controllerRest = new ControllerRest();
    $jsonFormatter = new JSONFormatter();

    $category_id = 0;
    if(!empty($_GET['category_id']))
        $category_id = $_GET['category_id'];

    $api_key = "";
    if(!empty($_GET['api_key']))
        $api_key = $_GET['api_key'];

    header ("content-type: text/json");
    header("Content-Type: application/text; charset=ISO-8859-1");
    if(Constants::API_KEY == $api_key) {
        $resultItems = $controllerRest->getResultItemsWithCategoryId($category_id);

        $no_of_rows = $resultItems->rowCount();
        $ind = 0;
        $count = $resultItems->columnCount();      

        
        echo "{ \"result_count\" : \"$no_of_rows\", ";

                // ITEMS
                echo "\"items\" : [";
                
                foreach ($resultItems as $row) 
                {
                    echo "{";
                    $inner_ind = 0;

                    $item_id = 0;
                    $user_id = 0;
                    foreach ($row as $columnName => $field) 
                    {
                        $val = trim(strip_tags($field));
                        if($columnName == "item_desc") {
                            $val = preg_replace('~[\r\n]+~', '', $val);
                            $val = htmlspecialchars(trim(strip_tags($val)));
                        }

                        if(!is_numeric($columnName)) {
                            echo "\"$columnName\" : \"$val\"";

                            if($inner_ind < $count - 1)
                              echo ",";

                            ++$inner_ind;

                            if($columnName == "item_id") {
                                $item_id = $val;
                            }

                            if($columnName == "user_id") {
                                $user_id = $val;
                            }
                        }
                    }

                    if($item_id > 0) {
                        echo ",";

                        $str = "";

                        $photoResult = $controllerRest->getResultPhotoItemsWithParams($item_id);
                        $no_of_rows_photo = $photoResult->rowCount();

                        if($no_of_rows_photo > 0)
                            $str = $jsonFormatter->getPhotos($photoResult);
                        
                        echo "\"photos\" : [$str]";


                        
                    }

                    if($user_id > 0) {

                        $resultUsers = $controllerRest->getResultUserWithParams($user_id);
                        $no_of_rows_user = $resultUsers->rowCount();

                        echo ",";
                        $str = "";
                        if($no_of_rows_user > 0)
                            $str = $jsonFormatter->getUser($resultUsers);

                        echo "\"user\" : $str ";
                    }

                    // if($count > $inner_ind) {
                    //     echo "\"slug\" : \"slug\"";
                    // }

                    echo "}";

                    if($ind < $no_of_rows - 1)
                      echo ",";

                    ++$ind;
                }
                echo "] ";


        echo "}";
    }
    else {

        $json = "{
                \"status\" : {
                                \"status_code\" : \"3\",
                                \"status_text\" : \"Invalid Access.\"
                            }
                }";

        echo $json;
    }

?>