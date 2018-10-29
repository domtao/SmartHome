package com.zunder.smart.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;


import com.door.Utils.ToastUtils;
import com.zunder.image.view.SmartImageView;
import com.zunder.scrollview.widget.WheelAdapter;
import com.zunder.scrollview.widget.WheelDeviceAdapter;
import com.zunder.scrollview.widget.WheelRoomDeviceAdapter;
import com.zunder.scrollview.widget.WheelView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.device.DeviceAllFragment;
import com.zunder.smart.activity.device.DeviceTypeFragment;
import com.zunder.smart.activity.home.DimmerFragment;
import com.zunder.smart.activity.home.MainFragment;
import com.zunder.smart.activity.procase.ProCaseAddFragment;
import com.zunder.smart.activity.procase.ProCaseFragment;
import com.zunder.smart.activity.room.RoomAddFragment;
import com.zunder.smart.activity.tv.ChannelAddActivity;
import com.zunder.smart.dao.impl.WidgetDAOProxy;
import com.zunder.smart.dao.impl.factory.RoomFactory;
import com.zunder.smart.activity.room.RoomFragment;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.DeviceTypeFactory;
import com.zunder.smart.json.Constants;
import com.zunder.smart.json.ProCaseUtils;
import com.zunder.smart.menu.HomeMenu;
import com.zunder.smart.menu.OnRightMenuClickListener;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.DeviceType;
import com.zunder.smart.model.ProCase;
import com.zunder.smart.model.Room;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.setting.ProjectUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TabHomeActivity extends FragmentActivity implements OnClickListener,CompoundButton.OnCheckedChangeListener {

	WheelView roomScroll;
	WheelView deviceScroll;
	public static List<String> ListRoom = new ArrayList<String>();
	public static List<Device> ListDevice = new ArrayList<Device>();
	private ImageButton roomButton,deviceButton;
	private Activity activity;
	private RadioButton roomRadio,deviceRadio;
	public RoomFragment roomFragment;
	public RoomAddFragment roomAddFragment;
	public DeviceAllFragment deviceAllFragment;
	public DeviceTypeFragment deviceTypeFragment;
	public MainFragment mainFragment;
	public DimmerFragment dimmerFragment;
	public ProCaseFragment proCaseFragment;
	public ProCaseAddFragment proCaseAddFragment;
	private static TabHomeActivity instance;
	private int roomId=0;
	private int deviceTypeKey=0;
	private Button homeEdite;
	private HomeMenu rightButtonMenuAlert;
	SmartImageView smartImageView;
	private ImageView setImage;
	public static TabHomeActivity getInstance() {
		return instance;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_home);
		activity = this;
		instance=this;
		homeEdite=(Button)findViewById(R.id.homeEdite);
		homeEdite.setOnClickListener(this);
		roomScroll = (WheelView) findViewById(R.id.roomScrollView);
		deviceScroll = (WheelView) findViewById(R.id.deviceScrollView);
		roomButton = (ImageButton) findViewById(R.id.roomButton);
		deviceButton = (ImageButton) findViewById(R.id.deviceButton);
		roomRadio=(RadioButton)findViewById(R.id.roomRadio);
		deviceRadio=(RadioButton)findViewById(R.id.deviceRadio);
		smartImageView=(SmartImageView)findViewById(R.id.smartImageView) ;
		setImage=(ImageView)findViewById(R.id.setImage);
		setImage.setOnClickListener(this);
		roomRadio.setOnCheckedChangeListener(this);
		deviceRadio.setOnCheckedChangeListener(this);
		roomButton.setOnClickListener(this);
		deviceButton.setOnClickListener(this);
		initFragment();
		handler.postAtTime(new Runnable() {
			@Override
			public void run() {
				initScrollView();
			}
		},1000);
	}
	public void initFragment(){
		roomFragment= (RoomFragment) getSupportFragmentManager()
				.findFragmentById(R.id.roomFragment);
		getSupportFragmentManager().beginTransaction().hide(roomFragment).commit();
		roomAddFragment= (RoomAddFragment) getSupportFragmentManager()
				.findFragmentById(R.id.roomAddFragment);
		getSupportFragmentManager().beginTransaction().hide(roomAddFragment).commit();
		deviceAllFragment= (DeviceAllFragment) getSupportFragmentManager()
				.findFragmentById(R.id.deviceAllFragment);
		getSupportFragmentManager().beginTransaction().hide(deviceAllFragment).commit();
		deviceTypeFragment= (DeviceTypeFragment) getSupportFragmentManager()
				.findFragmentById(R.id.deviceTypeFragment);
		getSupportFragmentManager().beginTransaction().hide(deviceTypeFragment).commit();

		mainFragment= (MainFragment) getSupportFragmentManager()
				.findFragmentById(R.id.mainFragment);
		getSupportFragmentManager().beginTransaction().hide(mainFragment).commit();

		dimmerFragment= (DimmerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.dimmerFragment);
		getSupportFragmentManager().beginTransaction().hide(dimmerFragment).commit();

		proCaseFragment= (ProCaseFragment) getSupportFragmentManager()
				.findFragmentById(R.id.proCaseFragment);
		getSupportFragmentManager().beginTransaction().hide(proCaseFragment).commit();

		proCaseAddFragment= (ProCaseAddFragment) getSupportFragmentManager()
				.findFragmentById(R.id.proCaseAddFragment);
		getSupportFragmentManager().beginTransaction().hide(proCaseAddFragment).commit();

	}
	public void initScrollView(){
		String fileName = ProjectUtils.getRootPath().getRootName();
		homeEdite.setText(fileName);
		smartImageView.setImageUrl(Constants.HTTPS+ProjectUtils.getRootPath().getRootImage());

		roomScroll.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(int index,String itemName) {
				if(roomRadio.isChecked()){
					Room room= RoomFactory.getInstance().getRoomByName(ListRoom.get(index));
					if(room!=null){
						roomId=room.getId();
						ChannelAddActivity.roomId=roomId;
						ListDevice=DeviceFactory.getInstance().getDevices(roomId,1);
						WheelDeviceAdapter adapter=new WheelDeviceAdapter(ListDevice);
						deviceScroll.setAdapter(adapter);
						if(ListDevice.size()>0) {
							deviceScroll.setCurrentItem(0);
						}else{
							if(MainActivity.getInstance().mHost.getCurrentTab()==1) {
								hideFragMent(0);
							}
						}
					}
				}else if(deviceRadio.isChecked()){
					DeviceType deviceType=DeviceTypeFactory.getInstance().getByName(ListRoom.get(index));
					if(deviceType!=null){
						ChannelAddActivity.roomId=0;
						deviceTypeKey=deviceType.getDeviceTypeKey();
						ListDevice=DeviceFactory.getInstance().getDeviceByTypeKey(deviceTypeKey);
						WheelRoomDeviceAdapter adapter=new WheelRoomDeviceAdapter(ListDevice);
						deviceScroll.setAdapter(adapter);
						if(ListDevice.size()>0) {
							deviceScroll.setCurrentItem(0);
						}else{
							if(MainActivity.getInstance().mHost.getCurrentTab()==1) {
								hideFragMent(0);
							}
						}
					}
				}
			}
		});

		deviceScroll.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(int index,String itemName) {
				if (ListDevice != null && index < ListDevice.size()) {
					Device device = ListDevice.get(index);
					if (device == null) {
						if (MainActivity.getInstance().mHost.getCurrentTab() == 1) {
							hideFragMent(0);
							hideFragMent(4);
						}
					} else {

						if (device.getDeviceTypeKey() == 7) {
							if (mainFragment != null && !mainFragment.isHidden()) {
								hideFragMent(0);
							}
							if (MainActivity.getInstance().mHost.getCurrentTab() == 1) {
								showFragMent(4);
								dimmerFragment.init(device);
							}
						} else {
							if (device.getDeviceTypeKey() == 9) {
								setImage.setVisibility(View.GONE);
							} else {
								setImage.setVisibility(View.VISIBLE);
							}
							if (dimmerFragment != null && !dimmerFragment.isHidden()) {
								hideFragMent(4);
							}
							if (MainActivity.getInstance().mHost.getCurrentTab() == 1) {
								showFragMent(0);
								mainFragment.init(device);
							}
						}
					}
				}
			}
		});
        //joe
        if(roomRadio.isChecked()){
			deviceTypeKey=0;
			roomRadio.setTextSize(20);
			deviceRadio.setTextSize(16);
			deviceRadio.setChecked(false);
			ListRoom= RoomFactory.getInstance().getRoomName(1);
			WheelAdapter adapter=new WheelAdapter(ListRoom);
			roomScroll.setAdapter(adapter);
			if(ListRoom.size()>0) {
				roomScroll.setCurrentItem(0);
			}
		}else {
			roomRadio.setChecked(true);
		}
    }
	public void  RoomChange()
	{
		deviceTypeKey=0;
		roomRadio.setTextSize(20);
		deviceRadio.setTextSize(16);
		deviceRadio.setChecked(false);
		ListRoom=RoomFactory.getInstance().getRoomName(1);
		WheelAdapter adapter=new WheelAdapter(ListRoom);
		roomScroll.setAdapter(adapter);
		roomScroll.setCurrentItem(0);
	}

	public void deviceTypeChange(){
		ListRoom= DeviceTypeFactory.getInstance().getDeviceTypeNames(1);
		WheelAdapter adapter=new WheelAdapter(ListRoom);
		roomScroll.setAdapter(adapter);
		roomScroll.setCurrentItem(0);
	}

	public void setSelect(int select){
		deviceScroll.setCurrentItem(select);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	if(mainFragment!=null&&!mainFragment.isHidden()) {
		mainFragment.setDeviceStateListener();
	}else	if(dimmerFragment!=null&&!dimmerFragment.isHidden()) {
		dimmerFragment.setDeviceStateListener();
	}

	}

	private Handler handler=new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
        	super.dispatchMessage(msg);
        }
    };

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	private void initRightButtonMenuAlert() {
		final List<ProCase> listFiles=new ArrayList <ProCase>();
		listFiles.addAll(ProCaseUtils.getInstance().getAll());
		listFiles.add(ProCaseUtils.getInstance().addManger());
		rightButtonMenuAlert = new HomeMenu(activity,listFiles);
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
						ToastUtils.ShowError(activity,"已是当前专案", Toast.LENGTH_SHORT,true);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.roomButton:
			if(roomRadio.isChecked()) {
				showFragMent(1);
				roomFragment.setAdpapter();
			}else if(deviceRadio.isChecked()){
				showFragMent(7);
				deviceTypeFragment.setAdpapter();
			}
			break;
		case R.id.deviceButton:
			showFragMent(3);
			deviceAllFragment.initAdapter(roomId,deviceTypeKey);
			break;
		case R.id.homeEdite:
			initRightButtonMenuAlert();
			break;
		case R.id.setImage:
			if(mainFragment!=null&&!mainFragment.isHidden()) {
				mainFragment.setActivity();
			}else	if(dimmerFragment!=null&&!dimmerFragment.isHidden()) {
				dimmerFragment.setActivity();
			}
			break;
		default:
			break;
		}
	}
	//joe 灯光全开
	public void AllOpen(){
		List<Device> listDevice = DeviceFactory.getInstance().getDeviceByAction(roomId, deviceTypeKey, -1,1);
		for (int i=0;i<listDevice.size();i++){
			Device device=listDevice.get(i);
			SendCMD.getInstance().sendCMD(250, device.getRoomName()+ device.getDeviceName() +getString(R.string.open_1) , device);
		}
	}
	//joe 灯光全关
	public void AllClose(){
		List<Device> listDevice = DeviceFactory.getInstance().getDeviceByAction(roomId, deviceTypeKey, -1,1);
		for (int i=0;i<listDevice.size();i++){
			Device device=listDevice.get(i);
			SendCMD.getInstance().sendCMD(250, device.getRoomName()+device.getDeviceName() +getString(R.string.close_1) , device);
		}

	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()){
			case R.id.roomRadio:
				if(isChecked){
					deviceTypeKey=0;
					roomRadio.setTextSize(20);
					deviceRadio.setTextSize(16);
					deviceRadio.setChecked(false);
					ListRoom= RoomFactory.getInstance().getRoomName(1);
					WheelAdapter adapter=new WheelAdapter(ListRoom);
					roomScroll.setAdapter(adapter);
					roomScroll.setCurrentItem(0);
				}
				break;
			case R.id.deviceRadio:
				if(isChecked){
					roomId=0;
					roomRadio.setTextSize(16);
					deviceRadio.setTextSize(20);
					roomRadio.setChecked(false);
					ListRoom= DeviceTypeFactory.getInstance().getDeviceTypeNames(1);
					WheelAdapter adapter=new WheelAdapter(ListRoom);
					roomScroll.setAdapter(adapter);
					roomScroll.setCurrentItem(0);
				}
				break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == event.KEYCODE_BACK) {
			if(roomAddFragment!=null&&!roomAddFragment.isHidden()) {
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_left,
								R.anim.slide_out_right)
						.hide(roomAddFragment).commit();
			}else if(roomFragment!=null&&!roomFragment.isHidden()) {
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_left,
								R.anim.slide_out_right)
						.hide(roomFragment).commit();
			}else if(deviceAllFragment!=null&&!deviceAllFragment.isHidden()) {
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_left,
								R.anim.slide_out_right)
						.hide(deviceAllFragment).commit();
			}else if(deviceTypeFragment!=null&&!deviceTypeFragment.isHidden()) {
				getSupportFragmentManager()
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_left,
								R.anim.slide_out_right)
						.hide(deviceTypeFragment).commit();
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
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	public void showFragMent(int index){
		Fragment fragment=null;
		switch (index){
			case 0:
				fragment=mainFragment;
				break;
			case 1:
				fragment=roomFragment;
				break;
			case 2:
				fragment=roomAddFragment;
				break;
			case 3:
				fragment=deviceAllFragment;
				break;
			case 4:
				fragment=dimmerFragment;
				break;
			case 5:
				fragment=proCaseFragment;
				break;
			case 6:
				fragment=proCaseAddFragment;
				break;
			case 7:
				fragment=deviceTypeFragment;
				break;
		}
		if(fragment!=null&&fragment.isHidden()) {
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
				fragment=mainFragment;
				break;
			case 1:
				fragment=roomFragment;
				break;
			case 2:
				fragment=roomAddFragment;
				break;
			case 3:
				fragment=deviceAllFragment;
				break;
			case 4:
				fragment=dimmerFragment;
				break;
			case 5:
				fragment=proCaseFragment;
				break;
			case 6:
				fragment=proCaseAddFragment;
				break;
			case 7:
				fragment=deviceTypeFragment;
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
			for (int i=1;i<8;i++) {
				hideFragMent(i);
			}
		}
	}
	public void init() {
		if (instance != null) {
			if (ListDevice.size() > 0) {
				deviceScroll.setCurrentItem(0);
			} else {
				hideFragMent(0);
			}
		}
	}
}
