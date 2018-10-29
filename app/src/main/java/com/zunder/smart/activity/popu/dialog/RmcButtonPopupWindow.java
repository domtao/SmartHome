package com.zunder.smart.activity.popu.dialog;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.RoomFactory;
import com.zunder.smart.model.Room;
import com.zunder.smart.roll.WheelView;

import java.util.ArrayList;
import java.util.List;

public class RmcButtonPopupWindow implements OnClickListener {

	private Activity activity;
	private PopupWindow popupWindow;
	View pupView;
	private int oneIndex=0;
	private int twoIndex=0;
	private List<String> oneList=new ArrayList<String>();
	private List<String> twoList=new ArrayList<String>();
	WheelView arceWheel,deviceWheel;
	TextView symbol;
	public RmcButtonPopupWindow(Activity activity, String name) {
		super();
		this.activity=activity;
		this.oneList= RoomFactory.getInstance().getRoomName(1);
		if(oneList.size()>0){
			Room room=RoomFactory.getInstance().getRoomByName(oneList.get(0));
			this.twoList= DeviceFactory.getInstance().getRmcDeviceName(room.getId());
		}
		init(name);
	}
	private OnOCListener onOCListene=null;
	public boolean isShow() {
		return popupWindow.isShowing();
	}

	private void init(String name) {
		// TODO Auto-generated method stub
		pupView = ((Activity) activity).getLayoutInflater().inflate(
				R.layout.popwindow_two_layout, null);
		ImageView save = (ImageView) pupView.findViewById(R.id.save);
		TextView title = (TextView) pupView.findViewById(R.id.titleTxt);
		TextView backTxt = (TextView) pupView.findViewById(R.id.backTxt);
		backTxt.setOnClickListener(this);
		title.setText(name);
		TextView showTxt = (TextView) pupView.findViewById(R.id.showTxt);
		symbol=(TextView)pupView.findViewById(R.id.symbol);
		//区域
		 arceWheel = (WheelView) pupView
				.findViewById(R.id.arceWheel);
		arceWheel.setOffset(2);
		arceWheel.setItems(oneList);
		arceWheel.setSeletion(0);
		arceWheel
				.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
					@Override
					public void onSelected(int selectedIndex, String item) {
						oneIndex=selectedIndex-2;

						Room room=RoomFactory.getInstance().getRoomByName(item);
						if(room!=null) {
							twoIndex=0;
							twoList = DeviceFactory.getInstance().getRmcDeviceName(room.getId());
							if(twoList.size()>0) {
								deviceWheel.setVisibility(View.VISIBLE);
								symbol.setVisibility(View.VISIBLE);
								deviceWheel.setItems(twoList);
								deviceWheel.setSeletion(0);
							}else{
								twoIndex=0;
								ToastUtils.ShowError(activity,"该空间没有设备", Toast.LENGTH_SHORT,true);
								deviceWheel.setVisibility(View.GONE);
								symbol.setVisibility(View.GONE);
							}
						}
					}
				});

		//
		deviceWheel = (WheelView) pupView
				.findViewById(R.id.sensorWheel);
		deviceWheel.setOffset(2);
		deviceWheel.setItems(twoList);
		deviceWheel.setSeletion(0);
		deviceWheel
				.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
					@Override
					public void onSelected(int selectedIndex, String item) {
						twoIndex=selectedIndex-2;
					}
				});
		save.setOnClickListener(this);

		if(twoList.size()==0){
			deviceWheel.setVisibility(View.GONE);
			symbol.setVisibility(View.GONE);
		}
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
				if(oneList.size()>0&&twoList.size()>0) {
					onOCListene.onResult(oneIndex, oneList.get(oneIndex), twoIndex, twoList.get(twoIndex));
				}
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
		public void onResult(int oneIndex, String oneData, int twoIndex, String twoData);
	}

}
