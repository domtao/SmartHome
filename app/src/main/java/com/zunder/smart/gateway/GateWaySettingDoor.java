/**
 * 
 */
package com.zunder.smart.gateway;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dialog.ButtonAlert;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.EditTxtAlert;
import com.zunder.smart.dialog.EditTxtTwoAlert;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.utils.ListNumBer;
import com.p2p.core.P2PHandler;

import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Administrator
 * 
 */
public class GateWaySettingDoor extends AppCompatActivity {
	String contactId;
	String password;

	public static String P2P_ALARM = "com.door.P2P_ALARM";
	public static String P2P_PWD = "com.door.P2P_PWD";
	public static String P2P_SET_PWD = "com.door.P2P_SET_PWD";
	public static String P2P_VECTOR_PWD = "com.door.P2P_VECTOR_PWD";
	public static String P2P_SET_VECTOR_PWD = "com.door.P2P_SET_VECTOR_PWD";

	public static String P2P_SET_RF = "com.door.P2P_SET_RF";
	public static String P2P_RF = "com.door.P2P_RF";
	public static String P2P_MANGER = "com.door.P2P_MAGER";
	public static String P2P_MOVE = "com.door.P2P_MOVE";
	public static String P2P_SET_MOVE = "com.door.P2P_SET_MOVE";
	private FrameLayout setting_alarm,setting_rf, setting_mail, setting_user,
			setting_move, setting_smart_alarm, setting_ftp, setting_sdcard,
			setting_ap, setting_ddns, setting_time,
			 setting_timing, setting_code, setting_sence,
			setting_answer_radio, setting_timing_radio, setting_operator,
			setting_anHong, setting_point;
	String userId="";
	String[] data=null;
	int maxCount=0;
	private TextView back_item, title_item, fun_item;

	private Activity activity;

	TextView machineTxt, moveTxt, userTxt, timingTxt, modetxt,
			alarmTxt, operatorTxt, anHongTxt, pointTxt,rfTxt;

