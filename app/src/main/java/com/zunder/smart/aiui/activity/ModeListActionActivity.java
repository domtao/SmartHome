package com.zunder.smart.aiui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.zunder.smart.dao.impl.factory.CloundDeviceFactory;
import com.zunder.smart.dao.impl.factory.CloundModeListFactory;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.ModeListFactory;
import com.zunder.smart.model.Device;
import com.zunder.smart.model.Mode;
import com.zunder.smart.model.ModeList;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.JSONHelper;
import com.zunder.smart.utils.ListNumBer;

//情景添加設備列表
public class ModeListActionActivity extends Activity implements OnClickListener {

	private TextView backTxt;
	private TextView titleTxt;
	private TextView editeTxt;
	private RelativeLayout actionLayout;
	private TextView actionTxt;
	private RelativeLayout functionLayout;
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
	private static int Id;

	private int modeId;
	private int deviceTypeKey;
	private static String modelOpration="";
	private static int deviceId;
	private static Mode mode;
	public static void startActivity(Activity activity,int _Id, String _modelOpration,
									 Mode _mode, int  _deviceId) {
		Id=_Id;
		deviceId=_deviceId;
		modelOpration = _modelOpration;

		mode=_mode;
		Intent intent = new Intent(activity, ModeListActionActivity.class);
		activity.startActivityForResult(intent, 100);
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
		msgTxt = (TextView) findViewById(R.id.msgTxt);
		actionLayout = (RelativeLayout) findViewById(R.id.actionLayout);
		actionTxt = (TextView) findViewById(R.id.actionTxt);

		functionLayout = (RelativeLayout) findViewById(R.id.functionLayout);
		functionTxt = (TextView) findViewById(R.id.functionTxt);
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
		initAdapter();
	}

