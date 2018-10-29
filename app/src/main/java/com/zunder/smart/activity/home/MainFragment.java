package com.zunder.smart.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;


import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zunder.control.ButtonBean;
import com.zunder.control.ButtonInfo;
import com.zunder.control.ButtonUtils;
import com.zunder.control.SubButtonUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.TabHomeActivity;
import com.zunder.smart.activity.sub.LightActivity;
import com.zunder.smart.activity.sub.MusicActivity;
import com.zunder.smart.activity.sub.ProjectorActivity;
import com.zunder.smart.activity.sub.SubActivity;
import com.zunder.smart.activity.timmer.AirTimer;
import com.zunder.smart.activity.timmer.CurTimer;
import com.zunder.smart.activity.timmer.DimmerTimer;
import com.zunder.smart.activity.timmer.EmptyTimer;
import com.zunder.smart.activity.timmer.LightTimer;
import com.zunder.smart.activity.timmer.LockTimer;
import com.zunder.smart.activity.timmer.MusicTimer;
import com.zunder.smart.activity.timmer.NewWinTimer;
import com.zunder.smart.activity.timmer.PeopleTimer;
import com.zunder.smart.activity.timmer.ProjectorTimer;
import com.zunder.smart.activity.timmer.SensorTimer;
import com.zunder.smart.activity.timmer.SeriesTimer;
import com.zunder.smart.activity.timmer.TvTimer;
import com.zunder.smart.activity.tv.Channelctivity;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.activity.sub.AirActivity;
import com.zunder.smart.activity.sub.AirSwitchActivity;
import com.zunder.smart.activity.sub.FreshAirActivity;
import com.zunder.smart.activity.sub.RedFraActivity;
import com.zunder.smart.activity.sub.SequencerActivity;
import com.zunder.smart.activity.tv.TvActivity;
import com.zunder.smart.listener.DeviceStateListener;
import com.zunder.smart.model.Device;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import java.util.List;

public class MainFragment extends Fragment implements OnClickListener,DeviceStateListener {

	private Activity activity;
	private Button allON;
	private Button allOFF;
	LinearLayout controlLayout;
	Button showBtn;
	private TextView modeTxt;
	private Button chButton;
	private ImageView stateImage;
	private Device device;
	private LinearLayout tempLayout;
	private ImageView tempImage0;
	private ImageView tempImage1;
	private ImageView modeImage;
	private ImageView speedImage;
	private LinearLayout stateLayout;

	private List <ButtonInfo> list;
	private int[] stateImages;
	private int[] imageIndexs;
	private int[] tempImages;
	private int[] modeImages;
	private int[] speedImages;
	private TextView peopleTxt;

