package com.zunder.smart.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zunder.smart.R;
import com.zunder.smart.model.Archive;
import com.zunder.smart.model.DeviceIo;
import com.zunder.smart.model.Archive;

import java.util.List;

public class BackQueryAdapter extends BaseAdapter {

	List<Archive> list;
	int mSelect = -1;
	Activity context;

	public BackQueryAdapter(Activity context, List<Archive> list) {
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

		if (mSelect == list.get(position).getId()) {
			convertView.setBackgroundResource(R.drawable.emotion_bg);
		} else {
			convertView.setBackgroundResource(0);
		}

		viewCach.but_text.setText(list.get(position).getProjectName());
		return convertView;
	}

	private final class ViewCach {

		TextView but_text;
		ImageView del;
	}

}
