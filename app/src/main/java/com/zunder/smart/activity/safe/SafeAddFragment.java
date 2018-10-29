package com.zunder.smart.activity.safe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.TabMainActivity;
import com.zunder.smart.activity.main.TabMyActivity;
import com.zunder.smart.activity.popu.dialog.ActionViewWindow;
import com.zunder.smart.activity.popu.dialog.DeviceTypeViewWindow;
import com.zunder.smart.activity.popu.dialog.IOViewWindow;
import com.zunder.smart.activity.popu.dialog.ProductViewWindow;
import com.zunder.smart.activity.popu.dialog.RoomViewWindow;
import com.zunder.smart.activity.popu.dialog.TimeViewWindow;
import com.zunder.smart.activity.popu.dialog.TwoPopupWindow;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.DeviceTypeFactory;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.dao.impl.factory.ProductFactory;
import com.zunder.smart.dao.impl.factory.RoomFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.TimeAlert;
import com.zunder.smart.json.Constants;
import com.zunder.smart.listener.DeviceAddListener;
import com.zunder.smart.listener.SafeListener;
import com.zunder.smart.model.ComModel;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.DeviceIo;
import com.zunder.smart.model.DeviceType;
import com.zunder.smart.model.Mode;
import com.zunder.smart.model.Products;
import com.zunder.smart.model.Room;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.service.aduio.AduioService;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.utils.ListNumBer;

import java.util.ArrayList;
import java.util.List;

public class SafeAddFragment extends Fragment implements OnClickListener,View.OnTouchListener,SafeListener {

	private TextView backTxt;
	ImageView studyTxt;
	private TextView editeTxt;
	private static RelativeLayout roomLayout,typeLayout,ioLayout;
	private static TextView roomTxt,typeTxt,ioTxt;
	private static String modelOpration = "Add";
	TimeAlert alert;
	static Device paramsDevice;
	public  Activity activity;
	private static int roomId = -1;
	private static int deviceTypekey =18;
	private static int productsKey = 91;
	private static String io = "0";
	private int seqencing = 0;
	private static EditText deviceId, deviceName;
	private String deviceNameStr = "";
	private static int Id;
	RoomViewWindow roomViewWindow;
	ActionViewWindow deviceTypeViewWindow;
	AlertViewWindow alertViewWindow;
//	TwoPopupWindow devicePopuWindow;
	private int safeType=0;
	private LinearLayout setLayout;

	private List<String> listIO=new ArrayList <String>();

