package com.zunder.smart.activity.safe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.SafeInfoActivity;
import com.zunder.smart.activity.main.TabMainActivity;
import com.zunder.smart.activity.timmer.SafeTimer;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dialog.DialogAlert;
import com.zunder.smart.listener.SafeListener;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;

public class AnHongFragment extends Fragment implements OnClickListener,SafeListener {

	private Activity activity;
	private ImageView safe,safeHis;
	private ImageView imageState;
	private LinearLayout homeLayout,sleepLayout,liveLayout;
	View root;

	private int anhongState=0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		activity = getActivity();
		root = inflater.inflate(R.layout.fragment_anhong, container,
				false);
		homeLayout=(LinearLayout)root.findViewById(R.id.homeLayout);
		sleepLayout=(LinearLayout)root.findViewById(R.id.sleepLayout);
		liveLayout=(LinearLayout)root.findViewById(R.id.liveLayout);
		safe=(ImageView)root.findViewById(R.id.safe);
		safeHis=(ImageView)root.findViewById(R.id.safeHis);
		imageState=(ImageView)root.findViewById(R.id.imageState);
		imageState.setOnClickListener(this);
		safe.setOnClickListener(this);
		safeHis.setOnClickListener(this);
		homeLayout.setOnClickListener(this);
		sleepLayout.setOnClickListener(this);
		liveLayout.setOnClickListener(this);
		setBackCode();
		return root;
	}
	public void setBackCode(){
		TcpSender.setSafeListener(this);
		SendCMD cmdsend = SendCMD.getInstance();
		cmdsend.sendCMD(242,"A", null);
	}

	public static IWidgetDAO sql() {
		return MyApplication.getInstance().getWidgetDataBase();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!isHidden()) {
			setBackCode();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.safe:
			 TabMainActivity.getInstance().showFragMent(7);
				TabMainActivity.getInstance().safeFragment.init(0);
				break;
			case R.id.safeHis:
				SafeInfoActivity.startActivity(activity);
				break;
			case R.id.homeLayout: {
				//joe 在家安防
				SendCMD cmdsend = SendCMD.getInstance();
				cmdsend.sendCMD(242, "B", null);
			}
			break;
			case R.id.liveLayout: {
				//joe 离家安防
				SendCMD cmdsend = SendCMD.getInstance();
				cmdsend.sendCMD(242,"C", null);
			}
			break;
			case R.id.sleepLayout: {
				//joe 睡觉安防
				SendCMD cmdsend = SendCMD.getInstance();
				cmdsend.sendCMD(242, "D", null);
			}
			break;
			case R.id.imageState:
				if(anhongState==0)
				{
					DialogAlert alert = new DialogAlert( TabMainActivity.getInstance());
					alert.init( TabMainActivity.getInstance().getString(R.string.tip),"是否布防");

					alert.setSureListener(new DialogAlert.OnSureListener() {
						@Override
						public void onSure() {
							// TODO Auto-generated method stub
							//joe 安防布防
							SendCMD cmdsend = SendCMD.getInstance();
							cmdsend.sendCMD(242,"5", null);
						}

						@Override
						public void onCancle() {
							// TODO Auto-generated method stub
						}
					});
					alert.show();
				}else{
					DialogAlert alert = new DialogAlert( TabMainActivity.getInstance());
					alert.init( TabMainActivity.getInstance().getString(R.string.tip), "是否撤防");
					alert.setSureListener(new DialogAlert.OnSureListener() {
						@Override
						public void onSure() {
							// TODO Auto-generated method stub
							//joe 安防撤防
							SendCMD cmdsend = SendCMD.getInstance();
							cmdsend.sendCMD(242,"4", null);
						}
						@Override
						public void onCancle() {
							// TODO Auto-generated method stub
						}
					});
					alert.show();
				}
				break;

		}
	}

	@Override
	public void setBackCode(String cmd) {
		//joe 安防解码
		SafeTimer.getInstance().setBackCode(cmd,onSafeIndexSureListener);
	}
	//Joe 安防回码更新控件图片
	private SafeTimer.OnSafeIndexSureListener onSafeIndexSureListener=new SafeTimer.OnSafeIndexSureListener() {
		@Override
		public void onState(int powerState, int alramIndex) {
			if(powerState>0){
				imageState.setImageResource(R.mipmap.safe_close);
				anhongState=1;
			}else{
				imageState.setImageResource(R.mipmap.safe_open);
				anhongState=0;
			}
			if(alramIndex==1){
				//joe 在家
				homeLayout.setBackgroundResource(R.drawable.button_safe_select_shape);
				sleepLayout.setBackgroundResource(R.drawable.button_safe_shape);
				liveLayout.setBackgroundResource(R.drawable.button_safe_shape);
			}else if(alramIndex==2){
				//joe 离家
				homeLayout.setBackgroundResource(R.drawable.button_safe_shape);
				sleepLayout.setBackgroundResource(R.drawable.button_safe_shape);
				liveLayout.setBackgroundResource(R.drawable.button_safe_select_shape);
			}else if(alramIndex==3){
				//joe 睡觉
				homeLayout.setBackgroundResource(R.drawable.button_safe_shape);
				sleepLayout.setBackgroundResource(R.drawable.button_safe_select_shape);
				liveLayout.setBackgroundResource(R.drawable.button_safe_shape);
			}else{
				//joe 其它
				homeLayout.setBackgroundResource(R.drawable.button_safe_shape);
				sleepLayout.setBackgroundResource(R.drawable.button_safe_shape);
				liveLayout.setBackgroundResource(R.drawable.button_safe_shape);
			}
		}
		//安防
		@Override
		public void onIOState(int powerState,int alramIndex,int passIndex,int eleIndex,int disIndex,int openModeIndex,int closeModeIndex,int nocIndex,int openDoorIndex, int closeDoorIndex){

		}
	};

}