	static GateWay gateWay;
	public static void startActivity(Activity activity, GateWay _gateWay) {
		Intent intent = new Intent(activity, GateWaySettingDoor.class);
		gateWay= _gateWay;
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.test_screen);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gateway_door);
		activity = this;
		contactId=gateWay.getGatewayID();
		 password = P2PHandler.getInstance().EntryPassword(gateWay.getUserPassWord());
		SharedPreferences sp=getSharedPreferences("door_info",MODE_PRIVATE);
		userId = sp.getString("userId",null);
		regFilter();
		initView();
		listenerClick();
		userTxt.setText(getString(R.string.usersetting1)+"\n"+gateWay.getUserPassWord());
		P2PHandler.getInstance().getNpcSettings(contactId,password);
		P2PHandler.getInstance().getBindAlarmId(contactId,password);
	}

	private void listenerClick() {
		// ��������

		// ����WIFI

		setting_alarm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final ButtonAlert buttonAlert = new ButtonAlert(activity);
				buttonAlert.setTitle(R.mipmap.info_systemset,getString(R.string.tip), getString(R.string.alrm_push1));
				buttonAlert.setButton(getString(R.string.open_2), getString(R.string.close_1), getString(R.string.cancle));
				buttonAlert
						.setVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE);
				buttonAlert.setOnSureListener(new ButtonAlert.OnSureListener() {
					@Override
					public void onSure() {
						// TODO Auto-generated method stub
					//	P2PHandler.getInstance().getAlarmCenterParameters();

						if(isSwitch()==1){
							ToastUtils.ShowSuccess(activity,"设备已开启报警推送",Toast.LENGTH_SHORT,true);
							return;
						}
						if(data!=null) {

							List<String> list=new ArrayList<String>();
							for (int i = 0; i < data.length; i++) {
								if(AppTools.isNumeric(data[i])) {
									list.add(data[i]);
								}
							}
							String[] tempData = new String[list.size() + 1];
							for (int i = 0; i < list.size(); i++) {
								tempData[i] =list.get(i);
							}
							tempData[list.size()] = userId;
							P2PHandler.getInstance().setBindAlarmId(contactId, password, tempData.length, tempData);
							P2PHandler.getInstance().getBindAlarmId(contactId, password);
						}else{
							maxCount=5;
							data=new String[]{userId};
							P2PHandler.getInstance().setBindAlarmId(contactId,password,data.length,data);
							P2PHandler.getInstance().getBindAlarmId(contactId,password);
						}
					//	alarmTxt.setText("报警推送\n开启");
						buttonAlert.dismiss();
					}

					@Override
					public void onSearch() {
						// TODO Auto-generated method stub
//						P2PHandler.getInstance().del
						if(isSwitch()==0){
							ToastUtils.ShowSuccess(activity,getString(R.string.closepush),Toast.LENGTH_SHORT,true);
							return;
						}
						if(data!=null){
							List<String> list=new ArrayList<String>();
							for (int i=0;i<data.length;i++){
								if(!data[i].equals(userId)&&AppTools.isNumeric(data[i])) {
									list.add(data[i]);
								}
							}
							String[] tempData=new String[list.size()];
							for (int j=0;j<list.size();j++){
							tempData[j]=list.get(j);
							}
							P2PHandler.getInstance().setBindAlarmId(contactId,password,tempData.length,tempData);
							P2PHandler.getInstance().getBindAlarmId(contactId,password);
						}else{
							maxCount=5;
							data=new String[]{userId};
							P2PHandler.getInstance().setBindAlarmId(contactId,password,data.length,data);
							P2PHandler.getInstance().getBindAlarmId(contactId,password);
						}
					//	alarmTxt.setText("报警推送\n关闭");
						buttonAlert.dismiss();
					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub
					}
				});
				buttonAlert.show();
			}
		});
		setting_move.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final ButtonAlert buttonAlert = new ButtonAlert(activity);
				buttonAlert.setTitle(R.mipmap.info_systemset,getString(R.string.tip), getString(R.string.move1));
				buttonAlert.setButton(getString(R.string.open_2), getString(R.string.close_1), getString(R.string.cancle));
				buttonAlert
						.setVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE);
				buttonAlert.setOnSureListener(new ButtonAlert.OnSureListener() {
					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						P2PHandler.getInstance().setMotion(contactId,password,1);
						buttonAlert.dismiss();


					}

					@Override
					public void onSearch() {
						// TODO Auto-generated method stub
						P2PHandler.getInstance().setMotion(contactId,password,0);
						buttonAlert.dismiss();
					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub
					}
				});
				buttonAlert.show();
			}
		});
		setting_rf.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final ButtonAlert buttonAlert = new ButtonAlert(activity);
				buttonAlert.setTitle(R.mipmap.info_systemset,getString(R.string.tip), getString(R.string.peplered1));
				buttonAlert.setButton(getString(R.string.open_2), getString(R.string.close_1), getString(R.string.cancle));
				buttonAlert
						.setVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE);
				buttonAlert.setOnSureListener(new ButtonAlert.OnSureListener() {
					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						P2PHandler.getInstance().setInfraredSwitch(contactId,password,1);
						buttonAlert.dismiss();

					}

					@Override
					public void onSearch() {
						// TODO Auto-generated method stub
						P2PHandler.getInstance().setInfraredSwitch(contactId,password,0);
						buttonAlert.dismiss();
					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub
					}
				});
				buttonAlert.show();
			}
		});
		setting_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final EditTxtAlert alert = new EditTxtAlert(activity);
				alert.setTitle(android.R.drawable.ic_dialog_info, getString(R.string.admin_set),0);
				alert.setHint(getString(R.string.input_admin_set));
				alert.setOnSureListener(new EditTxtAlert.OnSureListener() {

					@Override
					public void onSure(String fileName) {
						// TODO Auto-generated method stub
						String newPwd = P2PHandler.getInstance().EntryPassword(fileName);
						P2PHandler.getInstance().setDevicePassword(contactId,password,newPwd);
						GateWay gateWay= GateWayFactory.getInstance().getGateWay(contactId);
						if(gateWay!=null){
							if(gateWay.getUserName().equals("admin")){
								gateWay.setUserPassWord(fileName);
								MyApplication.getInstance().getWidgetDataBase().updateGateWay(gateWay,gateWay.getGatewayName());
								GateWayFactory.getInstance().clearList();
								MainActivity.getInstance().stopActivityServer();
								MainActivity.getInstance().startActivityServer();
							}
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
		});




		setting_operator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent intent = new Intent(activity,
				// SettingUserActivity.class);
				// startActivity(intent);

				final EditTxtTwoAlert alert = new EditTxtTwoAlert(activity);
				alert.setTitle(R.mipmap.info_systemset, getString(R.string.input_option_set));
				alert.setHint(getString(R.string.input_option_count), getString(R.string.input_option_pwd));
				alert.setText(contactId,"");
				alert.setVisible(View.VISIBLE, View.GONE, View.VISIBLE);
				alert.setInputType(InputType.TYPE_CLASS_TEXT,InputType.TYPE_NUMBER_FLAG_DECIMAL);
				alert.setOnSureListener(new EditTxtTwoAlert.OnSureListener() {

					@Override
					public void onSure(String editName, String editValue) {
						String newPwd = P2PHandler.getInstance().EntryPassword(editValue);
						P2PHandler.getInstance().setDeviceVisitorPassword(contactId,password,newPwd);
						alert.dismiss();
					}

					@Override
					public void onSearch() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub
					}
				});
				alert.show();
			}
		});
		// �����ʼ�
		setting_mail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(activity, SettingMailActivity.class);
