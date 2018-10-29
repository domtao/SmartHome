package com.zunder.smart.remote.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.factory.ActionFactory;
import com.zunder.smart.dao.impl.factory.ActionParamFactory;
import com.zunder.smart.dao.impl.factory.RoomFactory;
import com.zunder.smart.dao.impl.factory.FunctionFactory;
import com.zunder.smart.dao.impl.factory.FunctionParamFactory;
import com.zunder.smart.dao.impl.factory.RedInfraFactory;
import com.zunder.smart.model.Device;
import com.zunder.smart.popwindow.listener.PopWindowListener;
import com.zunder.smart.roll.WheelView;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.utils.ListNumBer;

import java.util.ArrayList;
import java.util.List;

public class TimeActionPopupWindow extends Dialog implements OnClickListener {

	private Activity context;

	TextView titleTxt, tipTxt, saveTxt, msgTxt, backTxt;
	ViewFlipper viewFlipper;
	WheelView actionView, functionView, actionParamView, functionParamView,
			timeListView, timeUnit, delayListView, delayUnitView, endHour,
			endSecond, startHour, startSecond,endMoth,startMoth;

	public PopWindowListener listener;
	private int typeId;
	private int deviceId;
	ImageView preImage, nextImage;
	private String deviceName;
	private String actionName = "";
	private String funcctionName = "";
	private String actionParams = "";
	private String functionParams = "";
	private String hourStr = "";
	private String hourUnit = "";
	private String delayStr = "#";
	private String delayNumber = "";
	private String startHourStr = "";
	private String startMinuteStr = "";
	private String endHourStr = "";
	private String endMinuteStr = "";
	private int actionShow = 0;
	private int functionShow = 0;
	private int actionPramShow = 0;
	private int functionParamShow = 0;
	private String deviceType = "";
	private String startMothStr="0";
	private String endMothStr="0";
	/**
	 * @param listener
	 *            the listener to set
	 */
	public void setListener(PopWindowListener listener) {
		this.listener = listener;
	}

