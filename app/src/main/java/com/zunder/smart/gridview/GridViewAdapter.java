package com.zunder.smart.gridview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xiaolin on 2015/1/24.
 */
public class GridViewAdapter<T> extends BaseAdapter {
	private Context context;
	private List<T> list;

	public GridViewAdapter(Context context, List<T> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public T getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		return null;
	}

	public void hideView(int pos) {

	}

	public void showHideView() {

	}

	public void removeView(int pos) {

	}


}
