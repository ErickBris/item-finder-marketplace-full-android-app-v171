package com.libraries.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MGListAnimatedAdapter extends BaseAdapter  {
	
	Context c;
	private int count;
	private int resId;
	OnMGListAnimatedAdapter mCallback;
	
	@SuppressWarnings("unused")
	private int mLastPosition = 1;
	
	public interface OnMGListAnimatedAdapter {
		
        public void OnMGListAnimatedAdapterCreated(MGListAnimatedAdapter 
        		adapter, View v, int position, ViewGroup viewGroup);
    }
	
	public void setOnMGListAnimatedAdapter(OnMGListAnimatedAdapter listener) {
		
		try {
            mCallback = (OnMGListAnimatedAdapter) listener;
        } catch (ClassCastException e)  {
            throw new ClassCastException(this.toString() + " must implement OnMGListAnimatedAdapter");
        }
	}
	
	
	public MGListAnimatedAdapter(Context c, int count, int resId) {
		this.c = c;
		this.count = count;
		this.resId = resId;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public View getView(int pos, View v, ViewGroup viewGroup) {
		// TODO Auto-generated method stub
		
		ViewHolder viewHolder = null;
		
		if(v == null) {
			LayoutInflater li = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate(resId, null);
			
			viewHolder = new ViewHolder();
			viewHolder.view = v;
			v.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) v.getTag();
			Log.w("OnMGListAnimatedAdapter Class", "View being reused.");
		}
		
		if(mCallback != null)
			mCallback.OnMGListAnimatedAdapterCreated(this, viewHolder.view, pos, viewGroup);
		
//		TranslateAnimation animation = null;
//        if (pos > mLastPosition) {
//            animation = new TranslateAnimation(
//                Animation.RELATIVE_TO_SELF,
//                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 1.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f);
//
//            animation.setDuration(600);
//            v.startAnimation(animation);
//            mLastPosition = pos;
//        }

		
		return v;
	}
	
	public class ViewHolder {
		
		public View view;
	}
	
//	@Override
//	public int getViewTypeCount() {                 
//
//	    return count;
//	}
//
//	@Override
//	public int getItemViewType(int position) {
//
//	    return position;
//	}
}

