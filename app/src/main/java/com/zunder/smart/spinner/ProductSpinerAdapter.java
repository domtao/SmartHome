package com.zunder.smart.spinner;

import java.util.ArrayList;
import java.util.List;

import com.zunder.smart.R;
import com.zunder.smart.model.Products;
import com.zunder.smart.model.Products;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProductSpinerAdapter extends BaseAdapter {

	public static interface IOnItemSelectListener {
		public void onItemClick(int pos);
	};

	private Context mContext;
	private List<Products> mObjects = new ArrayList<Products>();
	private int mSelectItem = 0;

	private LayoutInflater mInflater;

	public ProductSpinerAdapter(Context context) {
		init(context);
	}

	public void refreshData(List<Products> objects, int selIndex) {
		mObjects = objects;
		if (selIndex < 0) {
			selIndex = 0;
		}
		if (selIndex >= mObjects.size()) {
			selIndex = mObjects.size() - 1;
		}

		mSelectItem = selIndex;
	}

	private void init(Context context) {
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {

		return mObjects.size();
	}

	@Override
	public Products getItem(int pos) {
		return mObjects.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.spiner_item_layout, null);
			viewHolder = new ViewHolder();
			viewHolder.mTextView = (TextView) convertView
					.findViewById(R.id.textView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Products item = getItem(pos);
		viewHolder.mTextView.setText(item.getProductsName()+"  "+item.getProductsCode());
		return convertView;
	}

	public static class ViewHolder {
		public TextView mTextView;
	}

}
