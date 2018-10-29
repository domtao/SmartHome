package com.zunder.smart.activity.centercontrol.edite.util;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.zunder.base.Moveable;
import com.zunder.base.RMCBaseView;
import com.zunder.base.RMSBaseView;
import com.zunder.smart.activity.centercontrol.edite.TouchPanelActivity;

public class MoveableListener implements OnTouchListener {
	public static Boolean moveable = false;
	private int screenWidth, screenHeight;
	private int tabHeight;
	private int t = 0;
	private Moveable m;

	public MoveableListener() {
		super();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		Boolean flag = false;
		try {

			t++;
			if (t == 45) {
				moveable = true;
			}

			if (moveable) {
				TouchPanelActivity.getInstance()
						.hideTouchPopupWindow();
				screenHeight = TouchPanelActivity.getInstance().getCurrentTabView().getLayout()
						.getHeight();
				screenWidth = TouchPanelActivity.getInstance()
						.getCurrentTabView().getLayout().getWidth();
				tabHeight = TouchPanelActivity.getInstance()
						.getTabHostHeight();
				int mCurX = (int) event.getRawX() - v.getWidth() / 2;
				int mCurY = (int) event.getRawY() - v.getHeight() / 2
						- tabHeight;
				int edgeX = screenWidth - v.getWidth();
				int edgeY = screenHeight - v.getHeight();
				if (action == MotionEvent.ACTION_MOVE) {
					if (mCurX < 0)
						mCurX = 0;
					if (mCurY < 0)
						mCurY = 0;
					if (mCurX > edgeX)
						mCurX = edgeX;
					if (mCurY > edgeY)
						mCurY = edgeY;
					m = (Moveable)getBaseView(v);
					m.moveTo(mCurX, mCurY);

				}
				if (action == MotionEvent.ACTION_UP) {
					mCurX = roundOff(mCurX);
					mCurY = roundOff(mCurY);
					if (mCurX < 0)
						mCurX = 0;
					if (mCurY < 0)
						mCurY = 0;
					if (mCurX > edgeX)
						mCurX = edgeX;
					if (mCurY > edgeY)
						mCurY = edgeY;
					m.moveTo(mCurX, mCurY);
				}
				flag = true;
			}
			if (action == MotionEvent.ACTION_UP) {
				moveable = false;
				t = 0;
			}
		} catch (Exception e) {
			System.out.println(this.getClass().toString() + ":" + e);
		}
		return flag;
	}

	private int roundOff(int value) {
		value += 5;
		int remainder = value % 10;
		return value - remainder;
	}
	
	private View getBaseView(View v){
		if(v instanceof RMSBaseView){
			return v;
		}else{
			return getBaseView((View)v.getParent());
		}
	}
}
