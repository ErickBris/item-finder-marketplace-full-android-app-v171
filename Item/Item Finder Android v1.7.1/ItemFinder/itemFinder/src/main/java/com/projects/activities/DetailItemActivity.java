package com.projects.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.config.Config;
import com.config.UIConfig;
import com.db.DbHelper;
import com.db.Queries;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.libraries.datetime.TimeAgo;
import com.libraries.html.MGHtml;
import com.libraries.imageview.RoundedImageView;
import com.libraries.refreshlayout.SwipeRefreshActivity;
import com.libraries.slider.MGSlider;
import com.libraries.slider.MGSliderAdapter;
import com.libraries.twitter.TwitterApp;
import com.libraries.twitter.TwitterApp.TwitterAppListener;
import com.libraries.utilities.MGUtilities;
import com.models.Category;
import com.models.Favorite;
import com.models.Item;
import com.models.Photo;
import com.models.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.projects.itemfinder.MainActivity;
import com.projects.itemfinder.R;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import twitter4j.auth.AccessToken;

public class DetailItemActivity extends SwipeRefreshActivity implements OnClickListener {

	private DisplayImageOptions options;
	private Item item;
	private Queries q;
	private SQLiteDatabase db;
	private TwitterApp mTwitter;
	private boolean isPending = false;
    private ArrayList<Photo> photos;
	private boolean willEdit = false;
    private boolean updateItemWhenViewResumes = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		setContentView(R.layout.fragment_item_detail);
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
		item = (Item) this.getIntent().getSerializableExtra("item");
		willEdit = this.getIntent().getBooleanExtra("willEdit", false);
		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(UIConfig.SLIDER_PLACEHOLDER)
			.showImageForEmptyUri(UIConfig.SLIDER_PLACEHOLDER)
			.showImageOnFail(UIConfig.SLIDER_PLACEHOLDER)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();

        mTwitter = new TwitterApp(this, twitterAppListener);
        showSwipeProgress();
        updateItemDetails(true);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

