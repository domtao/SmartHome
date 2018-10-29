package com.zunder.smart.activity.device;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.activity.main.TabHomeActivity;
import com.zunder.smart.activity.main.TabMainActivity;
import com.zunder.smart.activity.main.TabModeActivity;
import com.zunder.smart.activity.main.TabMyActivity;
import com.zunder.smart.activity.popu.dialog.DeviceTypeViewWindow;
import com.zunder.smart.activity.popu.dialog.IOViewWindow;
import com.zunder.smart.activity.popu.dialog.ProductViewWindow;
import com.zunder.smart.activity.popu.dialog.RoomViewWindow;
import com.zunder.smart.activity.popu.dialog.TimeViewWindow;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.factory.ModeListFactory;
import com.zunder.smart.dao.impl.factory.RedInfraFactory;
import com.zunder.smart.dao.impl.factory.RoomFactory;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.DeviceTypeFactory;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.dao.impl.factory.ProductFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.TimeAlert;
import com.zunder.smart.listener.DeviceAddListener;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.DeviceIo;
import com.zunder.smart.model.DeviceType;
import com.zunder.smart.model.ModeList;
import com.zunder.smart.model.Products;
import com.zunder.smart.model.RedInfra;
import com.zunder.smart.model.Room;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.service.aduio.AduioService;
import com.zunder.smart.tools.AppTools;

import java.util.List;

