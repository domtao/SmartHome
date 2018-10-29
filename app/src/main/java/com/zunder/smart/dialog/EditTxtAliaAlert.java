package com.zunder.smart.dialog;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditTxtAliaAlert extends Dialog implements OnClickListener {

	private Activity context;
	private TextView titleTxt;
	EditText edit1, edit2, edit3, edit4, edit5, edit6, edit7, edit8;
	private Button cancleBtn, sureBtn;
	ImageView icoImg;

	public EditTxtAliaAlert(Activity context) {
		super(context, R.style.MyDialog);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_alia_verify);
		this.context = context;
		cancleBtn = (Button) findViewById(R.id.cancle_bt);
		sureBtn = (Button) findViewById(R.id.sure_bt);
		edit1 = (EditText) findViewById(R.id.edit1);
		edit2 = (EditText) findViewById(R.id.edit2);
		edit3 = (EditText) findViewById(R.id.edit3);
		edit4 = (EditText) findViewById(R.id.edit4);
		edit5 = (EditText) findViewById(R.id.edit5);
		edit6 = (EditText) findViewById(R.id.edit6);
		edit7 = (EditText) findViewById(R.id.edit7);
		edit8 = (EditText) findViewById(R.id.edit8);
		cancleBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		titleTxt = (TextView) findViewById(R.id.title_tv);
		icoImg = (ImageView) findViewById(R.id.icoImg);
		edit1.clearFocus();
		edit2.clearFocus();
		edit3.clearFocus();
		edit4.clearFocus();
		edit5.clearFocus();
		edit6.clearFocus();
		edit7.clearFocus();
		edit8.clearFocus();
	}
	public void setTitle(int imageId, String title) {
		icoImg.setImageResource(imageId);
		titleTxt.setText(title);
	}

	public void setValues(String[] names) {
		if (names.length == 8) {
			edit1.setText(names[0]);
			edit2.setText(names[1]);
			edit3.setText(names[2]);
			edit4.setText(names[3]);
			edit5.setText(names[4]);
			edit6.setText(names[5]);
			edit7.setText(names[6]);
			edit8.setText(names[7]);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancle_bt:
			dismiss();
			break;
		case R.id.sure_bt:
			if(TextUtils.isEmpty(edit1.getText())||TextUtils.isEmpty(edit2.getText())||TextUtils.isEmpty(edit3.getText())||TextUtils.isEmpty(edit4.getText())||
					TextUtils.isEmpty(edit5.getText())||TextUtils.isEmpty(edit6.getText())||TextUtils.isEmpty(edit7.getText())||TextUtils.isEmpty(edit8.getText())){
				ToastUtils.ShowError(context,"别名不能为空", Toast.LENGTH_SHORT,true);
				return;
			}
			String resString = edit1.getText() + "," + edit2.getText() + ","
					+ edit3.getText() + "," + edit4.getText() + ","
					+ edit5.getText() + "," + edit6.getText() + ","
					+ edit7.getText() + "," + edit8.getText();
			if (onSureListener != null) {
				onSureListener.onSure(resString);
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
	 * the onSureListener to set
	 */
	public void setOnSureListener(OnSureListener onSureListener) {
		this.onSureListener = onSureListener;
	}

}
