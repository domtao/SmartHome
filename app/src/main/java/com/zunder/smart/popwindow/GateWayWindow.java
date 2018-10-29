package com.zunder.smart.popwindow;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

import com.door.Utils.ToastUtils;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.R.id;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.roll.WheelView;
import com.zunder.smart.service.GateWayService;
import com.zunder.smart.setting.ProjectUtils;
import com.zunder.smart.socket.info.ISocketCode;

public class GateWayWindow {
	PopupWindow popModeWindow;
	View view;
	Activity activity;
	GateWay gateWay;
	int seletIndex = 0;

	public GateWayWindow(Activity activity, GateWay gateWay) {
		this.activity = activity;
		this.gateWay = gateWay;
		init();
	}

	private void init() {
		LayoutInflater layoutInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.popwindow_gateway_layout, null);
		popModeWindow = new PopupWindow(view,
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		ImageView save = (ImageView) view.findViewById(id.save);
		TextView title = (TextView) view.findViewById(id.titleTxt);
		TextView back = (TextView) view.findViewById(id.back);
		final TextView codeEdite = (TextView) view.findViewById(id.showTxt);
		title.setText(gateWay.getGatewayName());
		WheelView wheel_gateway = (WheelView) view
				.findViewById(id.wheel_gateway);
		wheel_gateway.setOffset(2);

		wheel_gateway.setItems(Arrays.asList(activity.getResources()
				.getStringArray(R.array.gateWays)));

		wheel_gateway.setSeletion(0);
		wheel_gateway
				.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
					@Override
					public void onSelected(int selectedIndex, String item) {
						seletIndex = selectedIndex - 2;
						codeEdite.setText(item);
					}
				});
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (seletIndex) {
				case 0:
					int result = MyApplication.getInstance()
							.getWidgetDataBase()
							.updateIsCurrent(gateWay.getId(), 1);
					if (result > 0) {
						ProjectUtils.saveRootPath();
						ToastUtils.ShowSuccess(activity,
								activity.getString(R.string.updateSuccess),
								Toast.LENGTH_SHORT,true);
						List<GateWay> list = GateWayService.list;
						for (int i = 0; i < list.size(); i++) {

							GateWay way = list.get(i);
							if (way.getId() == gateWay.getId()) {
								way.setIsCurrent(1);
							} else {
								if (way.getIsCurrent() == 1) {
									String sql = "update t_gateway set IsCurrent=0 where Id="
											+ way.getId();
									sql().execSQL(sql);
									way.setIsCurrent(0);
								}
							}
						}
						ProjectUtils.getRootPath().setRootID(
								gateWay.getGatewayID());
//						activity.changeList();
						if (gateWay.getTypeId() == 2) {
							MainActivity.getInstance().sendCode(ISocketCode.setConnect(gateWay.getGatewayID(),gateWay.getUserName(),
									gateWay.getUserPassWord(),
									gateWay.getIsCurrent()));
						}
						MainActivity.getInstance().setTip(gateWay.getGatewayName()+activity.getString(R.string.line));
					} else {
						ToastUtils.ShowError(activity,
								activity.getString(R.string.updateFail),
								Toast.LENGTH_SHORT,true);
					}
					break;
				case 1:
					String sql = "update t_gateway set IsCurrent=0 where Id="
							+ gateWay.getId();
					sql().execSQL(sql);

					List<GateWay> list = GateWayService.list;
					for (int i = 0; i < list.size(); i++) {

						GateWay way = list.get(i);
						if (way.getId() == gateWay.getId()) {
							way.setIsCurrent(2);
							break;
						}
					}

//					activity.changeList();
					break;
				case 2:
					String sql2 = "update t_gateway set IsCurrent=0 where Id="
							+ gateWay.getId();
					sql().execSQL(sql2);

					List<GateWay> ways = GateWayService.list;
					for (int i = 0; i < ways.size(); i++) {
						GateWay way = ways.get(i);
						if (way.getId() == gateWay.getId()) {
							way.setIsCurrent(0);
							break;
						}
					}
					ToastUtils.ShowSuccess(activity,
							activity.getString(R.string.updateSuccess),
							Toast.LENGTH_SHORT,true);
//					activity.changeList();
					break;

				default:
					break;
				}
				if(alertViewOnCListener!=null){
					alertViewOnCListener.onItem();
				}
				popModeWindow.dismiss();
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popModeWindow.dismiss();
			}
		});

		// popModeWindow.setOutsideTouchable(true);
		popModeWindow.setBackgroundDrawable(new BitmapDrawable());

		popModeWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
		popModeWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
			}
		});

		// ���������¼�
		popModeWindow.setTouchInterceptor(new OnTouchListener() {
			public boolean onTouch(View view, MotionEvent event) {
				return false;
			}
		});

	}
	private AlertViewOnCListener alertViewOnCListener=null;
	public void openPop() {
		popModeWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}

	public boolean isShow() {
		return popModeWindow.isShowing();
	}

	public void diss() {
		popModeWindow.dismiss();
	}

	public IWidgetDAO sql() {
		return MyApplication.getInstance().getWidgetDataBase();
	}
	public void setAlertViewOnCListener(AlertViewOnCListener alertViewOnCListener) {
		this.alertViewOnCListener = alertViewOnCListener;
	}
	public interface AlertViewOnCListener {
		public void onItem();
		public void cancle();
	}
}
