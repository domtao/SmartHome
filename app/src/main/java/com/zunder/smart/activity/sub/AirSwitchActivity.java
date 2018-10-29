package com.zunder.smart.activity.sub;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.activity.popu.dialog.ActionViewWindow;
import com.zunder.smart.activity.sub.set.DeviceTimerActivity;
import com.zunder.smart.activity.sub.set.MoreActivity;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.RedInfraFactory;
import com.zunder.smart.dialog.EditTxtAlert;
import com.zunder.smart.dialog.EditTxtAliaAlert;
import com.zunder.smart.dialog.TipAlert;
import com.zunder.smart.dialog.WarnDialog;
import com.zunder.smart.listener.AirSwitchListener;
import com.zunder.smart.listener.AlertViewListener;
import com.zunder.smart.listener.DeviceStateListener;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.AirSwitchAlia;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.MasterRak;
import com.zunder.smart.model.RedInfra;
import com.zunder.smart.popwindow.AirSwitchViewWindow;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.utils.ListNumBer;

public class AirSwitchActivity extends Activity implements OnClickListener,
		DeviceStateListener,AlertViewListener,AirSwitchListener{
	private int index=0;
	private String title="";
	private Activity activity;
	static  Button checkBox_1, checkBox_2, checkBox_3, checkBox_4, checkBox_5,
			checkBox_6, checkBox_7, checkBox_8;
	static CheckBox checkBox_all, checkBoxTiming;
	ImageView setParam1,setParam2,setParam3,setParam4,setParam5,setParam6,setParam7,setParam8;
	private static int Id = 0;
	static Device deviceParams;
	TextView titelView, backTxt, editeTxt;
	String deviceName = "";
	private int styleIndex=0;
	boolean refreshSetPara=false;
	TextView txtView1, txtView2, txtView3, txtView4, txtView5, txtView6,
			txtView7, txtView8;
    String[] hnits=new String[]{"数据发送电压上门限,单位1V","数据发送电压下门限,单位1V","16位漏电电流上限,单位0.1mA","数据发送功率门限,单位1W"
    ,"数据发送温度上限,单位0.1度","数据发送电流门限,单位0.01A","修改电量大小"};
	TextView quantity1,quantity2,quantity3,quantity4,quantity5,quantity6,quantity7,quantity8;
	TextView voltage1,voltage2,voltage3,voltage4,voltage5,voltage6,voltage7,voltage8;
	TextView power1,power2,power3,power4,power5,power6,power7,power8;
	TextView electric1,electric2,electric3,electric4,electric5,electric6,electric7,electric8;
	TextView temp1,temp2,temp3,temp4,temp5,temp6,temp7,temp8;
	AirSwitchAlia airSwitchAlia;
	public static void startActivity(Activity activity, int _id) {
		Id = _id;
		Intent intent = new Intent(activity, AirSwitchActivity.class);
	activity.startActivityForResult(intent,100);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_airswitch);
		activity = this;
		deviceParams = DeviceFactory.getInstance().getDevicesById(Id);
		titelView = (TextView) findViewById(R.id.titleTxt);
		backTxt = (TextView) findViewById(R.id.backTxt);
		editeTxt = (TextView) findViewById(R.id.editeTxt);

		editeTxt.setOnClickListener(this);
		checkBox_1 = (Button) findViewById(R.id.checkBox_1);
		checkBox_2 = (Button) findViewById(R.id.checkBox_2);
		checkBox_3 = (Button) findViewById(R.id.checkBox_3);
		checkBox_4 = (Button) findViewById(R.id.checkBox_4);
		checkBox_5 = (Button) findViewById(R.id.checkBox_5);
		checkBox_6 = (Button) findViewById(R.id.checkBox_6);
		checkBox_7 = (Button) findViewById(R.id.checkBox_7);
		checkBox_8 = (Button) findViewById(R.id.checkBox_8);
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


		quantity1= (TextView) findViewById(R.id.quantity1);
		quantity2= (TextView) findViewById(R.id.quantity2);
		quantity3= (TextView) findViewById(R.id.quantity3);
		quantity4= (TextView) findViewById(R.id.quantity4);
		quantity5= (TextView) findViewById(R.id.quantity5);
		quantity6= (TextView) findViewById(R.id.quantity6);
		quantity7= (TextView) findViewById(R.id.quantity7);
		quantity8= (TextView) findViewById(R.id.quantity8);
		voltage1= (TextView) findViewById(R.id.voltage1);
		voltage2= (TextView) findViewById(R.id.voltage2);
		voltage3= (TextView) findViewById(R.id.voltage3);
		voltage4= (TextView) findViewById(R.id.voltage4);
		voltage5= (TextView) findViewById(R.id.voltage5);
		voltage6= (TextView) findViewById(R.id.voltage6);
		voltage7= (TextView) findViewById(R.id.voltage7);
		voltage8= (TextView) findViewById(R.id.voltage8);
		power1= (TextView) findViewById(R.id.power1);
		power2= (TextView) findViewById(R.id.power2);
		power3= (TextView) findViewById(R.id.power3);
		power4= (TextView) findViewById(R.id.power4);
		power5= (TextView) findViewById(R.id.power5);
		power6= (TextView) findViewById(R.id.power6);
		power7= (TextView) findViewById(R.id.power7);
		power8= (TextView) findViewById(R.id.power8);

		electric1= (TextView) findViewById(R.id.electric1);
		electric2= (TextView) findViewById(R.id.electric2);
		electric3= (TextView) findViewById(R.id.electric3);
		electric4= (TextView) findViewById(R.id.electric4);
		electric5= (TextView) findViewById(R.id.electric5);
		electric6= (TextView) findViewById(R.id.electric6);
		electric7= (TextView) findViewById(R.id.electric7);
		electric8= (TextView) findViewById(R.id.electric8);

		temp1= (TextView) findViewById(R.id.temp1);
		temp2= (TextView) findViewById(R.id.temp2);
		temp3= (TextView) findViewById(R.id.temp3);
		temp4= (TextView) findViewById(R.id.temp4);
		temp5= (TextView) findViewById(R.id.temp5);
		temp6= (TextView) findViewById(R.id.temp6);
		temp7= (TextView) findViewById(R.id.temp7);
		temp8= (TextView) findViewById(R.id.temp8);

		setParam1.setOnClickListener(this);
		setParam2.setOnClickListener(this);
		setParam3.setOnClickListener(this);
		setParam4.setOnClickListener(this);
		setParam5.setOnClickListener(this);
		setParam6.setOnClickListener(this);
		setParam7.setOnClickListener(this);
		setParam8.setOnClickListener(this);

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
				airSwitchAlia.setName1("断路器1路");
				airSwitchAlia.setName2("断路器2路");
				airSwitchAlia.setName3("断路器3路");
				airSwitchAlia.setName4("断路器4路");
				airSwitchAlia.setName5("断路器5路");
				airSwitchAlia.setName6("断路器6路");
				airSwitchAlia.setName7("断路器7路");
				airSwitchAlia.setName8("断路器8路");
				deviceParams.setData1("断路器1路,断路器2路,断路器3路,断路器4路,断路器5路,断路器6路,断路器7路,断路器8路");
			MyApplication.getInstance().getWidgetDataBase()
					.updateDevice(deviceParams);
		}
		initRightButtonMenuAlert();
		handler.sendEmptyMessage(1);

		styleIndex=0;
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		TcpSender.setDeviceStateListener(this);
		TcpSender.setAirSwitchListener(this);
		String cmd ="*C0009FA06" + deviceParams.getDeviceID() + "00B40000";
		TcpSender.sendMssageAF(cmd);
	}
	private RightMenu rightButtonMenuAlert;

	private void initRightButtonMenuAlert() {

		rightButtonMenuAlert = new RightMenu(activity, R.array.right_airswitch,
				R.drawable.right_airswitch_images);
		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {

			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub
				switch (position) {
					case 0:
						styleIndex=1;
						setVisible(View.VISIBLE);
						break;
					case 1:
						styleIndex=0;
						setVisible(View.VISIBLE);
						break;
					case 2:
						DeviceTimerActivity.startActivity(activity,deviceParams.getId());
						break;
					case 3:
						MoreActivity.startActivity(activity,deviceParams.getId());
						break;
				}
				rightButtonMenuAlert.dismiss();
			}
		});
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
			showControlWindow(txtView1.getText().toString(),1,(deviceParams.getDeviceAnalogVar2() & (1 << 0)) >  0?1:0);
			break;
		case R.id.checkBox_2:
			showControlWindow(txtView2.getText().toString(),2,(deviceParams.getDeviceAnalogVar2() & (1 << 1)) >  0?1:0);
			break;
		case R.id.checkBox_3:
			showControlWindow(txtView3.getText().toString(),3,(deviceParams.getDeviceAnalogVar2() & (1 << 2)) >  0?1:0);
			break;
		case R.id.checkBox_4:
			showControlWindow(txtView4.getText().toString(),4,(deviceParams.getDeviceAnalogVar2() & (1 << 3)) >  0?1:0);
			break;
		case R.id.checkBox_5:
			showControlWindow(txtView5.getText().toString(),5,(deviceParams.getDeviceAnalogVar2() & (1 << 4)) >  0?1:0);
			break;
		case R.id.checkBox_6:
			showControlWindow(txtView6.getText().toString(),6,(deviceParams.getDeviceAnalogVar2() & (1 << 5)) >  0?1:0);
			break;
		case R.id.checkBox_7:
			showControlWindow(txtView7.getText().toString(),7,(deviceParams.getDeviceAnalogVar2() & (1 << 6)) > 0?1:0);
			break;
		case R.id.checkBox_8:
			showControlWindow(txtView8.getText().toString(),8,(deviceParams.getDeviceAnalogVar2() & (1 << 7)) > 0?1:0);
			break;
		case R.id.checkBox_all:
			if(deviceParams.getOpenStart()>0){
				checkBox_all.setChecked(false);
			}else{
				checkBox_all.setChecked(true);
			}
			showControlWindow("总开",9,deviceParams.getState()>0?1:0);
			break;
		case R.id.checkBoxTiming:
			DeviceTimerActivity.startActivity(activity, Id);
			break;
			case R.id.setParam1:
				if(styleIndex==0) {
					showParamWindow(txtView1.getText().toString(), 1, "81");
				}else{
					ShowAliaDialog(1);
				}
				break;
			case R.id.setParam2:
				if(styleIndex==0) {
					showParamWindow(txtView2.getText().toString(),2,"82");
				}else{
					ShowAliaDialog(2);
				}
				break;
			case R.id.setParam3:
				if(styleIndex==0) {
					showParamWindow(txtView3.getText().toString(),3,"83");
				}else{
					ShowAliaDialog(3);
				}
				break;
			case R.id.setParam4:
				if(styleIndex==0) {
					showParamWindow(txtView4.getText().toString(),4,"84");
				}else{
					ShowAliaDialog(4);
				}
				break;
			case R.id.setParam5:
				if(styleIndex==0) {
					showParamWindow(txtView5.getText().toString(),5,"85");
				}else{
					ShowAliaDialog(5);
				}
				break;
			case R.id.setParam6:
				if(styleIndex==0) {
					showParamWindow(txtView6.getText().toString(),6,"86");
				}else{
					ShowAliaDialog(6);
				}
				break;
			case R.id.setParam7:
				if(styleIndex==0) {
					showParamWindow(txtView7.getText().toString(),7,"87");
				}else{
					ShowAliaDialog(7);
				}
				break;
			case R.id.setParam8:
				if(styleIndex==0) {
					showParamWindow(txtView8.getText().toString(),8,"88");
				}else{
					ShowAliaDialog(8);
				}
				break;
		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TcpSender.setAirSwitchListener(null);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private  Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case -1:
					TipAlert errAlert = new TipAlert(activity,
							getString(R.string.tip),"获取数据超时");
					errAlert.show();
					break;
				case 0:
					if ((warnDialog != null) && warnDialog.isShowing()) {
						searchflag = false;
						startCount=0;
						warnDialog.dismiss();
					}
					break;
			case 1:
				int value = deviceParams.getDeviceAnalogVar2();
				int disable = deviceParams.getDeviceAnalogVar3();
				int state = deviceParams.getState();
				if((disable& (1 << 0)) > 0){
					checkBox_1.setBackgroundResource(R.mipmap.empty_dis);
				}else{
					if ((value & (1 << 0)) > 0) {
						checkBox_1.setBackgroundResource(R.mipmap.empty_on);
					} else {
						checkBox_1.setBackgroundResource(R.mipmap.empty_off);
					}
				}
				if((disable& (1 << 1)) > 0){
					checkBox_2.setBackgroundResource(R.mipmap.empty_dis);
				}else {
					if ((value & (1 << 1)) > 0) {
						checkBox_2.setBackgroundResource(R.mipmap.empty_on);
					} else {
						checkBox_2.setBackgroundResource(R.mipmap.empty_off);
					}
				}
				if((disable& (1 << 2)) > 0){
					checkBox_3.setBackgroundResource(R.mipmap.empty_dis);
				}else {
					if ((value & (1 << 2)) > 0) {
						checkBox_3.setBackgroundResource(R.mipmap.empty_on);
					} else {
						checkBox_3.setBackgroundResource(R.mipmap.empty_off);
					}
				}
				if((disable& (1 << 3)) > 0){
					checkBox_4.setBackgroundResource(R.mipmap.empty_dis);
				}else {
					if ((value & (1 << 3)) > 0) {
						checkBox_4.setBackgroundResource(R.mipmap.empty_on);
					} else {
						checkBox_4.setBackgroundResource(R.mipmap.empty_off);
					}
				}
				if((disable& (1 << 4)) > 0){
					checkBox_5.setBackgroundResource(R.mipmap.empty_dis);
				}else {
					if ((value & (1 << 4)) > 0) {
						checkBox_5.setBackgroundResource(R.mipmap.empty_on);
					} else {
						checkBox_5.setBackgroundResource(R.mipmap.empty_off);
					}
				}
				if((disable& (1 << 5)) > 0){
					checkBox_6.setBackgroundResource(R.mipmap.empty_dis);
				}else {
					if ((value & (1 << 5)) > 0) {
						checkBox_6.setBackgroundResource(R.mipmap.empty_on);
					} else {
						checkBox_6.setBackgroundResource(R.mipmap.empty_off);
					}
				}
				if((disable& (1 << 6)) > 0){
					checkBox_7.setBackgroundResource(R.mipmap.empty_dis);
				}else {
					if ((value & (1 << 6)) > 0) {
						checkBox_7.setBackgroundResource(R.mipmap.empty_on);
					} else {
						checkBox_7.setBackgroundResource(R.mipmap.empty_off);
					}
				}
				if((disable& (1 << 7)) > 0){
					checkBox_8.setBackgroundResource(R.mipmap.empty_dis);
				}else {
					if ((value & (1 << 7)) > 0) {
						checkBox_8.setBackgroundResource(R.mipmap.empty_on);
					} else {
						checkBox_8.setBackgroundResource(R.mipmap.empty_off);
					}
				}
				if (state > 0) {
					checkBox_all.setChecked(false);
				} else {
					checkBox_all.setChecked(true);
				}
				break;
			default:
				break;
			}
		}
	};

	private void showParamWindow(final String _title , final int _index, final String cmd) {
		showDialog();
		this.title=_title;
		this.index=_index;
		TcpSender.setAlertViewListener(this);
		String cmdStr = "*C0009FA06" + deviceParams.getDeviceID() + "0E"+AppTools.toHex(index-1) +"0000" ;
		TcpSender.sendMssageAF(cmdStr);
		refreshSetPara=true;
	}
	 AirSwitchViewWindow alertViewWindow;
	@Override
	public void alertViewParam(String value1, String value2, String value3, String value4, String value5, String value6) {
		if(refreshSetPara) {
			refreshSetPara = false;
			handler.sendEmptyMessage(0);
			alertViewWindow = new AirSwitchViewWindow(activity, title, ListNumBer.getAirSwitch(value1, value2, value3, value4, value5, value6), null, 0);
			alertViewWindow.setAlertViewOnCListener(new AirSwitchViewWindow.AlertViewOnCListener() {
				@Override
				public void onItem(int pos, String itemName) {
					TcpSender.setAlertViewListener(null);
					setSetParam(itemName, hnits[pos], index, (81 + pos) + "");
				}

				@Override
				public void cancle() {
					setVisible(View.GONE);
				}
			});
			alertViewWindow.show();
		}
	}
	private void showControlWindow(final String _title , final int index,int selection) {
		final	AlertViewWindow alertViewWindow=new AlertViewWindow(activity,_title,ListNumBer.getAction(),null,selection);
		alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
			@Override
			public void onItem(int pos, String itemName) {
						switch (pos){
					case 0:
						//打开
						if(index==9){
							SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
									deviceName + getString(R.string.open_1), deviceParams);
						}else {
							SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
									deviceName + getString(R.string.open_1) + getString(R.string.air_on) + index + getString(R.string.road),
									deviceParams);
						}
						break;
					case 1:
						//关闭
						if(index==9){
							SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
									deviceName + getString(R.string.close_1), deviceParams);
						}else {
							SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
									deviceName + getString(R.string.close_1) + getString(R.string.air_on) + index + getString(R.string.road),
									deviceParams);
						}
						break;
				}
				alertViewWindow.dismiss();
			}
		});
		alertViewWindow.show();
	}

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
	public void setSetParam(String title, String hitStr, final int index, final String cmd){
		final EditTxtAlert alert = new EditTxtAlert(activity);
		alert.setTitle(android.R.drawable.ic_dialog_info,title,0);
		alert.setHint(hitStr);
		if(cmd.equals("83")||cmd.equals("85")||cmd.equals("86")) {
			alert.setEditTextType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		}else{
			alert.setEditTextType(InputType.TYPE_CLASS_NUMBER);
		}
		alert.setOnSureListener(new EditTxtAlert.OnSureListener() {
			@Override
			public void onSure(String fileName) {
				// TODO Auto-generated method stub
				if (!"".equals(fileName)) {
					String result=fileName;
					if(cmd.equals("83")||cmd.equals("85")) {
						String[] tempS = fileName.split("\\.");
						if(tempS.length!=2) {
							ToastUtils.ShowError(activity,"请输入正确格式的值",Toast.LENGTH_SHORT,true);
							return;
						}
						char[] tempC = tempS[1].toCharArray();
						int resultNum = tempC.length;
						if(resultNum!=1){
							ToastUtils.ShowError(activity,"请输入正确格式的值",Toast.LENGTH_SHORT,true);
							return;
						}
						result=""+(int)Float.parseFloat(fileName)*10;
					}else if(cmd.equals("86")) {
						String[] tempS = fileName.split("\\.");
						if(tempS.length!=2) {
							ToastUtils.ShowError(activity,"请输入正确格式的值",Toast.LENGTH_SHORT,true);
							return;
						}
						char[] tempC = tempS[1].toCharArray();
						int resultNum = tempC.length;
						if(resultNum!=2){
							ToastUtils.ShowError(activity,"请输入正确格式的值",Toast.LENGTH_SHORT,true);
							return;
						}
						result=""+	(int)Float.parseFloat(fileName)*100;
					}
					SendCMD.getInstance().sendCMD(238, cmd+":"+AppTools.toHex(index)+result,
							deviceParams);
					alert.dismiss();
					if(alertViewWindow!=null){
						alertViewWindow.dismiss();
						setVisible(View.GONE);
					}
				} else {
					Toast.makeText(activity,
							"内容不能为空",
							Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onCancle() {
				// TODO Auto-generated method stub
			}
		});
		alert.show();

	}

	@Override
	public void setCmd(final int index, final String value1, final String value2,final String value3,final String value4, final String value5) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				if(index==1) {
					//温度
					voltage1.setText(value1);
					power1.setText(value2);
					temp1.setText(value3);
					electric1.setText(value4);
					quantity1.setText(value5);
				}else 	if(index==2) {
					voltage2.setText(value1);
					power2.setText(value2);
					temp2.setText(value3);
					electric2.setText(value4);
					quantity2.setText(value5);

				}else 	if(index==3) {
					voltage3.setText(value1);
					power3.setText(value2);
					temp3.setText(value3);
					electric3.setText(value4);
					quantity3.setText(value5);
				}
				else 	if(index==4) {
					voltage4.setText(value1);
					power4.setText(value2);
					temp4.setText(value3);
					electric4.setText(value4);
					quantity4.setText(value5);
				}
				else 	if(index==5) {
					voltage5.setText(value1);
					power5.setText(value2);
					temp5.setText(value3);
					electric5.setText(value4);
					quantity5.setText(value5);
				}
				else 	if(index==6) {
					voltage6.setText(value1);
					power6.setText(value2);
					temp6.setText(value3);
					electric6.setText(value4);
					quantity6.setText(value5);
				}else 	if(index==7) {
					voltage7.setText(value1);
					power7.setText(value2);
					temp7.setText(value3);
					electric7.setText(value4);
					quantity7.setText(value5);
				}else 	if(index==8) {
					voltage8.setText(value1);
					power8.setText(value2);
					temp8.setText(value3);
					electric8.setText(value4);
					quantity8.setText(value5);
				}



			}
		});
	}

	WarnDialog warnDialog = null;
	public void showDialog(){
		if (warnDialog == null) {
			warnDialog = new WarnDialog(activity,getString(R.string.searching));
			warnDialog.setMessage("正在获取数据");
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
						if (startCount >= 3) {
							searchflag = false;
							if ((warnDialog != null) && warnDialog.isShowing()) {
								warnDialog.dismiss();
								handler.sendEmptyMessage(-1);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void ShowAliaDialog(final int index){
		final ActionViewWindow actionViewWindow=new ActionViewWindow(activity,"别名设置",ListNumBer.getAirSwitchs(),0);
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
