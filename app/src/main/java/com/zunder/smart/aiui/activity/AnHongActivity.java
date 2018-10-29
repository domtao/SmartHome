package com.zunder.smart.aiui.activity;

import java.util.ArrayList;

import java.util.List;

import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.adapter.DeviceAdapter;
import com.zunder.smart.aiui.adapter.AnHongAdapter;
import com.zunder.smart.aiui.info.AnHong;
import com.zunder.smart.broadcast.ReceiverBroadcast;
import com.zunder.smart.dialog.SecurityAlert;
import com.zunder.smart.dialog.TipAlert;
import com.zunder.smart.dialog.WarnDialog;
import com.zunder.smart.listener.AnHongListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.socket.info.ISocketCode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class AnHongActivity extends Activity implements OnClickListener,
		AnHongListener {
	private ListView listView;
	AnHongAdapter adapter;
	AnHongActivity activity;
	TextView freshDevice;
	TextView addDevice;
	private RightMenu rightButtonMenuAlert;
	List<AnHong> list = new ArrayList<AnHong>(32);

	public static void startActivity(Activity activity) {
		Intent intent = new Intent(activity, AnHongActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ReceiverBroadcast.setAnHongListener(this);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.aiui_anhong_list);
		activity = this;
		initview();
		String result = ISocketCode.setGetAnHong("getAnHong",
				AiuiMainActivity.deviceID);
		MainActivity.getInstance().sendCode( result);
		showDialog();
	}
	WarnDialog warnDialog = null;
	public void showDialog(){
		if (warnDialog == null) {
			warnDialog = new WarnDialog(activity, getString(R.string.searching));
			warnDialog.setMessage(getString(R.string.get_anhong_info)+" 10");
			warnDialog.setSureListener(new WarnDialog.OnSureListener() {
				@Override
				public void onCancle() {
					searchflag = false;
					warnDialog.dismiss();
				}
			});
		}

		warnDialog.show();
		startTime();
	}

	private boolean searchflag = false;
	private int startCount = 0;

	private void startTime() {
		startCount=0;
		searchflag=true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (searchflag) {
					try {
						Thread.sleep(1000);
						startCount++;
						if (startCount >= 10) {
							searchflag = false;
							if ((warnDialog != null) && warnDialog.isShowing()) {
								warnDialog.dismiss();
								handler.sendEmptyMessage(-1);
							}
						}else{
							Message message = handler.obtainMessage();
							message.what = -2;
							message.obj = startCount;
							handler.sendMessage(message);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	private void initview() {
		listView = (ListView) findViewById(R.id.anhongList);
		freshDevice = (TextView) findViewById(R.id.backTxt);
		addDevice = (TextView) findViewById(R.id.addDevice);
		freshDevice.setOnClickListener(this);
		addDevice.setOnClickListener(this);
		adapter = new AnHongAdapter(activity, list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				final SecurityAlert alert = new SecurityAlert(activity);
				alert.setTitle(R.mipmap.info_systemset, "安防提示信息ID:0000"
						+ (list.get(position).getId() + 1));
				alert.setText(list.get(position).getMsgName() + "",
						list.get(position).getMsgInfo());

				alert.setHint(getString(R.string.input_name1), getString(R.string.tip_info));
				alert.setVisible(View.VISIBLE, View.GONE, View.VISIBLE);
				alert.setOnSureListener(new SecurityAlert.OnSureListener() {
					@Override
					public void onSure(String editName, String editValue) {
						String fileName = editName;
						String cmd = list.get(position).getId() + ":"
								+ fileName + ":" + editValue;
						String result = ISocketCode.setUpdateAnHong(cmd,
								AiuiMainActivity.deviceID);
						MainActivity.getInstance().sendCode( result);
						list.get(position).setMsgName(editName);
						list.get(position).setMsgInfo(editValue);
						adapter.notifyDataSetChanged();
					}
					@Override
					public void onSearch() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub

					}
				});
				alert.show();
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			back();
			break;
		case R.id.addDevice:
			list.clear();
			String result = ISocketCode.setGetAnHong("getAnHong",
					AiuiMainActivity.deviceID);
			MainActivity.getInstance().sendCode(result);
			break;
		default:
			break;
		}

	}

	public void back() {
		DeviceAdapter.edite = 0;
		finish();
	}

	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			switch (msg.what) {
				case -2:
					int index=Integer.parseInt(msg.obj.toString());
					warnDialog.setMessage("正在获取安防信息"+(10-index));
					break;
				case -1:
					TipAlert errAlert = new TipAlert(activity,
							getString(R.string.tip),"获取数据超时");
					errAlert.show();
					break;
				case 0:
				if((warnDialog != null) && warnDialog.isShowing()) {
				searchflag = false;
				startCount=0;
				warnDialog.dismiss();
				}
				TipAlert alert = new TipAlert(activity,
						getString(R.string.tip), msg.obj.toString());
				alert.show();
				break;
			case 1:
				if ((warnDialog != null) && warnDialog.isShowing()) {
					searchflag = false;
					startCount=0;
					warnDialog.dismiss();
				}
				addAnHong((AnHong) msg.obj);
				adapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	};

	public void addAnHong(AnHong anHong) {
		int flag = 0;
		for (AnHong _anHong : list) {
			if (_anHong.getId() == anHong.getId()) {
				_anHong.setMsgName(anHong.getMsgName());
				flag = 1;
				break;
			}
		}
		if (flag == 0) {
			list.add(anHong);
		}
		sort(list);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == event.KEYCODE_BACK) {
			back();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void getAnHong(AnHong anHong) {
		// TODO Auto-generated method stub
		Message message = handler.obtainMessage();
		message.what = 1;
		message.obj = anHong;
		handler.sendMessage(message);
	}

	public void sort(List<AnHong> list) {
		int i, j, small;
		AnHong temp;
		int n = list.size();
		for (i = 0; i < n - 1; i++) {
			small = i;
			for (j = i + 1; j < n; j++) {
				if (list.get(j).getId() < list.get(small).getId())
					small = j;
				if (small != i) {
					temp = list.get(i);
					list.set(i, list.get(small));
					list.set(small, temp);
				}
			}
		}
	}
}