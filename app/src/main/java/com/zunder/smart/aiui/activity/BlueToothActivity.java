/**
 * 
 */
package com.zunder.smart.aiui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.aiui.adapter.BlueToothAdapter;
import com.zunder.smart.aiui.info.BlueDevice;
import com.zunder.smart.broadcast.ReceiverBroadcast;
import com.zunder.smart.dialog.TipAlert;
import com.zunder.smart.dialog.WarnDialog;
import com.zunder.smart.listener.BlueToothListener;
import com.zunder.smart.listview.SwipeListView;
import com.zunder.smart.socket.info.ISocketCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 
 */
public class BlueToothActivity extends Activity implements OnClickListener,BlueToothListener {

	private TextView edite;
	SwipeListView listView;
	BlueToothActivity activity;
	TextView titleTxt,backTxt;
	WarnDialog warnDialog;
	BlueToothAdapter blueToothAdapter;
	List<BlueDevice> list=new ArrayList<BlueDevice>();

	public static void startActivity(Activity activity) {
		Intent intent = new Intent(activity, BlueToothActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.bluetooth_main);
		ReceiverBroadcast.setBlueToothListener(this);
		activity = this;
		initView();
		showDialog();
	}
	public void showDialog(){
			if (warnDialog == null) {
				warnDialog = new WarnDialog(activity, getString(R.string.search));
				warnDialog.setMessage("正在获取设备 5");
				warnDialog.setSureListener(new WarnDialog.OnSureListener() {
					@Override
					public void onCancle() {
						searchflag = false;
						warnDialog.dismiss();
					}
				});
			}
		warnDialog.show();
		startTime();
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
						if (startCount >= 20) {
							searchflag = false;
							if ((warnDialog != null) && warnDialog.isShowing()) {
								warnDialog.dismiss();
								handler.sendEmptyMessage(-1);
							}
						}else{
							Message message = handler.obtainMessage();
							message.what = -2;
							handler.sendMessage(message);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		list.clear();
		sendCode("getDevice:00");
//		startTime();
	}

	private void initView() {
		backTxt=(TextView)findViewById(R.id.backTxt);
		backTxt.setOnClickListener(this);
		titleTxt=(TextView)findViewById(R.id.titleTxt);
		edite = (TextView) findViewById(R.id.edite);
		edite.setOnClickListener(this);
		listView = (SwipeListView) findViewById(R.id.deviceList);
		listView.setEmptyView((TextView)findViewById(R.id.emptyName));
		blueToothAdapter=new BlueToothAdapter(activity,list);
		listView.setAdapter(blueToothAdapter);
	}
	public void onItemClick(int pos){
		BlueDevice blueDevice=list.get(pos);
		String address=blueDevice.getAddress();
		if(blueDevice.getBoundState()==12){
			if(blueDevice.getConnectState()==2) {
				blueDevice.setConnectState(0);
				sendCode("setDisConnect:"+address);
			}else{
				blueDevice.setConnectState(1);
				sendCode("setConnect:"+address);
			}
		}else{
			blueDevice.setConnectState(11);
			sendCode("setBound:"+address);
		}
		handler.sendEmptyMessage(1);
	}
	private Handler handler=new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what){
				case -2:
					warnDialog.setMessage("正在获取蓝牙设备 "+startCount);
					break;
				case -1:
					TipAlert errAlert = new TipAlert(activity,
							getString(R.string.tip),getString(R.string.pass));
					errAlert.show();
					break;
				case 0:
					if ((warnDialog != null) && warnDialog.isShowing()) {
						searchflag = false;
						startCount=0;
						warnDialog.dismiss();
					}
					TipAlert alert = new TipAlert(activity,
							getString(R.string.tip), msg.obj.toString());
					alert.show();
					break;
				case 1:
					if ((warnDialog != null) && warnDialog.isShowing()) {
						searchflag = false;
						startCount=0;
						warnDialog.dismiss();
					}
					sort(list);
					blueToothAdapter.notifyDataSetChanged();
					break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.edite:
			break;
			case R.id.backTxt:
				finish();
				break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ReceiverBroadcast.setBlueToothListener(null);
	}
	@Override
	public void SearchDevice(BlueDevice blueDevice) {
		Add(blueDevice);
		handler.sendEmptyMessage(1);
	}

	@Override
	public void OnBondState(String address, int state) {

		for (int i=0;i<list.size();i++) {
			if (list.get(i).getAddress().equals(address)) {
				list.get(i).setBoundState(state);
				break;
			}
		}
		handler.sendEmptyMessage(1);
	}

	@Override
	public void OnConnectState(String address, int state) {

		for (int i=0;i<list.size();i++) {
			if (list.get(i).getAddress().equals(address)) {
				list.get(i).setConnectState(state);
				break;
			}
		}
		handler.sendEmptyMessage(1);
	}

	public void sendCode(String cmd){
		String result = ISocketCode.setBlueTooth(cmd,
			AiuiMainActivity.deviceID);
		MainActivity.getInstance().sendCode(result);
	}


	public void Add(BlueDevice _blueDevice) {

		boolean flag=true;
		for (int i=0;i<list.size();i++) {
			BlueDevice blueDevice=list.get(i);
			if (blueDevice.getAddress().equals(_blueDevice.getAddress())) {
				blueDevice.setBoundState(_blueDevice.getBoundState());
				flag=false;
				break;
			}
		}
		if(flag){
			list.add(_blueDevice);
		}
	}

	public void sort(List<BlueDevice> list) {
		int i, j, small;
		BlueDevice temp;
		int n = list.size();
		for (i = 0; i < n - 1; i++) {
			small = i;
			for (j = i + 1; j < n; j++) {
				if (list.get(j).getBoundState() > list.get(small).getBoundState())
					small = j;
				if (small != i) {
					temp = list.get(i);
					list.set(i, list.get(small));
					list.set(small, temp);
				}
			}
		}
	}
}