	View root;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		root = inflater.inflate(R.layout.fragment_main, container,
				false);
		activity = getActivity();
		controlLayout = (LinearLayout) root.findViewById(R.id.controlLayout);
		showBtn = (Button) root.findViewById(R.id.showBtn);
		chButton=(Button)root.findViewById(R.id.chButton);
		allON = (Button) root.findViewById(R.id.allON);
		allOFF = (Button) root.findViewById(R.id.allOFF);
		stateImage = (ImageView) root.findViewById(R.id.stateImage);
		tempLayout = (LinearLayout) root.findViewById(R.id.tempLayout);
		tempImage0 = (ImageView) root.findViewById(R.id.tempImage0);
		tempImage1 = (ImageView) root.findViewById(R.id.tempImage1);
		modeImage = (ImageView) root.findViewById(R.id.modeImage);
		speedImage = (ImageView) root.findViewById(R.id.speedImage);
		stateLayout=(LinearLayout)root.findViewById(R.id.stateLayout);
		modeTxt=(TextView)root.findViewById(R.id.modeTxt);
		peopleTxt=(TextView)root.findViewById(R.id.peopleTxt);
		speedImage.setOnClickListener(this);
		allON.setOnClickListener(this);
		allOFF.setOnClickListener(this);
		showBtn.setOnClickListener(this);
		chButton.setOnClickListener(this);
		return root;
	}


	public static IWidgetDAO sql() {
		return MyApplication.getInstance().getWidgetDataBase();
	}

	private Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			switch (msg.what) {
				case 0:
					showBtn.setVisibility(View.VISIBLE);
					controlLayout.setVisibility(View.GONE);
					break;
				case 1:
					if (device != null) {
					//	0:关   1:开
						int state = device.getState();
						stateImage.setImageResource(stateImages[state == 0 ? 0 : 1]);
					}

					break;
			}
		}
	};

	//joe 加载控件与数据
	public void init(Device device) {
		this.device = device;

		ButtonBean buttonBean = ButtonUtils.getInstance().getButtonBean(device.getDeviceTypeKey());
		if (buttonBean != null) {
			list = buttonBean.getList();
			stateImages = buttonBean.getState();
			imageIndexs = buttonBean.getImageIndexs();
			tempImages=buttonBean.getTempImage();
			modeImages=buttonBean.getModeImage();
			speedImages=buttonBean.getSpeedImage();
			initButtons();
			initImage();
		}
		controlLayout.setVisibility(View.VISIBLE);
		showBtn.setVisibility(View.GONE);
		tempLayout.setVisibility(View.GONE);

		setDeviceStateListener();
		startTime();
	}

	public void setActivity(){
		if(device!=null) {
			if (device.getDeviceTypeKey() == 1) {
				LightActivity.startActivity(activity, device.getId());
			}else if (device.getDeviceTypeKey() == 4) {
				AirActivity.startActivity(activity, device.getId());
			}else if (device.getDeviceTypeKey() == 5) {
				TvActivity.startActivity(activity, device.getId());
			}else if (device.getDeviceTypeKey() == 6) {
				RedFraActivity.startActivity(activity, device.getId());
			}else if (device.getDeviceTypeKey() == 8) {
				MusicActivity.startActivity(activity, device.getId());
			}else if (device.getDeviceTypeKey() == 11) {
				ProjectorActivity.startActivity(activity, device.getId());
			} else if (device.getDeviceTypeKey() == 12) {
				AirSwitchActivity.startActivity(activity, device.getId());
			} else if (device.getDeviceTypeKey() == 13) {
				SequencerActivity.startActivity(activity, device.getId());
			} else if (device.getDeviceTypeKey() == 14) {
				FreshAirActivity.startActivity(activity, device.getId());
			}else{
				SubActivity.startActivity(activity, device.getId());
			}
		}
	}
	public void setDeviceStateListener(){
		TcpSender.setDeviceStateListener(this);
		SendCMD.getInstance().sendCMD(255,"1",device);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}


	//joe 控件赋值
	public void initButtons() {
		controlLayout.removeAllViews();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Button button = new Button(activity);
				ButtonInfo buttonInfo = list.get(i);
				button.setTag(buttonInfo.getId() + "");
				button.setText(buttonInfo.getBtnTitle());
				button.setTextSize(14);
				if(buttonInfo.getColor()==1){
				    button.setBackgroundResource(R.drawable.button_close_shape);
					button.setTextColor(getResources().getColor(R.color.font_close_press));
				}else {
                    button.setBackgroundResource(R.drawable.button_boder_shape);
					button.setTextColor(getResources().getColor(R.color.font_open_press));
                }
				button.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ButtonInfo info = SubButtonUtils.getInstance().getButtonInfo(list, Integer.parseInt(((Button) v).getTag().toString()));
						if (info != null) {
							SendCMD.getInstance().sendCMD(250, device.getRoomName() + device.getDeviceName() + info.getCMD(), device);
						}
						playKeytone();
					}
				});
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
				params.weight = 1.0f;
