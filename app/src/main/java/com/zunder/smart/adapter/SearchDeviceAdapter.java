package com.zunder.smart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.zunder.image.view.SmartImageView;
import com.zunder.smart.R;
import com.zunder.smart.json.Constants;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.Device;
import com.zunder.smart.service.SendCMD;

import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchDeviceAdapter extends  RecyclerView.Adapter<SearchDeviceAdapter.DefaultViewHolder> {
	private static List<Device> list;
	public static int edite = 0;
	int posion = -1;
	int pos = -1;
	static int muCheck = -1;
	int imgsId = 0;
	int imges = 0;
	public static Map<String, Integer> map;
	private static HashMap<Integer, Boolean> isSelected;
	static Context context;
	public static String isChange = "No";


	@SuppressWarnings("static-access")
	public SearchDeviceAdapter(Context context, List<Device> list) {
		map = new HashMap<String, Integer>();
		this.context=context;
		this.list = list;
		isSelected = new HashMap<Integer, Boolean>();
		initDate(muCheck);
	}
	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public void changSwichSate(int posion, int imgsId) {
		this.posion = posion;
		this.imgsId = imgsId;
		notifyDataSetChanged();
	}
	public void muCheck(int _muCheck) {
		muCheck = _muCheck;
		if(isSelected!=null){
			initDate(_muCheck);
		}
		notifyDataSetChanged();
	}

	private void initDate(int isCheckAll) {
		for (int i = 0; i < list.size(); i++) {
			if(isCheckAll==1){
				isSelected.put(i, true);
			}else{
				isSelected.put(i, false);
			}
		}
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
	public SearchDeviceAdapter.DefaultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new SearchDeviceAdapter.DefaultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_device, parent, false));
	}

	@Override
	public void onBindViewHolder(SearchDeviceAdapter.DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}

	static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView deviceKey;
		TextView deviceType;
		TextView deviceID;
		SmartImageView img;
		TextView tranceType;
		CheckBox relayCheck;
		CheckBox routeCheck;
		CheckBox cb;
		OnItemClickListener mOnItemClickListener;

		public DefaultViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			img = (SmartImageView) itemView.findViewById(R.id.img);
			deviceKey = (TextView) itemView
					.findViewById(R.id.deviceKey);
			deviceID = (TextView) itemView
					.findViewById(R.id.deviceID);
			deviceType = (TextView) itemView
					.findViewById(R.id.deviceType);
			 tranceType = (TextView) itemView
					 .findViewById(R.id.tranceType);
			 relayCheck = (CheckBox) itemView
					 .findViewById(R.id.relayCheck);
			 routeCheck = (CheckBox) itemView
					 .findViewById(R.id.routeCheck);
			cb = (CheckBox) itemView.findViewById(R.id.item_cb);


		}

		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.mOnItemClickListener = onItemClickListener;
		}

		public void setData(final int position) {
			final Device device = list.get(position);
			if(device.getCmdDecodeType()==1) {
				deviceKey.setText((position + 1) + ":" + device.getPrimary_Key());
				deviceType.setText(device.getDeviceTimer());
				deviceID.setText(context.getString(R.string.deviceId) + ":" + device.getDeviceID());
				img.setImageUrl(Constants.HTTPS + device.getDeviceImage());
				if (muCheck >= 0) {
					cb.setVisibility(View.VISIBLE);
					if (isSelected.get(position)) {
						cb.setChecked(true);
					} else {
						cb.setChecked(false);
					}
				} else {
					cb.setVisibility(View.GONE);
				}
				if (device.getTranceType() == 0) {
					tranceType.setText("无线");
				} else if (device.getTranceType() == 1) {
					tranceType.setText("有线");
				} else if (device.getTranceType() == 2) {
					tranceType.setText("双协议");
				} else {
					tranceType.setText("");
				}
				if (device.getRelayIndex() > 0) {
					relayCheck.setChecked(true);
				} else {
					relayCheck.setChecked(false);
				}
				if (device.getRouteIndex() > 0) {
					routeCheck.setChecked(true);
				} else {
					routeCheck.setChecked(false);
				}
			}else{
				deviceKey.setText("情景代码:" + Integer.valueOf(device.getPrimary_Key(), 16));
				deviceType.setText(device.getDeviceTimer());
				deviceID.setText("情景IO:" + device.getDeviceID());
			}
			cb.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (((CheckBox) v).isChecked()) {
						isSelected.put(position, true);
					} else {
						isSelected.put(position, false);
					}
					setData(position);
				}
				// }
			});

			img.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//joe 点击图标发送命令
					String dioStr="0";
					String checkIOStr=device.getPrimary_Key().substring(2,4);
					if (!checkIOStr.equals("FA")) {
						dioStr = device.getPrimary_Key().substring(3, 4);
					}
					String netCmdStr ="*C0019FA" +device.getPrimary_Key().substring(4,12)+"0"+dioStr
							+"000000";
					Log.e("Code",netCmdStr);
					SendCMD.getInstance().sendCmd(200,netCmdStr,null);
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