	private CheckBox openModeWarm;
	private CheckBox closeModeWarm;
	private CheckBox modeOpen;
	private CheckBox modeClose;




	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_safe_add, container,
				false);
		activity = getActivity();

		setLayout=(LinearLayout)root.findViewById(R.id.setLayout);
		roomTxt=(TextView)root.findViewById(R.id.roomTxt);
		typeTxt=(TextView)root.findViewById(R.id.typeTxt);
		deviceName = (EditText) root.findViewById(R.id.deviceName);
		deviceId = (EditText) root.findViewById(R.id.deviceID);
		studyTxt = (ImageView) root.findViewById(R.id.studyTxt);
		backTxt = (TextView) root.findViewById(R.id.backTxt);
		editeTxt = (TextView) root.findViewById(R.id.editeTxt);
		ioTxt=(TextView)root.findViewById(R.id.ioTxt) ;
		openModeWarm = (CheckBox) root.findViewById(R.id.openModeWarm);
		closeModeWarm = (CheckBox) root.findViewById(R.id.closeModeWarm);
		modeOpen = (CheckBox) root.findViewById(R.id.modeOpen);
		modeClose = (CheckBox) root.findViewById(R.id.modeClose);

		openModeWarm.setOnClickListener(this);
		closeModeWarm.setOnClickListener(this);
		modeOpen.setOnClickListener(this);
		modeClose.setOnClickListener(this);

		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		studyTxt.setOnClickListener(this);
		roomLayout = (RelativeLayout) root.findViewById(R.id.roomLayout);
		typeLayout = (RelativeLayout) root.findViewById(R.id.typeLayout);
		ioLayout = (RelativeLayout) root.findViewById(R.id.ioLayout);
		roomLayout.setOnClickListener(this);
		typeLayout.setOnClickListener(this);
		ioLayout.setOnClickListener(this);
		root.setOnTouchListener(this);
		alert = new TimeAlert(activity);
		alert.setSureListener(new TimeAlert.OnSureListener() {

			@Override
			public void onCancle() {
				// TODO Auto-generated method stub
				alert.diss();
			}
		});
		return root;
	}
	public void iniit(int safeType,String _modelOpration,int _Id){
		this.safeType=safeType;
		modelOpration=_modelOpration;
		if(safeType==0){
			setLayout.setVisibility(View.VISIBLE);
			openModeWarm.setChecked(false);
			closeModeWarm.setChecked(false);
			modeOpen.setChecked(false);
			modeClose.setChecked(false);
		}else{
			setLayout.setVisibility(View.GONE);
		}

		if(modelOpration.equals("Add")) {
			Id=0;
			deviceName.setText("");
			deviceId.setText("");
			roomId=0;
			io="0";
			roomTxt.setText("点击选择");
			typeTxt.setText("点击选择");
			ioTxt.setText("回路1");
			ioLayout.setVisibility(View.GONE);
		}else if (modelOpration.equals("Edite")) {
			Id=_Id;
			Device device = DeviceFactory.getInstance().getDevicesById(Id);
			if (device != null) {
				Id = device.getId();
				io = device.getDeviceIO();
				deviceNameStr = device.getDeviceName();
				seqencing = device.getSeqencing();
				roomId = device.getRoomId();
				productsKey = device.getProductsKey();
				deviceName.setText(device.getDeviceName());
				deviceId.setText(device.getDeviceID());
				roomTxt.setText(device.getRoomName());
				typeTxt.setText(device.getDeviceTypeName());
				ioLayout.setVisibility(View.VISIBLE);
				ioTxt.setText("回路"+(Integer.parseInt(io)+1));
			}
		}
	}

	private static boolean startflag = false;
	private static int startCount = 0;
	private void startTime() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (startflag) {
					try {
						Thread.sleep(100);
						if (startflag) {
							if (startCount < 200) {
								startCount++;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public static IWidgetDAO sql() {
		return MyApplication.getInstance().getWidgetDataBase();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.backTxt:
				// MainActivity.getInstance().hideFragMent();
				back();
				break;
			case R.id.roomLayout:
				roomViewWindow=new RoomViewWindow(activity,"房间",0);
				roomViewWindow.setAlertViewOnCListener(new RoomViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, Room room) {
						roomId=room.getId();
						roomTxt.setText(room.getRoomName());
						roomViewWindow.dismiss();
					}

					@Override
					public void cancle() {

					}
				});
				roomViewWindow.show();
				break;
			case R.id.typeLayout: {
				deviceTypeViewWindow = new ActionViewWindow(activity, "类型", SafeUtils.getInstance().safeTypes(safeType), 0);
				deviceTypeViewWindow.setAlertViewOnCListener(new ActionViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String ItemName) {
						typeTxt.setText(ItemName);
						if(safeType==0) {
							listIO = SafeUtils.getInstance().safeIos(pos);
						}else{
							listIO = SafeUtils.getInstance().safeIos(4);
						}
						ioLayout.setVisibility(View.VISIBLE);
						ioTxt.setText(listIO.get(0).replace("设定","回路"));
						io = AppTools.getNumbers(listIO.get(0));
						deviceId.setText(AppTools.toHex(Integer.parseInt(io)-1)+"0000");
						deviceTypeViewWindow.dismiss();
					}
					@Override
					public void cancle() {

					}
				});
				deviceTypeViewWindow.show();
			}
				break;
			case R.id.ioLayout: {
				deviceTypeViewWindow = new ActionViewWindow(activity, "类型", listIO, 0);
				deviceTypeViewWindow.setAlertViewOnCListener(new ActionViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String ItemName) {
						ioTxt.setText("回路"+AppTools.getNumbers(ItemName));
						io= AppTools.getNumbers(ItemName);
						deviceId.setText(AppTools.toHex(Integer.parseInt(io)-1)+"0000");
						deviceTypeViewWindow.dismiss();
					}

					@Override
					public void cancle() {

					}
				});
				deviceTypeViewWindow.show();
			}
				break;
			case R.id.editeTxt:
				if (TextUtils.isEmpty(deviceName.getText())) {

					ToastUtils.ShowError(activity,
							getString(R.string.words_null),
							Toast.LENGTH_SHORT,true);
					return;
				}
				if (TextUtils.isEmpty(deviceId.getText())) {

					ToastUtils.ShowError(activity,
							getString(R.string.id_null),
							Toast.LENGTH_SHORT,true);
					return;
				}

				if (roomId == -1) {
					ToastUtils.ShowError(activity,
							getString(R.string.arce_null),
							Toast.LENGTH_SHORT,true);
					return;
				}
				if (deviceTypekey == -1) {

					ToastUtils.ShowError(activity,
							getString(R.string.type_null),
							Toast.LENGTH_SHORT,true);
					return;
				}
				if (productsKey == -1) {
					ToastUtils.ShowError(activity,
							getString(R.string.product_null),
							Toast.LENGTH_SHORT,true);
					return;
				}
				Device device = new Device();
				device.setId(Id);
				device.setDeviceName(deviceName.getText().toString());
				device.setDeviceID(deviceId.getText().toString());
				device.setDeviceIO((Integer.parseInt(io)-1)+"");
				device.setDeviceNickName("");
				device.setDeviceImage(null);
				device.setSeqencing(seqencing);
				device.setDeviceOnLine(1);
				device.setDeviceTimer("0");
				device.setDeviceOrdered("0");
				device.setStartTime("00:00");
				device.setEndTime("00:00");
				device.setSceneId(0);
				device.setDeviceTypeKey(18);
				device.setProductsKey(91);
				device.setGradingId(1);
				device.setRoomId(roomId);
				if (modelOpration.equals("Add")
						|| modelOpration.equals("newDevice")) {
					String resultStr=DeviceFactory.getInstance().judgeName(deviceName.getText().toString(),roomId);
					if (!resultStr.equals("0")) {
						ToastUtils.ShowError(activity,resultStr,Toast.LENGTH_SHORT,true);
					}else if(ModeFactory.getInstance().judgeName(deviceName.getText().toString()) != 0){
						ToastUtils.ShowError(activity,getString(R.string.mode_exite)+"["+deviceName.getText().toString()+"]",Toast.LENGTH_SHORT,true);
					}else{
						int result = sql().insertDevice(device);
						if (result > 1) {
							ToastUtils.ShowSuccess(activity,
									getString(R.string.addSuccess),
									Toast.LENGTH_SHORT,true);
							AddMode();
							DeviceFactory.getInstance().clearList();
							ModeFactory.getInstance().clearList();
							SendCMD._GetCmdArr.clear();
							deviceName.setText("");
							TabMainActivity.getInstance().safeFragment.init(safeType);
						}
					}
				} else if (modelOpration.equals("Edite")) {
					if (DeviceFactory.getInstance().updateName(Id, deviceName.getText()
							.toString(), deviceNameStr,roomId) != 0) {
						ToastUtils.ShowError(activity,getString(R.string.device_exite)+"["+deviceName.getText().toString()+"]",Toast.LENGTH_SHORT,true);
					}else if(ModeFactory.getInstance().judgeName(deviceName.getText().toString()) != 0) {
						ToastUtils.ShowError(activity,getString(R.string.mode_exite)+"["+deviceName.getText().toString()+"]",Toast.LENGTH_SHORT,true);
					}else{
						int num = sql().updateDevice(device);
						if (num > 0) {
							deviceNameStr = deviceName.getText().toString();
							DeviceFactory.getInstance().clearList();
							SendCMD._GetCmdArr.clear();
							ToastUtils.ShowSuccess(activity,
									getString(R.string.updateSuccess),
									Toast.LENGTH_SHORT, true);
							AddMode();
						}else{
							ToastUtils.ShowError(activity,getString(R.string.device_exite)+"["+deviceName.getText().toString()+"]",Toast.LENGTH_SHORT,true);
						}
					}
				}
				break;
			case R.id.studyTxt:
				if (TextUtils.isEmpty(deviceId.getText())) {
					ToastUtils.ShowError(activity,
							getString(R.string.id_null),
							Toast.LENGTH_SHORT,true);
					return;
				}
				final Device alrmDevice=DeviceFactory.getInstance().getGateWayDevice();
				alrmDevice.setDeviceIO((Integer.parseInt(io)-1)+"");
				alrmDevice.setDeviceID(deviceId.getText().toString());
				alrmDevice.setProductsCode("07");
				alertViewWindow=new AlertViewWindow(activity,"安防配对设置", ListNumBer.geSaOperator(),null,0);
				 alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String itemName) {
						switch (pos){
							case 0: {
								alertViewWindow.dismiss();
								TcpSender._isLearnFlag = true;
								alert.show(getString(R.string.threekey));
								SendCMD.getInstance().sendCmd(242, "1", alrmDevice);
								alert.show(getString(R.string.threekey));
							}
							break;
							case 1: {
								alertViewWindow.dismiss();
								alertViewWindow=new AlertViewWindow(activity,"设备列表", DeviceFactory.getInstance().getDeviceAnHiongName(),null,0);
								alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
									@Override
									public void onItem(int pos, String itemName) {
										final Device device=DeviceFactory.getInstance().getDevicesByName(itemName);
										List<String> list=SafeDeviceUtils.getInstance().getDeviceStrings(device.getDeviceTypeKey());
										if(list.size()>0){
											final AlertViewWindow comAlert=new AlertViewWindow(activity,device.getRoomName(),list,null,0);
											comAlert.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
												@Override
												public void onItem(int pos, String itemName) {
												ComModel comModel= SafeDeviceUtils.getInstance().getComModels(itemName);
													if(comModel!=null){
														device.setDeviceIO(comModel.getCmd());
														device.setDeviceDigtal(deviceId.getText().toString().substring(0, 2));
														SendCMD.getInstance().sendCmd(242, "7", device);
													}
													comAlert.dismiss();
												}
											});
											comAlert.show();
										}else {
											device.setDeviceDigtal(deviceId.getText().toString().substring(0, 2));
											SendCMD.getInstance().sendCmd(242, "7", device);
										}
										alertViewWindow.dismiss();
									}
								});
								alertViewWindow.show();
							}
							break;
						}

					}
				});
				alertViewWindow.show();
				break;
		default:
			break;
		}
	}

	public void AddMode(){

		int modeLoop=0;
		int IO=Integer.valueOf(deviceId.getText().toString().substring(0,2),16);

		if(IO<5){
			modeLoop=IO;
		}else if(IO<9){
			modeLoop=5;
		}else if(IO<25){
			modeLoop=0;
		}else if(IO<29){
			modeLoop=6;
		}else {
			modeLoop=7;
		}
		if(openModeWarm.isChecked()){
			Mode mode=new Mode();
			mode.setModeName(251+"_"+modeLoop+"开启报警情景");
			mode.setModeImage(Constants.MODEIMAGEPATH);
			mode.setModeType("FF");
			mode.setSeqencing(0);
			mode.setStartTime("00:00");
			mode.setEndTime("00:00");
			mode.setModeLoop(modeLoop);
			mode.setModeCode(251);
			mode.setModeNickName("");
			if(ModeFactory.getInstance().isExitode(251,modeLoop)==0) {
				int result = sql().insertMode(mode);
			}
		}
		if(closeModeWarm.isChecked()){
			Mode mode=new Mode();
			mode.setModeName(250+"_"+modeLoop+"关闭报警情景");
			mode.setModeImage(Constants.MODEIMAGEPATH);
			mode.setModeType("FF");
			mode.setSeqencing(0);
			mode.setStartTime("00:00");
			mode.setEndTime("00:00");
			mode.setModeLoop(modeLoop);
			mode.setModeCode(250);
			mode.setModeNickName("");
			if(ModeFactory.getInstance().isExitode(250,modeLoop)==0) {
				int result = sql().insertMode(mode);
			}
		}
		if(modeOpen.isChecked()){
			Mode mode=new Mode();
			mode.setModeName((IO+200)+"_0"+"开启联动情景");
			mode.setModeImage(Constants.MODEIMAGEPATH);
			mode.setModeType("FF");
			mode.setSeqencing(0);
			mode.setStartTime("00:00");
			mode.setEndTime("00:00");
			mode.setModeLoop(0);
			mode.setModeCode(IO+200);
			mode.setModeNickName("");
			if(ModeFactory.getInstance().isExitode(IO+200,0)==0) {
				int result = sql().insertMode(mode);
			}
		}
		if(modeClose.isChecked()) {
			Mode mode = new Mode();
			mode.setModeName((IO+200)+"_7"+"关闭联动情景");
			mode.setModeImage(Constants.MODEIMAGEPATH);
			mode.setModeType("FF");
			mode.setSeqencing(0);
			mode.setStartTime("00:00");
			mode.setEndTime("00:00");
			mode.setModeLoop(7);
			mode.setModeCode(IO + 200);
			mode.setModeNickName("");
			if (ModeFactory.getInstance().isExitode(200+IO, 7) == 0) {
				int result = sql().insertMode(mode);
			}
		}
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String blacCode = msg.obj.toString();
				String deviceId = blacCode.substring(8, 14);
				if (alert.isShow()) {
					Toast.makeText(activity, getString(R.string.match_success),
							Toast.LENGTH_SHORT).show();
					alert.diss();
				}

				break;
			}
		}
	};


	public void back() {
		DialogAlert alert = new DialogAlert(activity);
		alert.init(getString(R.string.tip), getString(R.string.is_edite));
		alert.setSureListener(new DialogAlert.OnSureListener() {
			@Override
			public void onSure() {
				// TODO Auto-generated method stub
				TabMainActivity.getInstance().hideFragMent(8);
			}
			@Override
			public void onCancle() {
				// TODO Auto-generated method stub

			}
		});
		alert.show();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(hidden){
			modelOpration="Add";
			TcpSender.setSafeListener(null);
		}else{
			TcpSender.setSafeListener(this);
		}
		onDialogDis();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}
	public void onDialogDis(){
		if(roomViewWindow!=null&&roomViewWindow.isShow()){
			roomViewWindow.dismiss();
		}
		if(deviceTypeViewWindow!=null&&deviceTypeViewWindow.isShow()){
			deviceTypeViewWindow.dismiss();
		}
		if(alertViewWindow!=null&&alertViewWindow.isShow()){
			alertViewWindow.dismiss();
		}

	}

	@Override
	public void setBackCode(String cmd) {
		if (deviceTypekey == 18) {
			Message msg = handler.obtainMessage();
			msg.what = 1;
			msg.obj = cmd;
			handler.sendMessage(msg);
		}
	}
}
