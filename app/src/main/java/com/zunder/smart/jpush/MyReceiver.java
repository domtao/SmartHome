package com.zunder.smart.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.json.SafeInfoUtils;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.History;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.setting.ProjectUtils;
import com.zunder.smart.tools.AppTools;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
				//MyApplication.AlarmWake =1;
			String content = bundle.getString(JPushInterface.EXTRA_ALERT);
			if(content==null||content=="null"||content.equals("null")){
				return;
			}

			History history=new History();
			history.setHistoryName(content);
			history.setCreateTime(AppTools.getCurrentTime());
			SafeInfoUtils.addProject(history);

			if(content.length()>3) {
				int index = content.indexOf("[");
				String deviceID = content
						.substring(index + 1, content.length() - 1);
				Device device = DeviceFactory.getInstance().getDeviceID(deviceID);
					if (MyApplication.SystemStart) {
						if (device == null ||(device!=null&& device.getId() == 0)) {
							Intent intent2 = new Intent(MyApplication.getInstance(),
									TestActivity.class);
							intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent2.putExtras(bundle);
							MyApplication.getInstance().startActivity(intent2);
							//ToastUtils.ShowError(MyApplication.getInstance(), "device == null" + deviceID, Toast.LENGTH_SHORT, true);
						} else {
							//弹出框
							Intent intent2 = new Intent(MyApplication.getInstance(),
									TestActivity.class);
							intent2.putExtras(bundle);
							intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent2.putExtras(bundle);
							MyApplication.getInstance().startActivity(intent2);
						}

					} else {
						ProjectUtils.getRootPath().setAlarmId(deviceID);
						ProjectUtils.saveRootPath();
						Intent intent2 = new Intent(MyApplication.getInstance(),
								MainActivity.class);
						intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
						Bundle bundle1 = new Bundle();
						intent2.putExtras(bundle1);
						MyApplication.getInstance().startActivity(intent2);
					}
			}
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			String content = bundle.getString(JPushInterface.EXTRA_ALERT);
			int index = content.indexOf("[");
			String deviceID = content
					.substring(index + 1, content.length() - 1);
			if(MyApplication.SystemStart) {
				// 打开自定义的Activity
				//if (TcpSender.isGateWay) {
					Device device = DeviceFactory.getInstance().getDeviceID(deviceID);
					if (device == null) {
						Intent i = new Intent(MyApplication.getInstance(), TestActivity.class);
						i.putExtras(bundle);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
						context.startActivity(i);
						//ToastUtils.ShowError(MyApplication.getInstance(), "device == null"+deviceID, Toast.LENGTH_SHORT, true);
					} else {
						TcpSender.preStatStr = "";
//						AnHonActivity.startActivity(MyApplication.getInstance(), device.getId(), 0);
					/*
					if(AnHonActivity.isOpen==0) {
						TcpSender.preStatStr="";
						Intent intent2 = new Intent(MainActivity.getInstance(),
								AnHonActivity.class);
						intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
						Bundle bundle1 = new Bundle();
						bundle1.putInt("id", device.getId());
						bundle1.putInt("musicid", R.raw.ringmusic);
						intent2.putExtras(bundle1);
						MainActivity.getInstance().startActivity(intent2);
					}
					*/
					}
				//}
			}else{
				ProjectUtils.getRootPath().setAlarmId(deviceID);
				ProjectUtils.saveRootPath();
				Intent intent2 = new Intent(MyApplication.getInstance(),
						MainActivity.class);
				intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Bundle bundle1 = new Bundle();
				intent2.putExtras(bundle1);
				MyApplication.getInstance().startActivity(intent2);
			}
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..
		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction()
					+ " connected state change to " + connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (TextUtils.isEmpty(bundle
						.getString(JPushInterface.EXTRA_EXTRA))) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(
							bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it = json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" + myKey + " - "
								+ json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	// send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
	}
}
