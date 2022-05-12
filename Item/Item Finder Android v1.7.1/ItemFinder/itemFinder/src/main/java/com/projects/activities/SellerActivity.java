package com.projects.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.config.Config;
import com.config.UIConfig;
import com.db.DbHelper;
import com.db.Queries;
import com.libraries.adapters.MGRawAdapter;
import com.libraries.asynctask.MGAsyncTask;
import com.libraries.blur.FastBlur;
import com.libraries.cache.util.ImageFetcherWrapper;
import com.libraries.dataparser.DataParser;
import com.libraries.datetime.TimeAgo;
import com.libraries.helpers.DateTimeHelper;
import com.libraries.html.MGHtml;
import com.libraries.imageview.MGImageView;
import com.libraries.imageview.RoundedImageView;
import com.libraries.refreshlayout.SwipeRefreshActivity;
import com.libraries.tab.MGTab;
import com.libraries.usersession.UserAccessSession;
import com.libraries.utilities.MGUtilities;
import com.models.Category;
import com.models.Data;
import com.models.Favorite;
import com.models.Item;
import com.models.Photo;
import com.models.ResponseReview;
import com.models.Review;
import com.models.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.projects.itemfinder.MainActivity;
import com.projects.itemfinder.R;

import java.util.ArrayList;

