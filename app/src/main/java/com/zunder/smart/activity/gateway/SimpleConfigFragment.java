package com.zunder.smart.activity.gateway;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bluecam.api.VoiceConfigActivity;
import com.bluecam.bluecamlib.CameraManager;
import com.door.Utils.ToastUtils;
import com.mediatek.elian.ElianNative;
import com.realtek.simpleconfiglib.SCLibrary;
import com.zunder.scrollview.widget.WheelAdapter;
import com.zunder.scrollview.widget.WheelView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.TabMyActivity;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dialog.WarnDialog;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.rak47.apconfig.ApconfigStep1;
import com.zunder.smart.rak47.simpleconfig_wizard.ConfigurationDevice;
import com.zunder.smart.rak47.simpleconfig_wizard.FileOps;
import com.zunder.smart.rak47.simpleconfig_wizard.SCCtlOps;
import com.zunder.smart.rak47.simpleconfig_wizard.WLANAPI;
import com.zunder.smart.service.GateWayService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import voice.encoder.DataEncoder;
import voice.encoder.VoicePlayer;
import voice.encoder.VoicePlayerListener;

public class SimpleConfigFragment extends Fragment implements Callback ,View.OnTouchListener{
	private EditText Ssid;
	private EditText Psk;
	private Button Connect_btn,search_network;
	private TextView Status;
	private String key = "0123456789012345" ;

	private WLANAPI mWifiAdmin;// wifi API

	private SCLibrary SCLib;
	private FileOps fileOps = new FileOps();
	private ConfigurationDevice.DeviceInfo[] configuredDevices;
	private static final int deviceNumber = 32;
	boolean ConnectAPProFlag = false;// user need to connect ap
	boolean ConfigureAPProFlag = false;// user need to connect ap
	private String AP_password = "";
	private WarnDialog pd;
	private Thread connectAPThread = null;
	private Thread backgroundThread = null;
	private boolean TimesupFlag_cfg = true;
	private static final int configTimeout = 120000;// 120s
	private static final int connectAPTimeout = 20;// 20s
	private static final String defaultPINcode = "";
	private String PINSet = null;
	private boolean ShowCfgSteptwo = false;
	private boolean timeout = false;
	private Activity activity;
	private int setIndex=0;
	static {
		System.loadLibrary("simpleconfiglib");
	}
	TextView backView;
	WheelView imageScrollView;
	ImageView imageState;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
	protected ProgressDialog progressDialog;

