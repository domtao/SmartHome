package com.zunder.smart.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zunder.smart.R;
import com.zunder.smart.model.ProjectorName;

import java.util.List;

public class ProjectorNameAdapter extends BaseAdapter {

	List<ProjectorName> list;
	int mSelect = -1;
	Activity activity;

	public ProjectorNameAdapter(Activity context, List<ProjectorName> list) {
		this.list = list;
		this.activity = context;

	}

	public void changeSelected(int edite) {
		mSelect = edite;
		notifyDataSetChanged();
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
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_projector, null);
			viewCach.imageName = (TextView) convertView
					.findViewById(R.id.ProName);

			convertView.setTag(viewCach);
		} else {
			viewCach = (ViewCach) convertView.getTag();
		}

		viewCach.imageName.setText(list.get(position).getProName());


		return convertView;
	}

	private final class ViewCach {
		TextView imageName;
	}

}
