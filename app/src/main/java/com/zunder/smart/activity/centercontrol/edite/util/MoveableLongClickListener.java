package com.zunder.smart.activity.centercontrol.edite.util;

import android.view.View;
import android.view.View.OnLongClickListener;

import com.zunder.base.RMSBaseView;
import com.zunder.smart.activity.centercontrol.edite.TouchPanelActivity;
import com.zunder.smart.activity.centercontrol.popu.ButtonManagePopupWindow;


public class MoveableLongClickListener implements OnLongClickListener {

	@Override
	public boolean onLongClick(View v) {
		try{
		View _view = getBaseView(v);
		ButtonManagePopupWindow bpw =TouchPanelActivity.getInstance().getButtonManagePopupWindow();
		if (!bpw.isShowing()) {
			bpw.setmView(_view);
			bpw.display(_view);
		}
		}catch(Exception e){
			System.out.println(this.getClass().toString()+":"+e);
		}
		return true;
	}

	
	private View getBaseView(View v){
		if(v instanceof RMSBaseView){
			return v;
		}else{
			return getBaseView((View)v.getParent());
		}
	}
}
