package com.zunder.smart.activity.gateway;

import hsl.p2pipcam.nativecaller.DeviceSDK;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.bluecam.api.bean.SearchBean;
import com.bluecam.bluecamlib.BCamera;
import com.bluecam.bluecamlib.Camera;
import com.bluecam.bluecamlib.CameraBean;
import com.bluecam.bluecamlib.CameraManager;
import com.bluecam.bluecamlib.listener.CameraEventListener;
import com.door.DoorApplication;
import com.door.Utils.ToastUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.activity.main.TabMyActivity;
import com.zunder.smart.adapter.GateMsgAdapter;
import com.zunder.smart.adapter.GateWayTypeAdapter;
import com.zunder.smart.custom.view.MyGridView;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.DeviceTypeFactory;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dao.impl.factory.GateWayTypeFactory;
import com.zunder.smart.dialog.WarnDialog;
import com.zunder.smart.json.MasterUtils;
import com.zunder.smart.listener.SearchDeviceListener;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.GateWayMsg;
import com.zunder.smart.model.GatewayType;
import com.zunder.smart.model.Master;
import com.zunder.smart.netty.MockLoginNettyClient;
import com.zunder.smart.popwindow.AirSwitchViewWindow;
import com.zunder.smart.scan.CaptureActivity;
import com.zunder.smart.service.BridgeService;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.SystemInfo;
import com.example.rak47x.RAKInfo;
import com.example.rak47x.Scanner;
import com.jwkj.shakmanger.LocalDevice;
import com.jwkj.shakmanger.ShakeListener;
import com.jwkj.shakmanger.ShakeManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class GateWayAddFragment extends Fragment implements
		SearchDeviceListener, OnClickListener,View.OnTouchListener{
	private Scanner _scanner;
	private TextView backTxt;
	private TextView editeTxt;

	private MyGridView  gateWayListView;
	private String modelOpration = "Add";
	private Activity activity;
	private int Id = -1;
	private int IsCurrent;
	private int seqencing = 0;
	private EditText gateWayId, gateWayName, userName, password;

	private int gatewayTypeID = -1;
	GateMsgAdapter msgAdapter;
	private String gateWayNameStr = "";
	private List<GateWayMsg> list = new ArrayList<GateWayMsg>();
	ImageView imageView;
	ImageButton setType;
	TextView typeTxt;

	private List<SearchBean> cameraList = new ArrayList<SearchBean>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_gateway_add, container,
				false);
		activity = getActivity();
		gateWayName = (EditText) root.findViewById(R.id.gateWayName);
		gateWayId = (EditText) root.findViewById(R.id.gateWayID);
		userName = (EditText) root.findViewById(R.id.userName);
		password = (EditText) root.findViewById(R.id.pwd);
		backTxt = (TextView) root.findViewById(R.id.backTxt);
		editeTxt = (TextView) root.findViewById(R.id.editeTxt);
		imageView = (ImageView) root.findViewById(R.id.scanImage);
		setType=(ImageButton)root.findViewById(R.id.set_type);
		typeTxt=(TextView)root.findViewById(R.id.typeTxt);
		setType.setOnClickListener(this);
		imageView.setOnClickListener(this);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		root.setOnTouchListener(this);
		gateWayListView = (MyGridView) root.findViewById(R.id.gateWayList);
		GateWayMsg gateWayMsg = new GateWayMsg();
		gateWayMsg.setDeviceID("00000");
		gateWayMsg.setDeviceName(getString(R.string.gateWayFind));
		list.add(gateWayMsg);
		msgAdapter = new GateMsgAdapter(activity, list);
		gateWayListView.setAdapter(msgAdapter);
		gateWayListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				if (position == 0) {
					find();
				} else {
					String deviceID = list.get(position).getDeviceID();
					if(gatewayTypeID<=2) {
						String cmd = "*C0019FA1C000000A90A0000";
						String result = ISocketCode.setForward(cmd, deviceID);
						MainActivity.getInstance().sendCode(result);
					}
					gateWayName.setText(list.get(position).getDeviceName());
					gateWayId.setText(deviceID);
					gatewayTypeID = list.get(position).getProductsCode();
					userName.setText("admin");

				}
			}
		});
		return root;
	}

	public void setEdite(String cmd,GateWay gateWay,int typeId){
		modelOpration = cmd;
		list.clear();
		msgAdapter.notifyDataSetChanged();
		if (modelOpration.equals("Add")) {
			gateWayName.setText("");
			gateWayId.setText("");
			password.setText("");
			gatewayTypeID = typeId;
			seqencing = GateWayTypeFactory.list.size() + 1;
			typeTxt.setText(GateWayTypeFactory.getGatewayName(gatewayTypeID));
			find();
		} else if (modelOpration.equals("Edite")) {
			Id = gateWay.getId();
			IsCurrent = gateWay.getIsCurrent();
			gatewayTypeID = gateWay.getTypeId();
			gateWayNameStr =gateWay.getGatewayName();
			gateWayName.setText(gateWayNameStr);
			gateWayId.setText(gateWay.getGatewayID());
			userName.setText(gateWay.getUserName());
			password.setText(gateWay.getUserPassWord());
			seqencing =gateWay.getSeqencing();
			typeTxt.setText(GateWayTypeFactory.getGatewayName(gatewayTypeID));
		}else if (modelOpration.equals("Edite1")){
			IsCurrent =  gateWay.getIsCurrent();
			gatewayTypeID= gateWay.getTypeId();
			gateWayNameStr = gateWay.getGatewayName();
			gateWayName.setText(gateWayNameStr);
			gateWayId.setText(gateWay.getGatewayID());
			userName.setText("admin");
			password.setText(gateWay.getUserPassWord());
			seqencing =gateWay.getSeqencing();
			typeTxt.setText(GateWayTypeFactory.getGatewayName(gatewayTypeID));
			modelOpration="Add";
		}
	}

	public static IWidgetDAO sql() {
		return MyApplication.getInstance().getWidgetDataBase();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.set_type:
				List<String> listTypes = GateWayTypeFactory.getGatewayNames();
				final AirSwitchViewWindow airSwitchViewWindow=new AirSwitchViewWindow(activity,"类型",listTypes,null,gatewayTypeID-1);
				airSwitchViewWindow.setAlertViewOnCListener(new AirSwitchViewWindow.AlertViewOnCListener(){

					@Override
					public void onItem(int pos, String itemName) {
						if(!typeTxt.getText().toString().equals(itemName)) {
							if(pos==3){
								gatewayTypeID=200;
							}else {
								gatewayTypeID = pos + 1;
							}
							typeTxt.setText(itemName);
							find();
							airSwitchViewWindow.dismiss();
						}
					}
					@Override
					public void cancle() {
					}
				});
				airSwitchViewWindow.show();
				break;
			case R.id.scanImage:
				Intent stratScan = new Intent(activity, CaptureActivity.class);
				activity.startActivityForResult(stratScan, 0);
				break;
			case R.id.backTxt:
				back();
				break;
			case R.id.editeTxt:
				if (TextUtils.isEmpty(gateWayName.getText())) {
					ToastUtils.ShowError(activity,
							getString(R.string.gateWayNamenull), Toast.LENGTH_SHORT,true);
					return;
				}
				if (TextUtils.isEmpty(gateWayId.getText())) {
					ToastUtils.ShowError(activity,
							getString(R.string.gateWayIDnull), Toast.LENGTH_SHORT,true);
					return;
				}
				if (gatewayTypeID == -1) {
					ToastUtils.ShowError(activity,
							getString(R.string.gateWayIDnull), Toast.LENGTH_SHORT,true);
					return;
				}
				String gatewayName = gateWayName.getText().toString();
				String gatewayID = gateWayId.getText().toString();
				String user_Name = userName.getText().toString();
				String userPassWprd = password.getText().toString();
				GateWay gateWay = new GateWay();
				gateWay.setGatewayName(gatewayName);
				gateWay.setGatewayID(gatewayID);
				gateWay.setUserName(user_Name);
				gateWay.setTypeId(gatewayTypeID);
				gateWay.setState("0");
				gateWay.setUserPassWord(userPassWprd);
				gateWay.setIsCurrent(IsCurrent);
				gateWay.setSeqencing(seqencing);
				GateWay temp=	GateWayFactory.getInstance().getGateWay(gatewayID);
				if ("Add".equals(modelOpration)) {
					if(temp!=null){
						ToastUtils.ShowError(activity,getString(R.string.no_re_add),Toast.LENGTH_SHORT,true);
						return ;
					}
					if (GateWayFactory.list.size() == 0) {
						gateWay.setIsCurrent(1);
					}
					int result = sql().insertGateWay(gateWay);
					if (result >= 1) {
						GateWayFactory.getInstance().clearList();
						if (MockLoginNettyClient.getInstans().isConnect()) {
							try {
								MainActivity.getInstance().initSocket();
							} catch (Exception e) {
							}
						}
						MainActivity.getInstance().stopActivityServer();
						MainActivity.getInstance().startActivityServer();
						ToastUtils.ShowSuccess(activity, getString(R.string.addSuccess),
								Toast.LENGTH_SHORT,true);
						back();
					} else if (result == -1) {
						ToastUtils.ShowError(activity,
								gatewayName + getString(R.string.exist),
								Toast.LENGTH_SHORT,true);
					}
				} else if ("Edite".equals(modelOpration)) {
					gateWay.setId(Id);
					int num = sql().updateGateWay(gateWay, gateWayNameStr);
					if (num == 2) {
						ToastUtils.ShowError(activity,
								gatewayName + getString(R.string.exist),
								Toast.LENGTH_SHORT,true);
					} else if (num == 1) {
						gateWayNameStr = gatewayName;
						ToastUtils.ShowSuccess(activity,
								gatewayName + getString(R.string.updateSuccess),
								Toast.LENGTH_SHORT,true);
						GateWayFactory.getInstance().clearList();
						MainActivity.getInstance().stopActivityServer();
						MainActivity.getInstance().startActivityServer();
						back();
					}
				}
				break;
			default:
				break;
		}
	}
	public void back() {
		warnDialog = null;
		TabMyActivity.getInstance().hideFragMent(5);
	}
	private boolean searchflag = false;
	private int startCount = 0;
	private JSONObject jsonObject;

	@Override
	public void callBack_SearchDevice(String DeviceInfo) {
		// TODO Auto-generated method stub
		if (searchflag) {
			startCount = 0;
			try {
				jsonObject = new JSONObject(DeviceInfo);
				String id = jsonObject.getString("DeviceID");
				int types = jsonObject.getInt("DeviceType") - 1;
				String ip = jsonObject.getString("IP");
				String name = jsonObject.getString("DeviceName");
				String mac = jsonObject.getString("Mac");
				int port = jsonObject.getInt("Port");
				int smartConnect = jsonObject.getInt("SmartConnect");
				Boolean f_fl = false;
				for (int a = 0; a < list.size(); a++) {
					if (list.get(a).getDeviceID().equals(id)) {//
						f_fl = true;
					}
				}
				if (!f_fl) {
					GateWayMsg cameraMsg = new GateWayMsg();
					cameraMsg.setDeviceName(name);
					cameraMsg.setDeviceID(id);
					cameraMsg.setDeviceType(200);
					cameraMsg.setIP(ip);
					cameraMsg.setPort(port);
					cameraMsg.setSmartConnect(smartConnect);
					cameraMsg.setMac(mac);
					list.add(cameraMsg);
				}
				// handler.sendEmptyMessage(0);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	WarnDialog warnDialog = null;

	private void startTime() {
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
							}
							if (gatewayTypeID == 1) {
								handler.sendEmptyMessage(1);
							} else if (gatewayTypeID == 2) {
								handler.sendEmptyMessage(2);
							} else if (gatewayTypeID == 3) {
								handler.sendEmptyMessage(3);
							} else if (gatewayTypeID == 200) {
								handler.sendEmptyMessage(4);
							}
						}else{
							handler.sendEmptyMessage(-1);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 0) {
				if (warnDialog == null) {

					warnDialog = new WarnDialog(activity, getString(R.string.searchdevice));
					warnDialog.setMessage(getString(R.string.searchdevice)+" 4");

					warnDialog.setSureListener(new WarnDialog.OnSureListener() {
						@Override
						public void onCancle() {
							searchflag = false;
							warnDialog.dismiss();
						}
					});
				}
				warnDialog.show();
			} else if (msg.what == 5) {
				list.addAll(MyApplication.getInstance().getApkName());
				setDeviceID();
			} else if (msg.what == 3) {
				List<Master> masterList = MasterUtils.Masters;
				for (Master master : masterList) {
					if (!master.getMc()
							.equals(SystemInfo.getMasterID(activity))) {
						GateWayMsg cameraMsg = new GateWayMsg();
						cameraMsg.setDeviceName(master.getMn());
						cameraMsg.setDeviceID(master.getMc());
						cameraMsg.setDeviceType(3);
						cameraMsg.setIP(master.getWf());
						cameraMsg.setPort(8899);
						cameraMsg.setSmartConnect(1);
						cameraMsg.setMac(master.getMc());
						list.add(cameraMsg);
					}
				}
				setDeviceID();
			}
			if (msg.what == 4) {
				setDeviceID();
			} else if (msg.what == 2) {
				List<Master> masterList = MasterUtils.Masters;
				for (Master master : masterList) {
					if (!master.getMc()
							.equals(SystemInfo.getMasterID(activity))) {
						GateWayMsg cameraMsg = new GateWayMsg();
						cameraMsg.setDeviceName(master.getMn());
						cameraMsg.setDeviceID(master.getMc());
						cameraMsg.setDeviceType(2);
						cameraMsg.setIP(master.getWf());
						cameraMsg.setPort(8899);
						cameraMsg.setSmartConnect(1);
						cameraMsg.setMac(master.getMc());
						list.add(cameraMsg);
					}
				}
				setDeviceID();
			} else if (msg.what == 1) {
				List<Master> masterList = MasterUtils.Masters;
				for (Master master : masterList) {
					if (!master.getMc()
							.equals(SystemInfo.getMasterID(activity))) {
						GateWayMsg cameraMsg = new GateWayMsg();
						cameraMsg.setDeviceName(master.getMn());
						cameraMsg.setDeviceID(master.getMc());
						cameraMsg.setDeviceType(1);
						cameraMsg.setIP(master.getWf());
						cameraMsg.setPort(8899);
						cameraMsg.setSmartConnect(1);
						cameraMsg.setMac(master.getMc());
						list.add(cameraMsg);
					}
				}
				setDeviceID();
			}else if (msg.what == 6) {
				for (SearchBean cam : cameraList) {
					GateWayMsg cameraMsg = new GateWayMsg();
					cameraMsg.setDeviceName("新未来");
					cameraMsg.setDeviceID(cam.getCamera().getCameraBean().getDevID());
					cameraMsg.setDeviceType(6);
					cameraMsg.setIP("192.168.1.1");
					cameraMsg.setPort(8899);
					cameraMsg.setSmartConnect(1);
					cameraMsg.setMac("00000000");
					list.add(cameraMsg);
				}
				setDeviceID();
			} else if(msg.what==-1){
				if(warnDialog!=null) {
				warnDialog.setMessage(getString(R.string.getdevice) + " " + (3 - startCount));
				}
			}
			super.handleMessage(msg);
		}
	};
	private void setDeviceID() {
		msgAdapter = new GateMsgAdapter(activity, list);
		gateWayListView.setAdapter(msgAdapter);
	}
	private int times = 3;
	private boolean isconfig = false;

	public void find() {
		if (gatewayTypeID == 1) {
		if (SystemInfo.getSSID(activity).startsWith("RAK")) {
			rakScanner();
		} else {
			if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT) {
				if (MockLoginNettyClient.getInstans().isConnect()) {
					try {
						MainActivity.getInstance().initSocket();
						Toast.makeText(activity, getString(R.string.connect_service), Toast.LENGTH_SHORT)
								.show();
					} catch (Exception e) {

					}
					return;
				}
			}else{
				if (MainActivity.getInstance().mainClient == null) {
					try {
						MainActivity.getInstance().initSocket();
						Toast.makeText(activity, getString(R.string.connect_service), Toast.LENGTH_SHORT)
								.show();
					}catch (Exception e){

					}
					return;
				}
			}
			if(!SystemInfo.hasWifiConnection()){
				ToastUtils.ShowError(activity, "请将小网关与手机连接到同一WIFI网络", Toast.LENGTH_SHORT,true);
				return;
			}
			MasterUtils.Masters.clear();
			String result = ISocketCode.setForwardWifiInfo(
					SystemInfo.getSSID(activity), "3");
			MainActivity.getInstance().sendCode(result);
		}
	} else if (gatewayTypeID == 2) {
			if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT) {
				if (MockLoginNettyClient.getInstans().isConnect()) {
					try {
						MainActivity.getInstance().initSocket();
						Toast.makeText(activity, getString(R.string.connect_service), Toast.LENGTH_SHORT)
								.show();
					} catch (Exception e) {
					}
					return;
				}
			}else{
				if (MainActivity.getInstance().mainClient == null) {
					try {
						MainActivity.getInstance().initSocket();
						Toast.makeText(activity, getString(R.string.connect_service), Toast.LENGTH_SHORT)
								.show();
					}catch (Exception e){

					}

					return;
				}

			}
			if(!SystemInfo.hasWifiConnection()){
				ToastUtils.ShowError(activity, "请将云知声与手机连接到同一WIFI网络", Toast.LENGTH_SHORT,true);
				return;
			}
			MasterUtils.Masters.clear();
			String result = ISocketCode.setForwardWifiInfo(
					SystemInfo.getSSID(activity), "0");
			MainActivity.getInstance().sendCode(result);
		}
		else  if (gatewayTypeID == 3) {
			{
				if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT) {
					if (MockLoginNettyClient.getInstans().isConnect()) {
						try {
							MainActivity.getInstance().initSocket();
							Toast.makeText(activity, getString(R.string.connect_service), Toast.LENGTH_SHORT)
									.show();
						} catch (Exception e) {
						}
						return;
					}
				}else{
					if (MainActivity.getInstance().mainClient == null) {
						try {
							MainActivity.getInstance().initSocket();
							Toast.makeText(activity, getString(R.string.connect_service), Toast.LENGTH_SHORT)
									.show();
						}catch (Exception e){

						}

						return;
					}

				}
				if(!SystemInfo.hasWifiConnection()){
					ToastUtils.ShowError(activity, "请将云知声与手机连接到同一WIFI网络", Toast.LENGTH_SHORT,true);
					return;
				}
				MasterUtils.Masters.clear();
				String result = ISocketCode.setForwardWifiInfo(
						SystemInfo.getSSID(activity), "4");
				MainActivity.getInstance().sendCode(result);

			}
		} else if (gatewayTypeID == 200)  {
			BridgeService.setSearchDeviceListener(this);
			DeviceSDK.searchDevice();
		}
		handler.sendEmptyMessage(0);
		searchflag = true;
		startCount = 0;
		startTime();
		list.clear();
		GateWayMsg gateWayMsg = new GateWayMsg();
		gateWayMsg.setDeviceID("00000");
		gateWayMsg.setDeviceName(getString(R.string.gateWayFind));
		list.add(gateWayMsg);
	}


	private void AddDeviceListItem(String groupname, String devicename, String deviceip, String devicemac, String devicesignal) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		GateWayMsg cameraMsg = new GateWayMsg();
		cameraMsg.setDeviceName(devicename);
		cameraMsg.setDeviceID("0000"+devicemac.replace(":", ""));
		cameraMsg.setDeviceType(1);
		cameraMsg.setIP(deviceip);
		cameraMsg.setPort(8899);
		cameraMsg.setSmartConnect(1);
		cameraMsg.setMac("0000"+devicemac.replace(":", ""));
		list.add(cameraMsg);
	}
	//@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == 0) {
