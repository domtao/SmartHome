package com.zunder.smart.dialog;

import com.zunder.smart.R;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//ȷ����ʾ��
public class ButtonAlert extends Dialog implements OnClickListener {

	private Activity context;
	private TextView titleTxt;
	private Button cancleBtn, searchBtn, sureBtn;
	ImageView icoImg;
	TextView msgTxt;

	public ButtonAlert(Activity context) {
		super(context, R.style.MyDialog);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_button_verify);
		this.context = context;
		cancleBtn = (Button) findViewById(R.id.cancle_bt);
		sureBtn = (Button) findViewById(R.id.sure_bt);
		searchBtn = (Button) findViewById(R.id.search_bt);
		msgTxt=(TextView)findViewById(R.id.msgTxt);
		cancleBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		searchBtn.setOnClickListener(this);
		titleTxt = (TextView) findViewById(R.id.title_tv);
		icoImg = (ImageView) findViewById(R.id.icoImg);
	}

	public void setTitle(int imageId, String title,String msg) {
		icoImg.setImageResource(imageId);
		msgTxt.setText(msg);
		titleTxt.setText(title);
	}

	public void setButton(String sure, String search, String cancle) {
		searchBtn.setText(search);
		sureBtn.setText(sure);
		cancleBtn.setText(cancle);
	}

	public void setVisible(int sure, int search, int cancle) {
		cancleBtn.setVisibility(cancle);
		searchBtn.setVisibility(search);
		sureBtn.setVisibility(sure);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancle_bt:
			if (onSureListener != null) {
				onSureListener.onCancle();
			}
			dismiss();
			break;
		case R.id.sure_bt:
			if (onSureListener != null) {
				onSureListener.onSure();
			}
			break;
		case R.id.search_bt:
			if (onSureListener != null) {
				onSureListener.onSearch();
			}
			break;

		default:
			break;
		}

	}

	public interface OnSureListener {
		public void onCancle();

		public void onSearch();

		public void onSure();
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
