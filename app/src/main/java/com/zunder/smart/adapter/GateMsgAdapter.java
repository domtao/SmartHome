package com.zunder.smart.adapter;

import java.util.List;

import com.zunder.smart.R;
import com.zunder.smart.model.GateWayMsg;
import com.zunder.smart.model.GateWayMsg;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GateMsgAdapter extends BaseAdapter {

	List<GateWayMsg> list;
	int mSelect = -1;
	int mEdite = -1;
	Activity context;

	public GateMsgAdapter(Activity context, List<GateWayMsg> list) {
		this.list = list;
		this.context = context;

	}

	public void changeSelected(int select, int edite) {
		this.mEdite = edite;
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
		viewCach.but_text.setText(list.get(position).getDeviceName());

		return convertView;
	}

	private final class ViewCach {

		TextView but_text;
		ImageView del;
	}

}
