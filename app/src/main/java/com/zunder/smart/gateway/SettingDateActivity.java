/**
 * 
 */
package com.zunder.smart.gateway;

import hsl.p2pipcam.nativecaller.DeviceSDK;

import java.util.Calendar;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import com.zunder.smart.R;
import com.zunder.smart.adapter.GateWayAdapter;
import com.zunder.smart.gateway.bean.DateModel;
import com.zunder.smart.listener.DevicePramsListener;
import com.zunder.smart.service.BridgeService;
import com.zunder.smart.adapter.GateWayAdapter;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ʱ������
 * 
 * @author wang.jingui
 * 
 */
public class SettingDateActivity extends BaseActivity implements
		DevicePramsListener, OnClickListener {
	private TextView deviceDateItem;
	private EditText timeZoneItem;
	private CheckBox dateCheck;
	private EditText ntpItem;
	private Button checkTimeItem;
	private ImageButton zoneSeletedItem;
	private ImageButton ntpSeletedItem;
	private LinearLayout ntpView;

	private PopupWindow timeZonePopWindow = null;
	private PopupWindow ntpServerPopWindow = null;
	private long userid;
	private DateModel dateModel = new DateModel();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gateway_date);
		initView();

		showProgressDialog("获取时钟参数");
		BridgeService.setDevicePramsListener(this);
		userid = GateWayAdapter.userid;

		DeviceSDK.getDeviceParam(userid, 0x2016);
	}

	private void initView() {
		backItem = (TextView) findViewById(R.id.back_item);
		backItem.setText("返回");
		titleItem = (TextView) findViewById(R.id.title_item);
		titleItem.setText("时钟设置");
		functionItem = (TextView) findViewById(R.id.fun_item);
		functionItem.setOnClickListener(this);
		backItem.setOnClickListener(this);

		deviceDateItem = (TextView) findViewById(R.id.date_tv_device_time);
		timeZoneItem = (EditText) findViewById(R.id.date_edit_timezone);
		dateCheck = (CheckBox) findViewById(R.id.date_cbx_check);
		ntpItem = (EditText) findViewById(R.id.date_edit_ntp_server);
		ntpView = (LinearLayout) findViewById(R.id.date_ntp_view);

		checkTimeItem = (Button) findViewById(R.id.date_btn_checkout);
		zoneSeletedItem = (ImageButton) findViewById(R.id.date_img_timezone_down);
		ntpSeletedItem = (ImageButton) findViewById(R.id.date_img_ntp_server_down);

		checkTimeItem.setOnClickListener(this);
		zoneSeletedItem.setOnClickListener(this);
		ntpSeletedItem.setOnClickListener(this);
		dateCheck.setOnClickListener(this);

	}

	@Override
	protected void function() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("now", 0);
			obj.put("timezone", dateModel.getTz());
			obj.put("ntp_enable", dateModel.getNtp_enable());
			obj.put("ntp_svr", dateModel.getNtp_ser());
			int Result = DeviceSDK.setDeviceParam(userid, 0x2015,
					obj.toString());
			if (Result > 0) {
				Toast.makeText(SettingDateActivity.this, "时钟设置成功.",
						Toast.LENGTH_SHORT).show();
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			showToast(getResources().getString(R.string.date_setting_failed));
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		switch (id) {
		case R.id.date_btn_checkout:
			TimeZone timeZone = TimeZone.getDefault();
			int tz = -timeZone.getRawOffset() / 1000;
			Calendar calendar = Calendar.getInstance();
			int now = (int) (calendar.getTimeInMillis() / 1000);
			try {
				JSONObject obj = new JSONObject();
				obj.put("now", now);
				obj.put("timezone", tz);
				obj.put("ntp_enable", dateModel.getNtp_enable());
				obj.put("ntp_svr", dateModel.getNtp_ser());
				int Result = DeviceSDK.setDeviceParam(userid, 0x2015,
						obj.toString());
				if (Result > 0) {
					Toast.makeText(SettingDateActivity.this, "时钟校准成功.",
							Toast.LENGTH_SHORT).show();
					try {
						Thread.sleep(100);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.date_cbx_check:
			if (dateCheck.isChecked()) {
				dateModel.setNtp_enable(1);
				ntpView.setVisibility(View.VISIBLE);
			} else {
				dateModel.setNtp_enable(0);
				ntpView.setVisibility(View.GONE);
			}
			break;

		case R.id.date_img_timezone_down:
			showTimeZonePopWindow();
			break;
		case R.id.date_img_ntp_server_down:
			showNtpServerPopWindow();
			break;
		// /ntpServer
		case R.id.date_ntpserver_kriss:
			ntpServerPopWindow.dismiss();
			dateModel.setNtp_ser(getResources().getString(
					R.string.date_ntp_server_time_kriss_re_kr));
			ntpItem.setText(R.string.date_ntp_server_time_kriss_re_kr);
			break;
		case R.id.date_ntpserver_nist:
			ntpServerPopWindow.dismiss();
			dateModel.setNtp_ser(getResources().getString(
					R.string.date_ntp_server_time_nist_gov));
			ntpItem.setText(R.string.date_ntp_server_time_nist_gov);
			break;
		case R.id.date_ntpserver_nuri:
			ntpServerPopWindow.dismiss();
			dateModel.setNtp_ser(getResources().getString(
					R.string.date_ntp_server_time_nuri_net));
			ntpItem.setText(R.string.date_ntp_server_time_nuri_net);
			break;
		case R.id.date_ntpserver_windows:
			ntpServerPopWindow.dismiss();
			dateModel.setNtp_ser(getResources().getString(
					R.string.date_ntp_server_time_windows_com));
			ntpItem.setText(R.string.date_ntp_server_time_windows_com);
			break;

		// timezone
		case R.id.date_zone_middle_island:
			timeZonePopWindow.dismiss();
			dateModel.setTz(39600);
			timeZoneItem.setText(R.string.date_middle_island);
			break;
		case R.id.date_zone_hawaii:
			timeZonePopWindow.dismiss();
			dateModel.setTz(36000);
			timeZoneItem.setText(R.string.date_hawaii);
			break;
		case R.id.date_zone_alaska:
			timeZonePopWindow.dismiss();
			dateModel.setTz(32400);
			timeZoneItem.setText(R.string.date_alaska);
			break;
		case R.id.date_zone_pacific_time:
			timeZonePopWindow.dismiss();
			dateModel.setTz(28800);
			timeZoneItem.setText(R.string.date_pacific_time);
			break;
		case R.id.date_zone_mountain_time:
			timeZonePopWindow.dismiss();
			dateModel.setTz(25200);
			timeZoneItem.setText(R.string.date_mountain_time);
			break;
		case R.id.date_zone_middle_part_time:
			timeZonePopWindow.dismiss();
			dateModel.setTz(21600);
			timeZoneItem.setText(R.string.date_middle_part_time);
			break;
		case R.id.date_zone_eastern_time:
			timeZonePopWindow.dismiss();
			dateModel.setTz(18000);
			timeZoneItem.setText(R.string.date_eastern_time);
			break;
		case R.id.date_zone_ocean_time:
			timeZonePopWindow.dismiss();
			dateModel.setTz(14400);
			timeZoneItem.setText(R.string.date_ocean_time);
			break;
		case R.id.date_zone_newfoundland:
			timeZonePopWindow.dismiss();
			dateModel.setTz(12600);
			timeZoneItem.setText(R.string.date_newfoundland);
			break;
		case R.id.date_zone_brasilia:
			timeZonePopWindow.dismiss();
			dateModel.setTz(10800);
			timeZoneItem.setText(R.string.date_brasilia);
			break;
		case R.id.date_zone_center_ocean:
			timeZonePopWindow.dismiss();
			dateModel.setTz(7200);
			timeZoneItem.setText(R.string.date_center_ocean);
			break;
		case R.id.date_zone_cap_verde_island:
			timeZonePopWindow.dismiss();
			dateModel.setTz(3600);
			timeZoneItem.setText(R.string.date_cape_verde_island);
			break;
		case R.id.date_zone_greenwich:
			timeZonePopWindow.dismiss();
			dateModel.setTz(0);
			timeZoneItem.setText(R.string.date_greenwich);
			break;
		case R.id.date_zone_brussels:
			timeZonePopWindow.dismiss();
			dateModel.setTz(-3600);
			timeZoneItem.setText(R.string.date_brussels);
			break;
		case R.id.date_zone_athens:
			timeZonePopWindow.dismiss();
			dateModel.setTz(-7200);
			timeZoneItem.setText(R.string.date_athens);
			break;
		case R.id.date_zone_nairobi:
			timeZonePopWindow.dismiss();
			dateModel.setTz(-10800);
			timeZoneItem.setText(R.string.date_nairobi);
			break;
		case R.id.date_zone_teheran:
			timeZonePopWindow.dismiss();
			dateModel.setTz(-12600);
			timeZoneItem.setText(R.string.date_teheran);
			break;
		case R.id.date_zone_baku:
			timeZonePopWindow.dismiss();
			dateModel.setTz(-14400);
			timeZoneItem.setText(R.string.date_baku);
			break;
		case R.id.date_zone_kebuer:
			timeZonePopWindow.dismiss();
			dateModel.setTz(-16200);
			timeZoneItem.setText(R.string.date_kebuer);
			break;
		case R.id.date_zone_islamabad:
			timeZonePopWindow.dismiss();
			dateModel.setTz(-18000);
			timeZoneItem.setText(R.string.date_islamabad);
			break;
		case R.id.date_zone_calcutta:
			timeZonePopWindow.dismiss();
			dateModel.setTz(-19800);
			timeZoneItem.setText(R.string.date_calcutta);
			break;
		case R.id.date_zone_alamotu:
			timeZonePopWindow.dismiss();
			dateModel.setTz(-21600);
			timeZoneItem.setText(R.string.date_alamotu);
			break;
		case R.id.date_zone_bangkok:
			timeZonePopWindow.dismiss();
			dateModel.setTz(-25200);
			timeZoneItem.setText(R.string.date_bangkok);
			break;
		case R.id.date_zone_beijing:
			timeZonePopWindow.dismiss();
			dateModel.setTz(-28800);
			timeZoneItem.setText(R.string.date_beijing);
			break;
		case R.id.date_zone_seoul:
			timeZonePopWindow.dismiss();
			dateModel.setTz(-32400);
			timeZoneItem.setText(R.string.date_seoul);
			break;
		case R.id.date_zone_darwin:
			timeZonePopWindow.dismiss();
			dateModel.setTz(-34200);
			timeZoneItem.setText(R.string.date_darwin);
			break;
		case R.id.date_zone_guam:
			timeZonePopWindow.dismiss();
			dateModel.setTz(-36000);
			timeZoneItem.setText(R.string.date_guam);
			break;
		case R.id.date_zone_soulumen:
			timeZonePopWindow.dismiss();
			dateModel.setTz(-39600);
			timeZoneItem.setText(R.string.date_suolumen);
			break;
		case R.id.date_zone_auckland:
			timeZonePopWindow.dismiss();
			dateModel.setTz(-43200);
			timeZoneItem.setText(R.string.date_auckland);
			break;
		case R.id.fun_item:
			function();
			break;
		case R.id.back_item:
			this.finish();
			break;
		default:
			break;
		}

	}

	private void showNtpServerPopWindow() {
		LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.gatewaydate_ntpserver_popwindow, null);
		Button ntpServerKriss = (Button) layout
				.findViewById(R.id.date_ntpserver_kriss);
		Button ntpServerNist = (Button) layout
				.findViewById(R.id.date_ntpserver_nist);
		Button ntpServerNuri = (Button) layout
				.findViewById(R.id.date_ntpserver_nuri);
		Button ntpServerWindows = (Button) layout
				.findViewById(R.id.date_ntpserver_windows);
		ntpServerKriss.setOnClickListener(this);
		ntpServerNist.setOnClickListener(this);
		ntpServerNuri.setOnClickListener(this);
		ntpServerWindows.setOnClickListener(this);
		ntpServerPopWindow = new PopupWindow(layout, 400,
				WindowManager.LayoutParams.WRAP_CONTENT);
		ntpServerPopWindow.setFocusable(true);
		ntpServerPopWindow.setOutsideTouchable(true);
		ntpServerPopWindow.setBackgroundDrawable(new ColorDrawable(0));
		ntpServerPopWindow.showAsDropDown(ntpSeletedItem, -200, 0);
	}

	private void showTimeZonePopWindow() {
		LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.gatewaydate_timezone_popwindow, null);
		Button zoneMiddleIsland = (Button) layout
				.findViewById(R.id.date_zone_middle_island);
		Button zoneHawaii = (Button) layout.findViewById(R.id.date_zone_hawaii);
		Button zoneAlaska = (Button) layout.findViewById(R.id.date_zone_alaska);
		Button zonePacificTime = (Button) layout
				.findViewById(R.id.date_zone_pacific_time);
		Button zoneMountainTime = (Button) layout
				.findViewById(R.id.date_zone_mountain_time);
		Button zoneMiddlePartTime = (Button) layout
				.findViewById(R.id.date_zone_middle_part_time);
		Button zoneEasternTime = (Button) layout
				.findViewById(R.id.date_zone_eastern_time);
		Button zoneOceanTime = (Button) layout
				.findViewById(R.id.date_zone_ocean_time);
		Button zoneNewfoundland = (Button) layout
				.findViewById(R.id.date_zone_newfoundland);
		Button zoneBrasilia = (Button) layout
				.findViewById(R.id.date_zone_brasilia);
		Button zoneCenterOcean = (Button) layout
				.findViewById(R.id.date_zone_center_ocean);
		Button zoneCapeVerdeIsland = (Button) layout
				.findViewById(R.id.date_zone_cap_verde_island);
		Button zoneGreenWich = (Button) layout
				.findViewById(R.id.date_zone_greenwich);
		Button zoneBrussels = (Button) layout
				.findViewById(R.id.date_zone_brussels);
		Button zoneAthens = (Button) layout.findViewById(R.id.date_zone_athens);
		Button zoneNairobi = (Button) layout
				.findViewById(R.id.date_zone_nairobi);
		Button zoneTeheran = (Button) layout
				.findViewById(R.id.date_zone_teheran);
		Button zoneBaku = (Button) layout.findViewById(R.id.date_zone_baku);
		Button zoneKebuer = (Button) layout.findViewById(R.id.date_zone_kebuer);
		Button zoneIslamambad = (Button) layout
				.findViewById(R.id.date_zone_islamabad);
		Button zoneIslamabad = (Button) layout
				.findViewById(R.id.date_zone_calcutta);
		Button zoneAlamotu = (Button) layout
				.findViewById(R.id.date_zone_alamotu);
		Button zoneBangkok = (Button) layout
				.findViewById(R.id.date_zone_bangkok);
		Button zoneBeijing = (Button) layout
				.findViewById(R.id.date_zone_beijing);
		Button zoneSeoul = (Button) layout.findViewById(R.id.date_zone_seoul);
		Button zoneDarwin = (Button) layout.findViewById(R.id.date_zone_darwin);
		Button zoneGuam = (Button) layout.findViewById(R.id.date_zone_guam);
		Button zoneSoulumen = (Button) layout
				.findViewById(R.id.date_zone_soulumen);
		Button zoneAuckland = (Button) layout
				.findViewById(R.id.date_zone_auckland);

		zoneMiddleIsland.setOnClickListener(this);
		zoneHawaii.setOnClickListener(this);
		zoneAlaska.setOnClickListener(this);
		zonePacificTime.setOnClickListener(this);
		zoneMountainTime.setOnClickListener(this);
		zoneMiddlePartTime.setOnClickListener(this);
		zoneEasternTime.setOnClickListener(this);
		zoneOceanTime.setOnClickListener(this);
		zoneNewfoundland.setOnClickListener(this);
		zoneBrasilia.setOnClickListener(this);
		zoneCenterOcean.setOnClickListener(this);
		zoneCapeVerdeIsland.setOnClickListener(this);
		zoneGreenWich.setOnClickListener(this);
		zoneBrussels.setOnClickListener(this);
		zoneAthens.setOnClickListener(this);
		zoneNairobi.setOnClickListener(this);
		zoneTeheran.setOnClickListener(this);
		zoneBaku.setOnClickListener(this);
		zoneKebuer.setOnClickListener(this);
		zoneIslamambad.setOnClickListener(this);
		zoneIslamabad.setOnClickListener(this);
		zoneAlamotu.setOnClickListener(this);
		zoneBangkok.setOnClickListener(this);
		zoneBeijing.setOnClickListener(this);
		zoneSeoul.setOnClickListener(this);
		zoneDarwin.setOnClickListener(this);
		zoneGuam.setOnClickListener(this);
		zoneSoulumen.setOnClickListener(this);
		zoneAuckland.setOnClickListener(this);

		timeZonePopWindow = new PopupWindow(layout, 700,
				WindowManager.LayoutParams.WRAP_CONTENT);
		timeZonePopWindow.setFocusable(true);
		timeZonePopWindow.setOutsideTouchable(true);
		timeZonePopWindow.setBackgroundDrawable(new ColorDrawable(0));
		timeZonePopWindow
				.setOnDismissListener(new PopupWindow.OnDismissListener() {
					@Override
					public void onDismiss() {
						timeZonePopWindow.dismiss();
					}
				});
		timeZonePopWindow.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (arg1.getAction() == MotionEvent.ACTION_OUTSIDE) {
					timeZonePopWindow.dismiss();
				}
				return false;
			}
		});
		timeZonePopWindow.showAsDropDown(zoneSeletedItem, -310, 0);
	}

	private Handler freshHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				hideProgressDialog();

				if (dateModel.getNtp_enable() == 1) {
					dateCheck.setChecked(true);
					ntpView.setVisibility(View.VISIBLE);
				} else {
					dateCheck.setChecked(false);
					ntpView.setVisibility(View.GONE);
				}
				ntpItem.setText(dateModel.getNtp_ser());
				deviceDateItem.setText("longtime:" + dateModel.getNow());
				setTimeZone();
			} else if (msg.what == 1) {
				if (msg.arg1 == 0) {
					showToast(getResources().getString(
							R.string.date_setting_failed));
				} else {
					showToast(getResources().getString(
							R.string.date_setting_success));
					finish();
				}
			}
		}
	};

	private void setTimeZone() {
		int utc = dateModel.getNow();
		Long lon = new Long(utc);
		switch (dateModel.getTz()) {
		case 39600:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT-11:00"));
			timeZoneItem.setText(R.string.date_middle_island);
			break;
		case 36000:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT-10:00"));
			timeZoneItem.setText(R.string.date_hawaii);
			break;
		case 32400:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT-09:00"));
			timeZoneItem.setText(R.string.date_alaska);
			break;
		case 28800:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT-08:00"));
			timeZoneItem.setText(R.string.date_pacific_time);
			break;
		case 25200:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT-07:00"));
			timeZoneItem.setText(R.string.date_mountain_time);
			break;
		case 21600:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT-06:00"));
			timeZoneItem.setText(R.string.date_middle_part_time);
			break;
		case 18000:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT-05:00"));
			timeZoneItem.setText(R.string.date_eastern_time);
			break;
		case 14400:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT-04:00"));
			timeZoneItem.setText(R.string.date_ocean_time);
			break;
		case 12600:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT-03:30"));
			ntpItem.setText(R.string.date_newfoundland);
			break;
		case 10800:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT-03:00"));
			timeZoneItem.setText(R.string.date_brasilia);
			break;
		case 7200:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT-02:00"));
			timeZoneItem.setText(R.string.date_center_ocean);
			break;
		case 3600:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT-01:00"));
			timeZoneItem.setText(R.string.date_cape_verde_island);
			break;
		case 0:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT"));
			timeZoneItem.setText(R.string.date_greenwich);
			break;
		case -3600:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT+01:00"));
			timeZoneItem.setText(R.string.date_brussels);
			break;
		case -7200:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT+02:00"));
			timeZoneItem.setText(R.string.date_athens);
			break;
		case -10800:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT+03:00"));
			timeZoneItem.setText(R.string.date_nairobi);
			break;
		case -12600:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT+03:30"));
			timeZoneItem.setText(R.string.date_teheran);
			break;
		case -14400:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT+04:00"));
			timeZoneItem.setText(R.string.date_baku);
			break;
		case -16200:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT+04:30"));
			timeZoneItem.setText(R.string.date_kebuer);
			break;
		case -18000:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT+05:00"));
			timeZoneItem.setText(R.string.date_islamabad);
			break;
		case -19800:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT+05:30"));
			timeZoneItem.setText(R.string.date_calcutta);
			break;

		case -21600:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT+06:00"));
			timeZoneItem.setText(R.string.date_alamotu);
			break;
		case -25200:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT+07:00"));
			timeZoneItem.setText(R.string.date_bangkok);
			break;
		case -28800:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT+08:00"));
			timeZoneItem.setText(R.string.date_beijing);
			break;
		case -32400:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT+09:00"));
			timeZoneItem.setText(R.string.date_seoul);
			break;
		case -34200:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT+09:30"));
			timeZoneItem.setText(R.string.date_darwin);
			break;
		case -36000:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT+10:00"));
			timeZoneItem.setText(R.string.date_guam);
			break;
		case -39600:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT+11:00"));
			timeZoneItem.setText(R.string.date_suolumen);
			break;
		case -43200:
			deviceDateItem.setText(setDeviceTime(lon * 1000, "GMT+12:00"));
			timeZoneItem.setText(R.string.date_auckland);
			break;
		default:
			break;
		}
	}

	private String setDeviceTime(long millisutc, String tz) {

		TimeZone timeZone = TimeZone.getTimeZone(tz);
		Calendar calendar = Calendar.getInstance(timeZone);
		calendar.setTimeInMillis(millisutc);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int weekNum = calendar.get(Calendar.DAY_OF_WEEK);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);

		String strWeek = "";
		switch (weekNum) {
		case 1:
			strWeek = "Sun";
			break;
		case 2:
			strWeek = "Mon";
			break;
		case 3:
			strWeek = "Tues";
			break;
		case 4:
			strWeek = "Wed";
			break;
		case 5:
			strWeek = "Thur";
			break;
		case 6:
			strWeek = "Fri";
			break;
		case 7:
			strWeek = "Sat";
			break;
		}
		String strMonth = "";
		switch (month) {
		case 1:
			strMonth = "Jan";
			break;
		case 2:
			strMonth = "Feb";
			break;
		case 3:
			strMonth = "Mar";
			break;
		case 4:
			strMonth = "Apr";
			break;
		case 5:
			strMonth = "May";
			break;
		case 6:
			strMonth = "Jun";
			break;
		case 7:
			strMonth = "Jul";
			break;
		case 8:
			strMonth = "Aug";
			break;
		case 9:
			strMonth = "Sept";
			break;
		case 10:
			strMonth = "Oct";
			break;
		case 11:
			strMonth = "Nov";
			break;
		case 12:
			strMonth = "Dec";
			break;
		}
		String strHour = "";
		if (hour < 10) {
			strHour = "0" + hour;
		} else {
			strHour = String.valueOf(hour);
		}
		String strMinute = "";
		if (minute < 10) {
			strMinute = "0" + minute;
		} else {
			strMinute = String.valueOf(minute);
		}
		String strSecond = "";
		if (second < 10) {
			strSecond = "0" + second;
		} else {
			strSecond = String.valueOf(second);
		}
		return strWeek + "," + day + " " + strMonth + year + " " + strHour
				+ ":" + strMinute + ":" + strSecond + "  UTC";
	}

	@Override
	public void getDevicePrams(long UserID, long nType, String param) {
		// TODO Auto-generated method stub
		if (userid == UserID && nType == 0X2016) {
			try {
				JSONObject obj = new JSONObject(param);
				dateModel.setNow(obj.getInt("now"));
				dateModel.setTz(obj.getInt("timezone"));
				dateModel.setNtp_enable(obj.getInt("ntp_enable"));
				dateModel.setNtp_ser(obj.getString("ntp_svr"));
				freshHandler.sendEmptyMessage(0);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

}
