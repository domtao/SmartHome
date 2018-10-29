package com.zunder.smart.activity.sub;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.sub.set.DeviceTimerActivity;
import com.zunder.smart.activity.sub.set.MoreActivity;
import com.zunder.smart.adapter.AnhongDialogAdapter;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.ProjectorAlert;
import com.zunder.smart.dialog.ProjectorCloundAlert;
import com.zunder.smart.listener.DeviceStateListener;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.BindProject;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.ProjectorName;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.remote.RemoteMainActivity;
import com.zunder.smart.roll.WheelView;
import com.zunder.smart.service.GateWayService;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.utils.ListNumBer;
import com.zunder.smart.webservice.ProjectorServiceUtils;

import java.util.ArrayList;
import java.util.List;

public class ProjectorActivity extends Activity implements OnClickListener,
		DeviceStateListener, OnLongClickListener {
	private static Activity activity;
	static CheckBox checkBox_1, checkBox_2, checkBox_3, checkBox_all,
			checkBoxTiming, source1, source2, source3, source4,remoteCheck;
	private static int Id = 0;
	private static Device deviceParams;
	TextView titelView, backTxt, actionName,remoteText;
	private static TextView checkBox_txt;
	private String deviceName = "";
	TextView sourceTxt1, sourceTxt2, sourceTxt3, sourceTxt4, timeSet;
	BindProject bindProject=null;
	private RightMenu rightButtonMenuAlert;
	public static void startActivity(Activity activity, int _id) {
		Id = _id;
		Intent intent = new Intent(activity, ProjectorActivity.class);
		activity.startActivityForResult(intent,100);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_projector);
		activity = this;
		deviceParams = DeviceFactory.getInstance().getDevicesById(Id);
		actionName = (TextView) findViewById(R.id.actionName);
		titelView = (TextView) findViewById(R.id.titleTxt);
		backTxt = (TextView) findViewById(R.id.backTxt);
		checkBox_txt=(TextView) findViewById(R.id.checkBox_txt);
		remoteText=(TextView) findViewById(R.id.remoteText);
		TcpSender.setDeviceStateListener(this);
		checkBox_1 = (CheckBox) findViewById(R.id.checkBox_1);
		checkBox_2 = (CheckBox) findViewById(R.id.checkBox_2);
		checkBox_3 = (CheckBox) findViewById(R.id.checkBox_3);
		source1 = (CheckBox) findViewById(R.id.source1);
		source2 = (CheckBox) findViewById(R.id.source2);
		source3 = (CheckBox) findViewById(R.id.source3);
		source4 = (CheckBox) findViewById(R.id.source4);
		remoteCheck= (CheckBox) findViewById(R.id.remoteCheck);

		checkBox_all = (CheckBox) findViewById(R.id.checkBox_all);
		checkBoxTiming = (CheckBox) findViewById(R.id.checkBoxTiming);
		sourceTxt1 = (TextView) findViewById(R.id.sourceTxt1);
		sourceTxt2 = (TextView) findViewById(R.id.sourceTxt2);
		sourceTxt3 = (TextView) findViewById(R.id.sourceTxt3);
		sourceTxt4 = (TextView) findViewById(R.id.sourceTxt4);
		timeSet = (TextView) findViewById(R.id.timeSet);
		timeSet.setOnClickListener(this);
		checkBox_1.setOnClickListener(this);
		checkBox_2.setOnClickListener(this);
		checkBox_3.setOnClickListener(this);
		source1.setOnClickListener(this);
		source2.setOnClickListener(this);
		source3.setOnClickListener(this);
		source4.setOnClickListener(this);
		source1.setOnLongClickListener(this);
		source2.setOnLongClickListener(this);
		source3.setOnLongClickListener(this);
		source4.setOnLongClickListener(this);
		remoteCheck.setOnClickListener(this);

		checkBox_all.setOnClickListener(this);
		backTxt.setOnClickListener(this);
		checkBoxTiming.setOnClickListener(this);
		deviceName = deviceParams.getRoomName()+deviceParams.getDeviceName();
		titelView.setText(deviceName);
		setParams();
		initRightButtonMenuAlert();
	}
	public void setParams() {
		String timerOrder = deviceParams.getDeviceOrdered();
		try{
			bindProject=JSONHelper.parseObject(timerOrder,BindProject.class);
			if(bindProject==null){
				bindProject=getBindProject();
				deviceParams.setDeviceOrdered(JSONHelper.toJSON(bindProject));
				MyApplication.getInstance().getWidgetDataBase()
						.updateDevice(deviceParams);
				DeviceFactory.getInstance().clearList();
			}
		}catch (Exception e){
			if(bindProject==null){
				bindProject=getBindProject();
				deviceParams.setDeviceOrdered(JSONHelper.toJSON(bindProject));
				MyApplication.getInstance().getWidgetDataBase()
						.updateDevice(deviceParams);
				DeviceFactory.getInstance().clearList();
			}
		}
		if(bindProject!=null) {
			if (!bindProject.getTent().equals("")) {
				actionName.setText(bindProject.getTent());
			}
			if (!bindProject.getSource1().equals("")){
				sourceTxt1.setText(bindProject.getSource1());
			}
			if (!bindProject.getSource2().equals("")) {
				sourceTxt2.setText(bindProject.getSource2());
			}
			if (!bindProject.getSource3().equals("")) {
				sourceTxt3.setText(bindProject.getSource3());
			}
			if (!bindProject.getSource4().equals("")) {
				sourceTxt4.setText(bindProject.getSource4());
			}
			if (!bindProject.getCentreControl().equals("")) {
				remoteText.setText(bindProject.getCentreControl());
			} else {
				remoteText.setText("绑定中控");
			}
		}
	}
	private void initRightButtonMenuAlert() {

			rightButtonMenuAlert = new RightMenu(activity, R.array.right_project,
					R.drawable.right_project_images);

		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {

			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub
				switch (position) {
					case 0: {
						setBind();
					}
						break;
					case 1: {
						DialogAlert alert = new DialogAlert(activity);
						alert.init(activity.getString(R.string.tip), getString(R.string.is_cancle_bind));

						alert.setSureListener(new DialogAlert.OnSureListener() {
							@Override
							public void onSure() {
								// TODO Auto-generated method stub
								if(bindProject==null){
									bindProject=getBindProject();
								}
								bindProject.setTent("");
								save();
								actionName.setText("无");
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
						setBindControl();
						break;
					case 3:{
						if(deviceParams.getProductsCode().equals("6F")) {
							showVideoWindow(activity.getString(R.string.power_study));
						}else{
							ToastUtils.ShowError(activity, "只有选择NEC才能编码学习!", Toast.LENGTH_SHORT,true);
						}
					}
						break;
					case 4:
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
		case R.id.timeSet:
			rightButtonMenuAlert.show(timeSet);

			break;
		case R.id.backTxt:
			back();
			break;
		case R.id.checkBox_1:
			if (!actionName.getText().equals(getString(R.string.no))) {
				SendCMD.getInstance()
						.sendCMD(
								0,
								actionName.getText()
										+ getString(R.string.close_1),

								null);
			} else {
				Toast.makeText(activity, getString(R.string.no_bind_pro), Toast.LENGTH_SHORT).show();
			}
			checkBox_1.setChecked(false);
			break;
		case R.id.checkBox_2:
			if (!actionName.getText().equals(getString(R.string.no))) {
				SendCMD.getInstance().sendCMD(0,deviceParams.getRoomName()+
						actionName.getText()+ getString(R.string.stop),
						null);
			} else {
				Toast.makeText(activity, getString(R.string.no_bind_pro), Toast.LENGTH_SHORT).show();
			}
			checkBox_2.setChecked(false);
			break;
		case R.id.checkBox_3:
			if (!actionName.getText().equals(getString(R.string.no))) {
				SendCMD.getInstance().sendCMD(0,deviceParams.getRoomName()+
						actionName.getText() + getString(R.string.open_1),
						null);
			} else {
				Toast.makeText(activity, getString(R.string.no_bind_pro), Toast.LENGTH_SHORT).show();
			}
			checkBox_3.setChecked(false);
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
		case R.id.source1:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+deviceName +getString(R.string.single_source1) ,
					deviceParams);
			Toast.makeText(activity, getString(R.string.single_source1), Toast.LENGTH_SHORT).show();
			break;
		case R.id.source2:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+deviceName + getString(R.string.single_source2),
					deviceParams);
			Toast.makeText(activity, getString(R.string.single_source2), Toast.LENGTH_SHORT).show();
			break;
		case R.id.source3:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+deviceName +getString(R.string.single_source3),
					deviceParams);
			Toast.makeText(activity, getString(R.string.single_source3), Toast.LENGTH_SHORT).show();
			break;
		case R.id.source4:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+deviceName + getString(R.string.single_source4),
					deviceParams);
			Toast.makeText(activity, getString(R.string.single_source4), Toast.LENGTH_SHORT).show();
			case R.id.remoteCheck:
				if (bindProject.getCentreControl().equals("")) {
					ToastUtils.ShowError(activity,"未绑定中控",Toast.LENGTH_SHORT,true);
				}else{
					String str=	remoteText.getText().toString().trim();
					GateWay gateWay= GateWayFactory.getInstance().getGateWayByName(str);
					if(gateWay!=null){
						RemoteMainActivity.deviceID=gateWay.getGatewayID();
						Intent intent = new Intent(activity,
								RemoteMainActivity.class);
						startActivity(intent);
					}else{
						ToastUtils.ShowError(activity,str+"不存在",Toast.LENGTH_SHORT,true);
					}
				}
				break;

		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		TcpSender.setDeviceStateListener(this);
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
				if(deviceParams.getState()>0){
					checkBox_all.setChecked(true);
					checkBox_txt.setText(activity.getString(R.string.open_2));
				}else{
					checkBox_all.setChecked(false);
					checkBox_txt.setText(activity.getString(R.string.close_1));
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void receiveDeviceStatus(String cmd) {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(1);

	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.source1:
			setAction("信号源1绑定",1, sourceTxt1);
			break;
		case R.id.source2:
			setAction("信号源2绑定",2, sourceTxt2);
			break;
		case R.id.source3:
			setAction("信号源3绑定",3, sourceTxt3);
			break;
		case R.id.source4:
			setAction("信号源4绑定",4, sourceTxt4);
			break;
		default:
			break;
		}
		return false;
	}
	@SuppressLint("NewApi")
	public void setBind() {

		List<String> list = DeviceFactory.getInstance().getCur();

		if (list.size()==0) {
			ToastUtils.ShowError(activity, getString(R.string.pro_no), Toast.LENGTH_SHORT,true);
		} else {
			final AlertViewWindow alertViewWindow=new AlertViewWindow(activity,getString(R.string.pro_bind),list,null,0);
			alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
				@Override
				public void onItem(int pos, String itemName) {
                    actionName.setText(itemName);
					if(bindProject==null){
						bindProject=getBindProject();
					}
					bindProject.setTent(itemName);
					save();
					alertViewWindow.dismiss();
				}
			});
			alertViewWindow.show();
		}
	}

	public void setBindControl() {

		List<String> list = GateWayFactory.getInstance().getRemoteCotrol();

		if (list.size()==0) {
			ToastUtils.ShowError(activity, getString(R.string.control_no), Toast.LENGTH_SHORT,true);
		} else {
			final AlertViewWindow alertViewWindow=new AlertViewWindow(activity,getString(R.string.pro_bind),list,null,0);
			alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
				@Override
				public void onItem(int pos, String itemName) {
					remoteText.setText(itemName);
					if(bindProject==null){
					bindProject=getBindProject();
					}
					bindProject.setCentreControl(itemName);
					save();
					alertViewWindow.dismiss();
				}
			});
			alertViewWindow.show();
		}
	}

	@SuppressLint("NewApi")
	public void setAction(String title,final int index, final TextView textView) {


		List<String> list= AppTools.convertToArray(activity.getResources().getStringArray(
				R.array.setProject));

		final AlertViewWindow alertViewWindow=new AlertViewWindow(activity,title,list,null,0);
		alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
			@Override
			public void onItem(int pos, String itemName) {
				textView.setText(itemName);
				if(bindProject==null){
					bindProject=getBindProject();
				}
				switch (index){
					case 1:
						bindProject.setSource1(itemName);
						break;
					case 2:
						bindProject.setSource2(itemName);
						break;
					case 3:
						bindProject.setSource3(itemName);
						break;
					case 4:
						bindProject.setSource4(itemName);
						break;
				}
				save();
				alertViewWindow.dismiss();
			}
		});
		alertViewWindow.show();


	}
	public void save() {
		if (bindProject!=null) {
			deviceParams.setDeviceOrdered(JSONHelper.toJSON(bindProject));
			MyApplication.getInstance().getWidgetDataBase()
					.updateDevice(deviceParams);
		}
	}
	View pupView;
	PopupWindow popVideoWindow = null;
	int selectIndex=0;
	private void showVideoWindow(final String _title ) {
		selectIndex=0;
		LayoutInflater layoutInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		pupView = layoutInflater.inflate(R.layout.popwindow_projector_layout,
				null);
		popVideoWindow = new PopupWindow(pupView,
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		TextView save = (TextView) pupView.findViewById(R.id.save);
		TextView net = (TextView) pupView.findViewById(R.id.net);
		TextView title = (TextView) pupView.findViewById(R.id.titleTxt);
		title.setText(_title);
		TextView showTxt = (TextView) pupView.findViewById(R.id.showTxt);
		WheelView wheel_camera = (WheelView) pupView
				.findViewById(R.id.wheel_camera);
		wheel_camera.setOffset(2);
		wheel_camera.setItems(ListNumBer.getProStudy());
		wheel_camera.setSeletion(0);
		wheel_camera
				.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
					@Override
					public void onSelected(int selectedIndex, String item) {
						selectIndex=selectedIndex-2;
					}
				});
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (selectIndex) {
					case 0: {
						final ProjectorAlert alert = new ProjectorAlert(activity);
						alert.setTitle(android.R.drawable.ic_dialog_info, getString(R.string.opencode));
						alert.setOnSureListener(new ProjectorAlert.OnSureListener() {
							@Override
							public void onSure(String fileName) {
								// TODO Auto-generated method stub
								String codeStr=fileName.trim();
								if (!"".equals(codeStr)&&((codeStr.length()&0x0001)==0)) {
									SendCMD.getInstance().sendCMD(238, "81:H"+toHex((codeStr.length()/2)+1)+codeStr,
											deviceParams);
									alert.dismiss();
								} else {
									Toast.makeText(activity,"input error!",Toast.LENGTH_SHORT).show();
								}

							}

							@Override
							public void onCancle() {
								// TODO Auto-generated method stub

							}
						});
						alert.show();
					}	popVideoWindow.dismiss();
					break;
					case 1: {
						final ProjectorAlert alert = new ProjectorAlert(activity);
						alert.setTitle(android.R.drawable.ic_dialog_info, getString(R.string.closecode));
						alert.setOnSureListener(new ProjectorAlert.OnSureListener() {
							@Override
							public void onSure(String fileName) {
								// TODO Auto-generated method stub
								String codeStr=fileName.trim();
								if (!"".equals(codeStr)&&((codeStr.length()&0x0001)==0)) {
									SendCMD.getInstance().sendCMD(238, "80:H"+toHex((codeStr.length()/2)+1)+codeStr,
											deviceParams);
									alert.dismiss();
								} else {
									Toast.makeText(activity,"input error!",Toast.LENGTH_SHORT).show();
								}
							}
							@Override
							public void onCancle() {
								// TODO Auto-generated method stub

							}
						});
						alert.show();
					}
					popVideoWindow.dismiss();
					break;
				}
			}
		});

		net.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (selectIndex) {
					case 0:
						new NamesAsyncTask(0).execute();
					break;
					case 1:
						new NamesAsyncTask(1).execute();
					break;
				}
			}
		});

		popVideoWindow.setFocusable(true);
		popVideoWindow.setOutsideTouchable(true);
		popVideoWindow.setBackgroundDrawable(new BitmapDrawable());
		popVideoWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
		popVideoWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
			}
		});

		popVideoWindow.setTouchInterceptor(new View.OnTouchListener() {
			public boolean onTouch(View view, MotionEvent event) {
				return false;
			}
		});
		popVideoWindow.showAtLocation(pupView, Gravity.BOTTOM, 0, 0);
	}
	private static String toHex(int number) {
		if (number <= 15) {
			return ("0" + Integer.toHexString(number).toUpperCase());
		} else {
			return Integer.toHexString(number).toUpperCase();
		}
	}

	public class NamesAsyncTask extends AsyncTask<String, Void, List<ProjectorName>> {
		private int index = 0;

		public NamesAsyncTask(int _index) {
			this.index = _index;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected List<ProjectorName> doInBackground(String... params) {
			List<ProjectorName> list = null;
			try {
				String json = ProjectorServiceUtils.getProjectors(1);
				list = (List<ProjectorName>) JSONHelper.parseCollection(json,
						List.class, ProjectorName.class);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return list;
		}

		@Override
		protected void onPostExecute(List<ProjectorName> result) {
			String msg;
			if(result!=null){
			if (index == 0) {
				final ProjectorCloundAlert alert = new ProjectorCloundAlert(activity,result);
				alert.setTitle(android.R.drawable.ic_dialog_info, getString(R.string.opencode));
				alert.setOnSureListener(new ProjectorCloundAlert.OnSureListener() {
					@Override
					public void onSure(String name,String code) {
						// TODO Auto-generated method stub
						String codeStr = code.trim().toUpperCase();
							SendCMD.getInstance().sendCMD(238, "81:H" + toHex((codeStr.length() / 2) + 1) + codeStr,
									deviceParams);
							alert.dismiss();
					}
					@Override
					public void onCancle() {
						// TODO Auto-generated method stub
					}
				});
				alert.show();
				popVideoWindow.dismiss();
			} else if (index == 1) {
				final ProjectorCloundAlert alert = new ProjectorCloundAlert(activity,result);
				alert.setTitle(android.R.drawable.ic_dialog_info, getString(R.string.closecode));
				alert.setOnSureListener(new ProjectorCloundAlert.OnSureListener() {
					@Override
					public void onSure(String name,String code) {
						// TODO Auto-generated method stub
						String codeStr = code.trim().toUpperCase();
						if (!"".equals(codeStr) && ((codeStr.length() & 0x0001) == 0)) {
							SendCMD.getInstance().sendCMD(238, "80:H" + toHex((codeStr.length() / 2) + 1) + codeStr,
									deviceParams);
							alert.dismiss();
						} else {
							Toast.makeText(activity, "input error!", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub

					}
				});
				alert.show();
				popVideoWindow.dismiss();
			}
		}else{
				ToastUtils.ShowError(activity,"云端没有上传品牌",Toast.LENGTH_SHORT,true);
			}
	}
	}
	public BindProject getBindProject(){
		BindProject	bindProject=new BindProject();
		bindProject.setTent("");
		bindProject.setSource1("");
		bindProject.setSource2("");
		bindProject.setSource3("");
		bindProject.setSource4("");
		bindProject.setCentreControl("");
		return bindProject;
	}
}
