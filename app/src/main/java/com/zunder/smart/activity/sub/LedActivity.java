package com.zunder.smart.activity.sub;

import com.zunder.smart.R;
import com.zunder.smart.activity.sub.set.DeviceTimerActivity;
import com.zunder.smart.activity.sub.set.ElectricActivity;
import com.zunder.smart.activity.sub.set.MoreActivity;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.RedInfraFactory;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.Device;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.SendThread;
import com.zunder.smart.service.TcpSender;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class LedActivity extends BaseActivity implements OnClickListener{
	private static Activity mContext;
	private TextView backTxt;
	private TextView timeSet;



	private static String dio;
	private static String did;
	private static String mid1;
	private static String mid2;
	private static String dtype;
	private static String cmd;
	private String dname;
	private static String blakCode;
	boolean IsLongClick = false;
	static int id;
	static Device deviceParams = null;
	private Button buttonColor;
	private Button buttons0;
	private Button buttons1;
	private Button buttons2;
	private Button buttons3;
	private Button buttons4;
	private Button buttons5;
	private Button buttons6;
	private Button buttons7;
	private Button buttons8;
	private Button buttons9;
	private Button buttons10;
	private Button buttons11;
	private Button[] buttons;
	private RightMenu rightButtonMenuAlert;
	private static int panIndex=0;
	public static void startActivity(Activity activity, int _id) {
		id = _id;
		Intent intent = new Intent(activity, LedActivity.class);
		activity.startActivityForResult(intent,100);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_led);
		mContext = this;
		TcpSender.setDeviceStateListener(this);
		deviceParams = DeviceFactory.getInstance().getDevicesById(id);
		dname = deviceParams.getDeviceName();
		dtype = deviceParams.getProductsCode();
		did = deviceParams.getDeviceID().substring(0, 2);
		mid1 = deviceParams.getDeviceID().substring(2, 4);
		mid2 = deviceParams.getDeviceID().substring(4, 6);
		dio = deviceParams.getDeviceIO();
		findView();
		showPlayTitleView();

	}

	static	ImageButton bt_colorpan = null;
	int value = 0;

	private void findView() {
		// TODO Auto-generated method stub

		backTxt = (TextView) findViewById(R.id.backTxt);
		timeSet = (TextView) findViewById(R.id.timeSet);
		buttonColor = (Button) findViewById(R.id.buttonColor);
		buttons0 = (Button) findViewById(R.id.buttons0);
		buttons1 = (Button) findViewById(R.id.buttons1);
		buttons2 = (Button) findViewById(R.id.buttons2);
		buttons3 = (Button) findViewById(R.id.buttons3);
		buttons4 = (Button) findViewById(R.id.buttons4);
		buttons5 = (Button) findViewById(R.id.buttons5);
		buttons6 = (Button) findViewById(R.id.buttons6);
		buttons7 = (Button) findViewById(R.id.buttons7);
		buttons8 = (Button) findViewById(R.id.buttons8);
		buttons9 = (Button) findViewById(R.id.buttons9);
		buttons10 = (Button) findViewById(R.id.buttons10);
		buttons11 = (Button) findViewById(R.id.buttons11);
		buttons0.setOnClickListener(this);
		buttons1.setOnClickListener(this);
		buttons2.setOnClickListener(this);
		buttons3.setOnClickListener(this);
		buttons4.setOnClickListener(this);
		buttons5.setOnClickListener(this);
		buttons6.setOnClickListener(this);
		buttons7.setOnClickListener(this);
		buttons8.setOnClickListener(this);
		buttons9.setOnClickListener(this);
		buttons10.setOnClickListener(this);
		buttons11.setOnClickListener(this);
		buttons=new Button[]{buttons0,buttons1,buttons2,buttons3,buttons4,buttons5,buttons6,buttons7,buttons8,buttons9};

		bt_colorpan = (ImageButton) findViewById(R.id.colorpan);
		TextView show_colorled = (TextView) findViewById(R.id.titleTxt);

		show_colorled.setText(deviceParams.getRoomName()+dname);


		backTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				back();
			}
		});
		timeSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rightButtonMenuAlert.show(timeSet);
			}
		});
		initRightButtonMenuAlert();
	}
	private void initRightButtonMenuAlert() {

		rightButtonMenuAlert = new RightMenu(mContext, R.array.right_led,
					R.drawable.right_menu_images);
		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {

			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub
				switch (position) {
					case 0:
						DeviceTimerActivity.startActivity(mContext,deviceParams.getId());
						break;
					case 1:
						MoreActivity.startActivity(mContext,deviceParams.getId());
						break;
				}
				rightButtonMenuAlert.dismiss();
			}
		});

	}
	private void showPlayTitleView() {
		// TODO Auto-generated method stub
		bt_colorpan.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int posx = 0, posy = 0;
				posy = (bt_colorpan.getHeight() / 2);
				posx = (bt_colorpan.getWidth() / 2);
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if ((event.getX() > (posx - 35))
							&& (event.getX() < (posx + 35))
							&& (event.getY() > (posy - 35))
							&& (event.getY() < (posy + 35))) {

						SendCMD.getInstance().sendCmd(250,deviceParams.getRoomName()+dname+getString(R.string.open_1),deviceParams);

						IsLongClick = true;
					} else {
						IsLongClick = false;
						double angle = (Math.atan2(event.getY() - posy,
								event.getX() - posx) * 180)
								/ Math.PI;
						if(panIndex==1){
							String[] array = getString(R.string.colors).split(",");
							// <string name="colors">红色,紫色,蓝色,青色,绿色,黄色,橙色</string>
							int index = 0;
						if(angle<-145|angle>170){
							index = 0;
							buttonColor.setBackgroundColor(mContext.getResources().getColor(R.color.red));
						}else if(angle<-120){
							index = 1;
							buttonColor.setBackgroundColor(mContext.getResources().getColor(R.color.zs));
						}else if(angle<-65){
							index = 2;
							buttonColor.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
						}else if(angle<0){
							index = 3;
							buttonColor.setBackgroundColor(mContext.getResources().getColor(R.color.qs));
						}else if(angle<65){
							index = 4;
							buttonColor.setBackgroundColor(mContext.getResources().getColor(R.color.iask_text_green));
						}else if(angle<120){
							index = 5;
							buttonColor.setBackgroundColor(mContext.getResources().getColor(R.color.possible_result_points));
						} else {
							buttonColor.setBackgroundColor(mContext.getResources().getColor(R.color.orange));
							index = 6;
							}
							SendCMD.getInstance().sendCmd(250,deviceParams.getRoomName()+dname+array[index],deviceParams);
							}
							else{
							if(angle<0&&angle>=-90)		{
								int colorval = ((int) (((angle + 90) * 100) / 360));
								SendCMD.getInstance().sendCmd(250, deviceParams.getRoomName()+dname + colorval, deviceParams);
							}else if(angle>0&&angle<=180){
								int colorval = ((int) (((angle + 90) * 100) / 360));
								SendCMD.getInstance().sendCmd(250, deviceParams.getRoomName()+dname + colorval, deviceParams);
							}else {
								int colorval = ((int) (((angle + 450) * 100) / 360));
								SendCMD.getInstance().sendCmd(250, deviceParams.getRoomName()+dname + colorval, deviceParams);
							}
						}
					}
					playKeytone();
					break;
				case MotionEvent.ACTION_MOVE:

					break;
				case MotionEvent.ACTION_UP:
					IsLongClick = false;
					break;
				default:
					break;
				}
				return false;
			}
		}); }

	public void back(){
		Intent resultIntent = new Intent();
		setResult(0, resultIntent);
		finish();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	private void playKeytone() {

		MediaPlayer.create(mContext, R.raw.ping_short).start();

	}

	public Handler handler = new Handler() {
		@SuppressLint("ResourceAsColor")
		public void handleMessage(android.os.Message msg) {
			try {
				if (msg.what == 0) {
					int index = deviceParams.getDeviceAnalogVar3() - 1;
					for (int i = 0; i < buttons.length; i++) {
						if (index == i) {
							buttons[i].setBackgroundResource(R.drawable.button_boder_shape_red);
						} else {
							buttons[i].setBackgroundResource(R.drawable.button_boder_shape);
						}
					}

				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		};
	};


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		TcpSender.setDeviceStateListener(this);
		SendCMD.getInstance().sendCMD(255,"1",deviceParams);
	}

	@Override
	public void receiveDeviceStatus(String cmd) {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(0);
		deviceParams = DeviceFactory.getInstance().getDevicesById(id);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.buttons0:
				SendCMD.getInstance().sendCmd(250, deviceParams.getRoomName()+dname + "情景",
						deviceParams);
				break;
			case R.id.buttons1:
				SendCMD.getInstance().sendCmd(250, deviceParams.getRoomName()+dname + "模式1",
						deviceParams);
				break;
			case R.id.buttons2:
				SendCMD.getInstance().sendCmd(250, deviceParams.getRoomName()+dname + "模式2",
						deviceParams);
				break;
			case R.id.buttons3:
				SendCMD.getInstance().sendCmd(250, deviceParams.getRoomName()+dname + "模式3",
						deviceParams);
				break;
			case R.id.buttons4:
				SendCMD.getInstance().sendCmd(250, deviceParams.getRoomName()+dname + "模式4",
						deviceParams);
				break;
			case R.id.buttons5:
				SendCMD.getInstance().sendCmd(250, deviceParams.getRoomName()+dname + "模式5",
						deviceParams);
				break;
			case R.id.buttons6:
				SendCMD.getInstance().sendCmd(250, deviceParams.getRoomName()+dname + "模式6",
						deviceParams);
				break;
			case R.id.buttons7:
				SendCMD.getInstance().sendCmd(250, deviceParams.getRoomName()+dname + "模式7",
						deviceParams);
				break;
			case R.id.buttons8:
				SendCMD.getInstance().sendCmd(250, deviceParams.getRoomName()+dname + "模式8",
						deviceParams);
				break;
			case R.id.buttons9:
				SendCMD.getInstance().sendCmd(250, deviceParams.getRoomName()+dname + "打开",
						deviceParams);
				break;
			case R.id.buttons10:
				SendCMD.getInstance().sendCmd(250, deviceParams.getRoomName()+dname + " 闪烁",
						deviceParams);
				break;
			case R.id.buttons11:
				SendCMD.getInstance().sendCmd(250, deviceParams.getRoomName()+dname + "关闭",
						deviceParams);
				break;
		}
		playKeytone();
	}
}
