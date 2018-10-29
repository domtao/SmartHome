package com.zunder.smart.adapter;

import java.util.List;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.DialogAlert.OnSureListener;
import com.zunder.smart.model.Device;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.model.Device;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.tools.AppTools;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RelayAdapter extends BaseAdapter {

	List<Device> list;
	int mSelect = -1;
	int mEdite = -1;
	Activity context;

	public RelayAdapter(Activity context, List<Device> list) {
		this.list = list;
		this.context = context;
	}

	public void changeSelected(int select, int edite) {
		this.mEdite = edite;
		if (select != mSelect) {
			mSelect = select;
			notifyDataSetChanged();
		}
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
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_arce, null);
			viewCach.but_text = (TextView) convertView.findViewById(R.id.text);
			viewCach.del = (ImageView) convertView.findViewById(R.id.delArce);
			convertView.setTag(viewCach);
		} else {
			viewCach = (ViewCach) convertView.getTag();
		}
		if (mEdite != -1) {
			if (mSelect != -1 && position != list.size() - 1) {
				viewCach.del.setVisibility(View.VISIBLE);
			} else {
				viewCach.del.setVisibility(View.GONE);
			}
		} else {
			if (mSelect == list.get(position).getId()) {
				convertView.setBackgroundResource(R.drawable.emotion_bg);
			} else {
				convertView.setBackgroundResource(0);
			}
		}
		viewCach.but_text.setText(list.get(position).getDeviceName());
		viewCach.del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if(AppTools.isCharNum(list.get(position).getDeviceName())){
//					ToastUtils.ShowError(context,	context.getString(R.string.deleterelaydevice), Toast.LENGTH_SHORT,true);
//					return;
//				}
				DialogAlert alert = new DialogAlert(context);
				alert.init(list.get(position).getDeviceName(), 	context.getString(R.string.deleterelay));
				alert.setSureListener(new DialogAlert.OnSureListener() {
					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						Device device=list.get(position);
						if(DeviceFactory.getInstance().getGateWayDevice()!=null){
							device.setDeviceBackCode(DeviceFactory.getInstance().getGateWayDevice().getDeviceBackCode());
							device.setCmdDecodeType(DeviceFactory.getInstance().getGateWayDevice().getCmdDecodeType());
						}
						SendCMD.getInstance().sendCMD(247, "0",device);
						list.remove(position);
						Toast.makeText(context,
								context.getString(R.string.deleteSuccess),
								Toast.LENGTH_SHORT).show();
						SendCMD.getInstance().sendCMD(247, "2", DeviceFactory.getInstance().getGateWayDevice());
						notifyDataSetChanged();

					}
					@Override
					public void onCancle() {
						// TODO Auto-generated method stub
					}
				});
				alert.show();

			}
		});
		return convertView;
	}

	private final class ViewCach {

		TextView but_text;
		ImageView del;
	}

}
