package com.zunder.smart.aiui.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.MyApplication;
import com.zunder.smart.dao.impl.IWidgetDAO;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

public class PickerDialog extends Activity implements OnClickListener {
	private static Context mContext;

	private Button subMitBtn;
	private int mHour;
	private int mMinute;
	private EditText sechInteralTime, sechStartTime;
	TextView sechEndTime;
	private ImageView startTime, endTime;
	String editeStr = "Add";
	String scheStartTime;
	ImageButton appFresh;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.picker_add);

		Bundle bundle = getIntent().getExtras();

		mContext = this;
		findView();

		editeStr = bundle.getSerializable("Edite").toString().trim();
		if (editeStr.equals("Edite")) {

			scheStartTime = bundle.getSerializable("ScheStartTime").toString()
					.trim();
			sechStartTime.setText(scheStartTime);
			sechEndTime.setText(bundle.getSerializable("ScheEndTime")
					.toString().trim());
			sechInteralTime.setText(bundle.getSerializable("ScheIntervalTime")
					.toString().trim());
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		return super.onKeyDown(keyCode, event);
	}

	private void findView() {
		appFresh = (ImageButton) findViewById(R.id.appFresh);
		subMitBtn = (Button) findViewById(R.id.sechSubmit);
		sechStartTime = (EditText) findViewById(R.id.sechStartTime);
		sechInteralTime = (EditText) findViewById(R.id.sechIntervalTime);

		sechEndTime = (TextView) findViewById(R.id.sechEndTime);
		startTime = (ImageView) findViewById(R.id.startTimeSeach);
		endTime = (ImageView) findViewById(R.id.endTimeSeach);
		startTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String startTime = sechStartTime.getText().toString().trim();
				if (startTime.equals("")) {
					Calendar c = Calendar.getInstance();
					mHour = c.get(Calendar.HOUR_OF_DAY);
					mMinute = c.get(Calendar.MINUTE);
				} else {
					String[] timeStr = startTime.split(":");
					mHour = Integer.parseInt(timeStr[0]);
					mMinute = Integer.parseInt(timeStr[1]);

				}

				TimePickerDialog dialog = new TimePickerDialog(mContext,
						mTimeStartListener, mHour, mMinute, false);
				dialog.updateTime(mHour, mMinute);
				dialog.show();
			}
		});
		endTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				clockCycle();
			}
		});
	}

	private void updateStartTimeDisplay() {
		sechStartTime.setText(new StringBuilder().append(mHour).append(":")
				.append((mMinute < 10) ? "0" + mMinute : mMinute));

	}

	/**
	 * ʱ��ؼ��¼�
	 */
	private TimePickerDialog.OnTimeSetListener mTimeStartListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;

			updateStartTimeDisplay();
		}
	};

	private int timeConvert(String timeStr) {

		if (timeStr == "" || timeStr.equals("")) {
			return 0;
		}
		String[] timeStrs = timeStr.split(":");
		return Integer.parseInt(timeStrs[0]) * 60
				+ Integer.parseInt(timeStrs[1]);
	}

	public IWidgetDAO sqlite() {
		return MyApplication.getInstance().getWidgetDataBase();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.appFresh:

				break;

			default:
				break;
		}

	}

	int selitem = 0;
	public void clockCycle() {
		final String[] items = { "每天", "周一至周五", "周末" };
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setIcon(R.mipmap.img_air_time_meihong);
		builder.setTitle("闹钟周期");
		builder.setSingleChoiceItems(items, 0,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						selitem = which;
					}
				});
		builder.setPositiveButton(mContext.getString(R.string.sure),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch (selitem) {
							case 0:
								sechEndTime.setText("每天");
								break;
							case 1:
								sechEndTime.setText("周一 周二 周三 周四 周五");
								break;
							case 2:
								sechEndTime.setText("周六 周日");
								break;

							default:
								break;
						}
					}
				});
		builder.setNegativeButton(mContext.getString(R.string.cancle),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						// unhideenDiaog(dialog,true);
					}
				});
		builder.create().show();
	}
}
