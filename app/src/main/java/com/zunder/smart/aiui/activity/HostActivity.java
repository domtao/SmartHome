/**
 * 
 */
package com.zunder.smart.aiui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.adapter.HostAdapter;
import com.zunder.smart.broadcast.ReceiverBroadcast;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dialog.TipAlert;
import com.zunder.smart.dialog.WarnDialog;
import com.zunder.smart.listener.HostListener;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.service.GateWayService;
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

/**
 * @author Administrator
 * 
 */
public class HostActivity extends Activity implements OnClickListener,HostListener{

	SwipeMenuRecyclerView listView;
	HostActivity activity;
	TextView editeTxt,backTxt;
	WarnDialog warnDialog;
	HostAdapter adapter = null;
	List<GateWay> list=new ArrayList<GateWay>();

	public static void startActivity(Activity activity) {
		Intent intent = new Intent(activity, HostActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_host);
		activity = this;
		ReceiverBroadcast.setHostListener(this);
		initView();
		showDialog();
	}
	public void showDialog(){

		if (warnDialog == null) {
			warnDialog = new WarnDialog(activity, getString(R.string.search));
			LayoutInflater warnDialog_inflater = getLayoutInflater();
			warnDialog.setMessage("正在获取网关设备 5");

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
						if (startCount >= 6) {
							searchflag = false;
							if ((warnDialog != null) && warnDialog.isShowing()) {
								warnDialog.dismiss();
								handler.sendEmptyMessage(-1);
							}
						}else{
							Message message = handler.obtainMessage();
							message.what = -2;
							handler.sendMessage(message);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		list.clear();
		sendCode("getDevice:00");
//		startTime();
	}

	private void initView() {
		backTxt=(TextView)findViewById(R.id.backTxt);
		backTxt.setOnClickListener(this);
		editeTxt=(TextView)findViewById(R.id.editeTxt);
		editeTxt.setOnClickListener(this);
		listView = (SwipeMenuRecyclerView) findViewById(R.id.list_devices);


		listView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
		listView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		listView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		listView.addItemDecoration(new ListViewDecoration());// 添加分割线。

		listView.setSwipeMenuCreator(swipeMenuCreator);
		// 设置菜单Item点击监听。
		listView.setSwipeMenuItemClickListener(menuItemClickListener);

		adapter = new HostAdapter(activity,list,GateWayService.mp);

		listView.setAdapter(adapter);
	}
	private Handler handler=new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what){
				case -2:
					warnDialog.setMessage("正在获取网关设备 "+(6-startCount));
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
					adapter.notifyDataSetChanged();
					break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.editeTxt:
			final List<GateWay> listGateWay= GateWayFactory.getInstance().getAll();
			List<String> listStr=new ArrayList<String>();
			for (int i=0;i<listGateWay.size();i++){
				GateWay gateWay=listGateWay.get(i);
				if (gateWay.getGatewayID().equals(AiuiMainActivity.deviceID)||isExite(gateWay.getGatewayID())==1) {
					continue;
				}
				listStr.add(gateWay.getGatewayName());
			}
			if(listStr.size()>0) {
				final AlertViewWindow alertViewWindow = new AlertViewWindow(activity, "网关", listStr, null, 0);
				alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {

					@Override
					public void onItem(int pos, String itemName) {
						GateWay gateWay = GateWayFactory.getInstance().getGateWayById(itemName);
						if (gateWay != null) {
							gateWay.setSeqencing(1);
							sendCode("add:" + gateWay.convertTostring());
						}
						list.clear();
						sendCode("getDevice:00");
						alertViewWindow.dismiss();
					}
				});
				alertViewWindow.show();
			}else{
				ToastUtils.ShowError(activity,"网关已存在列表",Toast.LENGTH_SHORT,true);
			}
			break;
			case R.id.backTxt:
				finish();
				break;
		default:
			break;
		}
	}
	public int isExite(String deviceID){
		int result=0;
		for (int i=0;i<list.size();i++){
			if(list.get(i).getGatewayID().equals(deviceID)){
				result=1;
				break;
			}
		}
		return  result;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ReceiverBroadcast.setHostListener(null);
	}

	public void sendCode(String cmd){
		String result = ISocketCode.setGateWay(cmd,
			AiuiMainActivity.deviceID);
		MainActivity.getInstance().sendCode(result);
	}
	private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
		@Override
		public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
			int hSize = getResources().getDimensionPixelSize(R.dimen.item_gateway_height);
			int wSize = getResources().getDimensionPixelSize(R.dimen.item_width);
			SwipeMenuItem wechatItem = new SwipeMenuItem(activity)
						.setBackgroundDrawable(R.color.pink)
						.setText("模式")
						.setWidth(wSize)
						.setHeight(hSize);
				swipeRightMenu.addMenuItem(wechatItem);// 添加一个按钮到右侧菜单。


			SwipeMenuItem addItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.red)
					.setText(getString(R.string.delete))
					.setTextColor(Color.WHITE)
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(addItem); // 添加一个按钮到右侧菜单。
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
						List<String> listStr=new ArrayList<>();
						listStr.add("启用");
						listStr.add("禁用");
						final AlertViewWindow alertViewWindow=new AlertViewWindow(activity,"网关",listStr,null,0);
						alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener(){

							@Override
							public void onItem(int pos, String itemName) {
								if(gateWay!=null){
									if(pos==0) {
										gateWay.setSeqencing(1);
									}else{
										gateWay.setSeqencing(0);
									}
									sendCode("update:"+gateWay.convertTostring());
								}
								handler.sendEmptyMessage(1);
								alertViewWindow.dismiss();
							}
						});
						alertViewWindow.show();
						break;
					case 1:
						sendCode("delete:"+gateWay.getId());
						list.remove(adapterPosition);
						handler.sendEmptyMessage(1);
//						sendCode("getDevice:00");
						break;
					default:
						break;
				}
			}
		}
	};
	@Override
	public void getMsg(String result) {
		String[] content=result.split(":");
		if(content[0].equals("device")){
			String[] str=content[1].trim().split(";");
			GateWay gateWay=new GateWay();
			gateWay.setId(Integer.parseInt(str[0]));
			gateWay.setGatewayName(str[1]);
			gateWay.setGatewayID(str[2]);
			gateWay.setUserName(str[3]);
			gateWay.setUserPassWord(str[4]);
			gateWay.setTypeId(Integer.parseInt(str[5]));
			gateWay.setCreationTime("00:00:00");
			gateWay.setIsCurrent(Integer.parseInt(str[6]));
			gateWay.setState(str[7]);
			gateWay.setSeqencing(Integer.parseInt(str[8]));
			list.add(gateWay);
			handler.sendEmptyMessage(1);
		}else if(content[0].equals("Err")){
			Message message=handler.obtainMessage();
			message.what=0;
			message.obj=content[1];
			handler.sendMessage(message);
		}
	}
}