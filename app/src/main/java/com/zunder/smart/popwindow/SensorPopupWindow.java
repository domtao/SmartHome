package com.zunder.smart.popwindow;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.roll.WheelView;
import com.zunder.smart.utils.ListNumBer;
import com.zunder.smart.roll.WheelView;
import com.zunder.smart.utils.ListNumBer;

import java.util.ArrayList;
import java.util.List;

public class SensorPopupWindow implements OnClickListener {

	private Activity activity;
	private PopupWindow popupWindow;
	View pupView;
	private int arceIndex=0;
	private int sensorIndex=0;
	private List<String> arceList=new ArrayList<String>();
	private List<String> sensorList=new ArrayList<String>();
	public SensorPopupWindow(Activity activity, String name) {
		super();
		this.activity=activity;
		init(name);
	}
	private OnOCListener onOCListene=null;
	public boolean isShow() {
		return popupWindow.isShowing();
	}

	private void init(String name) {
		// TODO Auto-generated method stub
		pupView = ((Activity) activity).getLayoutInflater().inflate(
				R.layout.popwindow_sensor_layout, null);
		TextView save = (TextView) pupView.findViewById(R.id.save);
		TextView title = (TextView) pupView.findViewById(R.id.titleTxt);
		TextView backTxt = (TextView) pupView.findViewById(R.id.backTxt);
		backTxt.setOnClickListener(this);
		title.setText(name);
		TextView showTxt = (TextView) pupView.findViewById(R.id.showTxt);
		//区域
		arceList= ListNumBer.getArce();
		WheelView arceWheel = (WheelView) pupView
				.findViewById(R.id.arceWheel);
		arceWheel.setOffset(2);
		arceWheel.setItems(arceList);
		arceWheel.setSeletion(0);
		arceWheel
				.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
					@Override
					public void onSelected(int selectedIndex, String item) {
						arceIndex=selectedIndex-2;
					}
				});
//传感器
		sensorList=ListNumBer.getSensor();
		WheelView sensorWheel = (WheelView) pupView
				.findViewById(R.id.sensorWheel);
		sensorWheel.setOffset(2);
		sensorWheel.setItems(sensorList);
		sensorWheel.setSeletion(0);
		sensorWheel
				.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
					@Override
					public void onSelected(int selectedIndex, String item) {
						sensorIndex=selectedIndex-2;
					}
				});
		save.setOnClickListener(this);
		popupWindow = new PopupWindow(pupView,
				LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
		popupWindow.setOutsideTouchable(true);
	}

	public void show() {
		popupWindow.showAtLocation(pupView, Gravity.BOTTOM, 0, 0);
	}

	public void dismiss() {
		popupWindow.dismiss();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.save:
			if(onOCListene!=null){
				onOCListene.onResult(arceList.get(arceIndex)+sensorList.get(sensorIndex));
				dismiss();
			}
			break;
		case R.id.backTxt:
			dismiss();
			break;
		default:
			break;
		}
	}

	public void setOnOCListene(OnOCListener onOCListene) {
		this.onOCListene = onOCListene;
	}

	public interface OnOCListener {
		public void onResult(String result);
	}

}
