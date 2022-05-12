<?php 

  require_once 'header.php';
  $controller = new ControllerItem();
  $controllerCategory = new ControllerCategory();
  $controllerUser = new ControllerUser();

  $categories = $controllerCategory->getCategoriesLeafNode();
  $users = $controllerUser->getUsers();
  
  $extras = new Extras();
  if( isset($_POST['submit']) ) {
    
    $itm = new Item();
    $itm->item_name = htmlspecialchars(trim(strip_tags($_POST['item_name'])), ENT_QUOTES);
    $itm->item_price = htmlspecialchars(trim(strip_tags($_POST['item_price'])), ENT_QUOTES);
    $itm->item_currency = htmlspecialchars(trim(strip_tags($_POST['item_currency'])), ENT_QUOTES);

    // $item_desc = preg_replace('~[\r\n]+~', '', $_POST['item_desc']);
    // $itm->item_desc = htmlspecialchars(trim(strip_tags($item_desc)));

    $item_desc = htmlspecialchars(trim(strip_tags($_POST['item_desc'])));
    $item_desc = preg_replace( "/\r\n/", "&#10;", $item_desc );
    $itm->item_desc = str_replace('"', '&quot;', $item_desc);
    
    $itm->item_status = $_POST['item_status'];
    
    $itm->item_type = $_POST['item_type'];
    $itm->created_at = time();
    $itm->category_id = trim(strip_tags($_POST['category_id']));
    $itm->updated_at = time();
    $itm->featured = $_POST['featured'];
    $itm->user_id = $_POST['user_id'];
    $itm->is_deleted = 0;
    $itm->lon = $_POST['lon'];
    $itm->lat = $_POST['lat'];

    $controller->insertItem($itm);
    echo "<script type='text/javascript'>location.href='items.php';</script>";

  }

?>


