<?php 
  require_once 'header.php';
  $controller = new ControllerItem();
  $controllerPhoto = new ControllerPhoto();
  $items = $controller->getItems();
  if(!empty($_SERVER['QUERY_STRING'])) {
      $extras = new Extras();
      $item_id = $extras->decryptQuery1(KEY_SALT, $_SERVER['QUERY_STRING']);
      $item_id_featured = $extras->decryptQuery2(KEY_SALT, $_SERVER['QUERY_STRING']);
      if( $item_id != null ) {
          $controller->deleteItem($item_id, 1);
          echo "<script type='text/javascript'>location.href='items.php';</script>";
      }
      
      if($item_id_featured != null) {
          $itm = new Item();
          $itm->item_id = $item_id_featured[0];
          $itm->featured = $item_id_featured[1] == "yes" ? 0 : 1;
          $res = $controller->updateItemFeatured($itm);
          echo "<script type='text/javascript'>location.href='items.php';</script>";
      }

      // if($item_id_featured == null && $item_id == null) {
      //   echo "<script type='text/javascript'>location.href='403.php';</script>";
      // }
  }

  $begin = 0;
  $page = 1;
  $count = count($items);
  $pages = intval($count/Constants::NO_OF_ITEMS_PER_PAGE);
  $search_criteria = "";
  if( isset($_POST['button_search']) ) {
      $search_criteria = trim(strip_tags($_POST['search']));
      $items = $controller->getItemsBySearching($search_criteria);
  }
  else {
      if($count%Constants::NO_OF_ITEMS_PER_PAGE != 0)
        $pages += 1;

      if( !empty($_GET['page']) ) {

          $page = $_GET['page'];
          $begin = ($page -1) * Constants::NO_OF_ITEMS_PER_PAGE;
          $end = Constants::NO_OF_ITEMS_PER_PAGE;
          $items = $controller->getItemsAtRange($begin, $end);
      }
      else {
          $begin = ($page -1) * Constants::NO_OF_ITEMS_PER_PAGE;
          $end = Constants::NO_OF_ITEMS_PER_PAGE;
          $items = $controller->getItemsAtRange($begin, $end);
      }
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

      <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading clearfix">
          <h4 class="panel-title pull-left" style="padding-top: 7px;">Items</h4>
          <div class="btn-group pull-right">
            <!-- <a href="car_insert.php" class="btn btn-default btn-sm">Add Car</a> -->
            <form method="POST" action="">
                  <input type="text" style="height:100%;color:#000000;padding-left:5px;" placeholder="Search" name="search" value="<?php echo $search_criteria; ?>">
                  <button type="submit" name="button_search" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-search"></span></button>
                  <button type="submit" class="btn btn-default btn-sm" name="reset"><span class="glyphicon glyphicon-refresh"></span></button>
                  <a href="item_insert.php" class="btn btn-default btn-sm"><span class='glyphicon glyphicon-plus'></span></a>
            </form>
          </div>
        </div>

        <!-- Table -->
        <table class="table">
          <thead>
              <tr>
                  <th>#</th>
                  <th>Item Name</th>
                  <th width="15%">Item Price</th>
                  <th>Item Type</th>
                  <th>No of Photos</th>
                  <th>Status</th>
                  <th>Featured</th>
                  <th>Action</th>
              </tr>

          </thead>
          <tbody>
              <?php 
                  if($items != null) {
                    $ind = $begin + 1;
                    foreach ($items as $item)  {
                          $featured = "no";
                          if($item->featured == 1)
                              $featured = "yes";

                          $no_of_photos = $controllerPhoto->getNoOfPhotosByItemId($item->item_id);
                          $extras = new Extras();
                          $updateUrl = $extras->encryptQuery1(KEY_SALT, 'item_id', $item->item_id, 'item_update.php');
                          $deleteUrl = $extras->encryptQuery1(KEY_SALT, 'item_id', $item->item_id, 'items.php');
                          $featuredUrl = $extras->encryptQuery2(KEY_SALT, 'item_id', $item->item_id, 'featured', $featured, 'items.php');
                          $viewUrl = $extras->encryptQuery1(KEY_SALT, 'item_id', $item->item_id, 'photo_item_view.php');
                          $photoUrl = $extras->encryptQuery1(KEY_SALT, 'item_id', $item->item_id, 'photo_item_insert.php');
                          $reviewUrl = $extras->encryptQuery1(KEY_SALT, 'item_id', $item->item_id, 'item_reviews_view.php');

                          echo "<tr>";
                          echo "<td>$ind</td>";
                          echo "<td>$item->item_name</td>";
                          echo "<td>$item->item_currency $item->item_price</td>";

                          $item_type = "<strong>New</strong>";
                          if($item->item_type == 1)
                            $item_type = "Used";

                          echo "<td>$item_type</td>";
                          echo "<td>$no_of_photos Photo(s)</td>";
                          
                          if($item->item_status == 1) {
                            echo "<td><strong>Sold</strong></td>";
                          }
                          else {
                            echo "<td>------</td>";
                          }

                          if($item->featured == 1) {
                            // echo "<td><a href='$featuredUrl'>Yes</a></td>";
                            echo "<td><a class='btn btn-primary btn-xs' href='$featuredUrl'>Featured</a> ";
                          }
                          else {
                            echo "<td><a class='btn btn-default btn-xs' href='$featuredUrl'>Featured</a> ";
                          }


                          echo "<td>
                                    <a class='btn btn-primary btn-xs' href='$updateUrl'><span class='glyphicon glyphicon-pencil'></span></a>
                                    <button  class='btn btn-primary btn-xs' data-toggle='modal' data-target='#modal_$item->item_id'><span class='glyphicon glyphicon-remove'></span></button>
                                    <a class='btn btn-primary btn-xs' href='$viewUrl'><span class='glyphicon glyphicon-th'></span></a>
                                    
                                    
                                </td>";
                          echo "</tr>";


                          //<!-- Modal -->
                          echo "<div class='modal fade' id='modal_$item->item_id' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' aria-hidden='true'>

                                      <div class='modal-dialog'>
                                          <div class='modal-content'>
                                              <div class='modal-header'>
                                                    <button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button>
                                                    <h4 class='modal-title' id='myModalLabel'>Deleting Item</h4>
                                              </div>
                                              <div class='modal-body'>
                                                    <p>Deleting this is not irreversible. Do you wish to continue?
                                              </div>
                                              <div class='modal-footer'>
                                                  <button type='button' class='btn btn-default' data-dismiss='modal'>Close</button>
                                                  <a type='button' class='btn btn-primary' href='$deleteUrl'>Delete</a>
                                              </div>
                                          </div>
                                      </div>
                                </div>";

                          ++$ind;
                    }
                  }

              ?>

          </tbody>
          
        </table>

       
      </div>

       <div class="btn-group pull-right">
          <?php
              if(empty($search_criteria)) {
                    if($pages != 0) {
                        if($page == 1) {
                          echo "<a class='btn btn-primary btn-xs' href='items.php?page=1'><span class='glyphicon glyphicon-chevron-left'></span></a>";
                        }
                        else {
                          $newPage = $page -1;
                          echo "<a class='btn btn-primary btn-xs' href='items.php?page=$newPage'><span class='glyphicon glyphicon-chevron-left'></span></a>";
                        }

                        echo "<a class='btn btn-primary btn-xs' href='#'>$page/$pages</a>";

                        if($page == $pages) {

                          echo "<a class='btn btn-primary btn-xs' href='items.php?page=$pages'><span class='glyphicon glyphicon-chevron-right'></span></a>";
                        }
                        else {
                          $newPage = $page + 1;
                          echo "<a class='btn btn-primary btn-xs' href='items.php?page=$newPage'><span class='glyphicon glyphicon-chevron-right'></span></a>";
                        }
                    }
              }

              
              
          ?>
        </div>
    </div> <!-- /container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="bootstrap/js/jquery.js"></script>
    <script src="bootstrap/js/bootstrap.js"></script>
    
  

</body></html>