package com.zunder.smart.activity.sub.set;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.constants.ActionStrings;
import com.zunder.smart.activity.popu.dialog.ActionViewWindow;
import com.zunder.smart.activity.popu.dialog.TwoPopupWindow;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.ModeFactory;
import com.zunder.smart.listener.DeviceStateListener;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.Mode;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.service.TcpSender;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.utils.ListNumBer;


public class LinkageActivity extends Activity implements OnClickListener {
	public LinkageActivity activity;
	private static int Id;
	private Device device;
	private static int editState;
	String time="00";
	private int actionIndex=-1;

	public static void startActivity(Activity activity, int _id) {
		Id = _id;
		Intent intent = new Intent(activity, LinkageActivity.class);
		activity.startActivityForResult(intent,0);
	}
	private TextView backTxt;
	private TextView titleTxt;

	private RelativeLayout actionLayout;
	private TextView actionTxt;
	private RelativeLayout deviceLayout;
	private TextView deviceTxt;
	private RelativeLayout timeLayout;
	private TextView timeTxt;
	private RelativeLayout modeOnLayout;
	private TextView modeOnTxt;
	private RelativeLayout modeOFFLayout;
	private TextView modeOFFTxt;
	private Button moreBtn;
	String products="00";
	String deviceID="000000";




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_linkage);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		activity = this;
		backTxt = (TextView) findViewById(R.id.backTxt);
		titleTxt = (TextView) findViewById(R.id.titleTxt);
		backTxt.setOnClickListener(this);
		device= DeviceFactory.getInstance().getDevicesById(Id);

		actionLayout = (RelativeLayout) findViewById(R.id.actionLayout);
		actionTxt = (TextView) findViewById(R.id.actionTxt);
		deviceLayout = (RelativeLayout) findViewById(R.id.deviceLayout);
		deviceTxt = (TextView) findViewById(R.id.deviceTxt);
		timeLayout = (RelativeLayout) findViewById(R.id.timeLayout);
		timeTxt = (TextView) findViewById(R.id.timeTxt);
		modeOnLayout = (RelativeLayout) findViewById(R.id.modeOnLayout);
		modeOnTxt = (TextView) findViewById(R.id.modeOnTxt);
		modeOFFLayout = (RelativeLayout) findViewById(R.id.modeOFFLayout);
		modeOFFTxt = (TextView) findViewById(R.id.modeOFFTxt);
		moreBtn = (Button) findViewById(R.id.moreBtn);

		actionLayout.setOnClickListener(this);
		deviceLayout.setOnClickListener(this);
		timeLayout.setOnClickListener(this);
		modeOnLayout.setOnClickListener(this);
		modeOFFLayout.setOnClickListener(this);
		moreBtn.setOnClickListener(this);
		products=device.getProductsCode();
		deviceID=device.getDeviceID();

	}


	private  Handler handler = new Handler() {
		public void handleMessage(Message msg) {

		};
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.backTxt:
				this.finish();
				break;
			case R.id.actionLayout:{
			final ActionViewWindow actionViewWindow=new ActionViewWindow(activity,"动作", ActionStrings.getInstance().getLinkageStrings(),0);
				actionViewWindow.setAlertViewOnCListener(new ActionViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String ItemName) {
						actionTxt.setText(ItemName);
						actionViewWindow.dismiss();
						actionIndex=pos;
					}
					@Override
					public void cancle() {
					}
				});
				actionViewWindow.show();
			}
				break;
			case R.id.deviceLayout:
			{
				final ActionViewWindow actionViewWindow=new ActionViewWindow(activity,"设备", DeviceFactory.getInstance().getDeviceName(),0);
				actionViewWindow.setAlertViewOnCListener(new ActionViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String ItemName) {
						deviceTxt.setText(ItemName);
						actionViewWindow.dismiss();
					}
					@Override
					public void cancle() {

					}
				});
				actionViewWindow.show();
			}
					break ;
			case R.id.timeLayout: {
				final TwoPopupWindow twoPopupWindow = new TwoPopupWindow(activity, "时间", ListNumBer.getMinit60(), ListNumBer.getUnits());
				twoPopupWindow.setOnOCListene(new TwoPopupWindow.OnOCListener() {
					@Override
					public void onResult(int oneIndex,String oneData,int twoIndex, String twoData) {
						timeTxt.setText(oneData + twoData);
						if(twoIndex==0){
                            time=AppTools.toHex(oneIndex);
						}else if(twoIndex==1) {
							time=AppTools.toHex(64+oneIndex);
						}
						else if(twoIndex==2){
							time=AppTools.toHex(128+oneIndex);
						}
						twoPopupWindow.dismiss();
					}
				});
				twoPopupWindow.show();
			}
				break ;
			case R.id.modeOnLayout:
			{
				final ActionViewWindow actionViewWindow=new ActionViewWindow(activity,"情景", ModeFactory.getInstance().getModeName(),0);
				actionViewWindow.setAlertViewOnCListener(new ActionViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String ItemName) {
						modeOnTxt.setText(ItemName);
						actionViewWindow.dismiss();
					}
					@Override
					public void cancle() {

					}
				});
				actionViewWindow.show();
			}
				break ;
			case R.id.modeOFFLayout:
			{
				final ActionViewWindow actionViewWindow=new ActionViewWindow(activity,"情景",ModeFactory.getInstance().getModeName(),0);
				actionViewWindow.setAlertViewOnCListener(new ActionViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String ItemName) {
						modeOFFTxt.setText(ItemName);
						actionViewWindow.dismiss();
					}
					@Override
					public void cancle() {

					}
				});
				actionViewWindow.show();
			}
				break ;
			case R.id.moreBtn:
				//joe 联动设置发码
				if(TextUtils.isEmpty(actionTxt.getText())){
					ToastUtils.ShowError(activity,"请选择动作形态",Toast.LENGTH_SHORT,true);
					return;
				}
				if(TextUtils.isEmpty(deviceTxt.getText())){
					products="00";
					deviceID="000000";
				}

				String memIndex=AppTools.toHex(Integer.parseInt(device.getDeviceIO())+129);
				String linkDevice=products+deviceID+device.getDeviceIO();

				String modeOnCode="00";
				String modeOnIo="00";
				if(!TextUtils.isEmpty(modeOnTxt.getText())){
					Mode mode=ModeFactory.getInstance().getModeNyName(modeOnTxt.getText().toString());
					if(mode!=null){
						 modeOnCode=AppTools.toHex(mode.getModeCode());
						 modeOnIo=AppTools.toHex(mode.getModeLoop());
					}
				}

				String modeOffCode="00";
				String modeOffIo="00";
				if(!TextUtils.isEmpty(modeOFFTxt.getText())){
					Mode mode=ModeFactory.getInstance().getModeNyName(modeOFFTxt.getText().toString());
					if(mode!=null){
						modeOffCode=AppTools.toHex(mode.getModeCode());
						modeOffIo=AppTools.toHex(mode.getModeLoop());
					}
				}

				String cmdStr=memIndex+":H0"+actionIndex+linkDevice+time+modeOnCode+modeOnIo+modeOffCode+modeOffIo;
				SendCMD sendCMD=SendCMD.getInstance();
				sendCMD.sendCmd(238,cmdStr,device);
				break ;
		default:
			break;
		}

	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == event.KEYCODE_BACK) {
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}