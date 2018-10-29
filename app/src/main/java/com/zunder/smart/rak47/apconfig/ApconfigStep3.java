package com.zunder.smart.rak47.apconfig;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.zunder.smart.R;
import com.zunder.smart.rak47.simpleconfig_wizard.WLANAPI;
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
import android.widget.TextView;

public class ApconfigStep3 extends Activity {
	public static ApconfigStep3 apconfignote3;
	private TextView _apConfigNote2;
	private Button Next;
	private String get_mac = "";
	private Dialog warnDialog;
	private boolean isconfig = false;
	private boolean isscan = true;
	private int times = 10;
	private Scanner _scanner;
	private Handler handler = new Handler();
	TextView backView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apconfig_step3);
		apconfignote3 = this;
		_scanner = new Scanner(getApplicationContext());
		warnDialog = new Dialog(ApconfigStep3.this, R.style.MyDialog);

		Intent intent1 = getIntent();
		final String ssidString = intent1.getStringExtra("ssid");
		get_mac = intent1.getStringExtra("mac");
		_apConfigNote2 = (TextView) findViewById(R.id.ap_config_note2);
		if ((ApconfigStep1.type.equals("RAK475"))
				|| (ApconfigStep1.type.equals("RAK477")))
			_apConfigNote2
					.setText("Please connect to the network \""
							+ ssidString
							+ "\" have configured.\nThen click 'Scan' button to find the device.");
		else if ((ApconfigStep1.type.equals("RAK473"))
				|| (ApconfigStep1.type.equals("RAK476")))
			_apConfigNote2
					.setText("Please connect to the network \""
							+ ssidString
							+ "\" have configured.\nThen send \"at+auto_connect\" command to let the module connect to the network.\nThen click 'Scan' button to find the device.");
		Next = (Button) findViewById(R.id.next);
		Next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isscan = true;
				times = 10;
				isconfig = false;
				WLANAPI mWifiAdmin = new WLANAPI(getApplicationContext());
				if (mWifiAdmin.getSSID().contains(ssidString)) {
//					LayoutInflater warnDialog_inflater = getLayoutInflater();
//					View warnDialog_admin = warnDialog_inflater.inflate(
//							R.layout.dialog_admin,
//							(ViewGroup) findViewById(R.id.dialog_admin1));
//					TextView warn_title = (TextView) warnDialog_admin
//							.findViewById(R.id.dialog_title);
//					warn_title.setText(getString(R.string.searchdevice));
//					TextView warn_note = (TextView) warnDialog_admin
//							.findViewById(R.id.dialog_note);
//					warn_note.setText(getString(R.string.searching));
//					TextView warn_ok_btn = (TextView) warnDialog_admin
//							.findViewById(R.id.dialog_ok_btn);
//					TextView warn_cancel_btn = (TextView) warnDialog_admin
//							.findViewById(R.id.dialog_cancel_btn);
//					warnDialog.setCanceledOnTouchOutside(true);
//					warnDialog.setContentView(warnDialog_admin);
//					warn_cancel_btn
//							.setOnClickListener(new OnClickListener() {
//								@Override
//								public void onClick(View view) {
//									isscan = false;
//									warnDialog.dismiss();
//								}
//							});
//					warnDialog.show();
//
//					_scanner = new Scanner(getApplicationContext());
//					_scanner.setOnScanOverListener(new Scanner.OnScanOverListener() {
//						@Override
//						public void onResult(Map<InetAddress, RAKInfo> data,
//								InetAddress gatewayAddress) {
//							// TODO Auto-generated method stub
//							ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
//							isconfig = false;
//							if (data != null) {
//								for (Map.Entry<InetAddress, RAKInfo> entry : data
//										.entrySet()) {
//									if (isscan) {
//										String ip = entry.getKey()
//												.getHostAddress();
//										RAKInfo _rakInfo = entry.getValue();
//										String mac = _rakInfo.Mac;
//										String cmac = "";
//										if ((ApconfigStep1.type
//												.equals("RAK473"))
//												|| (ApconfigStep1.type
//														.equals("RAK476"))) {
//											cmac = mac.toUpperCase();
//										} else {
//											String[] macValueStrings = _rakInfo.Mac
//													.split(":");
//											byte macInt = (byte) (Integer
//													.parseInt(
//															macValueStrings[5],
//															16) + 1);
//											String bmac = Integer.toHexString(
//													macInt & 0xFF)
//													.toUpperCase();
//											if (bmac.length() == 1)
//												bmac = '0' + bmac;
//											cmac = macValueStrings[0] + ":"
//													+ macValueStrings[1] + ":"
//													+ macValueStrings[2] + ":"
//													+ macValueStrings[3] + ":"
//													+ macValueStrings[4] + ":"
//													+ bmac;
//										}
//
//										if (cmac.equals(get_mac)) {
//											warnDialog.dismiss();
//											isconfig = true;
//											LayoutInflater warnDialog_inflater = getLayoutInflater();
//											View warnDialog_admin = warnDialog_inflater
//													.inflate(
//															R.layout.dialog_admin,
//															(ViewGroup) findViewById(R.id.dialog_admin1));
//											TextView warn_title = (TextView) warnDialog_admin
//													.findViewById(R.id.dialog_title);
//											warn_title
//													.setText("配置设备");
//											TextView warn_note = (TextView) warnDialog_admin
//													.findViewById(R.id.dialog_note);
//											String deviceString = "";
//											deviceString = get_mac;
//											warn_note.setText(deviceString);
//											TextView warn_ok_btn = (TextView) warnDialog_admin
//													.findViewById(R.id.dialog_ok_btn);
//											warn_ok_btn
//													.setVisibility(View.VISIBLE);
//											warn_ok_btn.setText(getString(R.string.sure));
//											TextView warn_cancel_btn = (TextView) warnDialog_admin
//													.findViewById(R.id.dialog_cancel_btn);
//											warn_cancel_btn
//													.setVisibility(View.GONE);
//											warnDialog
//													.setCanceledOnTouchOutside(true);
//											warnDialog
//													.setContentView(warnDialog_admin);
//											warn_ok_btn
//													.setOnClickListener(new OnClickListener() {
//														@Override
//														public void onClick(
//																View view) {
//															warnDialog
//																	.dismiss();
//															CloseActivity();
//														}
//													});
//
//											warnDialog.show();
//											break;
//										}
//									}
//								}
//							}
//
//							if (isconfig == false) {
//								times--;
//								if (isscan && (times > 0)) {
//									handler.post(new Runnable() {
//										@Override
//										public void run() {
//											// TODO Auto-generated method stub
//											_scanner.scanAll();
//											Log.e("times==>", times + "");
//										}
//									});
//								} else {
//									warnDialog.dismiss();
//									final Dialog warnDialog = new Dialog(
//											ApconfigStep3.this,
//											R.style.MyDialog);
//									LayoutInflater warnDialog_inflater = getLayoutInflater();
//									View warnDialog_admin = warnDialog_inflater
//											.inflate(
//													R.layout.dialog_admin,
//													(ViewGroup) findViewById(R.id.dialog_admin1));
//									TextView warn_title = (TextView) warnDialog_admin
//											.findViewById(R.id.dialog_title);
//									warn_title.setText(getString(R.string.warning));
//									TextView warn_note = (TextView) warnDialog_admin
//											.findViewById(R.id.dialog_note);
//									warn_note
//											.setText("没有发现设备,请重试.");
//									TextView warn_ok_btn = (TextView) warnDialog_admin
//											.findViewById(R.id.dialog_ok_btn);
//									warn_ok_btn.setVisibility(View.VISIBLE);
//									warn_ok_btn.setText(getString(R.string.sure));
//									TextView warn_cancel_btn = (TextView) warnDialog_admin
//											.findViewById(R.id.dialog_cancel_btn);
//									warn_cancel_btn.setVisibility(View.GONE);
//									warnDialog.setCanceledOnTouchOutside(true);
//									warnDialog.setContentView(warnDialog_admin);
//									warn_ok_btn
//											.setOnClickListener(new OnClickListener() {
//												@Override
//												public void onClick(View view) {
//													warnDialog.dismiss();
//												}
//											});
//
//									warnDialog.show();
//								}
//							}
//						}
//					});
//					_scanner.scanAll();
				} else {
//					warnDialog.dismiss();
//					final Dialog warnDialog = new Dialog(ApconfigStep3.this,
//							R.style.MyDialog);
//					LayoutInflater warnDialog_inflater = getLayoutInflater();
//					View warnDialog_admin = warnDialog_inflater.inflate(
//							R.layout.dialog_admin,
//							(ViewGroup) findViewById(R.id.dialog_admin1));
//					TextView warn_title = (TextView) warnDialog_admin
//							.findViewById(R.id.dialog_title);
//					warn_title.setText(getString(R.string.warning));
//					TextView warn_note = (TextView) warnDialog_admin
//							.findViewById(R.id.dialog_note);
//					warn_note.setText("请连接网络 \""
//							+ ssidString + "\".");
//					TextView warn_ok_btn = (TextView) warnDialog_admin
//							.findViewById(R.id.dialog_ok_btn);
//					warn_ok_btn.setVisibility(View.VISIBLE);
//					warn_ok_btn.setText(getString(R.string.sure));
//					TextView warn_cancel_btn = (TextView) warnDialog_admin
//							.findViewById(R.id.dialog_cancel_btn);
//					warn_cancel_btn.setVisibility(View.GONE);
//					warnDialog.setCanceledOnTouchOutside(true);
//					warnDialog.setContentView(warnDialog_admin);
//					warn_ok_btn.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View view) {
//							warnDialog.dismiss();
//						}
//					});
//
//					warnDialog.show();
				}
			}
		});

		backView = (TextView) findViewById(R.id.backTxt);
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		CloseActivity();
		super.onDestroy();
	}

	void CloseActivity() {
		if (ApconfigStep1.apconfignote1 != null)
			ApconfigStep1.apconfignote1.finish();
		if (ApconfigStep2.apconfignote2 != null)
			ApconfigStep2.apconfignote2.finish();
	}
}
