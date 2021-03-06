package com.zunder.smart.popwindow;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.listener.AlertViewListener;
import com.zunder.smart.roll.WheelView;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.utils.ListNumBer;
import com.zunder.smart.roll.WheelView;
import com.zunder.smart.service.TcpSender;

import java.util.List;

public class AlertViewWindow implements OnClickListener {

	private Activity activity;
	private PopupWindow popupWindow;
	View pupView;
	private List<String> list;

	private int selection=0;
	ImageView save ;
	TextView title;;
	TextView showTxt;
	TextView back;
	WheelView wheel_camera;
	public AlertViewWindow(Activity activity, String name,List<String> _list,String msg,int _selection) {
		super();
		this.activity=activity;
		this.list=_list;
		if(_selection>=0&&selection<list.size()) {
			this.selection = _selection;
		}
		init(name);
		if(msg!=null){
			showTxt.setVisibility(View.VISIBLE);
			showTxt.setText(msg);
		}
	}

	public void setSelection(String msg,int selection){
		this.selection = selection;
		wheel_camera.setSeletion(selection);
		showTxt.setText(msg);
	}
	private AlertViewOnCListener alertViewOnCListener=null;
	public boolean isShow() {
		return popupWindow.isShowing();
	}

	private void init(String name) {
		// TODO Auto-generated method stub
		pupView = ((Activity) activity).getLayoutInflater().inflate(
				R.layout.popwindow_alertview_layout, null);
		save = (ImageView) pupView.findViewById(R.id.save);
		title = (TextView) pupView.findViewById(R.id.titleTxt);
		showTxt = (TextView) pupView.findViewById(R.id.showTxt);
		back=(TextView)pupView.findViewById(R.id.backTxt);
		 wheel_camera = (WheelView) pupView
				.findViewById(R.id.wheel_camera);
		wheel_camera.setOffset(2);
		wheel_camera.setItems(list);
		wheel_camera.setSeletion(selection);
		wheel_camera
				.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

					@Override
					public void onSelected(int selectedIndex, String item) {

						selection=selectedIndex-2;
					}
				});
		title.setText(name);
		save.setOnClickListener(this);
		back.setOnClickListener(this);
		popupWindow = new PopupWindow(pupView,
				LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
		popupWindow.setOutsideTouchable(true);
	}
	public void show() {
		popupWindow.showAtLocation(pupView, Gravity.BOTTOM, 0, 0);
	}

	public void dismiss() {
		TcpSender.setAlertViewListener(null);
		popupWindow.dismiss();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.save:
			if(alertViewOnCListener!=null){
				alertViewOnCListener.onItem(selection,list.get(selection));
			}
			break;
		case R.id.backTxt:
			dismiss();
			break;
		default:
			break;
		}
	}

	public void setAlertViewOnCListener(AlertViewOnCListener alertViewOnCListener) {
		this.alertViewOnCListener = alertViewOnCListener;
	}


	public interface AlertViewOnCListener {
		public void onItem(int pos,String itemName);
	}

}
