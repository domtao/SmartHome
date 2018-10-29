package com.zunder.smart.gateway;

import hsl.p2pipcam.nativecaller.DeviceSDK;

import org.json.JSONException;
import org.json.JSONObject;

import com.zunder.smart.R;
import com.zunder.smart.gateway.MyRender.RenderListener;
import com.zunder.smart.gateway.util.AudioPlayer;
import com.zunder.smart.gateway.util.CustomAudioRecorder;
import com.zunder.smart.gateway.util.CustomAudioRecorder.AudioRecordResult;
import com.zunder.smart.gateway.util.CustomBuffer;
import com.zunder.smart.gateway.util.CustomBufferData;
import com.zunder.smart.gateway.util.CustomBufferHead;
import com.zunder.smart.listener.PlayListener;
import com.zunder.smart.service.BridgeService;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class GateWayLanPlayActivity extends BaseActivity implements
		PlayListener, MyRender.RenderListener, AudioRecordResult, OnClickListener {
	private static GLSurfaceView glSurfaceView;
	private MyRender myRender;
	public static long userid = -1;
	private static String Cam_name;
	public static String Cam_did = "";
	private int linkState = -1;
	private boolean isAudio = false;
	CustomAudioRecorder customAudioRecorder;
	private AudioPlayer audioPlayer;
	private CustomBuffer AudioBuffer;
	FrameLayout four_view;
	RelativeLayout top_view;
	public static Activity activity;
	private float mPosX;
	private float mPosY;
	private float mCurrentPosX;
	private float mCurrentPosY;
	private boolean startFlag;
	// private int PTZ_mode;
	private String type;

	public static void startActivity(Activity activity, long userID,
			String _Cam_name, String _Cam_did, String _type) {
		userid = userID;
		Cam_name = _Cam_name;
		Cam_did = _Cam_did;

		Intent intent = new Intent(activity, GateWayLanPlayActivity.class);

		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		activity = this;

		customAudioRecorder = new CustomAudioRecorder(this);
		AudioBuffer = new CustomBuffer();
		audioPlayer = new AudioPlayer(AudioBuffer);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.device_play_laun_screen);
		initView();

		new LoadTask().execute();
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
	}

	private void initView() {
		glSurfaceView = (GLSurfaceView) findViewById(R.id.myglsurfaceview);
		myRender = new MyRender(glSurfaceView);
		myRender.setListener(this);
		glSurfaceView.setRenderer(myRender);

		glSurfaceView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				// ����
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
						// Toast.makeText(getApplicationContext(),
						// "X:"+mPosX+":"+mCurrentPosX+" Y:"+mPosY+":"+mCurrentPosY,
						// 0).show();
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
								// PTZ_mode=3;
								startFlag = true;
							}
						} else if ((mCurrentPosX - mPosX < 0)
								&& (mCurrentPosY - mPosY > 0)) {
							// �� ��
							if ((Math.abs(mCurrentPosX - mPosX) > 25)
									&& (Math.abs(mCurrentPosY - mPosY) < 25)) {
								DeviceSDK.ptzControl(userid, 4);
								// PTZ_mode=5;
								startFlag = true;
							} else if ((Math.abs(mCurrentPosX - mPosX) < 25)
									&& (Math.abs(mCurrentPosY - mPosY) > 25)) {
								DeviceSDK.ptzControl(userid, 2);
								// PTZ_mode=3;
								startFlag = true;
							}
						} else if ((mCurrentPosX - mPosX > 0)
								&& (mCurrentPosY - mPosY < 0)) {
							// �� ��
							if ((Math.abs(mCurrentPosX - mPosX) > 25)
									&& (Math.abs(mCurrentPosY - mPosY) < 25)) {
								DeviceSDK.ptzControl(userid, 6);
								// PTZ_mode=7;
								startFlag = true;
							} else if ((Math.abs(mCurrentPosX - mPosX) < 25)
									&& (Math.abs(mCurrentPosY - mPosY) > 25)) {
								DeviceSDK.ptzControl(userid, 0);
								// PTZ_mode=1;
								startFlag = true;
							}
						} else if ((mCurrentPosX - mPosX < 0)
								&& (mCurrentPosY - mPosY < 0)) {
							// �� ��
							if ((Math.abs(mCurrentPosX - mPosX) > 25)
									&& (Math.abs(mCurrentPosY - mPosY) < 25)) {
								DeviceSDK.ptzControl(userid, 4);
								// PTZ_mode=5;
								startFlag = true;
							} else if ((Math.abs(mCurrentPosX - mPosX) < 25)
									&& (Math.abs(mCurrentPosY - mPosY) > 25)) {
								DeviceSDK.ptzControl(userid, 0);
								// PTZ_mode=1;
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

		showbtoom();

	}


	private void openAudio(boolean switchSt) {
		if (switchSt) { // "��������"
			AudioBuffer.ClearAll();
			audioPlayer.AudioPlayStart();
			DeviceSDK.startPlayAudio(userid, 1);

		} else {
			DeviceSDK.stopPlayAudio(userid);
			AudioBuffer.ClearAll();
			audioPlayer.AudioPlayStop();

		}
	}

	private Handler frushHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
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
				// ���ӽ��Ռ��rҕ�l subStreamId = 0/1/2 720P/VGA/QVGA
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

	@SuppressWarnings("deprecation")
	private void showbtoom() {

	}

	int selitem = 0;

	public boolean onKeyDown(int keyCode, KeyEvent event) { // TODO
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			BridgeService.setPlayListener(null);
			DeviceSDK.stopPlayAudio(userid);
			DeviceSDK.stopPlayStream(userid);
			AudioBuffer.ClearAll();
			audioPlayer.AudioPlayStop();
			customAudioRecorder.releaseRecord();
			userid = -1;
			finish();
			return true;
		} else {
			if (!"4".equals(type)) {
				showbtoom();
			}
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

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

}
