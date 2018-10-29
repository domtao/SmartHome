package com.zunder.smart.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zunder.smart.R;

//ȷ����ʾ��
public class ApkTipAlert extends Dialog implements OnClickListener {

	private Activity context;
	private TextView titleTxt;

	private Button cancleBtn, sureBtn;
	ImageView icoImg;

	public ApkTipAlert(Activity context) {
		super(context, R.style.MyDialog);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_apk_tip);
		this.context = context;
		cancleBtn = (Button) findViewById(R.id.cancle_bt);
		sureBtn = (Button) findViewById(R.id.sure_bt);
		cancleBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		titleTxt = (TextView) findViewById(R.id.title_tv);
		icoImg = (ImageView) findViewById(R.id.icoImg);
	}

	public void setTitle(int imageId, String title) {
		icoImg.setImageResource(imageId);
		titleTxt.setText(title);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancle_bt:
			dismiss();
			break;
		case R.id.sure_bt:
			if (onSureListener != null) {
				onSureListener.onSure("http://a.app.qq.com/o/simple.jsp?pkgname=com.zunder.smart");
			}
			break;

		default:
			break;
		}

	}

	public interface OnSureListener {
		public void onCancle();

		public void onSure(String str);
	}

	private OnSureListener onSureListener;

	/**
	 * @param onSureListener
	 *            the onSureListener to set
	 */
	public void setOnSureListener(OnSureListener onSureListener) {
		this.onSureListener = onSureListener;
	}

}
