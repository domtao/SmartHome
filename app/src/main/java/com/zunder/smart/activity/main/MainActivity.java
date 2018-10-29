package com.zunder.smart.activity.main;

import java.net.Socket;
import java.util.List;
import com.andsync.xpermission.XPermissionUtils;
import com.door.DoorApplication;
import com.door.Utils.ToastUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.camera.CameraFragment;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.dao.impl.factory.ModeListFactory;
import com.zunder.smart.dao.impl.factory.RedInfraFactory;
import com.zunder.smart.dao.impl.factory.RoomFactory;
import com.zunder.smart.dialog.TipAlert;

import com.zunder.smart.json.MasterUtils;
import com.zunder.smart.listener.ReceivedListener;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.History;
import com.zunder.smart.model.Master;
import com.zunder.smart.model.ResultInfo;
import com.zunder.smart.netty.MockLoginNettyClient;
import com.zunder.smart.service.BridgeService;
import com.zunder.smart.service.GateWayService;
import com.zunder.smart.service.GateWayService.CameraBinder;
import com.zunder.smart.service.PhoneListenerService;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.service.aduio.AduioService;
import com.zunder.smart.setting.ProjectUtils;
import com.zunder.smart.socket.ICoallBack;
import com.zunder.smart.socket.ISocketPacket;
import com.zunder.smart.socket.SocketClient;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.CurrentVersion;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.tools.SystemInfo;
import com.zunder.smart.utils.Base64;
import com.zunder.smart.webservice.HistoryUtils;
import com.zunder.smart.webservice.SettingServiceUtils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TabActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends TabActivity implements OnClickListener,CompoundButton.OnCheckedChangeListener {

	private static String TAG = MainActivity.class.getSimpleName();
	private SharedPreferences spf;
	public static boolean isForeground = false;
	private Button plusImageView;

	private static MainActivity instance;
	private CameraBinder cameraBinder;
	Intent gateWayService;
	public TabHost mHost;
	static ReceivedListener receivedListener;
	private RadioButton mainRadio,homeRadio,myRadio,modeRadio;

	private int clickIndex=0;
	public static void setReceivedListener(ReceivedListener _receivedListener) {
		receivedListener = _receivedListener;
	}

	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			cameraBinder.stopFindDevice();
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			cameraBinder = (CameraBinder) service;
			cameraBinder.startFindDevice();
		}
	};

	public static MainActivity getInstance() {
		if (instance == null) {
			instance = new MainActivity();
		}
		return instance;
	}

	public void stopActivityServer() {
		if (gateWayService != null) {
			unbindService(conn);
			cameraBinder.stopFindDevice();
			gateWayService = null;
		}
	}

	public void freshFindDevice() {
		if (instance != null) {
			if (MockLoginNettyClient.getInstans().isConnect()) {
				try {
					MainActivity.getInstance().initSocket();
					Toast.makeText(instance, getString(R.string.connect_service), Toast.LENGTH_SHORT)
							.show();
				} catch (Exception e) {
				}
				return;
			}
			GateWayFactory.getInstance().clearList();
			stopActivityServer();
			startActivityServer();
		}
	}

	public void startActivityServer() {
		new DataTask().execute();
		gateWayService = new Intent(getInstance(), GateWayService.class);
		bindService(gateWayService, conn, Service.BIND_AUTO_CREATE);
	}
	TipAlert alert = null;
	private static RelativeLayout top;
	private Animation dwon_Translate;
	private Animation up_Translate;
	private TextView showTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		setContentView(R.layout.activity_smart_main);
		spf = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
		startActivityServer();
		init();

		MyApplication.SystemStart = true;
		String deviceID = SystemInfo.getMasterID(instance);
		JPushInterface.setAliasAndTags(instance, deviceID, null, null);
		mainRadio.setChecked(true);
		push();
}

	public void push(){
		String deviceID = SystemInfo.getMasterID(instance);
		JPushInterface.setAliasAndTags(instance, deviceID, null, null);
	}

	public void init() {
		Intent intent = new Intent(this, BridgeService.class);
		startService(intent);
		initView();
		initButton();
		List<GateWay> list = GateWayFactory.getInstance().getAll();
		for (int i = 0; i < list.size(); i++) {
			GateWay gateWay = list.get(i);
			if (gateWay.getTypeId() <3) {
				try {
					initSocket();
				} catch (Exception e) {
				}
				break;
			}
		}
		for (int i = 0; i < list.size(); i++) {
			GateWay gateWay = list.get(i);
			if (gateWay.getTypeId() == 4) {
				handler.sendEmptyMessage(-4);
				break;
			}
		}
		Intent phoneIntent = new Intent(this, PhoneListenerService.class);
		startService(phoneIntent);
	}

	public void ReceviceBroadcast(String result) {
		Log.e("cmdCode", result);
		Intent intent = new Intent("com.zunder.smart.receiver");
		Bundle bundle = new Bundle();
		bundle.putString("str", result);
		intent.putExtras(bundle);
		sendBroadcast(intent);
	}
	public void setTip(String msg){
		if(TabMyActivity.getInstance()!=null) {
			TabMyActivity.getInstance().setTip(msg);
		}
	}

	public void updateFresh(){
		TcpSender.isGateWay = false;
        GateWayFactory.getInstance().clearList();
        stopActivityServer();
        startActivityServer();
		RoomFactory.getInstance().clearList();
		DeviceFactory.getInstance().clearList();
		ModeFactory.getInstance().clearList();
		ModeListFactory.getInstance().clearList();
		RedInfraFactory.getInstance().clearList();
		if(TabMainActivity.getInstance()!=null) {
			TabMainActivity.getInstance().setAdapter();
		}
		if(TabHomeActivity.getInstance()!=null) {
			TabHomeActivity.getInstance().initScrollView();
		}
//		if(TabModeActivity.getInstance()!=null) {
//			TabModeActivity.getInstance().hiFragment();
//			TabModeActivity.getInstance().setAdapter();
//		}

//		if(TabMyActivity.getInstance()!=null){
//			TabMyActivity.getInstance().hiFragment();
//			if(TabMyActivity.getInstance().gatewayFragment!=null&&!TabMyActivity.getInstance().gatewayFragment.isHidden()){
//				TabMyActivity.getInstance().gatewayFragment.init();
//			}
//		}

		if (MockLoginNettyClient.getInstans().isConnect()) {
			try {
				initSocket();
			} catch (Exception e) {

			}
		}
	}
	int count = 0;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case -5:
					break;
				case -4://门铃
					DoorApplication.getInstance().attemptLogin();
					break;
				case -3:
					//TOAST文字显示
					if(showTxt!=null&&msg.obj!=null) {
						showTxt.setText(msg.obj.toString());
						if (top.getVisibility() == View.GONE) {
							top.setVisibility(View.VISIBLE);
							top.startAnimation(dwon_Translate);
							startTimeCode();
						}
					}
					break;
				case -2://Toast隐藏
					top.startAnimation(up_Translate);
					top.setVisibility(View.GONE);
					break;
				case -1:
					if (searchflag && startCount == 0) {
						List<GateWay> gateWayList = GateWayService.list;
						if (gateWayList != null && gateWayList.size() > 0) {
							for (int i = 0; i < gateWayList.size(); i++) {
								final GateWay gateWay = gateWayList.get(i);
								if (gateWay.getTypeId() >= 4 && gateWay.getTypeId() != 6) {
									String resultCode = ISocketCode.setConnect(
											gateWay.getGatewayID(),
											gateWay.getUserName(),
											gateWay.getUserPassWord(),
											gateWay.getIsCurrent());
									sendCode(resultCode);
								}
							}
						}
					}
					break;
				case 0:
					ToastUtils.ShowSuccess(instance, getString(R.string.servicesuccess), Toast.LENGTH_SHORT, true);
					if (ProjectUtils.getRootPath().getAlarmId().length() > 0) {
						startTimeAlarm(30);
					}
					break;
				case 1:
					Master msaterName = MasterUtils
							.getMaster(MainActivity.getInstance());
					String result = ISocketCode.setLine(msaterName.getMn(), msaterName.getPw(),
							msaterName.getOr(), msaterName.getOp(), 1);
					sendCode(result);
					connectGateway();
					break;
				case 2:
					freshFindDevice();
					break;
				case 3:
					ToastUtils.ShowSuccess(instance, msg.obj.toString(), Toast.LENGTH_SHORT, true);
					if(TabMyActivity.getInstance()!=null){
						TabMyActivity.getInstance().setTip(msg.obj.toString());
					}
					if(TabMainActivity.getInstance()!=null){
						TabMainActivity.getInstance().setBackCode();
					}
					break;
				case 10:
					String id = ProjectUtils.getRootPath().getAlarmId();
					if (id.length() > 0) {
						ProjectUtils.getRootPath().setAlarmId("");
						ProjectUtils.saveRootPath();
						Device deviceParams = DeviceFactory.getInstance().getDeviceID(id);
						if (deviceParams != null) {
							TcpSender.preStatStr = "";

						}
					}
					break;
			}
		}
	};

	public void connectGateway() {
		List<GateWay> gateWayList = GateWayService.list;
		if (gateWayList != null && gateWayList.size() > 0) {
			for (int i = 0; i < gateWayList.size(); i++) {
				final GateWay gateWay = gateWayList.get(i);
				if (gateWay.getTypeId() <3) {
					String resultCode = ISocketCode.setConnect(
							gateWay.getGatewayID(),
							gateWay.getUserName(),
							gateWay.getUserPassWord(),
							gateWay.getIsCurrent());
					sendCode(resultCode);
				}
			}
		}
	}

	@SuppressLint("ResourceAsColor")
	private void initView() {

		Resources res = getResources();
		mHost = this.getTabHost();
		mHost.addTab(mHost
				.newTabSpec("Main")
				.setIndicator("首页",
						res.getDrawable(R.drawable.tabbar_home_select))
				.setContent(new Intent(this, TabMainActivity.class)));
		mHost.addTab(mHost
				.newTabSpec("Home")
				.setIndicator("家居",
						res.getDrawable(R.drawable.tabbar_main_select))
				.setContent(new Intent(this, TabHomeActivity.class)));
		mHost.addTab(mHost
				.newTabSpec("Mode")
				.setIndicator("集合", res.getDrawable(R.drawable.tabbar_mode_select))
				.setContent(new Intent(this, TabModeActivity.class)));
		mHost.addTab(mHost
				.newTabSpec("Mine")
				.setIndicator("我的", res.getDrawable(R.drawable.tabbar_my_select))
				.setContent(new Intent(this, TabMyActivity.class)));

		mHost.setOnTabChangedListener(new OnTabChangedListener()); // 选择监听器

		top = (RelativeLayout) findViewById(R.id.addPromptLayout);
		showTxt = (TextView) findViewById(R.id.showTxt);
		dwon_Translate = AnimationUtils.loadAnimation(MainActivity.this,
				R.anim.down_animation);
		up_Translate = AnimationUtils.loadAnimation(this, R.anim.up_animation);
		dwon_Translate.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(Animation animation) {

			}
		});
		plusImageView = (Button) findViewById(R.id.plus_btn);
		plusImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			AduioService aduioService = AduioService.getInstance();
					aduioService.setInit(instance);
			}
		});
	}
	private void initButton() {
// TODO Auto-generated method stub

		mainRadio=(RadioButton)findViewById(R.id.mainRadio);
		homeRadio=(RadioButton)findViewById(R.id.homeRadio);
		modeRadio=(RadioButton)findViewById(R.id.modeRadio);
		myRadio=(RadioButton)findViewById(R.id.myRadio);

		mainRadio.setOnCheckedChangeListener(this);
		homeRadio.setOnCheckedChangeListener(this);
		modeRadio.setOnCheckedChangeListener(this);
		myRadio.setOnCheckedChangeListener(this);

		mainRadio.setOnClickListener(this);
		homeRadio.setOnClickListener(this);
		modeRadio.setOnClickListener(this);
		myRadio.setOnClickListener(this);
	}

	class OnTabChangedListener implements TabHost.OnTabChangeListener {

		@Override
		public void onTabChanged(String tabId) {
			mHost.setCurrentTabByTag(tabId);
		}
	}

	@Override
	protected void onStop() {
		showNotification();//显示通知栏图标
		super.onStop();
	}

	@Override
	protected void onResume() {
		isForeground = true;
		NotificationManager notificationManager = (NotificationManager) this
				.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(0);
		new ApkAsynTask().execute();
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(Intent.ACTION_MAIN);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.addCategory(Intent.CATEGORY_HOME);
		startActivity(i);
		super.onBackPressed();
	}

	//显示通知栏图标
	private void showNotification() {
		NotificationManager notificationManager = (NotificationManager)
				this.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(instance);
		builder.setContentText("正在运行");
		builder.setContentTitle("云知声");
		builder.setSmallIcon(R.mipmap.logo_png);
		builder.setTicker("云知声正在运行");
		builder.setAutoCancel(true);
		builder.setWhen(System.currentTimeMillis());
		Intent intent = new Intent(instance, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(instance, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		builder.setContentIntent(pendingIntent);
		Notification notification = builder.build();
		notificationManager.notify(0, notification);
	}

	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();
	}

	private static View view;
	//private static boolean startflag = false;
	private static int startCount = 0;


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.mainRadio:
				Log.e("click", "click " + clickIndex);
				if (clickIndex == 1) {
					clickIndex = 2;
				} else if(clickIndex==2){
					TabMainActivity.getInstance().hiFragment();
				}
				break;
			case R.id.homeRadio:
				Log.e("click","click "+clickIndex);
				if (clickIndex == 1) {
					clickIndex = 2;
					TabHomeActivity.getInstance().init();
				} else if(clickIndex==2){
					TabHomeActivity.getInstance().hiFragment();
				}
				break;
			case R.id.modeRadio:
				Log.e("click","click "+clickIndex);
				if (clickIndex == 1) {
					clickIndex = 2;
				} else if(clickIndex==2){
					TabModeActivity.getInstance().hiFragment();
				}
				break;
			case R.id.myRadio:
				Log.e("click","click "+clickIndex);
				if (clickIndex == 1) {
					clickIndex = 2;
				} else if(clickIndex==2){
					TabMyActivity.getInstance().hiFragment();
				}
				break;
		}
	}

	public void getLearn(Device device) {
	}

	public void showLine() {
		handler.sendEmptyMessage(-1);
	}

	public void showToast(String word) {
		if (word != null && word.length() > 0) {
			Message message = handler.obtainMessage();
			message.obj = word;
			message.what = 3;
			handler.sendMessage(message);
		}
	}

	public void showToast(final String word, final long time) {
		if (word != null && word.length() > 0) {
			Message message = handler.obtainMessage();
			message.obj = word;
			message.what = -3;
			handler.sendMessage(message);
		}
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();
		MockLoginNettyClient.getInstans().quite();
	}


	boolean searchflag = false;

	public void startTimeCode() {
		searchflag = true;
		startCount = 0;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (searchflag) {
					try {
						Thread.sleep(100);
						startCount++;
						if (startCount >= 20) {
							searchflag = false;
							startCount = 0;
							handler.sendEmptyMessage(-2);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	int AlarmFlag = 0;

	public void startTimeAlarm(int flag) {
		AlarmFlag = flag;
		startCount = 0;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (AlarmFlag > 0) {
					try {
						Thread.sleep(100);
						AlarmFlag--;
						//if ((AlarmFlag == 0)||(TcpSender.isGateWay) ) {
						if (AlarmFlag == 0) {
							AlarmFlag = 0;
							handler.sendEmptyMessage(10);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		XPermissionUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	public void setIP(String host, int port) {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
			MockLoginNettyClient.getInstans().setConnectorHost(host + ":" + port);
			MockLoginNettyClient.getInstans().connect();
		} else {
			if (mainClient != null) {
				mainClient.setHost(host);
				mainClient.setPort(port);
				mainClient.Connection();
			}
		}
	}

	public void sendCode(String cmd) {
		if (cmd != null) {
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
				if (ProjectUtils.getRootPath().getControlState() == 2) {
					showToast(getString(R.string.no_roles), 2);
				} else {
					try {
						MockLoginNettyClient.getInstans().SenddData(cmd);
					} catch (Exception e) {
					}
				}
			} else {
				try {
					if (mainClient != null) {
						mainClient.SenddData(cmd);
					} else {
						initSocket();
					}
				} catch (Exception e) {
				}


			}
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 200) {

		}
	}

	public void sendHistory() {
		String PrimaryKey = MyApplication.getInstance().PrimaryKey();
		if (!PrimaryKey.equals("")) {
			History history = SendCMD.getInstance().history;
			if (!history.getHistoryName().equals("")) {
				new SongAsyncTask().execute(history.getHistoryName(), history.getHistoryCode(), history.getMasterMac(), PrimaryKey);
			}
		}
	}

	public class SongAsyncTask extends AsyncTask<String, Void, String> {


		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			try {
				String json = HistoryUtils.insertHistorys(params[0], params[1], params[2], params[3]);

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {

		}
	}

	private class ApkAsynTask extends
			AsyncTask<String, Void, ResultInfo> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected ResultInfo doInBackground(String... params) {
			// TODO Auto-generated method stub
			ResultInfo result = null;
			try {
				result = JSONHelper.parseObject(
						SettingServiceUtils.getVersion("ApkVersion"),
						ResultInfo.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(ResultInfo result) {
			// TODO Auto-generated method stub
			// super.onPostExecute(resString);
			if (result != null && result.getResultCode() == 1) {
				try {
					int apkCode = Integer.parseInt(result.getMsg());
					int current = CurrentVersion.getVerCode(instance);
					if (current < apkCode) {
						handler.sendEmptyMessage(-5);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
				}
			}
		}
	}

	public SocketClient mainClient;

	// 1002设备登录
	public void initSocket() throws InterruptedException {

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
			MockLoginNettyClient mockLoginNettyClient = MockLoginNettyClient.getInstans();
			mockLoginNettyClient.setCoallBack(new ICoallBack() {
				@Override
				public void OnSuccess(Socket socket) {

					handler.sendEmptyMessage(0);
					SendCMD cmdsend = SendCMD.getInstance();
					cmdsend.sendCMD(242,"A", null);
				}

				@Override
				public void OnDisConnect(String e) {

					handler.sendEmptyMessage(2);
				}

				public void OnGateWay() {

				}

			});
			mockLoginNettyClient.connect();
		} else {
			if (mainClient == null) {
				mainClient = new SocketClient(2);
				mainClient.setOnReceiveListener(new ISocketPacket() {
					@Override
					public void SocketPacket(String msg) {

						String result = msg;
						if (!msg.contains("{") || !msg.contains("}")) {
							result = Base64.decodeStr(msg.trim());
						}
						Intent intent = new Intent("com.zunder.smart.receiver");
						Bundle bundle = new Bundle();
						bundle.putString("str", result);
						intent.putExtras(bundle);
						sendBroadcast(intent);
					}
				});
				// 设置连接服务器监听
				mainClient.setOnConnectListener(new ICoallBack() {
					@Override
					public void OnSuccess(Socket client) {
//					showToast("服务器连接成功", 2);
						handler.sendEmptyMessage(0);
					}

					@Override
					public void OnDisConnect(String e) {
						// TODO Auto-generated method stub

						handler.sendEmptyMessage(2);
					}

					@Override
					public void OnGateWay() {
						// TODO Auto-generated method stub
						handler.sendEmptyMessage(1);
					}

				});
			}
		}


	}
	int f = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	class DataTask extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			try {
				result = SettingServiceUtils.setPushMasterState(SystemInfo.getMasterID(MyApplication.getInstance()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {

		}

	}
	public void updatArce(){

	}
	public void closeClient() {
		if (mainClient != null) {
			mainClient.closeConnection();
			mainClient = null;
		}
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
// TODO Auto-generated method stub
		if (isChecked &&  mHost!= null) {
			if (buttonView == mainRadio) {
				mHost.setCurrentTabByTag("Main");
				clickIndex=1;
				Log.e("click","change "+clickIndex);
				TabMainActivity.getInstance().setCamera();
			} else if (buttonView == homeRadio) {
				mHost.setCurrentTabByTag("Home");
				clickIndex=1;
				Log.e("click","change "+clickIndex);
			} else if (buttonView == modeRadio) {
				mHost.setCurrentTabByTag("Mode");
				clickIndex=1;
				Log.e("click","change "+clickIndex);
			} else if (buttonView == myRadio) {
				mHost.setCurrentTabByTag("Mine");
				clickIndex=1;
				Log.e("click","change "+clickIndex);
				TabMyActivity.getInstance().setCamera();
			}
		}
	}
}