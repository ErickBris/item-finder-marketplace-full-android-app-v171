package com.projects.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.config.Config;
import com.db.DbHelper;
import com.db.Queries;
import com.libraries.adapters.MGListAdapter;
import com.libraries.refreshlayout.SwipeRefreshActivity;
import com.libraries.utilities.MGUtilities;
import com.models.Category;
import com.projects.itemfinder.MainActivity;
import com.projects.itemfinder.R;

import java.util.ArrayList;

public class SubCategoryActivity extends SwipeRefreshActivity implements OnItemClickListener{
	
	private ArrayList<Category> arrayData;
	private Queries q;
	private SQLiteDatabase db;
	private Category category;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		setContentView(R.layout.fragment_category);
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
		
		category = (Category) this.getIntent().getSerializableExtra("category");
		arrayData = q.getCategoriesByPID(category.getCategory_id());
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setList();
			}
		}, Config.DELAY_SHOW_ANIMATION);
	}

	private void setList() {
		if(arrayData == null)
			arrayData = new ArrayList<Category>();

		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		MGListAdapter adapter = new MGListAdapter(
				this, arrayData.size(), R.layout.category_entry);

		adapter.setOnMGListAdapterAdapterListener(new MGListAdapter.OnMGListAdapterAdapterListener() {

			@Override
			public void OnMGListAdapterAdapterCreated(MGListAdapter adapter, View v,
													  int position, ViewGroup viewGroup) {
				// TODO Auto-generated method stub
				Category category = arrayData.get(position);
				Spanned title = Html.fromHtml(category.getCategory());
				TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
				tvTitle.setText(title);
			}
		});
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		if(arrayData != null && arrayData.size() == 0) {
			MGUtilities.showNotifier(this, MainActivity.offsetY);
			return;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long resId) {
		// TODO Auto-generated method stub
		Category category = arrayData.get(pos);
		ArrayList<Category> categories = q.getCategoriesByPID(category.getCategory_id());
		Intent i;
		if(categories.size() > 0) {
			i = new Intent(this, SubCategoryActivity.class);
			i.putExtra("category", category);
			startActivity(i);
		}
		else {
			i = new Intent(this, ItemsActivity.class);
			i.putExtra("category", category);
			startActivity(i);
		}
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
}
