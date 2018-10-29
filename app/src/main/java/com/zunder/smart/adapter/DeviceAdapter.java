package com.zunder.smart.adapter;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.zunder.image.view.SmartImageView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;

import com.zunder.smart.activity.device.DeviceFragment;

import com.zunder.smart.json.Constants;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.Device;

import com.zunder.smart.service.SendCMD;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class DeviceAdapter extends SwipeMenuAdapter<DeviceAdapter.DefaultViewHolder> {
	public static List<Device> list;

	public static int edite = 0;
	int muCheck = -1;
	int posion = -1;
	int imgsId = 0;
	int roomId;
	public static Map<String, Integer> map;
	private static HashMap<Integer, Boolean> isSelected;
	static Activity activity;
	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static String isChange1 = "No";

	@SuppressWarnings("static-access")
	public DeviceAdapter(Activity activity, List<Device> list,int roomId) {
		super();
		map = new HashMap<String, Integer>();
		this.list = list;
		this.roomId=roomId;
		this.activity=activity;
		isSelected = new HashMap<Integer, Boolean>();
		initDate(muCheck);
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



	public List<Device> getItems() {
		return list;
	}

	@Override
	public long getItemId(int position) {
		return position;
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

	public void setList(List<Device> _list) {
		this.list = _list;
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

		return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
	}
	@Override
	public void onBindViewHolder(DefaultViewHolder holder, int position) {
		holder.setData(position);
		holder.setOnItemClickListener(mOnItemClickListener);
	}
	@Override
	public DeviceAdapter.DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
		return new DeviceAdapter.DefaultViewHolder(realContentView);
	}
	class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
				TextView title;
				TextView deviceName;
				TextView deviceEvent;
				CheckBox checkBox;
				SmartImageView img;
				TextView typeName;
				CheckBox cb;
				OnItemClickListener mOnItemClickListener;
				public DefaultViewHolder(View itemView) {
					super(itemView);
					itemView.setOnClickListener(this);
					title = (TextView) itemView.findViewById(R.id.title);
					img = (SmartImageView) itemView.findViewById(R.id.img);
					deviceName = (TextView) itemView
							.findViewById(R.id.deviceName);
					deviceEvent = (TextView) itemView
							.findViewById(R.id.deviceEvent);
					typeName= (TextView) itemView
							.findViewById(R.id.typeName);
					checkBox = (CheckBox) itemView
							.findViewById(R.id.checkBox_on);
					cb = (CheckBox) itemView.findViewById(R.id.item_cb);
				}
				public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
					this.mOnItemClickListener = onItemClickListener;
				}
				public void setData(final int position) {
					if(muCheck >=0){
						cb.setVisibility(View.VISIBLE);
						if(isSelected.get(position)){
							cb.setChecked(true);
						}else{
							cb.setChecked(false);
						}
					}else{
						cb.setVisibility(View.GONE);
					}
					final Device device = list.get(position);
					if(roomId==0){
						deviceName.setText(device.getRoomName()+device.getDeviceName());
					}else {
						deviceName.setText(device.getDeviceName());
					}
					deviceEvent.setText("设备ID:"+device.getDeviceID());
					typeName.setText(device.getDeviceTypeName());

					if(device.getProductsIO()>0){
						typeName.setText(device.getProductsName()+" 回路"+(Integer.parseInt(device.getDeviceIO())+1));
					}else{
						typeName.setText(device.getProductsName());
					}
					img.setImageUrl(Constants.HTTPS+device.getDeviceImage());

					if (device.getProductsCode().equals("FF")
							|| device.getProductsCode().equals("C9")
							|| device.getProductsCode().equals("65")) {
						checkBox.setBackgroundResource(R.drawable.check_selector);
						device.setDeviceOnLine(0);
					} else {
							if (!device.getProductsCode().equals("AD")
									|| device.getProductsCode().equals("FF")
									|| device.getProductsCode().equals("C9")
									|| device.getProductsCode().equals("65")) {
								SendCMD.getInstance().sendCMD(255, device.getDeviceName(),
										device);
							}
						checkBox.setBackgroundResource(R.drawable.checkbox_selector);
					}
					cb.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (((CheckBox) v).isChecked()) {
								isSelected.put(position, true);
							} else {
								isSelected.put(position, false);
							}
							notifyDataSetChanged();
						}
						// }
					});
					checkBox.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String deviceName = device.getDeviceName();
							SendCMD cmdsend = SendCMD.getInstance();
							if (device.getProductsCode().equals("FF")
									|| device.getProductsCode().equals("C9")
									|| device.getProductsCode().equals("64")
									|| device.getProductsCode().equals("65")) {
								cmdsend.sendCMD(0, deviceName, device);
							} else if( device.getProductsCode().equals("06")){
								if (((CheckBox) v).isChecked()) {
									((CheckBox) v).setChecked(false);
								} else {
								((CheckBox) v).setChecked(true);
								}
							}else {
								if (((CheckBox) v).isChecked()) {
									cmdsend.sendCMD(
											0,
											deviceName + MyApplication.getInstance().getString(R.string.open_1),
											device);
								} else {
									cmdsend.sendCMD(
											0,
											deviceName
													+ MyApplication.getInstance().getString(R.string.close_1),
											device);
								}
							}
						}
						// }
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
