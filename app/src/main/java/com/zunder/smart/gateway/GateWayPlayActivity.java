package com.zunder.smart.gateway;

import hsl.p2pipcam.nativecaller.DeviceSDK;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.bluecam.api.CameraPlayActivity;
import com.door.activity.DoorPlayActivity;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.gateway.MyRender.RenderListener;
import com.zunder.smart.gateway.util.AudioPlayer;
import com.zunder.smart.gateway.util.CustomAudioRecorder;
import com.zunder.smart.gateway.util.CustomAudioRecorder.AudioRecordResult;
import com.zunder.smart.gateway.util.CustomBuffer;
import com.zunder.smart.gateway.util.CustomBufferData;
import com.zunder.smart.gateway.util.CustomBufferHead;
import com.zunder.smart.listener.DevicePramsListener;
import com.zunder.smart.listener.PlayListener;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.roll.WheelView;
import com.zunder.smart.service.BridgeService;
import com.zunder.smart.service.GateWayService;
import com.zunder.smart.service.aduio.AduioService;
import com.zunder.smart.utils.ListNumBer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ViewFlipper;

@SuppressLint("ResourceAsColor")
public class GateWayPlayActivity extends BaseActivity implements PlayListener,
		RenderListener, AudioRecordResult, OnClickListener, DevicePramsListener {
	private static GLSurfaceView glSurfaceView;
	ViewFlipper flipper;
	LinearLayout split_play;
	private MyRender myRender;
	public static long userid = -1;
	private String Cam_name;
	public static String Cam_did = "";
	private int linkState = -1;
	private boolean isAudio = false;
	CustomAudioRecorder customAudioRecorder;
	private AudioPlayer audioPlayer;
	private CustomBuffer AudioBuffer;

	public static Activity activity;
	private Button tempButton;
	private float mPosX;
	private float mPosY;
	private float mCurrentPosX;
	private float mCurrentPosY;
	private boolean startFlag;
	// private int PTZ_mode;
	private String type;
	private ImageButton clarityVideo, listnerVideo;
	private ImageButton switchVideo, changeVideo, pointVideo, allscreen;
	RelativeLayout cloundBtn, videoBtn;
	TextView cloundTxt, videoTxt;
	RelativeLayout otherLayout;
	TextView listnerTxt;
	private boolean isVideo = true;
	private GateWay vGateWay = null;
	String[] pointStr = null;
	private boolean isSet = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		activity = this;
		// BridgeService.setPlayListener(this);
		BridgeService.setDevicePramsListener(this);
		customAudioRecorder = new CustomAudioRecorder(this);
		AudioBuffer = new CustomBuffer();
		audioPlayer = new AudioPlayer(AudioBuffer);

		Intent intent = getIntent();
		if (userid == -1) {
			userid = intent.getIntExtra("cid", -1);
		}
		Cam_name = intent.getStringExtra("Cam_name");
		Cam_did = intent.getStringExtra("Cam_did");
		vGateWay = GateWayFactory.getInstance().getGateWay(Cam_did);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.device_play_screen);

		if (vGateWay != null && vGateWay.getGateWayPoint().length() > 16) {
			pointStr = vGateWay.getGateWayPoint().split(",");
		} else {
			pointStr = new String[] { "0", "0", "0", "0", "0", "0", "0", "0",
					"0", "0", "0", "0", "0", "0", "0", "0" };
		}
		initView();
		cloundBtn.setBackgroundColor(R.color.green);
		new LoadTask().execute();
		sendCmd("*@?3");
	}

	public void sendCmd(String TampStr) {
		byte[] data = new byte[TampStr.length()];
		data = TampStr.getBytes();
		data[TampStr.length() - 1] = 13;
		String cmd = "";
		for (int i = 0; i < TampStr.length(); i++) {
			cmd = cmd + Integer.toHexString(data[i] / 16).toUpperCase()
					+ Integer.toHexString(data[i] & 15).toUpperCase();
		}
		int result = DeviceSDK.setDeviceParam(userid, 0x2725, cmd);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		DeviceSDK.stopPlayAudio(userid);
		AudioBuffer.ClearAll();
		audioPlayer.AudioPlayStop();

		DeviceSDK.stopPlayStream(userid);
		customAudioRecorder.releaseRecord();
		BridgeService.setDevicePramsListener(null);
		GateWayService.setDevicePramsListener();
	}

	FrameLayout[] layouts = null;
	int index = 0;

	private void initView() {
		listnerTxt = (TextView) findViewById(R.id.listnerTxt);
		allscreen = (ImageButton) findViewById(R.id.allscreen);
		otherLayout = (RelativeLayout) findViewById(R.id.otherLayout);
		glSurfaceView = (GLSurfaceView) findViewById(R.id.myglsurfaceview);
		myRender = new MyRender(glSurfaceView);
		myRender.setListener(this);
		glSurfaceView.setRenderer(myRender);

		glSurfaceView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					mPosX = event.getX();
					mPosY = event.getY();
					startFlag = false;
					break;
				// �ƶ�
				case MotionEvent.ACTION_MOVE:
					if (!startFlag) {
						mCurrentPosX = event.getX();
						mCurrentPosY = event.getY();

						if ((mCurrentPosX - mPosX > 0)
								&& (mCurrentPosY - mPosY > 0)) {
							// �� ��
							if ((Math.abs(mCurrentPosX - mPosX) > 25)
									&& (Math.abs(mCurrentPosY - mPosY) < 25)) {
								DeviceSDK.ptzControl(userid, 6);
								// PTZ_mode=7;
								startFlag = true;
							} else if ((Math.abs(mCurrentPosX - mPosX) < 25)
									&& (Math.abs(mCurrentPosY - mPosY) > 25)) {
								DeviceSDK.ptzControl(userid, 2);

								startFlag = true;
							}
						} else if ((mCurrentPosX - mPosX < 0)
								&& (mCurrentPosY - mPosY > 0)) {
							if ((Math.abs(mCurrentPosX - mPosX) > 25)
									&& (Math.abs(mCurrentPosY - mPosY) < 25)) {
								DeviceSDK.ptzControl(userid, 4);
							startFlag = true;
							} else if ((Math.abs(mCurrentPosX - mPosX) < 25)
									&& (Math.abs(mCurrentPosY - mPosY) > 25)) {
								DeviceSDK.ptzControl(userid, 2);
								startFlag = true;
							}
						} else if ((mCurrentPosX - mPosX > 0)
								&& (mCurrentPosY - mPosY < 0)) {
						if ((Math.abs(mCurrentPosX - mPosX) > 25)
									&& (Math.abs(mCurrentPosY - mPosY) < 25)) {
								DeviceSDK.ptzControl(userid, 6);
							startFlag = true;
							} else if ((Math.abs(mCurrentPosX - mPosX) < 25)
									&& (Math.abs(mCurrentPosY - mPosY) > 25)) {
								DeviceSDK.ptzControl(userid, 0);
								startFlag = true;
							}
						} else if ((mCurrentPosX - mPosX < 0)
								&& (mCurrentPosY - mPosY < 0)) {
						if ((Math.abs(mCurrentPosX - mPosX) > 25)
									&& (Math.abs(mCurrentPosY - mPosY) < 25)) {
								DeviceSDK.ptzControl(userid, 4);
							startFlag = true;
							} else if ((Math.abs(mCurrentPosX - mPosX) < 25)
									&& (Math.abs(mCurrentPosY - mPosY) > 25)) {
								DeviceSDK.ptzControl(userid, 0);
								startFlag = true;
							}
						}
					}
					break;
				// ����
				case MotionEvent.ACTION_UP:
					if (!startFlag) {
						if ((Math.abs(mCurrentPosX - mPosX) < 25)
								&& (Math.abs(mCurrentPosY - mPosY) < 25)) {
							// showbtoom();
						}
					}
					break;
				default:
					break;
				}
				return true;
			}
		});
		// SCREEN_ORIENTATION_PORTRAIT

		allscreen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GateWayLanPlayActivity.startActivity(activity, userid,
						Cam_name, Cam_did, type);
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
				if (isVideo) {
					switch (event.getAction()) {
						case MotionEvent.ACTION_UP:
							AduioService aduioService= AduioService.getInstance();
							aduioService.setInit(activity);

							aduioService.setOnCallInterFace(new AduioService.OnCallInterFace() {
								@Override
								public void OnCall(final String cmd) {
activity.runOnUiThread(new Runnable() {
	@Override
	public void run() {
		int id=	DeviceFactory.getInstance()
				.getDevicesContainsName(cmd);

		if (id>0&&vGateWay != null && vGateWay.getGateWayPoint().length() > 16) {
			String[] str=vGateWay.getGateWayPoint().split(",");

			for (int i=0;i<str.length;i++){

				if(Integer.parseInt(str[i])==id){
					DeviceSDK.ptzControl(userid, 31 + (2 * i));
					break;
				}

			}
		}
	}
});

								}
							});
							break;
						default:
							break;
					}
				} else {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						openAudio(false);

						DeviceSDK.startTalk(userid);
						customAudioRecorder.StartRecord();
						DeviceSDK.stopTalk(userid);
						customAudioRecorder.StopRecord();

						DeviceSDK.startTalk(userid);
						customAudioRecorder.StartRecord();
						showToast(getString(R.string.starttalk));
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						DeviceSDK.stopTalk(userid);
						customAudioRecorder.StopRecord();
						if (isAudio) {
							openAudio(true);
						}
					}
				}
				return false;
			}
		});


		// showbtoom();

		pointVideo.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if (isSet) {
					// openPointLongWindow();
					showPointLongWindow();
				} else {
					showToast(getString(R.string.pointnull));
				}
				return true;
			}
		});
	}

	/**
	 * ��������
	 */
	private void openAudio(boolean switchSt) {
		if (switchSt) {
			AudioBuffer.ClearAll();
			audioPlayer.AudioPlayStart();
			DeviceSDK.startPlayAudio(userid, 1);

		} else { // "�رռ���"
			DeviceSDK.stopPlayAudio(userid);
			AudioBuffer.ClearAll();
			audioPlayer.AudioPlayStop();

		}
	}

	private Handler frushHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// progressLayout.setVisibility(View.GONE);
		}
	};

	@Override
	public void cameraGetParamsResult(long userid, String cameraParams) {
		// TODO Auto-generated method stub

	}

	@Override
	public void callBackAudioData(long userID, byte[] pcm, int size) {
		if (userID == userid) {
			CustomBufferHead head = new CustomBufferHead();
			CustomBufferData data = new CustomBufferData();
			head.length = size;
			head.startcode = 0xff00ff;
			data.head = head;
			data.data = pcm;
			if (audioPlayer.isAudioPlaying()) {
				AudioBuffer.addData(data);
			}
		}
	}

	@Override
	public void callBackVideoData(long userID, byte[] data, int type, int size) {
		// TODO Auto-generated method stub
		if (userID == userid) {

		}
	}

	@Override
	public void smartAlarmCodeGetParamsResult(long userid, String params) {
		// TODO Auto-generated method stub

	}

	@Override
	public void smartAlarmNotify(long userid, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveNowDeviceStatus(long userid, int status) {

		backId = userid;
		Message msg = handler.obtainMessage(0, status, 0);
		handler.sendMessage(msg);
	}

	long backId;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				if (userid == backId) {
					if (linkState != msg.arg1) {
						linkState = msg.arg1;
						if (linkState == 100) {
						}
					}
				}
			} else if (msg.what == 1) {
				if (isSet) {
					pointVideo.setImageResource(R.mipmap.point_img);
				} else {
					pointVideo.setImageResource(R.mipmap.point_imged);
				}
			}
		}
	};

	private class LoadTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				DeviceSDK.setRender(userid, myRender);
				JSONObject obj = new JSONObject();
				obj.put("param", 13);
				obj.put("value", 1024);
				DeviceSDK.setDeviceParam(userid, 0x2026, obj.toString());
				JSONObject obj1 = new JSONObject();
				obj1.put("param", 6);
				obj1.put("value", 20);
				DeviceSDK.setDeviceParam(userid, 0x2026, obj1.toString());
				DeviceSDK.startPlayStream(userid, 0, 1);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	@Override
	public void initComplete(int size, int width, int height) {
		// TODO Auto-generated method stub
		frushHandler.sendEmptyMessage(0);
	}

	@Override
	public void takePicture(byte[] imageBuffer, int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void AudioRecordData(byte[] data, int len) {
		// TODO Auto-generated method stub
		DeviceSDK.SendTalkData(userid, data, len);
	}




	public boolean onKeyDown(int keyCode, KeyEvent event) { // TODO
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			BridgeService.setPlayListener(null);
			DeviceSDK.stopPlayAudio(userid);
			DeviceSDK.stopPlayStream(userid);
			AudioBuffer.ClearAll();
			audioPlayer.AudioPlayStop();
			customAudioRecorder.releaseRecord();
			GateWayService.setDevicePramsListener();
			// userid = -1;
			finish();
			return true;
		} else {
			// if(keyCode == KeyEvent.KEYCODE_MENU){
			if (!"4".equals(type)) {// ��������
				// showbtoom();
			}
			// }
			return super.onKeyDown(keyCode, event);
		}
	}

	String[] generalsTypes = null;
	List<List<String>> list = new ArrayList<List<String>>();

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
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
			cloundBtn.setBackgroundColor(R.color.green);
			break;
		case R.id.videoTxt:
			isVideo = false;
			videoBtn.setBackgroundColor(R.color.green);
			cloundBtn.setBackgroundColor(Color.WHITE);
			break;
		case R.id.pointVideo:
			showPointWindow();
			// openPointWindow();
			break;
		case R.id.listnerVideo:
			isAudio = !isAudio;
			if (isAudio) {
				listnerTxt.setTextColor(Color.RED);
			} else {
				listnerTxt.setTextColor(Color.BLACK);
			}
			openAudio(isAudio);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		BridgeService.setPlayListener(this);
		if (userid != -1) {
			new LoadTask().execute();
		}
	}
	private void showVideoWindow() {
		final AlertViewWindow alertViewWindow=new AlertViewWindow(activity,getString(R.string.videoswitch),getGateWay(),null,0);
		alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
			@Override
			public void onItem(int pos, String itemName) {
				GateWay gateWay = getGateWay(itemName);
				if (gateWay != null) {
					if (gateWay.getTypeId() == 4) {

						finish();
						overridePendingTransition(0, 0);
						Intent intent = new Intent(activity, DoorPlayActivity.class);
						intent.putExtra("Id", gateWay.getGatewayID());
						intent.putExtra("Pwd", gateWay.getUserPassWord());
						startActivity(intent);
						overridePendingTransition(0, 0);
					}else 	if (gateWay.getTypeId() == 6) {
						finish();
						overridePendingTransition(0, 0);
						Intent intent = new Intent(MyApplication.getInstance(), CameraPlayActivity.class);
						intent.putExtra("camID", gateWay.getGatewayID());
						startActivity(intent);
						overridePendingTransition(0, 0);
					} else {
						DeviceSDK.stopPlayAudio(userid);
						DeviceSDK.stopPlayStream(userid);
						AudioBuffer.ClearAll();
						audioPlayer.AudioPlayStop();
						customAudioRecorder.releaseRecord();
						userid = Integer.parseInt(GateWayService.mp.get(gateWay
								.getGatewayID()));
						try {
							DeviceSDK.setRender(userid, myRender);
							JSONObject obj = new JSONObject();
							obj.put("param", 13);
							obj.put("value", 1024);
							DeviceSDK.setDeviceParam(userid, 0x2026,
									obj.toString());
							JSONObject obj1 = new JSONObject();
							obj1.put("param", 6);
							obj1.put("value", 20);
							DeviceSDK.setDeviceParam(userid, 0x2026,
									obj1.toString());
							DeviceSDK.startPlayStream(userid, 0, 1);
							sendCmd("*@?3");
						} catch (Exception e) {
						}
					}
					alertViewWindow.dismiss();
				}
			}
		});

		alertViewWindow.show();
	}
	private void showChangeWindow() {
			final AlertViewWindow alertViewWindow = new AlertViewWindow(activity, getString(R.string.videochange), ListNumBer.getChange(),null,0);
			alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {

				@Override
				public void onItem(int pos, String itemName) {
					try {
						JSONObject obj = new JSONObject();
						obj.put("param", 5);
						obj.put("value", pos);
						DeviceSDK.setDeviceParam(userid, 0x2026, obj.toString());
						alertViewWindow.dismiss();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
	alertViewWindow.show();
	}
	private void showClarityWindow() {
		final AlertViewWindow alertViewWindow = new AlertViewWindow(activity, getString(R.string.definition), ListNumBer.getClarity(),null,0);
		alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {

			@Override
			public void onItem(int pos, String itemName) {
				DeviceSDK.stopPlayStream(userid);
				DeviceSDK.startPlayStream(userid, 10, pos);
				alertViewWindow.dismiss();
			}
		});
		alertViewWindow.show();
	}
	String nameItem = "";
	private void showPointWindow() {

		final AlertViewWindow alertViewWindow = new AlertViewWindow(activity, getString(R.string.point), ListNumBer.getPoint(vGateWay),getString(R.string.point_long),0);
		alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {

			@Override
			public void onItem(int pos, String itemName) {
				DeviceSDK.ptzControl(userid, 31 + (2 * pos));
				alertViewWindow.dismiss();
			}
		});
		alertViewWindow.show();
	}
	PopupWindow popintLongWindow;
	View pupView;
	int selitem=0;
	private void showPointLongWindow() {

			LayoutInflater layoutInflater = (LayoutInflater) activity
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			pupView = layoutInflater.inflate(R.layout.popwindow_point_layout,
					null);
			popintLongWindow = new PopupWindow(pupView,
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			TextView save = (TextView) pupView.findViewById(R.id.save);
			TextView title = (TextView) pupView.findViewById(R.id.titleTxt);
			title.setText(getString(R.string.point));
			TextView showTxt = (TextView) pupView.findViewById(R.id.showTxt);

			WheelView wheel_camera = (WheelView) pupView
					.findViewById(R.id.wheel_camera);

			wheel_camera.setOffset(2);
			wheel_camera.setItems(DeviceFactory.getInstance().getDeviceName());
			wheel_camera.setSeletion(1);
			wheel_camera
					.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

						@Override
						public void onSelected(int selectedIndex, String item) {
							nameItem = item;
						}
					});

			WheelView wheel_point = (WheelView) pupView
					.findViewById(R.id.wheel_point);

			wheel_point.setOffset(2);
			wheel_point.setItems(ListNumBer.getPoint(vGateWay));
			wheel_point.setSeletion(1);

			wheel_point
					.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

						@Override
						public void onSelected(int selectedIndex, String item) {
							selitem = selectedIndex - 2;
						}
					});
			save.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pointStr[selitem] = DeviceFactory.getInstance()
							.getDevicesByName(nameItem).getId() + "";
					String ponitStr = "";
					for (int i = 0; i < pointStr.length; i++) {
						if (i == 15) {
							ponitStr += pointStr[i];
						} else {
							ponitStr += pointStr[i] + ",";
						}
					}
					vGateWay.setGateWayPoint(ponitStr);
					Sqlite().updateGateWay(vGateWay, vGateWay.getGatewayName());
					// vGateWay.set
					DeviceSDK.ptzControl(userid, 30 + (2 * selitem));
					popintLongWindow.dismiss();
				}
			});
			popintLongWindow.setFocusable(true);
			popintLongWindow.setOutsideTouchable(true);
			popintLongWindow.setBackgroundDrawable(new BitmapDrawable());
			popintLongWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
			popintLongWindow.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {

				}
			});

			popintLongWindow.setTouchInterceptor(new OnTouchListener() {
				public boolean onTouch(View view, MotionEvent event) {
					return false;
				}
			});
			openPointLongWindow();
	}

	public void openPointLongWindow() {
		selitem = 0;
		popintLongWindow.showAtLocation(pupView, Gravity.BOTTOM, 0, 0);
	}

	public List<String> getGateWay() {
		List<String> resultLlist = new ArrayList<String>();
		List<GateWay> list = GateWayService.list;
		for (int i = 0; i < list.size(); i++) {
			GateWay gateWay = list.get(i);
			if (gateWay.getTypeId() >2) {
				resultLlist.add(gateWay.getGatewayName());
			}
		}
		return resultLlist;
	}


	public GateWay getGateWay(String gateWayName) {
		GateWay gateWay=null;
		List<GateWay> list = GateWayService.list;
		for (int i = 0; i < list.size(); i++) {
			gateWay = list.get(i);
			if (gateWay.getGatewayName().equals(gateWayName)) {
				break;
			}
		}
		return gateWay;
	}

	@Override
	public void getDevicePrams(long UserID, long nType, String param) {
		// TODO Auto-generated method stub
		if (nType == 10022) {
			if (param.startsWith("*@")) {
				if (param.substring(5, 21).equals("0000000000000000")) {
					isSet = true;
				} else {
					isSet = false;
				}
				handler.sendEmptyMessage(1);
			}
		}
	}

}
