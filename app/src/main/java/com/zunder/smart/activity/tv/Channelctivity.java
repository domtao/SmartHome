package com.zunder.smart.activity.tv;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.popu.dialog.ActionViewWindow;
import com.zunder.smart.activity.sub.BaseActivity;
import com.zunder.smart.activity.sub.RedFraAddActivity;
import com.zunder.smart.activity.sub.set.DeviceTimerActivity;
import com.zunder.smart.activity.sub.set.ElectricActivity;
import com.zunder.smart.activity.sub.set.MoreActivity;
import com.zunder.smart.adapter.ChannelAdapter;
import com.zunder.smart.adapter.RedFraAdapter;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.dao.impl.factory.RedInfraFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.DialogAlert.OnSureListener;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.RedInfra;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.utils.ListNumBer;

import java.util.List;

public class Channelctivity extends BaseActivity implements OnClickListener {
	private Activity mContext;
	private TextView backTxt;
	private TextView editeTxt;
	private TextView titleTxt;
	static String deviceID = "000000";
	public static int id;
	private static String infraId;
	private static String dname;
	private RightMenu rightButtonMenuAlert;
	private SwipeMenuRecyclerView gridView;
	public boolean editeFla = false;
	ChannelAdapter adapter;
	public List<Device> deviceList;
	Device deviceParams = null;