public class DeviceAddFragment extends Fragment implements OnClickListener,
		DeviceAddListener ,View.OnTouchListener{

	private TextView backTxt;
	ImageView studyTxt;
	private TextView editeTxt;
	private static RelativeLayout roomLayout,typeLayout,productLayout;
	private static TextView roomTxt,typeTxt,productTxt,ioTxt;
	static RelativeLayout  ioLayout;
	private static String modelOpration = "Add";
	TimeAlert alert;
	static Device paramsDevice;
	RelativeLayout startTime;
	TextView timeTxt;
	public  Activity activity;
	private static int roomId = -1;
	private static int deviceTypekey = -1;
	private static int productsKey = -1;
	private static String io = "0";
	private int seqencing = 0;
	private static EditText deviceId, deviceName, nickName;
	private String deviceNameStr = "";
	private Button audioBtn;
	private static int Id;
	private int sendId = 0;
	ImageButton deviceNameAudio;
	RoomViewWindow roomViewWindow;
	DeviceTypeViewWindow deviceTypeViewWindow;
	ProductViewWindow productViewWindow;
	IOViewWindow ioViewWindow;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_device_add, container,
				false);
		activity = getActivity();
		TcpSender.setDeviceAddListener(this);

		roomTxt=(TextView)root.findViewById(R.id.roomTxt);
		typeTxt=(TextView)root.findViewById(R.id.typeTxt);
		deviceName = (EditText) root.findViewById(R.id.deviceName);
		deviceId = (EditText) root.findViewById(R.id.deviceID);
		nickName = (EditText) root.findViewById(R.id.nickName);
		studyTxt = (ImageView) root.findViewById(R.id.studyTxt);
		backTxt = (TextView) root.findViewById(R.id.backTxt);
		editeTxt = (TextView) root.findViewById(R.id.editeTxt);
		timeTxt=(TextView)root.findViewById(R.id.timeTxt);
		ioTxt=(TextView)root.findViewById(R.id.ioTxt) ;
		productTxt = (TextView) root.findViewById(R.id.productTxt);
		deviceNameAudio = (ImageButton) root.findViewById(R.id.deviceNameAudio);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		studyTxt.setOnClickListener(this);
		roomLayout = (RelativeLayout) root.findViewById(R.id.roomLayout);
		typeLayout = (RelativeLayout) root.findViewById(R.id.typeLayout);
		productLayout = (RelativeLayout) root.findViewById(R.id.productLayout);
		ioLayout = (RelativeLayout) root.findViewById(R.id.ioLayout);
		roomLayout.setOnClickListener(this);
		typeLayout.setOnClickListener(this);
		productLayout.setOnClickListener(this);
		ioLayout.setOnClickListener(this);
		root.setOnTouchListener(this);
		audioBtn = (Button) root.findViewById(R.id.audio);
		startTime = (RelativeLayout) root.findViewById(R.id.startTime);
		startTime.setOnClickListener(this);
		deviceNameAudio.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deviceName.requestFocus();
				AduioService aduioService= AduioService.getInstance();
				aduioService.setEditDevice(activity,deviceName,deviceName.getText().toString());
			}
		});
		audioBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				nickName.requestFocus();
				AduioService aduioService= AduioService.getInstance();
				aduioService.setEditDevice(activity,nickName,nickName.getText().toString());
			}
		});
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

	public void setAdpapter(String _modelOpration,int _Id,Device _device) {
		modelOpration=_modelOpration;
		if (modelOpration.equals("Add")) {
			seqencing = DeviceFactory.getInstance().getAll().size() + 1;
			productLayout.setVisibility(View.GONE);
			ioLayout.setVisibility(View.GONE);
			io = "0";
			roomId = -1;
			deviceTypekey = -1;
			productsKey = -1;
			deviceName.setText("");
			deviceId.setText("");
			nickName.setText("");
			Id = 0;
			timeTxt.setText("00:00~00:00");
			roomTxt.setText("点击选择");
			typeTxt.setText("点击选择");
			Device newDev=DeviceFactory.getInstance().getNewDevice();
			if(newDev!=null){
			io = newDev.getDeviceIO();
			deviceNameStr = newDev.getDeviceName();
			seqencing = newDev.getSeqencing();
			roomId = newDev.getRoomId();
			deviceTypekey = newDev.getDeviceTypeKey();
			sendId = newDev.getSceneId();
			productsKey = newDev.getProductsKey();
			deviceId.setText(newDev.getDeviceID());
			timeTxt.setText(newDev.getStartTime()+"~"+newDev.getEndTime());
			nickName.setText(newDev.getDeviceNickName());
			roomTxt.setText(newDev.getRoomName());
			typeTxt.setText(newDev.getDeviceTypeName());
			productTxt.setText(newDev.getProductsName());
			if (newDev.getDeviceTypeClick()>0&&deviceTypekey!=-1) {
				productLayout.setVisibility(View.VISIBLE);
			} else {
				productLayout.setVisibility(View.GONE);
			}
			if (productsKey != -1 && newDev.getProductsIO() > 0) {
				ioLayout.setVisibility(View.VISIBLE);
				ioTxt.setText("回路"+(Integer.parseInt(io)+1));
			} else {
				ioLayout.setVisibility(View.GONE);
			}
		}
		} else if (modelOpration.equals("Edite")) {
			Id=_Id;
			Device device = DeviceFactory.getInstance().getDevicesById(Id);
			if (device != null) {
				Id = device.getId();
				io = device.getDeviceIO();
				deviceNameStr = device.getDeviceName();
				seqencing = device.getSeqencing();
				roomId = device.getRoomId();
				deviceTypekey = device.getDeviceTypeKey();
				sendId = device.getSceneId();
				productsKey = device.getProductsKey();
				deviceName.setText(device.getDeviceName());
				deviceId.setText(device.getDeviceID());
				timeTxt.setText(device.getStartTime()+"~"+device.getEndTime());
				nickName.setText(device.getDeviceNickName());

				roomTxt.setText(device.getRoomName());
				typeTxt.setText(device.getDeviceTypeName());
				productTxt.setText(device.getProductsName());
				if (device.getDeviceTypeClick()>0&&deviceTypekey!=-1) {
					productLayout.setVisibility(View.VISIBLE);
				} else {
					productLayout.setVisibility(View.GONE);
				}
				if (productsKey != -1 && device.getProductsIO() > 0) {
					ioLayout.setVisibility(View.VISIBLE);
					ioTxt.setText("回路"+(Integer.parseInt(io)+1));
				} else {
					ioLayout.setVisibility(View.GONE);
				}
			}
		} else if (modelOpration.equals("newDevice")) {
			paramsDevice=_device;
			if (paramsDevice != null) {
				io = paramsDevice.getDeviceIO();
				roomId = 1;
				deviceTypekey = paramsDevice.getDeviceTypeKey();
				productsKey = paramsDevice.getProductsKey();
				deviceName.setText("");
				deviceId.setText(paramsDevice.getDeviceID());
				roomTxt.setText(RoomFactory.getInstance().getRoomById(roomId));
				typeTxt.setText(DeviceTypeFactory.getInstance().getDeviceTypeName(deviceTypekey));
				if (deviceTypekey != -1) {
					productLayout.setVisibility(View.VISIBLE);
					productTxt.setText(ProductFactory.getProductNameByKey(productsKey));
				} else {
					productLayout.setVisibility(View.GONE);
				}
				if (productsKey != -1 && paramsDevice.getProductsIO() > 0) {
					ioLayout.setVisibility(View.VISIBLE);
					ioTxt.setText("回路"+(Integer.parseInt(io)+1));
				} else {
					ioLayout.setVisibility(View.GONE);
				}
			}
		}
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
			case R.id.typeLayout:
				deviceTypeViewWindow=new DeviceTypeViewWindow(activity,"类型",deviceTypekey-1);
				deviceTypeViewWindow.setAlertViewOnCListener(new DeviceTypeViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, DeviceType deviceType) {
						deviceTypekey=deviceType.getDeviceTypeKey();
						typeTxt.setText(deviceType.getDeviceTypeName());
						io="0";
						List<Products> list=ProductFactory.getProducts(deviceTypekey);
						if(list.size()>1){
							productLayout.setVisibility(View.VISIBLE);
							productTxt.setText(list.get(0).getProductsName());
							productsKey=list.get(0).getProductsKey();
							if(list.get(0).getProductsIO()>1){
								ioLayout.setVisibility(View.VISIBLE);
							}
						}else{
							productLayout.setVisibility(View.GONE);
							Products products=list.get(0);
							productsKey=products.getProductsKey();
							if(products.getProductsIO()>1){
								ioLayout.setVisibility(View.VISIBLE);

							}else{
								ioLayout.setVisibility(View.GONE);

							}
						}
						deviceTypeViewWindow.dismiss();
					}
					@Override
					public void cancle() {
					}
				});
				deviceTypeViewWindow.show();
				break;
			case R.id.productLayout:
				if(deviceTypekey==4||deviceTypekey==5){
					if (TextUtils.isEmpty(deviceId.getText())) {

						ToastUtils.ShowError(activity,
								getString(R.string.id_null),
								Toast.LENGTH_SHORT,true);
						return;
					}
					Device device=new Device();
					device.setDeviceID(deviceId.getText().toString());
					ProductsActivity.startActivity(activity,deviceTypekey,device);
				}else {
					productViewWindow = new ProductViewWindow(activity, "产品", deviceTypekey);
					productViewWindow.setAlertViewOnCListener(new ProductViewWindow.AlertViewOnCListener() {
						@Override
						public void onItem(int pos, Products products) {
							productTxt.setText(products.getProductsName());
							io = "0";
							productsKey = products.getProductsKey();
							if (products.getProductsIO() > 1) {
								ioLayout.setVisibility(View.VISIBLE);
							} else {
								ioLayout.setVisibility(View.GONE);
							}
							productViewWindow.dismiss();
						}

						@Override
						public void cancle() {

						}
					});
					productViewWindow.show();
				}
				break;
			case R.id.ioLayout:
				ioViewWindow=new IOViewWindow(activity,"回路",ProductFactory.getProduct(productsKey).getProductsIO(),0);
				ioViewWindow.setAlertViewOnCListener(new IOViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, DeviceIo deviceIo) {
						io=pos+"";
						ioTxt.setText(deviceIo.getIoName());
						ioViewWindow.dismiss();
					}

					@Override
					public void cancle() {

					}
				});
				ioViewWindow.show();
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
				device.setDeviceIO(io);
				device.setDeviceNickName(nickName.getText().toString()
						.replace("，", ","));
				device.setDeviceImage("");
				device.setSeqencing(seqencing);
				device.setDeviceOnLine(1);
				device.setDeviceTimer("0");
				device.setDeviceOrdered("0");
				String time=timeTxt.getText().toString().replace("--","~");
				String[] times=time.split("~");
				device.setStartTime(times[0]);
				device.setEndTime(times[1]);
				device.setSceneId(sendId);
				device.setDeviceTypeKey(deviceTypekey);
				device.setProductsKey(productsKey);
				device.setGradingId(1);
				device.setRoomId(roomId);
				device.setRoomName(roomTxt.getText().toString());
				device.setDeviceTypeName(typeTxt.getText().toString());
				device.setProductsName(productTxt.getText().toString());
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
							DeviceFactory.getInstance().setNewDevice(device);
							ToastUtils.ShowSuccess(activity,
									getString(R.string.addSuccess),
									Toast.LENGTH_SHORT,true);
							DeviceFactory.getInstance().clearList();
							ModeListFactory.getInstance().clearList();
							SendCMD._GetCmdArr.clear();
							deviceName.setText("");
							nickName.setText("");
							SendCMD.getInstance().sendCMD(254, "11:01:07:0"+device.getDeviceIO(), device);
							if(TabHomeActivity.getInstance()!=null) {
								TabHomeActivity.getInstance().RoomChange();
							}
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
							ModeListFactory.getInstance().clearList();
							SendCMD._GetCmdArr.clear();
							//红外设备
							if(device.getDeviceTypeKey()==6){
								List<RedInfra> list = RedInfraFactory.getInstance().getInfraById(device
										.getId());
								for (RedInfra infrared : list) {
									infrared.setDeviceId(device.getDeviceID());
									MyApplication.getInstance().getWidgetDataBase()
											.updateRedFra(infrared);
								}
							}
							ToastUtils.ShowSuccess(activity,
									getString(R.string.updateSuccess),
									Toast.LENGTH_SHORT, true);
							if(TabHomeActivity.getInstance()!=null) {
								TabHomeActivity.getInstance().RoomChange();
							}
						}else{
							ToastUtils.ShowError(activity,getString(R.string.device_exite)+"["+deviceName.getText().toString()+"]",Toast.LENGTH_SHORT,true);
						}
					}
				}
				break;
			case R.id.startTime:
