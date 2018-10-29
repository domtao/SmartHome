package com.zunder.smart.activity.centercontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.constants.ActionStrings;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.activity.main.TabMainActivity;
import com.zunder.smart.activity.main.TabModeActivity;
import com.zunder.smart.activity.popu.dialog.ActionViewWindow;
import com.zunder.smart.activity.popu.dialog.TimeViewWindow;
import com.zunder.smart.activity.popu.dialog.TwoPopupWindow;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.ModeListFactory;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.ModeList;
import com.zunder.smart.utils.ListNumBer;

//情景添加設備列表
public class RMCActionActivity extends Activity implements OnClickListener{

	private TextView backTxt;
	private TextView titleTxt;
	private TextView editeTxt;
	private RelativeLayout actionLayout;
	private TextView actionTxt;
	private RelativeLayout functionLayout;
	private LinearLayout funcLayout;
	private TextView functionTxt;
	private RelativeLayout timeLayout;
	private TextView timeTxt;
	private RelativeLayout delayLayout;
	private TextView delayTxt;
	private RelativeLayout periodLayout;
	private TextView periodTxt;
	private RelativeLayout monthLayout;
	private TextView monthTxt;
	private TextView msgTxt;
	private Activity activity;
	private int Id;
	private static int DeviceId;
	private int deviceTypeKey;
	ActionViewWindow actionViewWindow;
    TwoPopupWindow twoPopupWindow;
	TimeViewWindow timeViewWindow;
	String deviceName="";
	public static void startActivity(Activity activity,int _DeviceId) {
		DeviceId=_DeviceId;
		Intent intent = new Intent(activity, RMCActionActivity.class);
		activity.startActivityForResult(intent,102);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_mode_list_action);
		activity = this;
		backTxt = (TextView) findViewById(R.id.backTxt);
		titleTxt = (TextView) findViewById(R.id.titleTxt);
		editeTxt = (TextView) findViewById(R.id.editeTxt);
		msgTxt=(TextView)findViewById(R.id.msgTxt);
		actionLayout = (RelativeLayout) findViewById(R.id.actionLayout);
		actionTxt = (TextView) findViewById(R.id.actionTxt);
		funcLayout=(LinearLayout)findViewById(R.id.funcLayout);
		functionLayout = (RelativeLayout) findViewById(R.id.functionLayout);
		functionTxt= (TextView) findViewById(R.id.functionTxt);
		timeLayout = (RelativeLayout) findViewById(R.id.timeLayout);
		timeTxt = (TextView) findViewById(R.id.timeTxt);
		delayLayout = (RelativeLayout) findViewById(R.id.delayLayout);
		delayTxt = (TextView) findViewById(R.id.delayTxt);
		periodLayout = (RelativeLayout) findViewById(R.id.periodLayout);
		periodTxt = (TextView) findViewById(R.id.periodTxt);
		monthLayout = (RelativeLayout) findViewById(R.id.monthLayout);
		monthTxt = (TextView) findViewById(R.id.monthTxt);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		actionLayout.setOnClickListener(this);
		functionLayout.setOnClickListener(this);
		timeLayout.setOnClickListener(this);
		delayLayout.setOnClickListener(this);
		periodLayout.setOnClickListener(this);
		monthLayout.setOnClickListener(this);
		initAdapter(DeviceId);
	}

	public void back(String data){
		Intent resultIntent = new Intent();
		resultIntent.putExtra("actionValue",data);
		this.setResult(102, resultIntent);
		this.finish();
	}
    void initAdapter(int DeviceId){
		this.DeviceId=DeviceId;
		Device device= DeviceFactory.getInstance().getDevicesById(DeviceId);
		if(device!=null){
			deviceName=device.getDeviceName();
			deviceTypeKey=device.getDeviceTypeKey();
			titleTxt.setText(device.getRoomName()+device.getDeviceName());
			if(device.getFunctionShow()==1){
				funcLayout.setVisibility(View.VISIBLE);
			}else{
				funcLayout.setVisibility(View.GONE);
			}
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.backTxt:
				finish();
				break;
			case R.id.editeTxt:
				save();
//				back(msgTxt.getText().toString());
				break;
			case R.id.actionLayout:
			    actionViewWindow=new ActionViewWindow(activity,"动作", ActionStrings.getInstance().getActionStrings(deviceTypeKey),0);
				actionViewWindow.setAlertViewOnCListener(new ActionViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String ItemName) {
						actionTxt.setText(ItemName);
						showMsg();
						actionViewWindow.dismiss();
					}
					@Override
					public void cancle() {

					}
				});
				actionViewWindow.show();
			break;
			case R.id.functionLayout:
			    twoPopupWindow=new TwoPopupWindow(activity,"更多动作",ActionStrings.getInstance().getFunctionStrings(deviceTypeKey,DeviceId),ActionStrings.getInstance().getFunctionParamString(deviceTypeKey));
				twoPopupWindow.setOnOCListene(new TwoPopupWindow.OnOCListener() {
					@Override
					public void onResult(int oneIndex,String oneData,int twoIndex, String twoData) {
						functionTxt.setText(oneData+twoData);
						showMsg();
						twoPopupWindow.dismiss();
					}
				});
				twoPopupWindow.show();
				break;
			case R.id.timeLayout:
			    twoPopupWindow=new TwoPopupWindow(activity,"执行时间", ListNumBer.getMinit60(),ListNumBer.getUnits());
				twoPopupWindow.setOnOCListene(new TwoPopupWindow.OnOCListener() {
					@Override
					public void onResult(int oneIndex,String oneData,int twoIndex, String twoData) {
						timeTxt.setText(oneData+twoData);
						showMsg();
						twoPopupWindow.dismiss();
					}
				});
				twoPopupWindow.show();
					break;
			case R.id.delayLayout:
			    twoPopupWindow=new TwoPopupWindow(activity,"延时", ListNumBer.getMinit60(),ListNumBer.getUnits());
				twoPopupWindow.setOnOCListene(new TwoPopupWindow.OnOCListener() {
					@Override
					public void onResult(int oneIndex,String oneData,int twoIndex, String twoData) {
						delayTxt.setText(oneData+twoData);
						showMsg();
						twoPopupWindow.dismiss();
					}
				});
				twoPopupWindow.show();
				break;
			case R.id.periodLayout:
				timeViewWindow=new TimeViewWindow(activity);
				timeViewWindow.setAlertViewOnCListener(new TimeViewWindow.AlertViewOnCListener() {
					@Override
					public void onItem(int pos, String itemName) {
						periodTxt.setText(itemName);
						showMsg();
						timeViewWindow.dismiss();
					}

					@Override
					public void cancle() {

					}
				});
				timeViewWindow.show();
				break;
			case R.id.monthLayout:
			    twoPopupWindow=new TwoPopupWindow(activity,"月份管控", ListNumBer.getMonth(),ListNumBer.getMonth());
				twoPopupWindow.setOnOCListene(new TwoPopupWindow.OnOCListener() {
					@Override
					public void onResult(int oneIndex,String oneData,int twoIndex, String twoData) {

						monthTxt.setText(oneData+"~"+twoData);
						showMsg();
						twoPopupWindow.dismiss();
					}
				});
				twoPopupWindow.show();
				break;
			default:
				break;
		}
	}
	public void showMsg(){
		msgTxt.setText(deviceName+actionTxt.getText().toString()+functionTxt.getText().toString()+timeTxt.getText().toString()+"#"+delayTxt.getText().toString()+"("+periodTxt.getText().toString()+")["+monthTxt.getText().toString()+"]");
	}
	public void save() {


		if(TextUtils.isEmpty(actionTxt.getText())&&TextUtils.isEmpty(functionTxt.getText())){
			ToastUtils.ShowError(activity,"请选择设备动作",Toast.LENGTH_SHORT,true);
			return;
		}
		String result=deviceName+actionTxt.getText().toString()+functionTxt.getText().toString();

		if(!timeTxt.getText().toString().equals("0秒")){
			result+=timeTxt.getText().toString();
		}
		if(!delayTxt.getText().toString().equals("0秒")){
			result+="#"+delayTxt.getText().toString();

		}
		if(!periodTxt.getText().toString().equals("00:00~00:00")){
			result+="("+periodTxt.getText().toString()+")";

		}
		if(!monthTxt.getText().toString().equals("0月~0月")){
			result+="["+monthTxt.getText().toString()+"]";
		}
		back(result);
	}

}
