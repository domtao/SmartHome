package com.zunder.smart.activity;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.door.Utils.Util;
import com.zunder.smart.R;

public class PlayMP3Activity extends Activity {

	private MediaPlayer mediaPlayer;
	public static int isShow = 0;
	private static final float BEEP_VOLUME = 5.0f;
	int ids;
	private String sourceMsg = "";

	private int playcount = 0;
	TextView tv_time, tv_ms, tv_jc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mp_dialog);
		isShow = 1;
		stop();
		Intent intent = getIntent();
		int rid = intent.getIntExtra("mp", 1);
		playcount = intent.getIntExtra("repeat", 1);
		sourceMsg = intent.getStringExtra("msg");
		initview();
		showPlay(rid);
	}

	private void initview() {
		// TODO Auto-generated method stub
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_ms = (TextView) findViewById(R.id.tv_ms);
		tv_jc = (TextView) findViewById(R.id.tv_jc);
//		tv_time.setText(Util.getDate());
		tv_ms.setText(sourceMsg);
		tv_jc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isShow = 0;
				stop();
				finish();
			}
		});
	}

	private void initUI() {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// init(ids);
				try {
					while (playcount > 0) {
						init(ids);
						// Thread.sleep(3000);
						do {
							Thread.sleep(500);
							if (mediaPlayer == null) {
								break;
							}
						} while (mediaPlayer.isPlaying());
						if (playcount > 0) {
							playcount--;
						}
					}
					// } catch (InterruptedException e) {
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void showPlay(int ids) {
		// playcount=5; //1;
		this.ids = ids;
		initUI();
	}

	// public void setFlag(String setMsg,int SetCount){
	// playcount=SetCount;
	// sourceMsg=setMsg;
	// }
	private void init(int ids) {
		if (mediaPlayer != null) {
			mediaPlayer.reset();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		if (mediaPlayer == null) {
			this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);
			mediaPlayer.setOnPreparedListener(OnPreparedListener);
			AssetFileDescriptor file = getResources().openRawResourceFd(ids);
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

	public void stop() {
		// if(mediaPlayer != null && mediaPlayer.isPlaying())
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		playcount = 0;
	}

	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
	private final OnPreparedListener OnPreparedListener = new OnPreparedListener() {

		@Override
		public void onPrepared(MediaPlayer mp) {
			// TODO Auto-generated method stub
			mp.start();
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) { // TODO
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			isShow = 0;
			stop();
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
