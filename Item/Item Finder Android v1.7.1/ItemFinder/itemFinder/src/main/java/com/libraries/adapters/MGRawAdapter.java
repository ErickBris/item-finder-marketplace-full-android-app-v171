package com.libraries.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MGRawAdapter extends BaseAdapter  {

	Context c;
	private int count;
	OnMGRawAdapterListener mCallback;

	public interface OnMGRawAdapterListener {
        public View OnMGRawAdapterCreated(MGRawAdapter adapter, View v, int position, ViewGroup viewGroup);
    }

	public void setOnMGRawAdapterListener(OnMGRawAdapterListener listener) {
		try {
            mCallback = (OnMGRawAdapterListener) listener;
        } catch (ClassCastException e)  {
            throw new ClassCastException(this.toString() + " must implement OnMGRawAdapterListener");
        }
	}

	public MGRawAdapter(Context c, int count) {
		this.c = c;
		this.count = count;
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
		if(mCallback == null)
			throw new IllegalArgumentException(this.toString() + " must call setOnMGRawAdapterListener");

		v = mCallback.OnMGRawAdapterCreated(this, v, pos, viewGroup);
		return v;
	}


	public void clearAdapter() {
		// TODO Auto-generated method stub
		count = 0;
	}
}

