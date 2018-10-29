package com.zunder.smart.adapter;

import java.util.List;

import com.zunder.smart.R;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyWifiAdapter extends BaseAdapter {
	LayoutInflater inflater;
	List<ScanResult> list;
	Context context;
	public MyWifiAdapter(Context context, List<ScanResult> list) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
		this.list = list;
		this.context=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		view = inflater.inflate(R.layout.wifi_item, null);
		ScanResult scanResult = list.get(position);
		TextView name = (TextView) view.findViewById(R.id.wifi_name);
		TextView plus = (TextView) view.findViewById(R.id.wifi_plus);
		TextView signalStrenth = (TextView) view
				.findViewById(R.id.signal_strenth);
		name.setText(scanResult.SSID);
		String capabilities = scanResult.capabilities;
		String encrypt = "";
		if (capabilities.indexOf("WPA2-PSK") != -1) {
			encrypt = "WPA2_PSK";
		} else if (capabilities.indexOf("WPA-PSK") != -1
				|| capabilities.indexOf("WPA1-PSK") != -1) {
			encrypt = "WPA_PSK";
		} else if (capabilities.indexOf("WEP") != -1) {
			encrypt = "WEP";
		} else {
			encrypt = "NONE";
		}
		if (capabilities.indexOf("CCMP") != -1
				&& capabilities.indexOf("TKIP") != -1) {
			encrypt += "(TKIPAES)";
		} else if (capabilities.indexOf("CCMP") != -1) {
			encrypt += "(AES)";
		} else if (capabilities.indexOf("TKIP") != -1) {
			encrypt += "(TKIP)";
		}
		plus.setText(context.getString(R.string.safe) + encrypt);
		int levelVal = Math.abs(scanResult.level);
		if (levelVal > 100) {
			levelVal = 0;
		} else if (levelVal > 80) {
			levelVal = 50 + (100 - levelVal);
		} else if (levelVal > 70) {
			levelVal = 70 + (80 - levelVal);
		} else if (levelVal > 60) {
			levelVal = 80 + (70 - levelVal);
		} else if (levelVal > 50) {
			levelVal = 90 + (60 - levelVal);
		} else {
			levelVal = 100;
		}
		signalStrenth.setText(context.getString(R.string.signal_intensity)+":" + levelVal + "("
				+ list.get(position).frequency / 100 + ")");
		return view;
	}
}
