package com.zunder.smart.dialog;

import com.zunder.smart.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

//ȷ����ʾ��
public class EditTxtAlert extends Dialog implements OnClickListener {

	private Activity context;
	private TextView titleTxt;
	EditText editText;
	private Button cancleBtn, sureBtn;
	ImageView icoImg,timeImage;

	int hourOfDay,minute;
	public EditTxtAlert(Activity context) {
		super(context, R.style.MyDialog);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_editeview_verify);
		this.context = context;
		cancleBtn = (Button) findViewById(R.id.cancle_bt);
		sureBtn = (Button) findViewById(R.id.sure_bt);
		editText = (EditText) findViewById(R.id.editTxt);
		editText.clearFocus();
		cancleBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		titleTxt = (TextView) findViewById(R.id.title_tv);
		icoImg = (ImageView) findViewById(R.id.icoImg);
		timeImage=(ImageView)findViewById(R.id.timeImage);
		timeImage.setOnClickListener(this);
	}

	public void setTitle(int imageId, String title ,int time) {
		icoImg.setImageResource(imageId);
		titleTxt.setText(title);
		if (time==1){
			timeImage.setVisibility(View.VISIBLE);
		}
	}
	public void setEditTextType(int type){
		editText.setInputType(type);
	}
	public void setEditText(String text){
		editText.setText(text);
	}
	public void setHint(String str) {
		editText.setHint(str);
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
				onSureListener.onSure(editText.getText().toString());
			}
			break;
			case R.id.timeImage:
				final Calendar ca = Calendar.getInstance();
				hourOfDay = ca.get(Calendar.HOUR_OF_DAY);
				minute = ca.get(Calendar.MINUTE);
				new TimePickerDialog(context, mtimeListener, hourOfDay, minute, true).show();
				break;
		default:
			break;
		}
	}
	private TimePickerDialog.OnTimeSetListener mtimeListener = new TimePickerDialog.OnTimeSetListener()
	{
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			String _hour = (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + "";
			String _minute = (minute < 10 ? "0" + minute : minute) + "";
			editText.setText( _hour+":"+_minute);
		}
	};

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
