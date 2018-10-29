package com.zunder.smart.dialog;

import com.zunder.smart.activity.main.MainActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.widget.LinearLayout;

public class MusicPlayMedia extends LinearLayout implements
		OnCompletionListener {
	Context context;
	private static MediaPlayer mediaPlayer;
	public static int playState = 0;
	private int state;
	public static MusicPlayMedia instansMedia;

	public static MusicPlayMedia getInstansMedia() {
		if (instansMedia == null) {
			instansMedia = new MusicPlayMedia(MainActivity.getInstance(), "");
		}
		return instansMedia;
	}

	public MusicPlayMedia(Context context, String musicName) {

		super(context);
		this.context = context;
		instansMedia = this;
		init();
	}

	public void init() {

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setVolume(0.5f, 0.5f);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				try {
					mp.start();
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}
		});
		mediaPlayer.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				try {
				} catch (Exception e) {
					// TODO: handle exception
				}
				return true;
			}
		});
		;
	}

	public void play(String url) throws Exception {
		mediaPlayer.reset();

		mediaPlayer.setDataSource(context, Uri.parse(url));
		mediaPlayer.prepareAsync();
		mediaPlayer.start();

	}

	public void stop() {
		if (mediaPlayer != null) {
			try {
				mediaPlayer.stop();
				mediaPlayer.reset();

			} catch (Exception e) {
			}
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		stop();
	}

	public boolean isPlaying() {
		if (mediaPlayer == null || instansMedia == null) {
			return false;
		}
		return mediaPlayer.isPlaying();
	}

}
