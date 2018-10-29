/**
 *
 */
package com.zunder.smart.activity.gateway;

import org.json.JSONException;

import com.door.Utils.ToastUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.activity.main.TabMyActivity;
import com.zunder.smart.aiui.activity.SecurityActivity;
import com.zunder.smart.broadcast.ReceiverBroadcast;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dialog.AIUIAlert;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.EditTxtAlert;
import com.zunder.smart.dialog.EditTxtTwoAlert;
import com.zunder.smart.dialog.WarnDialog;
import com.zunder.smart.activity.RoutingActivity;
import com.zunder.smart.listener.DownListener;
import com.zunder.smart.listener.MachineCode;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.MasterRak;
import com.zunder.smart.model.ResultInfo;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.socket.info.ResponseInfo;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.utils.ListNumBer;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * @author Administrator
 *
 */
public class GateWaySettingFragment extends Fragment implements MachineCode,DownListener,View.OnTouchListener{
	private FrameLayout setting_camera_alias, setting_timing, setting_sence,setting_city,
			setting_code, setting_answer_radio, setting_timing_radio,
			setting_user, setting_operator, setting_anHong,setting_Security,setting_History,setting_Control,setting_greeting,setting_wakeup,setting_master;
	private TextView back_item;
	private static String deviceID;
	private Activity activity;
	ImageView codeFresh;
	TextView machineTxt,routeTxt, timingTxt, modetxt, userTxt, operatorTxt, aliaTxt,
			anHongTxt,answerRadioTxt,timingRadioTxt,historyTxt,controlTxt,cityTxt,greetingTxt,wakeupTxt;
	MasterRak master = null;
	AlertViewWindow alertViewWindow;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.gateway_setting_cloud, container,
				false);
		activity =getActivity();
		ReceiverBroadcast.setMachineCode(this);
		ReceiverBroadcast.setDownListener(this);
		initView(root);
		listenerClick();
		root.setOnTouchListener(this);
		return root;
	}
	public void setInit(GateWay gateWay){
		if(gateWay!=null) {
			deviceID = gateWay.getGatewayID();
			if (gateWay.getTypeId()==1) {
				setting_Security.setVisibility(View.VISIBLE);
				setting_greeting.setVisibility(View.GONE);
				setting_wakeup.setVisibility(View.GONE);
				setting_city.setVisibility(View.GONE);
				setting_History.setVisibility(View.GONE);
			}else{
				setting_greeting.setVisibility(View.VISIBLE);
				setting_wakeup.setVisibility(View.VISIBLE);
				setting_city.setVisibility(View.VISIBLE);
				setting_History.setVisibility(View.VISIBLE);
			}
			new SetAsyncTask().execute();
		}
	}

	WarnDialog warnDialog = null;

	void initDialog() {
		if(warnDialog==null) {
			warnDialog = new WarnDialog(activity, getString(R.string.getdeviceinfo));
			warnDialog.setMessage(getString(R.string.getting));

			warnDialog.setSureListener(new WarnDialog.OnSureListener() {
				@Override
				public void onCancle() {
					searchflag = false;
					warnDialog.dismiss();
				}
			});
		}
		warnDialog.show();
	}

	private boolean searchflag = false;
	private int startCount = 0;

	private void startTime() {
		searchflag = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (searchflag) {
					try {
						Thread.sleep(100);
						startCount++;
						if (startCount >= 20) {
							searchflag = false;
							if ((warnDialog != null) && warnDialog.isShowing()) {
								warnDialog.dismiss();
							}

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void initParam() {

		String result = ISocketCode.setVersionApk("AIUIVersion",
				deviceID);
		MainActivity.getInstance().sendCode(result);
		 result = ISocketCode.setForwardMasterInfo("get master info",
				deviceID);
		MainActivity.getInstance().sendCode( result);
		String cmd = "*SB01C00000E";
		result = ISocketCode.setForward(cmd, deviceID);
		MainActivity.getInstance().sendCode(result);
		 cmd = "*T85FF0000FFFF0000000000000000";
		result = ISocketCode.setForward(cmd, deviceID);
		MainActivity.getInstance().sendCode( result);
		cmd = "*M85FF0000FFFF0000000000000000";
		result = ISocketCode.setForward(cmd, deviceID);
		MainActivity.getInstance().sendCode( result);
		 cmd = "*@?";
		result = ISocketCode.setForward(cmd, deviceID);
		MainActivity.getInstance().sendCode( result);
		cmd = "*C0015AA1CFF000002FF0100";
		result = ISocketCode.setForward(cmd, deviceID);
		MainActivity.getInstance().sendCode( result);
	}
	private void listenerClick() {

		setting_city.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				final EditTxtAlert alert = new EditTxtAlert(activity);
				alert.setTitle(android.R.drawable.ic_dialog_info, getString(R.string.city),0);
				alert.setHint(getString(R.string.input_city));
				alert.setOnSureListener(new EditTxtAlert.OnSureListener() {

					@Override
					public void onSure(String fileName) {
						// TODO Auto-generated method stub
						if (fileName.equals("")) {
							ToastUtils.ShowError(activity,getString(R.string.city_null),Toast.LENGTH_SHORT,true);
							return;
						}
						String result=ISocketCode.setForwardNameControl("City:"+fileName,
								deviceID);
						MainActivity.getInstance().sendCode(result);

						 result = ISocketCode.setVersionApk("AIUIVersion",
								deviceID);
						MainActivity.getInstance().sendCode(result);
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
		setting_camera_alias.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// GateWayUserActivity.startActivity(activity, deviceID);

				final EditTxtAlert alert = new EditTxtAlert(activity);
				alert.setTitle(android.R.drawable.ic_dialog_info, getString(R.string.ailasetting1),0);
				alert.setHint(getString(R.string.input_alia));
				alert.setOnSureListener(new EditTxtAlert.OnSureListener() {

					@Override
					public void onSure(String fileName) {
						// TODO Auto-generated method stub
						if (master != null) {
							if (fileName.equals("")&&fileName.length()<5) {
								master.setMn(getString(R.string.device_cloud_txt));
							} else {
								master.setMn(fileName);
							}

							String cmd = JSONHelper.toJSON(master);
							String result = ISocketCode.setUpdateMasterInfo(
									cmd, deviceID);
							MainActivity.getInstance().sendCode(
									result);
							alert.dismiss();
						} else {
							master = new MasterRak();
							master.setMn(getString(R.string.device_cloud_txt));
							master.setPw("123");
							master.setOr("123");
							master.setOp("");
							master.setMn(fileName);

							String cmd = JSONHelper.toJSON(master);
							String result = ISocketCode.setUpdateMasterInfo(
									cmd, deviceID);
							MainActivity.getInstance().sendCode(
									result);
							alert.dismiss();
						}
						MainActivity.getInstance().sendCode( ISocketCode.setForwardMasterInfo("get master info",
								deviceID));
					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub

					}
				});
				alert.show();
			}
		});
		setting_user.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// GateWayUserActivity.startActivity(activity, deviceID);
				final EditTxtAlert alert = new EditTxtAlert(activity);
				alert.setTitle(android.R.drawable.ic_dialog_info, getString(R.string.adminsetting1),0);
				alert.setHint(getString(R.string.input_admin_set));
				alert.setOnSureListener(new EditTxtAlert.OnSureListener() {

					@Override
					public void onSure(String fileName) {
						// TODO Auto-generated method stub
						if (master != null&&fileName.length()<8) {
							master.setPw(fileName);
							String cmd = JSONHelper.toJSON(master);
							String result = ISocketCode.setUpdateMasterInfo(
									cmd, deviceID);
							MainActivity.getInstance().sendCode(result);
							MainActivity.getInstance().sendCode( ISocketCode.setForwardMasterInfo("get master info", deviceID));
							GateWay gateWay= GateWayFactory.getInstance().getGateWay(deviceID);
							gateWay.setUserPassWord(fileName);
							MyApplication.getInstance().getWidgetDataBase().updateGateWay(gateWay,gateWay.getGatewayName());
							GateWayFactory.getInstance().clearList();
							alert.dismiss();
						} else {
							Toast.makeText(activity, getString(R.string.settingfail1),
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

						String cmd = "*C0015AA1CFF000000FF0000";
						String result = ISocketCode.setForward(cmd, deviceID);
						MainActivity.getInstance().sendCode( result);

						cmd = "*C0015AA1CFF000002FF0100";
						result = ISocketCode.setForward(cmd, deviceID);
						MainActivity.getInstance().sendCode( result);
					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub
					}
				});
				alert.show();

			}
		});
		setting_Security.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				SecurityActivity.startActivity(activity,deviceID);

			}
		});
		setting_operator.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// GateWayUserActivity.startActivity(activity, deviceID);
				final EditTxtTwoAlert alert = new EditTxtTwoAlert(activity);
				alert.setTitle(R.mipmap.info_systemset, getString(R.string.input_option_set));
				alert.setHint(getString(R.string.input_option_count), getString(R.string.input_option_pwd));
				if (master != null) {
					alert.setText(master.getOr(), master.getOp());
				}
				alert.setVisible(View.VISIBLE, View.GONE, View.VISIBLE);
				alert.setOnSureListener(new EditTxtTwoAlert.OnSureListener() {
					@Override
					public void onSure(String editName, String editValue) {
						if (master != null&&editName.length()<8&&editName.length()<8) {
							master.setOr(editName);
							master.setOp(editValue);
							String cmd = JSONHelper.toJSON(master);
							String result = ISocketCode.setUpdateMasterInfo(
									cmd, deviceID);
							MainActivity.getInstance().sendCode(
									result);
							MainActivity.getInstance().sendCode( ISocketCode.setForwardMasterInfo("get master info",
									deviceID));
							GateWay gateWay=GateWayFactory.getInstance().getGateWay(deviceID);
							if(gateWay!=null){
								if(!gateWay.getUserName().equals("admin")){
									gateWay.setUserName(editName);
									gateWay.setUserPassWord(editValue);
									MyApplication.getInstance().getWidgetDataBase().updateGateWay(gateWay,gateWay.getGatewayName());
									GateWayFactory.getInstance().clearList();
									MainActivity.getInstance().stopActivityServer();
									MainActivity.getInstance().startActivityServer();
								}
							}
							alert.dismiss();
						} else {
							Toast.makeText(activity, getString(R.string.settingfail),
									Toast.LENGTH_SHORT).show();
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
		setting_timing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogAlert alert1 = new DialogAlert(activity);
				alert1.init(activity.getString(R.string.tip), getString(R.string.cleanalltiming));

				alert1.setSureListener(new DialogAlert.OnSureListener() {
					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						String cmd = "*T80FF0000FFFF0000000000000000";
						String result = ISocketCode.setForward(cmd, deviceID);
						MainActivity.getInstance().sendCode( result);
						cmd = "*T85FF00000000FFFF000000000000";
						result = ISocketCode.setForward(cmd, deviceID);
						MainActivity.getInstance().sendCode( result);
					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stube
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
						String cmd = "*M80FF0000FFFF0000000000000000";
						String result = ISocketCode.setForward(cmd, deviceID);
						MainActivity.getInstance().sendCode( result);
						cmd = "*M85FF0000FFFF0000000000000000";
						result = ISocketCode.setForward(cmd, deviceID);
						MainActivity.getInstance().sendCode( result);
					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub
					}
				});
				alert.show();
			}
		});

		back_item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ReceiverBroadcast.setMachineCode(null);
				ReceiverBroadcast.setDownListener(null);
				TabMyActivity.getInstance().hideFragMent(6);
			}
		});
		codeFresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String cmd = "*SB01C00000E";
				String result = ISocketCode.setForward(cmd, deviceID);
				MainActivity.getInstance().sendCode( result);
			}
		});
		setting_code.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 alertViewWindow=new AlertViewWindow(activity,getString(R.string.machineseting), ListNumBer.getMachineCode(),null,0);
				alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String itemName) {
						switch (pos){
							case 0: {
								machineDialog();
							}
							break;
							case 1: {
								String cmd = "*AL";
								String result = ISocketCode.setForward(cmd, deviceID);
								MainActivity.getInstance().sendCode( result);
							}
							break;
						}
						alertViewWindow.dismiss();
					}
				});
				alertViewWindow.show();
			}

		});
		setting_answer_radio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				alertViewWindow=new AlertViewWindow(activity,getString(R.string.answerbro), ListNumBer.getSwitch(),null,0);
				alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String itemName) {
						switch (pos){
							case 0: {
								//2018_07_16修改對應內存為reg 4 bit 0
								String result = ISocketCode.setForward(
										"*C0011AA1CFF000004000000", deviceID);
								MainActivity.getInstance().sendCode(result);
								result = ISocketCode.setForward("*@?", deviceID);
								MainActivity.getInstance().sendCode(result);
							}
								break;
							case 1: {
								String result = ISocketCode.setForward(
										"*C0011AA1CFF000004000100", deviceID);
								MainActivity.getInstance().sendCode(result);

								result = ISocketCode.setForward("*@?", deviceID);
								MainActivity.getInstance().sendCode(result);
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
				 alertViewWindow=new AlertViewWindow(activity,getString(R.string.timmingbro), ListNumBer.getSwitch(),null,0);
				alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String itemName) {
						switch (pos){
							case 0:{
								//2018_07_16修改對應內存為reg 4 bit 1
								String result = ISocketCode.setForward(
										"*C000AFA1CFF000004010100", deviceID);
								MainActivity.getInstance().sendCode( result);
								result = ISocketCode.setForward("*@?", deviceID);
								MainActivity.getInstance().sendCode( result);
							}
								break;
							case 1:{
								String result = ISocketCode.setForward(
										"*C000AFA1CFF000004010000", deviceID);
								MainActivity.getInstance().sendCode( result);

								result = ISocketCode.setForward("*@?", deviceID);
								MainActivity.getInstance().sendCode( result);
							}
								break;
						}
						alertViewWindow.dismiss();
					}
				});
				alertViewWindow.show();
			}
		});
		setting_Control.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {


			  alertViewWindow=new AlertViewWindow(activity,getString(R.string.remotecontrol1), ListNumBer.getSwitch(),null,0);
				alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String itemName) {
						switch (pos){
							case 0:{
								String result = ISocketCode.setForward(
										"*C0019FA1CFF000020000000", deviceID);
								MainActivity.getInstance().sendCode( result);
								result = ISocketCode.setForward("*@?", deviceID);
								MainActivity.getInstance().sendCode( result);
							}
								break;
							case 1:
							{
								String result = ISocketCode.setForward(
										"*C0019FA1CFF000010000000", deviceID);
								MainActivity.getInstance().sendCode( result);

								result = ISocketCode.setForward("*@?", deviceID);
								MainActivity.getInstance().sendCode( result);
							}
								break;
						}
						alertViewWindow.dismiss();
					}
				});
				alertViewWindow.show();
			}
		});
		setting_greeting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				final EditTxtAlert alert = new EditTxtAlert(activity);
				alert.setTitle(android.R.drawable.ic_dialog_info, "问候语设置",0);
				alert.setHint("请输入开机问候语");
				alert.setEditText(greetingTxt.getText().toString().replace("问候语","").replace("\n",""));
				alert.setOnSureListener(new EditTxtAlert.OnSureListener() {
					@Override
					public void onSure(String fileName) {
						// TODO Auto-generated method stub
						if (fileName.length()>=0) {

							String result=ISocketCode.setForwardNameControl("Greeting:"+fileName,
									deviceID);
							MainActivity.getInstance().sendCode(result);

							result = ISocketCode.setVersionApk("AIUIVersion",
									deviceID);
							MainActivity.getInstance().sendCode(result);

							alert.dismiss();
						} else {
							Toast.makeText(activity, "问候语不能为空",
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
		});
		setting_wakeup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				final EditTxtAlert alert = new EditTxtAlert(activity);
				alert.setTitle(android.R.drawable.ic_dialog_info, "唤醒应答设置",0);
				alert.setHint("请输入唤醒应答");
				alert.setEditText(wakeupTxt.getText().toString().replace("唤醒应答","").replace("\n",""));
				alert.setOnSureListener(new EditTxtAlert.OnSureListener() {
					@Override
					public void onSure(String fileName) {
						// TODO Auto-generated method stub
						String result=ISocketCode.setForwardNameControl("WakeUp:"+fileName,
								deviceID);
						if (fileName.length()==0) {
							result=ISocketCode.setForwardNameControl("WakeUp:no",
									deviceID);
						}
						MainActivity.getInstance().sendCode(result);
							result = ISocketCode.setVersionApk("AIUIVersion",
									deviceID);
							MainActivity.getInstance().sendCode(result);
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
	}
	private void initView(View root) {
		routeTxt=(TextView)root.findViewById(R.id.routeTxt);
		answerRadioTxt= (TextView) root.findViewById(R.id.answerRadioTxt);
		timingRadioTxt= (TextView) root.findViewById(R.id.timingRadioTxt);
		cityTxt=(TextView)root.findViewById(R.id.cityTxt);
		greetingTxt=(TextView)root.findViewById(R.id.greetingTxt);
		wakeupTxt=(TextView)root.findViewById(R.id.wakeupTxt);
		setting_Security = (FrameLayout) root.findViewById(R.id.setting_Security);
		setting_Control= (FrameLayout) root.findViewById(R.id.setting_Control);
		setting_greeting= (FrameLayout) root.findViewById(R.id.setting_greeting);
		setting_wakeup= (FrameLayout) root.findViewById(R.id.setting_wakeup);
		setting_anHong = (FrameLayout) root.findViewById(R.id.setting_anHong);
		setting_city=(FrameLayout)root.findViewById(R.id.setting_city) ;
		setting_master=(FrameLayout)root.findViewById(R.id.setting_master);
		setting_master.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RoutingActivity.startActivity(activity,deviceID);
			}
		});
		machineTxt = (TextView) root.findViewById(R.id.machineTxt);
		codeFresh = (ImageView) root.findViewById(R.id.codeFresh);
		setting_camera_alias = (FrameLayout) root.findViewById(R.id.setting_camera_alias);
		setting_timing = (FrameLayout) root.findViewById(R.id.setting_timing);
		setting_sence = (FrameLayout) root.findViewById(R.id.setting_sence);
		setting_code = (FrameLayout) root.findViewById(R.id.setting_code);
		setting_operator = (FrameLayout) root.findViewById(R.id.setting_operator);
		setting_user = (FrameLayout) root.findViewById(R.id.setting_user);
		back_item = (TextView) root.findViewById(R.id.back_item);
		anHongTxt = (TextView) root.findViewById(R.id.anHongTxt);
		setting_answer_radio = (FrameLayout) root.findViewById(R.id.answerRadio);
		setting_timing_radio = (FrameLayout) root.findViewById(R.id.timingRadio);
		setting_History=(FrameLayout)root.findViewById(R.id.setting_History);
		setting_History.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ReceiverBroadcast.setDownListener(GateWaySettingFragment.this);
				ToastUtils.ShowSuccess(activity,"正在获取版本信息",Toast.LENGTH_SHORT,true);
				String	result = ISocketCode.setVersionApk("AIUIVersion",
						deviceID);
				MainActivity.getInstance().sendCode(result);
			}
		});
		timingTxt = (TextView) root.findViewById(R.id.timingTxt);
		modetxt = (TextView) root.findViewById(R.id.modeTxt);
		operatorTxt = (TextView) root.findViewById(R.id.operatorTxt);
		userTxt = (TextView) root.findViewById(R.id.userTxt);
		aliaTxt = (TextView) root.findViewById(R.id.aliaTxt);
		historyTxt=(TextView)root.findViewById(R.id.historyTxt);
		controlTxt=(TextView)root.findViewById(R.id.controlTxt);
		GateWay gateWay= GateWayFactory.getInstance().getGateWay(deviceID);
		if(gateWay!=null&&gateWay.getTypeId()==1){
			setting_Security.setVisibility(View.VISIBLE);
		}else{
			setting_greeting.setVisibility(View.VISIBLE);
			setting_wakeup.setVisibility(View.VISIBLE);
			setting_city.setVisibility(View.VISIBLE);
			setting_History.setVisibility(View.VISIBLE);
		}
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
				if ("".equals(machineStr) || machineStr.length() != 12) {
					Toast.makeText(activity, getString(R.string.input_code),
							Toast.LENGTH_SHORT).show();
					return;
				}
				if ("".equals(pwdStr) || !"himomo6882".equals(pwdStr)) {
					Toast.makeText(activity, getString(R.string.pwdnulll), Toast.LENGTH_SHORT)
							.show();
				} else {
					String cmdStr = "*D1" + machineStr.toUpperCase();
					String result = ISocketCode.setForward(cmdStr, deviceID);
					MainActivity.getInstance().sendCode( result);
					alert.dismiss();
					result = ISocketCode.setForward("*SB01C00000E", deviceID);
					MainActivity.getInstance().sendCode( result);
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
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					machineTxt.setText(getString(R.string.machinesetting1)+"\n" + msg.obj.toString());
					break;
				case 1:
					modetxt.setText(getString(R.string.modesetting1)+"\n" + msg.obj.toString());
					break;
				case 2:
					timingTxt.setText(getString(R.string.timmingsetting1)+"\n" + msg.obj.toString());
					break;
				case 3:
					if ((warnDialog != null) && warnDialog.isShowing()) {
						warnDialog.dismiss();
					}
					searchflag = false;
					if (master != null) {
						String pwd = master.getPw();
						String masterOperator = master.getOr();
						String operatorPwd = master.getOp();
						String userName=master.getMn();
						userTxt.setText(getString(R.string.adminsetting1)+"\n" + getString(R.string.account)
								+ "admin" + "\t\t"+getString(R.string.psk_text)+ (pwd.equals("") ? getString(R.string.noset) : pwd));
						operatorTxt
								.setText(getString(R.string.usersetting1)+"\n"
										+ getString(R.string.account)
										+ (masterOperator.equals("") ? getString(R.string.noset)
										: masterOperator)
										+ "\t\t"+getString(R.string.psk_text)
										+ (operatorPwd.equals("") ? getString(R.string.noset)
										: operatorPwd));
						aliaTxt.setText(getString(R.string.ailasetting1)+"\n"+getString(R.string.alias)+":" + (userName.equals("")?getString(R.string.noset):userName));
					}
					break;
				case 4:
					anHongTxt.setText(getString(R.string.matchsetting1)+"\n" + msg.obj.toString());
					break;
				case 5:
					String cmd=msg.obj.toString();
					int routeNumber=Integer.valueOf(cmd.substring(9, 10), 16);
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
					if(routeNumber>0){
						routeTxt.setText("路由转发\n目前已设定"+routeNumber+"个,剩余"+(6-routeNumber)+"个可设置");
					}
					break;
				case 6:
					String result=msg.obj.toString();
					int index=result.indexOf("city");

					if(index!=-1) {
						historyTxt.setText(getString(R.string.aiuiservice)+"\n" + result.substring(0, index));
						String city=result.substring(index,result.length()).replace("city:","");
						cityTxt.setText(getString(R.string.citysetting1)+"\n"+getString(R.string.city)+":" + (city.equals("")?getString(R.string.noset):city));
					}else{
						historyTxt.setText(getString(R.string.aiuiservice)+"\n" + result);
					}

					break;
				case 7:
					String result2=msg.obj.toString();
					int index2=result2.indexOf("city");
					if(index2!=-1) {
						historyTxt.setText(getString(R.string.aiuiservice)+"\n" + result2.substring(0, index2));
						String city=result2.substring(index2,result2.length()).replace("city:","");
						cityTxt.setText(getString(R.string.citysetting1)+"\n"+getString(R.string.city)+":" + (city.equals("")?getString(R.string.noset):city));
					}else{
						historyTxt.setText(getString(R.string.aiuiservice)+"\n" + result2);
					}
					AIUIAlert apkAlert0 = new AIUIAlert(activity);
					apkAlert0.setOnSureListener(new AIUIAlert.OnSureListener() {
						@Override
						public void onCancle() {
							ReceiverBroadcast.setDownListener(GateWaySettingFragment.this);
						}
						@Override
						public void onSure(String str) {
						}
					});
					apkAlert0.show();
					break;
				case 8: {
					String greetingInfo = msg.obj.toString();
					if (greetingInfo.length() <= 0) {
						greetingTxt.setText("问候语\n未设置");
					} else {
						greetingTxt.setText("问候语\n" + greetingInfo);
					}
				}
					break;
				case 9: {
					String greetingInfo = msg.obj.toString();
					if (greetingInfo.equals("no")) {
						wakeupTxt.setText("唤醒应答\n未设置");
					} else {
						wakeupTxt.setText("唤醒应答\n" + greetingInfo);
					}
				}
					break;
				default:
					break;
			}
		}
	};

	@Override
	public void setCode(ResponseInfo socketInfo) {
		if (socketInfo.getMsgType().equals("GetMasterInfo")) {
			try {
				master = (MasterRak) JSONHelper.parseObject(
						socketInfo.getContent(), MasterRak.class);
				handler.sendEmptyMessage(3);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (deviceID.equals(socketInfo.getToID())||socketInfo.getToID().equals("000")) {
			String cmd = socketInfo.getContent();
			Log.e("CMD","net work_"+cmd);
			if (cmd.indexOf("DST NETWORK") != -1) {
				Message message = handler.obtainMessage();
				message.obj = cmd.substring(17).trim();
				message.what = 0;
				handler.sendMessage(message);
			} else if (cmd.startsWith("*M")) {
				String type = cmd.substring(4, 6);
				if (type.equals("05")) {
					int number = Integer.valueOf(cmd.substring(10, 12), 16);
					Message message = handler.obtainMessage();
					message.obj =  getString(R.string.modeseted)+":" + (250 - number) + "\t\t"+getString(R.string.surplus)+":"
							+ number;
					message.what = 1;
					handler.sendMessage(message);
				}
			} else if (cmd.startsWith("*T")) {
				String type = cmd.substring(4, 6);
				if (type.equals("05")) {
					int number = Integer.valueOf(cmd.substring(10, 12), 16);
					Message message = handler.obtainMessage();
					message.obj = getString(R.string.timmingseted)+":" + (200 - number) + "\t\t"+getString(R.string.surplus)+":"
							+ number;
					message.what = 2;
					handler.sendMessage(message);

				}

			} else if (cmd.startsWith("*d")) {
				// if(workType==1){
				// *TE1050100C8
				String type = cmd.substring(4, 6);
				if (type.equals("07")) {
					int number = Integer.valueOf(cmd.substring(18, 20), 16);
					Message message = handler.obtainMessage();
					message.obj =   getString(R.string.matchseted)+":" + number + "\t\t"+getString(R.string.surplus)+":"
							+ (32 - number);
					message.what = 4;
					handler.sendMessage(message);
				} else if (type.equals("1C")) {

					Message message = handler.obtainMessage();
					message.obj = cmd;
					message.what = 5;
					handler.sendMessage(message);

				}
			}
		}
	}


	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (hidden){
			ReceiverBroadcast.setMachineCode(null);
			ReceiverBroadcast.setDownListener(null);
		}else{
			ReceiverBroadcast.setMachineCode(this);
			ReceiverBroadcast.setDownListener(this);
		}
		onDialogDis();
	}

	@Override
	public void count(String number) {
		if(number.startsWith("ApkNew")){
			Message message=handler.obtainMessage();
			message.what=6;
			message.obj=number.replace("ApkNew:","");
			handler.dispatchMessage(message);
		}else if(number.startsWith("ApkUpdate")){
			Message message=handler.obtainMessage();
			message.what=7;
			message.obj=number.replace("ApkUpdate:","");
			handler.dispatchMessage(message);
		}else if(number.startsWith("GreetingInfo")){
			Message message=handler.obtainMessage();
			message.what=8;
			message.obj=number.replace("GreetingInfo:","");
			handler.dispatchMessage(message);
		}else if(number.startsWith("WakeUp")){
			Message message=handler.obtainMessage();
			message.what=9;
			message.obj=number.replace("WakeUp:","");
			handler.dispatchMessage(message);
		}
		Log.e("DomTao",number);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

	public class SetAsyncTask extends AsyncTask<String, Void, ResultInfo> {

		@Override
		protected void onPreExecute() {
			initDialog();
			startTime();
		}

		@Override
		protected ResultInfo doInBackground(String... params) {
			ResultInfo result = null;
			initParam();
			return result;
		}

		@Override
		protected void onPostExecute(ResultInfo result) {

		}
	}
	public void onDialogDis(){
		if(alertViewWindow!=null&&alertViewWindow.isShow()){
			alertViewWindow.dismiss();
		}
	}
}
