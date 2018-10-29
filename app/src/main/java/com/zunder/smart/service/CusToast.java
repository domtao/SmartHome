package com.zunder.smart.service;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

public class CusToast {
	public static final int LENGTH_MAX = -1;
	private boolean mCanceled = true;
	private Handler mHandler;
	private Context mContext;
	private Toast mToast;

	public CusToast(Context context) {
		this(context, new Handler());
	}

	public CusToast(Context context, Handler h) {
		mContext = context;
		mHandler = h;
		mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
		mToast.setGravity(Gravity.BOTTOM, 0, 0);
	}

	public void show(int resId, int duration) {
		mToast.setText(resId);
		if (duration != LENGTH_MAX) {
			mToast.setDuration(duration);
			mToast.show();
		} else if (mCanceled) {
			mToast.setDuration(Toast.LENGTH_LONG);
			mCanceled = false;
			showUntilCancel();
		}
	}


	public void show(String text, int duration) {
		mToast.setText(text);
		if (duration != LENGTH_MAX) {
			mToast.setDuration(duration);
			mToast.show();
		} else {
			if (mCanceled) {
				mToast.setDuration(Toast.LENGTH_LONG);
				mCanceled = false;
				showUntilCancel();
				showTime();
			}
		}
	}


	public void hide() {
		mToast.cancel();
		mCanceled = true;
	}

	public boolean isShowing() {
		return !mCanceled;
	}

	private void showUntilCancel() {
		if (mCanceled)
			return;
		mToast.show();
		mHandler.postDelayed(new Runnable() {
			public void run() {
				showUntilCancel();
			}
		}, 3000);
	}

	private Boolean searchflag = true;
	private int showCount = 0;

	public void showTime() {
		searchflag = true;
		showCount = 0;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (searchflag) {
					try {
						Thread.sleep(100);
						showCount++;
						if (showCount >= 100) {
							searchflag = false;
							showCount = 0;
							hide();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}