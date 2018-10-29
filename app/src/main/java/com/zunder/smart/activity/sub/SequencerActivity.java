package com.zunder.smart.activity.sub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.popu.dialog.ActionViewWindow;
import com.zunder.smart.activity.sub.set.DeviceTimerActivity;
import com.zunder.smart.activity.sub.set.MoreActivity;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.RedInfraFactory;
import com.zunder.smart.dialog.EditTxtAlert;
import com.zunder.smart.dialog.EditTxtAliaAlert;
import com.zunder.smart.listener.DeviceStateListener;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.AirSwitchAlia;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.RedInfra;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.utils.ListNumBer;

public class SequencerActivity extends Activity implements OnClickListener,
        DeviceStateListener {
	private Activity activity;
	static CheckBox checkBox_1, checkBox_2, checkBox_3, checkBox_4, checkBox_5,
			checkBox_6, checkBox_7, checkBox_8, checkBox_all, checkBoxTiming;
	private static int Id = 0;
	static Device deviceParams;
	TextView titelView, backTxt, editeTxt;
	String deviceName = "";
	TextView txtView1, txtView2, txtView3, txtView4, txtView5, txtView6,
			txtView7, txtView8;
	ImageView setParam1,setParam2,setParam3,setParam4,setParam5,setParam6,setParam7,setParam8;
	AirSwitchAlia airSwitchAlia;
	private RightMenu rightButtonMenuAlert;
	public static void startActivity(Activity activity, int _id) {
		Id = _id;
		Intent intent = new Intent(activity, SequencerActivity.class);
		activity.startActivityForResult(intent,100);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_sequencer);
		activity = this;
		deviceParams = DeviceFactory.getInstance().getDevicesById(Id);
		titelView = (TextView) findViewById(R.id.titleTxt);
		backTxt = (TextView) findViewById(R.id.backTxt);
		editeTxt = (TextView) findViewById(R.id.editeTxt);
		TcpSender.setDeviceStateListener(this);
		editeTxt.setOnClickListener(this);
		checkBox_1 = (CheckBox) findViewById(R.id.checkBox_1);
		checkBox_2 = (CheckBox) findViewById(R.id.checkBox_2);
		checkBox_3 = (CheckBox) findViewById(R.id.checkBox_3);
		checkBox_4 = (CheckBox) findViewById(R.id.checkBox_4);
		checkBox_5 = (CheckBox) findViewById(R.id.checkBox_5);
		checkBox_6 = (CheckBox) findViewById(R.id.checkBox_6);
		checkBox_7 = (CheckBox) findViewById(R.id.checkBox_7);
		checkBox_8 = (CheckBox) findViewById(R.id.checkBox_8);
		checkBox_all = (CheckBox) findViewById(R.id.checkBox_all);
		checkBoxTiming = (CheckBox) findViewById(R.id.checkBoxTiming);
		txtView1 = (TextView) findViewById(R.id.txt1);
		txtView2 = (TextView) findViewById(R.id.txt2);
		txtView3 = (TextView) findViewById(R.id.txt3);
		txtView4 = (TextView) findViewById(R.id.txt4);
		txtView5 = (TextView) findViewById(R.id.txt5);
		txtView6 = (TextView) findViewById(R.id.txt6);
		txtView7 = (TextView) findViewById(R.id.txt7);
		txtView8 = (TextView) findViewById(R.id.txt8);

		setParam1=(ImageView)findViewById(R.id.setParam1);
		setParam2=(ImageView)findViewById(R.id.setParam2);
		setParam3=(ImageView)findViewById(R.id.setParam3);
		setParam4=(ImageView)findViewById(R.id.setParam4);
		setParam5=(ImageView)findViewById(R.id.setParam5);
		setParam6=(ImageView)findViewById(R.id.setParam6);
		setParam7=(ImageView)findViewById(R.id.setParam7);
		setParam8=(ImageView)findViewById(R.id.setParam8);

		checkBox_1.setOnClickListener(this);
		checkBox_2.setOnClickListener(this);
		checkBox_3.setOnClickListener(this);
		checkBox_4.setOnClickListener(this);
		checkBox_5.setOnClickListener(this);
		checkBox_6.setOnClickListener(this);
		checkBox_7.setOnClickListener(this);
		checkBox_8.setOnClickListener(this);
		checkBox_all.setOnClickListener(this);
		backTxt.setOnClickListener(this);
		checkBoxTiming.setOnClickListener(this);

		setParam1.setOnClickListener(this);
		setParam2.setOnClickListener(this);
		setParam3.setOnClickListener(this);
		setParam4.setOnClickListener(this);
		setParam5.setOnClickListener(this);
		setParam6.setOnClickListener(this);
		setParam7.setOnClickListener(this);
		setParam8.setOnClickListener(this);
		deviceName = deviceParams.getDeviceName();
		titelView.setText(deviceName);
		String[] names =deviceParams.getData1().split(",");
		if (names!=null&&names.length == 8) {
			txtView1.setText(names[0]);
			txtView2.setText(names[1]);
			txtView3.setText(names[2]);
			txtView4.setText(names[3]);
			txtView5.setText(names[4]);
			txtView6.setText(names[5]);
			txtView7.setText(names[6]);
			txtView8.setText(names[7]);
			airSwitchAlia=new AirSwitchAlia();
			airSwitchAlia.setName1(names[0]);
			airSwitchAlia.setName2(names[1]);
			airSwitchAlia.setName3(names[2]);
			airSwitchAlia.setName4(names[3]);
			airSwitchAlia.setName5(names[4]);
			airSwitchAlia.setName6(names[5]);
			airSwitchAlia.setName7(names[6]);
			airSwitchAlia.setName8(names[7]);
		} else {
			airSwitchAlia=new AirSwitchAlia();
			airSwitchAlia.setName1("时序1路");
			airSwitchAlia.setName2("时序2路");
			airSwitchAlia.setName3("时序3路");
			airSwitchAlia.setName4("时序4路");
			airSwitchAlia.setName5("时序5路");
			airSwitchAlia.setName6("时序6路");
			airSwitchAlia.setName7("时序7路");
			airSwitchAlia.setName8("时序8路");
			deviceParams.setData1("时序1路,时序2路,时序3路,时序4路,时序5路,时序6路,时序7路,时序8路");
			MyApplication.getInstance().getWidgetDataBase()
					.updateDevice(deviceParams);
		}
		initRightButtonMenuAlert();
	}
	private void initRightButtonMenuAlert() {

		rightButtonMenuAlert = new RightMenu(activity, R.array.right_sque,
				R.drawable.right_airswitch_images);
		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {

			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub
				switch (position) {
					case 0:
						setVisible(View.VISIBLE);
						break;
					case 1:
						DeviceTimerActivity.startActivity(activity,deviceParams.getId());
						break;
					case 2:
						MoreActivity.startActivity(activity,deviceParams.getId());
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

	public void back(){
		Intent resultIntent = new Intent();
		setResult(0, resultIntent);
		finish();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			back();
			break;
		case R.id.editeTxt:
			setVisible(View.GONE);
			rightButtonMenuAlert.show(editeTxt);
			break;
		case R.id.checkBox_1:
			if (checkBox_1.isChecked()) {
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceName + getString(R.string.open_1) + getString(R.string.sequ_1),
						deviceParams);
			} else {
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceName + getString(R.string.close_1) + getString(R.string.sequ_1),
						deviceParams);
			}
			break;
		case R.id.checkBox_2:
			if (checkBox_2.isChecked()) {
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceName + getString(R.string.open_1) + getString(R.string.sequ_2),
						deviceParams);
			} else {
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceName + getString(R.string.close_1) + getString(R.string.sequ_2),
						deviceParams);
			}
			break;
		case R.id.checkBox_3:
			if (checkBox_3.isChecked()) {
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceName + getString(R.string.open_1) + getString(R.string.sequ_3),
						deviceParams);
			} else {
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceName + getString(R.string.close_1) +getString(R.string.sequ_3),
						deviceParams);
			}
			break;
		case R.id.checkBox_4:
			if (checkBox_4.isChecked()) {
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceName + getString(R.string.open_1) + getString(R.string.sequ_4),
						deviceParams);
			} else {
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceName + getString(R.string.close_1) +getString(R.string.sequ_4),
						deviceParams);
			}
			break;
		case R.id.checkBox_5:
			if (checkBox_5.isChecked()) {
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceName + getString(R.string.open_1) +getString(R.string.sequ_5),
						deviceParams);
			} else {
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceName + getString(R.string.close_1) + getString(R.string.sequ_5),
						deviceParams);
			}
			break;
		case R.id.checkBox_6:
			if (checkBox_6.isChecked()) {
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceName + getString(R.string.open_1) + getString(R.string.sequ_6),
						deviceParams);
			} else {
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceName + getString(R.string.close_1) + getString(R.string.sequ_6),
						deviceParams);
			}
			break;
		case R.id.checkBox_7:
			if (checkBox_7.isChecked()) {
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceName + getString(R.string.open_1) + getString(R.string.sequ_7),
						deviceParams);
			} else {
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceName + getString(R.string.close_1) + getString(R.string.sequ_7),
						deviceParams);
			}
			break;
		case R.id.checkBox_8:
			if (checkBox_8.isChecked()) {
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceName + getString(R.string.open_1) + getString(R.string.sequ_8),
						deviceParams);
			} else {
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceName + getString(R.string.close_1) +getString(R.string.sequ_8),
						deviceParams);
			}
			break;
		case R.id.checkBox_all:
			if (checkBox_all.isChecked()) {
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceName + getString(R.string.open_1), deviceParams);
			} else {
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceName + getString(R.string.close_1), deviceParams);
			}
			break;
		case R.id.checkBoxTiming:
			DeviceTimerActivity.startActivity(activity, Id);
			break;
			case R.id.setParam1:
				ShowAliaDialog(1);
				break;
			case R.id.setParam2:
				ShowAliaDialog(2);
				break;
			case R.id.setParam3:
				ShowAliaDialog(3);
				break;
			case R.id.setParam4:
				ShowAliaDialog(4);
				break;
			case R.id.setParam5:
					ShowAliaDialog(5);
				break;
			case R.id.setParam6:
				ShowAliaDialog(6);
				break;
			case R.id.setParam7:
				ShowAliaDialog(7);
				break;
			case R.id.setParam8:
				ShowAliaDialog(8);
				break;
		default:
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
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				int value = deviceParams.getDeviceAnalogVar2();
				int state = deviceParams.getState();
				if ((value & (1 << 0)) > 0) {
					checkBox_1.setChecked(true);
				} else {
					checkBox_1.setChecked(false);
				}

				if ((value & (1 << 1)) > 0) {
					checkBox_2.setChecked(true);
				} else {
					checkBox_2.setChecked(false);
				}

				if ((value & (1 << 2)) > 0) {
					checkBox_3.setChecked(true);
				} else {
					checkBox_3.setChecked(false);
				}
				if ((value & (1 << 3)) > 0) {
					checkBox_4.setChecked(true);
				} else {
					checkBox_4.setChecked(false);
				}
				if ((value & (1 << 4)) > 0) {
					checkBox_5.setChecked(true);
				} else {
					checkBox_5.setChecked(false);
				}
				if ((value & (1 << 5)) > 0) {
					checkBox_6.setChecked(true);
				} else {
					checkBox_6.setChecked(false);
				}
				if ((value & (1 << 6)) > 0) {
					checkBox_7.setChecked(true);
				} else {
					checkBox_7.setChecked(false);
				}
				if ((value & (1 << 7)) > 0) {
					checkBox_8.setChecked(true);
				} else {
					checkBox_8.setChecked(false);
				}
				if (state > 0) {
					checkBox_all.setChecked(true);
				} else {
					checkBox_all.setChecked(false);
				}

				break;

			default:
				break;
			}
		}
	};
	public void setVisible(int visible){
		setParam1.setVisibility(visible);
		setParam2.setVisibility(visible);
		setParam3.setVisibility(visible);
		setParam4.setVisibility(visible);
		setParam5.setVisibility(visible);
		setParam6.setVisibility(visible);
		setParam7.setVisibility(visible);
		setParam8.setVisibility(visible);
	}

	@Override
	public void receiveDeviceStatus(String cmd) {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(1);

	}

	public void ShowAliaDialog(final int index){
		final ActionViewWindow actionViewWindow=new ActionViewWindow(activity,"别名设置", ListNumBer.getAirSwitchs(),0);
		actionViewWindow.setAlertViewOnCListener(new ActionViewWindow.AlertViewOnCListener() {
			@Override
			public void onItem(int pos, String ItemName) {
				if(ItemName.equals("自定义")){
					final EditTxtAlert alert = new EditTxtAlert(activity);
					alert.setTitle(android.R.drawable.ic_dialog_info, getString(R.string.ailasetting1),0);
					alert.setHint(getString(R.string.input_alia));
					alert.setOnSureListener(new EditTxtAlert.OnSureListener() {

						@Override
						public void onSure(String fileName) {
							// TODO Auto-generated method stub
							if (fileName.equals("")&&fileName.length()<5) {
								ToastUtils.ShowError(activity,"名称不能为空",Toast.LENGTH_SHORT,true);
								return;
							}
							if(index==1){
								airSwitchAlia.setName1(fileName);
								txtView1.setText(fileName);
							}else if(index==2){
								airSwitchAlia.setName2(fileName);
								txtView2.setText(fileName);
							}else if(index==3){
								airSwitchAlia.setName3(fileName);
								txtView3.setText(fileName);
							}else if(index==4){
								airSwitchAlia.setName4(fileName);
								txtView4.setText(fileName);
							}else if(index==5){
								airSwitchAlia.setName5(fileName);
								txtView5.setText(fileName);
							}else if(index==6){
								airSwitchAlia.setName6(fileName);
								txtView6.setText(fileName);
							}else if(index==7){
								airSwitchAlia.setName7(fileName);
								txtView7.setText(fileName);
							}else if(index==8){
								airSwitchAlia.setName8(fileName);
								txtView8.setText(fileName);
							}
							deviceParams.setData1(airSwitchAlia.getString());
							MyApplication.getInstance().getWidgetDataBase()
									.updateDevice(deviceParams);
							alert.dismiss();
						}

						@Override
						public void onCancle() {
							// TODO Auto-generated method stub
						}
					});
					alert.show();
				}else{
					if(index==1){
						airSwitchAlia.setName1(ItemName);
						txtView1.setText(ItemName);
					}else if(index==2){
						airSwitchAlia.setName2(ItemName);
						txtView2.setText(ItemName);
					}else if(index==3){
						airSwitchAlia.setName3(ItemName);
						txtView3.setText(ItemName);
					}else if(index==4){
						airSwitchAlia.setName4(ItemName);
						txtView4.setText(ItemName);
					}else if(index==5){
						airSwitchAlia.setName5(ItemName);
						txtView5.setText(ItemName);
					}else if(index==6){
						airSwitchAlia.setName6(ItemName);
						txtView6.setText(ItemName);
					}else if(index==7){
						airSwitchAlia.setName7(ItemName);
						txtView7.setText(ItemName);
					}else if(index==8){
						airSwitchAlia.setName8(ItemName);
						txtView8.setText(ItemName);
					}
					deviceParams.setData1(airSwitchAlia.getString());
					MyApplication.getInstance().getWidgetDataBase()
							.updateDevice(deviceParams);
				}
				actionViewWindow.dismiss();
			}

			@Override
			public void cancle() {

			}
		});
		actionViewWindow.show();
	}
}
