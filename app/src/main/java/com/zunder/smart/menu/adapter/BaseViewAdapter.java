package com.zunder.smart.menu.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BaseViewAdapter<T> extends BaseAdapter {

	private List<T> list;
	protected Context context;

	public BaseViewAdapter(Context context, List<T> list) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setList(List<T> list) {
		this.list.clear();
		this.list = list;
		notifyDataSetChanged();
	}

	public Context getContext() {
		return context;
	}
}