//				openPopWindow();
				final TimeViewWindow timeViewWindow=new TimeViewWindow(activity);
				timeViewWindow.setAlertViewOnCListener(new TimeViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String itemName) {
						timeTxt.setText(itemName);
						timeViewWindow.dismiss();
					}

					@Override
					public void cancle() {

					}
				});
				timeViewWindow.show();
						break;
			case R.id.studyTxt:
				TcpSender._isLearnFlag = true;
				if(alert==null){
					alert = new TimeAlert(activity);
					alert.setSureListener(new TimeAlert.OnSureListener() {

						@Override
						public void onCancle() {
							// TODO Auto-generated method stub
							alert.diss();
						}
					});
				}
				alert.show(getString(R.string.threekey));
				break;
		default:
			break;
		}
	}
	public void setproduct(Products products){
		productTxt.setText(products.getProductsName());
		io = "0";
		productsKey = products.getProductsKey();
		if (products.getProductsIO() > 1) {
			ioLayout.setVisibility(View.VISIBLE);
		} else {
			ioLayout.setVisibility(View.GONE);
		}
	}

	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Device device = (Device) msg.obj;
				io = device.getDeviceIO();
				deviceTypekey = device.getDeviceTypeKey();
				productsKey = device.getProductsKey();;
				deviceId.setText(device.getDeviceID());
				typeTxt.setText(DeviceTypeFactory.getInstance().getDeviceTypeName(deviceTypekey));
				if (deviceTypekey != -1) {
					productLayout.setVisibility(View.VISIBLE);
					productTxt.setText(ProductFactory.getProductNameByKey(productsKey));
				} else {
					productLayout.setVisibility(View.GONE);
				}
				if (productsKey != -1 && device.getProductsIO() > 0) {
					ioLayout.setVisibility(View.VISIBLE);
					ioTxt.setText("回路"+(Integer.parseInt(io)+1));
				} else {
					ioLayout.setVisibility(View.GONE);
				}
				alert.diss();
				break;
			case 1:
				String blacCode = msg.obj.toString();
				String deviceId = blacCode.substring(8, 14);
				break;
			}
		}
	};

	public void setLearnDevice(Device device) {

		if (device != null) {

			Message msg = handler.obtainMessage();
			msg.what = 0;
			msg.obj = device;
			handler.sendMessage(msg);

		}
	}

	public void setAlarmDevice(String blaCode) {
		if (deviceTypekey == 7) {
			Message msg = handler.obtainMessage();
			msg.what = 1;
			msg.obj = blaCode;
			handler.sendMessage(msg);
		}
	}

	public void back() {
		DialogAlert alert = new DialogAlert(activity);
		alert.init(getString(R.string.tip), getString(R.string.is_edite));
		alert.setSureListener(new DialogAlert.OnSureListener() {
			@Override
			public void onSure() {
				// TODO Auto-generated method stub
				TabMyActivity.getInstance().hideFragMent(-1);
				TcpSender.setDeviceAddListener(null);
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
			TcpSender.setDeviceAddListener(null);
			TabMyActivity.getInstance().deviceFragment.initAdapter();
		}else{
			TcpSender.setDeviceAddListener(this);
		}
		onDialogDis();
	}

	public static void addNew(Device device) {
		paramsDevice = device;
		if (paramsDevice != null) {
			io = paramsDevice.getDeviceIO();

			deviceTypekey = paramsDevice.getDeviceTypeKey();
			productsKey = paramsDevice.getProductsKey();

			deviceId.setText(paramsDevice.getDeviceID());
			typeTxt.setText(DeviceTypeFactory.getInstance().getDeviceTypeName(deviceTypekey));
			if (deviceTypekey != -1) {
				productLayout.setVisibility(View.VISIBLE);
				productTxt.setText(ProductFactory.getProductNameByKey(productsKey));
			} else {
				productLayout.setVisibility(View.GONE);
			}
			if (productsKey != -1 && paramsDevice.getProductsIO() > 0) {
				ioLayout.setVisibility(View.VISIBLE);
				io = paramsDevice.getDeviceIO();
				ioTxt.setText("回路"+(Integer.parseInt(io)+1));
			} else {
				ioLayout.setVisibility(View.GONE);
			}
		}
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
		if(productViewWindow!=null&&productViewWindow.isShow()){
			productViewWindow.dismiss();
		}
		if(ioViewWindow!=null&&ioViewWindow.isShow()){
			ioViewWindow.dismiss();
		}
	}
}
