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
public class SecurityAlert extends Dialog implements OnClickListener {

	private Activity context;
	private TextView titleTxt;
	EditText editName;
	EditText editValue;
	View search_View;
	private Button cancleBtn, searchBtn, sureBtn;
	ImageView icoImg;

	public SecurityAlert(Activity context) {
		super(context, R.style.MyDialog);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_security_verify);
		this.context = context;
		cancleBtn = (Button) findViewById(R.id.cancle_bt);
		sureBtn = (Button) findViewById(R.id.sure_bt);
		searchBtn = (Button) findViewById(R.id.search_bt);
		editName = (EditText) findViewById(R.id.editName);
		editValue = (EditText) findViewById(R.id.editValue);
		search_View=(View)findViewById(R.id.search_View);
		cancleBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		searchBtn.setOnClickListener(this);
		titleTxt = (TextView) findViewById(R.id.title_tv);
		icoImg = (ImageView) findViewById(R.id.icoImg);
	}

	public void setTitle(int imageId, String title) {
		icoImg.setImageResource(imageId);
		titleTxt.setText(title);
	}

	public void setHint(String strName, String strValue) {
		editName.setHint(strName);
		editValue.setHint(strValue);
	}

	public void setEditeVisible(int strName, int strValue) {
		editName.setVisibility(strName);
		editValue.setVisibility(strValue);
	}

	public void setEditeEnable(Boolean strName, Boolean strValue) {
		editName.setEnabled(strName);
		editValue.setEnabled(strValue);
	}

	public void setButton(String sure, String search) {
		searchBtn.setText(search);
		sureBtn.setText(sure);
	}

	public void setText(String strName, String strValue) {
		editName.setText(strName);
		editValue.setText(strValue);
	}

	public void setInputType(int name, int value) {
		editName.setInputType(name);

		editValue.setInputType(value);
	}

	public void setVisible(int sure, int search, int cancle) {
		cancleBtn.setVisibility(cancle);
		searchBtn.setVisibility(search);
		search_View.setVisibility(search);
		sureBtn.setVisibility(sure);
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
				onSureListener.onSure(editName.getText().toString(), editValue
						.getText().toString());
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

		public void onSure(String editName, String editValue);
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
