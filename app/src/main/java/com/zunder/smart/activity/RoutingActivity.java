package com.zunder.smart.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.adapter.RouteAdapter;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.listener.RouteListener;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.Route;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.service.GateWayService;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.view.ListViewDecoration;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RoutingActivity extends Activity implements OnClickListener,
		RouteListener {
	private TextView backTxt;
	private List<Route> listPassive = new ArrayList<Route>();
	private SwipeMenuRecyclerView listView;
	public static RoutingActivity activity;
	RouteAdapter adapter;
	TextView msgTxt;
	TextView addDevice;
	static String deviceID ;
	public static void startActivity(Activity activity,String _deviceID) {
		 deviceID=_deviceID;
		Intent intent = new Intent(activity, RoutingActivity.class);
		activity.startActivity(intent);
	}
	int index = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_routing);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		TcpSender.setRouteListener(this);
		activity = this;
		backTxt = (TextView) findViewById(R.id.backTxt);
		msgTxt=(TextView)findViewById(R.id.msgTxt);
		backTxt.setOnClickListener(this);
		addDevice=(TextView)findViewById(R.id.addDevice);
		addDevice.setOnClickListener(this);
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

		adapter = new RouteAdapter(activity,listPassive);
		listView.setAdapter(adapter);
		listView.setAdapter(adapter);
        showProgressDialog(getString(R.string.load_data));
//		SendCMD.getInstance().sendCmd(256,"02:00",null);
		String result = ISocketCode.setForward(
				"*N020000000000000000", deviceID);
		MainActivity.getInstance().sendCode( result);
        startCount = 0;
        searchflag = true;
        startTime();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			TcpSender.setRouteListener(null);
			finish();
			break;

			case R.id.addDevice:
				if(getGateWay().size()>0) {
					showVideoWindow();
				}else{
					ToastUtils.ShowError(activity,getString(R.string.gateway_no_line),Toast.LENGTH_SHORT,true);
				}
				break;
		}
	}

	protected ProgressDialog progressDialog;

	protected void showProgressDialog(String msg) {
		progressDialog = null;
		progressDialog = ProgressDialog.show(this, "", msg, true, true);
	}

	protected void hideProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	private int startCount = 0;
	private boolean searchflag = false;

	private void startTime() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (searchflag) {
					try {
						Thread.sleep(100);
						startCount++;
						if (startCount >= 50) {
							searchflag = false;
							if ((progressDialog != null)
									&& progressDialog.isShowing()) {
								progressDialog.dismiss();
							}

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TcpSender.setPassiveListener(null);
	}

	public void back() {
		TcpSender.setRouteListener(null);
		this.finish();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				String cmd=msg.obj.toString();
				String routeID=cmd.substring(4);
				Route route=new Route();
				route.setRouteID(routeID);
				route.setRouteName(getGateWayName(routeID));
				route.setRouteType(5);
				if(listPassive.size()==0){
					listPassive.add(route);
				}else {
					int index=0;
				for (int i=0;i<listPassive.size();i++){
					if(route.getRouteID().equals(listPassive.get(i).getRouteID())){
						index=1;
						break;
					}
				}
				if(index==0){
					listPassive.add(route);
				}
				}
				adapter.notifyDataSetChanged();
				searchflag = false;
				if ((progressDialog != null)
						&& progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				break;
			case 1:

				break;
			case 2:
				//hideProgressDialog();
//				PassiveListActivity.startActivity(activity, msg.obj.toString());
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
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}



	private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
		@Override
		public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
			int hSize = getResources().getDimensionPixelSize(R.dimen.item_height);
			int wSize = getResources().getDimensionPixelSize(R.dimen.item_width);
			SwipeMenuItem delItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.red)
					.setText(getString(R.string.delete))
					.setTextColor(Color.WHITE)
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(delItem); // 添加一个按钮到右侧菜单。
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
						alert.init(activity.getString(R.string.tip),"是否删除路由");
						alert.setSureListener(new DialogAlert.OnSureListener() {

							@Override
							public void onSure() {
								// TODO Auto-generated
								String cmd =listPassive.get(adapterPosition).getRouteID();
								//SendCMD.getInstance().sendCmd(256,"00:"+cmd,null);
								String  result = ISocketCode.setForward(
										"*N00"+cmd, deviceID);
								MainActivity.getInstance().sendCode( result);
								listPassive.remove(adapterPosition);


								adapter.notifyDataSetChanged();
							//	SendCMD.getInstance().sendCmd(256,"02:00",null);
								result = ISocketCode.setForward(
										"*N020000000000000000", deviceID);
								MainActivity.getInstance().sendCode( result);
							}
							@Override
							public void onCancle() {
								// TODO Auto-generated
							}
						});
						alert.show();
					case 1:
						break;
				}
			}
		}
	};


	@Override
	public void setRoute(String cmd) {
		Log.e("N",cmd);
		Message msg=new Message();
		msg.what=0;
		msg.obj=cmd;
		handler.sendMessage(msg);
	}

	public List<String> getGateWay() {
		List<String> resultLlist = new ArrayList<String>();
		List<GateWay> list = GateWayService.list;
		for (int i = 0; i < list.size(); i++) {
			GateWay gateWay = list.get(i);
			if (gateWay.getTypeId()<3) {
				resultLlist.add(gateWay.getGatewayName());
			}
		}
		return resultLlist;
	}

	private void showVideoWindow() {
		final AlertViewWindow alertViewWindow=new AlertViewWindow(activity,getString(R.string.seletgateway), getGateWay(),null,0);
		alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
			@Override
			public void onItem(int pos, String itemName) {
				if(itemName.equals("")){
					return;
				}
				GateWay gateWay=getGateWay(itemName);
					if(gateWay!=null) {
						try {
							String cmd =gateWay.getGatewayID();

							String result = ISocketCode.setForward(
									"*N01"+cmd, deviceID);
							MainActivity.getInstance().sendCode( result);
//							SendCMD.getInstance().sendCmd(256,"01:"+cmd,null);
//							Thread.sleep(1000);
//							SendCMD.getInstance().sendCmd(256,"02:00",null);
							result = ISocketCode.setForward(
									"*N020000000000000000", deviceID);
							MainActivity.getInstance().sendCode( result);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				alertViewWindow.dismiss();
			}
		});
		alertViewWindow.show();
	}
	public GateWay getGateWay(String gateWayName) {
		GateWay gateWay=null;
		List<GateWay> list = GateWayService.list;
		for (int i = 0; i < list.size(); i++) {
			gateWay = list.get(i);
			if (gateWay.getGatewayName().equals(gateWayName)) {
				break;
			}
		}
		return gateWay;
	}

	public String getGateWayName(String gateWayID) {
		String gateWay = "小网关";
		List<GateWay> list = GateWayService.list;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getGatewayID().equals(gateWayID)) {
				gateWay = list.get(i).getGatewayName();
				break;
			}
		}
		return gateWay;
	}
}
