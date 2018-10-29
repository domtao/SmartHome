/**

 * 
 */
package com.zunder.smart.activity.gateway;

import java.util.List;

import com.bluecam.api.SettingActivity;
import com.door.Utils.ToastUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.activity.main.TabMyActivity;
import com.zunder.smart.adapter.GateWayAdapter;
import com.zunder.smart.aiui.activity.AiuiMainActivity;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.gateway.GateWaySettingActivity;
import com.zunder.smart.gateway.GateWaySettingDoor;
import com.zunder.smart.listener.CameraHandleListener;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.popwindow.GateWayWindow;
import com.zunder.smart.remote.RemoteMainActivity;
import com.zunder.smart.service.GateWayService;
import com.zunder.smart.view.ListViewDecoration;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import hsl.p2pipcam.nativecaller.DeviceSDK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Administrator
 * 
 */
public class GateWayFragment extends Fragment implements CameraHandleListener,View.OnTouchListener {
	private SwipeRefreshLayout freshlayout;
	List<GateWay> list;
	public static boolean uploadCamera = false;
	String stata;
	GateWayAdapter adapter = null;
	TextView titleView;
	private SwipeMenuRecyclerView list_devices;
	TextView add_devices;
	private TextView refresh_btn;
	private Activity activity;
	private RightMenu rightButtonMenuAlert;
	public static void startActivity(Activity activity) {
		Intent intent = new Intent(activity, GateWayFragment.class);
		activity.startActivity(intent);
	}
	GateWayWindow ga = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_gateway, container,
				false);
		root.setOnTouchListener(this);
		activity =getActivity();
		freshlayout = (SwipeRefreshLayout) root.findViewById(R.id.freshlayout);
		freshlayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW);
		freshlayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#BBFFFF"));
		freshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
			@Override
			public void onRefresh() {
				MainActivity.getInstance().freshFindDevice();
				if (freshlayout.isRefreshing()) {
					freshlayout.setRefreshing(false);
				}

			}
		});
		titleView = (TextView) root.findViewById(R.id.title);
		refresh_btn = (TextView) root.findViewById(R.id.refresh_btn);
		list_devices = (SwipeMenuRecyclerView) root.findViewById(R.id.list_devices);

		refresh_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

