package com.zunder.smart.activity.centercontrol.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import com.zunder.smart.R;
import com.zunder.smart.activity.centercontrol.CenterControlActivity;
import com.zunder.smart.json.Constants;
import com.zunder.smart.model.RootCenter;
import com.zunder.smart.setting.RootCenterUtils;
import com.zunder.smart.tools.AppTools;

public class SettingDialog extends Dialog implements View.OnClickListener {

	private RadioGroup screenOrientationRadioGroup;
	private RadioGroup tabPositionRadioGroup;

	private EditText min_et;
	private EditText height_et;

	private EditText paddingEditText;
	Activity context;
	private CheckBox isCustomColor;
	private EditText colorEditText;
	private RootCenter rootCenter;
	private Button cancleBtn, sureBtn;
	public SettingDialog(Activity context) {
		super(context, R.style.MyDialog);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting);
		init();
	}

	protected void init() {
		try {
			rootCenter= RootCenterUtils.getRootCenter();
			cancleBtn = (Button) findViewById(R.id.cancle_bt);
			sureBtn = (Button) findViewById(R.id.sure_bt);
			cancleBtn.setOnClickListener(this);
			sureBtn.setOnClickListener(this);
			min_et=(EditText)findViewById(R.id.setting_min_et);
			height_et=(EditText)findViewById(R.id.tab_height_et);

			paddingEditText=(EditText)findViewById(R.id.padding_et);
			screenOrientationRadioGroup=(RadioGroup)findViewById(R.id.screen_orientation_rg);
			tabPositionRadioGroup=(RadioGroup)findViewById(R.id.tab_position_rg);
			screenOrientationRadioGroup.check(rootCenter.getScreenOrientation()== 0?R.id.landscape_rb:R.id.portrait_rb);
			tabPositionRadioGroup.check(rootCenter.getTabPosition()==0?R.id.up_rb:R.id.bottom_rb);
			paddingEditText.setText(rootCenter.getPadding()+"");


		    min_et.setText(rootCenter.getMinTabCount()+"");
		    height_et.setText(rootCenter.getTabHeight()+"");

		    isCustomColor=(CheckBox)findViewById(R.id.is_custom_color_cb);

		    colorEditText=(EditText)findViewById(R.id.color_et);

		    
		    String colorString=rootCenter.getColorString();
		    colorEditText.setText(colorString);
		    if(!TextUtils.isEmpty(colorString)){
		    	isCustomColor.setChecked(true);
		    }

		} catch (Exception e) {
			// TODO: handle exception
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
				doSure();
				break;

			default:
				break;
		}

	}

	public void doSure() {
		// TODO Auto-generated method stub

		int tabHeight=60;
		int tabMin=4;
		String tabBackgroundString="";
		tabHeight=Integer.parseInt(AppTools.getNumbers(height_et.getText().toString()));
		tabMin=Integer.parseInt(AppTools.getNumbers(min_et.getText().toString()));

		if(tabMin<=0){
			min_et.setText(""); return;
		}
		if(tabHeight<0){
			height_et.setText("");
			return;
		}
		rootCenter.setTabHeight(tabHeight);
		rootCenter.setMinTabCount(tabMin);

		rootCenter.setColorString(isCustomColor.isChecked()?colorEditText.getText().toString():"");

		rootCenter.setScreenOrientation(screenOrientationRadioGroup.getCheckedRadioButtonId()==R.id.landscape_rb?0:1);
		rootCenter.setTabPosition(tabPositionRadioGroup.getCheckedRadioButtonId()==R.id.up_rb?0:1);
		rootCenter.setPadding(Integer.parseInt(paddingEditText.getText().toString()));
		RootCenterUtils.saveRootPath();
		CenterControlActivity.getInstance().refresh();
		dismiss();
	}


}
