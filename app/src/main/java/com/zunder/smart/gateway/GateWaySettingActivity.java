/**
 * 
 */
package com.zunder.smart.gateway;

import hsl.p2pipcam.nativecaller.DeviceSDK;

import org.json.JSONObject;

import com.zunder.smart.R;
import com.zunder.smart.adapter.GateWayAdapter;
import com.zunder.smart.dialog.ButtonAlert;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.EditTxtAlert;
import com.zunder.smart.dialog.EditTxtTwoAlert;
import com.zunder.smart.gateway.model.GateUserInfo;
import com.zunder.smart.listener.DevicePramsListener;
import com.zunder.smart.listener.SearchDeviceListener;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.service.BridgeService;
import com.zunder.smart.service.GateWayService;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.utils.ListNumBer;
import com.zunder.smart.webservice.HistoryUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Administrator
 * 
 */
public class GateWaySettingActivity extends BaseActivity implements
		DevicePramsListener,SearchDeviceListener {
	private FrameLayout setting_alerm, setting_mail, setting_user,
			setting_wifi, setting_smart_alarm, setting_ftp, setting_sdcard,
			setting_ap, setting_ddns, setting_camera_alias, setting_time,
			setting_language, setting_timing, setting_code, setting_sence,
			setting_answer_radio, setting_timing_radio, setting_operator,setting_History,setting_Control,
			setting_anHong, setting_point;
	private TextView back_item, title_item, fun_item;
	private String camear_name;
	private String add_or_edit = "0";
	private Activity activity;
	ImageView codeFresh;
	int Id = 0;
	TextView machineTxt, wifiTxt, userTxt, systemTxt, timingTxt, modetxt,
			aliaTxt, operatorTxt, anHongTxt, pointTxt,answerRadioTxt,timingRadioTxt;
	GateUserInfo gateUserInfo = null;
	TextView controlTxt;
	private static	String deviceID="";
	public static void startActivity(Activity activity,String _deviceID) {
		deviceID=_deviceID;
		Intent intent = new Intent(activity, GateWaySettingActivity.class);
		activity.startActivity(intent);
	}

	// SharedPreferences spnet = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.test_screen);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gateway_setting);
		BridgeService.setDevicePramsListener(this);
		BridgeService.setSearchDeviceListener(this);
		activity = this;
		Intent intent = getIntent();
		add_or_edit = intent.getStringExtra("edit");
		Id = intent.getIntExtra("Id", 0);
		initView();
		listenerClick();
		DeviceSDK.getDeviceParam(GateWayAdapter.userid, 0x2013);
		DeviceSDK.getDeviceParam(GateWayAdapter.userid, 0x2003);
		DeviceSDK.getDeviceParam(GateWayAdapter.userid, 0x271C);
//		DeviceSDK.getDeviceParam(GateWayAdapter.userid, 0x2701);
		DeviceSDK.getDeviceParam(GateWayAdapter.userid, 0x2016);
		sendCmd("*SB01C00000E3");
		sendCmd("*T85FF0000FFFF00000000000000003");
		sendCmd("*M85FF0000FFFF00000000000000003");
		sendCmd("*C0015AA1CFF000002FF01003");
		sendCmd("*@?3");
		DeviceSDK.searchDevice();
	}

	private void listenerClick() {
		// ��������
		setting_alerm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, SettingAlarmActivity.class);
				startActivity(intent);
			}
		});
		// ����WIFI
		setting_wifi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(activity, SettingGateWayWifiActivity.class);
				startActivity(intent);
			}
		});
		setting_ftp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(activity, SettingFtpActivity.class);