//				MainActivity.getInstance().freshFindDevice();
				TabMyActivity.getInstance().hideFragMent(2);
			}
		});

		add_devices = (TextView) root.findViewById(R.id.editeTxt);
		add_devices.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			rightButtonMenuAlert.show(add_devices);
			}
		});

		initRightButtonMenuAlert();
		init();
		return  root;
	}
	public void init(){
		index=0;
		list = GateWayService.getInstance().getGatewayList();
		list_devices.setLayoutManager(new LinearLayoutManager(activity));// 布局管理器。
		list_devices.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		list_devices.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		list_devices.addItemDecoration(new ListViewDecoration());// 添加分割线。
		list_devices.setSwipeMenuCreator(swipeMenuCreator);
		// 设置菜单Item点击监听。
		list_devices.setSwipeMenuItemClickListener(menuItemClickListener);
		adapter = new GateWayAdapter(activity,list,GateWayService.mp);
		adapter.setOnItemClickListener(onItemClickListener);
		list_devices.setAdapter(adapter);
		if(list.size()>0){
			list_devices.setBackgroundResource(0);
		}else{
			list_devices.setBackgroundResource(R.mipmap.empty_gateway);
		}
	}
	@SuppressLint("ResourceAsColor")
	private void initRightButtonMenuAlert() {
		rightButtonMenuAlert = new RightMenu(activity, R.array.right_device,
				R.drawable.right_backup_images);
		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {

			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub
				switch (position) {
					case 0:
						TabMyActivity.getInstance().showFragMent(3);
					break;
					case 1:
						TabMyActivity.getInstance().showFragMent(4);
					break;
					case 2:
						TabMyActivity.getInstance().showFragMent(5);
						TabMyActivity.getInstance().gatewayAddFragment.setEdite("Add",null,3);
					break;
				}
				rightButtonMenuAlert.dismiss();
			}
		});
	}
	public void setting(int position) {
		Intent intent = new Intent(activity, GateWaySettingActivity.class);
		intent.putExtra("edit", "edit");
		intent.putExtra("Id", list.get(position).getId());
		startActivityForResult(intent, 100);
	}

	@Override
	public boolean changeList() {
		// TODO Auto-generated method stub
		list = GateWayService.getInstance().getGatewayList();;
		index=0;
		adapter = new GateWayAdapter(activity,list, GateWayService.mp);
		adapter.setOnItemClickListener(onItemClickListener);
		list_devices.setAdapter(adapter);
		if(list.size()>0){
			list_devices.setBackgroundResource(0);
		}else{
			list_devices.setBackgroundResource(R.mipmap.empty_gateway);
		}
		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
//		list = GateWayService.getInstance().getGatewayList();;
//		index=0;
//		adapter = new GateWayAdapter(activity,list, GateWayService.mp);
//		adapter.setOnItemClickListener(onItemClickListener);
//		list_devices.setAdapter(adapter);
	}

	int index=0;
	private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
		@Override
		public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
			int hSize = getResources().getDimensionPixelSize(R.dimen.item_gateway_height);
			int wSize = getResources().getDimensionPixelSize(R.dimen.item_width);
			if(list.get(index).getTypeId()==2) {
				SwipeMenuItem deleteItem = new SwipeMenuItem(activity)
						.setBackgroundDrawable(R.color.orange)
						.setText(getString(R.string.manger))
						.setWidth(wSize)
						.setHeight(hSize);
				swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧菜单。
			}
				SwipeMenuItem wechatItem = new SwipeMenuItem(activity)
						.setBackgroundDrawable(R.color.pink)
						.setText(getString(R.string.setting))
						.setWidth(wSize)
						.setHeight(hSize);
				swipeRightMenu.addMenuItem(wechatItem);// 添加一个按钮到右侧菜单。

			SwipeMenuItem closeItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.green)
					.setText(getString(R.string.edit))
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(closeItem); // 添加一个按钮到右侧菜单。
			SwipeMenuItem addItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.red)
					.setText(getString(R.string.delete))
					.setTextColor(Color.WHITE)
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(addItem); // 添加一个按钮到右侧菜单。
			index++;
		}
	};

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(int position) {

			if(list.size()<=0){
				list = GateWayService.list;
				index=0;
				list_devices.setSwipeMenuCreator(swipeMenuCreator);
				adapter = new GateWayAdapter(activity,list, GateWayService.mp);
				adapter.setOnItemClickListener(onItemClickListener);
				list_devices.setAdapter(adapter);
				return;
			}
			GateWay cb = list.get(position);
			String name = cb.getState();
			if(cb.getTypeId()<3){

				if (name.equals(activity.getString(R.string.gateWayLine))) {
					if (ga != null) {
						if (ga.isShow()) {
							ga.diss();
							return;
						}
						ga = null;
					}
					ga = new GateWayWindow(activity, list.get(position));
					ga.setAlertViewOnCListener(new GateWayWindow.AlertViewOnCListener() {
						@Override
						public void onItem() {
							adapter.notifyDataSetChanged();
						}

						@Override
						public void cancle() {

						}
					});
					ga.openPop();
					GateWayAdapter.userid = Long.parseLong(GateWayService.mp
							.get(cb.getGatewayID()));
				} else {
					ToastUtils.ShowError(activity, getString(R.string.device_no_line), Toast.LENGTH_SHORT, true);
				}
			}
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
				final GateWay gateWay=adapter.getItems().get(adapterPosition);
				switch (menuPosition){
					case 0:
						String name = gateWay.getState();
						if(gateWay.getTypeId()==2){
							if (name.equals(activity.getString(R.string.gateWayLine))) {
								AiuiMainActivity.deviceID = gateWay
										.getGatewayID();
								AiuiMainActivity.startActivity(activity);
							} else {
								ToastUtils.ShowError(activity,  getString(R.string.device_no_line), Toast.LENGTH_SHORT,true);
							}
						}	else  if(gateWay.getTypeId()==3){
						if (name.equals(activity.getString(R.string.gateWayLine))) {
							RemoteMainActivity.deviceID = gateWay
									.getGatewayID();
							RemoteMainActivity.startActivity(activity);
						} else {
							ToastUtils.ShowError(activity,  getString(R.string.device_no_line), Toast.LENGTH_SHORT,true);
						}
//						ToastUtils.ShowError(activity, "您当前没有管理权限", Toast.LENGTH_SHORT,true);
					}else if(gateWay.getTypeId()==1){
						if (name.equals(activity.getString(R.string.gateWayLine))) {
							if (gateWay.getUserName().equals("admin")) {
								TabMyActivity.getInstance().showFragMent(6);
//								GateWaySettingFragment.startActivity(activity, gateWay.getGatewayID());
								TabMyActivity.getInstance().gateWaySettingFragment.setInit(gateWay);
							} else {
								ToastUtils.ShowError(activity, getString(R.string.no_rol_info),
										Toast.LENGTH_SHORT, true);
							}
						} else {
							ToastUtils.ShowError(activity,  getString(R.string.device_no_line), Toast.LENGTH_SHORT,true);
						}
					}else if(gateWay.getTypeId()==200){
							if (name.equals(activity.getString(R.string.gateWayLine))) {
								if (gateWay.getTypeId()==3) {
									GateWaySettingDoor.startActivity(activity,gateWay);
								}else{
									GateWayAdapter.userid = Long
											.parseLong(GateWayService.mp.get(gateWay.getGatewayID()));
									if (gateWay.getUserName().equals("admin")) {
										GateWaySettingActivity.startActivity(activity,gateWay.getGatewayID());
									} else {
										ToastUtils.ShowError(activity, getString(R.string.no_rol_info),
												Toast.LENGTH_SHORT,true);
									}
								}
							}
						}
						break;
					case 1:
						if(gateWay.getTypeId()==5) {
							DialogAlert alert = new DialogAlert(activity);
							alert.init(gateWay.getGatewayName(),
									activity.getString(R.string.isDelGateWay));
							alert.setSureListener(new DialogAlert.OnSureListener() {

								@Override
								public void onSure() {
									// TODO Auto-generated method stub
									int result = MyApplication.getInstance()
											.getWidgetDataBase()
											.deleteGateWay(gateWay.getId());
									if (result > 0) {
										DeviceSDK.closeDevice(gateWay
												.getUserid());
										DeviceSDK.destoryDevice(gateWay
												.getUserid());
										String useridStr = gateWay.getUserid()
												+ "";
										GateWayService.mp.remove(gateWay
												.getGatewayID());
										GateWayService.list_userid.remove(useridStr);

										ToastUtils.ShowSuccess(activity,activity.getString(R.string.deleteSuccess),Toast.LENGTH_SHORT,true);
										GateWayService.list.remove(adapterPosition);
										GateWayFactory.getInstance().clearList();
										changeList();
									} else {
										ToastUtils.ShowError(activity, activity.getString(R.string.deleteFail), Toast.LENGTH_SHORT,true);
									}
								}
								@Override
								public void onCancle() {
									// TODO Auto-generated method stub
								}
							});
							alert.show();
						}
						else if(gateWay.getTypeId()==2) {
							if ( gateWay.getState().equals(activity.getString(R.string.gateWayLine))) {
								if (gateWay.getUserName().equals("admin")) {
									TabMyActivity.getInstance().showFragMent(6);
									TabMyActivity.getInstance().gateWaySettingFragment.setInit(gateWay);
								} else {
									ToastUtils.ShowError(activity, getString(R.string.no_rol_info),
											Toast.LENGTH_SHORT, true);
								}
							}else{
									ToastUtils.ShowError(activity,  getString(R.string.device_no_line), Toast.LENGTH_SHORT,true);
								}
						}else if(gateWay.getTypeId()==3){
							ToastUtils.ShowError(activity, "您当前没有设置权限", Toast.LENGTH_SHORT,true);
						}else{
							TabMyActivity.getInstance().showFragMent(5);
							TabMyActivity.getInstance().gatewayAddFragment.setEdite("Edite",gateWay,0);
						}
						break;
					case 2:
						if(gateWay.getTypeId()==2) {
							TabMyActivity.getInstance().showFragMent(5);
							TabMyActivity.getInstance().gatewayAddFragment.setEdite("Edite",gateWay,0);
						}else 	if(gateWay.getTypeId()==3) {
							TabMyActivity.getInstance().showFragMent(5);
							TabMyActivity.getInstance().gatewayAddFragment.setEdite("Edite",gateWay,0);
						}else{
							DialogAlert alert = new DialogAlert(activity);
							alert.init(gateWay.getGatewayName(),
									activity.getString(R.string.isDelGateWay));
							alert.setSureListener(new DialogAlert.OnSureListener() {
								@Override
								public void onSure() {
									// TODO Auto-generated method stub
									int result = MyApplication.getInstance()
											.getWidgetDataBase()
											.deleteGateWay(gateWay.getId());
									if (result > 0) {
										DeviceSDK.closeDevice(gateWay
												.getUserid());
										DeviceSDK.destoryDevice(gateWay
												.getUserid());
										String useridStr = gateWay.getUserid()
												+ "";
										GateWayService.mp.remove(gateWay
												.getGatewayID());
										GateWayService.list_userid.remove(useridStr);
									//	GateWayFactory.clearList();
										ToastUtils.ShowSuccess(activity,activity.getString(R.string.deleteSuccess),Toast.LENGTH_SHORT,true);
									//	adapter.getItems().remove(adapterPosition);
										GateWayService.list.remove(adapterPosition);
										GateWayFactory.getInstance().clearList();
//										adapter.notifyDataSetChanged();
										changeList();
									} else {
										ToastUtils.ShowError(activity, activity.getString(R.string.deleteFail), Toast.LENGTH_SHORT,true);
									}
								}
								@Override
								public void onCancle() {
									// TODO Auto-generated method stub
								}
							});
							alert.show();
						}
						break;
					case 3:
						DialogAlert alert = new DialogAlert(activity);
						alert.init(gateWay.getGatewayName(),
								activity.getString(R.string.isDelGateWay));
						alert.setSureListener(new DialogAlert.OnSureListener() {

							@Override
							public void onSure() {
								// TODO Auto-generated method stub
								int result = MyApplication.getInstance()
										.getWidgetDataBase()
										.deleteGateWay(gateWay.getId());
								if (result > 0) {
									DeviceSDK.closeDevice(gateWay
												.getUserid());
										DeviceSDK.destoryDevice(gateWay
												.getUserid());
										String useridStr = gateWay.getUserid()
												+ "";
										GateWayService.mp.remove(gateWay
												.getGatewayID());
										GateWayService.link_userid.remove(useridStr);
										ToastUtils.ShowSuccess(activity,
												activity.getString(R.string.deleteSuccess),
												Toast.LENGTH_SHORT,true);
									GateWayService.list.remove(adapterPosition);
									GateWayFactory.getInstance().clearList();
									changeList();
								} else {
									ToastUtils.ShowError(activity, activity.getString(R.string.deleteFail), Toast.LENGTH_SHORT,true);
								}
							}
							@Override
							public void onCancle() {
								// TODO Auto-generated method stub
							}
						});
						alert.show();
						break;
					default:
						break;
				}

			}
		}
	};

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(hidden) {
			if (ga != null && ga.isShow()) {
				ga.diss();
			}
            GateWayService.setCameraHandleListener(null);
		}else{
			GateWayService.setCameraHandleListener(this);
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}
}
