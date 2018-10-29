package com.zunder.smart.activity.sub;

import java.util.List;

import com.bumptech.glide.Glide;
import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.activity.sub.set.ElectricActivity;
import com.zunder.smart.activity.sub.set.MoreActivity;
import com.zunder.smart.activity.sub.set.StudyActivity;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.ProductFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.DialogAlert.OnSureListener;
import com.zunder.smart.dialog.StudyAlert;

import com.zunder.smart.activity.sub.set.DeviceTimerActivity;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.Products;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.SendThread;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.utils.ListNumBer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;
import android.widget.PopupWindow.OnDismissListener;

@SuppressLint("NewApi")
public class AirActivity extends BaseActivity implements OnClickListener{
	private static Activity mContext;
	private static ImageView im_by,  im_hot, im_cool,
			im_show_speed, im_show_by, im_show_model;
	private static Button im_temp_up, im_temp_down, im_power;
	TextView aiImage;
	private static TextView tx_show_power, backTxt;
	private Button bt_fan, bt_model;
	private RightMenu rightButtonMenuAlert;
	private static Boolean powerFlag = false;
	private static ImageView temp0,temp1;

	private static boolean startflag = false;
	private static String[] PowerStstr = null;
	private static int[] showPic = {
			R.mipmap.mode_auto_black, R.mipmap.mode_cold_black,
			R.mipmap.mode_drying_black3, R.mipmap.mode_warm_black,
			R.mipmap.mode_wind_black };
	private static int[] tmperPic = {R.mipmap.sz_0,
			R.mipmap.sz_1,R.mipmap.sz_2,R.mipmap.sz_3,
			R.mipmap.sz_4,R.mipmap.sz_5,R.mipmap.sz_6
			,R.mipmap.sz_7,R.mipmap.sz_8,R.mipmap.sz_9};
	private static int[] fanPic = {
			R.mipmap.wind_amount_white_one, R.mipmap.wind_amount_white_two,
			R.mipmap.wind_amount_white_three, R.mipmap.wind_amount_white_four,
			R.mipmap.wind_amount_white_five };
	private static int[] timePic = {
			R.mipmap.timeset0, R.mipmap.timeset0, R.mipmap.timeset1,
			R.mipmap.timeset1, R.mipmap.timeset2, R.mipmap.timeset3,
			R.mipmap.timeset4, R.mipmap.timeset5, R.mipmap.timeset6,
			R.mipmap.timeset7, R.mipmap.timeset8 };
	boolean isLearnFlag = false;
	private static int fanSt = 0;
	static String deviceID = "000000";
	static int id;
	static Device deviceParams = null;

	private Button downTime;
	private TextView timeTxt;
	private Button upTime;
	private LinearLayout timeLayout;
	private TextView msgTxt;
	private Button timeSure;
	int time=0;
	private int Hour;

	public static void startActivity(Activity activity, int _id) {
		id = _id;
		Intent intent = new Intent(activity, AirActivity.class);
		activity.startActivityForResult(intent,0);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_air);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		TcpSender.setDeviceStateListener(this);
		mContext = this;
		PowerStstr = mContext.getResources().getStringArray(R.array.PowerStstr);

