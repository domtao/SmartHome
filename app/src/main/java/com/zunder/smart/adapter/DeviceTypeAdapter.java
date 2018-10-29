package com.zunder.smart.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.zunder.image.view.SmartImageView;
import com.zunder.smart.R;
import com.zunder.smart.json.Constants;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.DeviceType;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.zunder.smart.model.DeviceType;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DeviceTypeAdapter extends SwipeMenuAdapter<DeviceTypeAdapter.DefaultViewHolder> {

	List<DeviceType> list;
	Context context;
	int mSelect = -1;
	public DeviceTypeAdapter(Context context ,List<DeviceType> list) {
		this.list = list;
		this.context=context;
	}

	public void changeSelected(int positon) {
		if (positon != mSelect) {
			mSelect = positon;
			notifyDataSetChanged();
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
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
		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device_type, parent, false);
	}

	@Override
	public DeviceTypeAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new DeviceTypeAdapter.DefaultViewHolder(realContentView);
	}
	@Override
	public void onBindViewHolder(DeviceTypeAdapter.DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}
	class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		SmartImageView img;
		TextView but_text;
		OnItemClickListener mOnItemClickListener;
		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			img = (SmartImageView) itemView.findViewById(R.id.img);
			but_text = (TextView) itemView.findViewById(R.id.text);
		}

		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.mOnItemClickListener = onItemClickListener;
		}

		public void setData(int position) {
			String bitmap = list.get(position).getDeviceTypeImage();
			img.setImageUrl(Constants.HTTPS+bitmap);
			but_text.setText(list.get(position).getDeviceTypeName());
		}
		@Override
		public void onClick(View v) {
			if (mOnItemClickListener != null) {
				mOnItemClickListener.onItemClick(getAdapterPosition());
			}
		}
	}
}
