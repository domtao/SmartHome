package com.zunder.smart.activity.sub;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.iflytek.cloud.thirdparty.V;
import com.zunder.control.ButtonBean;
import com.zunder.control.ButtonInfo;
import com.zunder.control.ButtonUtils;
import com.zunder.control.SubButtonUtils;
import com.zunder.scrollview.widget.WheelAdapter;
import com.zunder.scrollview.widget.WheelView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.activity.popu.dialog.ActionViewWindow;
import com.zunder.smart.activity.sub.set.DeviceTimerActivity;
import com.zunder.smart.activity.sub.set.MoreActivity;
import com.zunder.smart.activity.timmer.AirTimer;
import com.zunder.smart.activity.timmer.CurTimer;
import com.zunder.smart.activity.timmer.DimmerTimer;
import com.zunder.smart.activity.timmer.EmptyTimer;
import com.zunder.smart.activity.timmer.LockTimer;
import com.zunder.smart.activity.timmer.MusicTimer;
import com.zunder.smart.activity.timmer.NewWinTimer;
import com.zunder.smart.activity.timmer.PeopleTimer;
import com.zunder.smart.activity.timmer.ProjectorTimer;
import com.zunder.smart.activity.timmer.SensorTimer;
import com.zunder.smart.activity.timmer.SeriesTimer;
import com.zunder.smart.activity.timmer.TvTimer;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dialog.EditTxtAlert;
import com.zunder.smart.listener.DeviceStateListener;
import com.zunder.smart.listener.ElectricListeener;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.AirSwitchAlia;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.DeviceTypeParam;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.service.GateWayService;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.utils.ListNumBer;

import java.util.ArrayList;
import java.util.List;

