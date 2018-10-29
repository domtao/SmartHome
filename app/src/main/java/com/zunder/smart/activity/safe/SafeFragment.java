package com.zunder.smart.activity.safe;

import android.app.Activity;
import android.mtp.MtpConstants;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.activity.main.TabMainActivity;
import com.zunder.smart.activity.main.TabModeActivity;
import com.zunder.smart.activity.main.TabMyActivity;
import com.zunder.smart.activity.popu.dialog.ActionViewWindow;
import com.zunder.smart.activity.popu.dialog.RoomViewWindow;
import com.zunder.smart.adapter.DeviceAdapter;
import com.zunder.smart.adapter.SafeAdapter;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.DeviceTypeFactory;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.dao.impl.factory.ModeListFactory;
import com.zunder.smart.dao.impl.factory.RoomFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.listener.OnItemEditeListener;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.Mode;

import java.util.ArrayList;
import java.util.List;

public class SafeFragment extends Fragment implements OnClickListener,View.OnTouchListener,CompoundButton.OnCheckedChangeListener{

	SwipeMenuRecyclerView gridView;
	private Activity activity;
	private TextView backTxt;
	private TextView titleTxt;
	private TextView editeTxt;
	private RadioButton sensorRadio;
	private RadioButton controlRadio;
	private int safeType=0;
	List<Device> listDevice=new ArrayList<Device>();
	SafeAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_safe, container,
				false);
		activity = getActivity();
		backTxt = (TextView) root.findViewById(R.id.backTxt);
		titleTxt = (TextView) root.findViewById(R.id.titleTxt);
		editeTxt = (TextView) root.findViewById(R.id.editeTxt);
		sensorRadio = (RadioButton) root.findViewById(R.id.sensorRadio);
		controlRadio = (RadioButton) root.findViewById(R.id.controlRadio);
		gridView = (SwipeMenuRecyclerView) root.findViewById(R.id.safeGrid);
		gridView.setLayoutManager(new GridLayoutManager(activity, 2));// 布局管理器。
		gridView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		gridView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		gridView.setSwipeMenuCreator(swipeMenuCreator);
		// 设置菜单Item点击监听。
		gridView.setSwipeMenuItemClickListener(menuItemClickListener);
//		gridView.addItemDecoration(new ListViewDecoration());// 添加分割线。
//		gridView.setLongPressDragEnabled(true);// 开启拖拽，就这么简单一句话。
		root.setOnTouchListener(this);
		sensorRadio.setOnCheckedChangeListener(this);
		controlRadio.setOnCheckedChangeListener(this);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		return root;
	}

	public void init(int safeType){
		if(safeType==0) {
			sensorRadio.setChecked(true);
		}else{
			controlRadio.setChecked(true);
		}
		listDevice=DeviceFactory.getInstance().getSafeDevice(safeType);
		adapter=new SafeAdapter(activity,listDevice);
		adapter.setOnItemClickListener(onItemClickListener);
		adapter.setOnItemEditeListener(onItemEditeListener);
		gridView.setAdapter(adapter);
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(final int position) {
//			if(safeType==0) {
				TabMainActivity.getInstance().showFragMent(9);
				TabMainActivity.getInstance().safeSetFragment.init(listDevice.get(position));
//			}
		}
	};
	private OnItemEditeListener onItemEditeListener=new OnItemEditeListener() {
		@Override
		public void onItemEditeClick(int position) {
			gridView.openRightMenu(position);
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.backTxt:
				TabMainActivity.getInstance().hideFragMent(7);
				break;
			case R.id.editeTxt:
				TabMainActivity.getInstance().showFragMent(8);
				TabMainActivity.getInstance().safeAddFragment.iniit(safeType,"Add",0);
				break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()){
			case R.id.sensorRadio:
				if(isChecked){
					safeType=0;
					sensorRadio.setTextSize(18);
					controlRadio.setTextSize(16);
					controlRadio.setChecked(false);
					editeTxt.setText("添加传感");
					init(0);
				}
				break;
			case R.id.controlRadio:
				if(isChecked){
					safeType=1;
					sensorRadio.setTextSize(16);
					controlRadio.setTextSize(18);
					sensorRadio.setChecked(false);
					editeTxt.setText("添加遥控");
					init(1);
				}
				break;
		}
	}
	private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
		@Override
		public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
			int hSize = getResources().getDimensionPixelSize(R.dimen.item_height110);
			int wSize = getResources().getDimensionPixelSize(R.dimen.item_width);
			SwipeMenuItem delItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.red)
					.setText(getString(R.string.delete))
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(delItem);// 添加一个按钮到右侧菜单。
			SwipeMenuItem wechatItem = new SwipeMenuItem(activity)
					.setBackgroundDrawable(R.color.green)
					.setText(getString(R.string.edit))
					.setWidth(wSize)
					.setHeight(hSize);
			swipeRightMenu.addMenuItem(wechatItem);// 添加一个按钮到右侧菜单。

		}
	};


			;
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
						DialogAlert alert = new DialogAlert(activity);
						//joe
						alert.init("",
								"是否删除["+device.getRoomName()+device.getDeviceName()+"]及关联的数据");
						alert.setSureListener(new DialogAlert.OnSureListener() {

							@Override
							public void onSure() {
								// TODO Auto-generated method stub
								int id = device.getId();
								int result = MyApplication.getInstance()
										.getWidgetDataBase().deleteDevice(id);
								int IO=Integer.valueOf(device.getDeviceID().substring(0,2),16);
								int modeLoop=0;
								if(IO<5){
									modeLoop=IO;
								}else if(IO<9){
									modeLoop=5;
								}else if(IO<25){
									modeLoop=0;
								}else if(IO<29){
									modeLoop=6;
								}else {
									modeLoop=7;
								}
								if (result > 0) {
									ToastUtils.ShowSuccess(activity,
											activity.getString(R.string.deleteSuccess),
											Toast.LENGTH_SHORT,true);
									Mode mode=ModeFactory.getInstance().getMode(251,modeLoop);
									if(mode!=null){
										MyApplication.getInstance()
												.getWidgetDataBase().deleteMode(mode.getId());
									}
									mode=ModeFactory.getInstance().getMode(250,modeLoop);
									if(mode!=null){
										MyApplication.getInstance()
												.getWidgetDataBase().deleteMode(mode.getId());
									}
									mode=ModeFactory.getInstance().getMode(IO+200,0);
									if(mode!=null){
										MyApplication.getInstance()
												.getWidgetDataBase().deleteMode(mode.getId());
									}
									mode=ModeFactory.getInstance().getMode(IO+200,7);
									if(mode!=null){
										MyApplication.getInstance()
												.getWidgetDataBase().deleteMode(mode.getId());
									}
									adapter.getItems().remove(adapterPosition);
									DeviceFactory.getInstance().delete(id);
									ModeFactory.getInstance().clearList();
									ModeListFactory.getInstance().clearList();
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
						TabMainActivity.getInstance().showFragMent(8);
						TabMainActivity.getInstance().safeAddFragment.iniit(safeType,"Edite",device.getId());
						break;
				}
			}
		}
	};
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(hidden){
			TabMainActivity.getInstance().setBackCode();
		}
		onDialogDis();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

	public void onDialogDis(){
	}
}
