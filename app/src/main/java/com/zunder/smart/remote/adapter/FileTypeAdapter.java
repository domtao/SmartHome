package com.zunder.smart.remote.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zunder.smart.R;
import com.zunder.smart.remote.model.FileType;
import com.zunder.smart.remote.model.FileType;

import java.util.List;

public class FileTypeAdapter extends BaseAdapter {

	List<FileType> list;
	int mSelect = -1;
	Activity activity;

	public FileTypeAdapter(Activity context, List<FileType> list) {
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
			convertView = inflater.inflate(R.layout.item_filetype, null);
			viewCach.imageName = (TextView) convertView
					.findViewById(R.id.imageName);
			viewCach.imageImg = (ImageView) convertView
					.findViewById(R.id.imageImg);

			convertView.setTag(viewCach);
		} else {
			viewCach = (ViewCach) convertView.getTag();
		}
		viewCach.imageName.setText(list.get(position).getTypeName());

		Glide.with(activity)
				.load(list.get(position).getTypeImage())
				.placeholder(R.mipmap.http_icon)
				.crossFade()
				.into(viewCach.imageImg);
		return convertView;
	}

	private final class ViewCach {

		ImageView imageImg;
		TextView imageName;
	}

}
