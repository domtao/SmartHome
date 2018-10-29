package com.zunder.smart.activity.room;

import java.util.Collections;
import java.util.List;

import com.door.Utils.ToastUtils;
import com.iflytek.cloud.thirdparty.V;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.centercontrol.CenterControlActivity;
import com.zunder.smart.activity.main.TabHomeActivity;
import com.zunder.smart.adapter.RoomAdapter;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.factory.RmcBeanFactory;
import com.zunder.smart.dao.impl.factory.RoomFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.model.Room;
import com.zunder.smart.view.ListViewDecoration;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RoomFragment extends Fragment implements OnClickListener,View.OnTouchListener {

	RoomAdapter adapter;
	private Activity activity;
	private TextView backTxt;
	private TextView editeTxt;
	List<Room> listRoom;
	SwipeMenuRecyclerView listView;
	private Button moreBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_room, container,
				false);
		activity=getActivity();
		backTxt = (TextView) root.findViewById(R.id.backTxt);
		editeTxt = (TextView) root.findViewById(R.id.editeTxt);
		moreBtn=(Button)root.findViewById(R.id.moreBtn);
		moreBtn.setOnClickListener(this);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		listView = (SwipeMenuRecyclerView) root.findViewById(R.id.roomList);
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
		root.setOnTouchListener(this);
		return root;
	}
	public void setAdpapter() {
		listRoom= RoomFactory.getInstance().getRoom(-1);
		adapter = new RoomAdapter(activity, listRoom);
		adapter.setOnItemClickListener(onItemClickListener);
		listView.setAdapter(adapter);
		if(RoomFactory.getInstance().getRoomControl()==1){
			moreBtn.setVisibility(View.VISIBLE);
		}else{
			moreBtn.setVisibility(View.GONE);
		}
	}

	public static IWidgetDAO sql() {
		return MyApplication.getInstance().getWidgetDataBase();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			TabHomeActivity.getInstance().hideFragMent(1);
			break;
		case R.id.editeTxt:
			TabHomeActivity.getInstance().showFragMent(2);
			TabHomeActivity.getInstance().roomAddFragment.setInit("Add",null);
			break;
		case R.id.moreBtn:
			CenterControlActivity.startActivity(activity);
			break;
		default:
			break;
		}
	}

	private OnItemMoveListener onItemMoveListener = new OnItemMoveListener() {
		@Override
		public boolean onItemMove(int fromPosition, int toPosition) {
			// 当Item被拖拽的时候。
			if(listRoom!=null) {
				Collections.swap(listRoom, fromPosition, toPosition);
				adapter.notifyItemMoved(fromPosition, toPosition);
				MyApplication.getInstance().getWidgetDataBase()
						.updateRoomSort(listRoom);
				RoomFactory.getInstance().clearList();
				TabHomeActivity.getInstance().initScrollView();
			}
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
			int hSize = getResources().getDimensionPixelSize(R.dimen.item_height50);
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

	private com.zunder.smart.popwindow.listener.OnItemClickListener onItemClickListener=new com.zunder.smart.popwindow.listener.OnItemClickListener(){

		@Override
		public void onItemClick(int pos) {
			TabHomeActivity.getInstance().showFragMent(2);
			TabHomeActivity.getInstance().roomAddFragment.setInit("Edite",listRoom.get(pos));
		}
	};

	public OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
		@Override
		public void onItemClick(Closeable closeable, final int adapterPosition, int menuPosition, int direction) {
			closeable.smoothCloseMenu();// 关闭被点击的菜单。
			if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {

				switch (menuPosition){
					case 0:
						DialogAlert alert = new DialogAlert(activity);
						alert.init(listRoom.get(adapterPosition).getRoomName(),
								activity.getString(R.string.isDelArce));
						alert.setSureListener(new DialogAlert.OnSureListener() {

							@Override
							public void onSure() {
								// TODO Auto-generated method stub
								int result = MyApplication.getInstance()
										.getWidgetDataBase()
										.deleteRoom(listRoom.get(adapterPosition).getId());
								if (result > 0) {
									MyApplication.getInstance()
											.getWidgetDataBase()
											.deleteRMCBeanByRoomId(listRoom.get(adapterPosition).getId());
									RmcBeanFactory.getInstance().clearList();
									RoomFactory.getInstance().clearList();
									ToastUtils.ShowSuccess(activity,
											activity.getString(R.string.deleteSuccess),
											Toast.LENGTH_SHORT,true);
									listRoom.remove(adapterPosition);
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
