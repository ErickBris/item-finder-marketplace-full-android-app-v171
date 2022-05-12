package com.config;

public class Config {

    // Max number of photos be uploaded per item
    public static int MAX_ITEM_UPLOAD_PHOTO = 5;

	// Change this on your own consumer key
	public static final String TWITTER_CONSUMER_KEY = "R0u1hsVNb1lOEMzbxW6Ju1jcm";

	// Change this on your own consumer secret
	public static final String TWITTER_CONSUMER_SECRET = "CGIZRrb7mabu16g89U1Uz1oDKor7qxWkqpQy4KLfmWzA9VsQBd";

	// Set to true if you want to display test ads in emulator
	public static final boolean TEST_ADS_USING_EMULATOR = true;

	// Set to true if you want to display test ads on your testing device
	public static final boolean TEST_ADS_USING_TESTING_DEVICE = true;

	// Add testing device hash
	// It is displayed upon running the app, please check logcat.
	public static final String TESTING_DEVICE_HASH = "3BE2FA86964E0348BBE40ECFE3FAD546";

	// Set to true if you want to display ads in all views.
	public static final boolean WILL_SHOW_ADS = true;

	// You AdMob Banner Unit ID
	public static final String BANNER_UNIT_ID = "ca-app-pub-2513284293470814/4631467285";

	// Change this url depending on the name of your web hosting.
	public static String BASE_URL = "http://mangasaurgames.com/apps/itemfinder-v1.7.1/";

    public static String SERVER_URL_DEFAULT_PAGE_FOR_FACEBOOK = "http://mangasaurgames.com/";

	// Max home items to show ignoring radius distance
	public static int GET_HOME_LATEST_COUNT = 20;

	// Maximum radius in km to fetch
	public static int MAX_RADIUS_IN_KM = 5000;

	// Default filter distance in km
	public static int DEFAULT_FILTER_DISTANCE_IN_KM = 1000;

	// Currency to appear if the user decided not to put any
	public static String DEFAULT_CURRENCY_IF_EMPTY = "$";

	// Your email that you wish that users on your app will contact you.
	public static String ABOUT_US_EMAIL = "mangasaurgames@gmail.com";

	// Edit this if you wish to increase
	// character count when adding reviews.
	public static int MAX_CHARS_REVIEWS = 255;

	// DO NOT EDIT THIS
	public final static String API_KEY = "8d29430ba38327d91303a935520e7b95";

	// DO NOT EDIT THIS
	public static String GET_ITEMS_URL = BASE_URL + "rest/get_items.php";

	// DO NOT EDIT THIS
	public static String SEARCH_ITEMS_JSON_URL = BASE_URL + "rest/search_items.php";

	// DO NOT EDIT THIS
	public static String GET_NEWS_URL = BASE_URL + "rest/get_news.php";

	// DO NOT EDIT THIS
	public static String REGISTER_URL = BASE_URL + "rest/register.php";

	// DO NOT EDIT THIS
	public static String USER_PHOTO_UPLOAD_URL = BASE_URL + "rest/file_uploader_user_photo.php";

	// DO NOT EDIT THIS
	public static String GET_USER_REVIEWS_JSON_URL = BASE_URL + "rest/get_user_reviews.php";

	// DO NOT EDIT THIS
	public static String POST_REVIEW_URL = BASE_URL + "rest/post_review.php";

	// DO NOT EDIT THIS
	public static String LOGIN_URL = BASE_URL + "rest/login.php";

	// DO NOT EDIT THIS
	public static String UPDATE_USER_PROFILE_URL = BASE_URL + "rest/update_user_profile.php";

	// DO NOT EDIT THIS
	public static String SYNC_ITEM_JSON_URL = BASE_URL + "rest/insert_item_android.php";

	// DO NOT EDIT THIS
	public static String SYNC_ITEM_SOLD_JSON_URL = BASE_URL + "rest/sold_item.php";

	// DO NOT EDIT THIS
	public static String SYNC_PUBLISH_ITEM_URL = BASE_URL + "rest/publish_item.php";

    // DO NOT EDIT THIS
	public static String INSERT_ITEM_PHOTO_ANDROID_JSON_URL = BASE_URL + "rest/insert_item_photo_android.php";

	// DO NOT EDIT THIS
	public final static int DELAY_SHOW_ANIMATION = 200;

    // DO NOT EDIT THIS
	public static int CATEGORY_SELECTION_REQUEST_CODE = 1110;

    // DO NOT EDIT THIS
	public static int NEW_REVIEW_REQUEST_CODE = 1118;

    // DO NOT EDIT THIS
	public static int NEW_ITEM_REQUEST_CODE = 1120;

    // DO NOT EDIT THIS
	public static int EDIT_ITEM_REQUEST_CODE = 1128;

    // DO NOT EDIT THIS
	public static final int TAG_CAMERA = 9911;

    // DO NOT EDIT THIS
	public static final int TAG_PHOTO_PIC = 9912;

    // DO NOT EDIT THIS
	public static String SD_CARD_PATH = "itemfinder_photos/";

    // DO NOT EDIT THIS
	public static int STATUS_SUCCESS = -1;

    // DO NOT EDIT THIS
	public static int SEARCH_RADIUS_MAXIMUM_KILOMETERS = 1000;

    // DO NOT EDIT THIS
	public static int SEARCH_RADIUS_KILOMETERS_DEFAULT = 500;

    // DO NOT EDIT THIS
	public static int SEARCH_RADIUS_MINIMUM_KILOMETERS = 0;

    // DO NOT EDIT THIS
	public static int SEARCH_MINIMUM_PRICE = 1;

    // DO NOT EDIT THIS
	public static int SEARCH_MAXIMUM_PRICE = 10000;

    // DO NOT EDIT THIS
	public static int SEARCH_MINIMUM_PRICE_DEFAULT = 1;

    // DO NOT EDIT THIS
	public static int SEARCH_MAXIMUM_PRICE_DEFAULT = SEARCH_MAXIMUM_PRICE / 2;

    // DO NOT EDIT THIS
	public static int MAX_CHARS_ITEM_DESCRIPTION = 500;

	// DO NOT EDIT THIS
	public static boolean AUTO_ADJUST_DISTANCE = true;


}
