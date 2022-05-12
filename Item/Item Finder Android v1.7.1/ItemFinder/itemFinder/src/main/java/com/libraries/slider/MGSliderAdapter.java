package com.libraries.slider;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class MGSliderAdapter extends PagerAdapter implements View.OnClickListener{
	
	private int resid = 0;
	private OnMGSliderAdapterListener mCallback;
	public View[] selectionViews;
	public int numOfItems = 0;
	public int maxThumbCount = 0;
	
	public interface OnMGSliderAdapterListener {
        public void onOnMGSliderAdapterCreated(MGSliderAdapter 
        		adapter, View v, int position);
    }
	
	public void setOnMGSliderAdapterListener(OnMGSliderAdapterListener listener) {
		try {
            mCallback = (OnMGSliderAdapterListener) listener;
        } catch (ClassCastException e) {
            throw new ClassCastException(this.toString() + " must implement OnMGSliderAdapterListener");
        }
	}

	public MGSliderAdapter(int resid, int numOfItems, int maxThumbCount) {
		// TODO Auto-generated constructor stub
		this.resid = resid;
		this.numOfItems = numOfItems;
		selectionViews = new View[numOfItems];
		this.maxThumbCount = maxThumbCount;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int size = maxThumbCount <= numOfItems ? maxThumbCount : numOfItems;
		return size;
	}

	@Override
	public boolean isViewFromObject(View v, Object obj) {
		// TODO Auto-generated method stub
		return v == ((View) obj);
	}

	@Override
	public Object instantiateItem(View container, int position)  {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) 
				container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View view = inflater.inflate(resid, null);
		selectionViews[position] = view;
		((ViewPager) container).addView(view,0);
		if(mCallback != null)
			mCallback.onOnMGSliderAdapterCreated(this, view, position);
		
		return view;
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return super.saveState();
	}
	
	@Override
    public void destroyItem(View collection, int position, Object view)  {
		// disabled so that the view will not be removed and to avoid
		// flickering when animation begins
//         ((ViewPager) collection).removeView((View) view);
    }


	private OnMGSliderThumbListener mCallbackThumb;

	public interface OnMGSliderThumbListener {


		public void onOnMGSliderThumbCreated(MGSliderAdapter
													   adapter, int position, View v);

		public void onOnMGSliderThumbSelected(MGSliderAdapter
													 adapter, int position, View v);

		public void onOnMGSliderThumbPreviousSelected(MGSliderAdapter
													  adapter, int position, View v);
	}

	public void setOnMGSliderThumbListener(OnMGSliderThumbListener listener) {
		try {
			mCallbackThumb = (OnMGSliderThumbListener) listener;
		} catch (ClassCastException e) {
			throw new ClassCastException(this.toString() + " must implement OnMGSliderThumbListener");
		}
	}

	View previousView;
	int lastIndex = -1;
	View[] views;

	public void createSlider(Context c, View parentView, int resIdEntry, int count) {

		if(parentView instanceof LinearLayout) {
			((LinearLayout)parentView).setOrientation(LinearLayout.HORIZONTAL);
			views = new View[count];
			for(int x = 0; x < count; x++) {
				LayoutInflater inf = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View v = inf.inflate(resIdEntry, null);
				v.setOnClickListener(this);
				v.setTag(x);
				views[x] = v;

				if(mCallbackThumb != null)
					mCallbackThumb.onOnMGSliderThumbCreated(this, x, v);

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);

				((LinearLayout)parentView).addView(v, params);
			}
		}
		else {
			throw new ClassCastException(this.toString() + " must be a Linear Layout parent View");
		}
	}

	public void setSelectedThumb(int index) {

		if(index >= 0 && index < views.length) {
			onClick(views[index]);
		}
		else {
			throw new ArrayIndexOutOfBoundsException(this.toString() + " tab index out of bounds");
		}
	}

	@Override
	public void onClick(View v) {

		if(lastIndex != -1) {
			if(mCallback != null)
				mCallbackThumb.onOnMGSliderThumbPreviousSelected(this, lastIndex, previousView);
		}

		int index = Integer.parseInt(v.getTag().toString());
		View viewSelected = views[index];

		lastIndex = index;
		previousView = viewSelected;

		if(mCallback != null)
			mCallbackThumb.onOnMGSliderThumbSelected(this, index, viewSelected);
	}

	public int getSelectedThumnIndex() {
		return lastIndex;
	}
}
