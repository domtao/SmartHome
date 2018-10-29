package com.zunder.smart.aiui.activity;

import java.util.List;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.adapter.DeviceAdapter;
import com.zunder.smart.aiui.adapter.CuModeAdapter;
import com.zunder.smart.aiui.adapter.CusModeAdapter;
import com.zunder.smart.aiui.info.SubscribeInfo;
import com.zunder.smart.broadcast.ReceiverBroadcast;
import com.zunder.smart.dao.impl.factory.CloundDeviceFactory;
import com.zunder.smart.dao.impl.factory.CloundModeFactory;
import com.zunder.smart.dao.impl.factory.CloundModeListFactory;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.dao.impl.factory.ModeListFactory;
import com.zunder.smart.dialog.ButtonAlert;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.SecurityAlert;
import com.zunder.smart.dialog.TipAlert;
import com.zunder.smart.dialog.WarnDialog;
import com.zunder.smart.listener.GetDeviceListener;
import com.zunder.smart.listener.OnItemClickListener;
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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.TextView;

@SuppressLint("NewApi")
public class CusModeActivity extends Activity implements OnClickListener,
		GetDeviceListener {
	private SwipeMenuRecyclerView modeListView;
	CuModeAdapter cuModeAdapter;
	CusModeActivity activity;
	TextView freshDevice;
	TextView addDevice;
	List<Mode> modes;

	private RightMenu rightButtonMenuAlert;

	public static void startActivity(Activity activity) {
		Intent intent = new Intent(activity, CusModeActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ReceiverBroadcast.setGetDeviceListener(this);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.aiui_cusmode);
		activity = this;
		initview();
		CloundModeFactory.list.clear();
		String result = ISocketCode.setGetMode("getMode",
				AiuiMainActivity.deviceID);
		MainActivity.getInstance().sendCode(result);
		  result = ISocketCode.setGetDevice("002",
				AiuiMainActivity.deviceID);
		MainActivity.getInstance().sendCode(result);
		if (AiuiMainActivity.gateWay.getUserName().equals("admin")) {

			modeListView.setSwipeMenuCreator(swipeMenuCreator);
			modeListView.setSwipeMenuItemClickListener(menuModeItemClickListener);
		} else {
			addDevice.setVisibility(View.GONE);
		}
		showDialog();
	}
	WarnDialog warnDialog = null;
	public void showDialog(){

		if (warnDialog == null) {
			warnDialog = new WarnDialog(activity, getString(R.string.search));
			LayoutInflater warnDialog_inflater = getLayoutInflater();
			warnDialog.setMessage(getString(R.string.getmode)+" 10");

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
	private void initRightButtonMenuAlert() {
		rightButtonMenuAlert = new RightMenu(activity, R.array.cusModelist,
				R.drawable.devicelist_images);
		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {
			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub

				switch (position) {
				case 0:
					DeviceAdapter.edite = -1;
					AddDeviceActivity.startActivity(activity, "AddMode", null);
					break;
				case 1:
					CloundDeviceFactory.list.clear();
					CloundModeFactory.list.clear();
					String result = ISocketCode.setGetMode("getMode",
							AiuiMainActivity.deviceID);
					MainActivity.getInstance().sendCode(result);
					 result = ISocketCode.setGetDevice("002",
							AiuiMainActivity.deviceID);
					MainActivity.getInstance().sendCode(result);
					break;
				case 2:
					final ButtonAlert buttonAlert = new ButtonAlert(activity);
					buttonAlert.setTitle(R.mipmap.info_systemset,getString(R.string.tip), getString(R.string.downdevice));
					buttonAlert.setButton( getString(R.string.cancle),  getString(R.string.sure), "");
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

	private void initview() {

		// 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
		// 设置菜单创建器。
		modeListView= (SwipeMenuRecyclerView) findViewById(R.id.modeList);
		modeListView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
		modeListView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		modeListView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		modeListView.addItemDecoration(new ListViewDecoration());// 添加分割线。

		freshDevice = (TextView) findViewById(R.id.backTxt);
		addDevice = (TextView) findViewById(R.id.addDevice);
		freshDevice.setOnClickListener(this);
		addDevice.setOnClickListener(this);
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
		modes = CloundModeFactory.list;
		cuModeAdapter = new CuModeAdapter(activity, modes);
		modeListView.setAdapter(cuModeAdapter);
		cuModeAdapter.setOnItemClickListener(onItemCusClickListener);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			back();
			break;
		case R.id.addDevice:
			//
//			rightButtonMenuAlert.show(addDevice);
//			DeviceAdapter.edite = -1;
//			AddDeviceActivity.startActivity(activity, "AddMode", null);
			final AlertViewWindow alertViewWindow=new AlertViewWindow(activity,"集合列表", ModeFactory.getInstance().getModeName(),null,0);
			alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
				@Override
				public void onItem(int pos, String itemName) {
					Mode mode=ModeFactory.getInstance().getModeNyName(itemName);
					if(mode==null){
						ToastUtils.ShowError(activity,"模式不存在",Toast.LENGTH_SHORT,true);
					}
					else{
						if(CloundModeFactory.updateName(mode.getId(),mode.getModeName(),"")==0) {
							String deviceID = AiuiMainActivity.deviceID;
							String result = ISocketCode.setAddMode(JSONHelper.toJSON(mode),
									deviceID);
							MainActivity.getInstance().sendCode(result);
							List <ModeList> modeList = ModeListFactory.getInstance().getModeDevice(mode.getId());
							for (int i = 0; i < modeList.size(); i++) {
								deviceID = AiuiMainActivity.deviceID;
								Log.e("down", mode.convertTostring());
								result = ISocketCode.setAddModeList(JSONHelper.toJSON(modeList.get(i)),
										deviceID);
								MainActivity.getInstance().sendCode(result);
							}
							CloundModeFactory.add(mode);
							cuModeAdapter.notifyDataSetChanged();
						}else{
							ToastUtils.ShowError(activity,"模式已经存在",Toast.LENGTH_SHORT,true);
						}
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
        Message message = handler.obtainMessage();
        message.what = 2;
        message.obj = mode;
        handler.sendMessage(message);
	}
	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			switch (msg.what) {
				case -2:
					int index=Integer.parseInt(msg.obj.toString());
					warnDialog.setMessage(getString(R.string.getmode)+" "+(10-index));
					break;
				case -1:
					TipAlert errAlert = new TipAlert(activity,
							getString(R.string.tip), getString(R.string.pass));
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
				break;
                case 2:
                    if ((warnDialog != null) && warnDialog.isShowing()) {
                        searchflag = false;
                        startCount=0;
                        warnDialog.dismiss();
                    }
                    CloundModeFactory.add((Mode) msg.obj);
					modes = CloundModeFactory.list;
					cuModeAdapter = new CuModeAdapter(activity, modes);
					cuModeAdapter.setOnItemClickListener(onItemCusClickListener);
					modeListView.setAdapter(cuModeAdapter);
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

	private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
		@Override
		public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
			int hSize = getResources().getDimensionPixelSize(R.dimen.item_height55);
			int wSize = getResources().getDimensionPixelSize(R.dimen.item_width);

			SwipeMenuItem wechatItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.red)
					.setText( getString(R.string.delete))
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(wechatItem);// 添加一个按钮到右侧菜单。

			SwipeMenuItem closeItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.orange)
					.setText( getString(R.string.edit))
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(closeItem); // 添加一个按钮到右侧菜单。



		}
	};

	private OnItemClickListener onItemCusClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(int position) {
			CusModeListActivity.startActivity(activity,
					modes.get(position));
			CloundModeListFactory.getInstance().getAll().clear();
		}
	};

	private OnSwipeMenuItemClickListener menuModeItemClickListener = new OnSwipeMenuItemClickListener() {
		@Override
		public void onItemClick(Closeable closeable, final int adapterPosition, int menuPosition, int direction) {
			closeable.smoothCloseMenu();// 关闭被点击的菜单。
			if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
				final Mode device=cuModeAdapter.getItems().get(adapterPosition);
				switch (menuPosition){
					case 0:
						DialogAlert alert = new DialogAlert(activity);

						alert.init(device.getModeName(),
								activity.getString(R.string.isDelDevice));
						alert.setSureListener(new DialogAlert.OnSureListener() {
							@Override
							public void onSure() {
								// TODO Auto-generated method stub
								String resultStr = ISocketCode.setDelMode(
										device.getId() + ";" + device.getModeName(),
										AiuiMainActivity.deviceID);
								MainActivity.getInstance().sendCode(resultStr);
								Toast.makeText(activity,
										activity.getString(R.string.deleteSuccess),
										Toast.LENGTH_SHORT).show();
								cuModeAdapter.getItems().remove(adapterPosition);
								cuModeAdapter.notifyDataSetChanged();
							}
							@Override
							public void onCancle() {
								// TODO Auto-generated method stub
							}
						});
						alert.show();
						break;
					case 1:
						//AddDeviceActivity.startActivity(activity, "Edite",
							//	device.getDeviceName());
					final SecurityAlert alertE = new SecurityAlert(activity);
						alertE.setTitle(R.mipmap.info_systemset, getString(R.string.modeidte));
						alertE.setText(device.getModeName(),device.getModeNickName());
						alertE.setHint(getString(R.string.input_name1), getString(R.string.input_alia));
						alertE.setVisible(View.VISIBLE, View.GONE, View.VISIBLE);
						alertE.setOnSureListener(new SecurityAlert.OnSureListener() {
							@Override
							public void onSure(String editName, String editValue) {
								Mode mode = new Mode();
								mode.setId(device.getId());
								mode.setModeName(editName);
								mode.setModeImage(null);
								mode.setModeType(device.getModeType());
								mode.setModeCode(device.getModeCode());
								mode.setModeLoop(device.getModeLoop());
								mode.setCreationTime(device.getCreationTime());
								mode.setSeqencing(device.getSeqencing());
								mode.setModeNickName(editValue);
								mode.setStartTime(device.getStartTime());
								mode.setEndTime(device.getEndTime());
								if (CloundDeviceFactory.updateName(device.getId(),editName, device.getModeName()) == 0&&CloundModeFactory.updateName(device.getId(),editName, device.getModeName()) == 0) {
									String resultStr = ISocketCode.setAddMode(JSONHelper.toJSON(mode),
											AiuiMainActivity.deviceID);
									MainActivity.getInstance().sendCode(resultStr);
									CloundModeFactory.update(device.getModeName(),editName);
									alertE.dismiss();
									cuModeAdapter.notifyDataSetChanged();
								}else{
									ToastUtils.ShowError(activity, getString(R.string.namebeing), Toast.LENGTH_SHORT,true);
								}
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
						alertE.show();


						break;
				}	} else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
			}
		}
	};
	public void getModeList(String  param){}

	@Override
	public void getSubscribe(SubscribeInfo subscribeInfo) {

	}
}