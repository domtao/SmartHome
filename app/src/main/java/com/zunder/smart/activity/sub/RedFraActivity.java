package com.zunder.smart.activity.sub;

import java.util.Collections;
import java.util.List;

import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.sub.set.DeviceTimerActivity;
import com.zunder.smart.activity.sub.set.ElectricActivity;
import com.zunder.smart.activity.sub.set.MoreActivity;
import com.zunder.smart.adapter.RedFraAdapter;


import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.RedInfraFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.DialogAlert.OnSureListener;
import com.zunder.smart.gridview.DragGridView;
import com.zunder.smart.listener.OnItemEditeListener;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.RedInfra;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class RedFraActivity extends BaseActivity implements OnClickListener {
	private static Activity mContext;
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
	RedFraAdapter adapter;
	public List<RedInfra> listRedFra;
	Device deviceParams = null;

	public static void startActivity(Activity activity, int _id) {
		id = _id;
		Intent intent = new Intent(activity, RedFraActivity.class);
		activity.startActivityForResult(intent,100);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_redfra);
		mContext = this;
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		TcpSender.setElectricListeener(this);
		TcpSender.setDeviceStateListener(this);
		deviceParams = DeviceFactory.getInstance().getDevicesById(id);
		deviceID = deviceParams.getDeviceID();
		infraId = deviceParams.getDeviceID();
		dname = deviceParams.getRoomName()+ deviceParams.getDeviceName();
		findView();
		setAdapter();
	}

	private void findView() {
		backTxt = (TextView) findViewById(R.id.backTxt);
		titleTxt = (TextView) findViewById(R.id.titleTxt);
		editeTxt = (TextView) findViewById(R.id.editeTxt);
		gridView = (SwipeMenuRecyclerView) findViewById(R.id.redFraGrid);
		gridView.setLayoutManager(new GridLayoutManager(mContext, 3));// 布局管理器。
		gridView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		gridView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		gridView.setSwipeMenuCreator(swipeMenuCreator);
		// 设置菜单Item点击监听。
		gridView.setSwipeMenuItemClickListener(menuItemClickListener);
		gridView.setLongPressDragEnabled(true);// 开启拖拽，就这么简单一句话。
		gridView.setOnItemMoveListener(onItemMoveListener);// 监听拖拽，更新UI。
		titleTxt.setText(deviceParams.getRoomName()+dname);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		setAdapter();
		initRightButtonMenuAlert();
	}

	public void setAdapter() {
		listRedFra = RedInfraFactory.getInstance().getInfraById(id);
		adapter = new RedFraAdapter(mContext, listRedFra);
		adapter.setOnItemClickListener(onItemClickListener);
		adapter.setOnItemEditeListener(onItemEditeListener);
		gridView.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		TcpSender.setDeviceStateListener(this);
		SendCMD.getInstance().sendCMD(255,"1",deviceParams);
	}

	private com.zunder.smart.listener.OnItemClickListener onItemClickListener = new com.zunder.smart.listener.OnItemClickListener() {
		@Override
		public void onItemClick(final int position) {
			if(listRedFra.get(position).getInfraredKey()>0){
				SendCMD cmd = SendCMD.getInstance();
				cmd.sendModeForInfrared(0,listRedFra.get(position), 0);
			} else {
				if(deviceParams.getState() == 0 ) {
					//deviceParams.setState(1);
					SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname + getString(R.string.open_1),
							deviceParams);
				}else{
					SendCMD.getInstance().sendCMD(250, deviceParams.getRoomName()+dname +  getString(R.string.close_1),
							deviceParams);
					//deviceParams.setState(0);
				}

			}
		}
	};
	private OnItemEditeListener onItemEditeListener=new OnItemEditeListener() {
		@Override
		public void onItemEditeClick(int position) {
//			adapter.notifyDataSetChanged();
			gridView.openRightMenu(position);
		}
	};

	private OnItemMoveListener onItemMoveListener = new OnItemMoveListener() {
		@Override
		public boolean onItemMove(int fromPosition, int toPosition) {
//			RedInfra _from = listRedFra.get(fromPosition);
//			if (fromPosition < toPosition) {
//				listRedFra.add(toPosition + 1, _from);
////				listRedFra.remove(fromPosition);
//			} else if (fromPosition > toPosition) {
//				listRedFra.add(toPosition, _from);
//				listRedFra.remove(fromPosition + 1);
//			}
//			adapter.notifyItemMoved(fromPosition, toPosition);
//			MyApplication.getInstance().getWidgetDataBase()
//					.updateRedInfraSort(listRedFra);
//			RedInfraFactory.getInstance().clearList();

			Log.e("sort"," "+fromPosition+"__"+toPosition);
			if(fromPosition<listRedFra.size()&&toPosition<listRedFra.size()) {
				Collections.swap(listRedFra, fromPosition, toPosition);
				adapter.notifyDataSetChanged();
				MyApplication.getInstance().getWidgetDataBase()
						.updateRedInfraSort(listRedFra);
				RedInfraFactory.getInstance().clearList();
			}
			return true;
		}

		@Override
		public void onItemDismiss(int position) {
			// 当Item被滑动删除掉的时候，在这里是无效的，因为这里没有启用这个功能。
			// 使用Menu时就不用使用这个侧滑删除啦，两个是冲突的。
		}
	};


	private void initRightButtonMenuAlert() {

		if (RedInfraFactory.getInstance().isExite(0, infraId) == 0) {
			rightButtonMenuAlert = new RightMenu(mContext, R.array.right_redfra,
					R.drawable.right_menu_images);
		} else {
			rightButtonMenuAlert = new RightMenu(mContext, R.array.right_redfras,
					R.drawable.right_menu_images);
		}
		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {

			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub
				switch (position) {
					case 0:
						int result = RedInfraFactory.getInstance().getInfraKey(infraId);
						if (result != -1) {
							Intent intent = new Intent(mContext,
									RedFraAddActivity.class);
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
						if (RedInfraFactory.getInstance().isExite(0, infraId) == 0) {
							Intent intent = new Intent(mContext,
									RedFraAddActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("Edite", "Add");
							bundle.putInt("fatherId", id);
							bundle.putString("did", infraId);
							bundle.putInt("infraKey", 0);
							intent.putExtras(bundle);
							startActivityForResult(intent, 100);
						} else {
							ElectricActivity.startActivity(mContext,deviceParams.getId());
						}
						break;
					case 2:
						DeviceTimerActivity.startActivity(mContext,deviceParams.getId());
						break;
					case 3:
						MoreActivity.startActivity(mContext,deviceParams.getId());
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
	public void back(){
		Intent resultIntent = new Intent();
		setResult(0, resultIntent);
		finish();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.backTxt:
				back();
				break;
			case R.id.editeTxt:
				rightButtonMenuAlert.show(editeTxt);
				break;
			default:
				break;
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
			return false;
		}
		return super.onKeyDown(keyCode, event);
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
						alert.init(listRedFra.get(adapterPosition).getInfraredName(),
								mContext.getString(R.string.isDelRedFra));
						alert.setSureListener(new OnSureListener() {

							@Override
							public void onSure() {
								// TODO Auto-generated method stub
								int result = MyApplication.getInstance()
										.getWidgetDataBase()
										.deleteRedFra(listRedFra.get(adapterPosition).getId());
								if (result > 0) {
									Toast.makeText(mContext,
											mContext.getString(R.string.deleteSuccess),
											Toast.LENGTH_SHORT).show();
									listRedFra.remove(adapterPosition);
									adapter.notifyDataSetChanged();
									RedInfraFactory.getInstance().clearList();
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
								RedFraAddActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("Edite", "Edite");
						bundle.putInt("id", listRedFra.get(adapterPosition).getId());
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
					if (RedInfraFactory.getInstance().isExite(0, infraId) == 1) {
						listRedFra.get(0).setPowerState(deviceParams.getState());
						setAdapter();
					}
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
