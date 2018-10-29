package com.zunder.smart.activity.sub.set;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zunder.smart.R;
import com.zunder.smart.activity.sub.AirActivity;
import com.zunder.smart.adapter.DeviceTimerAdapter;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.ProductFactory;
import com.zunder.smart.dialog.ButtonAlert;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.MutilCheckAlert;
import com.zunder.smart.listener.ElectricListeener;
import com.zunder.smart.listener.TimerListener;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.DeviceTimer;
import com.zunder.smart.model.Products;
import com.zunder.smart.popwindow.DeviceTimerPopupWindow;
import com.zunder.smart.roll.WheelView;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.SendThread;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.setting.ProjectUtils;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.utils.ListNumBer;
import com.zunder.smart.view.ListViewDecoration;

import java.util.ArrayList;
import java.util.List;


public class ElectricActivity extends Activity implements OnClickListener,ElectricListeener {
	// joe 电流侦测
	public ElectricActivity activity;
	private static int Id;
	private Device device;

	public static void startActivity(Activity activity, int _id) {
		Id = _id;
		Intent intent = new Intent(activity, ElectricActivity.class);
		activity.startActivityForResult(intent,0);
	}

	private TextView backTxt;
	private TextView titleTxt;
	private TextView minTxt;
	private Button readMin;
	private TextView maxTxt;
	private Button readMax;
	private TextView setTxt;
	private Button setBtn;
	private Button sureBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_electric);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		activity = this;
		TcpSender.setElectricListeener(this);
		backTxt = (TextView) findViewById(R.id.backTxt);
		titleTxt = (TextView) findViewById(R.id.titleTxt);
		minTxt = (TextView) findViewById(R.id.minTxt);
		readMin = (Button) findViewById(R.id.readMin);
		maxTxt = (TextView) findViewById(R.id.maxTxt);
		readMax = (Button) findViewById(R.id.readMax);
		setTxt = (TextView) findViewById(R.id.setTxt);
		setBtn = (Button) findViewById(R.id.setBtn);
		sureBtn = (Button) findViewById(R.id.sureBtn);
		backTxt.setOnClickListener(this);
		readMin.setOnClickListener(this);
		readMax.setOnClickListener(this);
		setBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		device=DeviceFactory.getInstance().getDevicesById(Id);
		if(device!=null){
			titleTxt.setText(device.getRoomName()+device.getDeviceName()+" 电流侦测");
		}
		SendCMD.getInstance().sendCMD(245, "5",
				device);
		SendCMD.getInstance().sendCMD(245, "2",
				device);
	}


	private  Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				{
					String cmd = msg.obj.toString();
					Device device = DeviceFactory.getInstance().getDevicesById(Id);
					String CommandType = cmd.substring(4, 6);
					String nowDeviceId = cmd.substring(10, 16);
					if(device.getDeviceID().equals(nowDeviceId)) {
						if ("12".equals(CommandType)) {

							String deviceProductCode = cmd.substring(8, 10);
							String memoryIndex = cmd.substring(18, 20);
							if (deviceProductCode.equals("05") && memoryIndex.equals("31")) { // memory
								String productsCode = cmd.substring(22, 24);
								if (productsCode.equals(device.getProductsCode())||(productsCode.equals("01")&&device.getProductsCode().equals("05"))) {
									sureBtn.setText("目前设备已开启电流侦测");
									sureBtn.setEnabled(false);
								} else {
									sureBtn.setText("设置电流");
									sureBtn.setEnabled(true);
								}
							}
						} else if (CommandType.equals("08")) {
							int function = Integer.parseInt(cmd.substring(16, 18));
							int value = Integer.valueOf(
									cmd.substring(20, 22), 16);

							switch (function) {
								case 0:
									minTxt.setText("最小值:" + value);
									break;
								case 1:
									maxTxt.setText("最大值:" + value);
									SendCMD.getInstance().sendCMD(245, "2",
											device);
									break;
								case 2:
									setTxt.setText("设定值:" + value);
									//SendCMD.getInstance().sendCMD(245, "5",device);
									break;
							}
						}
					}
				}
			}
		};
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			Intent resultIntent = new Intent();
			this.setResult(0, resultIntent);
			this.finish();
			break;
			case R.id.editeTxt:
				break;
			case R.id.readMin:
				//joe 读取最小值
				SendCMD.getInstance().sendCMD(245, "0", device);
				break;
			case R.id.readMax:
				//joe 读取最大值
				SendCMD.getInstance().sendCMD(245, "1",device);
				break;
			case R.id.setBtn:
				//joe 测试
				SendCMD.getInstance().sendCMD(245, "4",device);
				break;
			case R.id.sureBtn:
				//joe 设置电流值
				DialogAlert alert = new DialogAlert(activity);
				alert.init(getString(R.string.tip), getString(R.string.is_set_dete));
				alert.setSureListener(new DialogAlert.OnSureListener() {

					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						SendCMD.getInstance().sendCMD(245, "3",
								device);
						SendCMD.getInstance().sendCMD(245, "5",
								device);
					}
					@Override
					public void onCancle() {
						// TODO Auto-generated method stub
					}
				});
				alert.show();

				break;
		default:
			break;
		}

	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == event.KEYCODE_BACK) {
			TcpSender.setTimerListener(null);
			activity = null;
			Intent resultIntent = new Intent();
			this.setResult(0, resultIntent);
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}


	@Override
	public void setElectric(String cmd) {
		String cmdType = cmd.substring(0, 2);
		if (cmdType.equals("*C")) {
			if(cmd.substring(10,16).equals(device.getDeviceID())) {
					Message message = handler.obtainMessage();
					message.what = 1;
					message.obj = cmd;
					handler.sendMessage(message);
			}
		}
	}
}