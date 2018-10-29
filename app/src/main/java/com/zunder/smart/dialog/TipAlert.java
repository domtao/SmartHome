package com.zunder.smart.dialog;

import com.zunder.smart.R;

import a.a.a.a.c;
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

//ȷ����ʾ��
public class TipAlert implements OnClickListener {

	private Activity context;
	private PopupWindow popupWindow;
	private View view;
	private TextView titleTextView;
	private TextView messageTextView;
	private Button sureBtn;

	public interface OnSureListener {
		public void onSure();
	}

	private OnSureListener sureListener;

	public void setSureListener(OnSureListener sureListener) {
		this.sureListener = sureListener;
	}

	public TipAlert(Activity context, String title, String message) {
		super();
		this.context = context;
		init(title, message);
	}

	private void init(String title, String message) {
		LayoutInflater inflater = context.getLayoutInflater();
		view = inflater.inflate(R.layout.alert_tip_verify, null);
		sureBtn = (Button) view.findViewById(R.id.sure_tip);
		sureBtn.setOnClickListener(this);
		titleTextView = (TextView) view.findViewById(R.id.title_tip);
		messageTextView = (TextView) view.findViewById(R.id.message_tip);
		titleTextView.setText(title);
		messageTextView.setText(message);
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		ColorDrawable cd = new ColorDrawable(0xb0000000);
		popupWindow.setBackgroundDrawable(cd);
		popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);

	}

	public void show() {
		popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sure_tip:
			if (sureListener != null) {
				sureListener.onSure();
			}
			popupWindow.dismiss();
			break;

		default:
			break;
		}

	}

	public boolean isShow() {
		return popupWindow.isShowing();
	}
}
