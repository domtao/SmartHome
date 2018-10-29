package com.zunder.smart.activity.home;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zunder.control.ButtonBean;
import com.zunder.control.ButtonInfo;
import com.zunder.control.ButtonUtils;
import com.zunder.control.SubButtonUtils;
import com.zunder.scrollview.PureVerticalSeekBar;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.TabHomeActivity;
import com.zunder.smart.activity.timmer.DimmerTimer;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.activity.sub.LedActivity;
import com.zunder.smart.listener.DeviceStateListener;
import com.zunder.smart.model.Device;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;

import java.util.List;

public class DimmerFragment extends Fragment implements OnClickListener,DeviceStateListener,PureVerticalSeekBar.OnSlideChangeListener {

	private Activity activity;
	private Button allON;
	private Button allOFF;
	LinearLayout controlLayout;
	Button showBtn;
	private Device device;
	private TextView textView0,textView1,textView2,textView3,textView4,textView5;
	PureVerticalSeekBar liveBar;
	TextView liveTxt;
	LinearLayout liveLayout;

	PureVerticalSeekBar satuBar;
	TextView satuTxt;
	LinearLayout satuLayout;

	private List <ButtonInfo> list;
	View root;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		root = inflater.inflate(R.layout.fragment_dimmer, container,
				false);
		activity = getActivity();
		controlLayout = (LinearLayout) root.findViewById(R.id.controlLayout);
		showBtn = (Button) root.findViewById(R.id.showBtn);
		allON = (Button) root.findViewById(R.id.allON);
		allOFF = (Button) root.findViewById(R.id.allOFF);


		textView0=(TextView)root.findViewById(R.id.txt0);
		textView1=(TextView)root.findViewById(R.id.txt1);
		textView2=(TextView)root.findViewById(R.id.txt2);
		textView3=(TextView)root.findViewById(R.id.txt3);
		textView4=(TextView)root.findViewById(R.id.txt4);
		textView5=(TextView)root.findViewById(R.id.txt5);

		liveBar=(PureVerticalSeekBar)root.findViewById(R.id.liveBar);
		liveTxt=(TextView) root.findViewById(R.id.liveTxt);
		liveLayout=(LinearLayout) root.findViewById(R.id.liveLayout);

		satuBar=(PureVerticalSeekBar)root.findViewById(R.id.satuBar);
		satuTxt=(TextView) root.findViewById(R.id.satuTxt);
		satuLayout=(LinearLayout) root.findViewById(R.id.satuLayout);

		liveBar.setOnSlideChangeListener(this);
		satuBar.setOnSlideChangeListener(this);
		textView0.setOnClickListener(this);
		textView1.setOnClickListener(this);
		textView2.setOnClickListener(this);
		textView3.setOnClickListener(this);
		textView4.setOnClickListener(this);
		textView5.setOnClickListener(this);


		liveBar.setVertical_color(Color.GRAY, Color.GREEN);
		liveBar.setDragable(true);//设置是否可以拖动
		liveBar.setCircle_color(Color.GREEN);//设置圆形滑块颜色


		satuBar.setVertical_color(Color.GRAY, Color.GREEN);
		satuBar.setDragable(true);//设置是否可以拖动
		satuBar.setCircle_color(Color.GREEN);//设置圆形滑块颜色

