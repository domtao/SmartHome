package com.zunder.smart.adapter;

import android.view.View;

import android.view.ViewGroup;
import java.util.List;

import com.zunder.smart.R;
import com.zunder.smart.model.ModeImage;
import com.zunder.smart.model.ModeImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ModeImageAdapter extends BaseAdapter {


	private List<ModeImage> list;
	private Context context;
	int mSelect = -1;

	public ModeImageAdapter(Context context, List<ModeImage> list) {
		super();
		this.context = context;
		this.list = list;
	

	}

	public void changeSelected(int positon) {
		if (positon != mSelect) {
			mSelect = positon;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return list.size();

	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
					
					
					LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_mode_img, null);
			viewHolder = new ViewHolder();

			viewHolder.image = (ImageView) convertView.findViewById(R.id.img);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (mSelect == position) {
			convertView.setBackgroundResource(R.drawable.emotion_bg);
		} else {
			convertView.setBackgroundResource(0);
		}
		Bitmap bitmap = list.get(position).getImgId();
		if (bitmap != null) {
			viewHolder.image.setImageBitmap(bitmap);
		} else {
			viewHolder.image.setImageResource(R.mipmap.icon_cj_mr);
		}

		return convertView;

	}

	final class ViewHolder {
		public ImageView image;
	}
}
