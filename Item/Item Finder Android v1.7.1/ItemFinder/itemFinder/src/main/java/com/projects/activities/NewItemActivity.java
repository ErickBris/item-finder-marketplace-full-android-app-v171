package com.projects.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.config.Config;
import com.config.UIConfig;
import com.db.DbHelper;
import com.db.Queries;
import com.libraries.directories.Directory;
import com.libraries.asynctask.MGAsyncTask;
import com.libraries.bitmap.MGImageUtils;
import com.libraries.dataparser.DataParser;
import com.libraries.html.MGHtml;
import com.libraries.imageview.RoundedImageView;
import com.libraries.refreshlayout.SwipeRefreshActivity;
import com.libraries.scroller.MGScroller;
import com.libraries.usersession.UserAccessSession;
import com.libraries.usersession.UserSession;
import com.libraries.utilities.MGUtilities;
import com.models.Category;
import com.models.Data;
import com.models.Item;
import com.models.Photo;
import com.models.Status;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.projects.itemfinder.MainActivity;
import com.projects.itemfinder.R;
import org.apache.http.message.BasicNameValuePair;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class NewItemActivity extends SwipeRefreshActivity implements OnClickListener {

	private DisplayImageOptions options;
	private Queries q;
	private SQLiteDatabase db;
	private Category category = null;
	private ArrayList<BasicNameValuePair> params;
	private ArrayList<Uri> uris;
	private ArrayList<String> urisList;
	private static final int CAMERA_PIC_REQUEST_DTR = 88;
	private String pfTime = "";
	private Directory dir;
	private String path = "";
	private final int RESULT_LOAD_IMAGE = 1;
	private String dtrImagePath = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		setContentView(R.layout.fragment_add_item);
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

		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(UIConfig.SLIDER_PLACEHOLDER)
			.showImageForEmptyUri(UIConfig.SLIDER_PLACEHOLDER)
			.showImageOnFail(UIConfig.SLIDER_PLACEHOLDER)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();

		path = Environment.getExternalStorageDirectory() + "/" + Config.SD_CARD_PATH;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			dir = new Directory(path);
			if(!dir.isExist()){
				dir.createDir();
				dir.createSubDirCameraTaken();
				dir.createSubDirData();
			}
		}

		uris = new ArrayList<Uri>();
		urisList = new ArrayList<String>();

		Button btnMarkSold = (Button) findViewById(R.id.btnMarkSold);
		btnMarkSold.setVisibility(View.GONE);

		Button btnUnPublish = (Button) findViewById(R.id.btnUnPublish);
		btnUnPublish.setVisibility(View.GONE);

		Button btnSend = (Button) findViewById(R.id.btnSend);
		btnSend.setOnClickListener(this);

		LinearLayout linearCategory = (LinearLayout) findViewById(R.id.linearCategory);
		linearCategory.setOnClickListener(this);

		final EditText txtItemDesc = (EditText) findViewById(R.id.txtItemDesc);
		final TextView tvMaxCharCount = (TextView) findViewById(R.id.tvMaxCharCount);
		String charsLeft = String.format("%d %s",
				Config.MAX_CHARS_ITEM_DESCRIPTION,
				MGUtilities.getStringFromResource(this, R.string.chars_left));

		tvMaxCharCount.setText(charsLeft);
		InputFilter filter = new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end,
									   Spanned dest, int dstart, int dend) {
                int currCount = txtItemDesc.getText().toString().length();
                if(currCount > Config.MAX_CHARS_ITEM_DESCRIPTION)
					return "";

				String charsLeft = String.format("%d %s",
						Config.MAX_CHARS_ITEM_DESCRIPTION - txtItemDesc.getText().toString().length(),
						MGUtilities.getStringFromResource(NewItemActivity.this, R.string.chars_left));
				tvMaxCharCount.setText(charsLeft);
				return source;
			}
		};
		txtItemDesc.setFilters(new InputFilter[]{filter});

		setScroller();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        // Handle action bar actions click
        switch (item.getItemId()) {
	        default:
				showAlertBeforeExit(false);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.btnSend:
				if(!MGUtilities.hasConnection(this)) {
					MGUtilities.showAlertView(
							this,
							R.string.network_error,
							R.string.no_network_connection);
					return;
				}
				formatData();
				break;

			case R.id.linearCategory:
				Intent i = new Intent(this, CategorySelectionActivity.class);
				startActivityForResult(i, Config.CATEGORY_SELECTION_REQUEST_CODE);
				break;
		}
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
		if (requestCode == Config.CATEGORY_SELECTION_REQUEST_CODE && resultCode == RESULT_OK) {
			category = (Category) data.getSerializableExtra("category");
			TextView tvCategory = (TextView) findViewById(R.id.tvCategory);
			tvCategory.setText(category.getCategory());
		}

		if (requestCode == CAMERA_PIC_REQUEST_DTR && resultCode == RESULT_OK) {
			String newPath = String.format("%s%s", dir.getPFCameraTakenPath(), dtrImagePath);
			File f = new File(newPath);
			Uri uriFile = Uri.fromFile(f);
			uris.add(uriFile);
			urisList.add(newPath);
			setScroller();
		}

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			uris.add(selectedImage);
			urisList.add(MGImageUtils.getRealPathFromURI(selectedImage, NewItemActivity.this));
			setScroller();
		}
    }

	private void setScroller() {
		MGScroller scrollerUsers = (MGScroller) findViewById(R.id.scrollerPhotos);
		scrollerUsers.setOnScrollerListener(new MGScroller.ScrollerListener() {

			@Override
			public void onScrollerSelected(View v, int position) {
				// TODO Auto-generated method stub
				if (Integer.parseInt(v.getTag().toString()) == Config.TAG_CAMERA) {
					captureDTR();
				} else if (Integer.parseInt(v.getTag().toString()) == Config.TAG_PHOTO_PIC) {
					getPicture();
				} else {
					deletePhoto(position);
				}
			}

			@Override
			public void onScrollerCreated(MGScroller adapter, View v, int position) {
				// TODO Auto-generated method stub
				if (Integer.parseInt(v.getTag().toString()) == Config.TAG_CAMERA) {
					ImageView imgView = (ImageView) v.findViewById(R.id.imageViewButton);
					imgView.setImageResource(R.drawable.button_add_image_capture);
				} else if (Integer.parseInt(v.getTag().toString()) == Config.TAG_PHOTO_PIC) {
					ImageView imgView = (ImageView) v.findViewById(R.id.imageViewButton);
					imgView.setImageResource(R.drawable.button_add_image);
				} else {
					RoundedImageView imgView = (RoundedImageView) v.findViewById(R.id.imageViewButton);
					imgView.setImageBitmap(null);
					imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);

					try {
						Uri uri = uris.get(position);
						Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
						imgView.setImageBitmap(bitmap);
						imgView.setBorderWidth(UIConfig.BORDER_WIDTH);
						imgView.setBorderColor(getResources().getColor(UIConfig.THEME_MAIN_COLOR));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onScrollerFinishCreated(MGScroller scroller) {
				// TODO Auto-generated method stub
				if (uris.size() < Config.MAX_ITEM_UPLOAD_PHOTO) {
					addPhotoAndCamera(scroller);
				}
			}
		});
		scrollerUsers.createScroller(uris.size(), R.layout.scroller_entry);
	}

	private void addPhotoAndCamera(MGScroller scroller) {
		scroller.insertViewWithTag(R.layout.scroller_entry2, Config.TAG_CAMERA);
		scroller.insertViewWithTag(R.layout.scroller_entry2, Config.TAG_PHOTO_PIC);
	}

	private void deletePhoto(final int index) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.delete_image);
		builder.setMessage(R.string.delete_image_details)
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						urisList.remove(index);
						uris.remove(index);
						setScroller();
					}
				})
				.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private void formatData() {
		if(params == null)
			params = new ArrayList<BasicNameValuePair>();

		params.clear();
		if(category == null) {
			MGUtilities.showAlertView(this, R.string.category_selection_error, R.string.please_select_category);
			return;
		}

		EditText txtItemName = (EditText) findViewById(R.id.txtItemName);
		EditText txtItemPrice = (EditText) findViewById(R.id.txtItemPrice);
		EditText txtItemDesc = (EditText) findViewById(R.id.txtItemDesc);
		EditText txtItemCurrency = (EditText) findViewById(R.id.txtItemCurrency);

		String name = txtItemName.getText().toString().trim();
		String price = txtItemPrice.getText().toString().trim();
		String desc = txtItemDesc.getText().toString().trim();
		String currency = txtItemCurrency.getText().toString().trim();

		if(name.length() == 0) {
			MGUtilities.showAlertView(this, R.string.error_title_item_name, R.string.error_details_item_name);
			return;
		}

		if(price.length() == 0) {
			MGUtilities.showAlertView(this, R.string.error_title_price, R.string.error_details_price);
			return;
		}

		if(desc.length() == 0) {
			MGUtilities.showAlertView(this, R.string.error_title_desc, R.string.error_details_desc);
			return;
		}

		if(uris.size() == 0) {
			MGUtilities.showAlertView(this, R.string.error_title_photos, R.string.error_details_photos);
			return;
		}

		if(MainActivity.location == null) {
			MGUtilities.showAlertView(this,
					R.string.error_title_location,
					R.string.error_details_location);
			return;
		}

		String lat = String.format("%f", MainActivity.location.getLatitude());
		String lon = String.format("%f", MainActivity.location.getLongitude());

		RadioButton radioNew = (RadioButton) findViewById(R.id.radioNew);
		String itemType = radioNew.isChecked() ? "0" : "1";
		String strDesc = MGHtml.encodeText(desc.toString());

		if(currency.isEmpty())
			currency = Config.DEFAULT_CURRENCY_IF_EMPTY;

		SpannableString spannedName = new SpannableString(name);
		SpannableString spannedDesc = new SpannableString(strDesc);
		SpannableString spannedCurrency = new SpannableString(currency);

		params.add(new BasicNameValuePair("item_currency", Html.toHtml(spannedCurrency)));
		params.add(new BasicNameValuePair("item_name", Html.toHtml(spannedName)));
		params.add(new BasicNameValuePair("item_desc", Html.toHtml(spannedDesc)));
		params.add(new BasicNameValuePair("item_price", price));
		params.add(new BasicNameValuePair("lat", lat));
		params.add(new BasicNameValuePair("lon", lon));
		params.add(new BasicNameValuePair("item_type", itemType));
		params.add(new BasicNameValuePair("category_id", String.valueOf(category.getCategory_id())));

		UserAccessSession userAccess = UserAccessSession.getInstance(this);
		UserSession userSession = userAccess.getUserSession();
		params.add(new BasicNameValuePair("user_id", String.valueOf(userSession.getUser_id())));
		params.add(new BasicNameValuePair("login_hash", userSession.getLogin_hash()));
		params.add(new BasicNameValuePair("api_key", Config.API_KEY));
		syncToServer(true);
	}

	private void syncToServer(final boolean isUploadingPhoto) {

		MGAsyncTask task = new MGAsyncTask(this);
		task.setMGAsyncTaskListener(new MGAsyncTask.OnMGAsyncTaskListener() {

			@Override
			public void onAsyncTaskProgressUpdate(MGAsyncTask asyncTask) { }

			@Override
			public void onAsyncTaskPreExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				String msg = MGUtilities.getStringFromResource(
						getApplicationContext(),
						R.string.syncing_data_to_server);

				if(isUploadingPhoto) {
					msg = MGUtilities.getStringFromResource(
							getApplicationContext(),
							R.string.uploading_photo_to_server);
				}
				asyncTask.dialog.setMessage(msg);
			}

			@Override
			public void onAsyncTaskPostExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				Toast.makeText(NewItemActivity.this, R.string.item_added, Toast.LENGTH_SHORT).show();
				asyncTask.cancel(true);
                Intent i = new Intent();
                setResult(RESULT_OK, i);
				finish();
			}

			@Override
			public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				syncPhotoToServer();
			}
		});
		task.execute();
	}

	private void syncPhotoToServer() {
		try {
			Data data = DataParser.getJSONFromUrlWithPostRequestUsingData(Config.SYNC_ITEM_JSON_URL, params);
			if(data != null && data.getItem_details() != null) {

				Item item = data.getItem_details();
                Item itemUser = item.getItem_info();
				if(item.getUser() != null) {
					q.deleteUser(item.getUser().getUser_id());
					q.insertUser(item.getUser());
				}

//				if(item.getCategory() != null) {
//					q.deleteCategory(item.getCategory().getCategory_id());
//					q.insertCategory(item.getCategory());
//				}

				if(itemUser != null)
					q.insertItem(itemUser);

				if(uris.size() > 0) {
					for(String uriStr : urisList) {
						String strURL = String.format(
								"%s?item_id=%d",
								Config.INSERT_ITEM_PHOTO_ANDROID_JSON_URL,
                                itemUser.getItem_id());

						File f = new File(uriStr);
						Data dataPhoto = DataParser.uploadFileWithParams(strURL, null, f);
						Status status = dataPhoto.getStatus();
						if(status != null) {
							if(status.getStatus_code() == Config.STATUS_SUCCESS) {
								if(dataPhoto.getPhotos() != null)
									for(Photo p : dataPhoto.getPhotos()) {
										q.insertPhoto(p);
									}
							}
							else {
								Log.e("LOG", status.getStatus_text());
							}
						}
						else {
							Log.e("LOG", "failed upload");
						}
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void captureDTR() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		pfTime = String.valueOf (System.currentTimeMillis());
			dtrImagePath = String.format("%s.jpg", pfTime);
		//Grant permission to the camera activity to write the photo.
		cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
		//saving if there is EXTRA_OUTPUT
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(dir.getPFCameraTakenPath(), dtrImagePath)));

		startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST_DTR);
	}

	private void getPicture() {
		Intent i = new Intent(
				Intent.ACTION_PICK,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}


	public void showAlertBeforeExit(final boolean isBackPressed) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(R.string.exit_item);
		alert.setMessage(R.string.discard_changes);
		alert.setPositiveButton(this.getResources().getString(R.string.yes),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(isBackPressed)
							NewItemActivity.super.onBackPressed();
						else
							finish();
					}
				});
		alert.setNegativeButton(this.getResources().getString(R.string.no),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		alert.create();
		alert.show();
	}

	@Override
	public void onBackPressed() {
		showAlertBeforeExit(true);
	}
}
