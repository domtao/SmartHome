package com.zunder.smart.aiui.activity;

import java.util.ArrayList;
import java.util.List;

import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.aiui.adapter.CusDeviceAdapter;
import com.zunder.smart.dao.impl.factory.CloundDeviceFactory;
import com.zunder.smart.dao.impl.factory.ProductFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.Products;
import com.zunder.smart.popwindow.CusActionPopupWindow;
import com.zunder.smart.socket.info.ISocketCode;
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
import android.graphics.Color;
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

public class CusDeviceListActivity extends Activity implements OnClickListener {
	CusDeviceAdapter adapter;
	List<Device> listDevice;
	private TextView backTxt;
	private SwipeMenuRecyclerView listView;
	public static CusDeviceListActivity activity;
	public static List<String> list = new ArrayList<String>();

	public static void startActivity(Activity activity) {
		Intent intent = new Intent(activity, CusDeviceListActivity.class);
		activity.startActivityForResult(intent, 100);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_cus_device);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		activity = this;
		list.clear();
		backTxt = (TextView) findViewById(R.id.backTxt);
		backTxt.setOnClickListener(this);
		listView = (SwipeMenuRecyclerView) findViewById(R.id.deviceList);
		listView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
		listView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		listView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		listView.addItemDecoration(new ListViewDecoration());// 添加分割线。
		// 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
		// 设置菜单创建器。
		listView.setSwipeMenuCreator(swipeMenuCreator);
		// 设置菜单Item点击监听。
		listView.setSwipeMenuItemClickListener(menuItemClickListener);


		listDevice = CloundDeviceFactory.getDeviceList();

		adapter = new CusDeviceAdapter(activity,listDevice);
		adapter.setOnItemClickListener(onItemClickListener);
		listView.setAdapter(adapter);

	}

	public CusActionPopupWindow popupWindow = null;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.backTxt:
				back();
				break;
			default:
				break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void back() {

		Intent resultIntent = new Intent();
		this.setResult(100, resultIntent);
		this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == event.KEYCODE_BACK) {
			back();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}
			}
		}

		;
	};


	private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
		@Override
		public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
			int hSize = getResources().getDimensionPixelSize(R.dimen.item_height);
			int wSize = getResources().getDimensionPixelSize(R.dimen.item_width);


			SwipeMenuItem closeItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.red)
					.setText(getString(R.string.delete))
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(closeItem); // 添加一个按钮到右侧菜单。


			SwipeMenuItem addItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.blue)
					.setText(getString(R.string.edit))
					.setTextColor(Color.WHITE)
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(addItem); // 添加一个按钮到右侧菜单。
		}
	};

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(int position) {
			if (popupWindow != null && popupWindow.isShow()) {
				popupWindow.dismiss();
			} else {
				CusModeListActivity.isSend = 1;
				int productID = listDevice.get(position)
						.getProductsKey();
				Products product = ProductFactory.getProduct(productID);
				if (product != null) {
					popupWindow = new CusActionPopupWindow(activity,
							listDevice.get(position).getDeviceName(),
							listDevice.get(position).getProductsCode(),
							listDevice.get(position).getDeviceTypeKey(),
							listDevice.get(position).getId(), product
							.getActionShow(), product
							.getFunctionShow(), product
							.getActionPramShow(), product
							.getFunctionParamShow());
					popupWindow.show();
				}
			}}
		}

		;
		/**
		 * 菜单点击监听。
		 */
		private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
			@Override
			public void onItemClick(Closeable closeable, final int adapterPosition, int menuPosition, int direction) {
				closeable.smoothCloseMenu();// 关闭被点击的菜单。
				if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
					final Device device = adapter.getItems().get(adapterPosition);
					switch (menuPosition) {

						case 0:
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

									Toast.makeText(activity,
											activity.getString(R.string.deleteSuccess),
											Toast.LENGTH_SHORT).show();
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
							AddDeviceActivity.startActivity(activity, "Edite",
									device);
							break;
					}
				}
			}
		};

}