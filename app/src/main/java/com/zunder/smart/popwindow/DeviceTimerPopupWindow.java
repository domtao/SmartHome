package com.zunder.smart.popwindow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.aiui.activity.SubscribeAddActivity;
import com.zunder.smart.aiui.activity.VoiceAddActivity;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.dialog.CommandEditTxtAlert;
import com.zunder.smart.roll.WheelView;
import com.zunder.smart.utils.ListNumBer;
import com.zunder.smart.aiui.activity.SubscribeAddActivity;
import com.zunder.smart.aiui.activity.VoiceAddActivity;
import com.zunder.smart.roll.WheelView;
import com.zunder.smart.utils.ListNumBer;

import java.util.List;

public class DeviceTimerPopupWindow implements OnClickListener {

	private Activity activity;
	private PopupWindow popupWindow;
	View pupView;
	TextView tipTxt;
	String actionStr = "";
	String minuteStr = "";
	String unitsStr = "";


	public DeviceTimerPopupWindow(Activity activity) {
		super();
		this.activity = activity;
		init();
	}
	private OnCancleListener onSureListener=null;
	public boolean isShow() {
		return popupWindow.isShowing();
	}

	private void init() {
		// TODO Auto-generated method stub
		pupView = ((Activity) activity).getLayoutInflater().inflate(
				R.layout.popwindow_timer_layout, null);
		TextView back=(TextView)pupView.findViewById(R.id.back);
		 tipTxt=(TextView)pupView.findViewById(R.id.tipTxt);

		back.setOnClickListener(this);
		ImageView save = (ImageView) pupView.findViewById(R.id.save);

		WheelView actionView = (WheelView) pupView
				.findViewById(R.id.actionView);
		actionView.setOffset(2);
		actionView.setItems(ListNumBer.getAction());
		actionView.setSeletion(0);

		actionView
				.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
					@Override
					public void onSelected(int selectedIndex, String item) {
						if (item.equals("")) {
							minuteStr = "";
							unitsStr = "";
						}
						actionStr = item;
						tipTxt.setText(actionStr + minuteStr + unitsStr);
					}
				});

		final WheelView numberView = (WheelView) pupView
				.findViewById(R.id.numberView);

		numberView.setOffset(2);
		numberView.setItems(ListNumBer.getMinit());
		numberView.setSeletion(0);

		numberView
				.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
					@Override
					public void onSelected(int selectedIndex, String item) {
						minuteStr = item;
						if (item.equals("")) {
							unitsStr = "";
						} else {
							unitsStr = activity.getString(R.string.Second);
						}
						tipTxt.setText(actionStr + minuteStr + unitsStr);
					}
				});

		final WheelView unitView = (WheelView) pupView
				.findViewById(R.id.unitView);

		unitView.setOffset(2);
		unitView.setItems(ListNumBer.getUnits());
		unitView.setSeletion(0);
		unitView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				unitsStr = item;
				tipTxt.setText(actionStr + minuteStr + unitsStr);
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
		popupWindow.dismiss();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.back:
				dismiss();
				break;
		case R.id.save:
			if(TextUtils.isEmpty(tipTxt.getText())){
				ToastUtils.ShowError(activity,"请选择动作",Toast.LENGTH_SHORT,true);
				return;
			}
			if(onSureListener!=null){
				onSureListener.onSure(tipTxt.getText().toString().trim());
			}
			break;
		default:
			break;
		}
	}

	public void setOnSureListene(OnCancleListener onSureListener) {
		this.onSureListener = onSureListener;
	}

	public interface OnCancleListener {
		public void onSure(String str);
	}
	private void AskTimeSch(final String str) {
		final String items[] = (String[]) activity.getResources()
				.getStringArray(R.array.weeken);
		final boolean[] selected = new boolean[items.length];
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setIcon(R.mipmap.img_air_time_meihong);
		builder.setTitle(str);
		builder.setMultiChoiceItems(items, selected,
				new DialogInterface.OnMultiChoiceClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which,
										boolean isChecked) {
						// TODO Auto-generated method stub
						selected[which] = isChecked;
					}
				});
		builder.setPositiveButton(activity.getString(R.string.sure),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

								String tempWeek = "";
								for (int i = 0; i < selected.length; i++) {
									if (selected[i]) {
										tempWeek += items[i] ;
									}
								}
								if (tempWeek == "") {
									return;
								}
						if(activity instanceof VoiceAddActivity) {
							((VoiceAddActivity)activity).setTxt(str.substring(0,2)+tempWeek);
						}	else if(activity instanceof SubscribeAddActivity) {
							((SubscribeAddActivity)activity).setTxt(str.substring(0,2)+tempWeek);
						}
						dismiss();

					}
				});
		builder.setNegativeButton(activity.getString(R.string.cancle),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				});
		builder.create().show();

	}

	PopupWindow popWindow = null;
	PopupWindow popModeWindow = null;
	String startHourStr = "00";
	String startMinuteStr = "00";
	String endHourStr = "00";
	String endMinuteStr = "00";
	View timeview;
	private void showPopupWindow(final String str) {
		if (popWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			timeview = layoutInflater.inflate(R.layout.popwindow_time_layout,
					null);
			popWindow = new PopupWindow(timeview,
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);
			TextView save = (TextView) timeview.findViewById(R.id.save);

			WheelView startHour = (WheelView) timeview
					.findViewById(R.id.start_hour);
			final WheelView startSecond = (WheelView) timeview
					.findViewById(R.id.start_second);
			startHour.setItems(ListNumBer.getHour());
			startHour.setOffset(2);
			startHour.setSeletion(0);

			startSecond.setItems(ListNumBer.getMinit());
			startSecond.setOffset(2);
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

			final WheelView endHour = (WheelView) timeview.findViewById(R.id.end_hour);
			final WheelView endSecond = (WheelView) timeview
					.findViewById(R.id.end_second);
			endHour.setItems(ListNumBer.getHour());
			endHour.setOffset(2);
			endHour.setSeletion(0);

			endSecond.setItems(ListNumBer.getMinit());
			endSecond.setOffset(2);
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
			save.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String sart = startHourStr + ":" + startMinuteStr;
					String end = endHourStr + ":" + endMinuteStr;
//					if (timeConvert(end) >= timeConvert(sart)) {
						if(activity instanceof  VoiceAddActivity ) {
							((VoiceAddActivity)activity).setTxt(str+"("+sart+"--"+end+")");
						}	else if(activity instanceof SubscribeAddActivity) {
							((SubscribeAddActivity)activity).setTxt(str+"("+sart+"--"+end+")");
						}
						///dismiss();
						popWindow.dismiss();

				}
			});
		}
		popWindow.setOutsideTouchable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {

			}
		});

		popWindow.setTouchInterceptor(new View.OnTouchListener() {
			public boolean onTouch(View view, MotionEvent event) {
				return false;
			}
		});
		popWindow.showAtLocation(timeview, Gravity.BOTTOM, 0, 0);
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
