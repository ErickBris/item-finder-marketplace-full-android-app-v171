package com.db;

import java.util.ArrayList;
import com.models.Category;
import com.models.Favorite;
import com.models.Item;
import com.models.News;
import com.models.Photo;
import com.models.User;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Queries {
	
	private SQLiteDatabase db;
	private DbHelper dbHelper;

	public Queries(SQLiteDatabase db, DbHelper dbHelper) {
		this.db = db;
		this.dbHelper = dbHelper;
	}
	
	public void deleteTable(String tableName) {
		db = dbHelper.getWritableDatabase();
		try{
			db.delete(tableName, null, null);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		db.close();
	}

    public void insertUser(User entry) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", entry.getUser_id());
        values.put("email", entry.getEmail());
        values.put("full_name", entry.getFull_name());
        values.put("last_logged", entry.getLast_logged());
        values.put("phone_no", entry.getPhone_no());
        values.put("photo_url", entry.getPhoto_url());
        values.put("sms_no", entry.getSms_no());
        values.put("thumb_url", entry.getThumb_url());
        values.put("username", entry.getUsername());
        values.put("created_at", entry.getCreated_at());
        values.put("is_deleted", entry.getIs_deleted());
        values.put("updated_at", entry.getUpdated_at());
        db.insert("users", null, values);
    }

	public void insertNews(News entry) {
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("news_content", entry.getNews_content());
		values.put("news_title", entry.getNews_title());
		values.put("news_url", entry.getNews_url());
		values.put("photo_url", entry.getPhoto_url());
		values.put("created_at", entry.getCreated_at());
		values.put("is_deleted", entry.getIs_deleted());
		values.put("news_id", entry.getNews_id());
		values.put("updated_at", entry.getUpdated_at());
		db.insert("news", null, values);
	}

	public void updateItem(Item entry) {
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("user_id", entry.getUser_id());
		values.put("category_id", entry.getCategory_id());
		values.put("category", entry.getCategory());
		values.put("featured", entry.getFeatured());
		values.put("is_published", entry.getIs_published());
		values.put("item_desc", entry.getItem_desc());
		values.put("item_id", entry.getItem_id());
		values.put("item_name", entry.getItem_name());
		values.put("item_price", entry.getItem_price());
		values.put("item_status", entry.getItem_status());
		values.put("item_type", entry.getItem_type());
		values.put("lat", entry.getLat());
		values.put("lon", entry.getLon());
		values.put("created_at", entry.getCreated_at());
		values.put("is_deleted", entry.getIs_deleted());
		values.put("updated_at", entry.getUpdated_at());
		values.put("item_currency", entry.getItem_currency());
		db.update("items", values, "item_id =" + entry.getItem_id(), null);
	}

	public void insertItem(Item entry) {
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("item_currency", entry.getItem_currency());
		values.put("user_id", entry.getUser_id());
		values.put("category_id", entry.getCategory_id());
		values.put("category", entry.getCategory());
		values.put("featured", entry.getFeatured());
		values.put("is_published", entry.getIs_published());
		values.put("item_desc", entry.getItem_desc());
		values.put("item_id", entry.getItem_id());
		values.put("item_name", entry.getItem_name());
		values.put("item_price", entry.getItem_price());
		values.put("item_status", entry.getItem_status());
		values.put("item_type", entry.getItem_type());
		values.put("lat", entry.getLat());
		values.put("lon", entry.getLon());
		values.put("created_at", entry.getCreated_at());
		values.put("is_deleted", entry.getIs_deleted());
		values.put("updated_at", entry.getUpdated_at());
		db.insert("items", null, values);
	}

	public void insertCategory(Category entry) {
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("category", entry.getCategory());
		values.put("pid", entry.getPid());
		values.put("category_id", entry.getCategory_id());
		values.put("created_at", entry.getCreated_at());
		values.put("is_deleted", entry.getIs_deleted());
		values.put("updated_at", entry.getUpdated_at());
		db.insert("categories", null, values);
	}
	
	public void insertPhoto(Photo entry) {
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("photo_url", entry.getPhoto_url());
		values.put("thumb_url", entry.getThumb_url());
		values.put("created_at", entry.getCreated_at());
		values.put("is_deleted", entry.getIs_deleted());
		values.put("photo_id", entry.getPhoto_id());
        values.put("item_id", entry.getItem_id());
		values.put("updated_at", entry.getUpdated_at());
		db.insert("photos", null, values);
	}
	
	public void insertFavorite(Favorite entry) {
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("item_id", entry.getItem_id());
		db.insert("favorites", null, values);
	}

	public void deleteNews(int news_id) {
		db = dbHelper.getWritableDatabase();
		db.delete("news", "news_id = " + news_id, null);
	}

	public void deleteCategory(int category_id) {
		db = dbHelper.getWritableDatabase();
		db.delete("categories", "category_id = " + category_id, null);
	}

	public void deletePhoto(int photo_id) {
		db = dbHelper.getWritableDatabase();
		db.delete("photos", "photo_id = " + photo_id, null);
	}

	public void deleteItem(int item_id) {
		db = dbHelper.getWritableDatabase();
		db.delete("items", "item_id = " + item_id, null);
	}

	public void deleteUser(int user_id) {
		db = dbHelper.getWritableDatabase();
		db.delete("users", "user_id = " + user_id, null);
	}

	public void deleteFavorite(int itemId) {
		db = dbHelper.getWritableDatabase();
		db.delete("favorites", "item_id = " + itemId, null);
	}

	public ArrayList<News> getNews() {
		ArrayList<News> list = new ArrayList<News>();
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM news ORDER BY updated_at DESC", null); 
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				News news = formatNews(mCursor);
				list.add(news);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return list;
	}

    public User getUserByUserId(int userId) {
        User user = null;
        db = dbHelper.getReadableDatabase();
        String sql = String.format("SELECT * FROM users WHERE user_id = %d", userId);
        Cursor mCursor = db.rawQuery(sql, null);
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                user = formatUser(mCursor);
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        return user;
    }

	public News getNewsByNewsId(int newsId) {
		News news = null;
		db = dbHelper.getReadableDatabase();
		String sql = String.format("SELECT * FROM news WHERE news_id = %d", newsId);
		Cursor mCursor = db.rawQuery(sql, null); 
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
                news = formatNews(mCursor);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return news;
	}

	public ArrayList<Item> getItems() {
		ArrayList<Item> list = new ArrayList<Item>();
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM items", null);
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				Item entry = formatItem(mCursor);
				list.add(entry);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return list;
	}

	public ArrayList<Category> getCategories() {
		ArrayList<Category> list = new ArrayList<Category>();
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM categories ORDER BY category ASC", null); 
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				Category entry = formatCategory(mCursor);
				list.add(entry);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return list;
	}

	public ArrayList<Item> getItemsFavorites() {
		ArrayList<Item> list = new ArrayList<Item>();
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM items INNER JOIN favorites ON items.item_id = favorites.item_id ORDER BY items.item_name", null);
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				Item entry = formatItem(mCursor);
				list.add(entry);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return list;
	}

	public Category getCategoryByCategoryId(int categoryId) {
		Category entry = null;
		db = dbHelper.getReadableDatabase();
		String sql = String.format("SELECT * FROM categories WHERE category_id = %d", categoryId);
		Cursor mCursor = db.rawQuery(sql, null); 
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
                entry = formatCategory(mCursor);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return entry;
	}

    public Category formatCategory(Cursor mCursor) {
        Category entry = new Category();
        entry.setCategory( mCursor.getString( mCursor.getColumnIndex("category")) );
        entry.setPid( mCursor.getInt( mCursor.getColumnIndex("pid")) );
        entry.setCategory_id( mCursor.getInt( mCursor.getColumnIndex("category_id")) );
        entry.setCreated_at(mCursor.getInt(mCursor.getColumnIndex("created_at")));
        entry.setIs_deleted(mCursor.getInt(mCursor.getColumnIndex("is_deleted")));
        entry.setUpdated_at(mCursor.getInt(mCursor.getColumnIndex("updated_at")));
        return entry;
    }

    public Photo formatPhoto(Cursor mCursor) {
        Photo entry = new Photo();
        entry.setCreated_at(mCursor.getInt(mCursor.getColumnIndex("created_at")));
        entry.setIs_deleted(mCursor.getInt(mCursor.getColumnIndex("is_deleted")));
        entry.setPhoto_id(mCursor.getInt(mCursor.getColumnIndex("photo_id")));
        entry.setItem_id(mCursor.getInt(mCursor.getColumnIndex("item_id")));
        entry.setPhoto_url(mCursor.getString(mCursor.getColumnIndex("photo_url")));
        entry.setThumb_url(mCursor.getString(mCursor.getColumnIndex("thumb_url")));
        entry.setUpdated_at( mCursor.getInt( mCursor.getColumnIndex("updated_at")) );
        return entry;
    }

    public News formatNews(Cursor mCursor) {
        News news = new News();
        news.setCreated_at( mCursor.getInt(mCursor.getColumnIndex("created_at")));
        news.setIs_deleted(mCursor.getInt(mCursor.getColumnIndex("is_deleted")));
        news.setNews_content(mCursor.getString(mCursor.getColumnIndex("news_content")));
        news.setNews_id(mCursor.getInt(mCursor.getColumnIndex("news_id")));
        news.setNews_title(mCursor.getString(mCursor.getColumnIndex("news_title")));
        news.setNews_url(mCursor.getString(mCursor.getColumnIndex("news_url")));
        news.setPhoto_url(mCursor.getString(mCursor.getColumnIndex("photo_url")));
        news.setUpdated_at( mCursor.getInt( mCursor.getColumnIndex("updated_at")) );
        return news;
    }

	public Item formatItem(Cursor mCursor) {
		Item entry = new Item();
		entry.setFeatured(mCursor.getInt(mCursor.getColumnIndex("featured")));
		entry.setIs_published(mCursor.getInt(mCursor.getColumnIndex("is_published")));
		entry.setItem_desc(mCursor.getString(mCursor.getColumnIndex("item_desc")));
		entry.setItem_id(mCursor.getInt(mCursor.getColumnIndex("item_id")));
		entry.setItem_name(mCursor.getString(mCursor.getColumnIndex("item_name")));
		entry.setItem_price(mCursor.getString(mCursor.getColumnIndex("item_price")));
		entry.setItem_status(mCursor.getInt(mCursor.getColumnIndex("item_status")));
        entry.setItem_type(mCursor.getInt(mCursor.getColumnIndex("item_type")));
		entry.setLat(mCursor.getFloat(mCursor.getColumnIndex("lat")));
		entry.setLon(mCursor.getFloat(mCursor.getColumnIndex("lon")));
		entry.setUser_id(mCursor.getInt(mCursor.getColumnIndex("user_id")));
        entry.setCategory_id(mCursor.getInt(mCursor.getColumnIndex("category_id")));
        entry.setCreated_at(mCursor.getInt(mCursor.getColumnIndex("created_at")));
        entry.setIs_deleted(mCursor.getInt(mCursor.getColumnIndex("is_deleted")));
		entry.setUpdated_at( mCursor.getInt( mCursor.getColumnIndex("updated_at")) );
		entry.setCategory( mCursor.getString( mCursor.getColumnIndex("category")) );
		entry.setItem_currency( mCursor.getString( mCursor.getColumnIndex("item_currency")) );
		return entry;
	}

    public User formatUser(Cursor mCursor) {
        User entry = new User();
        entry.setUser_id(mCursor.getInt(mCursor.getColumnIndex("user_id")));
        entry.setEmail(mCursor.getString(mCursor.getColumnIndex("email")));
        entry.setFull_name(mCursor.getString(mCursor.getColumnIndex("full_name")));
        entry.setLast_logged(mCursor.getInt(mCursor.getColumnIndex("last_logged")));
        entry.setPhone_no(mCursor.getString(mCursor.getColumnIndex("phone_no")));
		entry.setSms_no(mCursor.getString(mCursor.getColumnIndex("sms_no")));
        entry.setPhoto_url(mCursor.getString(mCursor.getColumnIndex("photo_url")));
        entry.setThumb_url(mCursor.getString(mCursor.getColumnIndex("thumb_url")));
        entry.setUsername(mCursor.getString(mCursor.getColumnIndex("username")));
        entry.setCreated_at(mCursor.getInt(mCursor.getColumnIndex("created_at")));
        entry.setIs_deleted(mCursor.getInt(mCursor.getColumnIndex("is_deleted")));
        entry.setUpdated_at( mCursor.getInt( mCursor.getColumnIndex("updated_at")) );
        return entry;
    }

	public void closeDatabase() {
		db.close();
	}

    public Photo getPhotoByItemId(int itemId) {
        Photo entry = null;
        db = dbHelper.getReadableDatabase();
        String sql = String.format("SELECT * FROM photos WHERE item_id = %d ORDER BY photo_id ASC", itemId);
        Cursor mCursor = db.rawQuery(sql, null);
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                entry = formatPhoto(mCursor);
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        return entry;
    }

	public ArrayList<Category> getCategoriesByPID(int pid) {
		ArrayList<Category> list = new ArrayList<Category>();
		db = dbHelper.getReadableDatabase();
		String sql = String.format("SELECT * FROM categories WHERE pid = %d ORDER BY category ASC", pid);
		Cursor mCursor = db.rawQuery(sql, null);
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				Category entry = formatCategory(mCursor);
				list.add(entry);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return list;
	}

	public ArrayList<Item> getItemsFeatured() {
		ArrayList<Item> list = new ArrayList<Item>();
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM items WHERE featured = 1", null);
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				Item entry = formatItem(mCursor);
				list.add(entry);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return list;
	}

	public ArrayList<User> getUsersByUserNameOrFullName(String userNameAndFullName) {
		ArrayList<User> users = new ArrayList<User>();
		db = dbHelper.getReadableDatabase();
		String sql = "SELECT * FROM users WHERE UPPER(username) LIKE UPPER('%" + userNameAndFullName + "%') OR UPPER(full_name) LIKE UPPER('%" + userNameAndFullName + "%')";

		Cursor mCursor = db.rawQuery(sql, null);
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				User user = formatUser(mCursor);
				users.add(user);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return users;
	}

	public ArrayList<Item> getItemsByCategoryId(int categoryId) {
		ArrayList<Item> list = new ArrayList<Item>();
		db = dbHelper.getReadableDatabase();
		String sql = String.format("SELECT * FROM items WHERE category_id = %d", categoryId);
		Cursor mCursor = db.rawQuery(sql, null);
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				Item entry = formatItem(mCursor);
				list.add(entry);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return list;
	}

	public ArrayList<Photo> getPhotosByItemId(int itemId) {
		ArrayList<Photo> list = new ArrayList<Photo>();
		db = dbHelper.getReadableDatabase();
		String sql = String.format("SELECT * FROM photos WHERE item_id = %d", itemId);
		Cursor mCursor = db.rawQuery(sql, null);
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				Photo entry = formatPhoto(mCursor);
				list.add(entry);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return list;
	}

	public Favorite getFavoriteByItemId(int itemId) {
		Favorite entry = null;
		String sql = String.format("SELECT * FROM favorites WHERE item_id = %d", itemId);
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery(sql , null);
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				entry = new Favorite();
				entry.setFavorite_id( mCursor.getInt( mCursor.getColumnIndex("favorite_id")) );
				entry.setItem_id(mCursor.getInt(mCursor.getColumnIndex("item_id")));
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return entry;
	}

	public Item getItemByItemId(int itemId) {
		Item item = null;
		db = dbHelper.getReadableDatabase();
		String sql = String.format("SELECT * FROM items WHERE item_id = %d", itemId);
		Cursor mCursor = db.rawQuery(sql, null);
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				item = formatItem(mCursor);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return item;
	}

	public ArrayList<Item> getItemsByUserId(int userId) {
		ArrayList<Item> list = new ArrayList<Item>();
		db = dbHelper.getReadableDatabase();
		String sql = String.format("SELECT * FROM items WHERE user_id = %d", userId);
		Cursor mCursor = db.rawQuery(sql, null);
		mCursor.moveToFirst();
		if (!mCursor.isAfterLast()) {
			do {
				Item entry = formatItem(mCursor);
				list.add(entry);
			} while (mCursor.moveToNext());
		}
		mCursor.close();
		return list;
	}
}
