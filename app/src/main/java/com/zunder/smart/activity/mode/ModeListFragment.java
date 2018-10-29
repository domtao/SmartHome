package com.zunder.smart.activity.mode;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.zunder.smart.activity.device.DeviceFragment;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.activity.main.TabMainActivity;
import com.zunder.smart.activity.main.TabModeActivity;
import com.zunder.smart.activity.main.TabMyActivity;
import com.zunder.smart.adapter.DeviceAdapter;
import com.zunder.smart.adapter.ModeListAdapter;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dao.impl.factory.GateWayTypeFactory;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.dao.impl.factory.ModeListFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.listener.ModeMsgListener;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.Mode;
import com.zunder.smart.model.ModeList;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.popwindow.listener.OnItemClickListener;
import com.zunder.smart.popwindow.listener.PopWindowListener;
import com.zunder.smart.service.GateWayService;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.SendThread;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.view.ListViewDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//情景添加設備列表
public class ModeListFragment extends Fragment implements OnClickListener,View.OnTouchListener,ModeMsgListener{
	ModeListAdapter adapter;
	List<ModeList> modeList;
	Mode mode;
	private Activity activity;
	private TextView backTxt;
	private TextView titleTxt;
	private TextView editeTxt;
	SwipeMenuRecyclerView listView;
	public static int modeId = -1;
	public static int Edite = -1;
	private RightMenu rightButtonMenuAlert;
	static String title = "";
	private int selitem = 0;
	private String modecode;
	public static int isChange = 0;
	private Button moreBtn;
	TextView modeMsg;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_mode_list_1, container,
				false);
		root.setClickable(true);
		activity = getActivity();
		backTxt = (TextView) root.findViewById(R.id.backTxt);
		titleTxt = (TextView) root.findViewById(R.id.titleTxt);
		editeTxt = (TextView) root.findViewById(R.id.editeTxt);
		moreBtn=(Button)root.findViewById(R.id.moreBtn);
		modeMsg=(TextView)root.findViewById(R.id.modeMsg);
		moreBtn.setOnClickListener(this);
		root.setOnTouchListener(this);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		listView = (SwipeMenuRecyclerView) root.findViewById(R.id.deviceList);
		listView.setLayoutManager(new LinearLayoutManager(activity));// 布局管理器。
		listView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		listView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		listView.addItemDecoration(new ListViewDecoration());// 添加分割线。
		// 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
		// 设置菜单创建器。
		listView.setSwipeMenuCreator(swipeMenuCreator);
		// 设置菜单Item点击监听。
		listView.setSwipeMenuItemClickListener(menuItemClickListener);
		listView.setLongPressDragEnabled(true);// 开启拖拽，就这么简单一句话。
		listView.setOnItemMoveListener(onItemMoveListener);// 监听拖拽，更新UI。
		initRightButtonMenuAlert();
		return root;
	}

	public void initAdapter(int  _modeId) {
		modeId = _modeId;
		this.mode = ModeFactory.getInstance().getByIDMode(modeId);
		if (mode != null) {
			titleTxt.setText(mode.getModeName());
			if(mode.getModeType().equals("FF")){
				moreBtn.setVisibility(View.VISIBLE);
				editeTxt.setText("更多");
				init();
			}else{
				moreBtn.setVisibility(View.GONE);
				editeTxt.setText("添加设备");
			}
		}
		modeMsg.setVisibility(View.GONE);
		modeList = ModeListFactory.getInstance().getModeDevice(modeId);
		adapter = new ModeListAdapter(activity, modeList);
		adapter.setOnItemClickListener(onItemClickListener);
		listView.setAdapter(adapter);
		adapter.setOnItemClickListener(onItemClickListener);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.backTxt:
				if (MainActivity.getInstance().mHost.getCurrentTab() == 2) {
					TabModeActivity.getInstance().hideFragMent(2);
				}
				else {
					TabMainActivity.getInstance().hideFragMent(2);
				}
				break;
			case R.id.editeTxt:
				if(mode.getModeType().equals("FF")){
					rightButtonMenuAlert.show(editeTxt);
				}else {
					if (MainActivity.getInstance().mHost.getCurrentTab() == 2) {
						TabModeActivity.getInstance().showFragMent(3);
						TabModeActivity.getInstance().modeDeviceFragment.initAdapter(mode.getId());
					}
				}
				break;
			case R.id.moreBtn:
				if (mode != null) {
					if(GateWayFactory.getInstance().getMainGateWay()>1) {
						final AlertViewWindow alertViewWindow = new AlertViewWindow(activity, getString(R.string.seletgateway),GateWayFactory.getInstance().getGateWay(), null, 0);
						alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
							@Override
							public void onItem(int pos, String itemName) {
								if (itemName.equals("")) {
									return;
								}
								GateWay gateWay =GateWayFactory.getInstance().getGateWayByName(itemName);
								if (gateWay != null) {
									Device device=new Device();
									device.setDeviceBackCode(gateWay.getGatewayID());
									device.setCmdDecodeType(gateWay.getTypeId());
									DeviceFactory.getInstance().setGateWayDevice(device);
									SendCMD cmdsend = SendCMD.getInstance();
									cmdsend.sendCMD(1, mode.getModeName(), device);
								}

								alertViewWindow.dismiss();
							}
						});
						alertViewWindow.show();
					}else{
						if(GateWayFactory.getInstance().getMainGateWay()==1) {
							Device device = new Device();
							device.setDeviceBackCode(GateWayFactory.getInstance().list.get(0).getGatewayID());
							device.setCmdDecodeType(GateWayFactory.getInstance().list.get(0).getTypeId());
							DeviceFactory.getInstance().setGateWayDevice(device);
							SendCMD cmdsend = SendCMD.getInstance();
							cmdsend.sendCMD(1, mode.getModeName(), device);
						}
					}
				}
				break;
			default:
				break;
		}
	}
	private void initRightButtonMenuAlert() {

		rightButtonMenuAlert = new RightMenu(activity, R.array.modelists,
				R.drawable.modelist_images);

		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {

			@Override
			public void onItemClick(final int position) {
				// TODO Auto-generated method stub
				rightButtonMenuAlert.dismiss();
				if(position>0&&position<5) {
					Device _device = new Device();
					_device.setDeviceName(mode.getModeName());
					_device.setDeviceID("FF0000");
					_device.setProductsCode("FF");
					_device.setDeviceIO(mode.getModeLoop()+"");
					_device.setSceneId(mode.getModeCode());
					ModeTimerActivity.startActivity(activity, _device);
				}else {
					TabMainActivity.getInstance().showFragMent(3);
					TabMainActivity.getInstance().modeDeviceFragment.initAdapter(mode.getId());
				}
				rightButtonMenuAlert.dismiss();
			}
		});

	}
	void init(){
		if(GateWayFactory.getInstance().getMainGateWay()>=1) {
			Device device = new Device();
			device.setDeviceBackCode(GateWayFactory.getInstance().list.get(0).getGatewayID());
			device.setCmdDecodeType(GateWayFactory.getInstance().list.get(0).getTypeId());
			DeviceFactory.getInstance().setGateWayDevice(device);
			SendCMD cmdsend = SendCMD.getInstance();
			cmdsend.sendCMD(4, mode.getModeName(), device);
		}
	}
	public void back() {

		if (mode != null) {
			if (mode.getModeType().equals("FF")) {
				DialogAlert alert = new DialogAlert(activity);
				alert.init(getString(R.string.tip), getString(R.string.is_save_mode));
				alert.setSureListener(new DialogAlert.OnSureListener() {

					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						Edite = -1;
						//if (mode != null) {
						//	SendCMD cmdsend = SendCMD.getInstance();
						//	cmdsend.sendCMD(1, mode.getModeName(), null);
						//}
					}

					@Override
					public void onCancle() {
						// TODO Auto-generated method stub


					}
				});
				alert.show();
			} else {

			}
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case -3:
					ToastUtils.ShowSuccess(activity,msg.obj.toString(),Toast.LENGTH_SHORT,true);
					break;
				case 0:
					modeMsg.setVisibility(View.VISIBLE);
					Device device=DeviceFactory.getInstance().getGateWayDevice();
					if(device!=null) {
					    String result=GateWayFactory.getInstance().getGateWayByID(device.getDeviceBackCode());
                        int total = Integer.parseInt(msg.obj.toString());
                        modeMsg.setText("网关:"+result+"\n总数:250\n剩余:" + total);
                    }
					break;
			}
		}
	};

	private OnItemMoveListener onItemMoveListener = new OnItemMoveListener() {
		@Override
		public boolean onItemMove(int fromPosition, int toPosition) {
			// 当Item被拖拽的时候。
			Collections.swap(modeList, fromPosition, toPosition);
			adapter.notifyItemMoved(fromPosition, toPosition);
			MyApplication.getInstance().getWidgetDataBase()
					.updateModeListSort(modeList);
			ModeListFactory.getInstance().clearList();
			return true;// 返回true表示处理了，返回false表示你没有处理。
		}
		@Override
		public void onItemDismiss(int position) {
			// 当Item被滑动删除掉的时候，在这里是无效的，因为这里没有启用这个功能。
			// 使用Menu时就不用使用这个侧滑删除啦，两个是冲突的。
		}
	};

	private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
		@Override
		public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
			int hSize = getResources().getDimensionPixelSize(R.dimen.item_height60);
			int wSize = getResources().getDimensionPixelSize(R.dimen.item_width);
			SwipeMenuItem deleteItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.red)
					.setText(getString(R.string.delete))
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧菜单。

