package com.zunder.smart.adapter;

import a.a.a.a.c;
import a.a.a.a.d;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import java.util.List;

import com.bumptech.glide.Glide;
import com.zunder.smart.R;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.Passive;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PassiveAdapter extends SwipeMenuAdapter<PassiveAdapter.DefaultViewHolder> {
	private static List<Passive> list;
	private Activity context;
	public static int edite = 0;
	int _posion = -1;
	int imges = 0;
	public PassiveAdapter(Activity context, List<Passive> list) {
		this.context=context;
		this.list = list;
	}

	public void changSwichSate(int posion) {
		this._posion = posion;
		notifyDataSetChanged();
	}
	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.mOnItemClickListener = onItemClickListener;
	}
	@Override
	public int getItemCount() {
		return list.size();
	}

	@Override
	public View onCreateContentView(ViewGroup parent, int viewType) {

		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_passive, parent, false);
	}
	@Override
	public PassiveAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new PassiveAdapter.DefaultViewHolder(realContentView);
	}
	@Override
	public void onBindViewHolder(PassiveAdapter.DefaultViewHolder holder, int position) {
		holder.setData(position);

		holder.setOnItemClickListener(mOnItemClickListener);
	}
class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView passiveName;
		TextView deviceID;
		ImageView img;
		ImageView imgIndex;
    LinearLayout color_ly;
		OnItemClickListener mOnItemClickListener;

		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			img = (ImageView) itemView.findViewById(R.id.img);
			passiveName = (TextView) itemView
					.findViewById(R.id.passiveName);
			deviceID = (TextView) itemView
					.findViewById(R.id.passiveID);
            color_ly=(LinearLayout)itemView.findViewById(R.id.color_ly);
			imgIndex = (ImageView) itemView
					.findViewById(R.id.passiveImg);

		}

		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.mOnItemClickListener = onItemClickListener;
		}

		public void setData(int position) {

			if (_posion == position) {
                color_ly.setBackgroundColor(R.color.gray);
			} else {
                color_ly.setBackgroundColor(Color.WHITE);
			}
			Passive device=list.get(position);
			if (device.getOnStart().equals("00")) {
				imgIndex.setVisibility(View.GONE);
			} else {
				imgIndex.setVisibility(View.VISIBLE);
			}

			passiveName.setText(device.getName());
			deviceID.setText(device.getCmdString());
//			img.setImageResource(device.getImage());
//			// viewCach.deviceID.setText(device.get);
			Glide.with(context)
					.load(device.getImage())
					.placeholder(R.mipmap.load_img)
					.crossFade()
					.into(img);
		}

		@Override
		public void onClick(View v) {
			if (mOnItemClickListener != null) {
				mOnItemClickListener.onItemClick(getAdapterPosition());
			}
		}
	}
}