//			if(data!=null){
//				String device = data.getExtras().getString("result");
//				gateWayId.setText(device);
//				gatewayTypeID = 3;
//				typeAdapter.changeSelected(gatewayTypeID);
//				userName.setText("admin");
//			}else{
//				ToastUtils.ShowError(activity,"二维码扫描失败", Toast.LENGTH_SHORT,true);
//			}
//		}
	//}
	public void rakScanner(){
		if (_scanner == null) {
			_scanner = new Scanner(activity);
			_scanner.setOnScanOverListener(new Scanner.OnScanOverListener() {
				@Override
				public void onResult(Map<InetAddress, RAKInfo> data,
									 InetAddress gatewayAddress) {
					// TODO Auto-generated method stub
					if (data != null) {
						for (Map.Entry<InetAddress, RAKInfo> entry : data
								.entrySet()) {
							if (searchflag) {
								String ip = entry.getKey().getHostAddress();
								RAKInfo _rakInfo = entry.getValue();
								AddDeviceListItem(_rakInfo.GroupName,
										_rakInfo.NickName, ip,
										_rakInfo.Mac, _rakInfo.Rssi + "");
								isconfig = true;
							}
						}
					}
					if (isconfig == false) {
						times--;
						if (searchflag && (times > 0)) {
							handler.post(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									_scanner.scanAll();
									Log.e("times==>", times + "");
								}
							});
						}
					}
				}
			});
		}
		_scanner.scanAll();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(hidden){
			TabMyActivity.getInstance().gatewayFragment.changeList();
			BridgeService.setSearchDeviceListener(null);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}
}
