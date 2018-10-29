package com.zunder.smart.menu.adapter;

import java.util.List;

import com.zunder.smart.R;
import com.zunder.smart.menu.model.MenuItem;
import com.zunder.smart.menu.model.MenuItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RightMenuAdapter extends BaseViewAdapter<MenuItem> {

	public RightMenuAdapter(Context context, List<MenuItem> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_left_menu, null);
		}
		TextView text = (TextView) convertView.findViewById(R.id.text);
		text.setText(getItem(position).getName());
		ImageView image = (ImageView) convertView.findViewById(R.id.image);
		image.setImageDrawable(getItem(position).getDrawable());
		;
		return convertView;
	}

}
