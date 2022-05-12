package com.projects.fragments;

import java.util.ArrayList;

import com.projects.activities.ItemsActivity;
import com.projects.activities.SubCategoryActivity;
import com.libraries.adapters.MGListAdapter;
import com.libraries.adapters.MGListAdapter.OnMGListAdapterAdapterListener;
import com.config.Config;
import com.db.Queries;
import com.models.Category;
import com.libraries.utilities.MGUtilities;
import com.projects.itemfinder.MainActivity;
import com.projects.itemfinder.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryFragment extends Fragment implements OnItemClickListener{
	
	private View viewInflate;
	private ArrayList<Category> categoryList;
	Queries q;
	public CategoryFragment() { }
	
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
		q = main.getQueries();

		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				categoryList = q.getCategoriesByPID(0);
				showList();
			}
		}, Config.DELAY_SHOW_ANIMATION);
		main.showSwipeProgress();
	}
	
	private void showList() {
		MainActivity main = (MainActivity) this.getActivity();
		main.hideSwipeProgress();
		if(categoryList != null && categoryList.size() == 0) {
			MGUtilities.showNotifier(this.getActivity(), MainActivity.offsetY);
			return;
		}
		
		ListView listView = (ListView) viewInflate.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		MGListAdapter adapter = new MGListAdapter(
				getActivity(), categoryList.size(), R.layout.category_entry);
		
		adapter.setOnMGListAdapterAdapterListener(new OnMGListAdapterAdapterListener() {
			
			@Override
			public void OnMGListAdapterAdapterCreated(MGListAdapter adapter, View v,
					int position, ViewGroup viewGroup) {
				// TODO Auto-generated method stub
				Category category = categoryList.get(position);
				Spanned title = Html.fromHtml(category.getCategory());
				TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
				tvTitle.setText(title);
			}
		});
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long resId) {
		// TODO Auto-generated method stub
		Category category = categoryList.get(pos);
		ArrayList<Category> categories = q.getCategoriesByPID(category.getCategory_id());
		Intent i;
		if(categories.size() > 0) {
			i = new Intent(getActivity(), SubCategoryActivity.class);
			i.putExtra("category", category);
			getActivity().startActivity(i);
		}
		else {
			i = new Intent(getActivity(), ItemsActivity.class);
			i.putExtra("category", category);
			getActivity().startActivity(i);
		}
	}
}
