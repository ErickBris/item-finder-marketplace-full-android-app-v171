package com.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Data {

	private ArrayList<Item> category_items;
	private ArrayList<Item> featured_items;
	private ArrayList<Item> home_items;

	private ArrayList<Category> categories;
	private ArrayList<Photo> photos;

	private ArrayList<Item> items;
	private ArrayList<Item> user_items;
	private ArrayList<Item> search_items;
	private ArrayList<News> news;

	@JsonProperty("item_details")
	private Item item_details;

	@JsonProperty("status")
	private Status status;

	@JsonProperty("count")
	private int count;

	private int max_distance;
	private int default_distance;

	public void setMax_distance(int max_distance) {
		this.max_distance = max_distance;
	}
	public int getMax_distance() {
		return max_distance;
	}

	public void setDefault_distance(int default_distance) {
		this.default_distance = default_distance;
	}
	public int getDefault_distance() {
		return default_distance;
	}

	public void setCount(int count) {
		this.count = count;
	}
	public int getCount() {
		return count;
	}

	public void setNews(ArrayList<News> news) {
		this.news = news;
	}
	public ArrayList<News> getNews() {
		return news;
	}

	public void setUser_items(ArrayList<Item> user_items) {
		this.user_items = user_items;
	}
	public ArrayList<Item> getUser_items() {
		return user_items;
	}

	public void setItem_details(Item item_details) {
		this.item_details = item_details;
	}
	public Item getItem_details() {
		return item_details;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}
	public ArrayList<Item> getItems() {
		return items;
	}

	public void setCategories(ArrayList<Category> s) {
		categories = s;
	}
	public ArrayList<Category> getCategories() {
	    return categories;
	}

	public void setPhotos(ArrayList<Photo> s) {
	    photos = s;
	}
	public ArrayList<Photo> getPhotos() {
	    return photos;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	public Status getStatus() {
		return status;
	}

	public void setCategory_items(ArrayList<Item> category_items) {
		this.category_items = category_items;
	}
	public ArrayList<Item> getCategory_items() {
		return category_items;
	}

	public void setFeatured_items(ArrayList<Item> featured_items) {
		this.featured_items = featured_items;
	}
	public ArrayList<Item> getFeatured_items() {
		return featured_items;
	}

	public void setHome_items(ArrayList<Item> home_items) {
		this.home_items = home_items;
	}
	public ArrayList<Item> getHome_items() {
		return home_items;
	}

	public void setSearch_items(ArrayList<Item> search_items) {
		this.search_items = search_items;
	}
	public ArrayList<Item> getSearch_items() {
		return search_items;
	}
}
