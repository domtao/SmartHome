package com.zunder.smart.activity.safe;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.TabMainActivity;
import com.zunder.smart.activity.timmer.DimmerTimer;
import com.zunder.smart.activity.timmer.SafeTimer;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.json.Constants;
import com.zunder.smart.listener.DeviceStateListener;
import com.zunder.smart.listener.SafeListener;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.Mode;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;

public class SafeSetFragment extends Fragment implements OnClickListener ,OnTouchListener,SafeListener{
	private Activity activity;
	private TextView backTxt;
	private TextView titleTxt;
	private CheckBox openModeWarm;
	private CheckBox closeModeWarm;
	private CheckBox modeOpen;
	private CheckBox modeClose;
	private CheckBox modeNC;
	private Device device;
	private CheckBox stateCheck;
	private RelativeLayout openModeWarmLayout;
	private RelativeLayout closeModeWarmLayout;
	private RelativeLayout modeOpenLayout;
	private RelativeLayout modeCloseLayout;
	private ImageView powerStateImg;
	int IO=0;
	int modeLoop=0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_safe_set, container,
				false);
		activity = getActivity();
		backTxt = (TextView) root.findViewById(R.id.backTxt);
		titleTxt = (TextView) root.findViewById(R.id.titleTxt);
		openModeWarm = (CheckBox) root.findViewById(R.id.openModeWarm);
		closeModeWarm = (CheckBox) root.findViewById(R.id.closeModeWarm);
		modeOpen = (CheckBox) root.findViewById(R.id.modeOpen);
		modeClose = (CheckBox) root.findViewById(R.id.modeClose);
		modeNC = (CheckBox) root.findViewById(R.id.modeNC);
		stateCheck=(CheckBox) root.findViewById(R.id.stateCheck);
		openModeWarmLayout = (RelativeLayout) root.findViewById(R.id.openModeWarmLayout);
		closeModeWarmLayout = (RelativeLayout) root.findViewById(R.id.closeModeWarmLayout);
		modeOpenLayout = (RelativeLayout) root.findViewById(R.id.modeOpenLayout);
		modeCloseLayout = (RelativeLayout) root.findViewById(R.id.modeCloseLayout);
		powerStateImg=(ImageView)root.findViewById(R.id.powerState);

		backTxt.setOnClickListener(this);
		root.setOnTouchListener(this);
		openModeWarm.setOnClickListener(this);
		closeModeWarm.setOnClickListener(this);
		modeOpen.setOnClickListener(this);
		modeClose.setOnClickListener(this);
		modeNC.setOnClickListener(this);
		stateCheck.setOnClickListener(this);
		openModeWarmLayout.setOnClickListener(this);
		closeModeWarmLayout.setOnClickListener(this);
		modeOpenLayout.setOnClickListener(this);
		modeCloseLayout.setOnClickListener(this);
		return root;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(hidden){
			TcpSender.setSafeListener(null);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.backTxt:
				TabMainActivity.getInstance().hideFragMent(9);
				break;
			case R.id.stateCheck:
				//joe
				if(stateCheck.isChecked()){
					stateCheck.setChecked(false);
					DialogAlert alert = new DialogAlert(activity);
					alert.init(getString(R.string.tip), getString(R.string.is_open) + device.getDeviceName());
					alert.setSureListener(new DialogAlert.OnSureListener() {
						@Override
						public void onSure() {
							// TODO Auto-generated method stub
							SendCMD.getInstance().sendCmd(242, "2", device);
							SendCMD.getInstance().sendCmd(242, "9", device);
						}

						@Override
						public void onCancle() {
							// TODO Auto-generated method stub

						}
					});
					alert.show();
				}else{
					stateCheck.setChecked(true);
					DialogAlert alert = new DialogAlert(activity);
					alert.init(getString(R.string.tip), getString(R.string.is_off) + device.getDeviceName());
					alert.setSureListener(new DialogAlert.OnSureListener() {

						@Override
						public void onSure() {
							// TODO Auto-generated method stub
							SendCMD.getInstance().sendCmd(242, "3", device);
							SendCMD.getInstance().sendCmd(242, "9", device);

						}
						@Override
						public void onCancle() {
							// TODO Auto-generated method stub

						}
					});
					alert.show();
				}
				break;
			case R.id.openModeWarm:
			    // joe 发生报警
				if(openModeWarm.isChecked()){
					openModeWarm.setChecked(false);
					SendCMD.getInstance().sendCmd(242, "14", device);
					SendCMD.getInstance().sendCmd(242, "9", device);
				}else{
					openModeWarm.setChecked(true);
					SendCMD.getInstance().sendCmd(242, "24", device);
					SendCMD.getInstance().sendCmd(242, "9", device);
				}
				break;
			case R.id.closeModeWarm:
                // joe 解除报警
				if(closeModeWarm.isChecked()){
					closeModeWarm.setChecked(false);
					SendCMD.getInstance().sendCmd(242, "15", device);
					SendCMD.getInstance().sendCmd(242, "9", device);
				}else{
					closeModeWarm.setChecked(true);
					SendCMD.getInstance().sendCmd(242, "25", device);
					SendCMD.getInstance().sendCmd(242, "9", device);
				}
				break;
			case R.id.modeOpen:
			    //Joe 开门联动
				if(modeOpen.isChecked()){
					modeOpen.setChecked(false);
					SendCMD.getInstance().sendCmd(242, "22", device);
					SendCMD.getInstance().sendCmd(242, "9", device);
				}else{
					modeOpen.setChecked(true);
					SendCMD.getInstance().sendCmd(242, "12", device);
					SendCMD.getInstance().sendCmd(242, "9", device);
				}
				break;
			case R.id.modeClose:
			    //joe 关门联动
				if(modeClose.isChecked()){
					modeClose.setChecked(false);
					SendCMD.getInstance().sendCmd(242, "23", device);
					SendCMD.getInstance().sendCmd(242, "9", device);
				}else{
					modeClose.setChecked(true);
					SendCMD.getInstance().sendCmd(242, "13", device);
					SendCMD.getInstance().sendCmd(242, "9", device);
				}
				break;
			case R.id.modeNC:
			    // joe NO NC
				if(modeNC.isChecked()){
					modeNC.setChecked(false);
					SendCMD.getInstance().sendCmd(242, "21", device);
					SendCMD.getInstance().sendCmd(242, "9", device);
				}else{
					modeNC.setChecked(true);
					SendCMD.getInstance().sendCmd(242, "11", device);
					SendCMD.getInstance().sendCmd(242, "9", device);
				}
				break;
			case R.id.openModeWarmLayout:{
				Mode mode= ModeFactory.getInstance().getMode(251,modeLoop);
				if(mode!=null){
					TabMainActivity.getInstance().showFragMent(2);
					TabMainActivity.getInstance().modeListFragment.initAdapter(mode.getId());
				}else{
					if(ModeFactory.getInstance().AddOpenModeWarm(modeLoop)>0){
						ModeFactory.getInstance().clearList();
						mode= ModeFactory.getInstance().getMode(251,modeLoop);
						if(mode!=null){
							TabMainActivity.getInstance().showFragMent(2);
							TabMainActivity.getInstance().modeListFragment.initAdapter(mode.getId());
						}
					}
				}
			}
				break;
			case R.id.closeModeWarmLayout:{
				Mode mode=ModeFactory.getInstance().getMode(250,modeLoop);
				if(mode!=null){
					TabMainActivity.getInstance().showFragMent(2);
					TabMainActivity.getInstance().modeListFragment.initAdapter(mode.getId());
				}else{
					if(ModeFactory.getInstance().AddCloseModeWarm(modeLoop)>0){
						ModeFactory.getInstance().clearList();
						mode= ModeFactory.getInstance().getMode(250,modeLoop);
						if(mode!=null){
							TabMainActivity.getInstance().showFragMent(2);
							TabMainActivity.getInstance().modeListFragment.initAdapter(mode.getId());
						}
					}
				}
			}
				break;
			case R.id.modeOpenLayout:{
				Mode mode=ModeFactory.getInstance().getMode(IO+200,0);
				if(mode!=null){
					TabMainActivity.getInstance().showFragMent(2);
					TabMainActivity.getInstance().modeListFragment.initAdapter(mode.getId());
				}else{
					if(ModeFactory.getInstance().AddOpenMode(IO)>0){
						ModeFactory.getInstance().clearList();
						mode= ModeFactory.getInstance().getMode(IO+200,0);
						if(mode!=null){
							TabMainActivity.getInstance().showFragMent(2);
							TabMainActivity.getInstance().modeListFragment.initAdapter(mode.getId());
						}
					}
				}
			}
				break;
			case R.id.modeCloseLayout:{
				Mode mode=ModeFactory.getInstance().getMode(IO+200,7);
				if(mode!=null){
					TabMainActivity.getInstance().showFragMent(2);
					TabMainActivity.getInstance().modeListFragment.initAdapter(mode.getId());
				}else{
					if(ModeFactory.getInstance().AddCloseNode(IO)>0){
					ModeFactory.getInstance().clearList();
					mode= ModeFactory.getInstance().getMode(IO+200,7);
					if(mode!=null){
						TabMainActivity.getInstance().showFragMent(2);
						TabMainActivity.getInstance().modeListFragment.initAdapter(mode.getId());
					}
				}
				}
			}
				break;

		}
	}

	void init(Device _device){
		device=_device;
		device.setDeviceBackCode("");
		titleTxt.setText(device.getRoomName()+device.getDeviceName());
		IO=Integer.valueOf(device.getDeviceID().substring(0,2),16);
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
		openModeWarm.setChecked(false);
		closeModeWarm.setChecked(false);
		modeOpen.setChecked(false);
		modeClose.setChecked(false);
		modeNC.setChecked(false);
		TcpSender.setSafeListener(this);
		SendCMD.getInstance().sendCmd(242, "9", device);
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

	//Joe
	private SafeTimer.OnSafeIndexSureListener onSafeIndexSureListener=new SafeTimer.OnSafeIndexSureListener() {

		@Override
		public void onIOState(int powerState, int alramIndex, int passIndex, int eleIndex, int disIndex, int openModeIndex,int closeModeIndex, int nocIndex, int openDoorIndex, int closeDoorIndex) {
			if (device != null && device.getDeviceTypeKey() == 18) {
				stateCheck.setChecked(passIndex==1?true:false);
				openModeWarm.setChecked(openModeIndex==1?true:false);
				closeModeWarm.setChecked(closeModeIndex==0?true:false);
				modeOpen.setChecked(openDoorIndex==1?true:false);
				modeClose.setChecked(closeDoorIndex==1?true:false);
				modeNC.setChecked(nocIndex==1?true:false);
				//Joe 0911 安防状态图片
				powerStateImg.setImageResource(powerState>0?R.mipmap.safe_power_on:R.mipmap.safe_power_off);
			}
		}

		@Override
		public void onState(int powerState, int alramIndex) {

		}



	};

	@Override
	public void setBackCode(String cmd) {
		if(device!=null) {
			if(device.getDeviceTypeKey()==18&&device.getDeviceID().equals(cmd.substring(8,14))){//joe
				SafeTimer.getInstance().setIOBackeCode(cmd,onSafeIndexSureListener);
			}
		}
	}

}
