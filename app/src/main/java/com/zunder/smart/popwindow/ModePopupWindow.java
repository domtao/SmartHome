package com.zunder.smart.popwindow;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.R;
import com.zunder.smart.dialog.EditTxtAlert;
import com.zunder.smart.popwindow.listener.PopWindowListener;
import com.zunder.smart.roll.WheelView;
import com.zunder.smart.tools.AppTools;
import com.zunder.smart.utils.ListNumBer;

public class ModePopupWindow implements OnClickListener {

	private Activity context;
	private PopupWindow popupWindow;
	ImageView saveTxt;
	TextView backTxt;

	WheelView wheel_mode_code = null;
	View view;
	EditText codeEdite;
	int selection=0;
	public PopWindowListener listener;
ImageView cusCode;

	/**
	 * @param listener the listener to set
	 */
	public void setListener(PopWindowListener listener) {
		this.listener = listener;
	}

	public ModePopupWindow(Activity context,int selection) {
		super();
		this.context = context;
		this.selection=selection;
		init();
	}

	public boolean isShow() {
		return popupWindow.isShowing();
	}

	private void init() {
		// TODO Auto-generated method stub
		view = ((Activity) context).getLayoutInflater().inflate(
				R.layout.popwindow_mode_layout, null);
		wheel_mode_code = (WheelView) view
				.findViewById(R.id.wheel_mode_code);
		wheel_mode_code.setOffset(2);
		wheel_mode_code.setItems(ListNumBer.getModes());


		wheel_mode_code
				.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
					@Override
					public void onSelected(int selectedIndex, String item) {
						if(item.contains("已使用")){
							ToastUtils.ShowError(context,"情景ID已使用完,请选择其它ID",Toast.LENGTH_SHORT,true);
						}
						else {
							selection = selectedIndex - 1;
							codeEdite.setText(AppTools.getNumbers(item));
						}
					}
				});
		codeEdite = (EditText) view
				.findViewById(R.id.codeEdite);
		codeEdite.setText(selection+"");
		wheel_mode_code.setSeletion(selection-1);
		backTxt = (TextView) view.findViewById(R.id.backTxt);
		saveTxt = (ImageView) view.findViewById(R.id.save);
		cusCode= (ImageView) view.findViewById(R.id.cusCode);
		saveTxt.setOnClickListener(this);
		backTxt.setOnClickListener(this);
		cusCode.setOnClickListener(this);
		popupWindow = new PopupWindow(view,
				LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
		popupWindow.setOutsideTouchable(true);

	}

	public void show() {
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}

	public void dismiss() {
		popupWindow.dismiss();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.save:
				if(listener!=null) {
					listener.setParams(codeEdite.getText().toString());
				}
				break;
			case R.id.backTxt:
				dismiss();
				break;
			case R.id.cusCode:
				final EditTxtAlert alert = new EditTxtAlert(context);
				alert.setTitle(android.R.drawable.ic_dialog_info,"情景代码",0);
				alert.setHint("只能输入1--200");
				alert.setEditTextType(InputType.TYPE_CLASS_NUMBER);
				alert.setOnSureListener(new EditTxtAlert.OnSureListener() {

					@Override
					public void onSure(String fileName) {
						// TODO Auto-generated method stub
						if(!AppTools.isCharNum(fileName)){
							ToastUtils.ShowError(context,"只能输入数字", Toast.LENGTH_SHORT,true);
							return;
						}
						selection= Integer.parseInt(fileName);
						if(selection==0||selection>200){
							ToastUtils.ShowError(context,"只能输入数字1--200", Toast.LENGTH_SHORT,true);
							return;
						}
						codeEdite.setText(fileName);
						wheel_mode_code.setSeletion(selection-1);
						alert.dismiss();
					}
					@Override
					public void onCancle() {
						// TODO Auto-generated method stub
					}
				});
				alert.show();
				break;
			default:
				break;
		}
	}
}