//				startActivity(intent);
			}
		});
		// �����û�
		setting_user.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent intent = new Intent(activity,
				// SettingUserActivity.class);
				// startActivity(intent);

				final EditTxtAlert alert = new EditTxtAlert(activity);
				alert.setTitle(android.R.drawable.ic_dialog_info, getString(R.string.admin_set),0);
				alert.setHint(getString(R.string.input_admin_set));
				alert.setOnSureListener(new EditTxtAlert.OnSureListener() {

					@Override
					public void onSure(String fileName) {
						// TODO Auto-generated method stub
						JSONObject obj = new JSONObject();
						try {
							if (gateUserInfo != null) {
								obj.put("pwd1", "");
								obj.put("pwd2", gateUserInfo.getPwd2());
								obj.put("pwd3", fileName);
								obj.put("user1", "");
								obj.put("user2", gateUserInfo.getUser2());
								obj.put("user3", "admin");
								int Result = DeviceSDK.setDeviceParam(
										GateWayAdapter.userid, 0x2002,
										obj.toString());
								if (Result > 0) {
									Toast.makeText(activity, getString(R.string.settingsuccess),
											Toast.LENGTH_SHORT).show();
									Thread.sleep(100);
									Result = DeviceSDK.getDeviceParam(
											GateWayAdapter.userid, 0x2003);
									if (Result == 0) {
										finish();
									}
									alert.dismiss();

								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

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

				final EditTxtTwoAlert alert = new EditTxtTwoAlert(activity);
				alert.setTitle(R.mipmap.info_systemset, getString(R.string.input_option_set));
				alert.setHint(getString(R.string.input_option_count), getString(R.string.input_option_pwd));
				if (gateUserInfo != null) {

					alert.setText(gateUserInfo.getUser2(),
							gateUserInfo.getPwd2());
				}
				alert.setVisible(View.VISIBLE, View.GONE, View.VISIBLE);
				alert.setOnSureListener(new EditTxtTwoAlert.OnSureListener() {

					@Override
					public void onSure(String editName, String editValue) {
						JSONObject obj = new JSONObject();
						try {
							if (gateUserInfo != null) {
								obj.put("pwd1", "");
								obj.put("pwd2", editValue);
								obj.put("pwd3", gateUserInfo.getPwd3());
								obj.put("user1", "");
								obj.put("user2", editName);
								obj.put("user3", "admin");

								int Result = DeviceSDK.setDeviceParam(
										GateWayAdapter.userid, 0x2002,
										obj.toString());
								if (Result > 0) {
									Toast.makeText(activity, getString(R.string.settingsuccess),
											Toast.LENGTH_SHORT).show();
									Thread.sleep(100);

									Result = DeviceSDK.getDeviceParam(
											GateWayAdapter.userid, 0x2003);
									alert.dismiss();

								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
		});
		// �����ʼ�
		setting_mail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		setting_smart_alarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		setting_ap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		setting_sdcard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity,
						SettingSdcardActivity.class);
				startActivity(intent);
			}
		});
		setting_ddns.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		setting_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, SettingDateActivity.class);
				startActivity(intent);
			}
		});
		setting_language.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final ButtonAlert buttonAlert = new ButtonAlert(activity);
				buttonAlert.setTitle(R.mipmap.info_systemset,getString(R.string.tip), getString(R.string.systemset));
				buttonAlert.setButton(getString(R.string.mute1), getString(R.string.ch), getString(R.string.eh));
				buttonAlert
						.setVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE);
				buttonAlert.setOnSureListener(new ButtonAlert.OnSureListener() {
					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						JSONObject obj = new JSONObject();
						try {
							obj.put("language", 0);
							int Result = DeviceSDK.setDeviceParam(
									GateWayAdapter.userid, 0x271B,
									obj.toString());
							if (Result > 0) {
								Toast.makeText(activity, getString(R.string.settingsuccess),
										Toast.LENGTH_SHORT).show();
								Thread.sleep(100);
								DeviceSDK.getDeviceParam(GateWayAdapter.userid,
										0x271C);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						buttonAlert.dismiss();
					}

					@Override
					public void onSearch() {
						// TODO Auto-generated method stub
						JSONObject obj = new JSONObject();
						try {
							obj.put("language", 1);
							int Result = DeviceSDK.setDeviceParam(
									GateWayAdapter.userid, 0x271B,
									obj.toString());
							if (Result > 0) {
								Toast.makeText(activity, getString(R.string.settingsuccess),
										Toast.LENGTH_SHORT).show();
								Thread.sleep(100);
								DeviceSDK.getDeviceParam(GateWayAdapter.userid,
										0x271C);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						buttonAlert.dismiss();
					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub
						JSONObject obj = new JSONObject();
						try {
							obj.put("language", 2);
							int Result = DeviceSDK.setDeviceParam(
									GateWayAdapter.userid, 0x271B,
									obj.toString());
							if (Result > 0) {
								Toast.makeText(activity, getString(R.string.settingsuccess),
										Toast.LENGTH_SHORT).show();

								Thread.sleep(100);
								DeviceSDK.getDeviceParam(GateWayAdapter.userid,
										0x271C);

							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});
				buttonAlert.show();
			}
		});
		setting_camera_alias.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final EditTxtAlert alert = new EditTxtAlert(activity);
				alert.setTitle(android.R.drawable.ic_dialog_info, getString(R.string.ailasetting1),0);
				alert.setHint(getString(R.string.input_alia));
				alert.setOnSureListener(new EditTxtAlert.OnSureListener() {

					@Override
					public void onSure(String fileName) {
						// TODO Auto-generated method stub
						try {
							JSONObject obj = new JSONObject();
							obj.put("alias", fileName);
							int Result = DeviceSDK.setDeviceParam(
									GateWayAdapter.userid, 0x2702,
									obj.toString());
							if (Result > 0) {
								Toast.makeText(activity, getString(R.string.settingsuccess),
										Toast.LENGTH_SHORT).show();
								Message message = handler.obtainMessage();
								message.obj = getString(R.string.alias)+":" + fileName;
								message.what = 6;
								handler.sendMessage(message);
							}
						} catch (Exception e) {
							e.printStackTrace();

						}
					}
					@Override
					public void onCancle() {
						// TODO Auto-generated method stub

					}
				});
				alert.show();
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

						sendCmd("*T80FF0000FFFF00000000000000003");
						sendCmd("*T85FF00000000FFFF0000000000003");

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
						sendCmd("*M80FF0000FFFF00000000000000003");
						sendCmd("*M85FF0000FFFF00000000000000003");
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
				final AlertViewWindow alertViewWindow=new AlertViewWindow(activity,getString(R.string.machineseting), ListNumBer.getMachineCode(),null,0);
				alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String itemName) {
						switch (pos){
							case 0: {
								machineDialog();
							}
							break;
							case 1: {
								sendCmd("*AL3");
							}
							break;
						}
						alertViewWindow.dismiss();
					}
				});
				alertViewWindow.show();
				/*final ButtonAlert buttonAlert = new ButtonAlert(activity);
				buttonAlert.setTitle(R.mipmap.info_systemset, getString(R.string.machineseting));
				buttonAlert.setButton(getString(R.string.edite), getString(R.string.match_set), getString(R.string.cancle));
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
						sendCmd("*AL3");
						buttonAlert.dismiss();
					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub
					}
				});
				buttonAlert.show();
				*/
			}

		});

		back_item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				GateWayService.setMachineCode(null);
				BridgeService.setDevicePramsListener(null);
				BridgeService.setSearchDeviceListener(null);
				Intent resultIntent = new Intent();
				setResult(100, resultIntent);
				finish();
			}
		});
		codeFresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String cmd = "*SB01C00000E3";
				sendCmd(cmd);

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
								sendCmd("*CEA0AAA1CFF0000060100003");
								sendCmd("*@?3");
							}
							break;
							case 1: {
								sendCmd("*CEA0AAA1CFF0000060000003");
								sendCmd("*@?3");
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
							case 0: {
								sendCmd("*C000AFA1CFF0000040100003");
								sendCmd("*@?3");
							}
							break;
							case 1: {
								sendCmd("*C000AFA1CFF0000040000003");
								sendCmd("*@?3");
							}
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
						sendCmd("*C0015AA1CFF000000FF00003");
						sendCmd("*C0015AA1CFF000002FF01003");
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
				alert.init(activity.getString(R.string.tip), getString(R.string.cleanallpoint));

				alert.setSureListener(new DialogAlert.OnSureListener() {

					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						sendCmd("*@-3");
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

	public void sendCmd(String TampStr) {
		byte[] data = new byte[TampStr.length()];
		data = TampStr.getBytes();
		data[TampStr.length() - 1] = 13;
		String cmd = "";
		for (int i = 0; i < TampStr.length(); i++) {
			cmd = cmd + Integer.toHexString(data[i] / 16).toUpperCase()
					+ Integer.toHexString(data[i] & 15).toUpperCase();
		}
		int result = DeviceSDK.setDeviceParam(GateWayAdapter.userid, 0x2725,
				cmd);
	}

	private void initView() {

		answerRadioTxt= (TextView) findViewById(R.id.answerRadioTxt);
		controlTxt=(TextView)findViewById(R.id.controlTxt);
		timingRadioTxt= (TextView) findViewById(R.id.timingRadioTxt);
		setting_anHong = (FrameLayout) findViewById(R.id.setting_anHong);
		machineTxt = (TextView) findViewById(R.id.machineTxt);
		codeFresh = (ImageView) findViewById(R.id.codeFresh);
		setting_alerm = (FrameLayout) findViewById(R.id.setting_alerm);
		setting_mail = (FrameLayout) findViewById(R.id.setting_mail);
		setting_user = (FrameLayout) findViewById(R.id.setting_user);
		setting_wifi = (FrameLayout) findViewById(R.id.setting_wifi);
		setting_ftp = (FrameLayout) findViewById(R.id.setting_ftp);
		setting_smart_alarm = (FrameLayout) findViewById(R.id.setting_smart_alarm);
		setting_sdcard = (FrameLayout) findViewById(R.id.setting_sdcard);
		setting_ap = (FrameLayout) findViewById(R.id.setting_ap);
		setting_ddns = (FrameLayout) findViewById(R.id.setting_ddns);
		setting_camera_alias = (FrameLayout) findViewById(R.id.setting_camera_alias);
		setting_time = (FrameLayout) findViewById(R.id.setting_time);
		setting_language = (FrameLayout) findViewById(R.id.setting_language);
		setting_timing = (FrameLayout) findViewById(R.id.setting_timing);
		setting_sence = (FrameLayout) findViewById(R.id.setting_sence);
		setting_code = (FrameLayout) findViewById(R.id.setting_code);
		setting_History=(FrameLayout)findViewById(R.id.setting_History);
		setting_History.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogAlert alert = new DialogAlert(activity);
				alert.init(activity.getString(R.string.tip), getString(R.string.cleanallhis));
				alert.setSureListener(new DialogAlert.OnSureListener() {
					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						new AsyncTask(){

							@Override
							protected Object doInBackground(Object[] params) {
								String result="";
								try {
									result=	HistoryUtils.deleteHistorys(deviceID);
								} catch (Exception e) {
									e.printStackTrace();
								}	return "";
							}
							@Override
							protected void onPostExecute(Object o) {
								super.onPostExecute(o);
								DialogAlert dialogAlert=new DialogAlert(activity);
								dialogAlert.init(getString(R.string.tip),o.toString().split(":")[1]);
								dialogAlert.show();
							}
						}.execute();
					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub
					}
				});
				alert.show();
			}
		});
		setting_answer_radio = (FrameLayout) findViewById(R.id.answerRadio);
		setting_timing_radio = (FrameLayout) findViewById(R.id.timingRadio);

		setting_operator = (FrameLayout) findViewById(R.id.setting_operator);
		setting_point = (FrameLayout) findViewById(R.id.setting_point);
		back_item = (TextView) findViewById(R.id.back_item);
		wifiTxt = (TextView) findViewById(R.id.wifiTxt);
		userTxt = (TextView) findViewById(R.id.userTxt);
		systemTxt = (TextView) findViewById(R.id.systemTxt);
		title_item = (TextView) findViewById(R.id.title_item);
		title_item.setText(getString(R.string.gateSetting));

		timingTxt = (TextView) findViewById(R.id.timingTxt);
		modetxt = (TextView) findViewById(R.id.modeTxt);
		operatorTxt = (TextView) findViewById(R.id.operatorTxt);
		anHongTxt = (TextView) findViewById(R.id.anHongTxt);
		aliaTxt = (TextView) findViewById(R.id.aliaTxt);
		fun_item = (TextView) findViewById(R.id.fun_item);
		pointTxt = (TextView) findViewById(R.id.pointTxt);
		setting_Control= (FrameLayout) findViewById(R.id.setting_Control);
		fun_item.setText("");
		if ("0".equals(add_or_edit)) {
			setting_alerm.setVisibility(View.GONE);
			setting_mail.setVisibility(View.GONE);
			setting_user.setVisibility(View.GONE);
			setting_wifi.setVisibility(View.GONE);
			setting_smart_alarm.setVisibility(View.GONE);
		}
		setting_Control.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				final AlertViewWindow alertViewWindow=new AlertViewWindow(activity,getString(R.string.remotecontrol1), ListNumBer.getSwitch(),null,0);
				alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String itemName) {
						switch (pos){
							case 0: {
								sendCmd("*C0019FA1CFF0000200000003");
								sendCmd("*@?3");
							}
							break;
							case 1: {
								sendCmd("*C0019FA1CFF0000100000003");
								sendCmd("*@?3");
							}
							break;
						}
						alertViewWindow.dismiss();
					}
				});
				alertViewWindow.show();


			}
		});
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				machineTxt.setText(getString(R.string.machinesetting1)+"\n" + msg.obj.toString());
				break;
			case 1:
				wifiTxt.setText(getString(R.string.wifiSet)+"\n" + msg.obj.toString());
				break;
			case 2:
				try {
					if (gateUserInfo != null) {

						String pwd = gateUserInfo.getPwd3();
						userTxt.setText(getString(R.string.adminsetting1)+"\n" + getString(R.string.account)
								+ gateUserInfo.getUser3() + "\t\t"+getString(R.string.psk_text)+ (pwd.equals("") ? getString(R.string.noset) : pwd));

						String masterOperator = gateUserInfo.getUser2();
						String operatorPwd = gateUserInfo.getPwd2();
						operatorTxt
								.setText(getString(R.string.usersetting1)+"\n"
										+ getString(R.string.account)
										+ (masterOperator.equals("") ? getString(R.string.noset)
												: masterOperator)
										+ "\t\t"+getString(R.string.psk_text)
										+ (operatorPwd.equals("") ? getString(R.string.noset)
												: operatorPwd));
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case 3:
				systemTxt.setText(getString(R.string.systemset)+"\n" + msg.obj.toString());
				break;
			case 4:
				timingTxt.setText(getString(R.string.timmingsetting1)+"\n" + msg.obj.toString());

				break;
			case 5:
				modetxt.setText(getString(R.string.modesetting1)+"\n" + msg.obj.toString());

				break;

			case 6:
				aliaTxt.setText(getString(R.string.aliaset)+"\n" + msg.obj.toString());

				break;
			case 7:
				anHongTxt.setText(getString(R.string.matchsetting1)+"\n" + msg.obj.toString());

				break;
			case 8:
				pointTxt.setText(getString(R.string.pointsetting1)+"\n" + msg.obj.toString());

				break;
				case 9:
					String cmd=msg.obj.toString();
					int ack = Integer.valueOf(cmd.substring(11, 12), 16);
					int time = Integer.valueOf(cmd.substring(13, 14), 16);
					if(ack>0){
						answerRadioTxt.setText(getString(R.string.answerbro)+"\n"+getString(R.string.open_2));

					}else{
						answerRadioTxt.setText(getString(R.string.answerbro)+"\n"+getString(R.string.close_1));
					}
					if(time>0){
					timingRadioTxt.setText(getString(R.string.timmingbro)+"\n"+getString(R.string.open_2));
					}else{
					timingRadioTxt.setText(getString(R.string.timmingbro)+"\n"+getString(R.string.close_1));
					}
					if(cmd.length()>15){
						time=Integer.valueOf(cmd.substring(15, 16), 16);
						if(time>0){
							controlTxt.setText(getString(R.string.remotecontrol1)+"\n"+getString(R.string.open_2));
						}else{
							controlTxt.setText(getString(R.string.remotecontrol1)+"\n"+getString(R.string.close_1));
					}
					}else{
						controlTxt.setText(getString(R.string.remotecontrol));
					}
					break;
			default:
				break;
			}
		}
	};

	protected void onDestroy() {
		super.onDestroy();
		GateWayService.setDevicePramsListener();
		BridgeService.setSearchDeviceListener(null);
	}

	public void machineDialog() {

		final EditTxtTwoAlert alert = new EditTxtTwoAlert(activity);
		alert.setTitle(R.mipmap.info_systemset, getString(R.string.machinesetting1));
		alert.setHint(getString(R.string.input_code), getString(R.string.powerPwd));
		alert.setText(machineTxt.getText().toString().replace("\n", "")
				.substring(4), "");
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
					String cmdStr = "*D1" + machineStr.toUpperCase() + "3";
					sendCmd(cmdStr);
					alert.dismiss();
					sendCmd("*SB01C00000E3");
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

	@Override
	public void getDevicePrams(long UserID, long nType, String param) {
		// TODO Auto-generated method stub
		try {
			if (GateWayAdapter.userid == UserID) {
				if (nType == 0X2013) {
					JSONObject localJSONObject = new JSONObject(param);
					String ssID = localJSONObject.get("ssid").toString();
					Message message = handler.obtainMessage();
					message.obj = "SSID:" + (ssID.equals("") ? getString(R.string.noset) : ssID);
					message.what = 1;
					handler.sendMessage(message);
				} else if (nType == 0X2003) {
					gateUserInfo = (GateUserInfo) JSONHelper.parseObject(param,
							GateUserInfo.class);
					Message message = handler.obtainMessage();

					message.what = 2;
					handler.sendMessage(message);

				} else if (nType == 0x271C) {
					JSONObject jsonObject = new JSONObject(param);
					Message message = handler.obtainMessage();
					int index = jsonObject.getInt("language");
					if (index == 0) {
						message.obj =getString(R.string.sound) +":"+getString(R.string.mute1);
					} else if (index == 1) {
						message.obj =getString(R.string.langvage1) +":"+getString(R.string.ch);
					} else if (index == 2) {
						message.obj =getString(R.string.langvage1) +":"+getString(R.string.eh);
					}

					message.what = 3;
					handler.sendMessage(message);
				} else if (nType == 0x2016) {

				} else if (nType == 10022) {

					if (param.indexOf("DST NETWORK") != -1) {
						// DST NETWORK:D1500000000000000
						Message message = handler.obtainMessage();
						message.obj = param.substring(15).trim();
						message.what = 0;
						handler.sendMessage(message);
					} else if (param.startsWith("*M")) {

						String type = param.substring(4, 6);

						if (type.equals("05")) {
							int number = Integer.valueOf(
									param.substring(10, 12), 16);
							Message message = handler.obtainMessage();
							message.obj = getString(R.string.modeseted)+":" + (250 - number) + "\t\t"+getString(R.string.surplus)+":"
									+ number;
							message.what = 5;
							handler.sendMessage(message);
						}

					} else if (param.startsWith("*T")) {
						// if(workType==1){
						// *TE1050100C8
						String type = param.substring(4, 6);

						if (type.equals("05")) {
							int number = Integer.valueOf(
									param.substring(10, 12), 16);
							Message message = handler.obtainMessage();
							message.obj = getString(R.string.timmingseted)+":" + (200 - number) + "\t\t"+getString(R.string.surplus)+":"
									+ number;
							message.what = 4;
							handler.sendMessage(message);

						}

					} else if (param.startsWith("*d")) {
						// if(workType==1){
						// *TE1050100C8
						String type = param.substring(4, 6);

						if (type.equals("07")) {
							int number = Integer.valueOf(
									param.substring(18, 20), 16);
							Message message = handler.obtainMessage();
							message.obj =  getString(R.string.matchseted)+":" + number + "\t\t"+getString(R.string.surplus)+":"
									+ (32 - number);
							message.what = 7;
							handler.sendMessage(message);

						}else if (type.equals("1C")) {

							Message message = handler.obtainMessage();
							message.obj = param;
							message.what = 9;
							handler.sendMessage(message);

						}

					} else if (param.startsWith("*@")) {
						Message message = handler.obtainMessage();
						message.obj = param.substring(5, 21);

						message.what = 8;
						handler.sendMessage(message);
					}
				} else if (nType == 0x2701) {
					JSONObject jsonObject = new JSONObject(param);
					Message message = handler.obtainMessage();
					message.obj = getString(R.string.alias)+":" + jsonObject.getString("alias");
					message.what = 6;
					handler.sendMessage(message);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void callBack_SearchDevice(String DeviceInfo) {
		// TODO Auto-generated method stub
		try
		{
			JSONObject	jsonObject = new JSONObject(DeviceInfo);
			String id = jsonObject.getString("DeviceID");
			int types = jsonObject.getInt("DeviceType");
			String ip = jsonObject.getString("IP");
			String name = jsonObject.getString("DeviceName");
			String mac = jsonObject.getString("Mac");
			int port = jsonObject.getInt("Port");
			int smartConnect = jsonObject.getInt("SmartConnect");
			if(deviceID.equals(id)){
				Message message = handler.obtainMessage();
				message.obj = "别名:" + name;
				message.what = 6;
				handler.sendMessage(message);
			}
		}catch (Exception e){
		}
	}
}
