package com.zunder.smart.rak47;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zunder.smart.R;
import com.zunder.smart.rak47.apconfig.ApconfigStep1;
import com.zunder.smart.rak47.apconfig.ApconfigStep2;
import com.zunder.smart.rak47.apconfig.ApconfigStep3;
import com.example.rak47x.RAKInfo;
import com.example.rak47x.Scanner;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ScanInfoActivity extends Activity {
	private Scanner _scanner;
	private ListView _listDevice;
	private rak_adapter devicelistAdapter;
	private ArrayList<HashMap<String, Object>> devicelistItem;
	private Dialog warnDialog;
	private boolean isscan=true;
	private int times=10;
	private boolean isconfig=false;
	private Handler handler=new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_info);
		
		_listDevice = (ListView) findViewById(R.id.list_device);
        devicelistItem = new ArrayList<HashMap<String, Object>>();
		devicelistAdapter = new rak_adapter(this,devicelistItem, R.layout.device_list_item,
				            new String[]{"groupname","devicename","deviceip","devicemac","devicesingnal"}, 
			                new int[]{R.id.Group_name,R.id.Device_name,R.id.Device_ip,R.id.Device_mac,R.id.Device_singnal});
		_listDevice.setAdapter(devicelistAdapter);
		
		warnDialog= new Dialog(ScanInfoActivity.this,R.style.MyDialog);
		LayoutInflater warnDialog_inflater =getLayoutInflater();
//		View warnDialog_admin=warnDialog_inflater.inflate(R.layout.dialog_admin, (ViewGroup)findViewById(R.id.dialog_admin1));
//		TextView warn_title =(TextView)warnDialog_admin.findViewById(R.id.dialog_title);
//		warn_title.setText("Scan device");
//		TextView warn_note =(TextView)warnDialog_admin.findViewById(R.id.dialog_note);
//		warn_note.setText("Scaning device...");
//		TextView warn_ok_btn =(TextView)warnDialog_admin.findViewById(R.id.dialog_ok_btn);
//		TextView warn_cancel_btn =(TextView)warnDialog_admin.findViewById(R.id.dialog_cancel_btn);
//		warnDialog.setCanceledOnTouchOutside(true);
//		warnDialog.setContentView(warnDialog_admin);
//		warn_cancel_btn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				isscan=false;
//			}
//		});
//		warnDialog.show();
		
		_scanner=new Scanner(getApplicationContext());
        _scanner.setOnScanOverListener(new Scanner.OnScanOverListener() {
			@Override
			public void onResult(Map<InetAddress, RAKInfo> data,
					InetAddress gatewayAddress) {
				// TODO Auto-generated method stub
                if (data != null) {
                    for (Map.Entry<InetAddress, RAKInfo> entry : data.entrySet()) {
                    	if(isscan){
	                    	String ip = entry.getKey().getHostAddress();
	                    	RAKInfo _rakInfo=entry.getValue();
	                    	AddDeviceListItem(_rakInfo.GroupName,
	                    					_rakInfo.NickName,
	                    					ip,
	                    					_rakInfo.Mac,
	                    					_rakInfo.Rssi+"");
	                    	isconfig=true;
                    	}
                    }
                }
                if(isconfig==false){
                	times--;
                	if(isscan&&(times>0)){                      		
                		handler.post(new Runnable()
						{
							@Override
							public void run()
							{
								// TODO Auto-generated method stub
								_scanner.scanAll();
								Log.e("times==>", times+"");
							}
						});
                	}
                	else{
//		                warnDialog.dismiss();
//		                final Dialog warnDialog= new Dialog(ScanInfoActivity.this,R.style.MyDialog);
//						LayoutInflater warnDialog_inflater =getLayoutInflater();
//						View warnDialog_admin=warnDialog_inflater.inflate(R.layout.dialog_admin, (ViewGroup)findViewById(R.id.dialog_admin1));
//						TextView warn_title =(TextView)warnDialog_admin.findViewById(R.id.dialog_title);
//						warn_title.setText("Warning");
//						TextView warn_note =(TextView)warnDialog_admin.findViewById(R.id.dialog_note);
//						warn_note.setText("Not found device,please retry.");
//						TextView warn_ok_btn =(TextView)warnDialog_admin.findViewById(R.id.dialog_ok_btn);
//						warn_ok_btn.setVisibility(View.VISIBLE);
//						warn_ok_btn.setText("OK");
//						TextView warn_cancel_btn =(TextView)warnDialog_admin.findViewById(R.id.dialog_cancel_btn);
//						warn_cancel_btn.setVisibility(View.GONE);
//						warnDialog.setCanceledOnTouchOutside(true);
//						warnDialog.setContentView(warnDialog_admin);
//						warn_ok_btn.setOnClickListener(new View.OnClickListener() {
//							@Override
//							public void onClick(View view) {
//								warnDialog.dismiss();
//								CloseActivity();
//							}
//						});
//
//						warnDialog.show();
                	}
                }
                else{
                	warnDialog.dismiss();
                }
			}			
		});
        _scanner.scanAll();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		CloseActivity();
		super.onDestroy();
	}	
	
	void CloseActivity(){
		if(ApconfigStep1.apconfignote1!=null)
			ApconfigStep1.apconfignote1.finish();
		if(ApconfigStep2.apconfignote2!=null)
			ApconfigStep2.apconfignote2.finish();
		if(ApconfigStep3.apconfignote3!=null)
			ApconfigStep3.apconfignote3.finish();
		finish();
	}
	
	/**
	 * Add Device List
	 */	
	private void AddDeviceListItem(String groupname,String devicename,String deviceip,String devicemac,String devicesignal)
	{
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