	void initAdapter() {
		Device device= CloundDeviceFactory.getDeviceById(deviceId);
		if (device != null&&mode!=null) {
			this.modeId = mode.getId();
			this.deviceId = device.getId();
			deviceTypeKey = device.getDeviceTypeKey();
			titleTxt.setText(device.getRoomName() + device.getDeviceName());
			if (device.getFunctionShow() == 1) {
				functionLayout.setVisibility(View.VISIBLE);
			} else {
				functionLayout.setVisibility(View.GONE);
			}
		}
		if(modelOpration.equals("Add")){
			actionTxt.setText("");
			functionTxt.setText("");
			timeTxt.setText("0秒");
			delayTxt.setText("0秒");
			periodTxt.setText("00:00~00:00");
			monthTxt.setText("0月~0月");
			msgTxt.setText("");
		}else{
			ModeList modeList = CloundModeListFactory.getInstance().getModeList(Id);
			if (modeList != null) {
				actionTxt.setText(modeList.getModeAction());
				timeTxt.setText(modeList.getModeTime());
				functionTxt.setText(modeList.getModeFunction());
				delayTxt.setText(modeList.getModeDelayed());
				periodTxt.setText(modeList.getModePeriod());
				monthTxt.setText(modeList.getBeginMonth() + "月~" + modeList.getEndMonth() + "月");
				msgTxt.setText(modeList.getModeAction() + modeList.getModeFunction() + modeList.getModePeriod() + modeList.getModeDelayed() + modeList.getBeginMonth() + "月~" + modeList.getEndMonth() + "月");
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.backTxt:
				if (MainActivity.getInstance().mHost.getCurrentTab() == 2) {
					TabModeActivity.getInstance().hideFragMent(4);
				} else {
					TabMainActivity.getInstance().hideFragMent(4);
				}
				break;
			case R.id.editeTxt:
				save();
				break;
			case R.id.actionLayout:
				final ActionViewWindow actionViewWindow = new ActionViewWindow(activity, "动作", ActionStrings.getInstance().getActionStrings(deviceTypeKey), 0);
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
				final TwoPopupWindow functionWindow = new TwoPopupWindow(activity, "更多动作", ActionStrings.getInstance().getFunctionStrings(deviceTypeKey,Id), ActionStrings.getInstance().getFunctionParamString(deviceTypeKey));
				functionWindow.setOnOCListene(new TwoPopupWindow.OnOCListener() {
					@Override
					public void onResult(int oneIndex,String oneData,int twoIndex, String twoData) {
						functionTxt.setText(oneData + twoData);
						showMsg();
						functionWindow.dismiss();
					}
				});
				functionWindow.show();
				break;
			case R.id.timeLayout:
				final TwoPopupWindow timeWindow = new TwoPopupWindow(activity, "执行时间", ListNumBer.getMinit60(), ListNumBer.getUnits());
				timeWindow.setOnOCListene(new TwoPopupWindow.OnOCListener() {
					@Override
					public void onResult(int oneIndex,String oneData,int twoIndex, String twoData) {
						timeTxt.setText(oneData + twoData);
						showMsg();
						timeWindow.dismiss();
					}
				});
				timeWindow.show();
				break;
			case R.id.delayLayout:
				final TwoPopupWindow delayWindow = new TwoPopupWindow(activity, "延时", ListNumBer.getMinit60(), ListNumBer.getUnits());
				delayWindow.setOnOCListene(new TwoPopupWindow.OnOCListener() {
					@Override
					public void onResult(int oneIndex,String oneData,int twoIndex, String twoData) {
						delayTxt.setText(oneData + twoData);
						showMsg();
						delayWindow.dismiss();
					}
				});
				delayWindow.show();
				break;
			case R.id.periodLayout:
				final TimeViewWindow timeViewWindow = new TimeViewWindow(activity);
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
				final TwoPopupWindow monthWindow = new TwoPopupWindow(activity, "月份管控", ListNumBer.getMonth(), ListNumBer.getMonth());
				monthWindow.setOnOCListene(new TwoPopupWindow.OnOCListener() {
					@Override
					public void onResult(int oneIndex,String oneData,int twoIndex, String twoData) {
						monthTxt.setText(oneData + "~" + twoData);
						showMsg();
						monthWindow.dismiss();
					}
				});
				monthWindow.show();
				break;
			default:
				break;
		}
	}

	public void showMsg() {
		msgTxt.setText(actionTxt.getText().toString() + functionTxt.getText().toString() + timeTxt.getText().toString() + "#" + delayTxt.getText().toString() + "(" + periodTxt.getText().toString() + ")[" + monthTxt.getText().toString() + "]");
	}

	public void save() {
		ModeList modeList = new ModeList();
		modeList.setId(Id);
		modeList.setModeAction(actionTxt.getText().toString());
		modeList.setModeFunction(functionTxt.getText().toString());
		modeList.setModeTime(timeTxt.getText().toString());
		modeList.setModeDelayed(delayTxt.getText().toString());
		modeList.setModePeriod(periodTxt.getText().toString());
		modeList.setDeviceId(deviceId);
		String[] monthStr = monthTxt.getText().toString().replace("月", "").split("~");
		modeList.setBeginMonth(monthStr[0]);
		modeList.setEndMonth(monthStr[1]);
		modeList.setSeqencing(0);
		modeList.setModeId(modeId);

		Log.e("down", mode.convertTostring());
		String result = ISocketCode.setAddModeList(JSONHelper.toJSON(modeList),
				AiuiMainActivity.deviceID);
		MainActivity.getInstance().sendCode(result);
		if(modelOpration.equals("Add")){
			ToastUtils.ShowSuccess(activity, "添加成功!", Toast.LENGTH_SHORT, true);
			 result=ISocketCode.setGetModeList(mode.getId()+"",AiuiMainActivity.deviceID);
			MainActivity.getInstance().sendCode( result);
		}else{
			CloundModeListFactory.getInstance().update(modeList);
			ToastUtils.ShowSuccess(activity, "修改成功!", Toast.LENGTH_SHORT, true);
		}

//		if (Id == 0) {
//			if (MyApplication.getInstance().getWidgetDataBase().insertModeList(modeList) > 0) {
//				ToastUtils.ShowSuccess(activity, "添加成功!", Toast.LENGTH_SHORT, true);
//				ModeListFactory.getInstance().clearList();
//			} else {
//				ToastUtils.ShowError(activity, "插入失败!", Toast.LENGTH_SHORT, true);
//
//			}
//		} else {
//			if (MyApplication.getInstance().getWidgetDataBase().updateModeList(modeList) > 0) {
//				ToastUtils.ShowSuccess(activity, "修改成功!", Toast.LENGTH_SHORT, true);
//				ModeListFactory.getInstance().clearList();
//			} else {
//				ToastUtils.ShowError(activity, "修改失败!", Toast.LENGTH_SHORT, true);
//
//			}
//		}
	}
}
