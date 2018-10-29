package com.zunder.smart.rak47;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.zunder.smart.R;
import com.example.rak47x.RAKInfo;
import com.example.rak47x.Scanner;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class LocalScanActivity extends Activity {
	private TextView scan_button;
	private Scanner _scanner;
	private ListView _listDevice;
	private rak_adapter devicelistAdapter;
	private ArrayList<HashMap<String, Object>> devicelistItem;
	private Dialog warnDialog;
	private boolean isscan = true;
	private int times = 10;
	private boolean isconfig = false;
	private Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acvtivity_local_scan);

		scan_button = (TextView) findViewById(R.id.scan_button);
		scan_button.setOnClickListener(Scan_Click);

		_listDevice = (ListView) findViewById(R.id.list_device);
		devicelistItem = new ArrayList<HashMap<String, Object>>();
		devicelistAdapter = new rak_adapter(this, devicelistItem,
				R.layout.device_list_item,
				new String[] { "groupname", "devicename", "deviceip",
						"devicemac", "devicesingnal" }, new int[] {
						R.id.Group_name, R.id.Device_name, R.id.Device_ip,
						R.id.Device_mac, R.id.Device_singnal });
		_listDevice.setAdapter(devicelistAdapter);

		warnDialog = new Dialog(LocalScanActivity.this, R.style.MyDialog);
		LayoutInflater warnDialog_inflater = getLayoutInflater();
//		View warnDialog_admin = warnDialog_inflater.inflate(
//				R.layout.dialog_admin,
//				(ViewGroup) findViewById(R.id.dialog_admin1));
//		TextView warn_title = (TextView) warnDialog_admin
//				.findViewById(R.id.dialog_title);
//		warn_title.setText("�����豸");
//		TextView warn_note = (TextView) warnDialog_admin
//				.findViewById(R.id.dialog_note);
//		warn_note.setText("�����С���");
//		TextView warn_ok_btn = (TextView) warnDialog_admin
//				.findViewById(R.id.dialog_ok_btn);
//		TextView warn_cancel_btn = (TextView) warnDialog_admin
//				.findViewById(R.id.dialog_cancel_btn);
//		warnDialog.setCanceledOnTouchOutside(true);
//		warnDialog.setContentView(warnDialog_admin);
//		warn_cancel_btn.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				isscan = false;
//			}
//		});
//		showScanner();

		_scanner = new Scanner(getApplicationContext());
		scan();
	}

	public void showScanner() {
		warnDialog.show();

	}

	public void scan() {
		_scanner.setOnScanOverListener(new Scanner.OnScanOverListener() {
			@Override
			public void onResult(Map<InetAddress, RAKInfo> data,
					InetAddress gatewayAddress) {
				// TODO Auto-generated method stub
				if (data != null) {
					for (Map.Entry<InetAddress, RAKInfo> entry : data
							.entrySet()) {
						if (isscan) {
							String ip = entry.getKey().getHostAddress();
							RAKInfo _rakInfo = entry.getValue();
							AddDeviceListItem(_rakInfo.GroupName,
									_rakInfo.NickName, ip, _rakInfo.Mac,
									_rakInfo.Rssi + "");
							isconfig = true;
						}
					}
				}
				if (isconfig == false) {
					times--;
					if (isscan && (times > 0)) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								_scanner.scanAll();
								Log.e("times==>", times + "");
							}
						});
					} else {
						warnDialog.dismiss();
//						final Dialog warnDialog = new Dialog(
//								LocalScanActivity.this, R.style.MyDialog);
//						LayoutInflater warnDialog_inflater = getLayoutInflater();
//						View warnDialog_admin = warnDialog_inflater.inflate(
//								R.layout.dialog_admin,
//								(ViewGroup) findViewById(R.id.dialog_admin1));
//						TextView warn_title = (TextView) warnDialog_admin
//								.findViewById(R.id.dialog_title);
//						warn_title.setText("����");
//						TextView warn_note = (TextView) warnDialog_admin
//								.findViewById(R.id.dialog_note);
//						warn_note.setText("û���ҵ��豸,������.");
//						TextView warn_ok_btn = (TextView) warnDialog_admin
//								.findViewById(R.id.dialog_ok_btn);
//						warn_ok_btn.setVisibility(View.VISIBLE);
//						warn_ok_btn.setText("ȷ ��");
//						TextView warn_cancel_btn = (TextView) warnDialog_admin
//								.findViewById(R.id.dialog_cancel_btn);
//						warn_cancel_btn.setVisibility(View.GONE);
//						warnDialog.setCanceledOnTouchOutside(true);
//						warnDialog.setContentView(warnDialog_admin);
//						warn_ok_btn
//								.setOnClickListener(new OnClickListener() {
//									@Override
//									public void onClick(View view) {
//										warnDialog.dismiss();
//									}
//								});
//
//						warnDialog.show();
					}
				} else {
					warnDialog.dismiss();
				}
			}
		});
		_scanner.scanAll();
	}

	/**
	 * Scan
	 */
	OnClickListener Scan_Click = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			devicelistItem.clear();
			showScanner();
			_scanner.scanAll();
		}
	};

	/**
	 * Add Device List
	 */
	private void AddDeviceListItem(String groupname, String devicename,
			String deviceip, String devicemac, String devicesignal) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		// add text
		map.put("groupname", groupname);
		map.put("devicename", devicename);
		map.put("deviceip", deviceip);
		map.put("devicemac", devicemac);
		map.put("devicesingnal", devicesignal);
		devicelistItem.add(map);
		devicelistAdapter.notifyDataSetChanged();
	}
}
