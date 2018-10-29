package com.zunder.smart.activity.mode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zunder.smart.R;
import com.zunder.smart.adapter.ModeTimerAdapter;
import com.zunder.smart.dialog.ButtonAlert;
import com.zunder.smart.dialog.MutilCheckAlert;
import com.zunder.smart.listener.TimerListener;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.DeviceTimer;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.utils.ListNumBer;
import com.zunder.smart.view.ListViewDecoration;
import com.zunder.smart.roll.WheelView;
import java.util.ArrayList;
import java.util.List;

public class ModeTimerActivity extends Activity implements OnClickListener,
		TimerListener {
	private static ModeTimerAdapter adapter;
	public static ModeTimerActivity activity;
	private TextView editeTxt, backTxt, weekTxt, titleTxt;
	private SwipeMenuRecyclerView timerList;
	public static List<DeviceTimer> list;
	static String type = "00";
	static String senceCode = "00";
	static String deviceID = "000000";
	static String dio = "0";
	static String deviceName = "";
	int id;
	WheelView hourView, timeView;
	private List<String> hourList, mnitsList;
	ImageButton set_week;
	LinearLayout setLayout;
	static Device device = null;
	String hourStr = "", minutStr = "";


	public static void startActivity(Activity activity, Device _device) {
		Intent intent = new Intent(activity, ModeTimerActivity.class);
		device = _device;
		activity.startActivityForResult(intent, 100);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mode_timer);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		activity = this;
		TcpSender.setTimerListener(this);
		list = new ArrayList<DeviceTimer>();
		backTxt = (TextView) findViewById(R.id.backTxt);
		titleTxt = (TextView) findViewById(R.id.titleMsg);
		timerList = (SwipeMenuRecyclerView) findViewById(R.id.timerList);
		timerList.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
		timerList.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		timerList.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		timerList.addItemDecoration(new ListViewDecoration());// 添加分割线。
		// 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
		// 设置菜单创建器。
		timerList.setSwipeMenuCreator(swipeMenuCreator);
		// 设置菜单Item点击监听。
		timerList.setSwipeMenuItemClickListener(menuItemClickListener);
		adapter = new ModeTimerAdapter(list);
		timerList.setAdapter(adapter);


		editeTxt = (TextView) findViewById(R.id.editeTxt);
		set_week = (ImageButton) findViewById(R.id.set_week);
		hourView = (WheelView) findViewById(R.id.hourList);
		timeView = (WheelView) findViewById(R.id.timeList);
		weekTxt = (TextView) findViewById(R.id.weekTxt);
		setLayout = (LinearLayout) findViewById(R.id.setLayout);
		editeTxt.setOnClickListener(this);
		backTxt.setOnClickListener(this);
		set_week.setOnClickListener(this);


		if (device != null) {
			type = device.getProductsCode();
			deviceID = device.getDeviceID();
			dio = device.getDeviceIO();
			deviceName = device.getDeviceName();
		}
		SendCMD.getInstance().sendCmd(249, getString(R.string.book_search), device);
		hourView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

			@Override
			public void onSelected(int selectedIndex, String item) {
				hourStr = item;

			}
		});
		timeView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

			@Override
			public void onSelected(int selectedIndex, String item) {
				minutStr = item;
			}
		});
		titleTxt.setText(deviceName +getString(R.string.book_tine));
		wheelView();
	}

	public static void deletTimer(String cmdStr) {
		SendCMD.getInstance().sendCmd(248, cmdStr, device);
		SendCMD.getInstance().sendCmd(249,activity.getString(R.string.book_search), device);
	}

	public void wheelView() {

		String[] time = AppTools.getTime().split(":");
		hourList = ListNumBer.getHour();
		if (hourList.size() > 0) {
			hourView.setItems(hourList);
			hourView.setSeletion(ListNumBer.getIndex(hourList, time[0]));
			hourStr = time[0];
		}
		mnitsList = ListNumBer.getMinit();
		if (mnitsList.size() > 0) {
			timeView.setItems(mnitsList);
			timeView.setSeletion(ListNumBer.getIndex(mnitsList, time[1]));
			minutStr = time[1];
		}
	}

	public void timerCode(String code) {
		int type_window = 12;
		if (code.substring(12, 18).equals("FFFFF0")) {// 情景预约

			if (device == null)
				return;
			DeviceTimer deviceTimer = new DeviceTimer();
			deviceTimer.setTimer(code.substring(8, 10) + ":"
					+ code.substring(10, 12));
			deviceTimer.setNumber(code.substring(4, 6));
			boolean retAck = true;
			int senceId = Integer.valueOf(code.substring(18, 20), 16);
			;
			if (senceId != device.getSceneId()) {
				retAck = false;
			}

			if (!code.substring(17, 18).equals(dio)) {
				retAck = false;
			}

			if (retAck) {

				String[] arr =getString(R.string.weekeen).split(",");
				int week = Integer.valueOf(code.substring(6, 8), 16);

				String weekStr = "";

				if ((week & 0x80) > 0) {
					weekStr = getString(R.string.actionOne);
				}
				if ((week & 0x7f) == 0x7f) {
					weekStr = getString(R.string.everyday);

				} else {
					for (int i = 0; i < arr.length - 1; i++) {
						if ((week & (1 << i)) > 0) {
							weekStr += arr[i] + " ";
						}
					}
				}
				deviceTimer.setCycle(weekStr);
				deviceTimer.setWeek(toHex1602(week));

				add(deviceTimer);
				handler.sendEmptyMessage(0);
			}
		}

	}

	private static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				adapter.notifyDataSetChanged();
			}
		};
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			TcpSender.setTimerListener(null);
			Intent resultIntent = new Intent();
			this.setResult(100, resultIntent);
			this.finish();
			break;
		case R.id.editeTxt:
			if (editeTxt.getText().equals(getString(R.string.add))) {
				setLayout.setVisibility(View.VISIBLE);
				String[] time = AppTools.getTime().split(":");
				hourView.setSeletion(ListNumBer.getIndex(hourList, time[0]));

				timeView.setSeletion(ListNumBer.getIndex(mnitsList, time[1]));
				hourStr = time[0];
				minutStr = time[1];
				editeTxt.setText(getString(R.string.submit));
			} else {
				editeTxt.setText(getString(R.string.add));
				setLayout.setVisibility(View.GONE);
				if (hourStr.equals("") || minutStr.equals("")) {
					Toast.makeText(activity, getString(R.string.time_null), Toast.LENGTH_SHORT)
							.show();

					return;
				} else if (weekTxt.getText().toString().equals(getString(R.string.please_select))) {
					Toast.makeText(activity, getString(R.string.cycle_null), Toast.LENGTH_SHORT)
							.show();
					return;
				}

				SendCMD.getInstance().sendCmd(
						248,
						hourStr + ":" + minutStr + ":" + toHex1602(timerValue)
								+ ":00:3", device);

				SendCMD.getInstance().sendCmd(249, getString(R.string.book_search), device);

			}
			break;
		case R.id.set_week:
			AskTimeSch();
			break;
		default:
			break;
		}

	}

	public static void add(DeviceTimer _timer) {
		Boolean flag = true;
		if (list != null && list.size() > 0) {
			for (DeviceTimer timer : list) {
				if (timer.getTimer().endsWith(_timer.getTimer())) {
					flag = false;
					break;
				}
			}
		}

		if (flag == true) {
			list.add(_timer);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == event.KEYCODE_BACK) {
			TcpSender.setTimerListener(null);
			activity = null;
			Intent resultIntent = new Intent();
			this.setResult(100, resultIntent);
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	public String toHex1602(int a) {
		String ss = Integer.toHexString(a).toUpperCase();
		if (ss.length() < 2) {
			ss = "0" + ss;
		}
		return ss;
	}


	int timerValue = 0;
	private void AskTimeSch() {
		timerValue = 0;
		final String items[] = (String[]) activity.getResources()
				.getStringArray(R.array.weekens);
		final boolean[] selected = new boolean[items.length];
		for (int i = 0; i < selected.length; i++) {
			selected[i] = false;
		}

		final MutilCheckAlert mutilCheckAlert=new MutilCheckAlert(activity);
		mutilCheckAlert.setMultiChoiceItems(items, selected,
				new MutilCheckAlert.OnMultiChoiceClickListener() {
					@Override
					public void onClick(int which,
										boolean isChecked) {
						// TODO Auto-generated method stub
						selected[which] = isChecked;
					}
				});
		mutilCheckAlert.setOnSureListener(new MutilCheckAlert.OnSureListener() {
			@Override
			public void onCancle() {

			}
			@Override
			public void onSure() {
				String tempWeek = "";
				for (int i = 1; i < selected.length; i++) {
					if (selected[i]) {
						timerValue += (1 << (i - 1));
						tempWeek += items[i] + "-";
					}
				}

				if (selected[0]) {
					timerValue= 128;
					weekTxt.setText(items[0]);
				} else {
					if (tempWeek == "") {
						return;
					}
					weekTxt.setText(tempWeek.substring(0,
							tempWeek.lastIndexOf("-")));
				}
				mutilCheckAlert.dismiss();
			}
		});
		mutilCheckAlert.show();

	}
	private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
		@Override
		public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
			int hSize = getResources().getDimensionPixelSize(R.dimen.item_height);
			int wSize = getResources().getDimensionPixelSize(R.dimen.item_width);

			SwipeMenuItem addItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.red)
					.setText(getString(R.string.delete))
					.setTextColor(Color.WHITE)
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(addItem); // 添加一个按钮到右侧菜单。
		}
	};


	/**
	 * 菜单点击监听。
	 */
	private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
		@Override
		public void onItemClick(Closeable closeable, final int adapterPosition, int menuPosition, int direction) {
			closeable.smoothCloseMenu();// 关闭被点击的菜单。
			if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
				final ButtonAlert alert = new ButtonAlert(activity);
				alert.setTitle(R.mipmap.sys_help,"提示",
						activity.getString(R.string.is_Del));
				alert.setButton(getString(R.string.sure), "", getString(R.string.cancle));
				alert.setVisible(View.VISIBLE, View.GONE, View.VISIBLE);
				alert.setOnSureListener(new ButtonAlert.OnSureListener() {

					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						String cmdStr = list.get(adapterPosition).getTimer() + ":"
								+ list.get(adapterPosition).getWeek() + ":"
								+ list.get(adapterPosition).getEvent() + ":4";
						ModeTimerActivity.deletTimer(cmdStr);
						list.remove(adapterPosition);
					adapter.notifyDataSetChanged();
						alert.dismiss();
					}

					@Override
					public void onSearch() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub
						// alert.dismiss();
					}
				});
				alert.show();
			}
		}
	};

}