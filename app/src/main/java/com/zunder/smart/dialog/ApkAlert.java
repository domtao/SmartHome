package com.zunder.smart.dialog;

import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.aiui.activity.AiuiMainActivity;
import com.zunder.smart.broadcast.ReceiverBroadcast;
import com.zunder.smart.listener.DownListener;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.AppTools;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

//ȷ����ʾ��
public class ApkAlert extends Dialog implements OnClickListener, DownListener {

	private Activity context;
	private TextView leftText;
	private Button cancleBtn, sureBtn;
	ProgressBar progressBar;
	int indexValue = 0;

	public ApkAlert(Activity context) {
		super(context, R.style.MyDialog);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_apk_verify);
		ReceiverBroadcast.setDownListener(this);
		cancleBtn = (Button) findViewById(R.id.cancle_bt);
		sureBtn = (Button) findViewById(R.id.sure_bt);
		cancleBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		leftText = (TextView) findViewById(R.id.leftTxt);
		progressBar = (ProgressBar) findViewById(R.id.proBar);

		progressBar.setMax(100);
		leftText.setText("0 %");
	}

	private Handler handler = new Handler() {
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case 1:
				progressBar.setProgress(indexValue);
				leftText.setText((indexValue * 100 / progressBar.getMax())
						+ "%");
				if (indexValue == progressBar.getMax()) {
					// sureBtn.setClickable(true);
					TipAlert alert = new TipAlert(context, context.getString(R.string.tip),
							context.getString(R.string.softreboot));
					alert.show();
					dismiss();
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancle_bt:
			ReceiverBroadcast.setDownListener(null);
			dismiss();
			break;
		case R.id.sure_bt:
			indexValue = 0;
			down();
			sureBtn.setClickable(false);
			break;

		default:
			break;
		}

	}

	@Override
	public void count(String number) {
		// TODO Auto-generated method stub
		if (AppTools.isNumeric(number)) {
			indexValue = Integer.parseInt(number);
			Message msg = handler.obtainMessage();
			msg.what = 1;
			handler.sendMessage(msg);
		}
	}
	public void down() {

		String result = ISocketCode.setUpdateApk("updateApk",
				AiuiMainActivity.deviceID);
		MainActivity.getInstance().sendCode(result);
	}
}
