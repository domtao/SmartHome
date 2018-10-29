package com.zunder.smart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.bumptech.glide.Glide;
import com.zunder.smart.R;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.Device;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.zunder.smart.model.Device;

import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class PassiveListAdapter extends SwipeMenuAdapter<PassiveListAdapter.DefaultViewHolder> {
	public static List<Device> list;
	public static int edite = 0;
	int posion = -1;
	int pos = -1;
	int imgsId = 0;
	int imges = 0;
	static Context context;

	public PassiveListAdapter(Context context,List<Device> list) {
		this.context=context;
		this.list = list;
	}

	public void changSwichSate(int posion, int imgsId) {
		this.posion = posion;
		this.imgsId = imgsId;
		notifyDataSetChanged();
	}



	@Override
	public long getItemId(int position) {
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

		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
	}

	@Override
	public PassiveListAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new PassiveListAdapter.DefaultViewHolder(realContentView);
	}
	@Override
	public void onBindViewHolder(PassiveListAdapter.DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}
	static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView title;
		TextView deviceName;
		TextView deviceEvent;
		CheckBox checkBox;
		ImageView img;
		OnItemClickListener mOnItemClickListener;

		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			title = (TextView) itemView.findViewById(R.id.title);
			img = (ImageView) itemView.findViewById(R.id.img);
			deviceName = (TextView) itemView
					.findViewById(R.id.deviceName);
			deviceEvent = (TextView) itemView
					.findViewById(R.id.deviceEvent);
			checkBox = (CheckBox) itemView
					.findViewById(R.id.checkBox_on);

		}

		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.mOnItemClickListener = onItemClickListener;
		}

		public void setData(int position) {
			Device device=list.get(position);

			deviceName.setText(device.getDeviceName());

			Glide.with(context)
					.load(device.getDeviceImage())
					.placeholder(R.mipmap.load_img)
					.crossFade()
					.into(img);
//			int imgId = DeviceTypeIMG.initDeviceCtroll(device.getProductsCode(),
//					device.getLoadImageIndex(),
//					Integer.parseInt(device.getDeviceIO()));
//			if (imgId != -1) {
//				img.setImageResource(imgId);
//			}


			String delay = device.getModeDelayed();
			String modePeriod = device.getModePeriod();
			String temp = "";
			if (!delay.equals("00") && !delay.equals("0") && !delay.equals("")) {
				temp = "#" + delay;
			}
			if (!modePeriod.equals("00:00--00:00") && !modePeriod.equals("")) {
				temp = temp + "(" + modePeriod + ")";
			}
			deviceEvent.setText(device.getModeEvent() + temp);
		}

		@Override
		public void onClick(View v) {
			if (mOnItemClickListener != null) {
				mOnItemClickListener.onItemClick(getAdapterPosition());
			}
		}
	}
}
