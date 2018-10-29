package com.zunder.smart.spinner;

import java.util.ArrayList;
import java.util.List;

import com.zunder.smart.R;
import com.zunder.smart.model.Room;
import com.zunder.smart.model.Room;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ArceSpinerAdapter extends BaseAdapter {

	public static interface IOnItemSelectListener {
		public void onItemClick(int pos);
	};

	private Context mContext;
	private List<Room> mObjects = new ArrayList<Room>();
	private int mSelectItem = 0;

	private LayoutInflater mInflater;

	public ArceSpinerAdapter(Context context) {
		init(context);
	}

	public void refreshData(List<Room> objects, int selIndex) {
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
	public Room getItem(int pos) {
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

		Room item =  getItem(pos);
		viewHolder.mTextView.setText(item.getRoomName());

		return convertView;
	}

	public static class ViewHolder {
		public TextView mTextView;
	}

}
