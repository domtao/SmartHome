package com.zunder.smart.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zunder.smart.R;
import com.zunder.smart.adapter.MutiCheckdapter;

public class MutilCheckAlert extends Dialog implements OnClickListener {

	private Activity activity;

	private TextView title;
	private Button cancleBtn, sureBtn;
	MutiCheckdapter mutiCheckdapter;
	ListView listView;
	String[] list;
	public MutilCheckAlert(Activity context) {
		super(context, R.style.MyDialog);
		this.activity = context;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_mutilcheck_verify);
		cancleBtn = (Button) findViewById(R.id.cancle_bt);
		sureBtn = (Button) findViewById(R.id.sure_bt);
		cancleBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		listView=(ListView)findViewById(R.id.listView);
	}
	private OnSureListener onSureListener;
	public void setMultiChoiceItems(String[] _list, boolean[] _selected,OnMultiChoiceClickListener onMultiChoiceClickListener){

		this.list=_list;
		mutiCheckdapter=new MutiCheckdapter(activity,list,_selected,onMultiChoiceClickListener);
		listView.setAdapter(mutiCheckdapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancle_bt:
			dismiss();
			break;
		case R.id.sure_bt:
			if(onSureListener!=null){
				onSureListener.onSure();

			}
			break;
		default:
			break;
		}
	}

	public void setOnSureListener(OnSureListener onSureListener) {
		this.onSureListener = onSureListener;
	}

	public interface  OnMultiChoiceClickListener{
		public void onClick(int postion, boolean isChecked) ;
	}
	public interface OnSureListener {
		public void onCancle();

		public void onSure();
	}
}
