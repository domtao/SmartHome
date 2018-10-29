package com.zunder.smart.dialog;

import java.util.List;

import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.aiui.activity.AiuiMainActivity;
import com.zunder.smart.broadcast.ReceiverBroadcast;
import com.zunder.smart.dao.impl.factory.RoomFactory;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.dao.impl.factory.ModeListFactory;
import com.zunder.smart.dao.impl.factory.RedInfraFactory;
import com.zunder.smart.listener.DownListener;
import com.zunder.smart.model.Room;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.Mode;
import com.zunder.smart.model.ModeList;
import com.zunder.smart.model.RedInfra;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.JSONHelper;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownAlert extends Dialog implements OnClickListener, DownListener {
	private  Object sendLock = new Object();
	private Activity context;
	private TextView countText;
	private TextView leftText, errorTxt;
	private Button cancleBtn, sureBtn;
	ProgressBar progressBar;
	int indexValue = 0;
	int indexError = 0;
	int count = 0;
	private int sendIndex=0;
	String tempResult="";
	public DownAlert(Activity context) {
		super(context, R.style.MyDialog);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_down_verify);
		this.context = context;
		ReceiverBroadcast.setDownListener(this);
		cancleBtn = (Button) findViewById(R.id.cancle_bt);
		sureBtn = (Button) findViewById(R.id.sure_bt);
		cancleBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		countText = (TextView) findViewById(R.id.countTxt);
		leftText = (TextView) findViewById(R.id.leftTxt);
		errorTxt = (TextView) findViewById(R.id.error);
		progressBar = (ProgressBar) findViewById(R.id.proBar);
		count = RoomFactory.getInstance().getAll().size()+ DeviceFactory.getInstance().getAll().size()  + ModeFactory.getInstance().getAll().size()
				+ ModeListFactory.getInstance().getAll().size()+RedInfraFactory.getInstance().getAll().size();
		countText.setText(context.getString(R.string.dateDown) + count + context.getString(R.string.page));
		progressBar.setMax(count);
		leftText.setText("0 %");
	}
	private Handler handler = new Handler() {
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case 1:
				progressBar.setProgress(indexValue);
				countText.setText( context.getString(R.string.count)+ count + context.getString(R.string.page)+","+context.getString(R.string.sort)+context.getString(R.string.dateDown)+ indexValue + context.getString(R.string.page));
				leftText.setText((indexValue * 100 / progressBar.getMax())
						+ "%");
				if (indexValue>= progressBar.getMax()) {
					ReceiverBroadcast.setDownListener(null);
					sureBtn.setClickable(true);
					searchflag=false;
					String result = ISocketCode.setDelAllDevice("CancleDown", AiuiMainActivity.deviceID);
					MainActivity.getInstance().sendCode(result);
					TipAlert alert = new TipAlert(context, context.getString(R.string.tip) , context.getString(R.string.downsuccess) );
					alert.show();
					dismiss();
				}
				break;
			case 2:
				errorTxt.setText(context.getString(R.string.downfail)  + indexError +context.getString(R.string.page) );
				break;
			case 3:
				errorTxt.setText(context.getString(R.string.downtime) +":"+startCount+context.getString(R.string.Second));
			break;
			case 4:
				TipAlert alert = new TipAlert(context, context.getString(R.string.tip) , context.getString(R.string.downfail));
				alert.show();
				dismiss();
			break;
			default:
				break;
			}
		};
	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancle_bt:
			indexValue = 0;
			searchflag=false;
			ReceiverBroadcast.setDownListener(null);
			dismiss();
			break;
		case R.id.sure_bt:
			indexValue = 0;
			startTime();
			String result = ISocketCode.setDelAllDevice("AllDown",AiuiMainActivity.deviceID);
			MainActivity.getInstance().sendCode(result);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						down();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
			sureBtn.setClickable(false);
			break;
		default:
			break;
		}
	}
	@Override
	public void count(String number) {
		// TODO Auto-generated method stub
		if(AppTools.isNumeric(number)) {
			if (Integer.parseInt(number) == 1) {
				sendIndex = 0;
				indexValue++;
				Message msg = handler.obtainMessage();
				msg.what = 1;
				handler.sendMessage(msg);
				synchronized (sendLock) {
					sendLock.notifyAll();
				}
			} else {
				indexError++;
				Message msg = handler.obtainMessage();
				msg.what = 2;
				handler.sendMessage(msg);
			}
		}
	}
	public void down()throws Exception{
		List<Room> listRoom= RoomFactory.getInstance().getAll();
		List<Device> listDevice = DeviceFactory.getInstance().getAll();
		List<Mode> listMode = ModeFactory.getInstance().getAll();
		List<ModeList> modeList = ModeListFactory.getInstance().getAll();
		List<RedInfra> listInfra = RedInfraFactory.getInstance().getAll();
		for (Room device : listRoom) {
				String deviceID = AiuiMainActivity.deviceID;
				Log.e("down", device.convertTostring());
				tempResult = ISocketCode.setAddArce(JSONHelper.toJSON(device),
						deviceID);
				MainActivity.getInstance().sendCode(tempResult);
				sendIndex = 1;
				synchronized (sendLock) {
					sendLock.wait();
				}
		}
		for (Device device : listDevice) {
				String deviceID = AiuiMainActivity.deviceID;
				Log.e("down", device.convertTostring());
				tempResult = ISocketCode.setAddDevice(JSONHelper.toJSON(device),
						deviceID);
				MainActivity.getInstance().sendCode(tempResult);
				sendIndex = 1;
				synchronized (sendLock) {
					sendLock.wait();
				}
		}
		for (Mode mode : listMode) {
			String deviceID = AiuiMainActivity.deviceID;
			Log.e("down",mode.convertTostring());
			tempResult = ISocketCode.setAddMode(JSONHelper.toJSON(mode),
					deviceID);
			MainActivity.getInstance().sendCode(tempResult);
			sendIndex=1;
			synchronized (sendLock) {
				sendLock.wait();
			}
		}
		for (ModeList mode : modeList) {
			String deviceID = AiuiMainActivity.deviceID;
			Log.e("down",mode.convertTostring());
			tempResult= ISocketCode.setAddModeList(JSONHelper.toJSON(mode),
					deviceID);
			MainActivity.getInstance().sendCode(tempResult);
			sendIndex=1;
			synchronized (sendLock) {
				sendLock.wait();
			}
		}
		for (RedInfra infra : listInfra) {
			String deviceID = AiuiMainActivity.deviceID;
			tempResult= ISocketCode.setAddRedInFra(JSONHelper.toJSON(infra),
					deviceID);
			MainActivity.getInstance().sendCode(tempResult);
			sendIndex=1;
			synchronized (sendLock) {
				sendLock.wait();
			}
		}
	}
	private boolean searchflag = false;
	private int startCount = 0;
	private void startTime() {
		startCount=0;
		searchflag=true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (searchflag) {
					try {
						Thread.sleep(1000);
						startCount++;
						sendIndex++;
						if (startCount>=120) {
							searchflag = false;
							handler.sendEmptyMessage(4);
						}else{
							Message message = handler.obtainMessage();
							message.what = 3;
							handler.sendMessage(message);
						}
						if(sendIndex>=4){
							MainActivity.getInstance().sendCode(tempResult);
							Log.e("down", tempResult);
							sendIndex=1;
						}
						Log.e("down", "run: "+sendIndex);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
