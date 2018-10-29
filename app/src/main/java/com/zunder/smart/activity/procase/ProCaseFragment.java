package com.zunder.smart.activity.procase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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

import com.door.Utils.ToastUtils;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.backup.BackClientActivity;
import com.zunder.smart.activity.backup.BackUpActivity;
import com.zunder.smart.activity.LoginActivity;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.activity.main.TabHomeActivity;
import com.zunder.smart.activity.main.TabMainActivity;
import com.zunder.smart.adapter.ProCaseAdapter;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.WidgetDAOProxy;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.dialog.TipAlert;
import com.zunder.smart.json.ProCaseUtils;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.menu.RightMenu;
import com.zunder.smart.model.ProCase;
import com.zunder.smart.setting.ProjectUtils;
import com.zunder.smart.setting.SmartFileUtils;
import com.zunder.smart.view.ListViewDecoration;

import java.io.File;
import java.util.List;

public class ProCaseFragment extends Fragment implements OnClickListener,View.OnTouchListener {

	ProCaseAdapter adapter;
	private Activity activity;
	private TextView backTxt;
	private TextView editeTxt;
	List<ProCase> listProCase;
	SwipeMenuRecyclerView listView;
	private RightMenu rightButtonMenuAlert;
	private Button addBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_pro_case, container,
				false);
		activity=getActivity();
		backTxt = (TextView) root.findViewById(R.id.backTxt);
		editeTxt = (TextView) root.findViewById(R.id.editeTxt);
		addBtn=(Button)root.findViewById(R.id.addBtn);
		addBtn.setOnClickListener(this);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);

		listView = (SwipeMenuRecyclerView) root.findViewById(R.id.caseList);
		listView.setLayoutManager(new LinearLayoutManager(activity));// 布局管理器。
		listView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		listView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
		listView.addItemDecoration(new ListViewDecoration());// 添加分割线。
		// 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
		// 设置菜单创建器。
		listView.setSwipeMenuCreator(swipeMenuCreator);
		// 设置菜单Item点击监听。
		listView.setSwipeMenuItemClickListener(menuItemClickListener);
		initRightButtonMenuAlert();
		root.setOnTouchListener(this);
		return root;
	}
	@SuppressLint("ResourceAsColor")
	private void initRightButtonMenuAlert() {
		rightButtonMenuAlert = new RightMenu(activity, R.array.right_procase,
				R.drawable.right_backup_images);
		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {
			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub
				switch (position) {
					case 0:
						if(MyApplication.getInstance().isLogin()==0){
							TipAlert alert = new TipAlert(activity,
									getString(R.string.tip), getString(R.string.login_account));
							alert.setSureListener(new TipAlert.OnSureListener() {
								@Override
								public void onSure() {
									LoginActivity.startActivity(activity);
								}
							});
							alert.show();
						}else {
							Intent intent = new Intent(activity,
									BackUpActivity.class);
							startActivity(intent);
						}
						break;
					case 1:
						if(MyApplication.getInstance().isLogin()==0){
							TipAlert alert = new TipAlert(activity,
									getString(R.string.tip), getString(R.string.login_account));
							alert.setSureListener(new TipAlert.OnSureListener() {
								@Override
								public void onSure() {
									LoginActivity.startActivity(activity);
								}
							});
							alert.show();
						}else {
							Intent intent = new Intent(activity,
									BackClientActivity.class);
							startActivity(intent);
						}
						break;
				}
				rightButtonMenuAlert.dismiss();
			}
		});
	}
	public void setAdpapter() {
		listProCase= ProCaseUtils.getInstance().getAll();
		adapter = new ProCaseAdapter(activity, listProCase);
		adapter.setOnItemClickListener(onItemClickListener);
		listView.setAdapter(adapter);
	}
	public static IWidgetDAO sql() {
		return MyApplication.getInstance().getWidgetDataBase();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backTxt:
			if(MainActivity.getInstance().mHost.getCurrentTab()==0) {
				TabMainActivity.getInstance().hideFragMent(5);
			}else{
				TabHomeActivity.getInstance().hideFragMent(5);
			}
			break;
		case R.id.editeTxt:
			rightButtonMenuAlert.show(editeTxt);
			break;
			case R.id.addBtn:
				if(MainActivity.getInstance().mHost.getCurrentTab()==0) {
					TabMainActivity.getInstance().showFragMent(6);
					TabMainActivity.getInstance().proCaseAddFragment.setInit("Add", null);
				}else{
					TabHomeActivity.getInstance().showFragMent(6);
					TabHomeActivity.getInstance().proCaseAddFragment.setInit("Add", null);
				}
				break;
		default:
			break;
		}

	}


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


		}
	};
	private com.zunder.smart.popwindow.listener.OnItemClickListener onItemClickListener=new com.zunder.smart.popwindow.listener.OnItemClickListener(){
		@Override
		public void onItemClick(int pos) {

			if(listProCase.get(pos).getProCaseAlia().equals("Root")){
				ToastUtils.ShowError(activity,"系统默认家庭,不能修改",Toast.LENGTH_SHORT,true);
				return;
			}
			if(MainActivity.getInstance().mHost.getCurrentTab()==0) {


				TabMainActivity.getInstance().showFragMent(6);
				TabMainActivity.getInstance().proCaseAddFragment.setInit("Edite", listProCase.get(pos));
			}else{
				TabHomeActivity.getInstance().showFragMent(6);
				TabHomeActivity.getInstance().proCaseAddFragment.setInit("Edite", listProCase.get(pos));
			}
		}
	};

	public OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
		@Override
		public void onItemClick(Closeable closeable, final int adapterPosition, int menuPosition, int direction) {
			closeable.smoothCloseMenu();// 关闭被点击的菜单。
			if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {

				switch (menuPosition){
					case 0:
						final ProCase proCase=listProCase.get(adapterPosition);
						if(proCase.getProCaseAlia().equals("Root")){
							ToastUtils.ShowError(activity,"系统默认家庭,不能删除",Toast.LENGTH_SHORT,true);
							return;
						}
						DialogAlert alert = new DialogAlert(activity);
						alert.init(proCase.getProCaseName(),"删除家庭及所有文件");
						alert.setSureListener(new DialogAlert.OnSureListener() {

							@Override
							public void onSure() {
								// TODO Auto-generated method stub

								SmartFileUtils.RecursionDeleteFile(new File(
										MyApplication.getInstance().getRootPath()
												+ File.separator + proCase.getProCaseAlia()));
								SmartFileUtils.delFile( proCase.getProCaseAlia());
								ToastUtils.ShowSuccess(activity,
										activity.getString(R.string.deleteSuccess),
										Toast.LENGTH_SHORT,true);
								ProCaseUtils.getInstance().delProCase(proCase.getProCaseKey());
								if(proCase.getProCaseIndex()==1){
									WidgetDAOProxy.instance = null;
									String rootPath = MyApplication.getInstance()
											.getRootPath()
											+ File.separator+"Root"
											+ File.separator + "homedatabases.db";
									ProjectUtils.getRootPath().setRootPath(rootPath);
									ProjectUtils.getRootPath().setRootName("默认家庭");
									ProjectUtils.saveRootPath();
									ProCaseUtils.getInstance().updateProCaseIndex(1,"20180801120000");
									MainActivity.getInstance().updateFresh();
								}
								setAdpapter();
							}

							@Override
							public void onCancle() {
								// TODO Auto-generated method stub

							}
						});
						alert.show();
						break;
					case 1:
//						TabHomeActivity.getInstance().showFragMent(6);
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
