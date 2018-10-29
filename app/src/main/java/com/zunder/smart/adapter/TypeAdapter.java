package com.zunder.smart.adapter;

import java.util.List;

import com.zunder.smart.R;
import com.zunder.smart.model.DeviceType;
import com.zunder.smart.model.DeviceType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TypeAdapter extends BaseAdapter {

	List<DeviceType> list;
	int mSelect = -1;
	Context context;

	public TypeAdapter(Context context, List<DeviceType> list) {
		this.list = list;
		this.context = context;

	}

	public void changeSelected(int positon) {
		if (positon != mSelect) {
			mSelect = positon;
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
			convertView.setTag(viewCach);
		} else {
			viewCach = (ViewCach) convertView.getTag();
		}
		if (mSelect == list.get(position).getDeviceTypeKey()) {
			convertView.setBackgroundResource(R.drawable.emotion_bg);
		} else {
			convertView.setBackgroundResource(0);
		}
		viewCach.but_text.setText(list.get(position).getDeviceTypeName());
		return convertView;
	}

	private final class ViewCach {
		TextView but_text;
	}
}

