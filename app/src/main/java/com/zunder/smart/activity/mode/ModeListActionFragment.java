package com.zunder.smart.activity.mode;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class ModeListActionFragment extends Fragment implements OnClickListener,View.OnTouchListener{

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
	private int DeviceId;
	private int modeId;
	private int deviceTypeKey;
	ActionViewWindow actionViewWindow;
    TwoPopupWindow twoPopupWindow;
	TimeViewWindow timeViewWindow;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_mode_list_action, container,
				false);
		activity = getActivity();
		backTxt = (TextView) root.findViewById(R.id.backTxt);
		titleTxt = (TextView) root.findViewById(R.id.titleTxt);
		editeTxt = (TextView) root.findViewById(R.id.editeTxt);
		msgTxt=(TextView)root.findViewById(R.id.msgTxt);
		actionLayout = (RelativeLayout) root.findViewById(R.id.actionLayout);
		actionTxt = (TextView) root.findViewById(R.id.actionTxt);
		funcLayout=(LinearLayout)root.findViewById(R.id.funcLayout);
		functionLayout = (RelativeLayout) root.findViewById(R.id.functionLayout);
		functionTxt= (TextView) root.findViewById(R.id.functionTxt);
		timeLayout = (RelativeLayout) root.findViewById(R.id.timeLayout);
		timeTxt = (TextView) root.findViewById(R.id.timeTxt);
		delayLayout = (RelativeLayout) root.findViewById(R.id.delayLayout);
		delayTxt = (TextView) root.findViewById(R.id.delayTxt);
		periodLayout = (RelativeLayout) root.findViewById(R.id.periodLayout);
		periodTxt = (TextView) root.findViewById(R.id.periodTxt);
		monthLayout = (RelativeLayout) root.findViewById(R.id.monthLayout);
		monthTxt = (TextView) root.findViewById(R.id.monthTxt);
		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		actionLayout.setOnClickListener(this);
		functionLayout.setOnClickListener(this);
		timeLayout.setOnClickListener(this);
		delayLayout.setOnClickListener(this);
		periodLayout.setOnClickListener(this);
		monthLayout.setOnClickListener(this);
		root.setOnTouchListener(this);
		return root;
	}

    void initAdapter(int Id,int modeId,int DeviceId){
		this.Id=Id;
		this.modeId=modeId;
		this.DeviceId=DeviceId;
		Device device= DeviceFactory.getInstance().getDevicesById(DeviceId);
		if(device!=null){
			deviceTypeKey=device.getDeviceTypeKey();
			titleTxt.setText(device.getRoomName()+device.getDeviceName());
			if(device.getFunctionShow()==1){
				funcLayout.setVisibility(View.VISIBLE);
			}else{
				funcLayout.setVisibility(View.GONE);
			}
		}
		if(Id!=0){
			ModeList modeList=ModeListFactory.getInstance().getModeList(Id);
			if(modeList!=null){
				actionTxt.setText(modeList.getModeAction());
				timeTxt.setText(modeList.getModeTime());
				functionTxt.setText(modeList.getModeFunction());
				delayTxt.setText(modeList.getModeDelayed());
				periodTxt.setText(modeList.getModePeriod());
				monthTxt.setText(modeList.getBeginMonth()+"月~"+modeList.getEndMonth()+"月");
				msgTxt.setText(modeList.getModeAction()+modeList.getModeFunction()+modeList.getModePeriod()+modeList.getModeDelayed()+modeList.getBeginMonth()+"月~"+modeList.getEndMonth()+"月");
			}
		}else{
			actionTxt.setText("");
			functionTxt.setText("");
			timeTxt.setText("0秒");
			delayTxt.setText("0秒");
			periodTxt.setText("00:00~00:00");
			monthTxt.setText("0月~0月");
			msgTxt.setText("");
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.backTxt:
				if(MainActivity.getInstance().mHost.getCurrentTab()	==2) {
					TabModeActivity.getInstance().hideFragMent(4);
				}else{
					TabMainActivity.getInstance().hideFragMent(4);
				}
				break;
			case R.id.editeTxt:
				save();
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
	}	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(hidden){
			if(MainActivity.getInstance().mHost.getCurrentTab()	==2) {
				TabModeActivity.getInstance().modeListFragment.initAdapter(modeId);
			}else{
				TabMainActivity.getInstance().modeListFragment.initAdapter(modeId);
			}
            onDialogDis();
		}
	}
	public void showMsg(){
		msgTxt.setText(actionTxt.getText().toString()+functionTxt.getText().toString()+timeTxt.getText().toString()+"#"+delayTxt.getText().toString()+"("+periodTxt.getText().toString()+")["+monthTxt.getText().toString()+"]");
	}
	public void save() {
		ModeList modeList = new ModeList();
		modeList.setId(Id);
		modeList.setModeAction(actionTxt.getText().toString());
		modeList.setModeFunction(functionTxt.getText().toString());
		modeList.setModeTime(timeTxt.getText().toString());
		modeList.setModeDelayed(delayTxt.getText().toString());
		modeList.setModePeriod(periodTxt.getText().toString());
		modeList.setDeviceId(DeviceId);
		String[] monthStr=monthTxt.getText().toString().replace("月","").split("~");
		modeList.setBeginMonth(monthStr[0]);
		modeList.setEndMonth(monthStr[1]);
		modeList.setSeqencing(ModeListFactory.getInstance().getAll().size() + 1);
		modeList.setModeId(modeId);
		if(Id==0)
		{
			if (MyApplication.getInstance().getWidgetDataBase().insertModeList(modeList) > 0) {
				ToastUtils.ShowSuccess(activity,"添加成功!",Toast.LENGTH_SHORT,true);
				ModeListFactory.getInstance().clearList();
			}else{
				ToastUtils.ShowError(activity,"插入失败!",Toast.LENGTH_SHORT,true);

			}
		}else{
			if (MyApplication.getInstance().getWidgetDataBase().updateModeList(modeList) > 0) {
				ToastUtils.ShowSuccess(activity,"修改成功!",Toast.LENGTH_SHORT,true);
				ModeListFactory.getInstance().clearList();
			}else{
				ToastUtils.ShowError(activity,"修改失败!",Toast.LENGTH_SHORT,true);

			}
		}
	}
	public void onDialogDis(){
		if(actionViewWindow!=null&&actionViewWindow.isShow()){
			actionViewWindow.dismiss();
		}
        if(timeViewWindow!=null&&timeViewWindow.isShow()){
            timeViewWindow.dismiss();
        }
        if(twoPopupWindow!=null&&twoPopupWindow.isShow()){
            twoPopupWindow.dismiss();
        }
    }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}
}
