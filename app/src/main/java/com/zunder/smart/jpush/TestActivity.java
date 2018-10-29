package com.zunder.smart.jpush;

import cn.jpush.android.api.JPushInterface;
import java.io.IOException;

import com.zunder.smart.R;
import com.zunder.smart.service.SendThread;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
public class TestActivity extends Activity {
	private static Activity mContext;
	private static final float BEEP_VOLUME = 5.0f;
	// private Context context;
	private static ImageView chang;
	static TextView alrmMsg;
	TextView backTxt;
	String deviceID = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_msg_info);
		mContext = this;
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		String content = getIntent().getStringExtra(JPushInterface.EXTRA_ALERT);
		init();
		initTitleView();
		alrmMsg.setText(content);
		int index = content.indexOf("[");
		deviceID = content.substring(index + 1, content.length() - 1);
	}

	private static MediaPlayer mediaPlayer;
	private static void init() {
		if (mediaPlayer != null) {
			mediaPlayer.reset();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		if (mediaPlayer == null) {
			mContext.setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);
			mediaPlayer.setOnPreparedListener(OnPreparedListener);
			mediaPlayer.setLooping(true);
			AssetFileDescriptor file = mContext.getResources()
					.openRawResourceFd(R.raw.ringmusic);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				// if(flg){
				// mediaPlayer.setLooping(true);
				// }
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private final static OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
	private final static OnPreparedListener OnPreparedListener = new OnPreparedListener() {

		@Override
		public void onPrepared(MediaPlayer mp) {
			// TODO Auto-generated method stub
			mp.start();
		}
	};

	public void stop() {
		alrmMsg.setText("");
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void initTitleView() {
		backTxt = (TextView) findViewById(R.id.backTxt);
		alrmMsg = (TextView) findViewById(R.id.alrmMsg);
		chang = (ImageView) findViewById(R.id.show_state);

		backTxt.setOnClickListener(an_backClick);
		chang.setImageResource(R.mipmap.widget_pic_off);
	}

	private OnClickListener an_backClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mediaPlayer != null) {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
				}
			}
			if (!deviceID.equals("")) {
				if(deviceID.length()>=2) {
					SendThread send = SendThread.getInstance("*C0019FA07"
							+ deviceID + "80000000");
					new Thread(send).start();
				}
			}
			finish();
		}
	};
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mediaPlayer != null) {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
				}
			}
			if (!deviceID.equals("")) {
				if(deviceID.length()>=2) {
					SendThread send = SendThread.getInstance("*C0019FA07"
							+ deviceID + "AA800000");
					new Thread(send).start();
				}
			}
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
