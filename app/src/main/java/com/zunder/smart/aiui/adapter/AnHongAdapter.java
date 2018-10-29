package com.zunder.smart.aiui.adapter;

import java.util.List;

import com.zunder.smart.R;
import com.zunder.smart.aiui.info.AnHong;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AnHongAdapter extends BaseAdapter {
	List<AnHong> list;

	private Activity context;

	public AnHongAdapter(Activity context, List<AnHong> list) {
		this.context = context;
		this.list = list;
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
			convertView = inflater.inflate(R.layout.item_anhong, null);

			viewCach = new ViewCach();

			viewCach.name_text = (TextView) convertView
					.findViewById(R.id.anHongName);
			viewCach.msg_text = (TextView) convertView
					.findViewById(R.id.anHongMsg);
			viewCach.id_text = (TextView) convertView
					.findViewById(R.id.anHongId);

			convertView.setTag(viewCach);
		} else {
			viewCach = (ViewCach) convertView.getTag();

		}
		viewCach.name_text.setText(list.get(position).getMsgName());
		viewCach.id_text.setText((position + 1) + "");
		viewCach.msg_text.setText(list.get(position).getMsgInfo());

		return convertView;
	}

	private final class ViewCach {

		TextView name_text;
		TextView msg_text;
		TextView id_text;

	}

}
