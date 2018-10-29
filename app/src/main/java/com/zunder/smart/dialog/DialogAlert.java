package com.zunder.smart.dialog;

import com.zunder.smart.R;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

//ȷ����ʾ��
public class DialogAlert extends Dialog implements OnClickListener {

	private Activity context;
	private TextView titleTextView;
	private TextView messageTextView;
	private Button cancleBtn, sureBtn;

	public interface OnSureListener {
		public void onCancle();

		public void onSure();
	}

	private OnSureListener sureListener;

	public void setSureListener(OnSureListener sureListener) {
		this.sureListener = sureListener;
	}

	public DialogAlert(Activity context) {
		super(context, R.style.MyDialog);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_dialog_verify);
		this.context = context;

		cancleBtn = (Button) findViewById(R.id.cancle_bt);
		sureBtn = (Button) findViewById(R.id.sure_bt);
		cancleBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		titleTextView = (TextView) findViewById(R.id.title_tv);
		messageTextView = (TextView) findViewById(R.id.message_tv);
	}

	public void init(String title, String message) {

//		titleTextView.setText(title);
		messageTextView.setText(message);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancle_bt:
			sureListener.onCancle();
			dismiss();
			break;
		case R.id.sure_bt:
			sureListener.onSure();
			dismiss();
			break;

		default:
			break;
		}

	}

}
