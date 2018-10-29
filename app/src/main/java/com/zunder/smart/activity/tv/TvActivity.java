package com.zunder.smart.activity.tv;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.activity.sub.BaseActivity;
import com.zunder.smart.activity.sub.set.DeviceTimerActivity;
import com.zunder.smart.activity.sub.set.ElectricActivity;
import com.zunder.smart.activity.sub.set.MoreActivity;
import com.zunder.smart.activity.sub.set.StudyActivity;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dialog.TimeAlert;
import com.zunder.smart.dialog.WarnDialog;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.Device;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.SendThread;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.utils.ListNumBer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TvActivity extends BaseActivity implements OnClickListener,
		OnLongClickListener {
	private static Activity mContext;
	private Button btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7,
			btn_8, btn_9, btn_back_track, btn_up, btn_down, btn_left,
			btn_right, btn_ok, btn_vol_up, btn_vol_down, btn_ch_up,
			btn_ch_down, mute_btn, digit_btn, btn_box;
	private static Button btn_power, tempAdd, tempLow;

	private Button btn_home, btn_local, btn_select, btn_back, prePage,
			nextPage, btn_set, btn_tv;
	private Button f1_btn;
	private Button f2_btn;
	private Button f3_btn;
	private Button f4_btn;
	private static Boolean powerFlag = false;
	static String deviceID = "000000";
	static int id = 0;
	String did = "00";
	String mid1 = "00";
	String mid2 = "00";
	String dtype = "00";
	TextView editeTxt, backTxt, titleTxt;
	private RightMenu rightButtonMenuAlert;
	static Device deviceParams = null;
	String dname = "";
	boolean IsLongClick = false;
	static TimeAlert alertTime;

	public static void startActivity(Activity activity, int _id) {
		id = _id;
		Intent intent = new Intent(activity, TvActivity.class);
		activity.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_tv);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mContext = this;
		TcpSender.setDeviceStateListener(this);
		TcpSender.setElectricListeener(this);
		deviceParams = DeviceFactory.getInstance().getDevicesById(id);
		deviceID = deviceParams.getDeviceID();
		did = deviceID.substring(0, 2);
		mid1 = deviceID.substring(2, 4);
		mid2 = deviceID.substring(4, 6);
		editeTxt = (TextView) findViewById(R.id.editeTxt);
		backTxt = (TextView) findViewById(R.id.backTxt);
		titleTxt = (TextView) findViewById(R.id.titleTxt);

		backTxt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btn_power = (Button) findViewById(R.id.btn_power);
		btn_box = (Button) findViewById(R.id.green_btn);
		btn_0 = (Button) findViewById(R.id.zero_btn);
		btn_1 = (Button) findViewById(R.id.one_btn);
		btn_2 = (Button) findViewById(R.id.two_btn);
		btn_3 = (Button) findViewById(R.id.three_btn);
		btn_4 = (Button) findViewById(R.id.four_btn);
		btn_5 = (Button) findViewById(R.id.five_btn);
		btn_6 = (Button) findViewById(R.id.six_btn);
		btn_7 = (Button) findViewById(R.id.seven_btn);
		btn_8 = (Button) findViewById(R.id.eight_btn);
		btn_9 = (Button) findViewById(R.id.nine_btn);

		digit_btn = (Button) findViewById(R.id.digit_btn);
		btn_back_track = (Button) findViewById(R.id.backtrack_btn);
		btn_up = (Button) findViewById(R.id.up_btn);
		btn_down = (Button) findViewById(R.id.down_btn);
		btn_left = (Button) findViewById(R.id.left_btn);
		btn_right = (Button) findViewById(R.id.right_btn);
		btn_ok = (Button) findViewById(R.id.ok_btn);
		btn_vol_up = (Button) findViewById(R.id.volume_add_btn);
		btn_vol_down = (Button) findViewById(R.id.volume_reduce_btn);
		btn_ch_up = (Button) findViewById(R.id.channel_add_btn);
		btn_ch_down = (Button) findViewById(R.id.channel_reduce_btn);
		mute_btn = (Button) findViewById(R.id.mute_btn);

		btn_home = (Button) findViewById(R.id.home_btn);
		btn_local = (Button) findViewById(R.id.local_btn);
		btn_select = (Button) findViewById(R.id.select_btn);
		btn_back = (Button) findViewById(R.id.back_btn);

		prePage = (Button) findViewById(R.id.prePage);
		nextPage = (Button) findViewById(R.id.nextPage);
		btn_set = (Button) findViewById(R.id.setBtn);
		btn_tv = (Button) findViewById(R.id.tvBtn);
		f1_btn = (Button) findViewById(R.id.f1_btn);
		f2_btn = (Button) findViewById(R.id.f2_btn);
		f3_btn = (Button) findViewById(R.id.f3_btn);
		f4_btn = (Button) findViewById(R.id.f4_btn);

		tempAdd = (Button) findViewById(R.id.tempAdd);
		tempLow = (Button) findViewById(R.id.tempLow);
		onclickLister();

		if (deviceParams != null) {
			dname = deviceParams.getDeviceName();
			titleTxt.setText(deviceParams.getRoomName()+dname);
			deviceID = deviceParams.getDeviceID();
			dtype = deviceParams.getProductsCode();
			did = deviceID.substring(0, 2);
			mid1 = deviceID.substring(2, 4);
			mid2 = deviceID.substring(4, 6);
		}
		initRightButtonMenuAlert();
		alertTime = new TimeAlert(mContext);
		alertTime.setSureListener(new TimeAlert.OnSureListener() {

			@Override
			public void onCancle() {
				// TODO Auto-generated method stub
				alertTime.diss();
			}
		});
		SendCMD.getInstance().sendCMD(255, "1",
				deviceParams);
	}
	public void onclickLister() {
		editeTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rightButtonMenuAlert.show(editeTxt);

			}
		});
		btn_power.setOnClickListener(this);
		btn_box.setOnClickListener(this);
		btn_0.setOnClickListener(this);
		btn_1.setOnClickListener(this);
		btn_2.setOnClickListener(this);
		btn_3.setOnClickListener(this);
		btn_4.setOnClickListener(this);
		btn_5.setOnClickListener(this);
		btn_6.setOnClickListener(this);
		btn_7.setOnClickListener(this);
		btn_8.setOnClickListener(this);
		btn_9.setOnClickListener(this);
		digit_btn.setOnClickListener(this);
		btn_back_track.setOnClickListener(this);
		btn_up.setOnClickListener(this);
		btn_down.setOnClickListener(this);
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
		btn_vol_up.setOnClickListener(this);
		btn_vol_down.setOnClickListener(this);
		btn_ch_up.setOnClickListener(this);
		btn_ch_down.setOnClickListener(this);
		mute_btn.setOnClickListener(this);
		btn_home.setOnClickListener(this);
		btn_local.setOnClickListener(this);
		btn_select.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		prePage.setOnClickListener(this);
		nextPage.setOnClickListener(this);
		btn_set.setOnClickListener(this);
		btn_tv.setOnClickListener(this);
		f1_btn.setOnClickListener(this);
		f2_btn.setOnClickListener(this);
		f3_btn.setOnClickListener(this);
		f4_btn.setOnClickListener(this);
		tempAdd.setOnClickListener(this);
		tempLow.setOnClickListener(this);
		btn_power.setOnLongClickListener(this);
		btn_tv.setOnLongClickListener(this);
		btn_home.setOnLongClickListener(this);
		btn_local.setOnLongClickListener(this);
		btn_select.setOnLongClickListener(this);
		btn_back.setOnLongClickListener(this);

		nextPage.setOnLongClickListener(this);
		prePage.setOnLongClickListener(this);
		btn_set.setOnLongClickListener(this);
		f1_btn.setOnLongClickListener(this);
		f2_btn.setOnLongClickListener(this);
		f3_btn.setOnLongClickListener(this);
		f4_btn.setOnLongClickListener(this);
		tempAdd.setOnLongClickListener(this);
		tempLow.setOnLongClickListener(this);
	}

	@SuppressLint("ResourceAsColor")
	private void initRightButtonMenuAlert() {
		if (dtype.equals("64")) {
			rightButtonMenuAlert = new RightMenu(mContext, R.array.right_tv_xx,
					R.drawable.right_tv_images);

		}else{
			rightButtonMenuAlert = new RightMenu(mContext, R.array.right_tv,
					R.drawable.right_tv_images);
		}

		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {

			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
						DeviceTimerActivity.startActivity(mContext,deviceParams.getId());
					break;
				case 1:
						ElectricActivity.startActivity(mContext,deviceParams.getId());
					break;
				case 2:
					MoreActivity.startActivity(mContext,deviceParams.getId());
					break;
				case 3:
					if (dtype.equals("64")) {
						StudyActivity.startActivity(mContext,deviceParams);
					}else{
						ToastUtils.ShowError(mContext,"请指定品牌是万能遥控",Toast.LENGTH_SHORT,true);
					}
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

	}

	protected void onDestroy() {
		super.onDestroy();
		TcpSender.setElectricListeener(null);
	};

	String cmd = "";

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (IsLongClick == true) {
			IsLongClick = false;
			return;
		}
		switch (v.getId()) {
		case R.id.btn_power:
			SendCMD.getInstance().sendCMD(250,deviceParams.getRoomName()+
					deviceParams.getDeviceName() + (powerFlag ? getString(R.string.open_1) : getString(R.string.close_1)),
					deviceParams);
			powerFlag = !powerFlag;
			SendThread send = SendThread.getInstance(cmd);
			new Thread(send).start();
			break;

		case R.id.green_btn:

			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.devicepower), deviceParams);
			break;
		case R.id.zero_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.key0), deviceParams);
			break;
		case R.id.one_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname +getString(R.string.key1), deviceParams);
			break;
		case R.id.two_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.key2), deviceParams);
			break;
		case R.id.three_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.key3), deviceParams);
			break;
		case R.id.four_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname +getString(R.string.key4), deviceParams);
			break;
		case R.id.five_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.key5), deviceParams);
			break;
		case R.id.six_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname +getString(R.string.key6) , deviceParams);
			break;
		case R.id.seven_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.key7), deviceParams);
			break;
		case R.id.eight_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname +getString(R.string.key8) , deviceParams);
			break;
		case R.id.nine_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.key9), deviceParams);
			break;
		case R.id.backtrack_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.goback), deviceParams);
			break;
		case R.id.digit_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.single_source), deviceParams);
			break;
		case R.id.mute_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.mute1), deviceParams);
			break;
		case R.id.volume_add_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.voladd), deviceParams);
			break;
		case R.id.volume_reduce_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.voldown), deviceParams);
			break;
		case R.id.channel_add_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname +getString(R.string.chadd) , deviceParams);
			break;
		case R.id.channel_reduce_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.chdown), deviceParams);
			break;
		case R.id.ok_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.enter), deviceParams);
			break;
		case R.id.left_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.keyleft), deviceParams);
			break;
		case R.id.right_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.keyright), deviceParams);
			break;
		case R.id.up_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname +getString(R.string.keyup), deviceParams);
			break;
		case R.id.down_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.keydown), deviceParams);
			break;
		case R.id.f1_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.kc0), deviceParams);
			break;
		case R.id.f2_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname +getString(R.string.kc1) , deviceParams);
			break;
		case R.id.f3_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.kc2), deviceParams);
			break;
		case R.id.f4_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.kc3), deviceParams);
			break;
		case R.id.home_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.kc4), deviceParams);
			break;
		case R.id.local_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.kc5), deviceParams);
			break;
		case R.id.tvBtn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname +getString(R.string.kc6) , deviceParams);
			break;
		case R.id.setBtn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.kc7), deviceParams);
			break;
		case R.id.prePage:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.kc8), deviceParams);
			break;
		case R.id.back_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname +getString(R.string.kc9), deviceParams);
			break;
		case R.id.select_btn:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.kc10), deviceParams);
			break;
		case R.id.tempAdd:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.kc11), deviceParams);
			break;
		case R.id.tempLow:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname +getString(R.string.kc12), deviceParams);
			break;
		case R.id.nextPage:
			SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.kc13), deviceParams);
			break;
		default:
			break;
		}
		playKeytone();
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TcpSender.setDeviceStateListener(this);
        TcpSender.setElectricListeener(this);
    }
	@Override
	public void setElectric(String cmd) {
		// TODO Auto-generated method stub
		String cmdType = cmd.substring(0, 2);
		//*C0008(ACT)(TYPE)(ID)(MID)(MID)
		if (cmdType.equals("*C")) {
			if(cmd.substring(10,16).equals(deviceID)) {
				String cmdNum=cmd.substring(4,6);
				String act = cmd.substring(6, 8);
				if((cmdNum.equals("08"))&&(act.equals("E0"))) {
					Message message = handler.obtainMessage();
					message.what = 1;
					message.obj = cmd;
					handler.sendMessage(message);
				}
			}
		}
	}

	private static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				if (msg.what == 0) {
					int powerSt = deviceParams.getState();
					if (powerSt > 0) {
						btn_power.setTextColor(mContext.getResources().getColor(R.color.font_blue_press));
						powerFlag = true;
					} else {
						btn_power.setTextColor(mContext.getResources().getColor(R.color.font_close_press));
						powerFlag = false;
					}
				} else if (msg.what == 1) {
					String cmd = msg.obj.toString();
					//*C0008(ACT)(TYPE)(ID)(MID)(MID)(mem)(LenH)(LenL)(St)
					String state=cmd.substring(22,24);
					String MsgText="红外學习壓縮不完整";
					if(state.equals("00")){ //成功
						String value1= Integer.parseInt(cmd.substring(18,22),16)+"";
						MsgText=mContext.getString(R.string.studySuccess)
								+ value1
								+ mContext.getString(R.string.rf_byte);
					}
					Toast.makeText(	mContext,MsgText,Toast.LENGTH_SHORT).show();
					if (alertTime.isShow()) {
						alertTime.diss();
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		};
	};

	@Override
	public void receiveDeviceStatus(String cmd) {
		// TODO Auto-generated method stub
		// deviceParams = DeviceFactory.getDevicesById(id);
		handler.sendEmptyMessage(0);

	}


	public void showProgressDialog() {
		if (progressDialog == null) {

			progressDialog = ProgressDialog.show(mContext,
					mContext.getString(R.string.tip),
					mContext.getString(R.string.loading), true, false);
		}
		progressDialog.show();

	}

	ProgressDialog progressDialog = null;

	public void hideProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}

	}


	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		IsLongClick = true;
		String commandID=did + mid1 + mid2;
		switch (v.getId()) {
		case R.id.btn_power:
			final	AlertViewWindow alertViewWindow=new AlertViewWindow(mContext,mContext.getString(R.string.study_on_off),ListNumBer.getStudy(),null,0);
			alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
				@Override
				public void onItem(int pos, String itemName) {
					String commandID=did + mid1 + mid2;
					if(pos==0) {
						cmd = "*C0019FA" + dtype + commandID + "E0400000" ;
					}else if(pos==1){
						cmd = "*C0019FA" + dtype + commandID + "E03F0000";
					}else if(pos==2){
						cmd = "*C0019FA" + dtype + commandID + "E03E0000";
					}
					if(!cmd.equals("")) {
						SendThread send = SendThread.getInstance(cmd);
						new Thread(send).start();
						alertTime.show(mContext.getString(R.string.keyStudy));
						alertViewWindow.dismiss();
					}
					cmd="";
				}
			});
			alertViewWindow.show();
			break;
		case R.id.f1_btn:
			cmd = "*C0019FA05" + commandID + "E0180000";
			break;
		case R.id.f2_btn:
			cmd = "*C0019FA05" + commandID + "E0190000" ;
			break;
		case R.id.f3_btn:
			cmd = "*C0019FA05" + commandID + "E01A0000" ;
			break;
		case R.id.f4_btn:
			cmd = "*C0019FA05" + commandID + "E01B0000" ;
			break;
		case R.id.home_btn:
			cmd = "*C0019FA05" + commandID + "E01C0000" ;
			break;
		case R.id.local_btn:
			cmd = "*C0019FA05" + commandID + "E01D0000" ;
			break;
		case R.id.tvBtn:
			cmd = "*C0019FA05" + commandID + "E01E0000" ;
			break;
		case R.id.setBtn:
			cmd = "*C0019FA05" + commandID + "E01F0000" ;
			break;
		case R.id.prePage:
			cmd = "*C0019FA05" + commandID + "E0200000" ;
			break;

		case R.id.back_btn:
			cmd = "*C0019FA05" + commandID + "E0210000" ;
			break;
		case R.id.select_btn:
			cmd = "*C0019FA05" + commandID + "E0220000" ;

		case R.id.tempAdd:
			cmd = "*C0019FA05" + commandID + "E0230000" ;
			break;
		case R.id.tempLow:
			cmd = "*C0019FA05" + commandID + "E0240000" ;
			break;
		case R.id.nextPage:
			cmd = "*C0019FA05" + commandID + "E0250000" ;
			break;
		default:
			break;
		}
		if(!cmd.equals("")) {
			alertTime.show(mContext.getString(R.string.keyStudy));
			SendThread send = SendThread.getInstance(cmd);
			new Thread(send).start();
		}
		return false;
	}

	private void playKeytone() {
		MediaPlayer.create(mContext, R.raw.ping_short).start();
	}

	static WarnDialog warnDialog = null;
	public void showDialog(){
		if (warnDialog == null) {
			warnDialog = new WarnDialog(mContext,getString(R.string.tip));
			warnDialog.setMessage(getString(R.string.start_down_red));
			warnDialog.setSureListener(new WarnDialog.OnSureListener() {
				@Override
				public void onCancle() {
					warnDialog.dismiss();
				}
			});
		}
			warnDialog.show();
	}
	private static boolean searchflag = false;
	private int startCount = 0;
	private void startTime() {
		startCount=0;
		searchflag=true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (searchflag) {
					try {
						Thread.sleep(1000);
						startCount++;
						if (startCount >= 5) {
							searchflag = false;
							if ((warnDialog != null) && warnDialog.isShowing()) {
								warnDialog.dismiss();

							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}


}
