package com.zunder.smart.adapter;

import java.util.List;

import com.zunder.smart.R;
import com.zunder.smart.model.Products;
import com.zunder.smart.model.Products;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductAdapter extends BaseAdapter {

	List<Products> list;
	int mSelect = -1;
	int mEdite = -1;
	Activity context;

	public ProductAdapter(Activity context, List<Products> list) {
		this.list = list;
		this.context = context;

	}

	public void changeSelected(int select) {
		if (select != mSelect) {
			mSelect = select;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewCach viewCach = null;
		if (convertView == null) {
			viewCach = new ViewCach();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_arce, null);
			viewCach.but_text = (TextView) convertView.findViewById(R.id.text);
			viewCach.del = (ImageView) convertView.findViewById(R.id.delArce);
			convertView.setTag(viewCach);
		} else {
			viewCach = (ViewCach) convertView.getTag();
		}

		if (mSelect == list.get(position).getProductsKey()) {
			convertView.setBackgroundResource(R.drawable.emotion_bg);
		} else {
			convertView.setBackgroundResource(0);
		}

		viewCach.but_text.setText(list.get(position).getProductsName());

		return convertView;
	}

	private final class ViewCach {

		TextView but_text;
		ImageView del;
	}

}
