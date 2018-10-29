package com.zunder.smart.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.zunder.smart.R;
import com.zunder.image.model.ImageNet;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {

	List<ImageNet> list;
	int mSelect = -1;
	Activity activity;

	public ImageAdapter(Activity context, List<ImageNet> list) {
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
			convertView = inflater.inflate(R.layout.item_image, null);
			viewCach.imageName = (TextView) convertView
					.findViewById(R.id.ImageName);
			viewCach.imgSrc = (ImageView) convertView
					.findViewById(R.id.imgSrc);

			convertView.setTag(viewCach);
		} else {
			viewCach = (ViewCach) convertView.getTag();
		}
		if (mSelect == position) {
			convertView.setBackgroundResource(R.drawable.emotion_bg);
		} else {
			convertView.setBackgroundResource(0);
		}
		viewCach.imageName.setText(list.get(position).getImageName());

		Glide.with(activity)
				.load(list.get(position).getImageUrl())
				.placeholder(R.mipmap.home)
				.crossFade()
				.into(viewCach.imgSrc);
		return convertView;
	}

	private final class ViewCach {
		TextView imageName;
		ImageView imgSrc;
	}

}
