package com.projects.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.libraries.adapters.MGListAnimatedAdapter;
import com.libraries.adapters.MGListAnimatedAdapter.OnMGListAnimatedAdapter;
import com.config.Config;
import com.config.UIConfig;
import com.db.Queries;
import com.libraries.html.MGHtml;
import com.libraries.imageview.MGImageView;
import com.models.Favorite;
import com.models.Item;
import com.models.Photo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.libraries.utilities.MGUtilities;
import com.projects.activities.DetailItemActivity;
import com.projects.itemfinder.MainActivity;
import com.projects.itemfinder.R;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FavoriteFragment extends Fragment implements OnItemClickListener, OnClickListener{
	
	private View viewInflate;
	private ArrayList<Item> arrayData;
	DisplayImageOptions options;
	
	public FavoriteFragment() { }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		viewInflate = inflater.inflate(R.layout.fragment_category, null);
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
		MainActivity main = (MainActivity) this.getActivity();
		final Queries q = main.getQueries();
		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(UIConfig.SLIDER_PLACEHOLDER)
			.showImageForEmptyUri(UIConfig.SLIDER_PLACEHOLDER)
			.showImageOnFail(UIConfig.SLIDER_PLACEHOLDER)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
		
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				arrayData = q.getItemsFavorites();
				showList();
			}
		}, Config.DELAY_SHOW_ANIMATION);
		main.showSwipeProgress();
	}
	
	private void showList() {

		if(MainActivity.location != null) {
			for(Item item : arrayData) {
				Location locStore = new Location("Store");
				locStore.setLatitude(item.getLat());
				locStore.setLongitude(item.getLon());
				float distance = locStore.distanceTo(MainActivity.location) / 1000;
				item.setDistance(distance);
			}
			Collections.sort(arrayData, new Comparator<Item>() {
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

		MainActivity main = (MainActivity) this.getActivity();
		main.hideSwipeProgress();

		final Queries q = main.getQueries();
		ListView listView = (ListView) viewInflate.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		MGListAnimatedAdapter adapter = new MGListAnimatedAdapter(
				getActivity(), arrayData.size(), R.layout.item_entry);
		
		adapter.setOnMGListAnimatedAdapter(new OnMGListAnimatedAdapter() {

			@Override
			public void OnMGListAnimatedAdapterCreated(MGListAnimatedAdapter adapter,
					View v, int position, ViewGroup viewGroup) {
				// TODO Auto-generated method stub
				final Item item = arrayData.get(position);
				Photo p = q.getPhotoByItemId(item.getItem_id());
				MGImageView imgViewPhoto = (MGImageView) v.findViewById(R.id.imgViewPhoto);
				imgViewPhoto.setCornerRadius(0.0f);
				imgViewPhoto.setBorderWidth(UIConfig.NO_BORDER_WIDTH);
				imgViewPhoto.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent i = new Intent(getActivity(), DetailItemActivity.class);
						i.putExtra("item", item);
						getActivity().startActivity(i);
					}
				});
				
				if(p != null) {
					MainActivity.getImageLoader().displayImage(p.getPhoto_url(), imgViewPhoto, options);
				}
				else {
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

				ImageView imgViewStarred = (ImageView) v.findViewById(R.id.imgViewStarred);
				imgViewStarred.setVisibility(View.VISIBLE);
				Favorite fave = q.getFavoriteByItemId(item.getItem_id());
				if(fave == null)
					imgViewStarred.setVisibility(View.INVISIBLE);

                ImageView imgViewFeatured = (ImageView) v.findViewById(R.id.imgViewFeatured);
                imgViewFeatured.setVisibility(View.VISIBLE);
                if(item.getFeatured() == 0)
                    imgViewFeatured.setVisibility(View.INVISIBLE);

                TextView tvDistance = (TextView) v.findViewById(R.id.tvDistance);
                tvDistance.setVisibility(View.INVISIBLE);

				if(item.getDistance() != -1 || MainActivity.location != null) {
					tvDistance.setVisibility(View.VISIBLE);
					double km = item.getDistance();
					String format = String.format(
							"%.2f %s",
							km,
							MGUtilities.getStringFromResource(getActivity(), R.string.km));
					tvDistance.setText(format);
				}
				else {
					tvDistance.setText(R.string.empty_distance);
				}

				ImageView imgViewRibbon = (ImageView) v.findViewById(R.id.imgViewRibbon);
				imgViewRibbon.setImageResource(R.drawable.ribbon_edge_sold);
				imgViewRibbon.setVisibility(View.GONE);

				if(item.getItem_status() == 1)
					imgViewRibbon.setVisibility(View.VISIBLE);

			}
		});
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		if(arrayData != null && arrayData.size() == 0) {
			MGUtilities.showNotifier(this.getActivity(), MainActivity.offsetY);
			return;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long resId) { }


	@Override
	public void onClick(View v) { }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity main = (MainActivity) this.getActivity();
        final Queries q = main.getQueries();

        Handler h = new Handler();
        h.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                arrayData = q.getItemsFavorites();
                showList();
            }
        }, Config.DELAY_SHOW_ANIMATION);
        main.showSwipeProgress();
    }
}
