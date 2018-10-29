package com.zunder.smart.activity.centercontrol.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.iflytek.cloud.thirdparty.B;
import com.zunder.base.RMSBaseView;
import com.zunder.base.menu.RMSTabView;
import com.zunder.cusbutton.RMCCustomButton;
import com.zunder.cusbutton.RMSCustomButton;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.centercontrol.CenterControlActivity;
import com.zunder.smart.activity.centercontrol.edite.TouchPanelActivity;
import com.zunder.smart.dao.impl.factory.RmsTabFactory;
import com.zunder.smart.dialog.EditTxtAlert;
import com.zunder.smart.model.RootCenter;
import com.zunder.smart.setting.RootCenterUtils;
import com.zunder.smart.tools.AppTools;

public class AddRMSCusbuttonDialog extends Dialog implements View.OnClickListener {

	Activity context;
	private TextView titleTxt;
	private EditText buttonName;
	private EditText buttonWidth;
	private EditText buttonHeight;
	private EditText buttonSize;
	private EditText buttonID;
	private Button cancleBt;
	private Button sureBt;
	private int X=0;
	private int Y=0;
	RMSBaseView rmsBaseView;

	public AddRMSCusbuttonDialog(Activity context,RMSBaseView rmsBaseView) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.rmsBaseView=rmsBaseView;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_rms_button);
		init();
	}
	protected void init() {
		titleTxt = (TextView) findViewById(R.id.titleTxt);
		buttonName = (EditText) findViewById(R.id.buttonName);
		buttonWidth = (EditText) findViewById(R.id.buttonWidth);
		buttonHeight = (EditText) findViewById(R.id.buttonHeight);
		buttonSize = (EditText) findViewById(R.id.buttonSize);
		buttonID = (EditText) findViewById(R.id.buttonID);
		cancleBt = (Button) findViewById(R.id.cancle_bt);
		sureBt = (Button) findViewById(R.id.sure_bt);
		cancleBt.setOnClickListener(this);
		sureBt.setOnClickListener(this);
		if(rmsBaseView!=null){
		    RMSCustomButton rmsCustomButton=(RMSCustomButton)rmsBaseView;
            titleTxt.setText("编辑按钮");
            buttonName.setText(rmsCustomButton.getText());
            buttonWidth .setText(rmsCustomButton.getWidth()+"");
            buttonHeight.setText(rmsCustomButton.getHeight()+"");
            buttonSize.setText(rmsCustomButton.getBtnSize()+"");
            buttonID .setText(rmsCustomButton.getId()+"");
            X=(int)rmsCustomButton.getX();
            Y=(int)rmsCustomButton.getY();
        }else{
			buttonID .setText((RmsTabFactory.getInstance().getMax()+1)+"");
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.cancle_bt:
				dismiss();
				break;
			case R.id.sure_bt:

			if(TextUtils.isEmpty(buttonHeight.getText())) {
				buttonHeight.setHint(R.string.text_is_null);
				return;
			}
			if(TextUtils.isEmpty(buttonWidth.getText()))
			{
				buttonWidth.setHint(R.string.text_is_null);
				return;
			}
			if(TextUtils.isEmpty(buttonSize.getText())) {
				buttonSize.setHint(R.string.text_is_null);
				return;
			}
			RMSTabView rmsTabView= TouchPanelActivity.getInstance().getCurrentTabView();
			if(rmsTabView!=null) {


				if(rmsBaseView!=null){
					rmsBaseView.setBtnName(buttonName.getText().toString());
					int H = Integer.parseInt(buttonHeight.getText().toString());
					int W = Integer.parseInt(buttonWidth.getText().toString());
					rmsBaseView.setBtnWidth(W);
					rmsBaseView.setBtnHeight(H);
					rmsBaseView.setBtnX(X);
					rmsBaseView.setBtnY(Y);
					rmsBaseView.setBtnSize(Integer.parseInt(buttonSize.getText().toString()));
					rmsBaseView.setBtnColor("000000");
					if(onSureListener!=null){
						onSureListener.onEditeSure(rmsBaseView);
					}
				}else{
					RMSCustomButton rmcBean = new RMSCustomButton(MyApplication.getInstance());

					rmcBean.setId(Integer.parseInt(buttonID.getText().toString()));
					rmcBean.setBtnName(buttonName.getText().toString());
					rmcBean.setBtnType(rmsTabView.getBtnType());
					rmcBean.setBackgroundFromProperties("");
					int H = Integer.parseInt(buttonHeight.getText().toString());
					int W = Integer.parseInt(buttonWidth.getText().toString());
					rmcBean.setBtnWidth(W);
					rmcBean.setBtnHeight(H);
					rmcBean.setBtnX(X);
					rmcBean.setBtnY(Y);
					rmcBean.setId(Integer.parseInt(buttonID.getText().toString()));
					rmcBean.setBtnSize(Integer.parseInt(buttonSize.getText().toString()));
					rmcBean.setRoomId(rmsTabView.getId());
					rmcBean.setBtnColor("000000");
					rmcBean.initParams();
					if(onSureListener!=null){
						onSureListener.onSure(rmcBean);
					}
				}
			}
				break;
			default:
				break;
		}

	}
	public interface OnSureListener {
		public void onCancle();

		public void onSure(RMSCustomButton rmcBean);
		public void onEditeSure(RMSBaseView rmcBean);
	}

	private OnSureListener onSureListener;

	/**
	 * @param onSureListener
	 *            the onSureListener to set
	 */
	public void setOnSureListener(OnSureListener onSureListener) {
		this.onSureListener = onSureListener;
	}


}
