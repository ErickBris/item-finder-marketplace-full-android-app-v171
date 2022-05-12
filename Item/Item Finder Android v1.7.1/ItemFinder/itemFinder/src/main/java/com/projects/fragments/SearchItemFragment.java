package com.projects.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.libraries.dataparser.DataParser;
import com.libraries.usersession.UserAccessSession;
import com.models.Data;
import com.models.Photo;
import com.projects.activities.CategorySelectionActivity;
import com.projects.activities.SearchResultActivity;
import com.config.Config;
import com.db.Queries;
import com.libraries.asynctask.MGAsyncTask;
import com.libraries.asynctask.MGAsyncTask.OnMGAsyncTaskListener;
import com.libraries.utilities.MGUtilities;
import com.models.Category;
import com.models.Item;
import com.models.User;
import com.projects.itemfinder.MainActivity;
import com.projects.itemfinder.MainActivity.OnLocatonListener;
import com.projects.itemfinder.R;

import org.apache.http.message.BasicNameValuePair;
import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SearchItemFragment extends Fragment implements OnClickListener, OnLocatonListener{

	private View viewInflate;
	private EditText txtKeywords;
	private EditText txtUSerNameSellerName;
	private RangeSeekBar rangeBarPrice;
	private RangeSeekBar rangeBarRadius;
	private Spinner spinnerSortBy;
	private ToggleButton toggleButtonNearby;
	private ImageView imgViewCategory;
	private TextView tvCategory;
	private MGAsyncTask task;
    private Category selectecCategory;
    Queries q;
    boolean _showRadius;

	public SearchItemFragment() { }
	
	@Override
    public void onDestroyView()  {
        super.onDestroyView();
        if(task != null)
        	task.cancel(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		viewInflate = inflater.inflate(R.layout.fragment_item_search, null);
		return viewInflate;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
        MainActivity main = (MainActivity) getActivity();
        q = main.getQueries();

		Button btnSearch = (Button) viewInflate.findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(this);

		txtUSerNameSellerName = (EditText) viewInflate.findViewById(R.id.txtSellerNameUserName);
		txtKeywords = (EditText) viewInflate.findViewById(R.id.txtKeywords);
		toggleButtonNearby = (ToggleButton) viewInflate.findViewById(R.id.toggleButtonNearby);
		toggleButtonNearby.setOnClickListener(this);

		rangeBarRadius = (RangeSeekBar) viewInflate.findViewById(R.id.rangeBarRadius);
		rangeBarRadius.setNotifyWhileDragging(true);
        rangeBarRadius.setEnabled(false);
        rangeBarRadius.setRangeValues(Config.SEARCH_RADIUS_MINIMUM_KILOMETERS, Config.SEARCH_RADIUS_MAXIMUM_KILOMETERS);
        rangeBarRadius.setSelectedMinValue(Config.SEARCH_RADIUS_MINIMUM_KILOMETERS);
        rangeBarRadius.setSelectedMaxValue(Config.SEARCH_RADIUS_KILOMETERS_DEFAULT);
		rangeBarRadius.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                updateRadius();
                Log.e(minValue.toString(), maxValue.toString());
            }
        });
        updateRadius();
        updateToggleButtonNearby();

		rangeBarPrice = (RangeSeekBar) viewInflate.findViewById(R.id.rangeBarPrice);
		rangeBarPrice.setNotifyWhileDragging(true);
        rangeBarPrice.setRangeValues(Config.SEARCH_MINIMUM_PRICE, Config.SEARCH_MAXIMUM_PRICE);
        rangeBarPrice.setSelectedMinValue(Config.SEARCH_MINIMUM_PRICE_DEFAULT);
        rangeBarPrice.setSelectedMaxValue(Config.SEARCH_MAXIMUM_PRICE_DEFAULT);
		rangeBarPrice.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                Log.e(minValue.toString(), maxValue.toString());
                updatePrice();
            }
        });
        updatePrice();

		tvCategory = (TextView) viewInflate.findViewById(R.id.tvCategory);
		imgViewCategory = (ImageView) viewInflate.findViewById(R.id.imgViewCategory);
		imgViewCategory.setOnClickListener(this);

		ArrayList<String> sortBy = new ArrayList<String>();
		sortBy.add(getActivity().getResources().getString(R.string.no_sorting));
		sortBy.add(getActivity().getResources().getString(R.string.price));
		sortBy.add(getActivity().getResources().getString(R.string.date_posted));
		sortBy.add(getActivity().getResources().getString(R.string.name));
		sortBy.add(getActivity().getResources().getString(R.string.distance));
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item, sortBy);
         
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerSortBy = (Spinner) viewInflate.findViewById(R.id.spinnerSortBy);
		spinnerSortBy.setAdapter(dataAdapter);
	}

    private void updateRadius() {
        String strSeekVal = String.format("%s: %d %s",
                MGUtilities.getStringFromResource(getActivity(), R.string.radius),
                rangeBarRadius.getSelectedMaxValue(),
                MGUtilities.getStringFromResource(getActivity(), R.string.km));

        final TextView tvRadiusText = (TextView) viewInflate.findViewById(R.id.tvRadiusText);
        tvRadiusText.setText(strSeekVal);
    }

    private void updatePrice() {
        final TextView tvPriceMin = (TextView) viewInflate.findViewById(R.id.tvPriceMin);
        final TextView tvPriceMax = (TextView) viewInflate.findViewById(R.id.tvPriceMax);

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String priceMin = formatter.format(Integer.parseInt(rangeBarPrice.getSelectedMinValue().toString()));
        String priceMax = formatter.format(Integer.parseInt(rangeBarPrice.getSelectedMaxValue().toString()));

        tvPriceMin.setText(priceMin);
        tvPriceMax.setText(priceMax);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.toggleButtonNearby:
                updateToggleButtonNearby();
				break;
				
			case R.id.btnSearch:
                if(MGUtilities.hasConnection(getActivity())) {
                    asyncSearchServer();
                }
                else {
                    asyncSearchLocal();
                }
				break;

			case R.id.imgViewCategory:
				Intent i = new Intent(this.getActivity(), CategorySelectionActivity.class);
                i.putExtra("willAddAllCategories", true);
				getActivity().startActivityForResult(i, Config.CATEGORY_SELECTION_REQUEST_CODE);
				break;
		}
	}

    private void updateToggleButtonNearby() {
        if(toggleButtonNearby.isChecked()) {
            MainActivity main = (MainActivity) this.getActivity();
            if(MainActivity.location == null)
                main.setOnLocatonListener(this);

            rangeBarRadius.setAlpha(1.0f);
            rangeBarRadius.setEnabled(true);
        }
        else {
            rangeBarRadius.setEnabled(false);
            rangeBarRadius.setAlpha(0.60f);
        }
    }

	private void asyncSearchLocal() {
        MainActivity main = (MainActivity) getActivity();
        main.showSwipeProgress();

		task = new MGAsyncTask(getActivity());
		task.setMGAsyncTaskListener(new OnMGAsyncTaskListener() {
			
			ArrayList<Item> arrayFilter;
			
			@Override
			public void onAsyncTaskProgressUpdate(MGAsyncTask asyncTask) { }
			
			@Override
			public void onAsyncTaskPreExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				asyncTask.dialog.hide();
				MainActivity main = (MainActivity) getActivity();
				main.showSwipeProgress();
			}
			
			@Override
			public void onAsyncTaskPostExecute(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				MainActivity main = (MainActivity) getActivity();
				main.hideSwipeProgress();
				if(arrayFilter != null && arrayFilter.size() == 0) {
					MGUtilities.showNotifier(SearchItemFragment.this.getActivity(), MainActivity.offsetY);
					return;
				}
				Intent i = new Intent(getActivity(), SearchResultActivity.class);
				i.putExtra("searchResults", arrayFilter);
                i.putExtra("showRadius", _showRadius);
				getActivity().startActivity(i);
			}
			
			@Override
			public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask) {
				// TODO Auto-generated method stub
				arrayFilter = search();
			}
		});
		task.execute();
	}

	private ArrayList<Item> search() {
		MainActivity main = (MainActivity) this.getActivity();
		Queries q = main.getQueries();

        _showRadius = false;
        if(toggleButtonNearby.isChecked() && MainActivity.location != null) {
            _showRadius = true;
        }

        if(spinnerSortBy.getSelectedItemPosition() == 4 && MainActivity.location != null) {
            _showRadius = true;
        }
		
		String strKeywords = txtKeywords.getText().toString().toLowerCase().trim();
        String strUserName = txtUSerNameSellerName.getText().toString().toLowerCase().trim();
	    int radius = Integer.parseInt(rangeBarRadius.getSelectedMinValue().toString());
        int priceMin = Integer.parseInt(rangeBarPrice.getSelectedMinValue().toString());
        int priceMax = Integer.parseInt(rangeBarPrice.getSelectedMaxValue().toString());

	    int countParams = strKeywords.length() > 0 ? 1 : 0;
        countParams += strUserName.length() > 0 ? 1 : 0;
	    countParams += radius > 0 && toggleButtonNearby.isChecked() ? 1 : 0;
	    countParams += selectecCategory != null ? 1 : 0;
        countParams += 1; // for price Min/Max;

	    ArrayList<Item> arrayItems = q.getItems();
        for(Item item : arrayItems)
            item.setUser(item.getUser());

	    ArrayList<Item> arrayFilter = new ArrayList<Item>();
	    for(Item item : arrayItems) {
	    	int qualifyCount = 0;
            if(strKeywords.length() > 0) {
                boolean isFoundKeyword = item.getItem_name().toLowerCase().contains(strKeywords) ||
                        item.getItem_desc().toLowerCase().contains(strKeywords);

                if( strKeywords.length() > 0  && isFoundKeyword)
                    qualifyCount += 1;
            }
            if(strUserName.length() > 0) {
                ArrayList<User> users = q.getUsersByUserNameOrFullName(strUserName);
                for(User user : users) {
                    if(item.getUser_id() == user.getUser_id()) {
                        qualifyCount += 1;
                        item.setUser(user);
                        break;
                    }
                }
            }
            String strPrice = "";
            try {
                char[] chrs = item.getItem_price().toCharArray();
                for(int x = 0; x < chrs.length; x++) {
                    char chr = chrs[x];
                    if(Character.isDigit(chr)) {
                        strPrice += chr;
                    }
                    else if(Character.isLetter(chr)) {
                        if(chr == '.' && item.getItem_price().length()-1 != x)
                            strPrice += chr;
                    }
                }
            }
            catch (Exception e) {
                strPrice = null;
            }
            if(strPrice != null && strPrice.length() > 0) {
                float price = Float.parseFloat(strPrice);
                item.setPrice(price);
                if(price >= priceMin && price <= priceMax)
                    qualifyCount += 1;
            }
	        if( selectecCategory != null) {
	            if(selectecCategory.getCategory_id() == item.getCategory_id())
                    qualifyCount += 1;
	        }
	        item.setDistance(-1);
	        if(toggleButtonNearby.isChecked()) {
				if(MainActivity.location != null) {
					Location locStore = new Location("Store");
					locStore.setLatitude(item.getLat());
					locStore.setLongitude(item.getLon());
					float distance = locStore.distanceTo(MainActivity.location) / 1000;
                    item.setDistance(distance);
					if(distance <= radius)
		                qualifyCount += 1;
				}
	        }
	        if(qualifyCount == countParams)
	        	arrayFilter.add(item);
	    }
		if(spinnerSortBy.getSelectedItemPosition() == 0 && toggleButtonNearby.isChecked()) {
            Collections.sort(arrayFilter, new Comparator<Item>() {
                @Override
                public int compare(Item t1, Item t2) {
                    if (t1.getDistance() < t2.getDistance())
                        return -1;
                    if (t1.getDistance() > t2.getDistance())
                        return 1;
                    return 0;
                }
            });
        }
        else if(spinnerSortBy.getSelectedItemPosition() == 1) { //price sorting
            Collections.sort(arrayFilter, new Comparator<Item>() {
                @Override
                public int compare(Item t1, Item t2) {
                    if (t1.getPrice() < t2.getPrice())
                        return -1;
                    if (t1.getPrice() > t2.getPrice())
                        return 1;
                    return 0;
                }
            });
        }
        else if(spinnerSortBy.getSelectedItemPosition() == 2) { //date posted
            Collections.sort(arrayFilter, new Comparator<Item>() {
                @Override
                public int compare(Item t1, Item t2) {
                    if (t1.getCreated_at() < t2.getCreated_at())
                        return -1;
                    if (t1.getCreated_at() > t2.getCreated_at())
                        return 1;
                    return 0;
                }
            });
        }
        else if(spinnerSortBy.getSelectedItemPosition() == 3) { //name
            Collections.sort(arrayFilter, new Comparator<Item>() {
                @Override
                public int compare(Item t1, Item t2) {

                    if(t1.getUser() != null && t2.getUser() != null) {
                        if (t1.getUser().getFull_name().compareToIgnoreCase(t2.getUser().getFull_name()) < 0)
                            return -1;
                        if (t1.getUser().getFull_name().compareToIgnoreCase(t2.getUser().getFull_name()) > 0)
                            return 1;
                    }

                    return 0;
                }
            });
        }
        else if(spinnerSortBy.getSelectedItemPosition() == 2) { //distance
            if(MainActivity.location != null) {
                for(Item item : arrayFilter) {
                    Location locStore = new Location("Store");
                    locStore.setLatitude(item.getLat());
                    locStore.setLongitude(item.getLon());
                    float distance = locStore.distanceTo(MainActivity.location) / 1000;
                    item.setDistance(distance);
                }
                Collections.sort(arrayFilter, new Comparator<Item>() {
                    @Override
                    public int compare(Item t1, Item t2) {
                        if (t1.getDistance() < t2.getDistance())
                            return -1;
                        if (t1.getDistance() > t2.getDistance())
                            return 1;
                        return 0;
                    }
                });
            }
        }
	    return arrayFilter;
	}

	@Override
	public void onLocationChanged(Location prevLoc, Location currentLoc) { }

    @Override
    public void onGPSError() { }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.CATEGORY_SELECTION_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                selectecCategory = (Category) data.getSerializableExtra("category");
                tvCategory.setText(selectecCategory.getCategory());

                if(selectecCategory.getCategory_id() == -1) {
                    selectecCategory = null;
                }
            }
        }
    }

    public void asyncSearchServer() {
        MainActivity main = (MainActivity) getActivity();
        main.showSwipeProgress();

        String strKeywords = txtKeywords.getText().toString().toLowerCase().trim();
        String strUserName = txtUSerNameSellerName.getText().toString().toLowerCase().trim();
        int radius = Integer.parseInt(rangeBarRadius.getSelectedMaxValue().toString());
        int priceMin = Integer.parseInt(rangeBarPrice.getSelectedMinValue().toString());
        int priceMax = Integer.parseInt(rangeBarPrice.getSelectedMaxValue().toString());

        final ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("keywords", Html.toHtml(new SpannableString(strKeywords))));
        params.add(new BasicNameValuePair("seller_name_or_user_name", Html.toHtml(new SpannableString(strUserName))));
        params.add(new BasicNameValuePair("item_price", String.valueOf(radius)));
        params.add(new BasicNameValuePair("price_min", String.valueOf(priceMin)));
        params.add(new BasicNameValuePair("price_max", String.valueOf(priceMax)));
        params.add(new BasicNameValuePair("api_key", Config.API_KEY));

        _showRadius = false;
        if(toggleButtonNearby.isChecked() && MainActivity.location != null) {
            params.add(new BasicNameValuePair("radius", String.valueOf(radius)));
            params.add(new BasicNameValuePair("lat", String.valueOf(MainActivity.location.getLatitude())));
            params.add(new BasicNameValuePair("lon", String.valueOf(MainActivity.location.getLongitude())));
            _showRadius = true;
        }

        if(spinnerSortBy.getSelectedItemPosition() == 4 && MainActivity.location != null) {
            params.add(new BasicNameValuePair("lat", String.valueOf(MainActivity.location.getLatitude())));
            params.add(new BasicNameValuePair("lon", String.valueOf(MainActivity.location.getLongitude())));
            _showRadius = true;
        }

        params.add(new BasicNameValuePair("sort_by", String.valueOf(spinnerSortBy.getSelectedItemPosition())));
        params.add(new BasicNameValuePair("category_id", selectecCategory == null ? "0" : String.valueOf(selectecCategory.getCategory_id())));

        task = new MGAsyncTask(getActivity());
        task.setMGAsyncTaskListener(new MGAsyncTask.OnMGAsyncTaskListener() {

            ArrayList<Item> arrayFilter;

            @Override
            public void onAsyncTaskProgressUpdate(MGAsyncTask asyncTask) { }

            @Override
            public void onAsyncTaskPreExecute(MGAsyncTask asyncTask) {
                asyncTask.dialog.hide();
            }

            @Override
            public void onAsyncTaskPostExecute(MGAsyncTask asyncTask) {
                MainActivity main = (MainActivity) getActivity();
                main.hideSwipeProgress();
                if(arrayFilter != null && arrayFilter.size() == 0) {
                    MGUtilities.showNotifier(SearchItemFragment.this.getActivity(), MainActivity.offsetY);
                    return;
                }
                Intent i = new Intent(getActivity(), SearchResultActivity.class);
                i.putExtra("searchResults", arrayFilter);
                i.putExtra("showRadius", _showRadius);
                getActivity().startActivity(i);
            }

            @Override
            public void onAsyncTaskDoInBackground(MGAsyncTask asyncTask) {

                arrayFilter = new ArrayList<Item>();
                UserAccessSession accessSession = UserAccessSession.getInstance(getActivity());
                try {
                    Data data = DataParser.getJSONFromUrlWithPostRequestUsingData(Config.SEARCH_ITEMS_JSON_URL, params);
                    if (data == null)
                        return;

                    if (data.getSearch_items() != null && data.getSearch_items().size() > 0) {
                        for (Item item : data.getSearch_items()) {
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

                            arrayFilter.add(item);
                        }
                    }

                    if (data.getCategories() != null && data.getCategories().size() > 0) {
                        for (Category category : data.getCategories()) {
                            q.deleteCategory(category.getCategory_id());
                            q.insertCategory(category);
                        }
                    }

                    if(accessSession.getFilterDistance() == 0 && data.getDefault_distance() > 0)
                        accessSession.setFilterDistance(data.getDefault_distance());

                    if(accessSession.getFilterDistanceMax() == 0)
                        accessSession.setFilterDistanceMax(data.getMax_distance());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        task.execute();
    }
}