//				startActivity(intent);
			}
		});
		// ���ܱ���
		setting_smart_alarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		setting_ap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(activity, SettingApActivity.class);
//				startActivity(intent);
			}
		});
		setting_sdcard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(activity,
//						SettingSdcardActivity.class);
//				startActivity(intent);
			}
		});
		setting_ddns.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(activity, SettingDDNSActivity.class);
//				startActivity(intent);
			}
		});
		setting_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(activity, SettingDateActivity.class);
//				startActivity(intent);
			}
		});

		setting_timing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogAlert alert1 = new DialogAlert(activity);
				alert1.init(activity.getString(R.string.tip), getString(R.string.cleanalltiming));

				alert1.setSureListener(new DialogAlert.OnSureListener() {

					@Override
					public void onSure() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub

					}
				});
				alert1.show();
			}
		});
		setting_sence.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogAlert alert = new DialogAlert(activity);
				alert.init(activity.getString(R.string.tip), getString(R.string.cleanallmode));

				alert.setSureListener(new DialogAlert.OnSureListener() {

					@Override
					public void onSure() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub

					}
				});
				alert.show();
			}
		});
		setting_code.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final ButtonAlert buttonAlert = new ButtonAlert(activity);
				buttonAlert.setTitle(R.mipmap.info_systemset,getString(R.string.tip), getString(R.string.machineseting));
				buttonAlert.setButton( getString(R.string.edit),  getString(R.string.setting),  getString(R.string.cancle));
				buttonAlert
						.setVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE);
				buttonAlert.setOnSureListener(new ButtonAlert.OnSureListener() {
					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						machineDialog();
						buttonAlert.dismiss();

					}

					@Override
					public void onSearch() {
						// TODO Auto-generated method stub

						buttonAlert.dismiss();
					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub
					}
				});
				buttonAlert.show();
			}

		});

		back_item.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent resultIntent = new Intent();
				setResult(100, resultIntent);
				finish();
			}
		});

		setting_answer_radio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final AlertViewWindow alertViewWindow=new AlertViewWindow(activity,getString(R.string.answerbro), ListNumBer.getSwitch(),null,0);
				alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String itemName) {
						switch (pos){
							case 0: {
							}
							break;
							case 1: {
							}
							break;
						}
						alertViewWindow.dismiss();
					}
				});
				alertViewWindow.show();

			}
		});
		setting_timing_radio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final AlertViewWindow alertViewWindow=new AlertViewWindow(activity,getString(R.string.timmingbro), ListNumBer.getSwitch(),null,0);
				alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String itemName) {
						switch (pos){
							case 0:

							break;
							case 1:

							break;
						}
						alertViewWindow.dismiss();
					}
				});
				alertViewWindow.show();

			}
		});
		setting_anHong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogAlert alert = new DialogAlert(activity);
				alert.init(activity.getString(R.string.tip), getString(R.string.cleanallanhong));

				alert.setSureListener(new DialogAlert.OnSureListener() {

					@Override
					public void onSure() {
						// TODO Auto-generated method stub


					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub

					}
				});
				alert.show();

			}
		});
		setting_point.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogAlert alert = new DialogAlert(activity);
				alert.init(activity.getString(R.string.tip), "清空所有预设点");

				alert.setSureListener(new DialogAlert.OnSureListener() {

					@Override
					public void onSure() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub

					}
				});
				alert.show();

			}
		});
	}



	private void initView() {
		setting_anHong = (FrameLayout) findViewById(R.id.setting_anHong);
		machineTxt = (TextView) findViewById(R.id.machineTxt);
		setting_alarm = (FrameLayout) findViewById(R.id.setting_alarm);
		setting_mail = (FrameLayout) findViewById(R.id.setting_mail);
		setting_user = (FrameLayout) findViewById(R.id.setting_user);
		setting_move = (FrameLayout) findViewById(R.id.setting_move);
		setting_rf = (FrameLayout) findViewById(R.id.setting_rf);
		setting_ftp = (FrameLayout) findViewById(R.id.setting_ftp);
		setting_smart_alarm = (FrameLayout) findViewById(R.id.setting_smart_alarm);
		setting_sdcard = (FrameLayout) findViewById(R.id.setting_sdcard);
		setting_ap = (FrameLayout) findViewById(R.id.setting_ap);
		setting_ddns = (FrameLayout) findViewById(R.id.setting_ddns);
		setting_time = (FrameLayout) findViewById(R.id.setting_time);
		setting_timing = (FrameLayout) findViewById(R.id.setting_timing);
		setting_sence = (FrameLayout) findViewById(R.id.setting_sence);
		setting_code = (FrameLayout) findViewById(R.id.setting_code);
		setting_answer_radio = (FrameLayout) findViewById(R.id.answerRadio);
		setting_timing_radio = (FrameLayout) findViewById(R.id.timingRadio);
		setting_operator = (FrameLayout) findViewById(R.id.setting_operator);
		setting_point = (FrameLayout) findViewById(R.id.setting_point);

		back_item = (TextView) findViewById(R.id.back_item);
		alarmTxt = (TextView) findViewById(R.id.alarmTxt);
		userTxt = (TextView) findViewById(R.id.userTxt);
		title_item = (TextView) findViewById(R.id.title_item);
		title_item.setText(getString(R.string.gateSetting));

		timingTxt = (TextView) findViewById(R.id.timingTxt);
		modetxt = (TextView) findViewById(R.id.modeTxt);
		operatorTxt = (TextView) findViewById(R.id.operatorTxt);
		anHongTxt = (TextView) findViewById(R.id.anHongTxt);
		moveTxt = (TextView) findViewById(R.id.moveTxt);
		rfTxt=(TextView)findViewById(R.id.rfTxt);
		fun_item = (TextView) findViewById(R.id.fun_item);
		pointTxt = (TextView) findViewById(R.id.pointTxt);
		fun_item.setText("");
	}
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}
	public void machineDialog() {

		final EditTxtTwoAlert alert = new EditTxtTwoAlert(activity);
		alert.setTitle(R.mipmap.info_systemset, getString(R.string.machinesetting1));
		alert.setHint(getString(R.string.input_code), getString(R.string.powerPwd));
		alert.setText(machineTxt.getText().toString().replace("\n", "")
				.substring(5), "");
		alert.setVisible(View.VISIBLE, View.GONE, View.VISIBLE);
		alert.setOnSureListener(new EditTxtTwoAlert.OnSureListener() {

			@Override
			public void onSure(String editName, String editValue) {
				final String machineStr = editName;
				final String pwdStr = editValue;
				if ("".equals(machineStr) || machineStr.length() != 14) {
					Toast.makeText(activity, getString(R.string.input_code),
							Toast.LENGTH_SHORT).show();
					return;
				}
				if ("".equals(pwdStr) || !"himomo6882".equals(pwdStr)) {
					Toast.makeText(activity, getString(R.string.pwdnulll), Toast.LENGTH_SHORT)
							.show();

				} else {

					alert.dismiss();
				}
			}

			@Override
			public void onSearch() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCancle() {
				// TODO Auto-generated method stub

			}
		});
		alert.show();

	}


    public void regFilter() {
        IntentFilter filter = new IntentFilter();
		filter.addAction(P2P_ALARM);
        filter.addAction(P2P_SET_MOVE);
        filter.addAction(P2P_MOVE);
        filter.addAction(P2P_RF);
		filter.addAction(P2P_SET_RF);
		filter.addAction(P2P_VECTOR_PWD);

		filter.addAction(P2P_SET_VECTOR_PWD);
		filter.addAction(P2P_SET_PWD);
		filter.addAction(P2P_PWD);



        registerReceiver(mReceiver, filter);
    }
	public BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(P2P_SET_MOVE)) {
//				@param result 0:设置成功  255：不支持  其他：操作失败
				int result = intent.getIntExtra("result", -1);
				if(result==0){
					ToastUtils.ShowSuccess(activity,"移动侦测设置成功!",Toast.LENGTH_SHORT,true);
				}else	if(result==255){
					ToastUtils.ShowError(activity,"移动侦测不支持!",Toast.LENGTH_SHORT,true);
				}else{
					ToastUtils.ShowError(activity,"移动侦测设置失败!",Toast.LENGTH_SHORT,true);
				}
				P2PHandler.getInstance().getNpcSettings(contactId,password);
			} else if (intent.getAction().equals(P2P_MOVE)) {
				// * @param state 1：开启 0：关闭 255：不支持
				int state = intent.getIntExtra("state", 255);
				if(state==0){
					moveTxt.setText(getString(R.string.move1)+"\n"+getString(R.string.close_1));
				}else if(state==1){
					moveTxt.setText(getString(R.string.move1)+"\n"+getString(R.string.open_2));
				}else{
					moveTxt.setText(getString(R.string.move1)+"\n"+getString(R.string.nogree));
				}
			}else if(intent.getAction().equals(P2P_RF)){
				// * @param state 0：关闭    1：开启
				int state = intent.getIntExtra("state", 0);
				if(state==0){
					rfTxt.setText(getString(R.string.peplered1)+"\n"+getString(R.string.close_1));
				}else if(state==1){
					rfTxt.setText(getString(R.string.peplered1)+"\n"+getString(R.string.open_2));
				}
			}

			else if(intent.getAction().equals(P2P_SET_RF)){
				// * @param state 0：关闭    1：开启
				int result = intent.getIntExtra("result", -1);
				if(result==0){
					ToastUtils.ShowSuccess(activity,"人体红外设置成功!",Toast.LENGTH_SHORT,true);
				}else{
					ToastUtils.ShowError(activity,"人体红外设置失败!",Toast.LENGTH_SHORT,true);
				}
				P2PHandler.getInstance().getNpcSettings(contactId,password);
			}	else if(intent.getAction().equals(P2P_VECTOR_PWD)){
				int pwd = intent.getIntExtra("pwd", -1);
				operatorTxt.setText("普通用户密码设置\n"+pwd);
			}	else if(intent.getAction().equals(P2P_SET_VECTOR_PWD)){
				// * @param state 0：关闭    1：开启
				int result = intent.getIntExtra("result", -1);
				if(result==0){
					ToastUtils.ShowSuccess(activity,"访客密码设置成功!",Toast.LENGTH_SHORT,true);
				}else{
					ToastUtils.ShowError(activity,"访客密码设置失败!",Toast.LENGTH_SHORT,true);
				}
				P2PHandler.getInstance().getNpcSettings(contactId,password);
			}	else if(intent.getAction().equals(P2P_SET_PWD)){
				// * @param state 0：关闭    1：开启
				int result = intent.getIntExtra("result", -1);
				if(result==0){
					ToastUtils.ShowSuccess(activity,"管理员密码设置成功!",Toast.LENGTH_SHORT,true);
				}else{
					ToastUtils.ShowError(activity,"管理员密码设置失败!",Toast.LENGTH_SHORT,true);
				}
				P2PHandler.getInstance().getNpcSettings(contactId,password);
			}
			else if(intent.getAction().equals(P2P_ALARM)){
				// * @param state 0：关闭    1：开启
				String srcID = intent.getIntExtra("srcID",-1)+"";
				int result = intent.getIntExtra("result", -1);
				maxCount = intent.getIntExtra("maxCount", 5);
				data = intent.getStringArrayExtra("data");
				if(srcID.equals(contactId)){

					if(data!=null&&data.length>0){
						for (int i=0;i<data.length;i++){
							if(data[i].equals(userId)){
								alarmTxt.setText("报警推送\n开启");
								break;
							}
						}
					}

				}else{
				}
			}
		}
	};
	public int isSwitch(){
		int result=0;
		if(data!=null&&data.length>0) {
			for (int i = 0; i < data.length; i++) {
				if (data[i].equals(userId)) {
					result = 1;
					break;
				}
			}
		}
		return result;
	}
}
