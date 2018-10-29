package com.door.activity;


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
import com.zunder.smart.model.Device;
import com.zunder.smart.model.Mode;

import java.util.List;

public class ModeDialogAdapter extends BaseAdapter {

	List<Mode> list;
	Activity activity;

	public ModeDialogAdapter(Activity activity, List<Mode> list) {
		this.list = list;
		this.activity = activity;
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
		textView.setText(list.get(position).getModeName());

		return convertView;
	}

	private final class ViewCach {
		TextView textView;
		ImageView imageView;
	}

}
