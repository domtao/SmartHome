/**
 * 
 */
package com.zunder.smart.gateway;

import java.util.List;

import hsl.p2pipcam.nativecaller.DeviceSDK;
import org.json.JSONException;
import org.json.JSONObject;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.adapter.GateWayAdapter;
import com.zunder.smart.adapter.MyWifiAdapter;
import com.zunder.smart.gateway.bean.WifiModel;
import com.zunder.smart.listener.DevicePramsListener;
import com.zunder.smart.service.BridgeService;
import com.zunder.smart.tools.SystemInfo;
import com.zunder.smart.adapter.GateWayAdapter;
import com.zunder.smart.adapter.MyWifiAdapter;
import com.zunder.smart.tools.SystemInfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Administrator
 * 
 */
public class SettingGateWayWifiActivity extends BaseActivity implements
		DevicePramsListener {
	
	private EditText edit_wifi_name, edit_wifi_pwd;
	private EditText chanl, plus;
	private ListView listView;
	private Button netButton;
	private LinearLayout lin;
	// private ImageButton cbox_show_admin_pwd;
	private TextView back_item, title_item, fun_item;
	private CheckBox cbox_show_wifi_pwd;
	private long userid;
	WifiManager wifiManager;
	private List<ScanResult> list;
	String encrypt = "";
	int channel = 0;
	static ProgressDialog pd = null;
	private boolean searchflag = false;
	private int startCount = 0;
	private WifiModel wifiModel = new WifiModel();
	private boolean getDataOk = false;
	String[] encrypts = { "NONE", "WEP", "WPA_PSK(TKIP)", "WPA_PSK(AES)",
			"WPA2_PSK(AES)", "WPA2_PSK(TKIP)" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gateway_wifi);
		BridgeService.setDevicePramsListener(this);
		userid = GateWayAdapter.userid;
		initView();
		initClick();
		// System.out.println("wifi start");
		handler1.sendEmptyMessage(0);
		searchflag = true;
		startCount = 0;
		DeviceSDK.getDeviceParam(userid, 0x2013);
		startTime();
		/*
		 * 6Mbps -82dBm 9Mbps -81dBm 12Mbps -79dBm 18Mbps -77dBm 24Mbps -74dBm
		 * 36Mbps -70dBm 48Mbps -66dBm 54Mbps -65dBm
		 */
	}
	
	/**
	 * ��WIFI
	 */
	/*
	 * private void openWifi() { if (!wifiManager.isWifiEnabled()) {
	 * wifiManager.setWifiEnabled(true); } }
	 */
	private void initView() {
		edit_wifi_name = (EditText) findViewById(R.id.edit_wifi_name);
		edit_wifi_pwd = (EditText) findViewById(R.id.edit_wifi_pwd);
		back_item = (TextView) findViewById(R.id.back_item);
		cbox_show_wifi_pwd = (CheckBox) findViewById(R.id.cbox_show_wifi_pwd);
		back_item.setText("返回");
		title_item = (TextView) findViewById(R.id.title_item);
		title_item.setText("Wi-Fi设置");
		fun_item = (TextView) findViewById(R.id.fun_item);
		listView = (ListView) findViewById(R.id.listView);
		netButton = (Button) findViewById(R.id.localnet);
		chanl = (EditText) findViewById(R.id.edit_wifi_channel);
		plus = (EditText) findViewById(R.id.edit_wifi_plus);
		lin = (LinearLayout) findViewById(R.id.foot_item);
		back_item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		netButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!SystemInfo.isLocationEnabled()){
					ToastUtils.ShowError(SettingGateWayWifiActivity.this, "请打开位置服务",
							Toast.LENGTH_LONG,true);
					return;
				}

				lin.setVisibility(View.VISIBLE);
				wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
