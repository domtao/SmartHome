package com.zunder.smart.adapter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.zunder.smart.R;
import com.zunder.smart.model.GateWayMsg;
import com.zunder.smart.model.GateWayMsg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GateWayMsgAdapter extends BaseAdapter {

	TextView cameraName = null;
	TextView cameraDid = null;
	ImageView cameraImage = null;
	List<GateWayMsg> list;
	Context context;
	private int[] images = { R.mipmap.camera_1, R.mipmap.camera_2,
			R.mipmap.camera_3, R.mipmap.camera_4, R.mipmap.camera_5 };

	public GateWayMsgAdapter(Context context, List<GateWayMsg> list) {
		this.context = context;
		this.list = list;

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
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_gateway_dialog, null);
			cameraName = (TextView) convertView.findViewById(R.id.cameraName);
			cameraDid = (TextView) convertView.findViewById(R.id.cameraDid);
			cameraImage = (ImageView) convertView
					.findViewById(R.id.cameraImage);
			viewCach = new ViewCach();
			viewCach.cameraDid = cameraDid;
			viewCach.cameraName = cameraName;
			viewCach.cameraImage = cameraImage;
			convertView.setTag(viewCach);
		} else {
			viewCach = (ViewCach) convertView.getTag();
			cameraName = viewCach.cameraName;
			cameraDid = viewCach.cameraDid;
			cameraImage = viewCach.cameraImage;
		}
		cameraName.setText(list.get(position).getDeviceName());
		cameraDid.setText(list.get(position).getDeviceID());
		cameraImage
				.setImageResource(images[list.get(position).getProductsCode()]);

		return convertView;
	}

	private final class ViewCach {
		TextView cameraDid;
		TextView cameraName;
		ImageView cameraImage;
	}

}
