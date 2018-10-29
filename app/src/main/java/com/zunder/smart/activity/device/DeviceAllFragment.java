package com.zunder.smart.activity.device;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
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
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.activity.main.TabHomeActivity;
import com.zunder.smart.activity.main.TabMyActivity;
import com.zunder.smart.adapter.DeviceAdapter;
import com.zunder.smart.adapter.DeviceAllAdapter;
import com.zunder.smart.adapter.SearchDeviceAdapter;
import com.zunder.smart.aiui.activity.AddDeviceActivity;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.DeviceTypeFactory;
import com.zunder.smart.dao.impl.factory.RoomFactory;
import com.zunder.smart.dialog.DialogAlert;

import com.zunder.smart.listener.DeviceListener;
import com.zunder.smart.listener.DeviceStateListener;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.Device;

import com.zunder.smart.service.TcpSender;
import com.zunder.smart.view.ListViewDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeviceAllFragment extends Fragment implements OnClickListener,
        DeviceStateListener,View.OnTouchListener {
	DeviceAllAdapter adapter;
	List<Device> listDevice;
	private TextView backTxt;
	private TextView titleTxt;
	private SwipeMenuRecyclerView listView;
	public Activity activity;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_all_device, container,
				false);
		activity = getActivity();
		backTxt = (TextView) root.findViewById(R.id.backTxt);
		titleTxt = (TextView) root.findViewById(R.id.titleTxt);
		backTxt.setOnClickListener(this);

		listView = (SwipeMenuRecyclerView) root.findViewById(R.id.deviceList);
		listView.setLayoutManager(new LinearLayoutManager(activity));// 布局管理器。
		listView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		listView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		listView.addItemDecoration(new ListViewDecoration());// 添加分割线。

		// 设置菜单Item点击监听。
		listView.setSwipeMenuItemClickListener(menuItemClickListener);
		listView.setLongPressDragEnabled(true);// 开启拖拽，就这么简单一句话。
		listView.setOnItemMoveListener(onItemMoveListener);
		root.setOnTouchListener(this);
		return root;
	}
	public void initAdapter(int roomId,int deviceTypeKey){
		//joe
		TcpSender.setDeviceStateListener(this);
		if(roomId!=0){
			titleTxt.setText(RoomFactory.getInstance().getRoomById(roomId)+"全部设备");
		}else if(deviceTypeKey!=0){
			titleTxt.setText(DeviceTypeFactory.getInstance().getDeviceTypeName(deviceTypeKey)+"全部设备");
		}
		listDevice = DeviceFactory.getInstance().getDeviceByAction(roomId, deviceTypeKey, 0,1);
		adapter = new DeviceAllAdapter(activity, listDevice,roomId);
		adapter.setOnItemClickListener(onItemClickListener);
		listView.setAdapter(adapter);
    }
	private OnItemMoveListener onItemMoveListener = new OnItemMoveListener() {
		@Override
		public boolean onItemMove(int fromPosition, int toPosition) {
			// 当Item被拖拽的时候。
			Device _from = listDevice.get(fromPosition);
			if (fromPosition < toPosition) {
				listDevice.add(toPosition + 1, _from);
				listDevice.remove(fromPosition);
			} else if (fromPosition > toPosition) {
				listDevice.add(toPosition, _from);
				listDevice.remove(fromPosition + 1);
			}
			adapter.notifyItemMoved(fromPosition, toPosition);
			MyApplication.getInstance().getWidgetDataBase().updateDeviceSort(listDevice);
			DeviceFactory.getInstance().clearList();
			return true;// 返回true表示处理了，返回false表示你没有处理。
		}
		@Override
		public void onItemDismiss(int position) {
			// 当Item被滑动删除掉的时候，在这里是无效的，因为这里没有启用这个功能。
			// 使用Menu时就不用使用这个侧滑删除啦，两个是冲突的。
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			TabHomeActivity.getInstance().hideFragMent(3);
			break;
		default:
			break;
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(hidden){
			TcpSender.setDeviceStateListener(null);
		}else{
			TcpSender.setDeviceStateListener(this);
		}
	}

	@Override
	public void receiveDeviceStatus(String cmd) {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(0);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}
			}
		};
	};



	private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
		@Override
		public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
			int hSize = getResources().getDimensionPixelSize(R.dimen.item_height);
			int wSize = getResources().getDimensionPixelSize(R.dimen.item_width);
			SwipeMenuItem wechatItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.red)
					.setText(getString(R.string.delete))
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(wechatItem);// 添加一个按钮到右侧菜单。

			SwipeMenuItem closeItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.orange)
					.setText(getString(R.string.edit))
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(closeItem); // 添加一个按钮到右侧菜单。

		}
	};

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(final int position) {

			TabHomeActivity.getInstance().setSelect(position);
			TabHomeActivity.getInstance().hideFragMent(3);
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

					final Device device=adapter.getItems().get(adapterPosition);
					switch (menuPosition){
						case 0:
							break;
						case 1:
							break;
							}
				}
			}
		};

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}
}
