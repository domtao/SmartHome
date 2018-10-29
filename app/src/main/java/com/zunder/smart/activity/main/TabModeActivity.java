package com.zunder.smart.activity.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.mode.ModeDeviceFragment;
import com.zunder.smart.activity.mode.ModeAddFragment;
import com.zunder.smart.activity.mode.ModeFragment;
import com.zunder.smart.activity.mode.ModeListActionFragment;
import com.zunder.smart.activity.mode.ModeListFragment;
import com.zunder.smart.adapter.ModeAdapter;
import com.zunder.smart.custom.view.PagerSlidingTabStrip;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.model.Mode;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.view.ListViewDecoration;

import java.util.List;

public class TabModeActivity extends FragmentActivity implements OnClickListener {
	
	ModeAdapter adapter;
	List<Mode> listMode;
	private TextView editeTxt;
	SwipeMenuRecyclerView gridView;

	public ModeFragment modeFragment;
	public ModeAddFragment modeAddFragment;
	public ModeListFragment modeListFragment;
	public ModeDeviceFragment modeDeviceFragment;
	public ModeListActionFragment modeListActionFragment;
	private static TabModeActivity instance;

	public static TabModeActivity getInstance() {
		return instance;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_mode);
		instance = this;
		editeTxt = (TextView) findViewById(R.id.editeTxt);
		editeTxt.setOnClickListener(this);
		gridView = (SwipeMenuRecyclerView) findViewById(R.id.modeGrid);
		gridView.setLayoutManager(new GridLayoutManager(instance, 3));// 布局管理器。
		gridView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		gridView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
//		gridView.addItemDecoration(new ListViewDecoration());// 添加分割线。
		gridView.setLongPressDragEnabled(true);// 开启拖拽，就这么简单一句话。
		gridView.setOnItemMoveListener(onItemMoveListener);// 监听拖拽，更新UI。
		initFragment();
//		setAdapter();
	}
	public void initFragment(){
		modeFragment= (ModeFragment) getSupportFragmentManager()
				.findFragmentById(R.id.modeFragment);
		getSupportFragmentManager().beginTransaction().hide(modeFragment).commit();
		modeAddFragment= (ModeAddFragment) getSupportFragmentManager()
				.findFragmentById(R.id.modeAddFragment);
		getSupportFragmentManager().beginTransaction().hide(modeAddFragment).commit();
		modeListFragment= (ModeListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.modeListFragment);
		getSupportFragmentManager().beginTransaction().hide(modeListFragment).commit();
		modeDeviceFragment= (ModeDeviceFragment) getSupportFragmentManager()
				.findFragmentById(R.id.modeDeviceFragment);
		getSupportFragmentManager().beginTransaction().hide(modeDeviceFragment).commit();
		modeListActionFragment= (ModeListActionFragment) getSupportFragmentManager()
				.findFragmentById(R.id.modeListActionFragment);
		getSupportFragmentManager().beginTransaction().hide(modeListActionFragment).commit();
	}
	@Override
	public void onResume() {
		super.onResume();
        setAdapter();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.editeTxt:
				showFragMent(0);
				modeFragment.initAdapter("C9");
				break;
			default:
				break;
		}
	}
	public void setAdapter() {
		listMode= ModeFactory.getInstance().getModesByType("C9",1);
		adapter = new ModeAdapter(instance,listMode);
		adapter.setOnItemClickListener(onItemClickListener);
		gridView.setAdapter(adapter);
	}

	public static IWidgetDAO sql() {
		return MyApplication.getInstance().getWidgetDataBase();
	}

	private OnItemMoveListener onItemMoveListener = new OnItemMoveListener() {
		@Override
		public boolean onItemMove(int fromPosition, int toPosition) {
//			if (fromPosition == listMode.size() - 1 || toPosition == listMode.size() - 1) {
//
//
//				ToastUtils.ShowError(instance,getString(R.string.s_sort),Toast.LENGTH_SHORT,true);
//				return true;
//			}
			Mode _from = listMode.get(fromPosition);
			if (fromPosition < toPosition) {
				listMode.add(toPosition + 1, _from);
				listMode.remove(fromPosition);
			} else if (fromPosition > toPosition) {
				listMode.add(toPosition, _from);
				listMode.remove(fromPosition + 1);
			}
			adapter.notifyItemMoved(fromPosition, toPosition);
			sql().updateModeSort(listMode);
			ModeFactory.getInstance().clearList();
			if (PagerSlidingTabStrip.currentPosition == 0) {

			}

			return true;
		}

		@Override
		public void onItemDismiss(int position) {
			// 当Item被滑动删除掉的时候，在这里是无效的，因为这里没有启用这个功能。
			// 使用Menu时就不用使用这个侧滑删除啦，两个是冲突的。

		}
	};
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(final int position) {
			if(listMode!=null&&listMode.size()>0&&position<listMode.size()) {
				final Mode mode = listMode.get(position);
				SendCMD cmdsend = SendCMD.getInstance();
				cmdsend.sendCMD(0, mode.getModeName(), null);
				ToastUtils.ShowSuccess(instance, mode.getModeName() + "模式", Toast.LENGTH_SHORT, true);
			}else{
				listMode= ModeFactory.getInstance().getModesByType("C9",1);
				adapter = new ModeAdapter(instance,listMode);
				adapter.setOnItemClickListener(onItemClickListener);
				gridView.setAdapter(adapter);
			}
	}
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
			if (keyCode == event.KEYCODE_BACK) {
				if(modeListActionFragment!=null&&!modeListActionFragment.isHidden()){
					getSupportFragmentManager()
							.beginTransaction()
							.setCustomAnimations(R.anim.slide_in_left,
									R.anim.slide_out_right)
							.hide(modeListActionFragment).commit();
				}else if(modeDeviceFragment!=null&&!modeDeviceFragment.isHidden()){
					getSupportFragmentManager()
							.beginTransaction()
							.setCustomAnimations(R.anim.slide_in_left,
									R.anim.slide_out_right)
							.hide(modeDeviceFragment).commit();
				}else	if(modeListFragment!=null&&!modeListFragment.isHidden()){
					getSupportFragmentManager()
							.beginTransaction()
							.setCustomAnimations(R.anim.slide_in_left,
									R.anim.slide_out_right)
							.hide(modeListFragment).commit();
				}else if(modeAddFragment!=null&&!modeAddFragment.isHidden()){
					getSupportFragmentManager()
							.beginTransaction()
							.setCustomAnimations(R.anim.slide_in_left,
									R.anim.slide_out_right)
							.hide(modeAddFragment).commit();
				}else if(modeFragment!=null&&!modeFragment.isHidden()){
					getSupportFragmentManager()
							.beginTransaction()
							.setCustomAnimations(R.anim.slide_in_left,
									R.anim.slide_out_right)
							.hide(modeFragment).commit();
				}
				return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	public void showFragMent(int index){
		Fragment fragment=null;
		switch (index){
			case 0:
				fragment=modeFragment;
				break;
			case 1:
				fragment=modeAddFragment;
				break;
			case 2:
				fragment=modeListFragment;
				break;
			case 3:
				fragment=modeDeviceFragment;
				break;
			case 4:
				fragment=modeListActionFragment;
				break;
			}
		if(fragment!=null) {
			getSupportFragmentManager()
					.beginTransaction()
					.setCustomAnimations(R.anim.slide_in_left,
							R.anim.slide_out_right)
					.show(fragment).commit();
		}
	}
	public void hideFragMent(int index){
		Fragment fragment=null;
		switch (index){
			case 0:
				fragment=modeFragment;
				break;
			case 1:
				fragment=modeAddFragment;
				break;
			case 2:
				fragment=modeListFragment;
				break;
			case 3:
				fragment=modeDeviceFragment;
				break;
			case 4:
				fragment=modeListActionFragment;
				break;
		}
		if(fragment!=null&&!fragment.isHidden()) {
			getSupportFragmentManager()
					.beginTransaction()
					.setCustomAnimations(R.anim.slide_in_left,
							R.anim.slide_out_right)
					.hide(fragment).commit();
		}
	}

	public void hiFragment(){
		if(instance!=null){
			for (int i=0;i<4;i++) {
				hideFragMent(i);
			}
		}
	}

}
