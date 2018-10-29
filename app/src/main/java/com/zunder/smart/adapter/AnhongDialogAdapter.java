package com.zunder.smart.adapter;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zunder.smart.R;

public class AnhongDialogAdapter extends BaseAdapter {

	String[] list;
	Activity activity;
	Boolean[] booleans;

	public AnhongDialogAdapter(Activity activity, String[] list,
			Boolean[] booleans) {
		this.list = list;
		this.activity = activity;
		this.booleans = booleans;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		TextView textView = null;
		ImageView imageView = null;
		ViewCach viewCach = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.widget_anhong_dialog_item, null);
			textView = (TextView) convertView.findViewById(R.id.dialogName);
			imageView = (ImageView) convertView.findViewById(R.id.dialogImage);
			viewCach = new ViewCach();
			viewCach.textView = textView;
			viewCach.imageView = imageView;
			convertView.setTag(viewCach);
		} else {
			viewCach = (ViewCach) convertView.getTag();
			textView = viewCach.textView;
			imageView = viewCach.imageView;
		}
		textView.setText(list[position]);
		if (booleans[position] == true) {
			imageView.setImageResource(R.mipmap.widget_alrm_image);
		} else {
			imageView.setImageResource(0);
		}
		return convertView;
	}

	private final class ViewCach {
		TextView textView;
		ImageView imageView;
	}

}
