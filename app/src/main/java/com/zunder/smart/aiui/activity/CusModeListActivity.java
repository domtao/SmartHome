package com.zunder.smart.aiui.activity;

import java.util.ArrayList;

import java.util.List;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.aiui.adapter.CusModeListAdapter;
import com.zunder.smart.aiui.info.SubscribeInfo;
import com.zunder.smart.broadcast.ReceiverBroadcast;
import com.zunder.smart.dao.impl.factory.CloundDeviceFactory;
import com.zunder.smart.dao.impl.factory.CloundModeListFactory;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.TipAlert;
import com.zunder.smart.dialog.DialogAlert.OnSureListener;
import com.zunder.smart.listener.GetDeviceListener;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.Mode;
import com.zunder.smart.model.ModeList;
import com.zunder.smart.model.VoiceInfo;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.view.ListViewDecoration;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class CusModeListActivity extends Activity implements OnClickListener ,GetDeviceListener {
	private SwipeMenuRecyclerView listView;
	CusModeListAdapter adapter;
	CusModeListActivity activity;
	TextView freshDevice;
	TextView addDevice;
	private RightMenu rightButtonMenuAlert;
	private static String deviceName = "";
	List<ModeList> list = new ArrayList<ModeList>();
	Device deviceParams;
	public static int isSend = -1;
	private static Mode mode;

	public static void startActivity(Activity activity, Mode _mode) {
		mode=_mode;
		Intent intent = new Intent(activity, CusModeListActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.aiui_cusmode_list);
		activity = this;
		initview();

		ReceiverBroadcast.setGetDeviceListener(this);
		String result=ISocketCode.setGetModeList(mode.getId()+"",AiuiMainActivity.deviceID);
		MainActivity.getInstance().sendCode( result);

		if(CloundDeviceFactory.list.size()==0){
			 result = ISocketCode.setGetDevice("001",
					AiuiMainActivity.deviceID);
			MainActivity.getInstance().sendCode( result);
		}

	}

	private void initview() {
		freshDevice = (TextView) findViewById(R.id.backTxt);
		addDevice = (TextView) findViewById(R.id.addDevice);
		listView = (SwipeMenuRecyclerView) findViewById(R.id.deviceList);
		listView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
		listView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		listView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		listView.addItemDecoration(new ListViewDecoration());// 添加分割线。
		// 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
		// 设置菜单创建器。
        if(mode.getModeType().equals("C9")) {
            listView.setSwipeMenuCreator(swipeMenuCreator);
            // 设置菜单Item点击监听。
            listView.setSwipeMenuItemClickListener(menuItemClickListener);
			addDevice.setVisibility(View.VISIBLE);
        }else{
			addDevice.setVisibility(View.GONE);
		}


		freshDevice.setOnClickListener(this);
		addDevice.setOnClickListener(this);
	}

	private void initRightButtonMenuAlert() {

		rightButtonMenuAlert = new RightMenu(activity, R.array.cusMode,
				R.drawable.cus_mode_images);
		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {
			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					CusDeviceListActivity.startActivity(activity);
					break;
				case 1:
					isSend = -1;
					// if (list.size() > 0) {
					String deviceOrdered = "";
					for (int i = 0; i < list.size(); i++) {
						deviceOrdered += list.get(i) + ",";
					}
					deviceParams.setDeviceOrdered(deviceOrdered);
					String result = ISocketCode.setUpdateDevice(
							deviceParams.convertTostring() + ";" + deviceName,
							AiuiMainActivity.deviceID);
					MainActivity.getInstance().sendCode(result);
					// }
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
		adapter = new CusModeListAdapter(list);
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

			final AlertViewWindow alertViewWindow=new AlertViewWindow(activity,"设备列表", CloundDeviceFactory.getDeviceNames(),null,0);
			alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
				@Override
				public void onItem(int pos, String itemName) {
					Device device=CloundDeviceFactory.getDevice(itemName);
					if(device==null){
						ToastUtils.ShowError(activity,"设备不存在",Toast.LENGTH_SHORT,true);
					}
					else{
						ModeListActionActivity.startActivity(activity,0,"Add",mode,device.getId());
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
		if (isSend > 0) {
			DialogAlert alert = new DialogAlert(activity);
			alert.init(getString(R.string.tip), getString(R.string.isEditemode));
			alert.setSureListener(new OnSureListener() {

				@Override
				public void onSure() {
					// TODO Auto-generated method stub
					// if (list.size() > 0) {
					CusModeListActivity.isSend = -1;
					String deviceOrdered = "";
					for (int i = 0; i < list.size(); i++) {
						deviceOrdered += list.get(i) + ",";
					}
					deviceParams.setDeviceOrdered(deviceOrdered);
					String result = "";
					MainActivity.getInstance().sendCode(result);
					// }
					finish();
				}

				@Override
				public void onCancle() {
					// TODO Auto-generated method stub
					finish();
				}
			});
			alert.show();
		} else {
			finish();

		}
	}

	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			switch (msg.what) {
			case 0:
				TipAlert alert = new TipAlert(activity,
						getString(R.string.tip), msg.obj.toString());
				alert.show();
				break;
			case 1:
//				CloundDeviceFactory.add((Device) msg.obj);
//				adapter = new CusModeListAdapter(list);
//				listView.setAdapter(adapter);
				break;
			default:
				break;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == event.KEYCODE_BACK) {
			back();
		}
		return super.onKeyDown(keyCode, event);
	}
	private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
		@Override
		public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
			int hSize = getResources().getDimensionPixelSize(R.dimen.item_height55);
			int wSize = getResources().getDimensionPixelSize(R.dimen.item_width);

			SwipeMenuItem delItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.red)
					.setText(getString(R.string.delete))
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(delItem);// 添加一个按钮到右侧菜单。

			SwipeMenuItem wechatItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.orange)
					.setText(getString(R.string.edit))
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(wechatItem);// 添加一个按钮到右侧菜单。
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
				switch (menuPosition) {
					case 0:
					DialogAlert alert = new DialogAlert(activity);
					alert.init(getString(R.string.tip), getString(R.string.is_Del) + "[" + list.get(adapterPosition).getDeviceName()+list.get(adapterPosition).getModeAction()+list.get(adapterPosition).getModeFunction() + "]");
					alert.setSureListener(new OnSureListener() {
						@Override
						public void onSure() {
							// TODO Auto-generated method stub
							Toast.makeText(activity,
									activity.getString(R.string.deleteSuccess),
									Toast.LENGTH_SHORT).show();

							String result = ISocketCode.setDelModeList(list.get(adapterPosition).getId()+";"+list.get(adapterPosition).getDeviceName()+list.get(adapterPosition).getModeAction()+list.get(adapterPosition).getModeFunction(),
									AiuiMainActivity.deviceID);
							MainActivity.getInstance().sendCode( result);
							adapter.getItems().remove(adapterPosition);
							adapter.notifyDataSetChanged();
						}
						@Override
						public void onCancle() {
							// TODO Auto-generated method stub
						}
					});
					alert.show();
					break;
					case 1:

						ModeListActionActivity.startActivity(activity,adapter.getItems().get(adapterPosition).getId(),"Edite",mode,adapter.getItems().get(adapterPosition).getDeviceId());
						break;
				}
			}
		}
	};
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ReceiverBroadcast.setGetDeviceListener(null);
	}

	@Override
	public void getDevice(Device device) {
		CloundDeviceFactory.add((device));
	}
	@Override
	public void getMode(Mode mode) {

	}

	@Override
	public void getMessage(String info) {
		Message message = handler.obtainMessage();
		message.what = 0;
		message.obj = info;
		handler.sendMessage(message);
	}
	@Override
	public void getVoice(VoiceInfo voiceInfo) {
		}
	@Override
	public void getModeList(String params) {
		try {
			ModeList modeList= JSONHelper.parseObject(params,ModeList.class);
			CloundModeListFactory.getInstance().add(modeList);
			list=CloundModeListFactory.getInstance().getAll();
			adapter = new CusModeListAdapter(list);
			listView.setAdapter(adapter);
		}catch (Exception e){
		}
	}

	@Override
	public void getSubscribe(SubscribeInfo subscribeInfo) {

	}
}