//				 openWifi();
				list = wifiManager.getScanResults();
				if (list == null) {
					Toast.makeText(SettingGateWayWifiActivity.this, "wifi列表为空",
							Toast.LENGTH_LONG).show();
				} else {
					MyWifiAdapter adapter = new MyWifiAdapter(
							SettingGateWayWifiActivity.this, list);
					listView.setAdapter(adapter);
				}

			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ScanResult sResult = list.get(arg2);
				edit_wifi_name.setText(sResult.SSID);
				String capabilities = sResult.capabilities;
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
					encrypt += "(TKIP)";
				} else if (capabilities.indexOf("CCMP") != -1) {
					encrypt += "(AES)";
				} else if (capabilities.indexOf("TKIP") != -1) {
					encrypt += "(TKIP)";
				}
				plus.setText(encrypt);
				/*
				 *1 2412MHz 2401/2423MHz
				　　2 2417MHz 2406/2428MHz
				　　3 2422MHz 2411/2433MHz
				　　4 2427MHz 2416/2438MHz
				　　5 2432MHz 2421/2443MHz
				　　6 2437MHz 2426/2448MHz
				　　7 2442MHz 2431/2453MHz
				　　8 2447MHz 2436/2458MHz
				　　9 2452MHz 2441/2463MHz
				　　10 2457MHz 2446/2468MHz
				　　11 2462MHz 2451/2473MHz
				　　12 2467MHz 2456/2478MHz
				　　13 2472MHz 2461/2483MHz 
				 */
				if(sResult.frequency==2412){
					channel=1;
				}else if(sResult.frequency==2417){
					channel=2;
				}else if(sResult.frequency==2422){
					channel=3;
				}else if(sResult.frequency==2427){
					channel=4;
				}else if(sResult.frequency==2432){
					channel=5;
				}else if(sResult.frequency==2437){
					channel=6;
				}else if(sResult.frequency==2442){
					channel=7;
				}else if(sResult.frequency==2447){
					channel=8;
				}else if(sResult.frequency==2452){
					channel=9;
				}else if(sResult.frequency==2457){
					channel=10;
				}else if(sResult.frequency==2462){
					channel=11;
				}else if(sResult.frequency==2467){
					channel=12;
				}else{
					channel=13;
				}
				//channel = sResult.frequency / 100;
				chanl.setText(channel+"");
			}
		});
	}

	private void initClick() {
		fun_item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				encrypt = plus.getText().toString();
				int lens = encrypts.length;
				int encryptint = 0;
				for (int i = 0; i < lens; i++) {
					if (encrypt.equals(encrypts[i])) {
						encryptint = i;
						break;
					}
				}
				if (chanl.getText().toString().equals("")) {
					channel = 13;
				} else {
					channel = Integer.valueOf(chanl.getText().toString());
				}
				JSONObject obj = new JSONObject();
				try {

					obj.put("enable", wifiModel.getEnable());
					obj.put("ssid", edit_wifi_name.getText().toString());
					obj.put("channel", channel);
					obj.put("mode", wifiModel.getMode());
					obj.put("authtype", encryptint);
					obj.put("encrypt", wifiModel.getEncryp());
					obj.put("keyformat", wifiModel.getKeyformat());
					obj.put("defkey", wifiModel.getDefkey());
					obj.put("key1", wifiModel.getKey1());
					obj.put("key2", wifiModel.getKey2());
					obj.put("key3", wifiModel.getKey3());
					obj.put("key4", wifiModel.getKey4());
					obj.put("key1_bits", wifiModel.getKey1_bits());
					obj.put("key2_bits", wifiModel.getKey2_bits());
					obj.put("key3_bits", wifiModel.getKey3_bits());
					obj.put("key4_bits", wifiModel.getKey4_bits());
					obj.put("wpa_psk", edit_wifi_pwd.getText().toString());
					// ��Կ
				} catch (Exception e) {
					e.printStackTrace();
				}
				int Result = DeviceSDK.setDeviceParam(userid, 0x2012,
						obj.toString());
				if (Result > 0) {
					Toast.makeText(SettingGateWayWifiActivity.this, "WiFi设置成功",
							Toast.LENGTH_SHORT).show();
					try {
						Thread.sleep(100);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				finish();
			}
		});
		// �@ʾ�ܴa
		cbox_show_wifi_pwd
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							edit_wifi_pwd
									.setTransformationMethod(HideReturnsTransformationMethod
											.getInstance());
						} else {
							edit_wifi_pwd
									.setTransformationMethod(PasswordTransformationMethod
											.getInstance());
						}
					}
				});
	}
	@Override
	public void getDevicePrams(long UserID, long nType, String param) {
		if (getDataOk == false) {
			if ((UserID == userid) && (nType == 0X2013)) {
				getDataOk = true;
				try {
					JSONObject localJSONObject = new JSONObject(param);
					this.wifiModel.setEncryp(localJSONObject.getInt("encrypt"));
					this.wifiModel.setKeyformat(localJSONObject
							.getInt("keyformat"));
					this.wifiModel.setDefkey(localJSONObject.getInt("defkey"));
					this.wifiModel.setEnable(localJSONObject.getInt("enable"));
					this.wifiModel.setSsid(localJSONObject.get("ssid")
							.toString());
					this.wifiModel
							.setChannel(localJSONObject.getInt("channel"));
					this.wifiModel.setMode(localJSONObject.getInt("mode"));
					this.wifiModel.setAuthtype(localJSONObject
							.getInt("authtype"));
					this.wifiModel.setKey1(localJSONObject.getString("key1"));
					this.wifiModel.setWpa_psk(localJSONObject
							.getString("wpa_psk"));
					this.wifiModel.setKey2(localJSONObject.getString("key2"));
					this.wifiModel.setKey3(localJSONObject.getString("key3"));
					this.wifiModel.setKey4(localJSONObject.getString("key4"));
					this.wifiModel.setKey1_bits(localJSONObject
							.getInt("key1_bits"));
					this.wifiModel.setKey2_bits(localJSONObject
							.getInt("key2_bits"));
					this.wifiModel.setKey3_bits(localJSONObject
							.getInt("key3_bits"));
					this.wifiModel.setKey4_bits(localJSONObject
							.getInt("key4_bits"));
					handler.sendEmptyMessage(0);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (searchflag) {
					if (startCount < 45) {
						startCount = 45;
					}
				}
			}
		}
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				try {
					edit_wifi_name.setText(wifiModel.getSsid());
					edit_wifi_pwd.setText(wifiModel.getWpa_psk());
					chanl.setText(wifiModel.getChannel() + "");
					plus.setText(encrypts[wifiModel.getAuthtype()]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	public Handler handler1 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 0) {
				pd = ProgressDialog.show(SettingGateWayWifiActivity.this, "",
						"获取WIFi数据");
			}
			super.handleMessage(msg);
		}
	};

	private void startTime() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (searchflag) {
					try {
						Thread.sleep(100);
						startCount++;
						if (startCount >= 50) {
							searchflag = false;
							if ((pd != null) && pd.isShowing()) {
								pd.dismiss();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
		
}