//				params.height=100;
				params.setMargins(10, 0, 10, 0);
				button.setLayoutParams(params);
				controlLayout.addView(button);
			}
		}
	}

	//joe 初始图片
	public void initImage() {
		stateLayout.removeAllViews();
		chButton.setVisibility(View.GONE);
		peopleTxt.setVisibility(View.GONE);
		if(device.getDeviceTypeKey() == 9){
			modeTxt.setText("情景代码:"+Integer.valueOf(device.getDeviceID().substring(0, 2), 16));
		}else{
			modeTxt.setText("");
		}
		if (device.getDeviceTypeKey() == 12) {//空开
			if(imageIndexs!=null){
				stateLayout.setBackgroundResource(imageIndexs[0]);
			}
			for (int i = 0; i < 8; i++) {
				ImageView imageView0 = new ImageView(activity);
				imageView0.setLayoutParams(new FrameLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT));
				imageView0.setImageResource(R.mipmap.empty_off);
				stateLayout.addView(imageView0);
			}
		} else if (device.getDeviceTypeKey() == 13) {//时序
			if(imageIndexs!=null){
				stateLayout.setBackgroundResource(imageIndexs[0]);
			}
			for (int i = 0; i < 8; i++) {
				ImageView imageView0 = new ImageView(activity);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				params.gravity=Gravity.TOP;
				params.setMargins(0,120,0,0);
				imageView0.setLayoutParams(params);
				imageView0.setImageResource(R.mipmap.seq_off);
				stateLayout.addView(imageView0);
			}
		} else if (device.getDeviceTypeKey() == 16) {//传感
			if(imageIndexs!=null){
				stateLayout.setBackgroundResource(imageIndexs[0]);
			}
			peopleTxt.setVisibility(View.VISIBLE);
			peopleTxt.setText("--");
		}else if (device.getDeviceTypeKey() == 17) {//人员计数
			if(imageIndexs!=null){
				stateLayout.setBackgroundResource(imageIndexs[0]);
			}
			peopleTxt.setVisibility(View.VISIBLE);
			peopleTxt.setText("当前人数:0");
		}else {
			if(device.getDeviceTypeKey()==5){
				chButton.setVisibility(View.VISIBLE);
			}
			stateLayout.setBackgroundResource(R.mipmap.img_bg);
			stateLayout.addView(stateImage);
			if (stateImages != null && stateImages.length == 2) {
				stateImage.setImageResource(stateImages[device.getState()>0?1:0]);
			}
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (isHidden()) {
			searchflag = false;
			TcpSender.setDeviceStateListener(null);
		} else {
			TcpSender.setDeviceStateListener(this);
			controlLayout.setVisibility(View.VISIBLE);
			showBtn.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		TcpSender.setDeviceStateListener(this);
		switch (v.getId()) {
			case R.id.allON: {
				DialogAlert alert = new DialogAlert(activity);
				alert.init(activity.getString(R.string.tip), getString(R.string.isAllON));

				alert.setSureListener(new DialogAlert.OnSureListener() {
					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						TabHomeActivity.getInstance().AllOpen();
					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub
					}
				});
				alert.show();
			}
			break;
			case R.id.allOFF: {
				DialogAlert alert = new DialogAlert(activity);
				alert.init(activity.getString(R.string.tip), getString(R.string.isAllOFF));

				alert.setSureListener(new DialogAlert.OnSureListener() {
					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						TabHomeActivity.getInstance().AllClose();
					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub
					}
				});
				alert.show();
			}
			break;
			case R.id.showBtn:
				controlLayout.setVisibility(View.VISIBLE);
				showBtn.setVisibility(View.GONE);
				startTime();
				break;
			case R.id.speedImage:
				if(device!=null) {
					SendCMD.getInstance().sendCMD(250,device.getRoomName()+
							device.getDeviceName() + getString(R.string.speedswitch), device);
				}
				break;
			case R.id.chButton:
				Channelctivity.startActivity(activity,device.getId());
				break;
		}
		playKeytone();
	}
	private void playKeytone() {
		startCount=0;
		MediaPlayer.create(activity, R.raw.ping_short).start();
	}
	//joe 开状态
	private void PowerOnSate(){
		stateImage.setImageResource(stateImages[1]);
	}
	//关状态
	private void PowerOffSatate(){
		stateImage.setImageResource(stateImages[0]);
	}

	private boolean searchflag = false;
	private int startCount = 0;

	private void startTime() {
		startCount = 0;
		searchflag = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (searchflag) {
					try {
						Thread.sleep(1000);
						startCount++;
						if (startCount >= 30) {
							Message message = handler.obtainMessage();
							message.what = 0;
							handler.sendMessage(message);
							searchflag = false;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	public void receiveDeviceStatus(String cmd) {
		if(device!=null) {
			if(device.getDevice_change()>0) {
				//joe 18_08_25 所有回码从这里开始
				if (device.getDeviceTypeKey() == 1) {//joe 灯光
					if (cmd.substring(8, 16).equals(device.getProductsCode() + device.getDeviceID())) {
						LightTimer.getInstance().setBackeCode(cmd, onLightIndexSureListener);
					}
				} else if (device.getDeviceTypeKey() == 2) {//joe 窗帘
					CurTimer.getInstance().setBackeCode(device, onCurIndexSureListener);
				} else if (device.getDeviceTypeKey() == 4) {//joe 空调
					AirTimer.getInstance().setBackeCode(device, onAirIndexSureListener);
				} else if (device.getDeviceTypeKey() == 5) {//joe 电视
					TvTimer.getInstance().setBackeCode(device, onTvIndexSureListener);
				} else if (device.getDeviceTypeKey() == 7) {//joe 调光
					DimmerTimer.getInstance().setBackeCode(device, onDimmerIndexSureListener);
				} else if (device.getDeviceTypeKey() == 8) {//joe 音乐
					MusicTimer.getInstance().setBackeCode(device, onMusicIndexSureListener);
				} else if (device.getDeviceTypeKey() == 10) {//joe 门锁
					LockTimer.getInstance().setBackeCode(device, onLockIndexSureListener);
				} else if (device.getDeviceTypeKey() == 11) {//joe 投影机
					ProjectorTimer.getInstance().setBackeCode(device, onProjectorIndexSureListener);
				} else if (device.getDeviceTypeKey() == 12) {//joe 空开
					EmptyTimer.getInstance().setBackeCode(device, onEmptyIndexSureListener);
				} else if (device.getDeviceTypeKey() == 13) {//joe 时序
					SeriesTimer.getInstance().setBackeCode(device, onSeriesIndexSureListener);
				} else if (device.getDeviceTypeKey() == 14) {//joe 新风
					NewWinTimer.getInstance().setBackeCode(device, onNewWinIndexSureListener);
				} else if (device.getDeviceTypeKey() == 16) {//joe 传感器
					SensorTimer.getInstance().setBackeCode(device,onSensorIndexSureListener);
				} else if (device.getDeviceTypeKey() == 17) {//joe 人员计数
					PeopleTimer.getInstance().setBackeCode(device,onPeopleIndexSureListener);
				} else {
					//灯光 插座
					handler.sendEmptyMessage(1);
				}
				device.setDevice_change(0);
			}
		}
	}


	@Override
	public void onResume() {
		super.onResume();
	}//joe 0823 窗帘回码
	private LightTimer.OnLightIndexSureListener onLightIndexSureListener=new LightTimer.OnLightIndexSureListener(){

		@Override
		public void onState(int powerState, int ioIndex1, int ioIndex2, int ioIndex3, int ioIndex4) {
			stateImage.setImageResource(stateImages[device.getState()>0 ? 1 : 0]);
			powerState();
		}
	};

	//joe 窗帘回码
	private CurTimer.OnCurIndexSureListener onCurIndexSureListener=new CurTimer.OnCurIndexSureListener(){

		@Override
		public void onState(int index) {
			//joe index
			// 窗帘进度
			if (device != null&&device.getDeviceTypeKey() == 2) {
				if (imageIndexs != null && index < imageIndexs.length) {
					stateImage.setImageResource(imageIndexs[index]);
				}
			}
		}

		@Override
		public void onCycle(int cycleIndex, int isSuccess, int processState) {

		}
	};

	//Joe 空调回码
	private AirTimer.OnAirIndexSureListener onAirIndexSureListener=new AirTimer.OnAirIndexSureListener(){

		@Override
		public void onState(int powerState, int tempIndex, int modeIndex, int speedIndex) {
			//joe  powerState 电源状态 0:关   1:开
			//  tempIndex 温度图片索引
			// modeIndex 模式图片索引
			//speedIndex 风速图片索引
			if (device != null&&device.getDeviceTypeKey() == 4) {
				if (powerState > 0) {
					PowerOnSate();
					tempLayout.setVisibility(View.VISIBLE);
					if (tempImages != null) {
						tempImage0.setImageResource(tempImages[tempIndex/10]);
						tempImage1.setImageResource(tempImages[tempIndex%10]);
					}
					if (modeImages != null && modeIndex < modeImages.length) {
						modeImage.setImageResource(modeImages[modeIndex]);
					}
					if (speedImages != null && speedIndex < speedImages.length) {
						speedImage.setImageResource(speedImages[speedIndex]);
					}
				} else {
					PowerOffSatate();
					tempLayout.setVisibility(View.GONE);
				}
				powerState();
			}else{
				AirTimer.getInstance().setBackeCode(null,null);
			}
		}
	};
	//Joe 调光
	private DimmerTimer.OnDimmerIndexSureListener onDimmerIndexSureListener=new DimmerTimer.OnDimmerIndexSureListener() {
		@Override
		public void onState(int powerState,Boolean[] booleans,int color,int dimmerStyle) {

			// powerState 电源状态 0:关   1:开
			//booleans 8个模式
			// color 0:白光   1:暖光    2:中性光    3:彩光
			// dimmerStyle 0:饱和度 1:色温
			if (device != null && device.getDeviceTypeKey() == 7) {
				if (powerState > 0) {
					PowerOnSate();
				} else {
					PowerOffSatate();
				}
				powerState();
			}
            Log.e("New","Dimmer  powerState:"+powerState+"  color:"+color+"   dimmerStyle:"+dimmerStyle);
		}
	};
	//Joe 电视回码
	private TvTimer.OnTvIndexSureListener onTvIndexSureListener=new TvTimer.OnTvIndexSureListener(){

		@Override
		public void onState(int powerState) {
			//joe  powerState 电源状态 0:关   1:开
			if (device != null&&device.getDeviceTypeKey() == 5) {
					if (powerState > 0) {
						PowerOnSate();
					} else {
						PowerOffSatate();
					}
			}else{
				TvTimer.getInstance().setBackeCode(null,null);
			}
            Log.e("New","TV   powerState:"+powerState);
		}
	};
	//joe 音乐回码
	private MusicTimer.OnMusicIndexSureListener onMusicIndexSureListener=new MusicTimer.OnMusicIndexSureListener() {
		@Override
		public void onState(int powerState, int playState, int muteState, int volume) {
			//joe powerState 电源状态  0:关   1:开
			// playState 播放状态     0:停止 1:暂停 2:播放
			//muteState 静音状态 0:非静音 1:静音
			// volume 音量大小
			if (device != null&&device.getDeviceTypeKey() == 8) {
					if (powerState > 0) {
						PowerOnSate();
					} else {
						PowerOffSatate();
					}
					powerState();
				}
            Log.e("New","Music   powerState:"+powerState+"  playState:"+playState+"   muteState:"+muteState+"   volume:"+volume);
		}
	};//joe 门锁回码
	private LockTimer.OnLockIndexSureListener onLockIndexSureListener=new LockTimer.OnLockIndexSureListener() {
		@Override
		public void onState(int powerState,int imageIndex) {
			//joe powerState 门锁状态 0:关门  1:开门
			// imageIndex  图片状态 0:关门  1:开门
			if (device != null&&device.getDeviceTypeKey() == 10) {
				stateImage.setImageResource(stateImages[imageIndex]);
				}
            Log.e("New","Lock   powerState:"+powerState+"  imageIndex:"+imageIndex);

        }
	};
	//joe 投影机 0911
	private ProjectorTimer.OnProjectorIndexSureListener onProjectorIndexSureListener=new ProjectorTimer.OnProjectorIndexSureListener() {
		@Override
		public void onState(int powerState) {
			//joe powerState 电源状态 0:关   1:开
			if (device != null && device.getDeviceTypeKey() == 11) {
				if (powerState > 0) {
					PowerOnSate();
				} else {
					PowerOffSatate();
				}
				powerState();
			}
            Log.e("New","Projector   powerState:"+powerState);
		}
	};
	//空开
	private EmptyTimer.OnEmptyIndexSureListener onEmptyIndexSureListener=new EmptyTimer.OnEmptyIndexSureListener() {

		@Override
		public void onState(int powerState,int[] states) {
			//joe powerState 总电源状态 0:关   1:开
			//states 1到8路的状态
			// 回路1-回路8状态	0:关 1:开 2:无效
			if (device != null && device.getDeviceTypeKey() == 12) {
				stateLayout.removeAllViews();
				for (int i = 0; i < states.length; i++) {
					ImageView imageView0 = new ImageView(activity);
					imageView0.setLayoutParams(new FrameLayout.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.WRAP_CONTENT));
					if (states[i] == 0) {
						imageView0.setImageResource(R.mipmap.empty_off);
					} else if (states[i] == 1) {
						imageView0.setImageResource(R.mipmap.empty_on);
					} else {
						imageView0.setImageResource(R.mipmap.empty_dis);
					}
					stateLayout.addView(imageView0);
				}
			}
		}
	};
	//时序开关
	private SeriesTimer.OnSeriesIndexSureListener onSeriesIndexSureListener=new SeriesTimer.OnSeriesIndexSureListener() {

		@Override
		public void onState(int powerState, int[] states) {
			//joe powerState 总电源状态 0:关   1:开
			//states 1到8路的状态
			// 回路1-回路8状态	0:关 1:开
			if (device != null && device.getDeviceTypeKey() == 13) {
				stateLayout.removeAllViews();
				for (int i = 0; i < states.length; i++) {
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
					ImageView imageView0 = new ImageView(activity);
					params.gravity=Gravity.TOP;
					params.setMargins(0,120,0,0);
					params.gravity=Gravity.TOP;
					imageView0.setLayoutParams(params);
					if (states[i] == 0) {
						imageView0.setImageResource(R.mipmap.seq_off);
					} else {
						imageView0.setImageResource(R.mipmap.seq_on);
					}
					stateLayout.addView(imageView0);
				}
			}
		}
	};
	//新风
	private NewWinTimer.OnNewWinIndexSureListener onNewWinIndexSureListener=new NewWinTimer.OnNewWinIndexSureListener() {
		@Override
		public void onState(int powerState, int temp, int side, int speedIndex) {
			//joe powerState 总电源状态 0:关   1:开
			//temp 温度
			//side 阀门  0:关 1:开
			// speedIndex 风速 0:自动	1:低速	2:中速	3:高速
			if (device != null && device.getDeviceTypeKey() == 14) {
				if (powerState > 0) {
					PowerOnSate();
				} else {
					PowerOffSatate();
				}
				powerState();
                Log.e("New","NewWin   powerState:"+powerState+"  temp:"+temp+"   side:"+side+"   speedIndex:"+speedIndex);
            }
		}
	};
	//joe 传感
	private SensorTimer.OnSensorIndexSureListener onSensorIndexSureListener=new SensorTimer.OnSensorIndexSureListener() {

		@Override
		public void onState(int powerState, int sensorNumber) {
			if (device != null && device.getDeviceTypeKey() == 16) {
				int product = Integer.parseInt(device.getProductsCode(), 16);
				if(product==34){
					peopleTxt.setText(powerState>0?"输出开启":"输出关闭");
				}else{
					int realVal=0;
					if(device.getCmdDecodeType()==4){
						realVal=device.getDeviceAnalogVar2();
					}else if(device.getCmdDecodeType()==5){
						realVal=(device.getDeviceAnalogVar3()*256)+device.getDeviceAnalogVar2();;
					}else if(device.getCmdDecodeType()==6){

					}else if(device.getCmdDecodeType()==7){

					}
					peopleTxt.setText(realVal+"");
				}
			}
		}
	};
	//Joe 人员计数
	private PeopleTimer.OnPeopleIndexSureListener onPeopleIndexSureListener=new PeopleTimer.OnPeopleIndexSureListener() {

		@Override
		public void onState(int powerState, int peopleNumber) {
			if (device != null && device.getDeviceTypeKey() == 17) {
				int realVal=0;
				if(device.getCmdDecodeType()==4){
					realVal=device.getDeviceAnalogVar2();
				}else if(device.getCmdDecodeType()==5){
					realVal=(device.getDeviceAnalogVar3()*256)+device.getDeviceAnalogVar2();;
				}else if(device.getCmdDecodeType()==6){

				}else if(device.getCmdDecodeType()==7){

				}
				peopleTxt.setText("当前人数:"+realVal);
			}
		}
	};
	public  void powerState(){

		for (int i=1;i<=list.size();i++) {
			Button button = (Button) controlLayout.findViewWithTag(i+"");
			if(button!=null){
				if(button.getText().equals("电源")){
					if(device.getState()>0){
						button.setBackgroundResource(R.drawable.button_close_shape);
						button.setTextColor(getResources().getColor(R.color.font_close_press));
					}else {
						button.setBackgroundResource(R.drawable.button_boder_shape);
						button.setTextColor(getResources().getColor(R.color.font_open_press));
					}
					//break;
				}
				if(device.getDeviceTypeKey()==8) {
					if (button.getText().equals("静音")) {
						if (((device.getDeviceAnalogVar3() & 8) > 0)&&(device.getState()>0)) {
							button.setBackgroundResource(R.drawable.button_close_shape);
							button.setTextColor(getResources().getColor(R.color.font_close_press));
						} else {
							button.setBackgroundResource(R.drawable.button_boder_shape);
							button.setTextColor(getResources().getColor(R.color.font_open_press));
						}
					}
					if (button.getText().equals("播放")) {
						if ((((device.getDeviceAnalogVar3() >> 1) & 3) == 1)&&(device.getState()>0)) {
							button.setBackgroundResource(R.drawable.button_close_shape);
							button.setTextColor(getResources().getColor(R.color.font_close_press));
						} else {
							button.setBackgroundResource(R.drawable.button_boder_shape);
							button.setTextColor(getResources().getColor(R.color.font_open_press));
						}
					}
					if (button != null && button.getText().equals("暂停")) {
						if ((((device.getDeviceAnalogVar3() >> 1) & 3) != 1)&&(device.getState()>0)) {
							button.setBackgroundResource(R.drawable.button_close_shape);
							button.setTextColor(getResources().getColor(R.color.font_close_press));
						} else {
							button.setBackgroundResource(R.drawable.button_boder_shape);
							button.setTextColor(getResources().getColor(R.color.font_open_press));
						}
					}
				}
			}
		}
	}
}
