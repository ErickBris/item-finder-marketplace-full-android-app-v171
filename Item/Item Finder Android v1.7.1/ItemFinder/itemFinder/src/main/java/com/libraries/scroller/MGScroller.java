package com.libraries.scroller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class MGScroller extends HorizontalScrollView implements OnClickListener {
	
	LinearLayout layout;
	
	public MGScroller(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.c = context;
	}

	public MGScroller(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.c = context;
	}

	@SuppressLint("NewApi") 
	public MGScroller(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.c = context;
	}

	Context c;
	int count;
	int resId;
	
	public ScrollerListener mCallback;
	
	public interface ScrollerListener {
		
        public void onScrollerCreated(MGScroller
											  adapter, View v, int position);
        
        public void onScrollerSelected(View v, int position);
        
        public void onScrollerFinishCreated(MGScroller scroller);
    }
	
	public void setOnScrollerListener(ScrollerListener listener) {
		
		try  {
            mCallback = (ScrollerListener) listener;
        } catch (ClassCastException e) {
        	
            throw new ClassCastException(this.toString() + " must ScrollerListener TabListener");
        }
	}
	
	
	public void createScroller(int count, int resId) {
		
		this.removeAllViews();
		
		layout = new LinearLayout(c);
		layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		layout.setOrientation(LinearLayout.HORIZONTAL);
		
		this.addView(layout);
		this.count = count;
		this.resId = resId;
		
		
		for(int x = 0; x < count; x++) {
			LayoutInflater li = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View v = li.inflate(resId, null);
			v.setTag(x);
//			v.setOnClickListener(this);
			final int index = x;
			v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if(mCallback != null)
						mCallback.onScrollerSelected(v, index);
				}
			});
			layout.addView(v, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, Gravity.CENTER));
			
			if(mCallback != null)
				mCallback.onScrollerCreated(this, v, x);
		}
		
		if(mCallback != null)
			mCallback.onScrollerFinishCreated(this);
		
	}
	
	public View insertViewWithTag(int resId, int tag) {
		
		count += 1;
		LayoutInflater li = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View v = li.inflate(resId, null);
		v.setTag(tag);
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(mCallback != null)
					mCallback.onScrollerSelected(v, count-1);
			}
		});
		layout.addView(v);
		
		if(mCallback != null)
			mCallback.onScrollerCreated(this, v, count-1);
		
		return v;
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		try {
			
			int index = Integer.parseInt(v.getTag().toString());
			
			if(mCallback != null)
				mCallback.onScrollerSelected(v, index);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
//	public TabAdapter(Context c, int count, int resId) {
//		this.c = c;
//		this.count = count;
//		this.resId = resId;
//	}
//
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return count;
//	}
//
//	@Override
//	public Object getItem(int pos) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public long getItemId(int pos) {
//		// TODO Auto-generated method stub
//		return pos;
//	}
//
//	@Override
//	public View getView(int pos, View v, ViewGroup viewGroup) {
//		// TODO Auto-generated method stub
//		
//		if(v == null) {
//			LayoutInflater li = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			v = li.inflate(resId, null);
//		}
//		
//		if(mCallback != null)
//			mCallback.onPlainListAdapterCreated(this, v, pos, viewGroup);
//		
//		return v;
//	}
	
	
}