	public TimeActionPopupWindow(Activity context, Device device) {
		super(context, R.style.MyDialog);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.popwindow_timming_layout);
		this.context = context;
		if(device!=null)
		this.deviceType = device.getProductsCode();
		this.typeId = device.getDeviceTypeKey();
		this.deviceId = device.getId();
		this.deviceName = device.getDeviceName();
		this.actionShow = device
				.getActionShow();
		this.functionShow = device
				.getFunctionShow();
		this.actionPramShow = device
				.getActionPramShow();
		this.functionParamShow = device
				.getFunctionParamShow();
		init();
	}


	private void init() {
		// TODO Auto-generated method stub

		viewFlipper = (ViewFlipper) findViewById(R.id.viewFliper);
		preImage = (ImageView) findViewById(R.id.imagePre);
		nextImage = (ImageView) findViewById(R.id.imageNext);
		preImage.setOnClickListener(this);
		nextImage.setOnClickListener(this);
		backTxt = (TextView) findViewById(R.id.backTxt);
		titleTxt = (TextView) findViewById(R.id.titleTxt);
		titleTxt.setText(deviceName);
		tipTxt = (TextView) findViewById(R.id.tipTxt);
		msgTxt = (TextView) findViewById(R.id.msgTxt);
		saveTxt = (TextView) findViewById(R.id.save);
		actionView = (WheelView) findViewById(R.id.actionList);
		functionView = (WheelView) findViewById(R.id.functionList);
		actionParamView = (WheelView) findViewById(R.id.actionParam);
		functionParamView = (WheelView) findViewById(R.id.functionParam);
		startMoth= (WheelView) findViewById(R.id.startMoth);
		endMoth= (WheelView) findViewById(R.id.endMoth);
		timeListView = (WheelView) findViewById(R.id.timeList);
		timeUnit = (WheelView) findViewById(R.id.timeUnit);

		delayUnitView = (WheelView) findViewById(R.id.delayUnit);
		delayListView = (WheelView) findViewById(R.id.delayList);

		startHour = (WheelView) findViewById(R.id.start_hour);
		startSecond = (WheelView) findViewById(R.id.start_second);
		endHour = (WheelView) findViewById(R.id.end_hour);
		endSecond = (WheelView) findViewById(R.id.end_second);
		action();
		saveTxt.setOnClickListener(this);
		backTxt.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.save:
			save();
			break;
			case R.id.imagePre:
				if (viewFlipper.getDisplayedChild() == 1) {
					tipTxt.setText(context.getString(R.string.actionParam));
					if (functionView.getVisibility() == View.VISIBLE
							|| actionParamView.getVisibility() == View.VISIBLE) {
						viewFlipper.setDisplayedChild(0);
						preImage.setVisibility(View.GONE);
					} else {
						preImage.setVisibility(View.GONE);
					}
				} else if (viewFlipper.getDisplayedChild() == 2) {
					tipTxt.setText(context.getString(R.string.actionParam));
					if (functionParamView.getVisibility() == View.VISIBLE
							|| actionParamView.getVisibility() == View.VISIBLE) {
						viewFlipper.setDisplayedChild(1);
                        preImage.setVisibility(View.VISIBLE);
                    } else {
                        viewFlipper.setDisplayedChild(0);
                        preImage.setVisibility(View.GONE);
                    }

				} else if (viewFlipper.getDisplayedChild() == 3) {
					tipTxt.setText(context.getString(R.string.actioncycle));
					viewFlipper.setDisplayedChild(2);
				} else if (viewFlipper.getDisplayedChild() == 4) {
					tipTxt.setText(context.getString(R.string.actiondelay));
					viewFlipper.setDisplayedChild(3);
					nextImage.setVisibility(View.VISIBLE);
				}else if (viewFlipper.getDisplayedChild() == 5) {
					tipTxt.setText(context.getString(R.string.actiontime));
					viewFlipper.setDisplayedChild(4);
					nextImage.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.imageNext:
				if (viewFlipper.getDisplayedChild() == 0) {
					viewFlipper.setDisplayedChild(1);
					tipTxt.setText(context.getString(R.string.actionParam));
					if(listActionParam==null&&listFunctionParam==null) {
						params();
					}else{
						if (listActionParam.size() == 0 && listFunctionParam.size() == 0) {
							viewFlipper.setDisplayedChild(2);
							tipTxt.setText(context.getString(R.string.actioncycle));
						}else{
							preImage.setVisibility(View.VISIBLE);
						}
					}	preImage.setVisibility(View.VISIBLE);
				} else if (viewFlipper.getDisplayedChild() == 1) {
					tipTxt.setText(context.getString(R.string.actioncycle));
					if(listUnitTime==null) {
						timeUnit();
					}
					viewFlipper.setDisplayedChild(2);
					preImage.setVisibility(View.VISIBLE);
				} else if (viewFlipper.getDisplayedChild() == 2) {
					tipTxt.setText(context.getString(R.string.actiondelay));
					if(delayList==null) {
						delay();
					}
					preImage.setVisibility(View.VISIBLE);
					viewFlipper.setDisplayedChild(3);
				} else if (viewFlipper.getDisplayedChild() == 3) {
//				time();
					tipTxt.setText(context.getString(R.string.actiontime));
					if(timeList==null){
						time();
					}
					preImage.setVisibility(View.VISIBLE);
					viewFlipper.setDisplayedChild(4);
				}
				else if (viewFlipper.getDisplayedChild() == 4) {
					tipTxt.setText(context.getString(R.string.s_e));
					if(mouthList==null) {
						moth();
					}
					viewFlipper.setDisplayedChild(5);
					nextImage.setVisibility(View.GONE);
					preImage.setVisibility(View.VISIBLE);
				}
				break;
		case R.id.backTxt:

			dismiss();
			break;
		default:
			break;
		}
	}
	public void save() {
		if (!startHourStr.equals("")) {

			if (startMinuteStr.equals("") || endHourStr.equals("")
					|| endMinuteStr.equals("")) {
				MainActivity.getInstance().showToast(context.getString(R.string.time_null), 3000);
				return;
			}
		} else if (startHourStr.equals("")) {
			startMinuteStr = "";
			endHourStr = "";
			endMinuteStr = "";
		}
		String delayString = "";
		if (!delayStr.equals("#")) {
			delayString = delayStr;
		}
		String timeString = "";
		if (startHourStr.equals("") || startMinuteStr.equals("")
				|| endHourStr.equals("") || endMinuteStr.equals("")) {
			timeString = "";
		} else {
			timeString = "(" + startHourStr + ":"
					+ startMinuteStr.replace(":", "") + "--"
					+ endHourStr.replace("--", "") + ":"
					+ endMinuteStr.replace(":", "") + ")";
		}
		String result=deviceName + actionName + funcctionName
				+ actionParams + functionParams + hourStr + hourUnit
				+ delayString + timeString;
		SendCMD sendCMD = new SendCMD().getInstance();
		sendCMD.sendCMD(0, result, null);
//		CusDeviceListActivity.list.add(deviceName + actionName + funcctionName
//				+ actionParams + functionParams + hourStr + hourUnit
//				+ delayString + timeString);
		if(onSureListener!=null){
			onSureListener.onSure(result);
		}
	}
	public IWidgetDAO sql() {
		return MyApplication.getInstance().getWidgetDataBase();
	}
	List<String> listAction=null;
	List<String> listFunction=null;
	List<String> listActionParam=null;
	List<String> listFunctionParam=null;
	List<String> listUnit=null;
	List<String> listUnitTime=null;
	List<String> delayList=null;
	List<String> mouthList=null;
	List<String> timeList=null;

	public void action() {
		if (typeId == 14) {
			preImage.setVisibility(View.GONE);
			nextImage.setVisibility(View.GONE);
		} else {
		}
		listAction = ActionFactory.getAction(typeId, actionShow);
		if (typeId == 14) {
			listAction = RoomFactory.getInstance().getRoomName(1);
		} else if (typeId == 8 && deviceType.equals("08")) {
			listAction = ActionFactory.getSequential();
		}
		if (listAction.size() > 0) {
			listAction.add(0, "");
			actionView.setItems(listAction);
			actionView.setSeletion(0);
		} else {
			actionView.setVisibility(View.GONE);
		}
		actionView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

			@Override
			public void onSelected(int selectedIndex, String item) {
				actionName = item;
				if (startHourStr.equals("") || startMinuteStr.equals("")
						|| endHourStr.equals("") || endMinuteStr.equals("")) {
					msgTxt.setText(actionName + funcctionName + actionParams
							+ functionParams + hourStr + hourUnit + delayStr);
				}
				else{
					msgTxt.setText(actionName + funcctionName + actionParams
							+ functionParams + hourStr + hourUnit + delayStr + "("
							+ startHourStr + ":" + startMinuteStr + "--"
							+ endHourStr + ":" + endMinuteStr + ")");
				}
			}
		});

	listFunction = FunctionFactory.getFunction(typeId,
				functionShow);
		if (typeId == 14) {
			listFunction = ActionFactory.getAction(typeId, actionShow);
		} else if (typeId == 8 && deviceType.equals("08")) {
			listFunction = FunctionFactory.getSequential();
		}else if (deviceType.equals("05")) {
			listFunction= RedInfraFactory.getInstance().getInfraNames(deviceId);
		}
		if (listFunction.size() > 0) {
			listFunction.add(0, "");
			functionView.setItems(listFunction);
			functionView.setSeletion(0);
		} else {
			functionView.setVisibility(View.GONE);
		}

		functionView
				.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

					@Override
					public void onSelected(int selectedIndex, String item) {
						funcctionName = item;
						if (startHourStr.equals("") || startMinuteStr.equals("")
								|| endHourStr.equals("") || endMinuteStr.equals("")) {
							msgTxt.setText(actionName + funcctionName + actionParams
									+ functionParams + hourStr + hourUnit + delayStr);
						}
						else{
							msgTxt.setText(actionName + funcctionName + actionParams
									+ functionParams + hourStr + hourUnit + delayStr + "("
									+ startHourStr + ":" + startMinuteStr + "--"
									+ endHourStr + ":" + endMinuteStr + ")");
						}
					}
				});

		if (listAction.size() == 0 && listFunction.size() == 0) {
			viewFlipper.setDisplayedChild(1);
			params();
		}
	}

	public void params() {
		listActionParam = ActionParamFactory.getActionParam(
				typeId, actionPramShow);
		if (listActionParam.size() > 0) {
			listActionParam.add(0, "");
			actionParamView.setItems(listActionParam);
			actionParamView.setSeletion(0);
		} else {
			actionParamView.setVisibility(View.GONE);
		}
		actionParamView
				.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

					@Override
					public void onSelected(int selectedIndex, String item) {
						actionParams = item;
						if (startHourStr.equals("") || startMinuteStr.equals("")
								|| endHourStr.equals("") || endMinuteStr.equals("")) {
							msgTxt.setText(actionName + funcctionName + actionParams
									+ functionParams + hourStr + hourUnit + delayStr);
						}
						else{
							msgTxt.setText(actionName + funcctionName + actionParams
									+ functionParams + hourStr + hourUnit + delayStr + "("
									+ startHourStr + ":" + startMinuteStr + "--"
									+ endHourStr + ":" + endMinuteStr + ")");
						}
					}
				});
		listFunctionParam = FunctionParamFactory.getFunctionParam(
				typeId, functionParamShow);
		if (listFunctionParam.size() > 0) {
			listFunctionParam.add(0, "");
			functionParamView.setItems(listFunctionParam);
			functionParamView.setSeletion(0);
		} else {
			functionParamView.setVisibility(View.GONE);
		}

		functionParamView
				.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

					@Override
					public void onSelected(int selectedIndex, String item) {
						functionParams = item;
						if (startHourStr.equals("") || startMinuteStr.equals("")
								|| endHourStr.equals("") || endMinuteStr.equals("")) {
							msgTxt.setText(actionName + funcctionName + actionParams
									+ functionParams + hourStr + hourUnit + delayStr);
						}
						else{
							msgTxt.setText(actionName + funcctionName + actionParams
									+ functionParams + hourStr + hourUnit + delayStr + "("
									+ startHourStr + ":" + startMinuteStr + "--"
									+ endHourStr + ":" + endMinuteStr + ")");
						}
					}
				});
		if (listActionParam.size() == 0 && listFunctionParam.size() == 0) {
			viewFlipper.setDisplayedChild(2);
			tipTxt.setText("动作周期");
			timeUnit();
		}else{
		preImage.setVisibility(View.VISIBLE);
	}
	}
	public void timeUnit() {
		preImage.setVisibility(View.VISIBLE);
		listUnitTime = FunctionParamFactory.getNormalList();
		listUnitTime.add(0, "");
		timeListView.setItems(listUnitTime);
		timeListView.setSeletion(0);

		timeListView
				.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

					@Override
					public void onSelected(int selectedIndex, String item) {
						hourStr = item;
						if (startHourStr.equals("") || startMinuteStr.equals("")
								|| endHourStr.equals("") || endMinuteStr.equals("")) {
							msgTxt.setText(actionName + funcctionName + actionParams
									+ functionParams + hourStr + hourUnit + delayStr);
						}
						else{
							msgTxt.setText(actionName + funcctionName + actionParams
									+ functionParams + hourStr + hourUnit + delayStr + "("
									+ startHourStr + ":" + startMinuteStr + "--"
									+ endHourStr + ":" + endMinuteStr + ")");
						}
					}
				});

		listUnit= FunctionParamFactory.getTimeUnit();
		listUnit.add(0, "");
		timeUnit.setItems(listUnit);
		timeUnit.setSeletion(0);

		timeUnit.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

			@Override
			public void onSelected(int selectedIndex, String item) {
				List<String> listTime = FunctionParamFactory.getNormalList();
				if(item.equals("")){
					listTime.add(0, "");
					timeListView.setItems(listTime);
				}else{
					listTime = FunctionParamFactory.getTimeList();
					listTime.add(0, "");
					timeListView.setItems(listTime);
				}
				if(hourStr.equals("")){
					timeListView.setSeletion(0);
				}else {
					if(listTime.size()-2<Integer.parseInt(hourStr)){
						hourStr="0";
						timeListView.setSeletion(1);
					}else {
						timeListView.setSeletion(Integer.parseInt(hourStr) + 1);
					}
				}
				hourUnit = item;
				if (startHourStr.equals("") || startMinuteStr.equals("")
						|| endHourStr.equals("") || endMinuteStr.equals("")) {
					msgTxt.setText(actionName + funcctionName + actionParams
							+ functionParams + hourStr + hourUnit + delayStr);
				}
				else{
					msgTxt.setText(actionName + funcctionName + actionParams
							+ functionParams + hourStr + hourUnit + delayStr + "("
							+ startHourStr + ":" + startMinuteStr + "--"
							+ endHourStr + ":" + endMinuteStr + ")");
				}
			}
		});
	}
	private OnSureListener onSureListener;
	/**
	 * @param onSureListener
	 *           the onSureListener to set
	 */
	public void setOnSureListener(OnSureListener onSureListener) {
		this.onSureListener = onSureListener;
	}
	public void delay() {
		List<String> delayUnit = new ArrayList<String>();
		delayUnit
				.add(MyApplication.getInstance().getString(R.string.delayTime));
		delayUnitView.setItems(delayUnit);
		delayUnitView.setSeletion(0);

		delayList = FunctionParamFactory.getTimeList();
		delayList.add(0, "");
		delayListView.setItems(delayList);
		delayListView.setSeletion(0);

		delayListView
				.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

					@Override
					public void onSelected(int selectedIndex, String item) {
						if(item.equals("")){
							delayStr="";
						}else {
							delayStr = "#" + item;
						}
						if (startHourStr.equals("") || startMinuteStr.equals("")
								|| endHourStr.equals("") || endMinuteStr.equals("")) {
							msgTxt.setText(actionName + funcctionName + actionParams
									+ functionParams + hourStr + hourUnit + delayStr);
						}
						else{
							msgTxt.setText(actionName + funcctionName + actionParams
									+ functionParams + hourStr + hourUnit + delayStr + "("
									+ startHourStr + ":" + startMinuteStr + "--"
									+ endHourStr + ":" + endMinuteStr + ")");
						}
					}
				});
	}
	public void moth() {
		mouthList = FunctionParamFactory.getMoth();
		mouthList.add(0, "");
		startMoth.setItems(mouthList);
		startMoth.setSeletion(0);

		startMoth
				.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

					@Override
					public void onSelected(int selectedIndex, String item) {
						if(!item.equals("")){
							startMothStr=item;
						}else {
							startMothStr="0";
						}
					}
				});

		List<String> endList = FunctionParamFactory.getMoth();
		endList.add(0, "");
		endMoth.setItems(endList);
		endMoth.setSeletion(0);

		endMoth.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

			@Override
			public void onSelected(int selectedIndex, String item) {
				if(!item.equals("")){
					endMothStr=item;
				}else{
					endMothStr="0";
				}
			}
		});
	}
	public void time() {
		timeList= ListNumBer.getHour();
		startHour.setOffset(2);
		startHour.setItems(timeList);
		startHour.setSeletion(0);

		startSecond.setOffset(2);
		startSecond.setItems(ListNumBer.getMinit());
		startSecond.setSeletion(0);

		startHour.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

			@Override
			public void onSelected(int selectedIndex, String item) {
				startHourStr = item;
				if (startHourStr.equals("") || startMinuteStr.equals("")
						|| endHourStr.equals("") || endMinuteStr.equals("")) {
					msgTxt.setText(actionName + funcctionName + actionParams
							+ functionParams + hourStr + hourUnit + delayStr);
				}
				else{
					msgTxt.setText(actionName + funcctionName + actionParams
							+ functionParams + hourStr + hourUnit + delayStr + "("
							+ startHourStr + ":" + startMinuteStr + "--"
							+ endHourStr + ":" + endMinuteStr + ")");
				}

			}
		});

		startSecond.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				startMinuteStr = item;
				if (startHourStr.equals("") || startMinuteStr.equals("")
						|| endHourStr.equals("") || endMinuteStr.equals("")) {
					msgTxt.setText(actionName + funcctionName + actionParams
							+ functionParams + hourStr + hourUnit + delayStr);
				}
				else{
					msgTxt.setText(actionName + funcctionName + actionParams
							+ functionParams + hourStr + hourUnit + delayStr + "("
							+ startHourStr + ":" + startMinuteStr + "--"
							+ endHourStr + ":" + endMinuteStr + ")");
				}
			}
		});

		endHour.setOffset(2);
		endHour.setItems(ListNumBer.getHour());
		endHour.setSeletion(0);
		endSecond.setOffset(2);
		endSecond.setItems(ListNumBer.getMinit());
		endSecond.setSeletion(0);

		endHour.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

			@Override
			public void onSelected(int selectedIndex, String item) {

				endHourStr = item;
				if (startHourStr.equals("") || startMinuteStr.equals("")
						|| endHourStr.equals("") || endMinuteStr.equals("")) {
					msgTxt.setText(actionName + funcctionName + actionParams
							+ functionParams + hourStr + hourUnit + delayStr);
				}
				else{
					msgTxt.setText(actionName + funcctionName + actionParams
							+ functionParams + hourStr + hourUnit + delayStr + "("
							+ startHourStr + ":" + startMinuteStr + "--"
							+ endHourStr + ":" + endMinuteStr + ")");
				}
			}
		});
		endSecond.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex, String item) {
				endMinuteStr = item;
				if (startHourStr.equals("") || startMinuteStr.equals("")
						|| endHourStr.equals("") || endMinuteStr.equals("")) {
					msgTxt.setText(actionName + funcctionName + actionParams
							+ functionParams + hourStr + hourUnit + delayStr);
				}
				else{
					msgTxt.setText(actionName + funcctionName + actionParams
							+ functionParams + hourStr + hourUnit + delayStr + "("
							+ startHourStr + ":" + startMinuteStr + "--"
							+ endHourStr + ":" + endMinuteStr + ")");
				}

			}
		});
	}
	public interface OnSureListener {
		public void onCancle();

		public void onSure(String cmd);
	}
}