<!DOCTYPE html>
<html lang="en"><head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="http://getbootstrap.com/assets/ico/favicon.ico">

    <title>Item Finder</title>

    <!-- Bootstrap core CSS -->
    <link href="bootstrap/css/bootstrap.css" rel="stylesheet">
    <!-- Custom styles for this template -->
    <link href="bootstrap/css/navbar-fixed-top.css" rel="stylesheet">
    <link href="bootstrap/css/custom.css" rel="stylesheet">

    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBB7Tce0Xd3GEb838FF5uRcIe8MQIRdQSo&sensor=false"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
    <script type="text/javascript">
        $(function(){
            var mapDiv = document.getElementById('map');
                var map = new google.maps.Map(mapDiv, {
                  center: new google.maps.LatLng(42.766727, -72.995293),
                  zoom: 10,
                  mapTypeId: google.maps.MapTypeId.ROADMAP,

                });

            var marker;
            google.maps.event.addListener(map, 'click', function (mouseEvent) {

                if(marker != null)
                  marker.setMap(null);

                var lat = document.getElementById('latitude');
                var longi = document.getElementById('longitude');
                lat.value = mouseEvent.latLng.lat(); //alert(mouseEvent.latLng.toUrlValue());
                longi.value = mouseEvent.latLng.lng();

                marker = new google.maps.Marker({
                    position: mouseEvent.latLng,
                    map: map,
                    title: 'Here!'
                });

            });

        });

        function validateLatLng(evt) {
            var theEvent = evt || window.event;
            var key = theEvent.keyCode || theEvent.which;
            key = String.fromCharCode( key );

            if(theEvent.keyCode == 8 || theEvent.keyCode == 127) {
                
            }
            else {
                var regex = /[0-9.]|\./;
                if( !regex.test(key) ) {
                  theEvent.returnValue = false;
                  if(theEvent.preventDefault) theEvent.preventDefault();
                }  
            }
        }
    </script>

    <!-- Just for debugging purposes. Don't actually copy this line! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>

  <body>

    <!-- Fixed navbar -->
    <div class="navbar navbar-default navbar-fixed-top" role="navigation">
      <div class="container">


        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Item Finder</a>
        </div>


        <div class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li ><a href="home.php">Home</a></li>
            <li ><a href="categories.php">Categories</a></li>
            <li class="active"><a href="items.php">Items</a></li>
            <li ><a href="news.php">News</a></li>
            <li ><a href="admin_access.php">Admin Access</a></li>
            <li ><a href="users.php">Users</a></li>
          </ul>
          
          <ul class="nav navbar-nav navbar-right">
            <li ><a href="index.php">Logout</a></li>
          </ul>
        </div><!--/.nav-collapse -->
        
      </div>
    </div>

    <div class="container">

      <!-- Example row of columns -->
      <div class="panel panel-default">
        <div class="panel-heading">
          <h3 class="panel-title">Add Item</h3>
        </div>

        <div class="panel-body">
              <div class="row">
                <div class="col-md-7">

                  <form action="" method="POST">

                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Item Name" name="item_name" required>
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Item Currency" name="item_currency" required maxlength="3">
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Item Price" name="item_price" required>
                      </div>
  
                      <br />
                      <div class="input-group" style="width:100%;" >
                        <select class="form-control" style="width:100%;" name="featured">
                          <option value="-1">Select if Item will be featured</option>
                          <option value="1">Item Featured</option>
                          <option value="0">Item Not Featured</option>
                        </select>
                      </div>

                      <br />
                      <div class="input-group" style="width:100%;" >
                        <select class="form-control" style="width:100%;" name="item_status">
                          <option value="1">Sold Item</option>
                          <option value="0">Not Sold Item</option>
                        </select>
                      </div>

                      <br />
                      <div class="input-group" style="width:100%;" >
                        <select class="form-control" style="width:100%;" name="item_type">
                          <option value="1">Used Item</option>
                          <option value="0">New item</option>
                        </select>
                      </div>

                      
                      <br />
                      <div class="input-group" style="width:100%;">
                        <select class="form-control" style="width:100%;" name="category_id">
                          <option value="None">No Category (Does not appear in listing)</option>
                          <?php
                              if($categories != null) {
                                  foreach ($categories as $category)  {
                                        echo "<option value='$category->category_id'>$category->category</option>";
                                  }
                              }
                          ?>
                          
                        </select>
                      </div>

                      <br />
                      <div class="input-group" style="width:100%;">
                        <select class="form-control" style="width:100%;" name="user_id">
                          <?php
                              if($users != null) {
                                  foreach ($users as $user)  {
                                        echo "<option value='$user->user_id'>$user->user_id - $user->full_name</option>";
                                  }
                              }
                          ?>
                          
                        </select>
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Click on the Map for Latitude" name="lat" onkeypress='validateLatLng(event)' id="latitude" required readonly>
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <input type="text" class="form-control" placeholder="Click on the Map for Longitude" name="lon" onkeypress='validateLatLng(event)' id="longitude" required readonly>
                      </div>

                      <br />
                      <div class="input-group">
                        <span class="input-group-addon"></span>
                        <textarea type="text" class="form-control" placeholder="Description" rows="10" name="item_desc" id="details"></textarea>
                      </div>

                      <br /> 
                      <p>
                          <button type="submit" name="submit" class="btn btn-info" onclick="checkInput()" role="button">Save</button> 
                          <a class="btn btn-info" href="items.php" role="button">Cancel</a>
                      </p>
                  </form> 
                  


              </div>
              <div class="col-md-5">
                  <h4>Click the Map to get latitude/longitude:</h4>
                  <div id="map" style="width:100%; height:400px"></div>
               </div>
        </div>
      </div>


    </div> <!-- /container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="bootstrap/js/jquery.js"></script>
    <script src="bootstrap/js/bootstrap.js"></script>
    <script>
        function checkInput() {
            var website = document.getElementById("website");
            var details = document.getElementById("details");


            var website = document.getElementById("website");
            var details = document.getElementById("details");

            var strWebsite = website.value.replace("http://", "");
            strFb = strWebsite.replace("https://", "");
            website.value = strWebsite;

            var strDetails = details.value.replace("http://", "");
            strFb = strDetails.replace("https://", "");
            details.value = strDetails;
        }
    </script>
    
  

</body></html>