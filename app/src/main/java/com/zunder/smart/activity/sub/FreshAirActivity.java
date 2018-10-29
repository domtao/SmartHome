package com.zunder.smart.activity.sub;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.activity.sub.set.DeviceTimerActivity;
import com.zunder.smart.activity.sub.set.MoreActivity;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.EditTxtAlert;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.Device;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.utils.ListNumBer;

@SuppressLint("NewApi")
public class FreshAirActivity extends BaseActivity implements OnClickListener {
	private static Activity mContext;

	TextView editeText,backTxt,tempText,workText,valueText,speedText,pmText,vocText,show_air;
	private Button workBtn,valueBtn,speedBtn;
	CheckBox checkBox_power;
	static int id;
	static Device deviceParams = null;
	private int speedIndex=0;

	public static void startActivity(Activity activity, int _id) {
		id = _id;
		Intent intent = new Intent(activity, FreshAirActivity.class);
		activity.startActivityForResult(intent,100);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_fresh_air);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		TcpSender.setDeviceStateListener(this);
		TcpSender.setElectricListeener(this);
		mContext = this;
		deviceParams= DeviceFactory.getInstance().getDevicesById(id);
		findView();
		initRightButtonMenuAlert();

	}
		private void findView() {

			backTxt = (TextView) findViewById(R.id.backTxt);
			pmText= (TextView) findViewById(R.id.pmText);
			vocText = (TextView) findViewById(R.id.vocText);
			tempText= (TextView) findViewById(R.id.tempText);
			workText= (TextView) findViewById(R.id.workText);
			valueText= (TextView) findViewById(R.id.valueText);
			speedText= (TextView) findViewById(R.id.speedText);
            editeText = (TextView) findViewById(R.id.editeText);
			checkBox_power=(CheckBox)findViewById(R.id.checkBox_power);
			show_air=(TextView)findViewById(R.id.show_air);
			workBtn=(Button)findViewById(R.id.workBtn);
			valueBtn=(Button)findViewById(R.id.valueBtn);
			speedBtn=(Button)findViewById(R.id.speedBtn);

			workBtn.setOnClickListener(this);
			valueBtn.setOnClickListener(this);
			speedBtn.setOnClickListener(this);
			tempText.setOnClickListener(this);
			editeText.setOnClickListener(this);
			backTxt.setOnClickListener(this);
			checkBox_power.setOnClickListener(this);
			show_air.setText(deviceParams.getDeviceName());

	}
	private RightMenu rightButtonMenuAlert;

	private void initRightButtonMenuAlert() {
		rightButtonMenuAlert = new RightMenu(mContext, R.array.right_fresh_air,
				R.drawable.right_freshair_images);
		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {

			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub
				switch (position) {
					case 0:{
						final EditTxtAlert alert = new EditTxtAlert(mContext);
						alert.setTitle(android.R.drawable.ic_dialog_info, "PM2.5浓度",0);
						alert.setHint("1--800表示1--800ug/m3");
						alert.setOnSureListener(new EditTxtAlert.OnSureListener() {

							@Override
							public void onSure(String fileName) {
								// TODO Auto-generated method stub
								if (fileName.equals("")) {
									ToastUtils.ShowError(mContext,getString(R.string.txt_null),Toast.LENGTH_SHORT,true);
									return;
								}
								alert.dismiss();
							}
							@Override
							public void onCancle() {
								// TODO Auto-generated method stub
							}
						});
						alert.show();
					}
						break;
					case 1: {
						final EditTxtAlert alert = new EditTxtAlert(mContext);
						alert.setTitle(android.R.drawable.ic_dialog_info, getString(R.string.vocdiget), 0);
						alert.setHint("1--300表示0.1--30ppm");
						alert.setOnSureListener(new EditTxtAlert.OnSureListener() {

							@Override
							public void onSure(String fileName) {
								// TODO Auto-generated method stub
								if (fileName.equals("")) {
									ToastUtils.ShowError(mContext, getString(R.string.txt_null), Toast.LENGTH_SHORT, true);
									return;
								}
								alert.dismiss();
							}

							@Override
							public void onCancle() {
								// TODO Auto-generated method stub
							}
						});
						alert.show();
					}
						break;
					case 2:
						DeviceTimerActivity.startActivity(mContext,deviceParams.getId());
						break;
					case 3:
						MoreActivity.startActivity(mContext,deviceParams.getId());
						break;

				}
				rightButtonMenuAlert.dismiss();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		TcpSender.setDeviceStateListener(this);
		SendCMD.getInstance().sendCMD(255,"1",deviceParams);
	}
	protected void onDestroy() {
		super.onDestroy();
		TcpSender.setElectricListeener(null);
	}
	@Override
	public void receiveDeviceStatus(String cmd) {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(0);

	}


	@Override
	public void setElectric(String cmd) {
		// TODO Auto-generated method stub
	}
	public void back(){
		Intent resultIntent = new Intent();
		setResult(0, resultIntent);
		finish();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.backTxt:
				back();
				break;
			case R.id.editeText:
				rightButtonMenuAlert.show(editeText);
				break;
			case R.id.workBtn:{
				final AlertViewWindow alertViewWindow=new AlertViewWindow(mContext,getString(R.string.work), ListNumBer.getWorks(),null,0);
				alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String itemName) {
						if(pos==2){
							DeviceTimerActivity.startActivity(mContext, id);
						}else{
//							workText.setText(itemName);
							String cmd= deviceParams.getDeviceName() + itemName;
							SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+cmd, deviceParams);
						}
						alertViewWindow.dismiss();
					}
				});
				alertViewWindow.show();
			}
				break;
			case R.id.valueBtn:{
				final AlertViewWindow alertViewWindow=new AlertViewWindow(mContext,getString(R.string.sideValue),ListNumBer.getValues(),null,0);
				alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String itemName) {
//						valueText.setText(itemName);
						String cmd=
								deviceParams.getDeviceName() + itemName;
						SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+cmd,deviceParams);
						alertViewWindow.dismiss();
					}
				});
				alertViewWindow.show();
			}
				break;
			case R.id.speedBtn:
			{
				final AlertViewWindow alertViewWindow=new AlertViewWindow(mContext,getString(R.string.speed),ListNumBer.getSpeeds(),null,speedIndex);
				alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String itemName) {
//						speedText.setText(itemName);

						String cmd=
								deviceParams.getDeviceName() + itemName;
						SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+cmd,deviceParams);
						alertViewWindow.dismiss();
					}
				});
				alertViewWindow.show();
			}
				break;
			case R.id.tempText:
				final AlertViewWindow alertViewWindow=new AlertViewWindow(mContext,getString(R.string.tempC),ListNumBer.getTemp(0,51),null,Integer.parseInt(tempText.getText().toString()));
				alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String itemName) {
						tempText.setText(itemName);
						alertViewWindow.dismiss();
					}
				});
				alertViewWindow.show();
				break;
			case R.id.checkBox_power:
				if(checkBox_power.isChecked()){
					String cmd=deviceParams.getDeviceName() + getString(R.string.open_1);
					SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+cmd,deviceParams);
					checkBox_power.setChecked(false);
					ToastUtils.ShowSuccess(mContext,cmd,Toast.LENGTH_SHORT,true);
				}else{
					String cmd=
							deviceParams.getDeviceName() + getString(R.string.close_1);
					SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+cmd,deviceParams);
					checkBox_power.setChecked(true);
					ToastUtils.ShowSuccess(mContext,cmd,Toast.LENGTH_SHORT,true);
				}
				break;
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	private  Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {
				if (msg.what == 0) {
					if (deviceParams.getState() > 0) {
						checkBox_power.setChecked(true);
					} else {
						checkBox_power.setChecked(false);
					}
					 speedIndex=deviceParams.getDeviceAnalogVar2() / 16;
					if(speedIndex==0){
						speedText.setText(getString(R.string.speed0));
					}else if(speedIndex==1){
						speedText.setText(getString(R.string.speed1));
					}else if(speedIndex==2){
						speedText.setText(getString(R.string.speed2));
					}else if(speedIndex==3){
						speedText.setText(getString(R.string.speed3));
					}
					int temp=deviceParams.getDeviceAnalogVar3()&0x003F;//溫度
					tempText.setText(temp+"");
					int side=deviceParams.getDeviceAnalogVar3()>>6;//閥門
					if(side>0){
						valueText.setText(getString(R.string.openValue));
					}else{
						valueText.setText(getString(R.string.closeValue));
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		};
	};
}
