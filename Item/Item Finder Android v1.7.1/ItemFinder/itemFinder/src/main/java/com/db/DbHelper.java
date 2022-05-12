package com.db;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	
	static final String TAG = "DbHelper";
	static final String DB_NAME = "db_itemfinder";
	static final int DB_VERSION = 1;
	static Activity activity;
	
	public DbHelper(Activity act) {
		super(act.getApplicationContext(), DB_NAME, null, DB_VERSION);
		activity = act;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
			
	    db.execSQL("CREATE TABLE IF NOT EXISTS categories("
	    		+ "category_id INTEGER PRIMARY KEY,"
	    		+ "category TEXT,"
	    		+ "pid INTEGER,"
	    		+ "created_at INTEGER, "
	    		+ "is_deleted INTEGER, "
	    		+ "updated_at INTEGER "
	    		+ ");");

		db.execSQL("CREATE TABLE IF NOT EXISTS items("
				+ "item_id INTEGER PRIMARY KEY,"
				+ "category_id NTEGER, "
				+ "category TEXT, "
				+ "created_at INTEGER, "
				+ "featured INTEGER, "
				+ "is_deleted INTEGER, "
				+ "item_desc TEXT, "
				+ "item_currency TEXT, "
				+ "item_name INTEGER, "
				+ "item_price TEXT, "
				+ "item_status INTEGER, "
				+ "item_type INTEGER, "
				+ "updated_at INTEGER, "
				+ "user_id INTEGER, "
				+ "is_published INTEGER, "
				+ "lat FLOAT, "
				+ "lon FLOAT, "
				+ "distance FLOAT "
				+ ");");

	    db.execSQL("CREATE TABLE IF NOT EXISTS reviews("
	    		+ "review_id INTEGER PRIMARY KEY,"
	    		+ "created_at TEXT,"
	    		+ "first_name TEXT,"
	    		+ "last_name TEXT,"
				+ "review TEXT,"
				+ "parent_user_id INTEGER,"
				+ "updated_at INTEGER,"
				+ "is_deleted INTEGER, "
				+ "user_id INTEGER"
	    		+ ");");

	    db.execSQL("CREATE TABLE IF NOT EXISTS photos("
	    		+ "photo_id INTEGER PRIMARY KEY," //AUTOINCREMENT
	    		+ "created_at INTEGER,"
	    		+ "photo_url TEXT,"
	    		+ "store_id INTEGER,"
	    		+ "thumb_url TEXT,"
	    		+ "is_deleted INTEGER, "
				+ "item_id INTEGER, "
	    		+ "updated_at INTEGER"
	    		+ ");");
	    
	    db.execSQL("CREATE TABLE IF NOT EXISTS news("
	    		+ "news_id INTEGER PRIMARY KEY," //AUTOINCREMENT
	    		+ "created_at INTEGER,"
	    		+ "news_content TEXT,"
	    		+ "news_title TEXT,"
	    		+ "news_url TEXT,"
	    		+ "photo_url TEXT,"
	    		+ "is_deleted INTEGER, "
	    		+ "updated_at INTEGER"
	    		+ ");");

	    db.execSQL("CREATE TABLE IF NOT EXISTS favorites("
	    		+ "favorite_id INTEGER PRIMARY KEY AUTOINCREMENT," //AUTOINCREMENT
	    		+ "item_id INTEGER"
	    		+ ");");

		db.execSQL("CREATE TABLE IF NOT EXISTS users("
				+ "user_id INTEGER PRIMARY KEY,"
                + "is_deleted INTEGER,"
                + "created_at INTEGER,"
				+ "deny_access INTEGER,"
				+ "email TEXT,"
				+ "facebook_id INTEGER,"
				+ "full_name TEXT,"
				+ "last_logged INTEGER,"
				+ "login_hash TEXT, "
				+ "password TEXT, "
				+ "phone_no TEXT, "
				+ "photo_url TEXT, "
				+ "sms_no TEXT, "
				+ "thumb_url TEXT, "
				+ "twitter_id INTEGER, "
				+ "updated_at INTEGER, "
				+ "username TEXT "
				+ ");");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { 
		db.execSQL("DROP TABLE IF EXISTS categories");
		db.execSQL("DROP TABLE IF EXISTS favorites");
		db.execSQL("DROP TABLE IF EXISTS items");
		db.execSQL("DROP TABLE IF EXISTS users");
		db.execSQL("DROP TABLE IF EXISTS reviews");
		db.execSQL("DROP TABLE IF EXISTS ratings");
		db.execSQL("DROP TABLE IF EXISTS photos");
		db.execSQL("DROP TABLE IF EXISTS news");
		db.execSQL("DROP TABLE IF EXISTS favorites");
		
	}
}