package com.zunder.smart.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zunder.smart.R;
import com.zunder.smart.model.History;

import java.util.List;

public class SafeInfoAdapter extends BaseAdapter {

	List<History> list;
	int mSelect = -1;
	int mEdite = -1;
	Activity context;

	public SafeInfoAdapter(Activity context, List<History> list) {
		this.list = list;
		this.context = context;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public void changeIndex(int index) {
		if (mSelect != index) {
			mSelect = index;
			notifyDataSetChanged();
		}
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
			convertView = inflater.inflate(R.layout.item_safeinfo, null);
			viewCach.historyName = (TextView) convertView
					.findViewById(R.id.historyName);
			viewCach.historyTime = (TextView) convertView
					.findViewById(R.id.historyTime);
			viewCach.title = (TextView) convertView
					.findViewById(R.id.title);


			convertView.setTag(viewCach);
		} else {
			viewCach = (ViewCach) convertView.getTag();
		}
		if (needTitle(position)) {
			viewCach.title.setText((list.get(position).getCreateTime().split(" "))[0]);
			viewCach.title.setVisibility(View.VISIBLE);
		} else {
			viewCach.title.setVisibility(View.GONE);
		}
		viewCach.historyName.setText(list.get(position).getHistoryName());
		viewCach.historyTime.setText((list.get(position).getCreateTime().split(" "))[1]);
		return convertView;
	}

	private final class ViewCach {

		TextView historyName;
		TextView historyTime;
		TextView title;
	}
	private boolean needTitle(int position) {

		if (position == 0) {
			return true;
		}

		if (position < 0) {
			return false;
		}

		History currentEntity = (History) getItem(position);
		History previousEntity = (History) getItem(position - 1);
		if (null == currentEntity || null == previousEntity) {
			return false;
		}
		String currentTitle = currentEntity.getCreateTime().split(" ")[0];
		String previousTitle = previousEntity.getCreateTime().split(" ")[0];
		if (null == previousTitle || null == currentTitle) {
			return false;
		}
		if (currentTitle.equals(previousTitle)) {
			return false;
		}

		return true;
	}
}
