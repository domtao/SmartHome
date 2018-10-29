/**
 * 
 */
package com.zunder.smart.onekey;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.zunder.smart.R;
import com.zunder.smart.service.UDPServer;
import com.iflytek.smartconfig.client.SmartConfigClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
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
public class SettingWifiActivity extends Activity {
	private EditText edit_wifi_name;
	private EditText edit_wifi_pwd;
	private Button startButton, stopButton;
	private static final String TAG = "MainActivity";

	private static final int PORT = 10255;
	public static final int MESSAGE_TIMEOUT = 1;

	private String mSSID;
	private int mLocalIP;

	private SendThread mSendThread;
	private Thread mUDPThread;
	private Handler mHandler;

	private UDPServer mUdpServer;
	private DatagramSocket mSocket;

	private boolean mIsConnected;
	private boolean mWaitRst;
	private String mWifiPass;
	private String mac;

	private TextView mToast, backView;
	WifiManager wifiManager;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aiui_wifi);
		context=this;
		initView();
		initWifi();
	}
	public void showProgressDialog(String msg) {
		if (progressDialog == null) {

			progressDialog = ProgressDialog.show(context,
					getString(R.string.tip), msg, true, false);

		}

		progressDialog.show();

	}

	ProgressDialog progressDialog = null;

	public void hideProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();


	} // wifi状态广播接收器

	private Boolean searchflag = true;
	private int startCount = 0;

	public void startTime() {
		searchflag = true;
		startCount = 0;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (searchflag) {
					try {
						Thread.sleep(100);
						startCount++;
						if (startCount >= 500) {
							searchflag = false;
							startCount = 0;
							hideProgressDialog();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// 网络状态改变广播
			if (intent.getAction().equals(
					WifiManager.NETWORK_STATE_CHANGED_ACTION)) {

				NetworkInfo info = intent
						.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

				if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
					String ssid = getSSID();
					mIsConnected = true;
					edit_wifi_pwd.setHint("请输入" + ssid + "的密码");
				} else if (info.getState().equals(
						NetworkInfo.State.DISCONNECTED)) {

					mIsConnected = false;
					edit_wifi_pwd.setHint("当前未连接wifi，请连接wifi");
				}
			}
			// WIFI状态改变广播
			else if (intent.getAction().equals(
					WifiManager.WIFI_STATE_CHANGED_ACTION)) {

				int wifistate = intent.getIntExtra(
						WifiManager.EXTRA_WIFI_STATE,
						WifiManager.WIFI_STATE_DISABLED);

				if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
					mIsConnected = false;

					new AlertDialog.Builder(SettingWifiActivity.this)
							.setTitle("提示")
							.setMessage("检测到wifi未打开，前去打开？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											if (android.os.Build.VERSION.SDK_INT > 10) {
												startActivity(new Intent(
														android.provider.Settings.ACTION_SETTINGS));
											} else {
												startActivity(new Intent(
														android.provider.Settings.ACTION_WIRELESS_SETTINGS));
											}
										}
									}).setNegativeButton("取消", null).show();
				}
			}
		}
	};

	private void initWifi() {

		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		registerReceiver(mWifiReceiver, filter);
	}

	private void initUDP() {
		mHandler = new Handler(mMsgCallback);
		mUdpServer = new UDPServer(mHandler, mSocket);
		mUDPThread = new Thread(mUdpServer);
	}

	// UDP返回结果回调
	private Handler.Callback mMsgCallback = new Handler.Callback() {
		@SuppressLint("NewApi")
		@Override
		public boolean handleMessage(Message msg) {

			if (msg.what == MESSAGE_TIMEOUT) {

				showTip("配置WIFI超时，请重试");

				resetSendAndRecv();
				return false;
			}

			String result = msg.getData().get("msg").toString();

			Log.d(TAG, "receive result : " + result);

			try {
				JSONObject jsonRst = new JSONObject(result);
				mac = jsonRst.getString("mac");

			} catch (JSONException e) {
				showTip("解析反馈消息异常！");
			}

			if (!mac.isEmpty() && null != mac) {
				showTip("评估板 " + mac + " 配置完成！");
				resetSendAndRecv();
				hideProgressDialog();
			}
			return false;
		}
	};

	private void initView() {

		backView = (TextView) findViewById(R.id.backTxt);
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mToast = (TextView) findViewById(R.id.tipTxt);
		edit_wifi_name = (EditText) findViewById(R.id.edit_wifi_name);
		edit_wifi_pwd = (EditText) findViewById(R.id.edit_wifi_pwd);
		startButton = (Button) findViewById(R.id.startBtn);
		stopButton = (Button) findViewById(R.id.stopBtn);
		edit_wifi_name.setText(getSSID());
		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!mWaitRst) {

					// if (mIsConnected && edit_wifi_pwd.getText().length() >=
					// 8) {

					if (null == mSendThread) {
						mSendThread = new SendThread();
						mSendThread.start();
					}

					if (null == mUDPThread) {
						mLocalIP = getLocalIP();
						mSSID = getSSID();
						mWifiPass = edit_wifi_pwd.getText().toString();

						if (null == mSocket) {
							try {
								mSocket = new DatagramSocket(PORT);
							} catch (SocketException e) {
								e.printStackTrace();
							}
						}
						initUDP();
						mUDPThread.start();
						mWaitRst = true;
						showProgressDialog("正在配置WIFI,大约需要60秒!");
						showTip("开始配置WIFI,大约需要60s");
						startTime();
					}
					// } else {
					// showTip("未连入有效网络或wifi密码长度小于8,请修改");
					// }
				} else {
					showTip("正在配置WIFI,请稍后");
				}
			}
		});
		stopButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mWaitRst = false;
				if (null != mSendThread) {
					mSendThread.stopRun();
					mSendThread = null;

					mUdpServer.setFlag(false);
					mUDPThread.interrupt();
					mHandler = null;
					mUdpServer = null;
					mUDPThread = null;
				}

				if (null != mSocket) {
					if (!mSocket.isClosed()) {
						mSocket.close();
					}

					mSocket = null;
				}

				showTip("停止配置WIFI");
			}
		});

	}

	private void resetSendAndRecv() {

		mWaitRst = false;
		if (mSendThread != null) {
			mSendThread.stopRun();
			mSendThread = null;
		}

		if (null != mUDPThread) {
			mUdpServer.setFlag(false);
			mUDPThread.interrupt();
			mHandler = null;
			mUdpServer = null;
			mUDPThread = null;
		}

		if (null != mSocket) {
			mSocket.close();
			mSocket = null;
		}
	}

	private int getLocalIP() {
		WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = manager.getConnectionInfo();

		return wifiInfo.getIpAddress();
	}

	// 获取wifi名称
	private String getSSID() {
		WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = manager.getConnectionInfo();

		return wifiInfo.getSSID().replace("\"", "");
	}

	// 发送广播包线程
	class SendThread extends Thread {
		private boolean mStopRun = false;

		public void stopRun() {
			mStopRun = true;
			interrupt();
		}

		@Override
		public void run() {

			SmartConfigClient.setPacketInterval(10);

			while (!mStopRun) {
				try {
					SmartConfigClient.send(mSSID, mWifiPass, mLocalIP);
				} catch (Exception e) {
					Log.e(TAG, "exception ");
				}
			}
		}
	}

	private void showTip(final String tip) {
		if (!TextUtils.isEmpty(tip)) {
			runOnUiThread(new Runnable() {
				public void run() {
					mToast.setText(tip);
					// mToast.show();
					// progressDialog.setTitle(tip);
				}
			});
		}
	}
}