		allON.setOnClickListener(this);
		allOFF.setOnClickListener(this);
		showBtn.setOnClickListener(this);
		return root;
	}
	public void setActivity(){
		if(device!=null) {
			if (device.getDeviceTypeKey() == 7) {
				LedActivity.startActivity(activity, device.getId());
			}
		}
	}

	public void setDeviceStateListener(){
		TcpSender.setDeviceStateListener(this);
		SendCMD.getInstance().sendCMD(255,"1",device);
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
					}

					break;
			}
		}
	};

	//joe 加载控件与数据
	public void init(Device device) {
		this.device = device;
		TcpSender.setDeviceStateListener(this);
		ButtonBean buttonBean = ButtonUtils.getInstance().getButtonBean(device.getDeviceTypeKey());
		if (buttonBean != null) {
			list = buttonBean.getList();
			initButtons();
		}
		controlLayout.setVisibility(View.VISIBLE);
		showBtn.setVisibility(View.GONE);

		startTime();
		SendCMD.getInstance().sendCMD(255,"1",device);
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
		String deviceCMD=device.getRoomName()+device.getDeviceName();
		switch (v.getId()) {
			case R.id.txt0:
				SendCMD.getInstance().sendCMD(250, device.getRoomName()+deviceCMD + "蓝色", device);
				break;
			case R.id.txt1:
				SendCMD.getInstance().sendCMD(250, device.getRoomName()+deviceCMD + "红色", device);
				break;
			case R.id.txt2:
				SendCMD.getInstance().sendCMD(250, device.getRoomName()+deviceCMD + "绿色", device);
				break;
			case R.id.txt3:
				SendCMD.getInstance().sendCMD(250, device.getRoomName()+deviceCMD + "橙色", device);
				break;
			case R.id.txt4:
				SendCMD.getInstance().sendCMD(250, device.getRoomName()+deviceCMD + "黄色", device);
				break;
			case R.id.txt5:
				SendCMD.getInstance().sendCMD(250, device.getRoomName()+deviceCMD + "粉色", device);
				break;
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
		}
		startCount = 0;
		playKeytone();
	}

	private void playKeytone() {
		MediaPlayer.create(activity, R.raw.ping_short).start();
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

			if(device.getDeviceTypeKey()==7){//joe 调光
				DimmerTimer.getInstance().setBackeCode(device,onDimmerIndexSureListener);
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	//Joe 调光
	private DimmerTimer.OnDimmerIndexSureListener onDimmerIndexSureListener=new DimmerTimer.OnDimmerIndexSureListener() {
		@Override
		public void onState(int powerState,Boolean[] booleans,int color,int dimmerStyle) {

			// powerState 电源状态 0:关   1:开
			//booleans 8个模式
			// color 0:白光   1:暖光    2:中性光    3:彩光
			// dimmerStyle 0:饱和度  1:色温  2:亮度
			if (device != null && device.getDeviceTypeKey() == 7) {
				powerState();
				if(dimmerStyle==0){
					satuLayout.setVisibility(View.VISIBLE);
					satuTxt.setText("饱和度");
				}else if(dimmerStyle==1){
					satuLayout.setVisibility(View.VISIBLE);
					satuTxt.setText("色温");
				}else{
					satuLayout.setVisibility(View.GONE);
				}
			}
            Log.e("New","Dimmer  powerState:"+powerState+"  color:"+color+"   dimmerStyle:"+dimmerStyle);
		}
	};

	@Override
	public void OnSlideChangeListener(View view, float progress)
	{
	}

	@Override
	public void onSlideStopTouch(View view, float progress)
	{
		int value = (int)progress;
		switch (view.getId()){
			case R.id.satuBar:
				{
				String text = satuTxt.getText().toString() + (value == 0 ? 1 : value);
				SendCMD.getInstance().sendCmd(250, device.getRoomName()+device.getDeviceName() +text, device);
				}
			break;
			case R.id.liveBar:
				{
				String text = liveTxt.getText().toString()  + value;
				SendCMD.getInstance().sendCmd(250, device.getRoomName()+device.getDeviceName() + text, device);
				}
			break;
		}
	}
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
				}
				if(button.getText().equals("中性光")){
					if((device.getDeviceAnalogVar2()==2)&&(device.getState()>0)){
						button.setBackgroundResource(R.drawable.button_close_shape);
						button.setTextColor(getResources().getColor(R.color.font_close_press));
					}else {
						button.setBackgroundResource(R.drawable.button_boder_shape);
						button.setTextColor(getResources().getColor(R.color.font_open_press));
					}
				}
				if(button.getText().equals("白光")){
					if((device.getDeviceAnalogVar2()==0)&&(device.getState()>0)){
						button.setBackgroundResource(R.drawable.button_close_shape);
						button.setTextColor(getResources().getColor(R.color.font_close_press));
					}else {
						button.setBackgroundResource(R.drawable.button_boder_shape);
						button.setTextColor(getResources().getColor(R.color.font_open_press));
					}
				}if(button.getText().equals("暖光")){
					if((device.getDeviceAnalogVar2()==1)&&(device.getState()>0)){
						button.setBackgroundResource(R.drawable.button_close_shape);
						button.setTextColor(getResources().getColor(R.color.font_close_press));
					}else {
						button.setBackgroundResource(R.drawable.button_boder_shape);
						button.setTextColor(getResources().getColor(R.color.font_open_press));
					}
				}
			}
		}
	}
}
