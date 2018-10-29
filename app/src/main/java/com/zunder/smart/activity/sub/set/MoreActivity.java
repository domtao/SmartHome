package com.zunder.smart.activity.sub.set;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.listener.DeviceStateListener;
import com.zunder.smart.listener.DeviceStatusListener;
import com.zunder.smart.listener.ElectricListeener;
import com.zunder.smart.model.Device;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.tools.AppTools;


public class MoreActivity extends Activity implements OnClickListener, DeviceStateListener {
	public MoreActivity activity;
	private static int Id;
	private Device device;
	private static int editState;

	public static void startActivity(Activity activity, int _id) {
		Id = _id;
		Intent intent = new Intent(activity, MoreActivity.class);
		activity.startActivityForResult(intent,0);
	}
	private TextView backTxt;
	private TextView titleTxt;
	private CheckBox allONBox;
	private CheckBox allOFFBox;
	private CheckBox bindDeviceBox;
	RelativeLayout linkageLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		activity = this;
		TcpSender.setDeviceStateListener(this);
		backTxt = (TextView) findViewById(R.id.backTxt);
		titleTxt = (TextView) findViewById(R.id.titleTxt);
		allONBox = (CheckBox) findViewById(R.id.allONBox);
		allOFFBox = (CheckBox) findViewById(R.id.allOFFBox);
		bindDeviceBox = (CheckBox) findViewById(R.id.bindDeviceBox);
		linkageLayout=(RelativeLayout)findViewById(R.id.linkageLayout);
		linkageLayout.setOnClickListener(this);
		allONBox.setOnClickListener(this);
		allOFFBox.setOnClickListener(this);
		bindDeviceBox.setOnClickListener(this);
		backTxt.setOnClickListener(this);
		device= DeviceFactory.getInstance().getDevicesById(Id);
		SendCMD.getInstance().sendCMD(253, "06:04",device);
	}


	private  Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				String cmd = msg.obj.toString();
				String CmdType=cmd.substring(4,6);
				int refreshIndex=0;
				int stateValue1=0;
				int stateValue2=0;
				int stateValue3=0;
				int stateValue14=0;
				if(CmdType.equals("23")){
					String memoryIndex=cmd.substring(6,8);
					if(memoryIndex.equals("06")){
						refreshIndex=7;
						stateValue1=Integer.valueOf(cmd.substring(22,24),16);
						stateValue2=Integer.valueOf(cmd.substring(20,22),16);
						stateValue3=Integer.valueOf(cmd.substring(18,20),16);
					}

				}else if(CmdType.equals("12")){
					String memoryIndex=cmd.substring(18,20);
					int stateValue=Integer.valueOf(cmd.substring(22,24),16);
					if(memoryIndex.equals("09")){
						refreshIndex=1;
						stateValue1=stateValue;
					}
					else if(memoryIndex.equals("08")){
						refreshIndex=2;
						stateValue2=stateValue;
					}
					else if(memoryIndex.equals("07")){
						refreshIndex=4;
						stateValue3=stateValue;
					}
				}
				if((refreshIndex&1)>0){
					if((stateValue1&(1<<Integer.parseInt(device.getDeviceIO())))>0){
						allONBox.setChecked(true);
					}else{
						allONBox.setChecked(false);
					}
					if((editState&1)>0) {
						editState^=1;
						ToastUtils.ShowSuccess(activity,
								"全开管制修改成功",
								Toast.LENGTH_SHORT, true);
					}
				}
				if((refreshIndex&2)>0){
					if((stateValue2&(1<<Integer.parseInt(device.getDeviceIO())))>0){
						allOFFBox.setChecked(true);
					}else{
						allOFFBox.setChecked(false);
					}
					if((editState&2)>0) {
						editState^=2;
						ToastUtils.ShowSuccess(activity,
								"全关管制修改成功",
								Toast.LENGTH_SHORT, true);
					}
				}
				if((refreshIndex&4)>0){
					if((stateValue3&(1<<Integer.parseInt(device.getDeviceIO())))>0){
						bindDeviceBox.setChecked(true);
					}else{
						bindDeviceBox.setChecked(false);
					}
					if((editState&4)>0) {
						editState^=4;
						ToastUtils.ShowSuccess(activity,
								"设备绑定修改成功",
								Toast.LENGTH_SHORT, true);
					}
				}
			}
		};
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String cmd;
		switch (v.getId()) {
			case R.id.backTxt:
				TcpSender.setDeviceStateListener(null);
				Intent resultIntent = new Intent();
				this.setResult(100, resultIntent);
				this.finish();
				break;
			case R.id.allONBox:
				//joe 全开管制
				if(allONBox.isChecked()){
					cmd="11:01:09:";
					//allONBox.setChecked(false);
				}else{
					cmd="11:00:09:";
					//allONBox.setChecked(true);
				}
				editState|=1;
				SendCMD.getInstance().sendCMD(254, cmd+AppTools.twos(device.getDeviceIO()),device);
				break;
			case R.id.allOFFBox:
				//joe 全关管制
				if(allOFFBox.isChecked()){
					cmd="11:01:08:";
					//allOFFBox.setChecked(false);
				}else{
					cmd="11:00:08:";
					//allOFFBox.setChecked(true);
				}
				editState|=2;
				SendCMD.getInstance().sendCMD(254, cmd+AppTools.twos(device.getDeviceIO()),device);
				break;
			case R.id.bindDeviceBox:
				//joe 绑定设备
				if(bindDeviceBox.isChecked()){
					cmd="11:01:07:";
					//bindDeviceBox.setChecked(false);
				}else{
					cmd="11:00:07:";
					//bindDeviceBox.setChecked(true);
				}
				editState|=4;
				SendCMD.getInstance().sendCMD(254, cmd+AppTools.twos(device.getDeviceIO()),device);
				break;
			case R.id.linkageLayout:
				LinkageActivity.startActivity(activity,device.getId());
				break;
		default:
			break;
		}

	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == event.KEYCODE_BACK) {
			TcpSender.setDeviceStateListener(null);
			activity = null;
			Intent resultIntent = new Intent();
			this.setResult(100, resultIntent);
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void receiveDeviceStatus(String cmd) {
		if(device!=null){
			if((device.getProductsCode()+device.getDeviceID()).equals(cmd.substring(8,16))){
				Message message = handler.obtainMessage();
				message.what = 1;
				message.obj = cmd;
				handler.sendMessage(message);
			}
		}

	}
}