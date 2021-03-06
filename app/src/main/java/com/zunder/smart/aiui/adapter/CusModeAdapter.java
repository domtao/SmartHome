package com.zunder.smart.aiui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.List;

import com.bumptech.glide.Glide;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.aiui.activity.AiuiMainActivity;
import com.zunder.smart.dao.impl.factory.DeviceTypeFactory;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.Device;
import com.zunder.smart.socket.info.ISocketCode;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class CusModeAdapter extends SwipeMenuAdapter<CusModeAdapter.DefaultViewHolder> {
	private static List<Device> list;
	public static int edite = 0;
	int posion = -1;
	int pos = -1;
	int imgsId = 0;
	int imges = 0;
	static Context context;
	public static String isChange = "No";

	@SuppressWarnings("static-access")
	public CusModeAdapter(Context context,List<Device> _list)
	{
		this.context=context;
		this.list = _list;
	}

	public void changSwichSate(int posion, int imgsId) {
		this.posion = posion;
		this.imgsId = imgsId;
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

	public List<Device> getItems() {
		return list;
	}
	@Override
	public View onCreateContentView(ViewGroup parent, int viewType) {

		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
	}

	@Override
	public CusModeAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new CusModeAdapter.DefaultViewHolder(realContentView);
	}
	@Override
	public void onBindViewHolder(CusModeAdapter.DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}
	static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView deviceName;
		TextView deviceEvent;
		CheckBox checkBox;
		ImageView img;
		OnItemClickListener mOnItemClickListener;

		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
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
			final Device device=list.get(position);
			deviceName.setText(device.getDeviceName());

			Glide.with(context)
					.load(DeviceTypeFactory.getInstance().getBitmapByID(device
							.getDeviceTypeKey()))
					.placeholder(R.mipmap.icon_cj_mr)
					.crossFade()
					.into(img);
			checkBox.setBackgroundResource(R.drawable.check_selector);
			checkBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					String deviceName = device.getDeviceName();

					if (((CheckBox) v).isChecked()) {
						deviceName = deviceName
								+ MyApplication.getInstance().getString(R.string.open_1);
					} else {
						deviceName = deviceName
								+ MyApplication.getInstance().getString(R.string.close_1);
					}

					String result = ISocketCode.setForwardNameControl(deviceName,
							AiuiMainActivity.deviceID);
					MainActivity.getInstance().sendCode(result);
				}

			});
		}

		@Override
		public void onClick(View v) {
			if (mOnItemClickListener != null) {
				mOnItemClickListener.onItemClick(getAdapterPosition());
			}
		}
	}

}
