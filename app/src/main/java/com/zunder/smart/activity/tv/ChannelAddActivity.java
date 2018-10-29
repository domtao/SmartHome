package com.zunder.smart.activity.tv;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.popu.dialog.BandViewWindow;
import com.zunder.smart.activity.popu.dialog.KeyViewWindow;
import com.zunder.smart.activity.popu.dialog.VersionViewWindow;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.dao.impl.factory.RedInfraFactory;
import com.zunder.smart.dialog.RedFraAlert;
import com.zunder.smart.dialog.RedFraCloundAlert;
import com.zunder.smart.dialog.TimeAlert;
import com.zunder.smart.listener.RedFralistener;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.InfraCode;
import com.zunder.smart.model.InfraName;
import com.zunder.smart.model.InfraVersion;
import com.zunder.smart.model.ProjectorName;
import com.zunder.smart.model.RedInfra;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.SendThread;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.utils.ListNumBer;
import com.zunder.smart.webservice.InfraServiceUtils;
import com.zunder.smart.webservice.ProjectorServiceUtils;

import java.util.List;

public class ChannelAddActivity extends Activity implements OnClickListener{

	private TextView backTxt;
	private TextView editeTxt;
	public static  int roomId=1;

	int Id = 0;
	private EditText deviceName;
	private EditText deviceID;
	private String deviceNameStr="";
	private String Edite = "Add";


	public static ChannelAddActivity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel_add);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Bundle bundle = getIntent().getExtras();
		Edite = bundle.getString("Edite");
		activity = this;
		deviceName = (EditText) findViewById(R.id.deviceName);
		deviceID = (EditText) findViewById(R.id.deviceID);
		backTxt = (TextView) findViewById(R.id.backTxt);
		editeTxt = (TextView) findViewById(R.id.editeTxt);

		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		if (Edite.equals("Edite")) {
			Id = bundle.getInt("id");
			Device device = DeviceFactory.getInstance().getDevicesById(Id);
			if (device != null) {
				deviceName.setText(device.getDeviceName());
				String deviceId=device.getDeviceID();
				int len=Integer.parseInt(deviceId.substring(0,1));
				deviceID.setText(deviceId.substring(1,(len+1)));
				deviceNameStr=device.getDeviceName();
			}
		} else {
			deviceName.setText("");
			deviceID.setText("");
		}
	}

	public static String toHex(int number) {
		if (number <= 15) {
			return ("0" + Integer.toHexString(number).toUpperCase());
		} else {
			return Integer.toHexString(number).toUpperCase();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.backTxt:
				TcpSender.setRedFralistener(null);
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("result", "Add");
				resultIntent.putExtras(bundle);
				this.setResult(100, resultIntent);
				finish();
				break;
			case R.id.editeTxt:
				save();
				break;
			case R.id.startTime:
				break;
			case R.id.endTime:
				break;
			default:
				break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TcpSender.setRedFralistener(null);
	}

	public void save() {
		if (TextUtils.isEmpty(deviceName.getText())) {

			ToastUtils.ShowError(activity,
					getString(R.string.words_null),
					Toast.LENGTH_SHORT,true);
			return;
		}
		if (TextUtils.isEmpty(deviceID.getText())) {
			ToastUtils.ShowError(activity,
					getString(R.string.id_null),
					Toast.LENGTH_SHORT,true);
			return;
		}

		if (roomId ==0) {
			ToastUtils.ShowError(activity,
					getString(R.string.arce_null),
					Toast.LENGTH_SHORT,true);
			return;
		}
		String deviceId="000000";
		if(deviceID.getText().toString().length()==2){
			deviceId="2"+deviceID.getText().toString()+"000";
		}else if(deviceID.getText().toString().length()==3){
			deviceId="3"+deviceID.getText().toString()+"00";
		}

		Device device = new Device();
		device.setId(Id);
		device.setDeviceName(deviceName.getText().toString());
		device.setDeviceID(deviceId);
		device.setDeviceIO("0");
		device.setDeviceNickName("");
		device.setDeviceImage("");
		device.setSeqencing(0);
		device.setDeviceOnLine(1);
		device.setDeviceTimer("0");
		device.setDeviceOrdered("0");
		device.setStartTime("00:00");
		device.setEndTime("00:00");
		device.setSceneId(0);
		device.setDeviceTypeKey(19);
		device.setProductsKey(92);
		device.setGradingId(1);
		device.setRoomId(roomId);
		if (Edite.equals("Add")) {
			String resultStr=DeviceFactory.getInstance().judgeName(deviceName.getText().toString(),roomId);
			if (!resultStr.equals("0")) {
				ToastUtils.ShowError(activity,resultStr,Toast.LENGTH_SHORT,true);
			}else if(ModeFactory.getInstance().judgeName(deviceName.getText().toString()) != 0){
				ToastUtils.ShowError(activity,getString(R.string.mode_exite)+"["+deviceName.getText().toString()+"]",Toast.LENGTH_SHORT,true);
			}else{
				int result = sql().insertDevice(device);
				if (result > 1) {
					ToastUtils.ShowSuccess(activity,
							getString(R.string.addSuccess),
							Toast.LENGTH_SHORT,true);
					DeviceFactory.getInstance().clearList();
					SendCMD._GetCmdArr.clear();
					deviceName.setText("");
					deviceID.setText("");

				}
			}
		} else if (Edite.equals("Edite")) {
			if (DeviceFactory.getInstance().updateName(Id, deviceName.getText()
					.toString(), deviceNameStr,roomId) != 0) {
				ToastUtils.ShowError(activity,getString(R.string.device_exite)+"["+deviceName.getText().toString()+"]",Toast.LENGTH_SHORT,true);
			}else if(ModeFactory.getInstance().judgeName(deviceName.getText().toString()) != 0) {
				ToastUtils.ShowError(activity,getString(R.string.mode_exite)+"["+deviceName.getText().toString()+"]",Toast.LENGTH_SHORT,true);
			}else{
				int num = sql().updateDevice(device);
				if (num > 0) {
					deviceNameStr = deviceName.getText().toString();
					DeviceFactory.getInstance().clearList();
					SendCMD._GetCmdArr.clear();
					ToastUtils.ShowSuccess(activity,
							getString(R.string.updateSuccess),
							Toast.LENGTH_SHORT, true);
				}else{
					ToastUtils.ShowError(activity,getString(R.string.device_exite)+"["+deviceName.getText().toString()+"]",Toast.LENGTH_SHORT,true);
				}
			}
		}
	}
	public IWidgetDAO sql() {
		return MyApplication.getInstance().getWidgetDataBase();
	}

}