    @Override
    public  void onResume() {
        super.onResume();
        if(updateItemWhenViewResumes) {
            updateItemWhenViewResumes = false;
            updateItemDetails(false);
        }
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        // Handle action bar actions click
        switch (item.getItemId()) {
	        default:
	        	finish();
	            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        // if nav drawer is opened, hide the action items
        return super.onPrepareOptionsMenu(menu);
    }

	private void updateItemDetails(boolean updateUserDetails) {
		final LinearLayout linearEdit = (LinearLayout) findViewById(R.id.linearEdit);
		linearEdit.setVisibility(View.GONE);
		if(willEdit) {
			linearEdit.setVisibility(View.VISIBLE);
			linearEdit.setOnClickListener(this);
		}

		photos = q.getPhotosByItemId(item.getItem_id());
		MGSlider slider = (MGSlider) findViewById(R.id.sliderImages);
		slider.setMaxSliderThumb(photos.size());
		MGSliderAdapter adapter = new MGSliderAdapter(
				R.layout.slider_item_entry, photos.size(), photos.size());

		adapter.setOnMGSliderAdapterListener(new MGSliderAdapter.OnMGSliderAdapterListener() {

            @Override
            public void onOnMGSliderAdapterCreated(MGSliderAdapter adapter, View v, final int position) {
                // TODO Auto-generated method stub
                Photo p = photos.get(position);
                ImageView imgViewThumb = (ImageView) v.findViewById(R.id.imgViewThumb);
                if (p != null) {
                    MainActivity.getImageLoader().displayImage(p.getPhoto_url(), imgViewThumb, options);
                } else {
                    imgViewThumb.setImageResource(UIConfig.SLIDER_PLACEHOLDER);
                }
                imgViewThumb.setTag(position);
                imgViewThumb.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent i = new Intent(DetailItemActivity.this, ImageViewerActivity.class);
                        i.putExtra("photoList", photos);
                        i.putExtra("index", position);
                        startActivity(i);
                    }
                });
            }
        });

		slider.setOnMGSliderListener(new MGSlider.OnMGSliderListener() {
            @Override
            public void onItemMGSliderToView(MGSlider slider, int pos) { }

            @Override
            public void onItemMGSliderViewClick(AdapterView<?> adapterView, View v, int pos, long resid) { }

            @Override
            public void onItemThumbCreated(MGSlider slider, ImageView imgView, int pos) {
                imgView.setImageResource(R.drawable.slider_thumb_unselected);
            }

            @Override
            public void onAllItemThumbCreated(MGSlider slider, LinearLayout linearLayout) {
                FrameLayout frameSlider = (FrameLayout) findViewById(R.id.frameSliderThumb);
                frameSlider.addView(linearLayout);
            }

            @Override
            public void onItemThumbSelected(MGSlider slider, ImageView[] buttonPoint, ImageView imgView, int pos) {

                if(buttonPoint == null)
                    return;

                for (int x = 0; x < buttonPoint.length; x++) {
                    buttonPoint[x].setImageResource(R.drawable.slider_thumb_unselected);
                }

                imgView.setImageResource(R.drawable.slider_thumb_selected);
                slider.setSlideAtIndex(pos);
            }

            @Override
            public void onItemPageScrolled(MGSlider slider, ImageView[] buttonPoint, int pos) {

                if(buttonPoint == null)
                    return;

                if (buttonPoint.length > 0) {
                    for (int x = 0; x < buttonPoint.length; x++) {
                        buttonPoint[x].setImageResource(R.drawable.slider_thumb_unselected);
                    }
                    buttonPoint[pos].setImageResource(R.drawable.slider_thumb_selected);
                }
            }
        });

        int photoCount = photos.size();
		slider.setOffscreenPageLimit(photoCount - 1);
		slider.setAdapter(adapter);
		slider.setActivity(this);

        if(photoCount > 1)
            slider.showThumb();

        Spanned spannedStr = null;
        TextView tvItemName = (TextView) findViewById(R.id.tvItemName);
        spannedStr = Html.fromHtml(item.getItem_name());
        spannedStr = Html.fromHtml(spannedStr.toString());
        tvItemName.setText(spannedStr.toString());

        TextView tvItemType = (TextView) findViewById(R.id.tvItemType);
        if(item.getItem_type() == 0)
            tvItemType.setText(R.string.new_item);
        else
            tvItemType.setText(R.string.used);

        TextView tvItemPrice = (TextView) findViewById(R.id.tvItemPrice);
		String strPrice = String.format("%s %s", Html.fromHtml(item.getItem_currency()), item.getItem_price());
        tvItemPrice.setText(strPrice);

        TextView tvItemDatePosted = (TextView) findViewById(R.id.tvItemDatePosted);
        tvItemDatePosted.setText(TimeAgo.toDuration(item.getCreated_at() * 1000));

        TextView tvItemDistance = (TextView) findViewById(R.id.tvItemDistance);
        if(MainActivity.location != null) {
            Location locStore = new Location("Store");
            locStore.setLatitude(item.getLat());
            locStore.setLongitude(item.getLon());

            float distance = locStore.distanceTo(MainActivity.location) / 1000;
            item.setDistance(distance);
            String distanceStr = String.format("%.2f %s",
                    distance,
                    MGUtilities.getStringFromResource(this, R.string.km));
            tvItemDistance.setText(distanceStr);
        }

        TextView tvItemCategory = (TextView) findViewById(R.id.tvItemCategory);
        Category category = q.getCategoryByCategoryId(item.getCategory_id());
        if(category == null)
            tvItemCategory.setText(R.string.no_value);
        else
            tvItemCategory.setText(Html.fromHtml(category.getCategory()));

		String strDesc = item.getItem_desc();
        strDesc = MGHtml.decodeText(strDesc);
        TextView tvItemDesc = (TextView) findViewById(R.id.tvItemDesc);
        tvItemDesc.setText(Html.fromHtml(strDesc.toString()));

        TextView tvItemSellerName = (TextView) findViewById(R.id.tvItemSellerName);
        TextView tvItemUserName = (TextView) findViewById(R.id.tvItemUserName);
        TextView tvItemJoined = (TextView) findViewById(R.id.tvItemJoined);

        final User user = q.getUserByUserId(item.getUser_id());
        tvItemSellerName.setText(R.string.no_value);
        tvItemUserName.setText(R.string.no_value);
        tvItemJoined.setText(R.string.no_value);

        if(user != null) {
            if(user.getFull_name() != null && user.getFull_name().length() > 0)
                tvItemSellerName.setText(Html.fromHtml(user.getFull_name()));

            if(user.getUsername() != null && user.getUsername().length() > 0)
                tvItemUserName.setText(user.getUsername());

            long millis = user.getCreated_at() * 1000;
            tvItemJoined.setText(TimeAgo.toDuration(millis));
        }

		if(updateUserDetails) {
            RoundedImageView imgViewPhoto = (RoundedImageView) findViewById(R.id.imgViewThumb);
            imgViewPhoto.setCornerRadius(R.dimen.corner_radius_detail_seller);
            imgViewPhoto.setBorderWidth(UIConfig.BORDER_WIDTH);
            imgViewPhoto.setBorderColor(getResources().getColor(UIConfig.THEME_BLACK_COLOR));

            if(user.getThumb_url() != null) {
                MainActivity.getImageLoader().displayImage(user.getThumb_url(), imgViewPhoto, options);
            }
        }

        LinearLayout linearSellerInfo = (LinearLayout) findViewById(R.id.linearSellerInfo);
        linearSellerInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailItemActivity.this, SellerActivity.class);
                i.putExtra("user", user);
                startActivity(i);
            }
        });

        LinearLayout linearItemShareToFacebook = (LinearLayout) findViewById(R.id.linearItemShareToFacebook);
        linearItemShareToFacebook.setOnClickListener(this);

        LinearLayout linearItemShareToTwitter = (LinearLayout) findViewById(R.id.linearItemShareToTwitter);
        linearItemShareToTwitter.setOnClickListener(this);

        LinearLayout linearItemMessageSeller = (LinearLayout) findViewById(R.id.linearItemMessageSeller);
        linearItemMessageSeller.setOnClickListener(this);

        LinearLayout linearItemEmailSeller = (LinearLayout) findViewById(R.id.linearItemEmailSeller);
        linearItemEmailSeller.setOnClickListener(this);

        LinearLayout linearItemCallSeller = (LinearLayout) findViewById(R.id.linearItemCallSeller);
        linearItemCallSeller.setOnClickListener(this);

        ImageView imgViewGallery = (ImageView) findViewById(R.id.imgViewGallery);
        imgViewGallery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(photos != null && photos.size() > 0) {
                    Intent i = new Intent(DetailItemActivity.this, ImageViewerActivity.class);
                    i.putExtra("photoList", photos);
                    startActivity(i);
                }
                else {
                    MGUtilities.showAlertView(
                            DetailItemActivity.this,
                            R.string.action_error,
                            R.string.no_image_to_display);
                }
            }
        });

        TextView tvGalleryCount = (TextView) findViewById(R.id.tvGalleryCount);
        tvGalleryCount.setText("" + photos.size());

        ImageView imgViewFeatured = (ImageView) findViewById(R.id.imgViewFeatured);
        imgViewFeatured.setVisibility(View.VISIBLE);
        if(item.getFeatured() == 0)
            imgViewFeatured.setVisibility(View.INVISIBLE);

		ToggleButton toggleButtonFave = (ToggleButton) findViewById(R.id.toggleButtonFave);
		toggleButtonFave.setOnClickListener(this);

		Favorite fave = q.getFavoriteByItemId(item.getItem_id());
		toggleButtonFave.setChecked(true);
		if(fave == null)
			toggleButtonFave.setChecked(false);

		hideSwipeProgress();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.linearItemCallSeller:
				call();
				break;
			case R.id.linearItemEmailSeller:
				email();
				break;
			case R.id.linearItemShareToFacebook:
				shareFB();
				break;
			case R.id.linearItemShareToTwitter:
				if(isPending)
					return;

				loginToTwitter();
				break;
			case R.id.linearItemMessageSeller:
				sms();
				break;
			case R.id.toggleButtonFave:
				checkFave(v);
				break;
			case R.id.linearEdit:
				Intent i = new Intent(this, EditItemActivity.class);
				i.putExtra("item", item);
				startActivityForResult(i, Config.EDIT_ITEM_REQUEST_CODE);
				break;
		}
	}

	private void checkFave(View view) {
		Favorite fave = q.getFavoriteByItemId(item.getItem_id());
		if(fave != null) {
			q.deleteFavorite(item.getItem_id());
			((ToggleButton) view).setChecked(false);
		}
		else {
			fave = new Favorite();
			fave.setItem_id(item.getItem_id());
			q.insertFavorite(fave);
			((ToggleButton) view).setChecked(true);
		}
	}

	private void call() {
        User user = q.getUserByUserId(item.getUser_id());
		if( user.getPhone_no() == null || user.getPhone_no().length() == 0 ) {
			MGUtilities.showAlertView(
                    this,
                    R.string.action_error,
                    R.string.no_phone_no);
			return;
		}
		PackageManager pm = this.getBaseContext().getPackageManager();
	    boolean canCall = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
	    if(!canCall) {
			MGUtilities.showAlertView(
					this,
					R.string.action_error,
					R.string.cannot_proceed);
			return;
		}
		String phoneNo = user.getPhone_no().replaceAll("[^0-9]", "");
		String uri = "tel:" + phoneNo;
		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse(uri));

        try {
            startActivity(intent);
        }
        catch (SecurityException e) {
            Toast.makeText(this, R.string.call_request_not_granted, Toast.LENGTH_SHORT).show();
        }
	}

	private void email() {
        User user = q.getUserByUserId(item.getUser_id());
		if(user.getEmail() == null || user.getEmail().length() == 0) {
			MGUtilities.showAlertView(
					this, 
					R.string.action_error, 
					R.string.cannot_proceed);
			return;
		}
		
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ user.getEmail() } );
		emailIntent.putExtra(Intent.EXTRA_SUBJECT,
				MGUtilities.getStringFromResource(this, R.string.email_subject));
		emailIntent.putExtra(Intent.EXTRA_TEXT, 
				MGUtilities.getStringFromResource(this, R.string.email_body) );
		emailIntent.setType("message/rfc822");
		
		startActivity(Intent.createChooser(emailIntent,
                MGUtilities.getStringFromResource(this, R.string.choose_email_client)));
	}
	
	private void sms() {
        User user = q.getUserByUserId(item.getUser_id());
		if( user.getSms_no() == null || user.getSms_no().length() == 0 ) {
			MGUtilities.showAlertView(
					this, 
					R.string.action_error, 
					R.string.no_sms_no);
			return;
		}
		PackageManager pm = this.getBaseContext().getPackageManager();
	    boolean canSMS = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
		if(!canSMS) {
			MGUtilities.showAlertView(
					this,
					R.string.action_error,
					R.string.handset_not_supported);
			return;
		}

		String smsNo = user.getSms_no().replaceAll("[^0-9]", "");
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.setData(Uri.parse("smsto:" + Uri.encode(smsNo)));
		startActivity(smsIntent);
	}

	private void shareFB() {
		Photo p = photos != null && photos.size() > 0 ? photos.get(0) : null;
		if (FacebookDialog.canPresentShareDialog(this,
				FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
			// Publish the post using the Share Dialog
//			FacebookDialog.ShareDialogBuilder shareDialog = new FacebookDialog.ShareDialogBuilder(this);
//			shareDialog.setLink(item.getItem_name());
//			if(p != null)
//				shareDialog.setPicture(p.getThumb_url());
//
//			shareDialog.build().present();

			FacebookDialog shareDialog = null;
			String itemDesc = String.format("%s: %s %s", item.getItem_name(), item.getItem_currency(), item.getItem_price());
			if(p != null) {
				shareDialog =
						new FacebookDialog.ShareDialogBuilder(this)
								.setLink(Config.SERVER_URL_DEFAULT_PAGE_FOR_FACEBOOK)
								.setPicture(p.getThumb_url())
								.setDescription(itemDesc)
								.build();
			}
			else {
				shareDialog =
						new FacebookDialog.ShareDialogBuilder(this)
								.setLink(Config.SERVER_URL_DEFAULT_PAGE_FOR_FACEBOOK)
								.setDescription(itemDesc)
								.build();
			}
			shareDialog.present();
		}
		else {
			// Fallback. For example, publish the post using the Feed Dialog
			Bundle params = new Bundle();
			params.putString("link", item.getItem_name());
			if(p != null)
				params.putString("picture", p.getThumb_url());

			WebDialog feedDialog = (
					new WebDialog.FeedDialogBuilder(this, Session.getActiveSession(), params))
					.setOnCompleteListener(new WebDialog.OnCompleteListener() {

						@Override
						public void onComplete(Bundle values, FacebookException error) {
							// TODO Auto-generated method stub
							if (error == null) {
								// When the story is posted, echo the success
								// and the post Id.
								final String postId = values.getString("post_id");
								if (postId != null) {
									// publish successful
								} else {
									// User clicked the Cancel button
									MGUtilities.showAlertView(
											DetailItemActivity.this,
											R.string.publish_error,
											R.string.publish_cancelled);
								}
							} else if (error instanceof FacebookOperationCanceledException) {
								// User clicked the "x" button
								MGUtilities.showAlertView(
										DetailItemActivity.this,
										R.string.publish_error,
										R.string.publish_cancelled);
							} else {
								MGUtilities.showAlertView(
										DetailItemActivity.this,
										R.string.network_error,
										R.string.problems_encountered_facebook);
							}
						}

					})
					.build();
			feedDialog.show();
		}
	}

	@SuppressLint("InflateParams") 
	private void postToTwitter() {
		isPending = false;
		LayoutInflater inflate = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflate.inflate(R.layout.twitter_dialog, null);
		// create dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setIcon(android.R.drawable.ic_dialog_info);
    	builder.setView(view);
    	builder.setTitle("Twitter Status");
    	builder.setCancelable(false);
    	
    	final EditText txtStatus = (EditText) view.findViewById(R.id.txtStatus);
    	txtStatus.setText("");
    	// set dialog button
    	builder.setPositiveButton("Tweet!", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   String tweet = txtStatus.getText().toString().trim();
	        	   InputStream is = getImage();
	        	   if(is == null)
	        		   mTwitter.updateStatus(tweet);
	        	   else
	        		   mTwitter.updateStatusWithLogo(is, tweet);
	           }
	       })
	       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			   public void onClick(DialogInterface dialog, int id) {
				   dialog.cancel();
			   }
		   });
    	// show dialog
		AlertDialog alert = builder.create();
    	alert.show();
	}
	
	public InputStream getImage() {
		Photo p = q.getPhotoByItemId(item.getItem_id());
		ImageView imgViewPhoto = (ImageView) findViewById(R.id.imgViewPhoto);
		if(p == null)
			return null;

		BitmapDrawable drawable = (BitmapDrawable)imgViewPhoto.getDrawable();
		Bitmap bitmap = drawable.getBitmap();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
		bitmap.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos); 
		byte[] bitmapdata = bos.toByteArray();
		ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
		return bs;
	}

	// FACEBOOK
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart()  {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == Config.EDIT_ITEM_REQUEST_CODE) {
			if(resultCode == RESULT_OK) {
                item = q.getItemByItemId(item.getItem_id());
				updateItemWhenViewResumes = true;
			}
		}
        else {
//			Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		}
    }

    // ###############################################################################################
    // TWITTER INTEGRATION METHODS
    // ###############################################################################################
    public void loginToTwitter() {
        if (mTwitter.hasAccessToken() == true) {
            try {
                postToTwitter();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            isPending = true;
            mTwitter.loginToTwitter();
        }
    }

    TwitterAppListener twitterAppListener = new TwitterAppListener() {

        @Override
        public void onError(String value)  {
            // TODO Auto-generated method stub
            Log.e("TWITTER ERROR**", value);
        }

        @Override
        public void onComplete(AccessToken accessToken) {
            // TODO Auto-generated method stub
            DetailItemActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    postToTwitter();
                }
            });
        }
    };

}
