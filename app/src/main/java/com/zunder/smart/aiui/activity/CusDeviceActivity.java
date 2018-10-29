package com.zunder.smart.aiui.activity;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.adapter.DeviceAdapter;
import com.zunder.smart.aiui.adapter.CusAdapter;
import com.zunder.smart.aiui.info.SubscribeInfo;
import com.zunder.smart.broadcast.ReceiverBroadcast;
import com.zunder.smart.custom.view.SuperListView;
import com.zunder.smart.dao.impl.factory.CloundDeviceFactory;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dialog.ButtonAlert;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.TipAlert;
import com.zunder.smart.activity.device.DeviceFragment;
import com.zunder.smart.dialog.WarnDialog;
import com.zunder.smart.listener.GetDeviceListener;
import com.zunder.smart.listview.SwipeListView;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.Mode;
import com.zunder.smart.model.VoiceInfo;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.socket.info.ISocketCode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class CusDeviceActivity extends Activity implements OnClickListener,
		GetDeviceListener {
	private SwipeRefreshLayout freshlayout;
	private SwipeListView listView;
	CusAdapter adapter;
	CusDeviceActivity activity;
	TextView freshDevice;
	TextView addDevice;
	private RightMenu rightButtonMenuAlert;
	WarnDialog warnDialog = null;

	public static void startActivity(Activity activity) {
		Intent intent = new Intent(activity, CusDeviceActivity.class);
		activity.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ReceiverBroadcast.setGetDeviceListener(this);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.aiui_cusdevice_list);
		activity = this;
		initview();
		CloundDeviceFactory.list.clear();
		String result = ISocketCode.setGetDevice("001",
				AiuiMainActivity.deviceID);
		MainActivity.getInstance().sendCode( result);
		showDialog();
	}
	public void showDialog(){
		if (warnDialog == null) {
			warnDialog = new WarnDialog(activity, getString(R.string.search));
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
		listView = (SwipeListView) findViewById(R.id.deviceList);
		freshDevice = (TextView) findViewById(R.id.backTxt);
		addDevice = (TextView) findViewById(R.id.addDevice);
		freshDevice.setOnClickListener(this);
		addDevice.setOnClickListener(this);
		if (AiuiMainActivity.gateWay.getUserName().equals("admin")) {
//			initRightButtonMenuAlert();
		} else {
			addDevice.setVisibility(View.GONE);
		}
		freshlayout = (SwipeRefreshLayout) findViewById(R.id.freshlayout);
		freshlayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW);
		freshlayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#BBFFFF"));
		freshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
			@Override
			public void onRefresh() {
				CloundDeviceFactory.list.clear();
				String result = ISocketCode.setGetDevice("001",
						AiuiMainActivity.deviceID);
				MainActivity.getInstance().sendCode(result);
				if (freshlayout.isRefreshing()) {
					freshlayout.setRefreshing(false);
				}
			}
		});
	}

	private void initRightButtonMenuAlert() {

		rightButtonMenuAlert = new RightMenu(activity, R.array.devicelists,
				R.drawable.devicelist_images);
		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {
			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub

				switch (position) {
				case 0:
//					AddDeviceActivity.startActivity(activity, "Edite",
//							device);
					break;
				case 1:
					CloundDeviceFactory.list.clear();
					String result = ISocketCode.setGetDevice("001",
							AiuiMainActivity.deviceID);
					MainActivity.getInstance().sendCode(result);
					showDialog();
					break;
				case 2:
					final ButtonAlert buttonAlert = new ButtonAlert(activity);
					buttonAlert.setTitle(R.mipmap.info_systemset,getString(R.string.tip), getString(R.string.downdevice));
					buttonAlert.setButton(getString(R.string.cancle), getString(R.string.sure), "");
					buttonAlert.setVisible(View.VISIBLE, View.VISIBLE,
							View.GONE);
					buttonAlert
							.setOnSureListener(new ButtonAlert.OnSureListener() {
								@Override
								public void onSure() {
									// TODO Auto-generated method stub
									buttonAlert.dismiss();
								}

								@Override
								public void onSearch() {
									// TODO Auto-generated method stub

									buttonAlert.dismiss();
								}

								@Override
								public void onCancle() {
									// TODO Auto-generated method stub
								}
							});
					buttonAlert.show();

					break;

				}
				rightButtonMenuAlert.dismiss();
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		adapter = new CusAdapter(activity,
				CloundDeviceFactory.getDeviceList());
		listView.setAdapter(adapter);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			back();
			break;
		case R.id.addDevice:
//			rightButtonMenuAlert.show(addDevice);
			final AlertViewWindow alertViewWindow=new AlertViewWindow(activity,"设备列表", DeviceFactory.getInstance().getCusDeviceName(),null,0);
			alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
				@Override
				public void onItem(int pos, String itemName) {
					Device device=DeviceFactory.getInstance().getDevicesByName(itemName);
					if(device==null){
						ToastUtils.ShowError(activity,"设备不存在",Toast.LENGTH_SHORT,true);
					}
					else{
						AddDeviceActivity.startActivity(activity, "Add",device);
					}
					alertViewWindow.dismiss();
				}
			});
			alertViewWindow.show();
			break;
		default:
			break;
		}
	}

	public void back() {
		DeviceAdapter.edite = 0;
		finish();
	}

	@Override
	public void getDevice(Device device) {
		// TODO Auto-generated method stub
		Message message = handler.obtainMessage();
		message.what = 1;
		message.obj = device;
		handler.sendMessage(message);
	}

	@Override
	public void getMode(Mode mode){

	}

	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			switch (msg.what) {
				case -2:
					int index=Integer.parseInt(msg.obj.toString());
					warnDialog.setMessage(getString(R.string.getdevice)+" "+(10-index));
					break;
				case -1:
					TipAlert errAlert = new TipAlert(activity,
							getString(R.string.tip),getString(R.string.pass));
					errAlert.show();
					break;
			case 0:
				if ((warnDialog != null) && warnDialog.isShowing()) {
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
				CloundDeviceFactory.add((Device) msg.obj);

				adapter = new CusAdapter(activity,
						CloundDeviceFactory.getDeviceList());
				listView.setAdapter(adapter);
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void getMessage(String info) {
		// TODO Auto-generated method stub
		Message message = handler.obtainMessage();
		message.what = 0;
		message.obj = info;
		handler.sendMessage(message);
	}

	@Override
	public void getVoice(VoiceInfo voiceInfo) {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == event.KEYCODE_BACK) {
			back();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void delete(final int adapterPosition){

		final Device	device=adapter.getItems().get(adapterPosition);
		DialogAlert alert = new DialogAlert(activity);

		alert.init(device.getDeviceName(),
				activity.getString(R.string.isDelDevice));
		alert.setSureListener(new DialogAlert.OnSureListener() {
			@Override
			public void onSure() {
				// TODO Auto-generated method stub

				String resultStr = ISocketCode.setDelDevice(
						device.getId() + ";" + device.getDeviceName(),
						AiuiMainActivity.deviceID);
				MainActivity.getInstance().sendCode(resultStr);

				ToastUtils.ShowSuccess(activity,
						activity.getString(R.string.deleteSuccess),
						Toast.LENGTH_SHORT,true);

				adapter.getItems().remove(adapterPosition);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onCancle() {
				// TODO Auto-generated method stub
			}
		});
		alert.show();
	}
	public void edite(int adapterPosition){

		Device device=adapter.getItems().get(adapterPosition);
		AddDeviceActivity.startActivity(activity, "Edite",
				device);
	}
	public void getModeList(String  param){}

	@Override
	public void getSubscribe(SubscribeInfo subscribeInfo) {
	}

}