public class SellerActivity extends SwipeRefreshActivity implements MGTab.OnTabListener,
		AdapterView.OnItemClickListener, MGRawAdapter.OnMGRawAdapterListener{

	public ImageFetcherWrapper imageFetcher;
	DisplayImageOptions options;
	MGTab tabSeller;
	private Queries q;
	private SQLiteDatabase db;
	private User user;
	private ArrayList<Item> userItems;
	private ArrayList<Review> userReviews;
	MGAsyncTask task;
    MGRawAdapter adapter;
    Bitmap cacheBlur;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		setContentView(R.layout.fragment_seller_detail);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
//        this.getActionBar().setIcon(R.drawable.header_logo);
//        this.getActionBar().setTitle("");

		this.getActionBar().setTitle("");
		this.getActionBar().setIcon(R.drawable.empty_image);
		this.getActionBar().setDisplayShowCustomEnabled(true);
		View viewActionBar = getLayoutInflater().inflate(R.layout.header, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.WRAP_CONTENT,
				ActionBar.LayoutParams.MATCH_PARENT,
				Gravity.CENTER);

		this.getActionBar().setCustomView(viewActionBar, params);


		DbHelper dbHelper = new DbHelper(this);
		q = new Queries(db, dbHelper);

		user = (User) this.getIntent().getSerializableExtra("user");
//		userItems = q.getItemsByUserId(user.getUser_id());
		imageFetcher = new ImageFetcherWrapper(
				SellerActivity.this, 0, 0, UIConfig.SLIDER_PLACEHOLDER);
		
        options = new DisplayImageOptions.Builder()
			.showImageOnLoading(UIConfig.SLIDER_PLACEHOLDER)
			.showImageForEmptyUri(UIConfig.SLIDER_PLACEHOLDER)
			.showImageOnFail(UIConfig.SLIDER_PLACEHOLDER)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();

		tabSeller = (MGTab) findViewById(R.id.tabSeller);
		tabSeller.setOnTabListener(this);
		tabSeller.createTab(3, R.layout.tab_entry, false);
		tabSeller.setSelectedTab(0);
	}

	@Override
	public void onTabCreated(MGTab tab, int index, View v) {
		if(index == 0) {
			TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
			tvTitle.setText(R.string.profile);
			tvTitle.setBackgroundResource(R.drawable.inner_tab_left_unselected);
		}
		else if(index == 1) {
			TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
			tvTitle.setText(R.string.items);
			tvTitle.setBackgroundResource(R.drawable.inner_tab_middle_unselected);
		}
		else if(index == 2) {
			TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
			tvTitle.setText(R.string.review);
			tvTitle.setBackgroundResource(R.drawable.inner_tab_right_unselected);
		}
	}

	@Override
	public void onTabSelected(MGTab tab, int index, View v) {
		if(index == 0) {
			TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
			tvTitle.setBackgroundResource(R.drawable.inner_tab_left_selected);
		}
		else if(index == 1) {
			TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
			tvTitle.setBackgroundResource(R.drawable.inner_tab_middle_selected);
		}
		else if(index == 2) {
			TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
			tvTitle.setBackgroundResource(R.drawable.inner_tab_right_selected);
		}

		updateList();
	}

	@Override
	public void onTabPreviouslySelected(MGTab tab, int index, View v) {
		if(index == 0) {
			TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
			tvTitle.setBackgroundResource(R.drawable.inner_tab_right_unselected);
		}
		else if(index == 1) {
			TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
			tvTitle.setBackgroundResource(R.drawable.inner_tab_right_unselected);
		}
		else if(index == 2) {
			TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
			tvTitle.setBackgroundResource(R.drawable.inner_tab_right_unselected);
		}
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        // Handle action bar actions click
        switch (item.getItemId()) {
			case R.id.menuReview:
                UserAccessSession session = UserAccessSession.getInstance(this);
                if(session.getUserSession() == null) {
                    MGUtilities.showAlertView(
                            this, R.string.user_review_error,
                            R.string.user_review_error_details);
                    return false;
                }

				Intent i = new Intent(this, NewReviewActivity.class);
				i.putExtra("user", user);
				startActivityForResult(i, Config.NEW_REVIEW_REQUEST_CODE);
				return true;

	        default:
	        	finish();	
	            return super.onOptionsItemSelected(item);
        }
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Config.NEW_REVIEW_REQUEST_CODE) {
			if(resultCode == RESULT_OK) {
				ArrayList<Review> reviews = (ArrayList<Review>) data.getSerializableExtra("reviews");
				if(reviews != null && reviews.size() > 0) {
					userReviews = reviews;
					if(tabSeller.getSelectedTabIndex() == 2) {
						formatList(userReviews.size());
					}
				}
			}
			if (resultCode == RESULT_CANCELED) { }
		}
	}
    
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        // if nav drawer is opened, hide the action items
        return super.onPrepareOptionsMenu(menu);
    }

	public void updateList() {
		int index = tabSeller.getSelectedTabIndex();

		if(task != null)
			task.cancel(true);

		if(index == 0) {
			formatList(2);
		}
		if(index == 1) {
			if(userItems == null) {
				getSellerItems();
			}
			else {
				userItems = q.getItemsByUserId(user.getUser_id());
				formatList(userItems.size() + 1);
			}
		}
		if(index == 2) {
			if(userReviews == null)
				getReviews();
			else
				formatList(userReviews.size());
		}
	}

	public void formatList(int rows) {
		ListView listViewMain = (ListView) findViewById(R.id.listView);
		listViewMain.setOnItemClickListener(this);
		if(adapter != null) {
            adapter.clearAdapter();
            adapter.notifyDataSetChanged();
        }
		if(rows > 0) {
            adapter = new MGRawAdapter(this, rows);
            adapter.setOnMGRawAdapterListener(this);
            listViewMain.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        if(tabSeller.getSelectedTabIndex() == 1 ) {
            if(userItems == null || userItems.size() == 0) {
                MGUtilities.showNotifier(this, MainActivity.offsetY);
                return;
            }
        }
        if(tabSeller.getSelectedTabIndex() == 2) {
            if(userReviews == null || userReviews.size() == 0) {
                MGUtilities.showNotifier(this, MainActivity.offsetY);
                return;
            }
        }
	}

	@Override
	public View OnMGRawAdapterCreated(MGRawAdapter adapter, View v, int position, ViewGroup viewGroup) {
		LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(tabSeller.getSelectedTabIndex() == 0) {
			if(position == 0) {
				v = li.inflate(R.layout.seller_profile_entry, null);
				TextView tvUserName = (TextView)v.findViewById(R.id.tvUserName);
                tvUserName.setText(R.string.no_value);

				TextView tvFullName = (TextView)v.findViewById(R.id.tvFullName);
                tvFullName.setText(R.string.no_value);

                if(user.getFull_name() != null && user.getFull_name().length() > 0)
                    tvFullName.setText(Html.fromHtml(user.getFull_name()));

                if(user.getUsername() != null && user.getUsername().length() > 0)
                    tvUserName.setText(user.getUsername());

				RoundedImageView imgViewPhoto = (RoundedImageView) v.findViewById(R.id.imgViewThumb);
				imgViewPhoto.setCornerRadius(R.dimen.corner_radius_seller_profile);
				imgViewPhoto.setBorderWidth(UIConfig.BORDER_WIDTH);
				imgViewPhoto.setBorderColor(getResources().getColor(UIConfig.THEME_BLACK_COLOR));

				ImageView imgViewCover = (ImageView)v.findViewById(R.id.imgViewCover);
				if(user.getThumb_url() != null) {
					MainActivity.getImageLoader().displayImage(user.getThumb_url(), imgViewPhoto, options);
				}
				if(user.getPhoto_url() != null) {
                    if(cacheBlur == null)
					    blurCoverPhoto(imgViewCover, user.getPhoto_url());
                    else
                        imgViewCover.setImageBitmap(cacheBlur);
				}
				return v;
			}
			if(position == 1) {
				v = li.inflate(R.layout.seller_profile_info_entry, null);
				TextView tvLastLogged = (TextView)v.findViewById(R.id.tvLastLogged);
				tvLastLogged.setText(TimeAgo.toDuration(user.getLast_logged() * 1000));

				TextView tvItemCount = (TextView)v.findViewById(R.id.tvItemCount);

                int count = 0;
                ArrayList<Item> _userItems = q.getItemsByUserId(user.getUser_id());
                if(_userItems.size() > 0)
                    count = _userItems.size();

				tvItemCount.setText(String.valueOf(count));
				return v;
			}
		}
		else if(tabSeller.getSelectedTabIndex() == 1) {
			if(position == 0) {
				v = li.inflate(R.layout.header_entry, null);
				TextView tvTitle = (TextView)v.findViewById(R.id.tvTitle);
				tvTitle.setText(R.string.user_items_title);
				return v;
			}
			if(position >= 1) {
				v = li.inflate(R.layout.item_entry, null);
				final Item item = userItems.get(position - 1);
				Photo p = q.getPhotoByItemId(item.getItem_id());
				MGImageView imgViewPhoto = (MGImageView) v.findViewById(R.id.imgViewPhoto);
				imgViewPhoto.setCornerRadius(0.0f);
				imgViewPhoto.setBorderWidth(UIConfig.NO_BORDER_WIDTH);
				imgViewPhoto.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent i = new Intent(SellerActivity.this, DetailItemActivity.class);
						i.putExtra("item", item);
						startActivity(i);
					}
				});

				if (p != null) {
					MainActivity.getImageLoader().displayImage(p.getPhoto_url(), imgViewPhoto, options);
				} else {
					imgViewPhoto.setImageResource(UIConfig.SLIDER_PLACEHOLDER);
				}

				Spanned name = Html.fromHtml(item.getItem_name());
				Spanned desc = Html.fromHtml(item.getItem_desc());
				String strDesc = MGHtml.decodeToPlainText(desc.toString());

				TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
				tvTitle.setText(name);

				TextView tvSubtitle = (TextView) v.findViewById(R.id.tvSubtitle);
				String strPrice = String.format("%s %s", Html.fromHtml(item.getItem_currency()), item.getItem_price());
				tvSubtitle.setText(strPrice);

				TextView tvDesc = (TextView) v.findViewById(R.id.tvDesc);
				tvDesc.setText(strDesc);

				TextView tvDistance = (TextView) v.findViewById(R.id.tvDistance);
				tvDistance.setVisibility(View.INVISIBLE);

				if(item.getDistance() != -1 || MainActivity.location != null) {
					tvDistance.setVisibility(View.VISIBLE);
					double km = item.getDistance();
					String format = String.format(
							"%.2f %s",
							km,
							MGUtilities.getStringFromResource(SellerActivity.this, R.string.km));
					tvDistance.setText(format);
				}
				else {
					tvDistance.setText(R.string.empty_distance);
				}

                ImageView imgViewFeatured = (ImageView) v.findViewById(R.id.imgViewFeatured);
                imgViewFeatured.setVisibility(View.VISIBLE);
                if (item.getFeatured() == 0)
                    imgViewFeatured.setVisibility(View.INVISIBLE);

                ImageView imgViewStarred = (ImageView) v.findViewById(R.id.imgViewStarred);
                imgViewStarred.setVisibility(View.VISIBLE);
                Favorite fave = q.getFavoriteByItemId(item.getItem_id());
                if(fave == null)
                    imgViewStarred.setVisibility(View.INVISIBLE);

                ImageView imgViewRibbon = (ImageView) v.findViewById(R.id.imgViewRibbon);
                imgViewRibbon.setImageResource(R.drawable.ribbon_edge_sold);
                imgViewRibbon.setVisibility(View.GONE);

                if(item.getItem_status() == 1)
                    imgViewRibbon.setVisibility(View.VISIBLE);
				return v;
			}
		}
		else if(tabSeller.getSelectedTabIndex() == 2) {
			if(position == 0) {
				v = li.inflate(R.layout.header_entry, null);
				TextView tvTitle = (TextView)v.findViewById(R.id.tvTitle);
				tvTitle.setText(R.string.member_reviews_title);
				return v;
			}
			if(position >=  1) {
				v = li.inflate(R.layout.user_review_entry, null);
				Review review = userReviews.get(position - 1);

				Spanned details1 = Html.fromHtml(review.getReview());
				details1 = Html.fromHtml(details1.toString());
				String reviewString = details1.toString();
				Spanned title = Html.fromHtml(review.getFull_name());

				TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
				tvTitle.setText(title);

				TextView tvDetails = (TextView) v.findViewById(R.id.tvDetails);
				tvDetails.setText(reviewString);

				String date = DateTimeHelper.getStringDateFromTimeStamp(review.getCreated_at(), "MM/dd/yyyy hh:mm a");
				TextView tvDatePosted = (TextView) v.findViewById(R.id.tvDatePosted);
				tvDatePosted.setText(date);

				RoundedImageView imgViewPhoto = (RoundedImageView) v.findViewById(R.id.imgViewThumb);
				imgViewPhoto.setCornerRadius(R.dimen.corner_radius_review);
				imgViewPhoto.setBorderWidth(UIConfig.BORDER_WIDTH);
				imgViewPhoto.setBorderColor(getResources().getColor(UIConfig.THEME_BLACK_COLOR));
				if(review.getThumb_url() != null) {
					MainActivity.getImageLoader().displayImage(review.getThumb_url(), imgViewPhoto, options);
				}
				return v;
			}
		}
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

		if(tabSeller.getSelectedTabIndex() == 1) {
			Item item = userItems.get(position);
			Intent i = new Intent(this, DetailItemActivity.class);
			i.putExtra("item", item);
			startActivity(i);
		}
	}

	public void getReviews() {
        showSwipeProgress();
		if(!MGUtilities.hasConnection(this)) {
			MGUtilities.showAlertView(
					this,
					R.string.network_error,
					R.string.no_network_connection);
			hideSwipeProgress();
			return;
		}

		task = new MGAsyncTask(SellerActivity.this);
		task.setMGAsyncTaskListener(new MGAsyncTask.OnMGAsyncTaskListener() {

			private ResponseReview response;

			@Override
			public void onAsyncTaskProgressUpdate(MGAsyncTask asyncTask) { }

			@Override
			public void onAsyncTaskPreExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				asyncTask.dialog.hide();
			}

			@Override
			public void onAsyncTaskPostExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				if(tabSeller.getSelectedTabIndex() == 2) {
                    if(adapter != null) {
                        adapter.clearAdapter();
                        adapter.notifyDataSetChanged();
                    }

					if(response != null && response.getReviews() != null) {
							formatList(response.getReviews().size() + 1);
                    }
                    else {
                        MGUtilities.showNotifier(SellerActivity.this, MainActivity.offsetY);
                        return;
                    }
				}
				hideSwipeProgress();
			}

			@Override
			public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				String reviewUrl = String.format("%s?api_key=%s&user_id=%d",
						Config.GET_USER_REVIEWS_JSON_URL, Config.API_KEY, user.getUser_id());

				response = DataParser.getJSONFromUrlReview(reviewUrl, null);
				if(response != null && response.getReviews() != null) {
					userReviews = response.getReviews();
				}
			}
		});
		task.execute();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(task != null)
			task.cancel(true);
	}

	private void blurCoverPhoto(final ImageView imgView, String photoUrl) {
		MainActivity.getImageLoader().displayImage(photoUrl, imgView, options,
				new SimpleImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) { }

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) { }

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						if(tabSeller.getSelectedTabIndex() == 0 && loadedImage != null) {
                            cacheBlur = FastBlur.fastBlur(loadedImage, 1.8f, 50);
							imgView.setImageBitmap(cacheBlur);
						}
					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view, int current, int total) { }
				});
	}

	public void getSellerItems() {
		showSwipeProgress();
		if(!MGUtilities.hasConnection(this)) {
			MGUtilities.showAlertView(
					this,
					R.string.network_error,
					R.string.no_network_connection);
			hideSwipeProgress();
			return;
		}

		task = new MGAsyncTask(SellerActivity.this);
		task.setMGAsyncTaskListener(new MGAsyncTask.OnMGAsyncTaskListener() {

			private Data data;

			@Override
			public void onAsyncTaskProgressUpdate(MGAsyncTask asyncTask) { }

			@Override
			public void onAsyncTaskPreExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				asyncTask.dialog.hide();
			}

			@Override
			public void onAsyncTaskPostExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				if(tabSeller.getSelectedTabIndex() == 1) {
					if(adapter != null) {
						adapter.clearAdapter();
						adapter.notifyDataSetChanged();
					}

					if(data != null && data.getUser_items() != null) {
						formatList(userItems.size() + 1);
					}
					else {
						MGUtilities.showNotifier(SellerActivity.this, MainActivity.offsetY);
						return;
					}
				}
				hideSwipeProgress();
			}

			@Override
			public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				UserAccessSession accessSession = UserAccessSession.getInstance(SellerActivity.this);
				int radius = Config.DEFAULT_FILTER_DISTANCE_IN_KM;
				if(accessSession.getFilterDistance() > 0)
					radius = accessSession.getFilterDistance();

				if( MGUtilities.hasConnection(SellerActivity.this) && MainActivity.location != null) {
					String strUrl = String.format("%s?api_key=%s&lat=%f&lon=%f&radius=%d&user_id=%d",
							Config.GET_ITEMS_URL,
							Config.API_KEY,
							MainActivity.location.getLatitude(),
							MainActivity.location.getLongitude(),
							radius,
							user.getUser_id());

					Log.e("URL", strUrl);
					try {
						DataParser parser = new DataParser();
						data = parser.getData(strUrl);
						if (data == null)
							return;

						userItems = new ArrayList<Item>();
						if (data.getUser_items() != null && data.getUser_items().size() > 0) {
							for (Item item : data.getUser_items()) {
								q.deleteItem(item.getItem_id());
								q.insertItem(item);
								if (item.getPhotos() != null) {
									for (Photo photo : item.getPhotos()) {
										q.deletePhoto(photo.getPhoto_id());
										q.insertPhoto(photo);
									}
								}
								if (item.getUser() != null) {
									q.deleteUser(item.getUser().getUser_id());
									q.insertUser(item.getUser());
								}

								userItems.add(item);
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		task.execute();
	}
}
