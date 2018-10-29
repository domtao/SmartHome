package com.zunder.smart.activity.mode;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.activity.main.TabMainActivity;
import com.zunder.smart.activity.main.TabModeActivity;
import com.zunder.smart.adapter.ModeTabAdapter;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.dao.impl.factory.ModeListFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.listener.ModeMsgListener;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.Mode;
import com.zunder.smart.model.ModeList;
import com.zunder.smart.popwindow.AlertViewWindow;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.view.ListViewDecoration;

import java.util.ArrayList;
import java.util.List;

public class ModeFragment extends Fragment implements OnClickListener,View.OnTouchListener,ModeMsgListener {
	ModeTabAdapter adapter;
	List<Mode> listMode;
	private Activity activity;
	private SwipeMenuRecyclerView modeList;
	private TextView editeTxt,backTxt,titleTxt;
	private String modeType="C9";
	EditText autoEdite;
	RelativeLayout autoLayout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_mode_edite, container,
				false);
		root.setOnTouchListener(this);
		activity = getActivity();
		autoLayout=(RelativeLayout)root.findViewById(R.id.autoLayout);
		backTxt=(TextView)root.findViewById(R.id.backTxt);
		backTxt.setOnClickListener(this);
		editeTxt=(TextView)root.findViewById(R.id.editeTxt);
		editeTxt.setOnClickListener(this);
		titleTxt=(TextView)root.findViewById(R.id.titleTxt);
		autoEdite=(EditText)root.findViewById(R.id.autoEdite);
		modeList = (SwipeMenuRecyclerView) root.findViewById(R.id.modeList);
		modeList.setLayoutManager(new LinearLayoutManager(activity));// 布局管理器。
		modeList.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		modeList.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		modeList.addItemDecoration(new ListViewDecoration());// 添加分割线。
		modeList.setSwipeMenuCreator(swipeMenuCreator);
		// 设置菜单Item点击监听。

		autoEdite.addTextChangedListener(new MyWatch());

		return root;
	}
	public void initAdapter(String modeType){
		autoEdite.setText("");
		this.modeType=modeType;
		if(modeType.equals("FF")){
			titleTxt.setText("情景列表");
			editeTxt.setText("更多");
		}else{
			titleTxt.setText("集合列表");
			editeTxt.setText("添加");
		}
		listMode=ModeFactory.getInstance().getModesByType(modeType,-1);
		if(listMode.size()>0){
			autoLayout.setVisibility(View.VISIBLE);
		}else{
			autoLayout.setVisibility(View.GONE);
		}
		adapter = new ModeTabAdapter(activity,listMode);
		adapter.setOnItemClickListener(onItemClickListener);
		modeList.setSwipeMenuItemClickListener(menuItemClickListener);
		modeList.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			if(MainActivity.getInstance().mHost.getCurrentTab()	==2) {
				TabModeActivity.getInstance().hideFragMent(0);
			}else{
				TabMainActivity.getInstance().hideFragMent(0);
			}
			break;
		case R.id.editeTxt:
			if(MainActivity.getInstance().mHost.getCurrentTab()	==2) {
				TabModeActivity.getInstance().showFragMent(1);
				TabModeActivity.getInstance().modeAddFragment.setDate("Add", modeType, 0);
			}else{
				TcpSender.setModeMsgListener(this);
				initRightButtonMenuAlert();
			}
			break;
		default:
			break;
		}
	}
	private void initRightButtonMenuAlert() {

		final RightMenu rightButtonMenuAlert = new RightMenu(activity, R.array.mode,
				R.drawable.modelist_images);

		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {

			@Override
			public void onItemClick(final int position) {
				// TODO Auto-generated method stub
				switch (position){
					case 0: {
						TabMainActivity.getInstance().showFragMent(1);
						TabMainActivity.getInstance().modeAddFragment.setDate("Add", modeType, 0);
					}
						break;
					case 1:
					{
						showVideoWindow(3);
					}
						break;
					case 2: {
						//joe 查询空间
						showVideoWindow(4);
					}
						break;
				}

				rightButtonMenuAlert.dismiss();
			}
		});
		rightButtonMenuAlert.show(editeTxt);
	}

	private void showVideoWindow(final int index) {
		if(GateWayFactory.getInstance().getMainGateWay()>1) {
			final AlertViewWindow alertViewWindow = new AlertViewWindow(activity, getString(R.string.seletgateway), GateWayFactory.getInstance().getGateWay(), null, 0);
			alertViewWindow.setAlertViewOnCListener(new AlertViewWindow.AlertViewOnCListener() {
				@Override
				public void onItem(int pos, String itemName) {
					if (itemName.equals("")) {
						return;
					}
					GateWay gateWay =  GateWayFactory.getInstance().getGateWayByName(itemName);
					if (gateWay != null) {
						Device device=new Device();
						device.setDeviceBackCode(GateWayFactory.getInstance().list.get(0).getGatewayID());
						device.setCmdDecodeType(GateWayFactory.getInstance().list.get(0).getTypeId());
						DeviceFactory.getInstance().setGateWayDevice(device);
						SendCMD cmdsend = SendCMD.getInstance();
						cmdsend.sendCMD(0, index+"", device);
					}
					alertViewWindow.dismiss();
				}
			});
			alertViewWindow.show();
		}else{
			if(GateWayFactory.getInstance().getMainGateWay()==1) {
				Device device=new Device();
				device.setDeviceBackCode(GateWayFactory.getInstance().list.get(0).getGatewayID());
				device.setCmdDecodeType(GateWayFactory.getInstance().list.get(0).getTypeId());
				DeviceFactory.getInstance().setGateWayDevice(device);
				SendCMD cmdsend = SendCMD.getInstance();
				cmdsend.sendCMD(0, index+"", null);
			}else{
				ToastUtils.ShowError(activity,"请添加网关设备",Toast.LENGTH_SHORT,true);
			}
		}
	}
	private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
		@Override
		public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
			int hSize = getResources().getDimensionPixelSize(R.dimen.item_height60);
			int wSize = getResources().getDimensionPixelSize(R.dimen.item_width);
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
						if(MainActivity.getInstance().mHost.getCurrentTab()	==2) {
							TabModeActivity.getInstance().showFragMent(1);
							TabModeActivity.getInstance().modeAddFragment.setDate("Edite", modeType, listMode.get(adapterPosition)
									.getId());
						}else{
							TabMainActivity.getInstance().showFragMent(1);
							TabMainActivity.getInstance().modeAddFragment.setDate("Edite", modeType, listMode.get(adapterPosition)
									.getId());
						}
						break;
					case 1:
						DialogAlert alert = new DialogAlert(activity);
						alert.init(listMode.get(adapterPosition).getModeName(),
								getString(R.string.isDelMode));
						alert.setSureListener(new DialogAlert.OnSureListener() {
							@Override
							public void onSure() {
								// TODO Auto-generated method stub
								int result = MyApplication
										.getInstance()
										.getWidgetDataBase()
										.deleteMode(
												listMode.get(adapterPosition)
														.getId());
								if (result > 0) {
									ToastUtils.ShowSuccess(activity,getString(R.string.deleteSuccess),Toast.LENGTH_SHORT,true);
									listMode.remove(adapterPosition);
									ModeFactory.getInstance().clearList();
									adapter.notifyDataSetChanged();
									if(MainActivity.getInstance().mHost.getCurrentTab()	==2) {
										TabModeActivity.getInstance().setAdapter();
									}else{
										TabMainActivity.getInstance().setAdapter();
									}
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
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(final int position) {
			if (MainActivity.getInstance().mHost.getCurrentTab() == 2) {
				TabModeActivity.getInstance().showFragMent(2);
				TabModeActivity.getInstance().modeListFragment.initAdapter(listMode.get(position).getId());
			}else{
				TabMainActivity.getInstance().showFragMent(2);
				TabMainActivity.getInstance().modeListFragment.initAdapter(listMode.get(position).getId());
			}
		}
	};
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					int total = Integer.parseInt(msg.obj.toString());
					ToastUtils.ShowSuccess(activity,"\n总数:250\n剩余:" + total,Toast.LENGTH_SHORT,true);
					break;
			}
		}
	};

	@Override
	public void sendMsg(String str) {

	}

	@Override
	public void modeNumber(int Total) {

	}

	private class MyWatch implements TextWatcher {
		@Override
		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

		}

		@Override
		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			//joe 0826 搜索条件 用户输入的字符串
			String input = charSequence.toString();
			int length = input.length();
			List<Mode> resultList= new ArrayList<Mode>();
			List<Mode> listMode= new ArrayList<Mode>();
			if (length>0) {
				//listMode=ModeFactory.getInstance().getModesByType(modeType,input);
				resultList=ModeFactory.getInstance().getModesByType(modeType,-1);

				List<ModeList> ModeListArr= new ArrayList<ModeList>();
				List<ModeList> allModeListArr= new ArrayList<ModeList>();
				for (int j=0;j<resultList.size();j++){
					if (ModeListArr.size() == 0) {
						ModeListArr = MyApplication.getInstance().getWidgetDataBase()
								.getModeList();
					}
					for (ModeList modeList : ModeListArr) {
						if (modeList.getModeId() == resultList.get(j).getModeCode()) {
							//// 获取当前情景或者集合的的所有子列表
							allModeListArr.add(modeList);
						}
					}
				}
				int cmdIndex=0;
				//String keyword="+:+:,:，";
				//String[] LogicIf=keyword.split(":");
				String[] LogicIf={"&","|",",","，"};
				for (cmdIndex = 0; cmdIndex < LogicIf.length; cmdIndex++) {
					if(input.contains(LogicIf[cmdIndex])){
						break;
					}
				}
                int isMatch;
				if(cmdIndex<LogicIf.length){
					int ifMode=(cmdIndex<1)?0:1;
					String[] logicIfArr = input.split(LogicIf[cmdIndex]);
					for(int j=0;j<logicIfArr.length;j++){
						if(logicIfArr[j].length()>0) {
							for (Mode modeArr : resultList) {
								if (modeArr.getModeName().contains(logicIfArr[j])) {
									if (!listMode.contains(modeArr)) {
										listMode.add(modeArr);
									}
								}
							}
						}
					}
					for (ModeList model :allModeListArr) {
                        isMatch=0;
						for(int j=0;j<logicIfArr.length;j++) {
							if(logicIfArr[j].length()>0) {
								if (model.getDeviceName().contains(logicIfArr[j])) {
									/////// 包含设备名称
                                    isMatch++;
								}
								if (getLogicTitle(model).contains(logicIfArr[j])) {
									////// 包含逻辑指令
                                    isMatch++;
								}
								if (model.getRoomName().contains(logicIfArr[j])) {
									////// 房间名称
                                    isMatch++;
								}
							}
						}
						if(ifMode>0){
							//[self checkModeModel:model];
							if(isMatch>0) {
								for (Mode modeArr : resultList) {
									if (model.getModeId() == modeArr.getId()) {
										if (!listMode.contains(modeArr)) {
											listMode.add(modeArr);
										}
									}
								}
							}
						}else{
							if(isMatch>=logicIfArr.length) {
								for (Mode modeArr : resultList) {
									if (model.getModeId() == modeArr.getId()) {
										if (!listMode.contains(modeArr)) {
											listMode.add(modeArr);
										}
									}
								}
							}
						}
					}
				}else {
                    for (Mode modeArr : resultList) {
						if(modeArr.getModeName().contains(input)) {
							if(!listMode.contains(modeArr)) {
								listMode.add(modeArr);
							}
						}
					}
					for (ModeList model :allModeListArr) {
                        isMatch=0;
						if (model.getDeviceName().contains(input)) {
							/////// 包含设备名称
                            isMatch=1;
						}
						else if (getLogicTitle(model).contains(input)) {
							////// 包含逻辑指令
                            isMatch=1;
						}
						else if (model.getRoomName().contains(input)) {
               				////// 房间名称
                            isMatch=1;
						}
						if(isMatch>0){
                    		//[self checkModeModel:model];
							for (Mode modeArr : resultList) {
								if(model.getModeId()==modeArr.getId()) {
									if(!listMode.contains(modeArr)) {
										listMode.add(modeArr);
									}
								}
							}
						}
					}
				}
			} else{
				listMode=ModeFactory.getInstance().getModesByType(modeType,-1);
			}
			adapter = new ModeTabAdapter(activity,listMode);
			adapter.setOnItemClickListener(onItemClickListener);
			modeList.setSwipeMenuItemClickListener(menuItemClickListener);
			modeList.setAdapter(adapter);
		}

		@Override
		public void afterTextChanged(Editable editable) {
		}
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

	///// 获取逻辑指令文本
	String getLogicTitle(ModeList model){
		String deleyTime=model.getModeDelayed().replace("秒","");
		if(Integer.parseInt(deleyTime)>0){
			deleyTime ="延时" + deleyTime + "秒";
		}
		String modePeriod="";
		if((model.getModePeriod().length()>0) && (!model.getModePeriod().equals("00:00--00:00"))){
			modePeriod = model.getModePeriod();
		}
		String month="";
		int statrM=0;
		int endM=0;
		if(model.getBeginMonth().length()>0){
			statrM=Integer.valueOf(model.getBeginMonth().substring(0, 1), 16);
		}
		if(model.getEndMonth().length()>0){
			endM=Integer.valueOf(model.getEndMonth().substring(0, 1), 16);
		}
		if(statrM>0 && endM>0){
			month=model.getBeginMonth()+"~"+model.getEndMonth();
		}
		return model.getModeAction() + model.getModeFunction() + model.getModeTime() + "  " + deleyTime+ "   "+ modePeriod + "    " + month;
	}
}