//			SwipeMenuItem wechatItem = new SwipeMenuItem(activity)
//					.setBackgroundDrawable(R.color.blue)
//					.setText(getString(R.string.edit))
//					.setWidth(wSize)
//					.setHeight(hSize);
//			swipeRightMenu.addMenuItem(wechatItem);// 添加一个按钮到右侧菜单。


		}
	};
private OnItemClickListener onItemClickListener=new OnItemClickListener(){

	@Override
	public void onItemClick(int pos) {
		if(MainActivity.getInstance().mHost.getCurrentTab()	==2) {
			TabModeActivity.getInstance().showFragMent(4);
			TabModeActivity.getInstance().modeListActionFragment.initAdapter(adapter.getitems().get(pos).getId(), modeId, adapter.getitems().get(pos).getDeviceId());
		}else{
			TabMainActivity.getInstance().showFragMent(4);
			TabMainActivity.getInstance().modeListActionFragment.initAdapter(adapter.getitems().get(pos).getId(), modeId, adapter.getitems().get(pos).getDeviceId());
		}
	}
};
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(hidden){
			TcpSender.setModeMsgListener(null);
		}else{
			TcpSender.setModeMsgListener(this);
		}
	}

	public OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
		@Override
		public void onItemClick(Closeable closeable, final int adapterPosition, int menuPosition, int direction) {
			closeable.smoothCloseMenu();// 关闭被点击的菜单。
			if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
				final ModeList modeList=adapter.getitems().get(adapterPosition);
				switch (menuPosition){
					case 0:
						DialogAlert alert = new DialogAlert(activity);
						alert.init(modeList.getDeviceName(),
								activity.getString(R.string.isDelModeList));
						alert.setSureListener(new DialogAlert.OnSureListener() {

							@Override
							public void onSure() {
								// TODO Auto-generated method stub
								int result = MyApplication.getInstance()
										.getWidgetDataBase()
										.deleteModelist(modeList.getId());
								if (result > 0) {
									ModeListFactory.getInstance().delete(modeList.getId());
									adapter.getitems().remove(adapterPosition);
									adapter.notifyDataSetChanged();
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
						if(MainActivity.getInstance().mHost.getCurrentTab()	==2) {
							TabModeActivity.getInstance().showFragMent(4);
							TabModeActivity.getInstance().modeListActionFragment.initAdapter(adapter.getitems().get(adapterPosition).getId(), modeId, adapter.getitems().get(adapterPosition).getDeviceId());
						}else{
							TabMainActivity.getInstance().showFragMent(4);
							TabMainActivity.getInstance().modeListActionFragment.initAdapter(adapter.getitems().get(adapterPosition).getId(), modeId, adapter.getitems().get(adapterPosition).getDeviceId());
						}
						break;
				}
			}
		}
	};

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

	@Override
	public void sendMsg(String str) {
		Message message = handler.obtainMessage();
		message.obj = str;
		message.what = -3;
		handler.sendMessage(message);
	}

	@Override
	public void modeNumber(int Total) {
		Message message = handler.obtainMessage();
		message.obj = Total;
		message.what = 0;
		handler.sendMessage(message);
	}
}
