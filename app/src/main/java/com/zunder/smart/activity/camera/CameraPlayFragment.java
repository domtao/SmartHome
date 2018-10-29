package com.zunder.smart.activity.camera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bluecam.api.CameraPlayActivity;
import com.door.activity.DoorPlayActivity;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.gateway.BaseActivity;
import com.zunder.smart.gateway.GateWayLanPlayActivity;
import com.zunder.smart.gateway.MyRender;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hsl.p2pipcam.nativecaller.DeviceSDK;

@SuppressLint("ResourceAsColor")
public class CameraPlayFragment extends Fragment implements PlayListener,
		RenderListener, AudioRecordResult, OnClickListener, DevicePramsListener {
	private static GLSurfaceView glSurfaceView;
	private MyRender myRender;
	public static long userid = -1;
	public static String Cam_did = "";
	CustomAudioRecorder customAudioRecorder;
	private AudioPlayer audioPlayer;
	private CustomBuffer AudioBuffer;

	public static Activity activity;
	private float mPosX;
	private float mPosY;
	private float mCurrentPosX;
	private float mCurrentPosY;
	private boolean startFlag;

	View root;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.camera_play, container,
				false);
		glSurfaceView = (GLSurfaceView) root.findViewById(R.id.myglsurfaceview);
		initView();
		return  root;
	}
	public void initData(int _userid){
		userid=_userid;
		BridgeService.setPlayListener(this);
//		initView();
		if (userid != -1) {
			new LoadTask().execute();
		}
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
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(hidden){
			if(AudioBuffer!=null){
				close();
			}
		}
	}

	public void close(){
		DeviceSDK.stopPlayAudio(userid);
		AudioBuffer.ClearAll();
		audioPlayer.AudioPlayStop();
		DeviceSDK.stopPlayStream(userid);
		customAudioRecorder.releaseRecord();
		BridgeService.setDevicePramsListener(null);
		GateWayService.setDevicePramsListener();
	}


	private void initView() {
		customAudioRecorder = new CustomAudioRecorder(this);
		AudioBuffer = new CustomBuffer();
		audioPlayer = new AudioPlayer(AudioBuffer);

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
	}



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

	}



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



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		default:
			break;
		}
	}








	@Override
	public void getDevicePrams(long UserID, long nType, String param) {
		// TODO Auto-generated method stub
		if (nType == 10022) {
		}
	}

}
