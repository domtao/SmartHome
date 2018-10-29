package com.zunder.smart.activity.device;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.activity.main.TabHomeActivity;
import com.zunder.smart.activity.main.TabMyActivity;
import com.zunder.smart.adapter.DeviceTypeAdapter;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.factory.DeviceTypeFactory;
import com.zunder.smart.dialog.TypeAlert;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.DeviceType;
import com.zunder.smart.view.ListViewDecoration;

import java.util.List;

public class DeviceTypeFragment extends Fragment implements OnClickListener,View.OnTouchListener{

	SwipeMenuRecyclerView swipeMenuRecyclerView;
	DeviceTypeAdapter adapter;
	List<DeviceType> listDeviceType;
	private Activity activity;
	private TextView backTxt;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_device_type, container,
				false);
		activity = getActivity();

		backTxt = (TextView) root.findViewById(R.id.backTxt);
		backTxt.setOnClickListener(this);
		root.setOnTouchListener(this);
		swipeMenuRecyclerView = (SwipeMenuRecyclerView) root.findViewById(R.id.deviceTypeGrid);
		swipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(activity));// 布局管理器。
		swipeMenuRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		swipeMenuRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		swipeMenuRecyclerView.addItemDecoration(new ListViewDecoration());// 添加分割线。
		// 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：

		swipeMenuRecyclerView.setLongPressDragEnabled(true);// 开启拖拽，就这么简单一句话。
		swipeMenuRecyclerView.setOnItemMoveListener(onItemMoveListener);// 监听拖拽，更新UI。

//		setAdpapter();
		return root;
	}
	private OnItemMoveListener onItemMoveListener = new OnItemMoveListener() {
		@Override
		public boolean onItemMove(int fromPosition, int toPosition) {
			// 当Item被拖拽的时候。
			DeviceType _from = listDeviceType.get(fromPosition);
			if (fromPosition < toPosition) {
				listDeviceType.add(toPosition + 1, _from);
				listDeviceType.remove(fromPosition);
			} else if (fromPosition > toPosition) {
				listDeviceType.add(toPosition, _from);
				listDeviceType.remove(fromPosition + 1);
			}
			adapter.notifyItemMoved(fromPosition, toPosition);
			sql().updateDeviceTypeSort(listDeviceType);
			DeviceTypeFactory.getInstance().clearList();
			TabHomeActivity.getInstance().deviceTypeChange();
			return true;// 返回true表示处理了，返回false表示你没有处理。
		}
		@Override
		public void onItemDismiss(int position) {
			// 当Item被滑动删除掉的时候，在这里是无效的，因为这里没有启用这个功能。
			// 使用Menu时就不用使用这个侧滑删除啦，两个是冲突的。
		}
	};
	public void setAdpapter() {
		listDeviceType = DeviceTypeFactory.getInstance().getDeviceTypes(1);
		adapter = new DeviceTypeAdapter(activity,listDeviceType);
		adapter.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(int position) {
				TabHomeActivity.getInstance().setSelect(position);
				TabHomeActivity.getInstance().hideFragMent(7);
			}
		});
		swipeMenuRecyclerView.setAdapter(adapter);
	}

	public static IWidgetDAO sql() {
		return MyApplication.getInstance().getWidgetDataBase();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			TabHomeActivity.getInstance().hideFragMent(7);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}
}
