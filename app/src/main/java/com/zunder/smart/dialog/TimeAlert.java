package com.zunder.smart.dialog;

import com.zunder.smart.R;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.service.TcpSender;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class TimeAlert {

	private Activity context;
	private PopupWindow popupWindow;
	private View view;
	private TextView titleTextView;
	private TextView messageTextView;
	private Button cancleBtn;
	int totalTime = 0;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case -1:
				if (sureListener != null) {
					sureListener.onCancle();
				}
				TcpSender._isLearnFlag = false;
				diss();
				break;
			case 0:
				messageTextView.setText((10 - totalTime) + "");
				break;
			default:
				break;
			}
		};
	};

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (totalTime >= 10) {
				Message msg = handler.obtainMessage();
				msg.what = -1;
				handler.sendMessage(msg);
			} else {

				Message msg = handler.obtainMessage();
				msg.what = 0;
				handler.sendMessage(msg);
				totalTime++;
			}
			handler.postDelayed(runnable, 1000);
		}
	};

	public interface OnSureListener {
		public void onCancle();
	}

	private OnSureListener sureListener;

	public void setSureListener(OnSureListener sureListener) {
		this.sureListener = sureListener;
	}

	public TimeAlert(Activity context) {
		super();
		this.context = context;
		init();
	}

	private void init() {
		LayoutInflater inflater = context.getLayoutInflater();
		view = inflater.inflate(R.layout.alert_time_verify, null);
		titleTextView = (TextView) view.findViewById(R.id.title_tv);
		messageTextView = (TextView) view.findViewById(R.id.message_tv);
		cancleBtn = (Button) view.findViewById(R.id.cancle_bt);
		cancleBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				diss();
			}
		});
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		ColorDrawable cd = new ColorDrawable(0xb0000000);
		popupWindow.setBackgroundDrawable(cd);
		popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);

	}

	public void show(String text) {
		titleTextView.setText(text);
		popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);
		handler.postDelayed(runnable, 1000);
	}

	public boolean isShow() {
		return popupWindow.isShowing();
	}

	public void diss() {
		totalTime = 0;
		messageTextView.setText((10 - totalTime) + "");
		popupWindow.dismiss();
		handler.removeCallbacks(runnable);
	}

}