	protected void showProgressDialog(String msg)
	{
		progressDialog = null;
		progressDialog = ProgressDialog.show(activity, "", msg, true, false);
	}
	protected void hideProgressDialog()
	{
		if(progressDialog != null && progressDialog.isShowing())
		{
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.activity_simple_config, container,
				false);
		root.setOnTouchListener(this);
		activity=getActivity();
		mWifiAdmin = new WLANAPI(activity);

		SCCtlOps.ConnectedSSID = null;
		SCCtlOps.ConnectedPasswd = null;
		// scan configured devices
		SCCtlOps.rtk_sc_control_reset();
		configuredDevices = new ConfigurationDevice.DeviceInfo[deviceNumber];
		for (int i = 0; i < deviceNumber; i++) {
			configuredDevices[i] = new ConfigurationDevice.DeviceInfo();
			configuredDevices[i].setaliveFlag(1);
			configuredDevices[i].setName("");
			configuredDevices[i].setIP("");
			configuredDevices[i].setmacAdrress("");
			configuredDevices[i].setimg(null);
			configuredDevices[i].setconnectedflag(false);
		}
		Ssid = (EditText) root.findViewById(R.id.ssid);
		Psk = (EditText) root.findViewById(R.id.psk);
		Connect_btn = (Button) root.findViewById(R.id.connect_network);
		Connect_btn.setOnClickListener(Connect_btn_Click);// ���ü���

		search_network= (Button) root.findViewById(R.id.search_network);
		search_network.setOnClickListener(Search_btn_Click);// ���ü���
		imageState=(ImageView)root.findViewById(R.id.imageState);

		Status = (TextView) root.findViewById(R.id.status);

		backView = (TextView) root.findViewById(R.id.backTxt);
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TabMyActivity.getInstance().hideFragMent(3);
			}
		});
		imageScrollView = (WheelView) root.findViewById(R.id.imageScrollView);

		return root;
	}
	public  void init(){
		List<String> list=new ArrayList <String>();
		list.add("小网关");
		list.add("HiLink");
		WheelAdapter adapter=new WheelAdapter(list);
		imageScrollView.setAdapter(adapter);

		imageScrollView.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(int index,String itemName) {
				if(index==0){
					setIndex=0;
					imageState.setImageResource(R.mipmap.small_gateway);
				}else if(index==1){
					setIndex=1;
					imageState.setImageResource(R.mipmap.small_link);
				}
			}
		});
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(hidden){
			if(SCLib!=null){
				SCLib.rtk_sc_exit();
			}
			SCCtlOps.ConnectedSSID = null;
			SCCtlOps.ConnectedPasswd = null;
		}
		else{
			init();
			if(SCLib==null){
				SCLib = new SCLibrary();
			}
			SCLib.rtk_sc_init();
			SCLib.TreadMsgHandler = new MsgHandler();
			// wifi manager init
			SCLib.WifiInit(activity);
			fileOps.SetKey(SCLib.WifiGetMacStr());
			WifiManager wifiManager = (WifiManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
			if (wifiManager.isWifiEnabled()) {
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				String ssidString = wifiInfo.getSSID();
				List<ScanResult> scanResults = wifiManager.getScanResults();
				for (ScanResult result : scanResults) {
					if (result.BSSID.equalsIgnoreCase(wifiInfo.getBSSID())
							&& result.SSID.equalsIgnoreCase(wifiInfo.getSSID()
							.substring(1, wifiInfo.getSSID().length() - 1))) {
						int frequency = result.frequency;
						if (frequency / 2400 == 1) {
							Connect_btn.setEnabled(true);
							Status.setText("");
						} else {
							Connect_btn.setEnabled(false);
							Status.setTextColor(Color.RED);
							Status.setText("The frequency of the network is 5GHz,it's unavailable to easyconfig, please switch network");
						}
						break;
					}
				}
				int lm = 5;
				lm = ssidString.indexOf('"');
				if (lm >= 0)
					ssidString = ssidString.substring((lm + 1),
							(ssidString.length() - 1));
				Ssid.setText(ssidString);
			} else {
				Status.setTextColor(Color.RED);
				Status.setText("The network is unavailable, please check the network");
			}
		}
	}

	// handler for the background updating
	Handler progressHandler = new Handler() {
		public void handleMessage(Message msg) {
		}
	};

	Handler handler_pd = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// Log.d(TAG,"handleMessage msg.what: " + String.valueOf(msg.what));
			switch (msg.what) {
			case 0:
				pd.dismiss();
				Log.e("pd==>", "pd.dismiss");
				break;
			case 1:
				int timeout = 10;
				int coutDown = timeout;

				while (coutDown > 0) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					coutDown--;
					if (coutDown == 0) {
						pd.dismiss();
					}
				}

				break;
			default:
				break;
			}
		}

	};
	private VoicePlayer player;
	void intVoice(){
		player  = CameraManager.startVoicePlayer();
		player.setListener(new VoicePlayerListener(){
			@Override
			public void onPlayEnd(VoicePlayer arg0) {

			}
			@Override
			public void onPlayStart(VoicePlayer arg0) {
			}
		});
		int libVersion = ElianNative.GetLibVersion();
		int protoVersion = ElianNative.GetProtoVersion();
	}
	private Handler timeHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			hideProgressDialog();
			ToastUtils.ShowSuccess(activity,"网络配置成功!", Toast.LENGTH_SHORT,true);
		}
	};
	OnClickListener Search_btn_Click = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			TabMyActivity.getInstance().showFragMent(5);
			TabMyActivity.getInstance().gatewayAddFragment.setEdite("Add",null,1);
			TabMyActivity.getInstance().hideFragMent(3);
		}
	};
	OnClickListener Connect_btn_Click = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if(setIndex==0) {
				Is_back = false;
				// TODO Auto-generated method stub
				WifiManager wifi = (WifiManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

				// check wifi is disable
				if (!wifi.isWifiEnabled()) {
					// OpenWifiPopUp();
					SCLib.WifiOpen();

					do {
						SystemClock.sleep(1000);
						wifi = (WifiManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
						// Log.d(TAG, "Turn on Wi-Fi wait");
					} while (!wifi.isWifiEnabled()); // check again if it is disable
					SystemClock.sleep(2500);
				}

				if (Ssid.getText().toString().equals("")) {
					DisplayToast("AP名称不能为空");
				} else {
					if (Psk.getText().toString().length() > 0) {
						AP_password = Psk.getText().toString();
					}

					// do connect ap action
					ConnectAPProFlag = true;

					connectAPThread = new Thread() {
						@Override
						public void run() {

							if (connect_action() == true) {
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										if (ConnectAPProFlag == true) {
											PINSet = null;
											startToConfigure();
											ConnectAPProFlag = false;
										}
									}
								});

							} else {
								if (ConnectAPProFlag != false) {
									activity.runOnUiThread(new Runnable() {
										@Override
										public void run() {
											AlertDialog.Builder errorAlert = new AlertDialog.Builder(
													activity);
											errorAlert.setTitle("连接AP失败");
											errorAlert.setMessage("请检查密码或其它问题");
											errorAlert
													.setPositiveButton("确定", null);
											errorAlert.show();
										}
									});
								}
								handler_pd.sendEmptyMessage(0);
							}
						}
					};
					connectAPThread.start();
				}
			}else{
				intVoice();
				showProgressDialog("正在配置wifi，请等待30秒");
				timeHandler.sendEmptyMessageDelayed(0,30*1000);
				done();
			}
		}
	};
	private void done(){

		startSendVoice();
		startSendElian();
	}
	private boolean isExit;
	//开始声波配置
	private void startSendVoice()
	{
		new Thread(){
			@Override
			public void run() {
				if(player == null)
				{
					return;
				}
				int count = 0;
				do
				{
					if(isExit)
					{
						player.stop();
						return;
					}
					player.play(DataEncoder.encodeSSIDWiFi(Ssid.getText().toString(), Psk.getText().toString()), 1, 200);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					count = count+1;
				}while (count<=2);
			}
		}.start();
	}


	//开始无线配置
	private void startSendElian()
	{
		new Thread(){
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int libVersion = ElianNative.GetLibVersion();
				int protoVersion = ElianNative.GetProtoVersion();
			ElianNative.InitSmartConnection(null, 0, 1);
				int ret = ElianNative.StartSmartConnection(Ssid.getText().toString(),Psk.getText().toString(), key);
				System.out.println("StartSmartConnection ret == "+ret);
			}
		}.start();
	}
	int c = 0;

	boolean connect_action() {
		boolean connect_ok = false;
		mWifiAdmin = new WLANAPI(MyApplication.getInstance().getApplicationContext());
		if (mWifiAdmin.getSSID().contains(Ssid.getText().toString())) {
			// save psk of the ssid
			connect_ok = true;
			ConnectAPProFlag = true;
			Editor editor = MyApplication.getInstance().getSharedPreferences(Ssid.getText().toString(),
					Context.MODE_PRIVATE).edit();
			editor.putString("psk", Psk.getText().toString());
			editor.commit();
			SCCtlOps.ConnectedSSID = Ssid.getText().toString();
			SCCtlOps.ConnectedPasswd = Psk.getText().toString();
		}

		return connect_ok;
	}

	boolean Is_back = false;

	public void startToConfigure() {

		ConfigureAPProFlag = true;
		if(pd==null) {
			pd = new WarnDialog(activity, "配置新的设备");
			pd.setMessage("配置中 0%");

			pd.setSureListener(new WarnDialog.OnSureListener() {
				@Override
				public void onCancle() {
					pd.dismiss();
					Is_back = true;
					ConfigureAPProFlag = false;
					TimesupFlag_cfg = true;
					SCLib.rtk_sc_stop();
					backgroundThread.interrupt();
				}
			});
		}else{
			pd.setMessage("配置中 0%");
		}
		pd.show();


		// create a thread for updating the progress bar
		backgroundThread = new Thread(new Runnable() {
			public void run() {
				try {
					c = 0;
					while ((Is_back == false) && (c < 100)) {
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								c++;
								pd.setMessage("配置中 "+c+"%");
							}
						});

						Thread.sleep(1200);
						progressHandler.sendMessage(progressHandler
								.obtainMessage());
					}
				} catch (InterruptedException e) {
				}
			}
		});
		backgroundThread.start();

		Thread ConfigDeviceThread = new Thread() {
			@Override
			public void run() {
				Configure_action();
				// wait dialog cancel
				if (ConnectAPProFlag == false) {
					c = 100;
					backgroundThread.interrupt();
					handler_pd.sendEmptyMessage(0);
				}

				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (ConfigureAPProFlag == true) {
							// show "start to configure"
							ConfigureAPProFlag = false;
							showConfiguredList();
						}
					}
				});
			}
		};
		ConfigDeviceThread.start();
	}

	private void Configure_action() {
		int stepOneTimeout = 30000;

		// check wifi connected
		if (SCCtlOps.ConnectedSSID == null) {
			return;
		}
		int connect_count = 200;

		// get wifi ip
		int wifiIP = SCLib.WifiGetIpInt();
		while (connect_count > 0 && wifiIP == 0) {
			wifiIP = SCLib.WifiGetIpInt();
			connect_count--;
		}
		if (wifiIP == 0) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(activity,
							"Allocating IP, please wait a moment",
							Toast.LENGTH_SHORT).show();
				}
			});

			return;
		}

		SCLib.rtk_sc_reset();

		if (PINSet == null) {
			SCLib.rtk_sc_set_default_pin(defaultPINcode);
		} else if (PINSet.length() > 0) {
			SCLib.rtk_sc_set_default_pin(defaultPINcode);
		}
		SCLib.rtk_sc_set_pin(PINSet);

		// Ssid.getText().toString();
		// SCCtlOps.ConnectedPasswd = Psk.getText().toString();
		SCLib.rtk_sc_set_ssid(SCCtlOps.ConnectedSSID);

		if (!SCCtlOps.IsOpenNetwork) {
			SCLib.rtk_sc_set_password(SCCtlOps.ConnectedPasswd);
		}

		TimesupFlag_cfg = false;

		SCLib.rtk_sc_set_ip(wifiIP);
		SCLib.rtk_sc_build_profile();

		/* Profile(SSID+PASSWORD, contain many packets) sending total time(ms). */
		SCLibrary.ProfileSendTimeMillis = configTimeout;

		// ==================== 1 ========================= 30s
		/* Time interval(ms) between sending two profiles. */
		SCLibrary.ProfileSendTimeIntervalMs = 50; // 50ms
		/* Time interval(ms) between sending two packets. */
		SCLibrary.PacketSendTimeIntervalMs = 0; // 0ms
		/* Each packet sending counts. */
		SCLibrary.EachPacketSendCounts = 1;

		// exception action
		exception_action();

		SCLib.rtk_sc_start();
		int watchCount = 0;
		try {
			do {
				Thread.sleep(1000);
				watchCount += 1000;
			} while (TimesupFlag_cfg == false && watchCount < stepOneTimeout);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// ==================== 2 =========================
		if (TimesupFlag_cfg == false) {
			int count = 0;
			/* Time interval(ms) between sending two profiles. */
			SCLibrary.ProfileSendTimeIntervalMs = 200; // 200ms
			/* Time interval(ms) between sending two packets. */
			SCLibrary.PacketSendTimeIntervalMs = 10; // 10ms
			/* Each packet sending counts. */
			SCLibrary.EachPacketSendCounts = 1;

			// exception action
			// exception_action();

			try {
				do {
					Thread.sleep(1000);
					count++;
					if ((((configTimeout - stepOneTimeout) / 1000) - count) < 0)
						break;
				} while (TimesupFlag_cfg == false);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			TimesupFlag_cfg = true;

			// Log.d("=== Configure_action ===","rtk_sc_stop 2");
			SCLib.rtk_sc_stop();

		}
	}

	private void exception_action() {
		if (Build.MANUFACTURER.equalsIgnoreCase("Samsung")) {
			// SCLibrary.PacketSendTimeIntervalMs = 5;
			if (Build.MODEL.equalsIgnoreCase("G9008")) { // Samsung Galaxy S5
															// SM-G9008
				SCLibrary.PacketSendTimeIntervalMs = 10;
			} else if (Build.MODEL.contains("SM-G9208")) { // samsun Galaxy S6
				SCLibrary.PacketSendTimeIntervalMs = 10;
			} else if (Build.MODEL.contains("N900")) { // samsun Galaxy note 3
				SCLibrary.PacketSendTimeIntervalMs = 5;
			} else if (Build.MODEL.contains("SM-N910U")) { // samsun Galaxy note
															// 4
				SCLibrary.PacketSendTimeIntervalMs = 5;
			}

		} else if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {// for MI
			if (Build.MODEL.equalsIgnoreCase("MI 4W")) {
				SCLibrary.PacketSendTimeIntervalMs = 5; // MI 4
			}
		} else if (Build.MANUFACTURER.equalsIgnoreCase("Sony")) {// for Sony
			if (Build.MODEL.indexOf("Xperia") > 0) {
				SCLibrary.PacketSendTimeIntervalMs = 5; // Z3
			}
		}

		// check link rate
		WifiManager wifi_service = (WifiManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiinfo = wifi_service.getConnectionInfo();
		if (wifiinfo.getLinkSpeed() > 78) {// MCS8 , 20MHZ , NOSGI
			SCLibrary.ProfileSendTimeIntervalMs = 100; // 50ms
			SCLibrary.PacketSendTimeIntervalMs = 15;
		}
	}

	private void showConfiguredList() {
		ShowCfgSteptwo = false;
		ConfigureAPProFlag = false;
		SCLib.rtk_sc_stop();
		backgroundThread.interrupt();
		handler_pd.sendEmptyMessage(0);

		final List<HashMap<String, Object>> InfoList = new ArrayList<HashMap<String, Object>>();
		String[] deviceList = null;

		final int itemNum = SCLib.rtk_sc_get_connected_sta_num();

		SCLib.rtk_sc_get_connected_sta_info(InfoList);

		final boolean[] isSelectedArray = new boolean[itemNum];
		Arrays.fill(isSelectedArray, Boolean.TRUE);

		// input data
		if (itemNum > 0) {
			Log.e("itemNum", "" + itemNum);
			deviceList = new String[itemNum];
			for (int i = 0; i < itemNum; i++) {
				Log.e("itemNum", "" + InfoList.get(i).get("MAC"));
				configuredDevices[i].setaliveFlag(1);

				if (InfoList.get(i).get("Name") == null)
					configuredDevices[i].setName((String) InfoList.get(i).get(
							"MAC"));
				else
					configuredDevices[i].setName((String) InfoList.get(i).get(
							"Name"));

				configuredDevices[i].setmacAdrress((String) InfoList.get(i)
						.get("MAC"));
				configuredDevices[i].setIP((String) InfoList.get(i).get("IP"));

				deviceList[i] = configuredDevices[i].getName();
			}
		} else {
			if ((TimesupFlag_cfg == true) && (Is_back == false)) {
				final WarnDialog warnDialog = new WarnDialog(activity, "Configure Timeout");

				warnDialog.setMessage("Retry or Go to APConfig?");
				warnDialog.setSureListener(new WarnDialog.OnSureListener() {
					@Override
					public void onCancle() {
						warnDialog.dismiss();
					}
				});
				warnDialog.show();
			}
			handler_pd.sendEmptyMessage(0);
			return;
		}
		String deviceString = "";
		for (int i = 0; i < itemNum; i++) {
			deviceString += configuredDevices[i].getName();
		}
		deviceString ="0000"+ deviceString.replace(":","").toUpperCase();
		GateWay gateWay= GateWayFactory.getInstance().getGateWayByDeviceId(deviceString);
		if(gateWay!=null){
			ToastUtils.ShowSuccess(activity,"设备已加入网关列表["+gateWay.getGatewayName()+"]",Toast.LENGTH_SHORT,true);
		}else {
			gateWay=new GateWay();

			//joe 搜索网关成功!
			TabMyActivity.getInstance().hideFragMent(3);
			TabMyActivity.getInstance().showFragMent(4);
			 gateWay.setGatewayName("Smart Wizard");
			 gateWay.setGatewayID(deviceString.trim());
			 gateWay.setTypeId(1);
			TabMyActivity.getInstance().gatewayAddFragment.setEdite("Edite1",gateWay,0);

		}
	}

	private Runnable Cfg_changeMessage = new Runnable() {
		@Override
		public void run() {
			// Log.v(TAG, strCharacters);
		}
	};

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

	/** Handler class to receive send/receive message */
	private class MsgHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ~SCCtlOps.Flag.CfgSuccessACK:// Config Timeout
				SCLib.rtk_sc_stop();

				break;
			case SCCtlOps.Flag.CfgSuccessACK: // Not Showable
				SCLib.rtk_sc_stop();
				TimesupFlag_cfg = true;

				if (ShowCfgSteptwo == true)
					activity.runOnUiThread(Cfg_changeMessage);

				List<HashMap<String, Object>> InfoList = new ArrayList<HashMap<String, Object>>();
				SCLib.rtk_sc_get_connected_sta_info(InfoList);
				String ip = InfoList.get(0).get("IP").toString();
				if (!ip.equals("0.0.0.0")) {// Client Got IP

				}
				break;

			default:
				break;
			}
		}
	}

	/**
	 * Display message
	 */
	public void DisplayToast(String str) {
		Toast.makeText(activity, str, Toast.LENGTH_SHORT).show();
	}


	void exit() {
		Is_back = true;
		if (SCLib != null)
			SCLib.rtk_sc_stop();
		if (backgroundThread != null)
			backgroundThread.interrupt();
		TimesupFlag_cfg = true;
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}
}
