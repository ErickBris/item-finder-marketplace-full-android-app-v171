<?php

  require '../header_rest.php';
  $controllerRest = new ControllerRest();

  $api_key = "";
    if(!empty($_GET['api_key']))
        $api_key = $_GET['api_key'];

  $resultNews = $controllerRest->getResultNews();

  header ("content-type: text/json");
  // header("Content-Type: application/text; charset=ISO-8859-1");


  if( Constants::API_KEY == $api_key ) {
      echo "{";

                // NEWS
                echo "\"news\" : [";
                $no_of_rows = $resultNews->rowCount();
                $ind = 0;
                $count = $resultNews->columnCount();
                foreach ($resultNews as $row) 
                {
                    echo "{";
                    $inner_ind = 0;
                    foreach ($row as $columnName => $field) 
                    {

                        $val = trim(strip_tags($field));
                        if(!is_numeric($columnName)) {
                            echo "\"$columnName\" : \"$val\"";

                            if($inner_ind < $count - 1)
                              echo ",";

                            ++$inner_ind;
                        }
                    }

                    if($count > $inner_ind) {
                        echo "\"slug\" : \"slug\"";
                    }

                    echo "}";

                    if($ind < $no_of_rows - 1)
                      echo ",";

                    ++$ind;
                }
                echo "]";

      echo "}";
  }
  
  else 
  {
    echo "{
                  \"status\" : {
                                \"status_code\" : \"3\",
                                \"status_text\" : \"Invalid Access.\"
                              }
                  }";
  }
?>