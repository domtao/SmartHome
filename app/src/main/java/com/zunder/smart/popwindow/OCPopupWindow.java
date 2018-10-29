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

public class OCPopupWindow implements OnClickListener {

	private Activity activity;
	private PopupWindow popupWindow;
	View pupView;
	String gateWayName = "";
	public OCPopupWindow(Activity activity, String name) {
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
		gateWayName=activity.getString(R.string.open_2);
		pupView = ((Activity) activity).getLayoutInflater().inflate(
				R.layout.popwindow_camara_layout, null);
		TextView save = (TextView) pupView.findViewById(R.id.save);
		TextView title = (TextView) pupView.findViewById(R.id.titleTxt);
		title.setText(name);
		TextView showTxt = (TextView) pupView.findViewById(R.id.showTxt);
		WheelView wheel_camera = (WheelView) pupView
				.findViewById(R.id.wheel_camera);
		wheel_camera.setOffset(2);
		wheel_camera.setItems(ListNumBer.getSwitch());
		wheel_camera.setSeletion(1);
		wheel_camera
				.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

					@Override
					public void onSelected(int selectedIndex, String item) {
						gateWayName = item;
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
			if(gateWayName.equals("")){
				ToastUtils.ShowError(activity,activity.getString(R.string.select_state), Toast.LENGTH_SHORT,true);
			}else if(gateWayName.equals(activity.getString(R.string.open_2))){
				if(onOCListene!=null){
					onOCListene.onOpen();
				}
				popupWindow.dismiss();
			}else if(gateWayName.equals(activity.getString(R.string.close_1))){
				if(onOCListene!=null){
					onOCListene.onClose();
				}
				popupWindow.dismiss();
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
		public void onOpen();
		public void onClose();
	}

}
