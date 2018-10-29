package com.zunder.smart.activity.sub;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.control.ButtonBean;
import com.zunder.control.ButtonInfo;
import com.zunder.control.SubButtonUtils;
import com.zunder.scrollview.widget.WheelAdapter;
import com.zunder.scrollview.widget.WheelView;
import com.zunder.smart.R;
import com.zunder.smart.activity.sub.set.DeviceTimerActivity;
import com.zunder.smart.activity.sub.set.MoreActivity;
import com.zunder.smart.activity.timmer.CurTimer;
import com.zunder.smart.activity.timmer.EmptyTimer;
import com.zunder.smart.activity.timmer.LockTimer;
import com.zunder.smart.activity.timmer.MusicTimer;
import com.zunder.smart.activity.timmer.NewWinTimer;
import com.zunder.smart.activity.timmer.ProjectorTimer;
import com.zunder.smart.activity.timmer.SeriesTimer;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.listener.DeviceStateListener;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.Device;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.utils.ListNumBer;

import java.util.List;

public class MusicActivity extends Activity implements OnClickListener,SeekBar.OnSeekBarChangeListener,
        DeviceStateListener {
	private Activity activity;

	private static int Id = 0;
	static Device deviceParams;
	TextView titleTxt, backTxt, editeTxt;
	String deviceName = "";
	int time=0;
	private int Hour;
	static ImageView  musicPre, musicNext, musicStop, musicMute;
	static SeekBar volume;
	private RightMenu rightButtonMenuAlert;
	private  boolean isMute = false,  isPlay = false;
	public static void startActivity(Activity activity, int _id) {
		Id = _id;
		Intent intent = new Intent(activity, MusicActivity.class);
		activity.startActivityForResult(intent,100);
	}
	private Button downTime;
	private TextView timeTxt;
	private Button upTime;
	private LinearLayout timeLayout;
	private TextView msgTxt;
	private Button timeSure;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music);
		activity = this;
		TcpSender.setDeviceStateListener(this);
		deviceParams = DeviceFactory.getInstance().getDevicesById(Id);
		backTxt = (TextView) findViewById(R.id.backTxt);
		titleTxt = (TextView) findViewById(R.id.titleTxt);
		editeTxt = (TextView) findViewById(R.id.editeTxt);
		timeTxt = (TextView) findViewById(R.id.timeTxt);
		downTime = (Button) findViewById(R.id.downTime);
		upTime = (Button) findViewById(R.id.upTime);
		timeLayout = (LinearLayout) findViewById(R.id.timeLayout);
		msgTxt = (TextView) findViewById(R.id.msgTxt);
		timeSure = (Button) findViewById(R.id.timeSure);

		musicPre = (ImageView) findViewById(R.id.musicPre);
		musicStop = (ImageView) findViewById(R.id.musicStop);
		musicNext = (ImageView) findViewById(R.id.musicNext);
		musicMute = (ImageView) findViewById(R.id.musicMute);
		volume = (SeekBar) findViewById(R.id.volumeValue);
		volume.setProgress(0);
		volume.setMax(100);
		volume.setOnSeekBarChangeListener(this);
		musicPre.setOnClickListener(this);
		musicNext.setOnClickListener(this);
		musicStop.setOnClickListener(this);
		musicMute.setOnClickListener(this);

		downTime.setOnClickListener(this);
		upTime.setOnClickListener(this);
		timeSure.setOnClickListener(this);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		deviceName = deviceParams.getDeviceName();
		titleTxt.setText(deviceParams.getRoomName()+deviceName);

		initRightButtonMenuAlert();
		handler.sendEmptyMessage(1);
		Hour= AppTools.getHour();
	}

	private void initRightButtonMenuAlert() {
		rightButtonMenuAlert = new RightMenu(activity, R.array.right_sub,
				R.drawable.right_airswitch_images);
		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {

			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub
				switch (position) {
					case 0:
						DeviceTimerActivity.startActivity(activity,deviceParams.getId());
						break;
					case 1:
						MoreActivity.startActivity(activity,deviceParams.getId());
						break;
				}
				rightButtonMenuAlert.dismiss();
			}
		});
	}
	public void back(){
		Intent resultIntent = new Intent();
		setResult(0, resultIntent);
		finish();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		TcpSender.setDeviceStateListener(this);
		SendCMD.getInstance().sendCMD(255,"1",deviceParams);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			back();
			break;
			case R.id.downTime: {
				if (time > 60) {
					time -= 60;
				}
				else if(time==60||time==30){
					time -= 30;
				}
				if(time<=0) {
					timeLayout.setVisibility(View.GONE);
				}
				Hour= AppTools.getHour();
				int hour = time / 60;
				int minit = time % 60;
				timeTxt.setText(hour + "小时" + minit + "分钟");
				int total =Hour+hour*60+minit;
				if(total/60>=24){
					msgTxt.setText("将会在执行"+(total/60-24)+":"+(total%60)+"定时");
				}else{
					msgTxt.setText("将会在执行"+total/60+":"+(total%60)+"定时");
				}
			}
			break;
			case R.id.upTime: {
				if (time < 720) {
					if(time<60){
						time += 30;
					}else {
						time += 60;
					}
				} else {
					return;
				}
				Hour= AppTools.getHour();
				int hour = time / 60;
				int minit = time % 60;
				timeLayout.setVisibility(View.VISIBLE);
				timeTxt.setText(hour + "小时" + minit + "分钟");
				int total =Hour+hour*60+minit;
				if(total/60>=24){
					msgTxt.setText("将会在执行"+(total/60-24)+":"+(total%60)+"定时");
				}else{
					msgTxt.setText("将会在执行"+total/60+":"+(total%60)+"定时");
				}
			}
			break;
			case R.id.musicPre:
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceParams.getDeviceName() + getString(R.string.pre_song), deviceParams);
				showToast(getString(R.string.pre_song));
				break;
			case R.id.musicStop:
				String strPlay = getString(R.string.play);
				if (isPlay) {
					strPlay = getString(R.string.stop);
				}
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceParams.getDeviceName() + strPlay, deviceParams);
				showToast(strPlay);
				break;
			case R.id.musicNext:
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceParams.getDeviceName() + getString(R.string.next_song), deviceParams);
				showToast(getString(R.string.next_song));
				break;
			case R.id.musicMute:
				String str = getString(R.string.mute_off);
				if (isMute) {
					str = getString(R.string.mute_on);
				}
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceParams.getDeviceName() + str, deviceParams);

				showToast(str);

				break;
		case R.id.editeTxt:
			rightButtonMenuAlert.show(editeTxt);
			break;

			case R.id.timeSure: {
				//joe 0823 执行 ==ok
				String cmd;
				if (time == 30) {
					cmd = "打开" + time + "分钟";
				} else {
					cmd = "打开" + (time / 60) + "小时";
				}
				SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName() +
						deviceParams.getDeviceName() + getString(R.string.tempC) + cmd, deviceParams);
			}
				break;
		default:
			break;
		}
	}
	public void showToast(String msg) {
		ToastUtils.ShowSuccess(activity, msg, Toast.LENGTH_SHORT,true);
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

				break;

			default:
				break;
			}
		}
	};



	@Override
	public void receiveDeviceStatus(String cmd) {
		// TODO Auto-generated method stub
		//joe 0823 子页面回码处理
		if(deviceParams!=null) {
		  if(deviceParams.getDeviceTypeKey()==8){//joe 音乐
				MusicTimer.getInstance().setBackeCode(deviceParams,onMusicIndexSureListener);
			}
		}

	}




	//joe 0823 音乐回码
	private MusicTimer.OnMusicIndexSureListener onMusicIndexSureListener=new MusicTimer.OnMusicIndexSureListener() {
		@Override
		public void onState(int powerState, int playState, int muteState, int _volume) {
			//joe powerState 电源状态  0:关   1:开
			// playState 播放状态     0:停止 1:暂停 2:播放
			//muteState 静音状态 0:非静音 1:静音
			// volume 音量大小
			if (deviceParams != null&&deviceParams.getDeviceTypeKey() == 8) {
				if(muteState==1){
					isMute=true;
					musicMute.setImageResource(R.mipmap.zun_music_nomute);
				}else {
					isMute=false;
					musicMute.setImageResource(R.mipmap.zun_music_mute);
				}

				if(playState==1){
					isPlay=true;
					musicStop.setImageResource(R.mipmap.zun_music_pause);
				}else{
					isPlay=false;
					musicStop.setImageResource(R.mipmap.zun_music_play);
				}


				volume.setProgress(_volume);

			}
			Log.e("New","Music   powerState:"+powerState+"  playState:"+playState+"   muteState:"+muteState+"   volume:"+volume);
		}
	};


	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		int p = seekBar.getProgress();
		SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
				deviceParams.getDeviceName() +getString(R.string.vol)  + p, deviceParams);
	}
}
