package com.zunder.smart.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.tv.ChannelAddActivity;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.RedInfra;

import java.util.List;

public class ChannelAdapter extends SwipeMenuAdapter<ChannelAdapter.DefaultViewHolder> {
	public static List<Device> list;
	public static int edite = 0;

	int posion = -1;

	static Activity activity;



	@SuppressWarnings("static-access")
	public ChannelAdapter(Activity activity, List<Device> list) {
		super();
		this.list = list;
		this.activity=activity;

	}

	public List<Device> getItems() {
		return list;
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
		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel, parent, false);
	}
	@Override
	public void onBindViewHolder(DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}
	@Override
	public ChannelAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new ChannelAdapter.DefaultViewHolder(realContentView);
	}
	class DefaultViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
		TextView textName;
		TextView textNum;
		ImageView editeImg;
		Button delBtn;
		Button editeBtn;
		LinearLayout editeLaout;
		OnItemClickListener mOnItemClickListener;
		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			textName = (TextView) itemView.findViewById(R.id.textName);
			textNum = (TextView) itemView.findViewById(R.id.textNum);
			editeLaout=(LinearLayout)itemView.findViewById(R.id.editeLaout);
			 editeImg=(ImageView)itemView.findViewById(R.id.editeImage);
			 delBtn=(Button) itemView.findViewById(R.id.delBtn);
			 editeBtn=(Button) itemView.findViewById(R.id.editeBtn);

		}
		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.mOnItemClickListener = onItemClickListener;
		}
		public void setData(final int position) {
			Device device = list.get(position);
			textName.setText(device.getDeviceName());
			String deviceId=device.getDeviceID();
			int len=Integer.parseInt(deviceId.substring(0,1));
			textNum.setText(deviceId.substring(1,(len+1)));
			editeImg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					editeLaout.setVisibility(View.VISIBLE);
				}
			});
			delBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					DialogAlert alert = new DialogAlert(activity);
					alert.init(list.get(position).getDeviceName(),
							activity.getString(R.string.isDelRedFra));
					alert.setSureListener(new DialogAlert.OnSureListener() {

						@Override
						public void onSure() {
							// TODO Auto-generated method stub
							int result = MyApplication.getInstance()
									.getWidgetDataBase()
									.deleteDevice(list.get(position).getId());
							if (result > 0) {
								ToastUtils.ShowSuccess(activity,
										activity.getString(R.string.deleteSuccess),
										Toast.LENGTH_SHORT,true);
								list.remove(position);
								notifyDataSetChanged();
								DeviceFactory.getInstance().clearList();
							}
						}
						@Override
						public void onCancle() {
							// TODO Auto-generated method stub
						}
					});
					alert.show();
				}
			});
			editeBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					Intent intent = new Intent(activity,
							ChannelAddActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("Edite", "Edite");
					bundle.putInt("id", list.get(position).getId());
					intent.putExtras(bundle);
					activity.startActivityForResult(intent, 100);
				}
			});
		}
		@Override
		public void onClick(View v) {
			if (mOnItemClickListener != null) {
				editeLaout.setVisibility(View.GONE);
				mOnItemClickListener.onItemClick(getAdapterPosition());
			}
		}
	}
}
