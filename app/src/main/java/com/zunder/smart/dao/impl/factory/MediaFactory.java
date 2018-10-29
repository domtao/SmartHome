package com.zunder.smart.dao.impl.factory;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.model.RedInfra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MediaFactory {
	static Activity activity;
	private volatile static MediaFactory install;
	public static MediaFactory getInstance(Activity _activity) {
		if (null == install) {
			activity=_activity;
			install = new MediaFactory();
		}
		return install;
	}
	private MediaPlayer mediaPlayer;

	public void init() {
		if (mediaPlayer != null) {
			mediaPlayer.reset();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		if (mediaPlayer == null) {
			activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);
			mediaPlayer.setOnPreparedListener(OnPreparedListener);
			mediaPlayer.setLooping(true);
			AssetFileDescriptor file= activity.getResources().openRawResourceFd(R.raw.ringmusic);

			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();

				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}
	private  MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
	private   MediaPlayer.OnPreparedListener OnPreparedListener = new MediaPlayer.OnPreparedListener() {
		@Override
		public void onPrepared(MediaPlayer mp) {
			// TODO Auto-generated method stub
			mp.start();
		}
	};
	public void stop() {
		if (mediaPlayer != null) {
			if(mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
				mediaPlayer.release();
			}
			mediaPlayer = null;
		}
	}
}