	public static void startActivity(Activity activity, int _id) {
		id = _id;
		Intent intent = new Intent(activity, Channelctivity.class);
		activity.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel);
		mContext = this;
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		TcpSender.setElectricListeener(this);
		TcpSender.setDeviceStateListener(this);
		deviceParams = DeviceFactory.getInstance().getDevicesById(id);
		deviceID = deviceParams.getDeviceID();
		infraId = deviceParams.getDeviceID();
		dname = deviceParams.getDeviceName();
		findView();
		setAdapter();
		//SendCMD.getInstance().sendCMD(255, "1",deviceParams);
	}

	private void findView() {

		backTxt = (TextView) findViewById(R.id.backTxt);
		titleTxt = (TextView) findViewById(R.id.titleTxt);
		editeTxt = (TextView) findViewById(R.id.editeTxt);
		gridView = (SwipeMenuRecyclerView) findViewById(R.id.redFraGrid);
		gridView.setLayoutManager(new GridLayoutManager(mContext, 3));// 布局管理器。
		gridView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		gridView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
//		gridView.setSwipeMenuCreator(swipeMenuCreator);
		// 设置菜单Item点击监听。
//		gridView.setSwipeMenuItemClickListener(menuItemClickListener);
		gridView.setLongPressDragEnabled(true);// 开启拖拽，就这么简单一句话。
		gridView.setOnItemMoveListener(onItemMoveListener);// 监听拖拽，更新UI。
		titleTxt.setText(deviceParams.getRoomName()+dname);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		setAdapter();
		initRightButtonMenuAlert();
	}

	public void setAdapter() {
		deviceList = DeviceFactory.getInstance().getDeviceByAction(ChannelAddActivity.roomId,19,0,0);
		adapter = new ChannelAdapter(mContext, deviceList);
		adapter.setOnItemClickListener(onItemClickListener);
		gridView.setAdapter(adapter);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}
	private com.zunder.smart.listener.OnItemClickListener onItemClickListener = new com.zunder.smart.listener.OnItemClickListener() {
		@Override
		public void onItemClick(final int position) {
			Device model=deviceList.get(position);
			SendCMD.getInstance().sendCMD(250, model.getRoomName()+model.getDeviceName() ,model);
//			deviceParams.setState(0);
		}
	};

	private OnItemMoveListener onItemMoveListener = new OnItemMoveListener() {
		@Override
		public boolean onItemMove(int fromPosition, int toPosition) {
			return true;
		}

		@Override
		public void onItemDismiss(int position) {
			// 当Item被滑动删除掉的时候，在这里是无效的，因为这里没有启用这个功能。
			// 使用Menu时就不用使用这个侧滑删除啦，两个是冲突的。
		}
	};


	private void initRightButtonMenuAlert() {

		rightButtonMenuAlert = new RightMenu(mContext, R.array.right_channel,
					R.drawable.right_menu_images);
		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {

			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub
				switch (position) {
					case 0:
						int result = RedInfraFactory.getInstance().getInfraKey(infraId);
						if (result != -1) {
							Intent intent = new Intent(mContext,
									ChannelAddActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("Edite", "Add");
							bundle.putInt("fatherId", id);
							bundle.putString("did", infraId);
							bundle.putInt("infraKey", result);
							intent.putExtras(bundle);
							startActivityForResult(intent, 100);
						}
						break;
					case 1:
						DeviceTimerActivity.startActivity(mContext,deviceParams.getId());
						break;
					case 2:
//						MoreActivity.startActivity(mContext,deviceParams.getId());
//						break;
//					case 3:
						final ActionViewWindow actionViewWindow = new ActionViewWindow(mContext, "频道发射间隔时间", ListNumBer.getMinit60(), 0);
						actionViewWindow.setAlertViewOnCListener(new ActionViewWindow.AlertViewOnCListener() {
							@Override
							public void onItem(int pos, String ItemName) {
								String cmd= AppTools.toHex(pos);
								SendCMD.getInstance().sendCMD(254, "0A:10:06:"+cmd ,deviceParams);
								actionViewWindow.dismiss();
							}

							@Override
							public void cancle() {

							}
						});
						actionViewWindow.show();
						break;
				}
				rightButtonMenuAlert.dismiss();
			}
		});

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.backTxt:
				finish();
				break;
			case R.id.editeTxt:
				rightButtonMenuAlert.show(editeTxt);
				break;
			default:
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		editeFla = false;
		setAdapter();
		initRightButtonMenuAlert();
	}
	private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
		@Override
		public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
			int hSize = getResources().getDimensionPixelSize(R.dimen.item_height110);
			int wSize = getResources().getDimensionPixelSize(R.dimen.item_width);
			SwipeMenuItem delItem = new SwipeMenuItem(mContext)
					.setBackgroundDrawable(R.color.red)
					.setText(getString(R.string.delete))
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(delItem);// 添加一个按钮到右侧菜单。
			SwipeMenuItem wechatItem = new SwipeMenuItem(mContext)
					.setBackgroundDrawable(R.color.green)
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
				switch (menuPosition){
					case 0:
						DialogAlert alert = new DialogAlert(mContext);
						alert.init(deviceList.get(adapterPosition).getDeviceName(),
								mContext.getString(R.string.isDelRedFra));
						alert.setSureListener(new OnSureListener() {

							@Override
							public void onSure() {
								// TODO Auto-generated method stub
								int result = MyApplication.getInstance()
										.getWidgetDataBase()
										.deleteDevice(deviceList.get(adapterPosition).getId());
								if (result > 0) {
									ToastUtils.ShowSuccess(mContext,
											mContext.getString(R.string.deleteSuccess),
											Toast.LENGTH_SHORT,true);
									deviceList.remove(adapterPosition);
									adapter.notifyDataSetChanged();
									DeviceFactory.getInstance().clearList();
								}
							}
							@Override
							public void onCancle() {
								// TODO Auto-generated method stub
							}
						});
						alert.show();
						break;
					case 1:
						Intent intent = new Intent(mContext,
								ChannelAddActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("Edite", "Edite");
						bundle.putInt("id", deviceList.get(adapterPosition).getId());
						intent.putExtras(bundle);
						startActivityForResult(intent, 100);
						break;
				}
			}
		}
	};
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
				case 0:
					break;
				default:
					break;
			}
		}
	};

	@Override
	public void receiveDeviceStatus(String cmd) {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(0);
	}
}
