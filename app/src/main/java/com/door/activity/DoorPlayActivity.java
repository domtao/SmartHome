package com.door.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bluecam.api.CameraPlayActivity;
import com.door.Utils.ToastUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dao.impl.factory.MediaFactory;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.dialog.ButtonAlert;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.activity.device.DeviceFragment;
import com.zunder.smart.gateway.GateWayPlayActivity;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.Mode;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.service.GateWayService;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.utils.ListNumBer;
import com.p2p.core.BaseMonitorActivity;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
import com.p2p.core.P2PView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ResourceAsColor")
public class DoorPlayActivity extends BaseMonitorActivity implements OnClickListener {

	public static void startActivity(Context activity,String getUserName,String getUserPassWord,int type){
		Intent intent=new Intent(activity,DoorPlayActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("Id",getUserName);
		intent.putExtra("Pwd",getUserPassWord);
		intent.putExtra("type",type);
		activity.startActivity(intent);
	}
	public static String P2P_ACCEPT = "com.XXX.P2P_ACCEPT";
	public static String P2P_READY = "com.XXX.P2P_READY";
	public static String P2P_REJECT = "com.XXX.P2P_REJECT";
	private ProgressDialog mProgressDialog;

	GateWay gateWay=null;
	RelativeLayout rl_p2pview;
	public static Activity activity;
	private Button tempButton;
	private ImageButton clarityVideo, listnerVideo;
	private ImageButton switchVideo, changeVideo, pointVideo, allscreen;
	RelativeLayout cloundBtn, videoBtn;
	TextView cloundTxt, videoTxt;
	RelativeLayout otherLayout;
	TextView listnerTxt,talkTxt;
	private boolean isVideo = true;
	private String callID, CallPwd;
	private String userId;
	private boolean isMute = true;
	OrientationEventListener mOrientationEventListener;
	private boolean mIsLand = false;
	private int screenWidth, screenHeigh;
	TextView backTxt,titleTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_monito);
		SharedPreferences sp=getSharedPreferences("door_info",MODE_PRIVATE);
		userId = sp.getString("userId",null);
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage(activity.getString(R.string.loadingvideo));

//		initData();
		callID = getIntent().getStringExtra("Id");
		CallPwd = getIntent().getStringExtra("Pwd");
		gateWay= GateWayFactory.getInstance().getGateWay(callID);
		initView();
		regFilter();
		String pwd = P2PHandler.getInstance().EntryPassword(CallPwd);//经过转换后的设备密码
		P2PHandler.getInstance().call(userId, pwd, true, 1, callID, "", "", 2, callID);
		showProgress();
		if(getIntent().getIntExtra("type",0)==13){
			titleTxt.setText(activity.getString(R.string.visitor));
			MediaFactory.getInstance(activity).init();
		}
	}

	public void regFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(P2P_REJECT);
		filter.addAction(P2P_ACCEPT);
		filter.addAction(P2P_READY);
		registerReceiver(mReceiver, filter);
	}
	@Override
	protected void onCaptureScreenResult(boolean isSuccess, int prePoint) {
		if (isSuccess) {
			ToastUtils.ShowSuccess(this, getString(R.string.screenshot_success), Toast.LENGTH_LONG, true);
		} else {
			ToastUtils.ShowError(this, getString(R.string.screenshot_error), Toast.LENGTH_LONG, true);
		}
	}

	@Override
	protected void onVideoPTS(long videoPTS) {

	}

	FrameLayout[] layouts = null;
	int index = 0;

	boolean isTalk=false;
	private void initView() {
		backTxt=(TextView)findViewById(R.id.backTxt);
		titleTxt=(TextView)findViewById(R.id.titleTxt);
		rl_p2pview=(RelativeLayout)findViewById(R.id.rl_p2pview) ;
		pView=(P2PView)findViewById(R.id.p2pview);
		initP2PView(7, P2PView.LAYOUTTYPE_TOGGEDER);//7是设备类型(技威定义的)
		listnerTxt = (TextView) findViewById(R.id.listnerTxt);
		talkTxt = (TextView) findViewById(R.id.talkTxt);
		allscreen = (ImageButton) findViewById(R.id.allscreen);
		otherLayout = (RelativeLayout) findViewById(R.id.otherLayout);

		// SCREEN_ORIENTATION_PORTRAIT

		allscreen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isTalk){
					MediaFactory.getInstance(activity).stop();
					setMute(false);
					isTalk=true;
					talkTxt.setText(getString(R.string.tallking));
					talkTxt.setTextColor(R.color.red);
				}else{
					setMute(true);
					isTalk=false;
					talkTxt.setText(activity.getString(R.string.talking));
					talkTxt.setTextColor(R.color.black);
				}
			}
		});

		cloundTxt = (TextView) findViewById(R.id.cloudTxt);
		videoTxt = (TextView) findViewById(R.id.videoTxt);

		cloundBtn = (RelativeLayout) findViewById(R.id.cloudBtn);
		videoBtn = (RelativeLayout) findViewById(R.id.videoBtn);

		clarityVideo = (ImageButton) findViewById(R.id.clarityVideo);
		changeVideo = (ImageButton) findViewById(R.id.changeVideo);
		switchVideo = (ImageButton) findViewById(R.id.switchVideo);
		pointVideo = (ImageButton) findViewById(R.id.pointVideo);
		listnerVideo = (ImageButton) findViewById(R.id.listnerVideo);
		listnerVideo.setOnClickListener(this);
		cloundTxt.setOnClickListener(this);
		videoTxt.setOnClickListener(this);
		changeVideo.setOnClickListener(this);
		switchVideo.setOnClickListener(this);
		clarityVideo.setOnClickListener(this);
		pointVideo.setOnClickListener(this);
		tempButton = (Button) findViewById(R.id.temp_aduio);
		tempButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						MediaFactory.getInstance(activity).stop();
						setMute(false);
						return true;
					case MotionEvent.ACTION_UP:
						setMute(true);
						return true;
				}
				return false;
			}
		});

		backTxt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MediaFactory.getInstance(activity).stop();
				finish();
			}
		});
		pointVideo.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {

				final ButtonAlert buttonAlert = new ButtonAlert(activity);
				buttonAlert.setTitle(getString(R.string.bind_device));
				buttonAlert.setButton(getString(R.string.sence1), getString(R.string.device), getString(R.string.cancle));
				buttonAlert
						.setVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE);
				buttonAlert.setOnSureListener(new ButtonAlert.OnSureListener() {
					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						buttonAlert.dismiss();
						List<Mode> list= ModeFactory.getInstance().getAll();
						ModeDialog  dsf = new ModeDialog(activity, list);
						dsf.setItemListen(new ModeDialog.OnItemListen() {
							@Override
							public void OnItemClick(String item) {
								if(gateWay!=null){
									gateWay.setGateWayPoint(item);
									MyApplication.getInstance().getWidgetDataBase().updateGateWay(gateWay,gateWay.getGatewayName());
								}
							}
						});
						dsf.show();
					}
					@Override
					public void onSearch() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub
					}
				});
				buttonAlert.show();
				return false;
			}
		});

		int d = P2PHandler.getInstance().setScreenShotpath(MyApplication.getInstance().getRootPath(), callID+".jpg");
		captureScreen(-1);
	}
	public void getScreenWithHeigh() {
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeigh = dm.heightPixels;
	}
	int selitem = 0;

	// ���������
	public boolean onKeyDown(int keyCode, KeyEvent event) { // TODO
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MediaFactory.getInstance(activity).stop();
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		MediaFactory.getInstance(activity).stop();
		switch (v.getId()) {
		case R.id.switchVideo:
			showVideoWindow();
			break;
		case R.id.changeVideo:
			showChangeWindow();
			break;
		case R.id.clarityVideo:
			showClarityWindow();
			break;
		case R.id.cloudTxt:
			isVideo = true;
			videoBtn.setBackgroundColor(Color.WHITE);
			break;
		case R.id.videoTxt:
			isVideo = false;
			cloundBtn.setBackgroundColor(Color.WHITE);
			break;
		case R.id.pointVideo:

			DialogAlert alert = new DialogAlert(activity);
			alert.init(activity.getString(R.string.tip), getString(R.string.isOpendoor));

			alert.setSureListener(new DialogAlert.OnSureListener() {
				@Override
				public void onSure() {
					// TODO Auto-generated method stub
					if(gateWay!=null) {
						String str=gateWay.getGateWayPoint();
						if(str!=null&&str!=""&&!str.contains(",")&&!str.equals("0")){
							SendCMD.getInstance().sendCmd(0, str, null);
							ToastUtils.ShowSuccess(activity,str,Toast.LENGTH_SHORT,true);
						}else{
							ToastUtils.ShowError(activity,getString(R.string.nobinddoor),Toast.LENGTH_SHORT,true);
						}
					}
				}

				@Override
				public void onCancle() {
					// TODO Auto-generated method stub
				}
			});
			alert.show();

			break;
		case R.id.listnerVideo:
			AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			if (manager != null) {
				if (isMute) {
					manager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
//					listnerTxt.setText("");
					listnerTxt.setTextColor(R.color.black);
					isMute=false;
				} else {
					manager.setStreamVolume(AudioManager.STREAM_MUSIC, manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
//					listnerTxt.setText(R.string.mute);
					listnerTxt.setTextColor(R.color.red);
					isMute=true;
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
        unregisterReceiver(mReceiver);
        P2PHandler.getInstance().finish();
	}

	String gateWayName = "";

	private void showVideoWindow() {

	final 	AlertViewWindow alertViewWindow=new AlertViewWindow(activity,getString(R.string.videoswitch),getGateWay(),null,0);
		alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
			@Override
			public void onItem(int pos, String itemName) {
				GateWay gateWays=getGateWay(itemName);

				if (gateWays!=null) {
					if(gateWay.getGatewayName().equals(itemName)){
						ToastUtils.ShowError(activity,getString(R.string.deviceuse),Toast.LENGTH_SHORT,true);
						alertViewWindow.dismiss();
						return;
					}
					if (gateWays.getTypeId() == 4) {
						callID = gateWays.getGatewayID();
						CallPwd = gateWays.getUserPassWord();
						gateWay=GateWayFactory.getInstance().getGateWay(callID);
						P2PHandler.getInstance().reject();
						mProgressDialog = new ProgressDialog(activity);
						mProgressDialog.setMessage(getString(R.string.loadingvideo));
						initView();
						regFilter();
						String pwd = P2PHandler.getInstance().EntryPassword(CallPwd);//经过转换后的设备密码
						P2PHandler.getInstance().call(userId, pwd, true, 1, callID, "", "", 2, callID);
						showProgress();
						alertViewWindow.dismiss();
					} else if(gateWays.getTypeId() == 6){
						finish();
						overridePendingTransition(0, 0);
						Intent intent = new Intent(MyApplication.getInstance(), CameraPlayActivity.class);
						intent.putExtra("camID", gateWay.getGatewayID());
						startActivity(intent);
						overridePendingTransition(0, 0);
					}else{
						finish();
						overridePendingTransition(0,0);
						String s = gateWay.getTypeId() + "";
						String did = gateWay.getGatewayID();
						Intent intent = new Intent(activity, GateWayPlayActivity.class);
						intent.putExtra("cid",
								Integer.parseInt(GateWayService.mp.get(gateWay.getGatewayID())));
						intent.putExtra("Cam_name", gateWay.getGatewayName());
						intent.putExtra("Cam_did", did);
						intent.putExtra("type", s);
						startActivity(intent);
						overridePendingTransition(0,0);
					}
				}else{
					ToastUtils.ShowError(activity,getString(R.string.deviceuse),Toast.LENGTH_SHORT,true);
					alertViewWindow.dismiss();
				}
			}
		});
		alertViewWindow.show();
	}

	public GateWay getGateWay(String gateWayName) {
		GateWay gateWay=null;
		List<GateWay> list = GateWayService.list;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getGatewayName().equals(gateWayName)) {
				gateWay = list.get(i);
				break;
			}
		}
		return gateWay;
	}

	private void showChangeWindow() {

		final 	AlertViewWindow alertViewWindow=new AlertViewWindow(activity,getString(R.string.videochange),ListNumBer.getChange(),null,0);
		alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {

			@Override
			public void onItem(int pos, String itemName) {
				try {
					switch (pos){
						case 0:
							P2PHandler.getInstance().setImageReverse(callID,CallPwd,0);
							break;
						case 1:
							P2PHandler.getInstance().setImageReverse(callID,CallPwd,1);
							break;
					}
					alertViewWindow.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		alertViewWindow.show();
	}


	private void showClarityWindow() {

		final 	AlertViewWindow alertViewWindow=new AlertViewWindow(activity,getString(R.string.definition),ListNumBer.getClarity(),null,0);
		alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {

			@Override
			public void onItem(int pos, String itemName) {
				try {
					switch (pos){
						case 0:
							P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_HD);
							break;
						case 1:
							P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_SD);
							break;
						case 2:
							P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_LD);
							break;
					}
					alertViewWindow.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		alertViewWindow.show();
	}

	public List<String> getGateWay() {
		List<String> resultLlist = new ArrayList<String>();
		List<GateWay> list = GateWayService.list;
		for (int i = 0; i < list.size(); i++) {
			GateWay gateWay = list.get(i);
			if ((gateWay.getTypeId() < 4&&gateWay.getTypeId()>1)||gateWay.getTypeId()==6) {
				resultLlist.add(gateWay.getGatewayName());
			}
		}
		return resultLlist;
	}
	@Override
	protected void onP2PViewSingleTap() {
		//vGateWay.setGateWayPoint(ponitStr);
//		Sqlite().updateGateWay(vGateWay, gateWayName);
	}
	@Override
	protected void onP2PViewFilling() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		gateWay=GateWayFactory.getInstance().getGateWay(callID);
		P2PHandler.getInstance().reject();
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage(getString(R.string.loadingvideo));
		initView();
		regFilter();
		String pwd = P2PHandler.getInstance().EntryPassword(CallPwd);//经过转换后的设备密码
		P2PHandler.getInstance().call(userId, pwd, true, 1, callID, "", "", 2, callID);
		showProgress();
	}

	@Override
	public int getActivityInfo() {
		return 0;
	}

	@Override
	protected void onGoBack() {

	}

	@Override
	protected void onGoFront() {
	}

	@Override
	protected void onExit() {

	}

	public BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(P2P_ACCEPT)) {
				int[] type = intent.getIntArrayExtra("type");
				P2PView.type = type[0];
				P2PView.scale = type[1];
//				tvContent.append("\n 监控数据接收");
//				Log.e("dxsTest", "监控数据接收:" + callID);
				hideProgress();;
				P2PHandler.getInstance().openAudioAndStartPlaying(1);//打开音频并准备播放，calllType与call时type一致
				int d = P2PHandler.getInstance().setScreenShotpath(MyApplication.getInstance().getRootPath(), callID+".jpg");
				captureScreen(-1);
			} else if (intent.getAction().equals(P2P_READY)) {
//				tvContent.append("\n 监控准备,开始监控");
//				Log.e("dxsTest", "监控准备,开始监控" + callID);
				pView.sendStartBrod();
			} else if (intent.getAction().equals(P2P_REJECT)) {
				int reason_code = intent.getIntExtra("reason_code", -1);
				int code1 = intent.getIntExtra("exCode1", -1);
				int code2 = intent.getIntExtra("exCode2", -1);
//				String reject = String.format("\n 监控挂断(reson:%d,code1:%d,code2:%d)", reason_code, code1, code2);
//				tvContent.append(reject);
			}
		}
	};
	public void showProgress() {
		mProgressDialog.show();
	}

	public void hideProgress() {
		mProgressDialog.dismiss();
	}

	public void showMsg(String msg) {
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
	}
}