public class SubActivity extends Activity implements OnClickListener,
        DeviceStateListener,ElectricListeener {
	private Activity activity;
	private static int Id = 0;
	static Device deviceParams;
	TextView titleTxt, backTxt, editeTxt;
	String deviceName = "";

	private WheelView wvTime, wvUtil;
	private ImageView stateImage;

	private RightMenu rightButtonMenuAlert;
	public static void startActivity(Activity activity, int _id) {
		Id = _id;
		Intent intent = new Intent(activity, SubActivity.class);
		activity.startActivityForResult(intent,100);
	}

	private LinearLayout mainLayout;
	private TextView timeTxt;
	private LinearLayout timeLayout;
	private TextView msgTxt;
	private Button timeSure;
	private List<ButtonInfo> list;
	private int[] stateImages;
	private int[] imageIndexs;
	String HourStr="0";
	String HourUnit="小时";
	private TextView peopleTxt;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub);
		activity = this;
		deviceParams = DeviceFactory.getInstance().getDevicesById(Id);
		mainLayout = (LinearLayout) findViewById(R.id.main_layout);
		backTxt = (TextView) findViewById(R.id.backTxt);
		titleTxt = (TextView) findViewById(R.id.titleTxt);
		editeTxt = (TextView) findViewById(R.id.editeTxt);
		timeTxt = (TextView) findViewById(R.id.timeTxt);
		timeLayout = (LinearLayout) findViewById(R.id.timeLayout);
		msgTxt = (TextView) findViewById(R.id.msgTxt);
		timeSure = (Button) findViewById(R.id.timeSure);
		wvTime=(WheelView)findViewById(R.id.wvTime) ;
		wvUtil=(WheelView)findViewById(R.id.wvUtil) ;
		stateImage=(ImageView)findViewById(R.id.stateImage);
		peopleTxt=(TextView)findViewById(R.id.peopleTxt);
		stateImage.setOnClickListener(this);
		List<String> list=ListNumBer.getNumbers();
		WheelAdapter adapter=new WheelAdapter(list);
		wvTime.setAdapter(adapter);
		List<String> listUnits=ListNumBer.getUnits();
		WheelAdapter unitsAdapter=new WheelAdapter(listUnits);

		wvUtil.setAdapter(unitsAdapter);
		timeSure.setOnClickListener(this);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		deviceName = deviceParams.getDeviceName();
		titleTxt.setText(deviceParams.getRoomName()+deviceName);
		initRightButtonMenuAlert();
		init();
		wvTime.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(int index,String itemName) {
				if (index== 0) {
					timeLayout.setVisibility(View.GONE);
				} else {
					timeLayout.setVisibility(View.VISIBLE);
				}
				HourStr=itemName;
			}
		});
		wvUtil.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(int index,String itemName) {
				HourUnit=itemName;
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		TcpSender.setDeviceStateListener(this);
		TcpSender.setElectricListeener(this);
		SendCMD.getInstance().sendCMD(255,"1",deviceParams);
	}

	void init(){
		ButtonBean buttonBean = SubButtonUtils.getInstance().getButtonBean(deviceParams.getDeviceTypeKey());
		if (buttonBean != null) {
			list = buttonBean.getList();
			stateImages = buttonBean.getState();
			imageIndexs = buttonBean.getImageIndexs();
			initButtons();
		}
		if(stateImages!=null&&stateImages.length>1){
			stateImage.setImageResource(stateImages[deviceParams.getState()>0?1:0]);
		}


		if(deviceParams.getDeviceTypeKey()==16){
			peopleTxt.setVisibility(View.VISIBLE);
			int product = Integer.parseInt(deviceParams.getProductsCode(), 16);
			if(product!=34){
				stateImage.setImageResource(stateImages[2]);
			}
		}else if(deviceParams.getDeviceTypeKey()==17){
			peopleTxt.setVisibility(View.VISIBLE);
		}else{
			peopleTxt.setVisibility(View.GONE);
		}


		//else if(deviceParams.getDeviceTypeKey()==16){
		//	peopleTxt.setVisibility(View.VISIBLE);
		//	peopleTxt.setText("35");
		//}
	}
	//joe 控件赋值
	public void initButtons() {
	    //joe 动态添加控件
        int ccount=list.size();
        int group=ccount/3;
        if((ccount%3)!=0){
            group+=1;
        }
		for(int i=0;i<group;i++){
			LinearLayout linearLayout=new LinearLayout(activity);
			linearLayout.setOrientation(LinearLayout.HORIZONTAL);
			LinearLayout.LayoutParams layout= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layout.height=150;
            layout.gravity = Gravity.TOP;
            layout.setMargins(10,10,10,10);
			linearLayout.setLayoutParams(layout);
			for(int j=0;j<3;j++){
				Button button=new Button(activity);
				ButtonInfo buttonInfo=list.get(i*3+j);
				button.setTag(buttonInfo.getId()+"");
				button.setText(buttonInfo.getBtnTitle());
				button.setTextColor(getResources().getColor(R.color.font_open_press));
				button.setBackgroundResource(R.drawable.button_boder_shape);
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						//joe 0823 发码
						ButtonInfo info=SubButtonUtils.getInstance().getButtonInfo(list,Integer.parseInt(((Button)v).getTag().toString()));
						if(info!=null){
							SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+deviceParams.getDeviceName() + info.getCMD(), deviceParams);
							ToastUtils.ShowSuccess(activity,deviceParams.getRoomName()+deviceParams.getDeviceName() + info.getCMD(),Toast.LENGTH_SHORT,true);
						}
						playKeytone();
					}
				});
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
				params.weight = 1.0f;
				params.setMargins(20,20,20,20);
				button.setLayoutParams(params);
				linearLayout.addView(button);
			}
			mainLayout.addView(linearLayout);
		}
	}
	private void playKeytone() {
		MediaPlayer.create(activity, R.raw.ping_short).start();
	}
	private void initRightButtonMenuAlert() {
		if(deviceParams.getDeviceTypeKey()==2) {
			rightButtonMenuAlert = new RightMenu(activity, R.array.right_cur,
					R.drawable.right_airswitch_images);
		}else{
			rightButtonMenuAlert = new RightMenu(activity, R.array.right_sub,
					R.drawable.right_cur_images);
		}
		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {

			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub
				switch (position) {
					case 0:
						DeviceTimerActivity.startActivity(activity,deviceParams.getId());
						break;
					case 1:
						if(deviceParams.getDeviceTypeKey()==2) {
							SendCMD.getInstance().sendCMD(253, "05:00", deviceParams);
							isCur = 0;
						}else{
							MoreActivity.startActivity(activity,deviceParams.getId());
						}
					break;
					case 2: {
						final AlertViewWindow alertViewWindow = new AlertViewWindow(activity, "电机转向", ListNumBer.getMotor(), deviceName, 0);
						alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
							@Override
							public void onItem(int pos, String itemName) {
								//joe 0824 窗帘翻转
								String hexString=(pos==0)?"11:00:03:03":"11:01:03:03";
								SendCMD.getInstance().sendCMD(254,hexString,deviceParams);
								alertViewWindow.dismiss();
							}
						});
						alertViewWindow.show();
					}
						break;
					case 3:
						SendCMD.getInstance().sendCMD(250,"无线配对",deviceParams);
						break;
					case 4:
						MoreActivity.startActivity(activity,deviceParams.getId());
						break;
				}
				rightButtonMenuAlert.dismiss();
			}
		});
	}

	public void back(){
		TcpSender.setElectricListeener(null);
		Intent resultIntent = new Intent();
		setResult(0, resultIntent);
		finish();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.stateImage:
				SendCMD.getInstance().sendCMD(0, deviceParams.getRoomName()+deviceName + "互控", null);
				break;
			case R.id.backTxt:
				back();
				break;
			case R.id.editeTxt:
				rightButtonMenuAlert.show(editeTxt);
				break;
			case R.id.timeSure: {
				//joe 0823 子页面打开定时命令
				SendCMD.getInstance().sendCMD(0, deviceParams.getRoomName()+deviceName + getString(R.string.open_1)+HourStr+HourUnit, null);
				ToastUtils.ShowSuccess(activity,deviceParams.getRoomName()+deviceName + getString(R.string.open_1)+HourStr+HourUnit,Toast.LENGTH_SHORT,true);
			}
			break;
			default:
				break;
		}
		playKeytone();
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

	private  Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (stateImages != null&&stateImages.length>1) {
					stateImage.setImageResource(stateImages[deviceParams.getState()>0?1:0]);
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
		//joe 子页面回码处理
		if(deviceParams!=null) {
			if (deviceParams.getDeviceTypeKey() == 2) {//joe 窗帘
				CurTimer.getInstance().setBackeCode(deviceParams, onCurIndexSureListener);
			}else if(deviceParams.getDeviceTypeKey()==10){//joe 门锁
				LockTimer.getInstance().setBackeCode(deviceParams,onLockIndexSureListener);
			}else if(deviceParams.getDeviceTypeKey()==11){//joe 投影机
				ProjectorTimer.getInstance().setBackeCode(deviceParams,onProjectorIndexSureListener);
			}else if(deviceParams.getDeviceTypeKey()==12){//joe 空开
				EmptyTimer.getInstance().setBackeCode(deviceParams,onEmptyIndexSureListener);
			}else if(deviceParams.getDeviceTypeKey()==13){//joe 时序
				SeriesTimer.getInstance().setBackeCode(deviceParams,onSeriesIndexSureListener);
			}else if (deviceParams.getDeviceTypeKey() == 16) {//joe 传感器
				SensorTimer.getInstance().setBackeCode(deviceParams,onSensorIndexSureListener);
			} else if (deviceParams.getDeviceTypeKey() == 17) {//joe 人员计数
				PeopleTimer.getInstance().setBackeCode(deviceParams,onPeopleIndexSureListener);
			}
			else {
				//灯光 插座
				handler.sendEmptyMessage(1);
			}
		}
	}
	 AlertViewWindow alertViewWindow=null;
	private int isCur=-1;
	//joe 0823 窗帘回码
	private CurTimer.OnCurIndexSureListener onCurIndexSureListener=new CurTimer.OnCurIndexSureListener(){

		@Override
		public void onState(int index) {
			//joe index 窗帘进度
			if (deviceParams != null&&deviceParams.getDeviceTypeKey() == 2) {
					stateImage.setImageResource(imageIndexs[index]);
			}
		}

		@Override
		public void onCycle(int cycleIndex, int isSuccess,int processState) {
			if (isCur ==0) {
				if(processState==1) {
					if (cycleIndex >= 5) {
						if (alertViewWindow == null) {
							alertViewWindow = new AlertViewWindow(activity, "行程设定", ListNumBer.getXinChen(5, 121, "秒钟"), "当前行程" + cycleIndex + "秒钟", cycleIndex - 5);
							alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
								@Override
								public void onItem(int pos, String itemName) {
									//joe 0824 窗帘行程
									isCur = 1;
									String cmdString = AppTools.getNumbers(itemName);
									SendCMD.getInstance().sendCMD(254, "0A:10:05:" + AppTools.toHex(Integer.parseInt(cmdString)), deviceParams);
									alertViewWindow.dismiss();
								}
							});
						} else {
							alertViewWindow.setSelection("当前行程" + cycleIndex + "秒钟", cycleIndex - 5);
						}
						alertViewWindow.show();
					}
				}
			}else{
				if((isCur==1)&&(processState==0)) {
					ToastUtils.ShowSuccess(activity, "行程已设定" + cycleIndex + "秒钟", Toast.LENGTH_SHORT, true);
				}
				isCur=2;
			}
		}
	};


	//joe 门锁回码
	private LockTimer.OnLockIndexSureListener onLockIndexSureListener=new LockTimer.OnLockIndexSureListener() {
		@Override
		public void onState(int powerState,int imageIndex) {
			//joe powerState 门锁状态 0:关门  1:开门
			// imageIndex  图片状态 0:关门  1:开门
			if (deviceParams != null&&deviceParams.getDeviceTypeKey() == 10) {

			}
		}
	};
	// joe 0823投影机
	private ProjectorTimer.OnProjectorIndexSureListener onProjectorIndexSureListener=new ProjectorTimer.OnProjectorIndexSureListener() {
		@Override
		public void onState(int powerState) {
			//joe powerState 电源状态 0:关   1:开
			if (deviceParams != null && deviceParams.getDeviceTypeKey() == 11) {
				if (powerState > 0) {

				} else {

				}
			}
			Log.e("New","Projector   powerState:"+powerState);
		}
	};
	//joe 0823空开
	private EmptyTimer.OnEmptyIndexSureListener onEmptyIndexSureListener=new EmptyTimer.OnEmptyIndexSureListener() {

		@Override
		public void onState(int powerState,int[] states) {
			//joe powerState 总电源状态 0:关   1:开
			//states 1到8路的状态
			// 回路1-回路8状态	0:关 1:开 2:无效
			if (deviceParams != null && deviceParams.getDeviceTypeKey() == 12) {

			}
		}
	};
	//joe 0823时序开关
	private SeriesTimer.OnSeriesIndexSureListener onSeriesIndexSureListener=new SeriesTimer.OnSeriesIndexSureListener() {

		@Override
		public void onState(int powerState, int[] states) {
			//joe powerState 总电源状态 0:关   1:开
			//states 1到8路的状态
			// 回路1-回路8状态	0:关 1:开
			if (deviceParams != null && deviceParams.getDeviceTypeKey() == 13) {

			}
		}
	};
	//joe 0823新风
	private NewWinTimer.OnNewWinIndexSureListener onNewWinIndexSureListener=new NewWinTimer.OnNewWinIndexSureListener() {
		@Override
		public void onState(int powerState, int temp, int side, int speedIndex) {
			//joe powerState 总电源状态 0:关   1:开
			//temp 温度
			//side 阀门  0:关 1:开
			// speedIndex 风速 0:自动	1:低速	2:中速	3:高速
			if (deviceParams != null && deviceParams.getDeviceTypeKey() == 8) {
				if (powerState > 0) {

				} else {

				}
			}
		}
	};
	private SensorTimer.OnSensorIndexSureListener onSensorIndexSureListener=new SensorTimer.OnSensorIndexSureListener() {

		@Override
		public void onState(int powerState, int sensorNumber) {
			if (deviceParams != null && deviceParams.getDeviceTypeKey() == 16) {
				int product = Integer.parseInt(deviceParams.getProductsCode(), 16);
				if(product==34){
					peopleTxt.setText(powerState>0?"输出开启":"输出关闭");
				}else{
					int realVal=0;
					if(deviceParams.getCmdDecodeType()==4){
						realVal=deviceParams.getDeviceAnalogVar2();
					}else if(deviceParams.getCmdDecodeType()==5){
						realVal=(deviceParams.getDeviceAnalogVar3()*256)+deviceParams.getDeviceAnalogVar2();;
					}else if(deviceParams.getCmdDecodeType()==6){

					}else if(deviceParams.getCmdDecodeType()==7){

					}
					peopleTxt.setText(realVal+"");
				}
			}
		}
	};
	private PeopleTimer.OnPeopleIndexSureListener onPeopleIndexSureListener=new PeopleTimer.OnPeopleIndexSureListener() {

		@Override
		public void onState(int powerState, int peopleNumber) {
			if (deviceParams != null && deviceParams.getDeviceTypeKey() == 17) {
				int realVal=0;
				if(deviceParams.getCmdDecodeType()==4){
					realVal=deviceParams.getDeviceAnalogVar2();
				}else if(deviceParams.getCmdDecodeType()==5){
					realVal=(deviceParams.getDeviceAnalogVar3()*256)+deviceParams.getDeviceAnalogVar2();;
				}else if(deviceParams.getCmdDecodeType()==6){

				}else if(deviceParams.getCmdDecodeType()==7){

				}
				peopleTxt.setText("当前人数:"+realVal);
			}
		}
	};
	@Override
	public void setElectric(String cmd) {
		if (deviceParams.getDeviceTypeKey() == 2) {//joe 窗帘
			CurTimer.getInstance().setCmdCode(deviceParams,cmd, onCurIndexSureListener);
		}
	}
}
