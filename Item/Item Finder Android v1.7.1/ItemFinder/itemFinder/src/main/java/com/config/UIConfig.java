package com.config;

import com.models.Menu;
import com.models.Menu.HeaderType;
import com.projects.itemfinder.R;

public class UIConfig {

	public static int THEME_BLACK_COLOR = R.color.theme_black_color;

	public static int THEME_MAIN_COLOR = R.color.theme_main_color;
	
	public static int BORDER_WIDTH = R.dimen.border_width;

	public static float NO_BORDER_WIDTH = 0.0f;
	
	public static Menu MENU_HOME = new Menu(
			R.string.home, R.drawable.ic_home, R.drawable.ic_home, HeaderType.HeaderType_CATEGORY);

	public static Menu MENU_CATEGORIES = new Menu(
			R.string.categories, R.drawable.ic_categories, R.drawable.ic_categories, HeaderType.HeaderType_CATEGORY);

	public static Menu MENU_STARRED = new Menu(
			R.string.starred, R.drawable.ic_starred, R.drawable.ic_starred, HeaderType.HeaderType_CATEGORY);

	public static Menu MENU_FEATURED = new Menu(
			R.string.featured, R.drawable.ic_featured, R.drawable.ic_featured, HeaderType.HeaderType_CATEGORY);

	public static Menu MENU_SEARCH = new Menu(
			R.string.search, R.drawable.ic_search, R.drawable.ic_search, HeaderType.HeaderType_CATEGORY);

	public static Menu MENU_PROMOTIONS = new Menu(
			R.string.promotions, R.drawable.ic_news, R.drawable.ic_news, HeaderType.HeaderType_CATEGORY);

	public static Menu MENU_ABOUT_US = new Menu(
			R.string.about_us, R.drawable.ic_about, R.drawable.ic_about, HeaderType.HeaderType_CATEGORY);

	public static Menu MENU_TERMS_CONDITION = new Menu(
			R.string.terms_condition, R.drawable.ic_tc, R.drawable.ic_tc, HeaderType.HeaderType_CATEGORY);

	public static Menu MENU_REGISTER = new Menu(
			R.string.register, R.drawable.ic_register, R.drawable.ic_register, HeaderType.HeaderType_CATEGORY);

	public static Menu MENU_LOGIN = new Menu(
			R.string.login, R.drawable.ic_login, R.drawable.ic_login, HeaderType.HeaderType_CATEGORY);
	
	public static Menu MENU_PROFILE = new Menu(
			R.string.profile, R.drawable.ic_register, R.drawable.ic_register, HeaderType.HeaderType_CATEGORY);

	public static Menu MENU_MY_ITEMS = new Menu(
			R.string.my_items, R.drawable.ic_categories, R.drawable.ic_categories, HeaderType.HeaderType_CATEGORY);

	public static Menu MENU_SETTINGS = new Menu(
			R.string.settings, R.drawable.ic_settings, R.drawable.ic_settings, HeaderType.HeaderType_CATEGORY);
	
	public static Menu HEADER_CATEGORIES = new Menu(
			R.string.header_categories, -1, -1, HeaderType.HeaderType_LABEL);

	public static Menu HEADER_EXTRAS = new Menu(
			R.string.header_extras, -1,-1, HeaderType.HeaderType_LABEL);

	public static Menu HEADER_USER = new Menu(
			R.string.header_users, -1, -1, HeaderType.HeaderType_LABEL);
	
	public static Menu[] MENUS_NOT_LOGGED = {
		
		HEADER_CATEGORIES,
		MENU_HOME,
		MENU_CATEGORIES,
		MENU_STARRED,
		MENU_FEATURED,
		MENU_SEARCH,
		MENU_PROMOTIONS,
		
		HEADER_EXTRAS,
		MENU_ABOUT_US,
		MENU_TERMS_CONDITION,
			MENU_SETTINGS,
		
		HEADER_USER,
		MENU_REGISTER,
		MENU_LOGIN,
	};
	
	public static Menu[] MENUS_LOGGED = {
		
		HEADER_CATEGORIES,
		MENU_HOME,
		MENU_CATEGORIES,
		MENU_STARRED,
		MENU_FEATURED,
		MENU_SEARCH,
		MENU_PROMOTIONS,
		MENU_MY_ITEMS,
		
		HEADER_EXTRAS,
		MENU_ABOUT_US,
		MENU_TERMS_CONDITION,
			MENU_SETTINGS,
		
		HEADER_USER,
		MENU_PROFILE
	};

	public static int SLIDER_PLACEHOLDER = R.drawable.slider_placeholder;
}
