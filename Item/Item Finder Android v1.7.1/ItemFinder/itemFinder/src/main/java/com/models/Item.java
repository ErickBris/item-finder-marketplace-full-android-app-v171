package com.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable {

	private static final long serialVersionUID = -3053478382970635522L;
	long created_at;;
	int updated_at;
	int is_deleted;
	int category_id;
	int featured;
	String item_desc;
	int item_id;
	String item_name;
	String item_price;
	int item_status;
	int item_type;
	int user_id;
	int is_published;
	float lat;
	float lon;
	ArrayList<Photo> photos;
	User user;
	float distance;
	float price;
	Item item_info;
	String category;
	String item_currency;

	public void setItem_currency(String item_currency) {
		this.item_currency = item_currency;
	}
	public String getItem_currency() {
		return item_currency;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	public String getCategory() {
		return category;
	}

//	public void setCategory(Category category) {
//		this.category = category;
//	}
//	public Category getCategory() {
//		return category;
//	}

	public void setItem_info(Item item_info) {
		this.item_info = item_info;
	}
	public Item getItem_info() {
		return item_info;
	}

	public void setUser(User user) {
		this.user = user;
	}
	public User getUser() {
		return user;
	}

	public void setPhotos(ArrayList<Photo> photos) {
		this.photos = photos;
	}
	public ArrayList<Photo> getPhotos() {
		return photos;
	}

	public void setFeatured(int featured) {
		this.featured = featured;
	}
	public int getFeatured() {
		return featured;
	}

	public void setItem_desc(String item_desc) {
		this.item_desc = item_desc;
	}
	public String getItem_desc() {
		return item_desc;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}
	public int getItem_id() {
		return item_id;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	public String getItem_name() {
		return item_name;
	}

	public void setItem_price(String item_price) {
		this.item_price = item_price;
	}
	public String getItem_price() {
		return item_price;
	}

	public void setItem_status(int item_status) {
		this.item_status = item_status;
	}
	public int getItem_status() {
		return item_status;
	}

	public void setItem_type(int item_type) {
		this.item_type = item_type;
	}
	public int getItem_type() {
		return item_type;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getUser_id() {
		return user_id;
	}

	public void setIs_published(int is_published) {
		this.is_published = is_published;
	}
	public int getIs_published() {
		return is_published;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public int getCategory_id() {
		return category_id;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}
	public float getLat() {
		return lat;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}
	public float getLon() {
		return lon;
	}

	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}
	public long getCreated_at() {
		return created_at;
	}

	public void setUpdated_at(int updated_at) {
		this.updated_at = updated_at;
	}
	public int getUpdated_at() {
		return updated_at;
	}

	public void setIs_deleted(int is_deleted) {
		this.is_deleted = is_deleted;
	}
	public int getIs_deleted() {
		return is_deleted;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}
	public float getDistance() {
		return distance;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	public float getPrice() {
		return price;
	}
}
