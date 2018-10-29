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
import com.zunder.smart.adapter.RadioAdapter;
import com.zunder.smart.model.ItemName;
import com.zunder.smart.model.ItemName;

import java.util.List;

public class RadioCheckAlert extends Dialog implements OnClickListener {

	private Activity activity;

	private TextView title;
	private Button cancleBtn, sureBtn;
	RadioAdapter mutiCheckdapter;
	ListView listView;
	List<ItemName> list;
	public RadioCheckAlert(Activity context,String titleStr) {
		super(context, R.style.MyDialog);
		this.activity = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_radio_verify);
		cancleBtn = (Button) findViewById(R.id.cancle_bt);
		sureBtn = (Button) findViewById(R.id.sure_bt);
		cancleBtn.setOnClickListener(this);
		sureBtn.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		listView=(ListView)findViewById(R.id.listView);
		title.setText(titleStr);
	}
	private OnSureListener onSureListener;

	public void setRadioItems(List<ItemName> _list, String seletItem, int selecIndex, OnRadioClickListener onRadioClickListener){
		this.list=_list;
		mutiCheckdapter=new RadioAdapter(activity,list,seletItem,selecIndex,onRadioClickListener);
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

	public interface  OnRadioClickListener{
		public void onClick(ItemName itemName, int postion, boolean isChecked) ;
	}
	public interface OnSureListener {
		public void onCancle();
		public void onSure();
	}
}
