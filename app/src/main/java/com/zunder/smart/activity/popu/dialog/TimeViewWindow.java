package com.zunder.smart.activity.popu.dialog;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zunder.smart.R;
import com.zunder.smart.roll.WheelView;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.utils.ListNumBer;

import java.util.List;

public class TimeViewWindow implements OnClickListener{

	private Activity activity;
	private PopupWindow popupWindow;
	View pupView;

	ImageView save ;
	String startHourStr = "00";
	String startMinuteStr = "00";
	String endHourStr = "00";
	String endMinuteStr = "00";
	public TimeViewWindow(Activity activity) {
		super();
		this.activity=activity;
		init();

	}
	private AlertViewOnCListener alertViewOnCListener=null;
	public boolean isShow() {
		return popupWindow.isShowing();
	}

	private void init() {
		// TODO Auto-generated method stub
		pupView = ((Activity) activity).getLayoutInflater().inflate(
				R.layout.popwindow_time_layout, null);

		 save = (ImageView) pupView.findViewById(R.id.save);

		WheelView startHour = (WheelView) pupView
				.findViewById(R.id.start_hour);
		final WheelView startSecond = (WheelView) pupView
				.findViewById(R.id.start_second);
		startHour.setOffset(2);
		startHour.setItems(ListNumBer.getHour());
		startHour.setSeletion(0);

		startSecond.setOffset(2);
		startSecond.setItems(ListNumBer.getMinit());
		startSecond.setSeletion(0);

		startHour
				.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

					@Override
					public void onSelected(int selectedIndex, String item) {
						startHourStr = item;
					}
				});

		startSecond
				.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
					@Override
					public void onSelected(int selectedIndex, String item) {
						startMinuteStr = item;
					}
				});

		WheelView endHour = (WheelView) pupView.findViewById(R.id.end_hour);
		final WheelView endSecond = (WheelView) pupView
				.findViewById(R.id.end_second);
		endHour.setOffset(2);
		endHour.setItems(ListNumBer.getHour());
		endHour.setSeletion(0);

		endSecond.setOffset(2);
		endSecond.setItems(ListNumBer.getMinit());
		endSecond.setSeletion(0);

		endHour.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

			@Override
			public void onSelected(int selectedIndex, String item) {
				endHourStr = item;
			}
		});
		endSecond
				.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
					@Override
					public void onSelected(int selectedIndex, String item) {
						endMinuteStr = item;
					}
				});
		save.setOnClickListener(this);

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
			String sart = startHourStr + ":" + startMinuteStr;
			String end = endHourStr + ":" + endMinuteStr;
			if (timeConvert(end) >= timeConvert(sart)) {
				if(alertViewOnCListener!=null){
					alertViewOnCListener.onItem(0,sart+"~"+end);
				}
			} else {
				Toast.makeText(activity, "时间格式错误", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.backTxt:
			if(alertViewOnCListener!=null){
				alertViewOnCListener.cancle();
			}
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
		public void onItem(int pos, String itemName);
		public void cancle();
	}
	private int timeConvert(String timeStr) {
		if (timeStr == "" || timeStr.equals("")) {
			return 0;
		}
		String[] timeStrs = timeStr.split(":");
		return Integer.parseInt(timeStrs[0]) * 60
				+ Integer.parseInt(timeStrs[1]);
	}
}