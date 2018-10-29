package com.zunder.smart.activity.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zunder.scrollview.widget.WheelAdapter;
import com.zunder.scrollview.widget.WheelView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.camera.CameraAddFragment;
import com.zunder.smart.activity.camera.CameraFragment;
import com.zunder.smart.activity.camera.CameraPlayFragment;
import com.zunder.smart.activity.mode.ModeAddFragment;
import com.zunder.smart.activity.mode.ModeDeviceFragment;
import com.zunder.smart.activity.mode.ModeFragment;
import com.zunder.smart.activity.mode.ModeListActionFragment;
import com.zunder.smart.activity.mode.ModeListFragment;
import com.zunder.smart.activity.procase.ProCaseAddFragment;
import com.zunder.smart.activity.procase.ProCaseFragment;
import com.zunder.smart.activity.safe.AnHongFragment;
import com.zunder.smart.activity.safe.SafeAddFragment;
import com.zunder.smart.activity.safe.SafeFragment;
import com.zunder.smart.activity.safe.SafeSetFragment;
import com.zunder.smart.activity.timmer.SafeTimer;
import com.zunder.smart.adapter.ModeMainAdapter;
import com.zunder.smart.dao.impl.WidgetDAOProxy;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.json.ProCaseUtils;
import com.zunder.smart.listener.OnItemClickListener;
import com.zunder.smart.listener.SafeListener;
import com.zunder.smart.menu.HomeMenu;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.model.Mode;
import com.zunder.smart.model.ProCase;
import com.zunder.smart.service.GateWayService;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.setting.ProjectUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TabMainActivity extends FragmentActivity implements OnClickListener{

	private SwipeMenuRecyclerView modeGrid;
	private RelativeLayout modeLayout;

	public ModeFragment modeFragment;
	public ModeAddFragment modeAddFragment;
	public ModeListFragment modeListFragment;
	public ModeDeviceFragment modeDeviceFragment;
	public ModeListActionFragment modeListActionFragment;
	public ProCaseFragment proCaseFragment;
	public ProCaseAddFragment proCaseAddFragment;
	public SafeSetFragment safeSetFragment;
	public SafeAddFragment safeAddFragment;
	public SafeFragment safeFragment;
    public CameraFragment cameraFragment;
    public CameraAddFragment cameraAddFragment;
    private ImageButton cameraButton;
    public AnHongFragment anHongFragment;
    public CameraPlayFragment cameraPlayFragment;


	private static TabMainActivity instance;
	List<Mode> listMode=new ArrayList <Mode>();
	ModeMainAdapter modeAdapter;
	private Button homeEdite;

	private HomeMenu rightButtonMenuAlert;

	WheelView mainScrollView;
	public static TabMainActivity getInstance() {
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_main);
		instance=this;
		init();
		List<String> list=new ArrayList <String>();
		list.add("安防");
		list.add("视频");
		WheelAdapter adapter=new WheelAdapter(list);
		mainScrollView.setAdapter(adapter);

		mainScrollView.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(int index,String itemName) {
				if(index==0){
					hideFragMent(13);
					showFragMent(12);
//					setBackCode();

				}else{

					List<GateWay> list=GateWayService.getInstance().getCameraList();
					if(list.size()>0){
						hideFragMent(12);
						showFragMent(13);
						GateWay gateWay=list.get(0);
						cameraPlayFragment.initData(Integer.parseInt(GateWayService.mp.get(gateWay.getGatewayID())));

					}else{
						ToastUtils.ShowError(instance,"没有添加摄像头",Toast.LENGTH_SHORT,true);
					}
				}
			}
		});
		mainScrollView.setCurrentItem(0);
		initFragment();
	}
	public void init(){
		modeLayout=(RelativeLayout)findViewById(R.id.modeLayout);
		modeLayout.setOnClickListener(this);

		mainScrollView = (WheelView) findViewById(R.id.mainScrollView);
		homeEdite=(Button)findViewById(R.id.homeEdite) ;

		cameraButton=(ImageButton)findViewById(R.id.cameraButton);
		cameraButton.setOnClickListener(this);


		modeGrid = (SwipeMenuRecyclerView) findViewById(R.id.modeGrid);
		modeGrid.setLayoutManager(new GridLayoutManager(instance, 3));// 布局管理器。
		modeGrid.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
		modeGrid.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
//		modeGrid.addItemDecoration(new ListViewDecoration());// 添加分割线。
		modeGrid.setLongPressDragEnabled(true);// 开启拖拽，就这么简单一句话。
//		modeGrid.setOnItemMoveListener(onItemMoveListener);// 监听拖拽，更新UI。
		homeEdite.setOnClickListener(this);

	}
	public void setAdapter() {
		listMode.clear();
		String fileName = ProjectUtils.getRootPath().getRootName();
		homeEdite.setText(fileName);
		listMode= ModeFactory.getInstance().getModesByType("FF",1);
		modeAdapter = new ModeMainAdapter(instance,listMode);
		modeAdapter.setOnItemClickListener(onItemClickListener);
		modeGrid.setAdapter(modeAdapter);
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

		proCaseFragment= (ProCaseFragment) getSupportFragmentManager()
				.findFragmentById(R.id.proCaseFragment);
		getSupportFragmentManager().beginTransaction().hide(proCaseFragment).commit();

		proCaseAddFragment= (ProCaseAddFragment) getSupportFragmentManager()
				.findFragmentById(R.id.proCaseAddFragment);
		getSupportFragmentManager().beginTransaction().hide(proCaseAddFragment).commit();

		safeFragment= (SafeFragment) getSupportFragmentManager()
				.findFragmentById(R.id.safeFragment);
		getSupportFragmentManager().beginTransaction().hide(safeFragment).commit();

		safeSetFragment= (SafeSetFragment) getSupportFragmentManager()
				.findFragmentById(R.id.safeSetFragment);
		getSupportFragmentManager().beginTransaction().hide(safeSetFragment).commit();

		safeAddFragment= (SafeAddFragment) getSupportFragmentManager()
				.findFragmentById(R.id.safeAddFragment);
		getSupportFragmentManager().beginTransaction().hide(safeAddFragment).commit();


        cameraFragment= (CameraFragment) getSupportFragmentManager()
                .findFragmentById(R.id.cameraFragment);
        getSupportFragmentManager().beginTransaction().hide(cameraFragment).commit();


        cameraAddFragment= (CameraAddFragment) getSupportFragmentManager()
                .findFragmentById(R.id.cameraAddFragment);
        getSupportFragmentManager().beginTransaction().hide(cameraAddFragment).commit();

		anHongFragment= (AnHongFragment) getSupportFragmentManager()
				.findFragmentById(R.id.anHongFragment);
//		getSupportFragmentManager().beginTransaction().hide(anHongFragment).commit();


		cameraPlayFragment= (CameraPlayFragment) getSupportFragmentManager()
				.findFragmentById(R.id.cameraPlayFragment);
		getSupportFragmentManager().beginTransaction().hide(cameraPlayFragment).commit();
	}

	public void setBackCode(){
		if(anHongFragment!=null&&!anHongFragment.isHidden()){
			anHongFragment.setBackCode();
		}
	}

	private void initRightButtonMenuAlert() {
		final List<ProCase> listFiles=new ArrayList <ProCase>();
		listFiles.addAll(ProCaseUtils.getInstance().getAll());
		listFiles.add(ProCaseUtils.getInstance().addManger());
		rightButtonMenuAlert = new HomeMenu(instance,listFiles);
		rightButtonMenuAlert.setListener(new OnRightMenuClickListener() {

			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub
				if(position==listFiles.size()-1){
					showFragMent(5);
					proCaseFragment.setAdpapter();
				}else{
					ProCase proCase=listFiles.get(position);
					if(proCase.getProCaseIndex()==1){
						ToastUtils.ShowError(instance,"已是当前专案", Toast.LENGTH_SHORT,true);
					}else {
						ProCaseUtils.getInstance().updateProCaseIndex(1,proCase.getProCaseKey());
						String fileName = proCase.getProCaseAlia();
						WidgetDAOProxy.instance = null;
						String rootPath = MyApplication.getInstance()
								.getRootPath()
								+ File.separator
								+ fileName
								+ File.separator + "homedatabases.db";
						ProjectUtils.getRootPath().setRootPath(rootPath);
						ProjectUtils.getRootPath().setRootName(proCase.getProCaseName());
						ProjectUtils.getRootPath().setRootImage(proCase.getProCaseImage());
						ProjectUtils.saveRootPath();
						homeEdite.setText(proCase.getProCaseName());
						MainActivity.getInstance().updateFresh();
					}
				}
				rightButtonMenuAlert.dismiss();
			}
		});
		rightButtonMenuAlert.show(homeEdite);
	}
	@Override
	public void onResume() {
		super.onResume();
		setAdapter();
		setBackCode();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.modeLayout:
				showFragMent(0);
				modeFragment.initAdapter("FF");
				break;
			case R.id.homeEdite:
				initRightButtonMenuAlert();
				break;
			case R.id.cameraButton:
				showFragMent(10);
				cameraFragment.changeList();
				break;
			default:
				break;
		}
	}
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(final int position) {
			if(listMode!=null&& listMode.size()>0) {
				final Mode mode = listMode.get(position);
				DialogAlert alert = new DialogAlert( TabMainActivity.getInstance());
				alert.init( TabMainActivity.getInstance().getString(R.string.tip),"是否打开["+mode.getModeName()+"]模式");

				alert.setSureListener(new DialogAlert.OnSureListener() {
					@Override
					public void onSure() {
						// TODO Auto-generated method stub
						//joe 安防布防
						SendCMD cmdsend = SendCMD.getInstance();
						cmdsend.sendCMD(0, mode.getModeName(), null);
					}
					@Override
					public void onCancle() {
						// TODO Auto-generated method stub
					}
				});
				alert.show();

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
			}else if(proCaseAddFragment!=null&&!proCaseAddFragment.isHidden()){
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_left,
								R.anim.slide_out_right)
						.hide(proCaseAddFragment).commit();
			}else if(proCaseFragment!=null&&!proCaseFragment.isHidden()){
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_left,
								R.anim.slide_out_right)
						.hide(proCaseFragment).commit();
			}else if(safeSetFragment!=null&&!safeSetFragment.isHidden()){
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_left,
								R.anim.slide_out_right)
						.hide(safeSetFragment).commit();
			}else if(safeAddFragment!=null&&!safeAddFragment.isHidden()){
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_left,
								R.anim.slide_out_right)
						.hide(safeAddFragment).commit();
			}else if(safeFragment!=null&&!safeFragment.isHidden()){
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_left,
								R.anim.slide_out_right)
						.hide(safeFragment).commit();
			}
			else if(cameraAddFragment!=null&&!cameraAddFragment.isHidden()){
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left,
                                R.anim.slide_out_right)
                        .hide(cameraAddFragment).commit();
            }
            else if(cameraFragment!=null&&!cameraFragment.isHidden()){
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_left,
                                R.anim.slide_out_right)
                        .hide(cameraFragment).commit();
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
			case 5:
				fragment=proCaseFragment;
				break;
			case 6:
				fragment=proCaseAddFragment;
				break;
			case 7:
				fragment=safeFragment;
				break;
			case 8:
				fragment=safeAddFragment;
				break;
			case 9:
				fragment=safeSetFragment;
				break;
            case 10:
                fragment=cameraFragment;
                break;
            case 11:
                fragment=cameraAddFragment;
                break;
			case 12:
				fragment=anHongFragment;
				break;
			case 13:
				fragment=cameraPlayFragment;
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
			case 5:
				fragment=proCaseFragment;
				break;
			case 6:
				fragment=proCaseAddFragment;
				break;
			case 7:
				fragment=safeFragment;
				break;
			case 8:
				fragment=safeAddFragment;
				break;
			case 9:
				fragment=safeSetFragment;
				break;
            case 10:
                fragment=cameraFragment;
                break;
            case 11:
                fragment=cameraAddFragment;
                break;
			case 12:
				fragment=anHongFragment;
				break;

			case 13:
				fragment=cameraPlayFragment;
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
				for (int i=0;i<12;i++) {
					hideFragMent(i);
				}
			}
	}
	public void setCamera(){
		if(cameraFragment!=null&&!cameraFragment.isHidden()){
			GateWayService.setCameraHandleListener(cameraFragment);
		}
	}
}
