package com.zunder.smart.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zunder.smart.R;
import com.zunder.smart.model.DeviceIo;
import com.zunder.smart.model.Mode;
import com.zunder.smart.model.Mode;

import java.util.List;

public class ModeIoAdapter extends BaseAdapter {

	List<Mode> list;
	int mSelect = -1;
	Activity context;

	public ModeIoAdapter(Activity context, List<Mode> list) {
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
			convertView = inflater.inflate(R.layout.item_modeio, null);
			viewCach.but_text = (TextView) convertView.findViewById(R.id.text);
			viewCach.textCode = (TextView) convertView.findViewById(R.id.textCode);
			convertView.setTag(viewCach);
		} else {
			viewCach = (ViewCach) convertView.getTag();
		}
		if (mSelect == list.get(position).getId()) {
			convertView.setBackgroundResource(R.drawable.emotion_bg);
		} else {
			convertView.setBackgroundResource(0);
		}
		Log.e("pos",position+"_"+list.get(position).getModeType());
		if (list.get(position).getModeType().equals("C9")) {
			viewCach.but_text.setText(list.get(position).getModeName());
		}else {
			viewCach.but_text.setText(list.get(position).getModeName());
			viewCach.textCode.setText(list.get(position).getModeCode()+"");
		}
		return convertView;
	}

	private final class ViewCach {
		TextView but_text;
		TextView textCode;
	}

}