		deviceParams = DeviceFactory.getInstance().getDevicesById(id);
		deviceID = deviceParams.getDeviceID();
		isLearnFlag = (deviceParams.getProductsCode().equals("4F")) ? true
				: false;
		findView();
		initTitleView();
		initRightButtonMenuAlert();
		if (isLearnFlag) {
			Toast.makeText(mContext, getString(R.string.four_f_msg),
					Toast.LENGTH_SHORT).show();
		}

	}


	private void initRightButtonMenuAlert() {

		if(isLearnFlag){
			rightButtonMenuAlert = new RightMenu(mContext, R.array.right_air_xx,
					R.drawable.right_cur_images);
		}else {
			rightButtonMenuAlert = new RightMenu(mContext, R.array.right_air,
					R.drawable.right_cur_images);
		}
		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {

			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub
				switch (position) {
					case 0:
						DeviceTimerActivity.startActivity(mContext, id);
						break;
					case 1:
                        ElectricActivity.startActivity(mContext,id);
						break;
					case 2: {
                        MoreActivity.startActivity(mContext,id);
					}
					break;
					case 3: {
						if(deviceParams.getProductsCode().equals("4F")) {
//							showarixuexi();
							StudyActivity.startActivity(mContext,deviceParams);
						}else{
							ToastUtils.ShowError(mContext,"请指定品牌是万能遥控",Toast.LENGTH_SHORT,true);
						}
					}
				}
				rightButtonMenuAlert.dismiss();
			}
		});
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TcpSender.setDeviceStateListener(this);
    }

    static int fanPicIndex = 0;

	private static void startTime() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (startflag) {
					try {
						Thread.sleep(400);
						handler.sendEmptyMessage(1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private void findView() {
		backTxt = (TextView) findViewById(R.id.backTxt);
		im_by = (ImageView) findViewById(R.id.air_by);
		setbackgroud(im_by, R.mipmap.img_air_direction_mid,
				R.mipmap.img_key_round_white);
		aiImage = (TextView) findViewById(R.id.ai_control);

		im_hot = (ImageView) findViewById(R.id.air_hot);
		setbackgroud(im_hot, R.mipmap.mode_warm_red,
				R.mipmap.img_key_round_white);
		im_cool = (ImageView) findViewById(R.id.air_cool);
		setbackgroud(im_cool, R.mipmap.mode_cold_blue,
				R.mipmap.img_key_round_white);
		im_temp_up = (Button) findViewById(R.id.air_temp_up);
		im_temp_down = (Button) findViewById(R.id.air_temp_down);
		im_power = (Button) findViewById(R.id.air_power);
		bt_fan = (Button) findViewById(R.id.air_speed);
		bt_model = (Button) findViewById(R.id.air_model);

		timeTxt = (TextView) findViewById(R.id.timeTxt);
		downTime = (Button) findViewById(R.id.downTime);
		upTime = (Button) findViewById(R.id.upTime);
		timeLayout = (LinearLayout) findViewById(R.id.timeLayout);
		msgTxt = (TextView) findViewById(R.id.msgTxt);
		timeSure = (Button) findViewById(R.id.timeSure);
		downTime.setOnClickListener(this);
		upTime.setOnClickListener(this);
		timeSure.setOnClickListener(this);

		im_show_speed = (ImageView) findViewById(R.id.air_wind_amount);
		temp0 = (ImageView) findViewById(R.id.temp0);
		temp1 = (ImageView) findViewById(R.id.temp1);
		im_show_by = (ImageView) findViewById(R.id.air_by_amount);
		im_show_model = (ImageView) findViewById(R.id.air_model_amount);
		tx_show_power = (TextView) findViewById(R.id.air_off_on);
		im_show_model.setVisibility(View.INVISIBLE);
		im_show_speed.setVisibility(View.INVISIBLE);
		im_show_by.setVisibility(View.INVISIBLE);
		//joe
		temp0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!powerFlag) {
					showVideoWindow("设定温度");
				}
			}
		});
		TextView tx_show_name = (TextView) findViewById(R.id.show_air);
		tx_show_name.setText(deviceParams.getRoomName()+deviceParams.getDeviceName());
		SendCMD.getInstance().sendCMD(255,"1",deviceParams);
	}

	@SuppressWarnings("deprecation")
	private void setbackgroud(ImageView im_by, int b2, int b1) {
		Bitmap bitmap2 = ((BitmapDrawable) getResources().getDrawable(b2))
				.getBitmap();
		Bitmap bitmap1 = ((BitmapDrawable) getResources().getDrawable(b1))
				.getBitmap();
		Drawable[] array = new Drawable[2];
		array[0] = new BitmapDrawable(bitmap1);
		array[1] = new BitmapDrawable(bitmap2);
		LayerDrawable la = new LayerDrawable(array);
		la.setLayerInset(0, 0, 0, 0, 0);
		la.setLayerInset(1, 40, 40, 40, 40);
		im_by.setImageDrawable(la);
	}

	private void initTitleView() {
		// im_om.setOnClickListener(songFanClick);
		im_by.setOnClickListener(songFanClick);
		im_power.setOnClickListener(butpowerClick);
		im_temp_up.setOnClickListener(butwinupClick);
		im_temp_down.setOnClickListener(butwindownClick);
		bt_fan.setOnClickListener(butfanClick);
		bt_model.setOnClickListener(butmodelClick);
		im_hot.setOnClickListener(buthotClick);
		im_cool.setOnClickListener(butcoolClick);
		aiImage.setOnClickListener(aiClick);
		backTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
            back();
			}
		});

	}
	public void back(){
		Intent resultIntent = new Intent();
		setResult(0, resultIntent);
		finish();
	}
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	back();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		TcpSender.setDeviceStateListener(this);
		SendCMD.getInstance().sendCMD(255,"1",deviceParams);
	}


	private OnClickListener songFanClick = new OnClickListener() {

		public void onClick(View v) {
			SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
					deviceParams.getDeviceName() + getString(R.string.funcontrol), deviceParams);

		}
	};
	private OnClickListener butpowerClick = new OnClickListener() {

		public void onClick(View v) {

			SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
					deviceParams.getDeviceName() + (powerFlag ? getString(R.string.open_1) : getString(R.string.close_1)),
					deviceParams);

		}
	};
	private OnClickListener butwinupClick = new OnClickListener() {

		public void onClick(View v) {

			SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
					deviceParams.getDeviceName() + getString(R.string.tempu), deviceParams);

		}
	};
	private OnClickListener butwindownClick = new OnClickListener() {

		public void onClick(View v) {

			SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
					deviceParams.getDeviceName() + getString(R.string.tempd), deviceParams);

		}
	};
	private OnClickListener butfanClick = new OnClickListener() {

		public void onClick(View v) {

			SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
					deviceParams.getDeviceName() + getString(R.string.speedswitch), deviceParams);

		}
	};

	private OnClickListener butmodelClick = new OnClickListener() {

		public void onClick(View v) {
			SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
					deviceParams.getDeviceName() + getString(R.string.modeswitch), deviceParams);

		}
	};
	private OnClickListener buthotClick = new OnClickListener() {

		public void onClick(View v) {
			SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
					deviceParams.getDeviceName() +getString(R.string.hot) , deviceParams);

		}
	};
	private OnClickListener butcoolClick = new OnClickListener() {

		public void onClick(View v) {
			SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
					deviceParams.getDeviceName() + getString(R.string.cool), deviceParams);
		}
	};
	private OnClickListener buttimerClick = new OnClickListener() {

		public void onClick(View v) {
			DeviceTimerActivity.startActivity(mContext, id);
		}
	};
	private OnClickListener aiClick = new OnClickListener() {

		public void onClick(View v) {
            rightButtonMenuAlert.show(aiImage);
		}
	};

	private static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {

				if (msg.what == 0) {
					tx_show_power.setText(PowerStstr[deviceParams.getState()]);
					if (deviceParams.getState() > 0) {
						powerFlag=false;
						im_power.setBackgroundResource(R.mipmap.power_wifi_blue);
						int temp=deviceParams.getDeviceAnalogVar3()&63;
						Glide.with(mContext)
								.load(tmperPic[temp/10])
								.crossFade()
								.into(temp0);
						Glide.with(mContext)
								.load(tmperPic[temp%10])
								.crossFade()
								.into(temp1);

						im_show_model.setVisibility(View.VISIBLE);
						im_show_speed.setVisibility(View.VISIBLE);
						im_show_by.setVisibility(View.VISIBLE);
					} else {
						powerFlag=true;
						im_show_model.setVisibility(View.INVISIBLE);
						im_show_speed.setVisibility(View.INVISIBLE);
						im_show_by.setVisibility(View.INVISIBLE);
						im_power.setBackgroundResource(R.mipmap.power_wifi);
						Glide.with(mContext)
								.load(tmperPic[0])
								.crossFade()
								.into(temp0);
						Glide.with(mContext)
								.load(tmperPic[0])
								.crossFade()
								.into(temp1);
					}
					Glide.with(mContext)
							.load(showPic[deviceParams
									.getDeviceAnalogVar2() & 15])
							.crossFade()
							.into(im_show_model);
					if (deviceParams.getDeviceAnalogVar2() / 16 == 0
							&& deviceParams.getState() > 0) {
						if(startflag==false) {
							startflag = true;
							startTime();
						}
					} else {
						startflag = false;
						Glide.with(mContext)
								.load(fanPic[deviceParams.getDeviceAnalogVar2() / 16])
								.crossFade()
								.into(im_show_speed);
					}
					im_show_by
							.setBackgroundResource((((deviceParams
									.getDeviceAnalogVar3() / 16) > 0) ? R.mipmap.wind_vertical_black
									: R.mipmap.img_air_direction_rock));
				} else if (msg.what == 1) {
					if(startflag){
					fanPicIndex++;
					if (fanPicIndex > 5) {
						fanPicIndex = 0;
					}
						Glide.with(mContext)
								.load(fanPic[fanPicIndex])
								.crossFade()
								.into(im_show_speed);
					 }
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		};
	};

	protected void onDestroy() {
		super.onDestroy();
        TcpSender.setDeviceStateListener(null);
	}
	@Override
	public void receiveDeviceStatus(String cmd) {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(0);
	}

	private void showVideoWindow(final String _title ) {

		final AlertViewWindow alertViewWindow=new AlertViewWindow(mContext,_title,ListNumBer.getTemp(18,31),null,deviceParams.getLoadImageIndex()-1);
		alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
			@Override
			public void onItem(int pos, String itemName) {
				SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
						deviceParams.getDeviceName() + getString(R.string.tempC)+itemName, deviceParams);
				alertViewWindow.dismiss();
			}
		});
		alertViewWindow.show();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.downTime: {
				if (time > 60) {
					time -= 60;
				}
				else if(time==60||time==30){
					time -= 30;
				}
				if (time <= 0) {
					timeLayout.setVisibility(View.GONE);
				}
				Hour = AppTools.getHour();
				int hour = time / 60;
				int minit = time % 60;
				timeTxt.setText(hour + "小时" + minit + "分钟");
				int total = Hour + hour * 60 + minit;
				if (total / 60 >= 24) {
					msgTxt.setText("将会在执行" + (total / 60 - 24) + ":" + (total % 60) + "定时");
				} else {
					msgTxt.setText("将会在执行" + total / 60 + ":" + (total % 60) + "定时");
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
				Hour = AppTools.getHour();
				int hour = time / 60;
				int minit = time % 60;
				timeLayout.setVisibility(View.VISIBLE);
				timeTxt.setText(hour + "小时" + minit + "分钟");
				int total = Hour + hour * 60 + minit;
				if (total / 60 >= 24) {
					msgTxt.setText("将会在执行" + (total / 60 - 24) + ":" + (total % 60) + "定时");
				} else {
					msgTxt.setText("将会在执行" + total / 60 + ":" + (total % 60) + "定时");
				}

			}
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
		}
	}
}
