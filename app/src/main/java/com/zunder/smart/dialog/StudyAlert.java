package com.zunder.smart.dialog;

import com.zunder.smart.R;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

//ȷ����ʾ��
public class StudyAlert extends Dialog implements OnClickListener {

	private Activity context;
	private TextView titleTxt;
	EditText editName;

	private Button preBtn, studyBtn, sendBtn, nextBtn;
	ImageView nameSearch;
	ImageView icoImg;

	public StudyAlert(Activity context) {
		super(context, R.style.MyDialog);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_study_verify);
		this.context = context;
		preBtn = (Button) findViewById(R.id.pre_bt);
		studyBtn = (Button) findViewById(R.id.study_bt);
		sendBtn = (Button) findViewById(R.id.send_bt);
		nextBtn = (Button) findViewById(R.id.next_bt);
		editName = (EditText) findViewById(R.id.editName);

		nameSearch = (ImageView) findViewById(R.id.nameSearch);
		nameSearch.setOnClickListener(this);
		preBtn.setOnClickListener(this);
		studyBtn.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);

		titleTxt = (TextView) findViewById(R.id.title_tv);
		icoImg = (ImageView) findViewById(R.id.icoImg);
	}

	public void setTitle(int imageId, String title) {
		icoImg.setImageResource(imageId);
		titleTxt.setText(title);
	}

	public void setHint(String strName) {
		editName.setHint(strName);
	}

	public void setEditeName(String strName) {
		editName.setText(strName);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.pre_bt:
			if (onSureListener != null) {
				onSureListener.onPre(editName.getText().toString());
			}
			break;
		case R.id.study_bt:
			if (onSureListener != null) {
				onSureListener.onStudy(editName.getText().toString());
			}
			break;
		case R.id.send_bt:
			if (onSureListener != null) {
				onSureListener.onSend(editName.getText().toString());
			}
			break;
		case R.id.nameSearch:
			if (onSureListener != null) {
				onSureListener.onSearch();
			}
			break;
		case R.id.next_bt:
			if (onSureListener != null) {
				onSureListener.onNext(editName.getText().toString());
			}
			break;

		default:
			break;
		}

	}

	public interface OnSureListener {
		public void onPre(String name);

		public void onStudy(String name);

		public void onSearch();

		public void onSend(String name);

		public void onNext(String name);
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
